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

package io.github.lamtong.maria.socket.util;

import io.github.lamtong.maria.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

/**
 * {@code WebSocket} 内部使用的 {@code Json Web Token} 工具类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class JwtUtil {

    private JwtUtil() {
    }

    /**
     * 从令牌中获取用户姓名.
     *
     * @param token 令牌
     * @return 用户姓名
     * @throws ExpiredJwtException 令牌已过期
     */
    public static String getUsername(String token) throws ExpiredJwtException {
        Claims body = Jwts.parser()
                .setSigningKey(JwtConstant.TOKEN_SECRET_KEY)
                .parseClaimsJws(token.replace(JwtConstant.TOKEN_PREFIX, JwtConstant.BLANK))
                .getBody();
        return body.getSubject();
    }

}
