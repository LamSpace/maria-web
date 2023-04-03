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
 * {@code Spring Security} 配置相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class SecurityConstant {

    /**
     * 允许访问校验码请求路径
     */
    public static final String AUTH_KAPTCHA = "/auth/kaptcha";

    /**
     * 允许登录请求路径
     */
    public static final String AUTH_LOGIN = "/auth/login";

    /**
     * 允许登出请求路径
     */
    public static final String AUTH_LOGOUT = "/auth/logout";

    /**
     * {@code Swagger} 前端页面访问路径
     */
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";

    /**
     * {@code Swagger} 前端访问资源路径
     */
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";

    /**
     * {@code Swagger} 前端访问路径
     */
    public static final String SWAGGER_UI = "/swagger-ui/**";

    /**
     * {@code Swagger} 前端(V2)访问路径
     */
    public static final String V2 = "/v2/**";

    /**
     * {@code WebJars} 访问路径
     */
    public static final String WEBJARS = "/webjars/**";

    /**
     * {@code Druid} 控制台默认访问路径
     */
    public static final String DRUID = "/druid/**";

    /**
     * {@code Actuator} 默认访问路径
     */
    public static final String ACTUATOR = "/actuator/**";

    private SecurityConstant() {
    }

}
