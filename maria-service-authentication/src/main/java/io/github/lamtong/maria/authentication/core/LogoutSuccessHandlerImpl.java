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
import io.github.lamtong.maria.authentication.util.JwtUtil;
import io.github.lamtong.maria.constant.ContentTypeConstant;
import io.github.lamtong.maria.constant.JwtConstant;
import io.github.lamtong.maria.constant.StringConstant;
import io.github.lamtong.maria.domain.response.Res;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 自定义登出成功处理器实现.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutSuccessHandlerImpl.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public LogoutSuccessHandlerImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) {
        String token = this.extractToken(request);
        String username = JwtUtil.getUsername(token);
        if (logger.isInfoEnabled()) {
            logger.info("User [{}] logout successful.", username);
        }
        this.removeTokenInCache(username);

        response.setContentType(ContentTypeConstant.JSON_VALUE_UTF_8);
        Res ok = Res.ok();
        ok.message("退出登录成功!");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(ok));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error occurred while writing logout success response, cause: {}.", e.getMessage());
            }
        }
    }

    /**
     * 从登出请求中获取令牌信息.
     *
     * @param request 登出请求.
     * @return 令牌信息
     */
    private String extractToken(HttpServletRequest request) {
        return request.getHeader(JwtConstant.TOKEN_HEADER);
    }

    /**
     * 从缓存中删除用户的令牌信息.
     *
     * @param username 用户名
     */
    private void removeTokenInCache(String username) {
        String key = JwtConstant.CACHE_NAME.concat(StringConstant.COLON).concat(username);
        byte[] keyBytes = Base64.getEncoder().encode(key.getBytes(StandardCharsets.UTF_8));
        Boolean delete = this.redisTemplate.delete(new String(keyBytes, StandardCharsets.UTF_8));
        if (logger.isInfoEnabled()) {
            logger.info("Delete token with username [{}] {}.", username, (Boolean.TRUE.equals(delete) ? "success" : "fail"));
        }
    }

}
