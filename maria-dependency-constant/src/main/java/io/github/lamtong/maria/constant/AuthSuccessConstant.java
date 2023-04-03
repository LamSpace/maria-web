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
 * 登录认证成功返回信息相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class AuthSuccessConstant {

    /**
     * 已认证成功用户账号
     */
    public static final String LOGIN_USERNAME = "username";

    /**
     * 已认证成功用户是否为超级管理员
     */
    public static final String LOGIN_ADMIN = "isAdmin";

    /**
     * 已认证成功用户 ID
     */
    public static final String LOGIN_ID = "id";

    /**
     * 已认证成功用户信息
     */
    public static final String LOGIN_USERINFO = "userInfo";

    /**
     * 已认证成功用户菜单
     */
    public static final String LOGIN_MENUS = "menus";

    /**
     * 已认证成功用户权限
     */
    public static final String LOGIN_PERMISSIONS = "perms";

    /**
     * 已认证成功用户信息实体名
     */
    public static final String LOGIN_USER = "user";

    /**
     * 已认证成功后返回的 {@code WebSocket} 地址
     */
    public static final String AUTH_SOCKET_ADDRESS = "socketToken";

    private AuthSuccessConstant() {
    }

}
