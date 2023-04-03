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

package io.github.lamtong.maria.constant;

/**
 * {@code JWT} 相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class JwtConstant {

    /**
     * Token在响应中的头的属性名
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 权限标志分隔符
     */
    public static final String SPLITTER = ",";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 空白字符串
     */
    public static final String BLANK = "";

    /**
     * Token claims属性名
     */
    public static final String TOKEN_CLAIMS = "authorities";

    /**
     * Token issuer属性值
     */
    public static final String TOKEN_ISSUER = "maria-web";

    /**
     * Token加密秘钥
     */
    public static final String TOKEN_SECRET_KEY = "CFJaNdRgUkXn2r5u8xADGKbPeShVmYq3s6v9yBEHMcQfTjWnZr4u7w";

    /**
     * Token过期时间, 默认1小时
     */
    public static final Long TOKEN_EXPIRATION = 60L * 60L * 1000L;

    /**
     * Token 存储在缓存中的哈希名称
     */
    public static final String CACHE_NAME = "maria-token";

    private JwtConstant() {
    }

}
