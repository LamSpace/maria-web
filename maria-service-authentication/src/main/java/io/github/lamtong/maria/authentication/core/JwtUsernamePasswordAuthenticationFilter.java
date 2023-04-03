/*
Copyright 2023 the original author, Lam Tong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.github.lamtong.maria.authentication.core;

import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import io.github.lamtong.maria.authentication.context.KaptchaContext;
import io.github.lamtong.maria.authentication.util.JwtUtil;
import io.github.lamtong.maria.constant.*;
import io.github.lamtong.maria.domain.entity.LogLogin;
import io.github.lamtong.maria.domain.entity.Menu;
import io.github.lamtong.maria.domain.entity.User;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.domain.response.ResCode;
import io.github.lamtong.maria.util.Base64Util;
import io.github.lamtong.maria.util.MenuUtil;
import io.github.lamtong.maria.util.RemoteIpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义用户密码认证过滤器。
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtUsernamePasswordAuthenticationFilter.class);

    private static final ThreadLocal<List<Menu>> MENU_CONTAINER = new ThreadLocal<>();

    private static final ThreadLocal<User> USER_CONTAINER = new ThreadLocal<>();

    private final KaptchaContext kaptchaContext;

    private final RedisTemplate<String, Object> redisTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public JwtUsernamePasswordAuthenticationFilter(KaptchaContext kaptchaContext,
                                                   RedisTemplate<String, Object> redisTemplate,
                                                   KafkaTemplate<String, String> kafkaTemplate) {
        this.kaptchaContext = kaptchaContext;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public static void setUser(User user) {
        USER_CONTAINER.set(user);
    }

    public static void setMenus(List<Menu> menus) {
        MENU_CONTAINER.set(menus);
    }

    /**
     * 尝试进行用户登录信息认证并返回认证结果.
     *
     * @param request  .
     * @param response .
     * @return 认证结果
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String method = request.getMethod();
        String sessionId = request.getSession().getId();
        if (!RequestMethodConstant.POST.equals(method)) {
            throw new AuthenticationServiceException("认证请求方法不支持, 请求方法: " + request.getMethod());
        }
        if (logger.isInfoEnabled()) {
            logger.info("Attempts to authenticate with session: [{}]", sessionId);
        }
        String payload = this.extractPayload(request);
        Map<String, Object> payloadMap = JSON.parseObject(payload).getInnerMap();
        if (!this.preAuthenticate(payloadMap, sessionId)) {
            throw new AuthenticationServiceException("校验码错误, 请重新输入.");
        } else {
            this.kaptchaContext.remove(sessionId);
        }

        String username = this.obtainUsername(payloadMap);
        if (username == null) {
            username = StringConstant.BLANK;
        }
        String password = this.obtainPassword(payloadMap);
        if (password == null) {
            password = StringConstant.BLANK;
        }

        username = username.trim();
        password = password.trim();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        super.setDetails(request, token);
        return super.getAuthenticationManager().authenticate(token);
    }

    /**
     * 认证成功处理流程, 向前端返回已认证用户信息以及对应的菜单和权限信息.
     *
     * @param request    .
     * @param response   .
     * @param chain      .
     * @param authResult 已认证结果
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = JwtUtil.generate(authResult);
        try {
            this.writeToken2Cache(authResult, token);
            this.writeToken2Web(response, token);
            this.logAuthentication(request, true, "");
        } finally {
            USER_CONTAINER.remove();
            MENU_CONTAINER.remove();
        }
    }

    /**
     * 登录失败, 返回登录失败结果以及失败原因.
     *
     * @param request  .
     * @param response .
     * @param failed   .
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setContentType(ContentTypeConstant.JSON_VALUE_UTF_8);
        Res res = Res.error(ResCode.ERROR);
        if (failed instanceof LockedException) {
            res.message("账号被锁定, 请联系管理员.");
        } else if (failed instanceof CredentialsExpiredException) {
            res.message("密码过期, 请联系管理员.");
        } else if (failed instanceof AccountExpiredException) {
            res.message("账号过期, 请联系管理员.");
        } else if (failed instanceof DisabledException) {
            res.message("账号禁用, 请联系管理员.");
        } else if (failed instanceof BadCredentialsException) {
            res.message("账号或密码错误, 请重新输入.");
        } else {
            res.message(failed.getLocalizedMessage());
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(res));
            this.logAuthentication(request, false, res.getMessage());
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while writing unsuccessfulAuthentication response, cause: {}.", e.getMessage());
            }
        } finally {
            USER_CONTAINER.remove();
            MENU_CONTAINER.remove();
        }
    }

    /**
     * 认证成功, 向前端 web 页面返回认证成功相关消息.
     */
    private void writeToken2Web(HttpServletResponse response,
                                String token) {
        response.setHeader(JwtConstant.TOKEN_HEADER, token);
        response.setCharacterEncoding(CharSetConstant.UTF8);
        response.setContentType(ContentTypeConstant.JSON_VALUE_UTF_8);

        Res ok = Res.ok();

        @SuppressWarnings(value = {"AlibabaCollectionInitShouldAssignCapacity"})
        Map<String, Object> loginAuthResult = new HashMap<>();

        @SuppressWarnings(value = {"AlibabaCollectionInitShouldAssignCapacity"})
        Map<String, Object> loginUserInfo = new HashMap<>();
        User user = USER_CONTAINER.get();
        loginUserInfo.put(AuthSuccessConstant.LOGIN_USERNAME, user.getUsername());
        loginUserInfo.put(AuthSuccessConstant.LOGIN_ADMIN, user.getType() == 0);
        loginUserInfo.put(AuthSuccessConstant.LOGIN_ID, user.getId());
        loginAuthResult.put(AuthSuccessConstant.LOGIN_USERINFO, loginUserInfo);

        List<Menu> menus = MENU_CONTAINER.get();
        Menu[] tree = MenuUtil.extractMenuTree(menus, false);
        String[] permissions = MenuUtil.extractPermissions(menus);
        loginAuthResult.put(AuthSuccessConstant.LOGIN_MENUS, tree);
        loginAuthResult.put(AuthSuccessConstant.LOGIN_PERMISSIONS, permissions);

        String encodeToken = Base64Util.encode(token);
        loginAuthResult.put(AuthSuccessConstant.AUTH_SOCKET_ADDRESS, encodeToken);

        ok.data(AuthSuccessConstant.LOGIN_USER, loginAuthResult);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(ok));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while writing successfulAuthentication response, cause: {}.", e.getMessage());
            }
        }
    }

    /**
     * 认证成功, 向缓存写入认证相关信息, 以便各微服务模块鉴权.
     */
    private void writeToken2Cache(Authentication auth,
                                  String token) {
        ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
        String key = JwtConstant.CACHE_NAME.concat(StringConstant.COLON).concat(auth.getName());
        byte[] keyBytes = Base64.getEncoder().encode(key.getBytes(StandardCharsets.UTF_8));
        byte[] valueBytes = Base64.getEncoder().encode(token.getBytes(StandardCharsets.UTF_8));
        operations.set(new String(keyBytes, StandardCharsets.UTF_8), new String(valueBytes, StandardCharsets.UTF_8),
                JwtConstant.TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    /**
     * 从请求体中获取登录提交的信息.
     *
     * @param request 请求实例
     * @return 登录提交信息（字符串形式）
     */
    private String extractPayload(HttpServletRequest request) {
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder payload = new StringBuilder();
        try (ServletInputStream is = request.getInputStream()) {
            while ((len = is.read(bytes)) != -1) {
                payload.append(new String(bytes, 0, len));
            }
        } catch (IOException e) {
            throw new AuthenticationServiceException("从请求体中提取认证信息时发生异常, 异常原因: " + e.getMessage());
        }
        return payload.toString();
    }

    /**
     * 正式校验用户名和密码之前先验证用户录入的校验码是否正确.
     *
     * @param payloadMap 登录请求提交的键值对
     * @param sessionId  会话ID
     * @return true: 验证码正确; 否则返回 false.
     */
    private boolean preAuthenticate(Map<String, Object> payloadMap,
                                    String sessionId) {
        String kaptcha = String.valueOf(payloadMap.get(AuthRequestConstant.PAYLOAD_KAPTCHA));
        return this.kaptchaContext.contains(sessionId, kaptcha);
    }

    /**
     * 从登录提交的信息中获取登录用户名.
     *
     * @param payloadMap 登录提交的信息
     * @return 登录用户名
     */
    private String obtainUsername(Map<String, Object> payloadMap) {
        return String.valueOf(payloadMap.get(AuthRequestConstant.PAYLOAD_USERNAME));
    }

    /**
     * 从登录提交的信息中获取登录密码.
     *
     * @param payloadMap 登录提交的信息
     * @return 登录密码
     */
    private String obtainPassword(Map<String, Object> payloadMap) {
        return String.valueOf(payloadMap.get(AuthRequestConstant.PAYLOAD_PASSWORD));
    }

    /**
     * 记录用户登录认证操作结果, 即用户登录认证是否成功.
     *
     * @param request   用户登录认证请求实例
     * @param success   登录认证是否成功
     * @param exception 登录认证失败原因
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    private void logAuthentication(HttpServletRequest request,
                                   boolean success,
                                   String exception) {
        LogLogin logLogin = new LogLogin();

        String username = USER_CONTAINER.get().getUsername();
        logLogin.setUsername(username);
        logLogin.setLoginTime(new Date());
        logLogin.setSuccess(success ? 1 : 0);
        logLogin.setException(exception);

        // 获取远程用户登录认证时 IP 地址
        String ip = RemoteIpUtil.getIp(request);
        logLogin.setIp(ip);

        // 获取远程用户登录认证时浏览器相关信息
        String agent = request.getHeader(LogOpConstant.REQUEST_HEADER_AGENT);
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        Browser browser = userAgent.getBrowser();

        logLogin.setBrowserName(browser.getName());
        logLogin.setBrowserType(browser.getBrowserType().getName());
        logLogin.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
        logLogin.setBrowserManufacturer(browser.getManufacturer().getName());

        // 获取远程用户登录认证时操作系统相关信息
        OperatingSystem os = userAgent.getOperatingSystem();
        logLogin.setOsName(os.getName());
        logLogin.setOsManufacturer(os.getManufacturer().getName());

        if (logger.isInfoEnabled()) {
            logger.info("Record login log [{}].", logLogin);
        }
        this.kafkaTemplate.send(ServiceLog.KAFKA_TOPIC_LOG_LOGIN, JSON.toJSONString(logLogin));
    }

}
