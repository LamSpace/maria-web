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
 * 模块跨域配置相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class CorsConstant {

    /**
     * 跨域匹配的路径
     */
    public static final String MAPPING = "/**";

    /**
     * 跨域站点模式
     */
    public static final String ORIGIN_PATTERN = "*";

    /**
     * 跨域可接收的请求头
     */
    public static final String HEADERS = "Authorization";

    /**
     * 可跨域方法（GET）
     */
    public static final String ALLOWED_METHOD_GET = "GET";

    /**
     * 可跨域方法（POST）
     */
    public static final String ALLOWED_METHOD_POST = "POST";

    /**
     * 可跨域方法（PUT）
     */
    public static final String ALLOWED_METHOD_PUT = "PUT";

    /**
     * 可跨域方法（DELETE）
     */
    public static final String ALLOWED_METHOD_DELETE = "DELETE";

    /**
     * 跨域是否允许传递参数？
     */
    public static final boolean ALLOWED_CREDENTIALS = true;

    /**
     * 免跨域检查时长
     */
    public static final long MAX_AGE = 3600L;

    private CorsConstant() {
    }

}
