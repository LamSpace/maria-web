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
 * 认证中心微服务模块相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ServiceAuthentication {

    /**
     * 认证中心微服务模块名, 需与 {@code spring.application.name} 保持一致
     */
    public static final String SERVICE_NAME = "maria-web-service-authentication";

    /**
     * 认证微服务认证服务控制器名称
     */
    public static final String CONTROLLER_URL = "auth";

    /**
     * 认证中心微服务模块控制器名称.
     */
    public static final String CONTROLLER_FEIGN_URL = "feignAuth";

    /**
     * 获取校验码请求路径
     */
    public static final String URL_KAPTCHA = "kaptcha";

    /**
     * 校验用户密码请求路径
     */
    public static final String URL_VERIFY_PASSWORD = "verify";

    /**
     * 认证中心微服务模块接收通知退出登录请求路径
     */
    public static final String URL_LOGOUT_NOTICE = "logoutNotice";

    /**
     * 认证中心微服务模块接收通知退出登录完整请求路径
     */
    public static final String URL_LOGOUT_NOTICE_VALUE = "/feignAuth/logoutNotice";

    /**
     * 参数用户 ID
     */
    public static final String PARA_USER_ID = "userId";

    /**
     * 参数用户 ID
     */
    public static final String PARA_USER_ID_VALUE = "用户ID";

    /**
     * 参数用户登录密码
     */
    public static final String PARA_PASSWORD = "password";

    /**
     * 参数用户登录密码
     */
    public static final String PARA_PASSWORD_VALUE = "用户密码";

    /**
     * 参数用户名
     */
    public static final String PARA_USERNAME = "username";

    /**
     * 参数用户名
     */
    public static final String PARA_USERNAME_VALUE = "用户名";

    /**
     * {@code Kaptcha} 上下文在 {@code Redis} 中的键
     */
    public static final String REDIS_KAPTCHA_CONTAINER_KEY = "kaptcha";

    private ServiceAuthentication() {
    }

}
