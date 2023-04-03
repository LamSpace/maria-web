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

package io.github.lamtong.maria.authentication.filter;

import io.github.lamtong.maria.authentication.util.JwtUtil;
import io.github.lamtong.maria.constant.CharSetConstant;
import io.github.lamtong.maria.constant.JwtConstant;
import io.github.lamtong.maria.constant.StringConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * 自定义过滤器, 过滤请求头属性 {@code Authorization} 并提取对应的认证信息, 将认证信息写入 {@code SecurityContextHolder} 上下文
 * 以便后续使用.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class JwtAuthorizedRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizedRequestFilter.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public JwtAuthorizedRequestFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(JwtConstant.TOKEN_HEADER);
        if (StringUtils.hasLength(token)) {
            if (logger.isInfoEnabled()) {
                logger.info("Filters request [{}] with token [{}]", request.getSession().getId(), token);
            }
            // 校验前端传递的令牌是否过期.
            if (JwtUtil.expired(token)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding(CharSetConstant.UTF8);
                response.getWriter().write("用户认证已过期, 请重新登录.");
                return;
            }
            String username = JwtUtil.getUsername(token);

            // 校验前端传递的令牌是否有效, 即令牌是否保存于系统缓存中. 若同一用户在多个地方登录, 则每次登录都会生成不同的令牌.
            // 前次登录的令牌信息已经从缓存中抹除. 即用户的前次登录作废.
            String cachedToken = this.retrieveCachedToken(username);
            if (!cachedToken.equals(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding(CharSetConstant.UTF8);
                response.getWriter().write("用户已在其他地方登录, 当前登录失效.");
                return;
            }

            List<GrantedAuthority> authorities = JwtUtil.getAuthorities(token);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    /**
     * 从缓存中获取用户对应的令牌信息。
     *
     * @param username 用户名
     * @return 用户的令牌信息
     */
    private String retrieveCachedToken(String username) {
        String key = JwtConstant.CACHE_NAME.concat(StringConstant.COLON).concat(username);
        byte[] keyBytes = Base64.getEncoder().encode(key.getBytes(StandardCharsets.UTF_8));
        ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
        String decodeToken = (String) operations.get(new String(keyBytes, StandardCharsets.UTF_8));
        byte[] bytes = Base64.getDecoder().decode(decodeToken);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
