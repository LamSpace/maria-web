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

package io.github.lamtong.maria.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * {@code Base64} 编解码工具类, 默认使用 {@link java.util.Base64} 类实现编解码.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Base64Util {

    /**
     * 默认字符集
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Base64Util() {
    }

    /**
     * 编码指定字符串.
     *
     * @param s 给定字符串
     * @return 编码后字符串
     */
    public static String encode(String s) {
        byte[] encode = Base64.getEncoder().encode(s.getBytes(DEFAULT_CHARSET));
        return new String(encode, DEFAULT_CHARSET);
    }

    /**
     * 解码给定字符串.
     *
     * @param s 给定字符串
     * @return 解码后字符串
     */
    public static String decode(String s) {
        byte[] decode = Base64.getDecoder().decode(s.getBytes(DEFAULT_CHARSET));
        return new String(decode, DEFAULT_CHARSET);
    }

}
