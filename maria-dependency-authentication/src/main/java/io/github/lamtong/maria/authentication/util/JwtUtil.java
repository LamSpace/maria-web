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

package io.github.lamtong.maria.authentication.util;

import io.github.lamtong.maria.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * {@code Json Web Token} 工具类, 用于生成、校验 Token.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class JwtUtil {

    /**
     * 根据用户认证信息生成 Token 令牌.
     *
     * @param auth 认证信息
     * @return 令牌实例
     */
    public static String generate(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        StringBuilder stringBuilder = new StringBuilder();
        for (GrantedAuthority authority : authorities) {
            stringBuilder.append(authority.getAuthority()).append(JwtConstant.SPLITTER);
        }
        String token = Jwts.builder()
                .claim(JwtConstant.TOKEN_CLAIMS, stringBuilder.toString())
                .setSubject(auth.getName())
                .setIssuer(JwtConstant.TOKEN_ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JwtConstant.TOKEN_SECRET_KEY)
                .compact();
        return JwtConstant.TOKEN_PREFIX.concat(token);
    }

    /**
     * 从指定的令牌中提取认证相关信息.
     *
     * @param token 指定令牌
     * @return 认证相关信息
     */
    public static Claims extractBody(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(JwtConstant.TOKEN_SECRET_KEY)
                .parseClaimsJws(token.replace(JwtConstant.TOKEN_PREFIX, JwtConstant.BLANK))
                .getBody();
    }

    /**
     * 检查指定的令牌是否过期.
     *
     * @param token 指定令牌
     * @return true: 令牌已过期; 否则返回 false
     */
    public static boolean expired(String token) {
        try {
            Claims body = extractBody(token);
            Date expiration = body.getExpiration();
            return !new Date().before(expiration);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 从指定的令牌中提取认证用户名.
     *
     * @param token 指定令牌
     * @return 认证用户名
     */
    public static String getUsername(String token) throws ExpiredJwtException {
        Claims body = extractBody(token);
        return body.getSubject();
    }

    /**
     * 从指定的令牌中提取用户的权限信息.
     *
     * @param token 指定令牌
     * @return 用户已认证的权限信息
     */
    public static List<GrantedAuthority> getAuthorities(String token) throws ExpiredJwtException {
        Claims body = extractBody(token);
        String s = String.valueOf(body.get(JwtConstant.TOKEN_CLAIMS));
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] split = s.split(JwtConstant.SPLITTER);
        for (String ss : split) {
            if (StringUtils.hasLength(ss)) {
                authorities.add(new SimpleGrantedAuthority(ss));
            }
        }
        return authorities;
    }

}
