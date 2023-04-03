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
 * 系统管理微服务模块相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ServiceSystem {

    /**
     * 系统管理微服务模块应用名称, 与 {@code spring.application.name} 保持一致.
     */
    public static final String SERVICE_NAME = "maria-web-service-system";

    private ServiceSystem() {
    }

    /**
     * 系统管理微服务用户管理模块相关常量定义.
     *
     * @author Lam Tong
     * @version 0.0.1
     * @since 0.0.1
     */
    public static final class ModuleUser {

        /**
         * 用户微服务控制器名称
         */
        public static final String CONTROLLER_URL = "user";

        /**
         * 用户微服务 OpenFeign 调用服务控制器名称
         */
        public static final String CONTROLLER_FEIGN_URL = "feignUser";

        /**
         * 参数用户名
         */
        public static final String PARA_USERNAME = "username";

        /**
         * 参数用户名
         */
        public static final String PARA_USERNAME_VALUE = "用户名";

        /**
         * 参数用户 ID
         */
        public static final String PARA_USER_ID = "id";

        /**
         * 参数用户 ID
         */
        public static final String PARA_USER_ID_VALUE = "用户ID";

        /**
         * 参数用户信息分页查询当前页码
         */
        public static final String PARA_CURRENT_PAGE = "cur";

        /**
         * 参数用户信息分页查询当前页分页
         */
        public static final String PARA_CURRENT_PAGE_VALUE = "当前分页";

        /**
         * 参数用户信息分页查询条件
         */
        public static final String PARA_PAGE_CONDITION = "parameters";

        /**
         * 参数用户信息分页查询条件
         */
        public static final String PARA_PAGE_CONDITION_VALUE = "分页查询条件";

        /**
         * 参数用户信息分页查询分页大小
         */
        public static final String PARA_PAGE_SIZE = "size";

        /**
         * 参数用户信息分页查询分页大小
         */
        public static final String PARA_PAGE_SIZE_VALUE = "分页大小";

        /**
         * 新增、修改用户信息实体
         */
        public static final String PARA_USER = "user";

        /**
         * 新增、修改用户信息实体
         */
        public static final String PARA_USER_VALUE = "用户信息实体";

        /**
         * 参数用户 ID 集合
         */
        public static final String PARA_USER_IDS = "ids";

        /**
         * 参数用户 ID 集合
         */
        public static final String PARA_USER_IDS_VALUE = "用户ID集合";

        /**
         * 参数配置的角色 ID 集合
         */
        public static final String PARA_UPDATED_ROLE_IDS = "updatedRoleIds";

        /**
         * 参数配置的角色 ID 集合
         */
        public static final String PARA_UPDATED_ROLE_IDS_VALUE = "角色ID集合";

        /**
         * 根据用户名查询用户信息请求子路径
         */
        public static final String URL_FIND_BY_USERNAME = "findByUserName";

        /**
         * 根据用户名查询用户信息完整请求路径
         */
        public static final String URL_FIND_BY_USERNAME_VALUE = "/feignUser/findByUserName";

        /**
         * 根据用户 ID 查找指定用户密码信息请求路径.
         */
        public static final String URL_GET_PASSWORD_BY_USER_ID = "getPasswordByUserId";

        /**
         * 根据用户 ID 查找指定用户密码信息完整请求路径.
         */
        public static final String URL_GET_PASSWORD_BY_USER_ID_VALUE = "/feignUser/getPasswordByUserId";

        /**
         * 条件查询用户信息列表请求路径
         */
        public static final String URL_GET_USER_LIST = "list";

        /**
         * 用户信息 CRUD 请求路径
         */
        public static final String URL_USER = "user";

        /**
         * 用户重置密码请求路径
         */
        public static final String URL_RESET_PASSWORD = "resetPassword";

        /**
         * 获取用户-角色关联关系请求路径
         */
        public static final String URL_USER_ROLE_BINDING = "userRoleBinding";

        /**
         * 配置用户-角色关联关系请求路径
         */
        public static final String URL_CONFIGURE_USER_ROLE_BINDING = "configureUserRoleBinding";

        /**
         * 根据用户名查询对应的角色 ID 请求路径
         */
        public static final String URL_ROLE_ID_BY_USERNAME = "roleIdByUserName";

        /**
         * 根据用户名查询对应的角色 ID 完整请求路径, 由用户微服务提供给角色微服务使用.
         */
        public static final String URL_ROLE_ID_BY_USERNAME_VALUE = "/feignUser/roleIdByUserName";

        /**
         * 新增用户密码默认密码.
         */
        public static final String DEFAULT_PASSWORD = "11111111";

        private ModuleUser() {
        }

    }

    /**
     * 系统管理微服务角色管理模块相关常量定义.
     *
     * @author Lam Tong
     * @version 0.0.1
     * @since 0.0.1
     */
    public static final class ModuleRole {

        /**
         * 角色微服务控制器名称
         */
        public static final String CONTROLLER_URL = "role";

        /**
         * 角色微服务 OpenFeign 调用服务控制器名称
         */
        public static final String CONTROLLER_FEIGN_URL = "feignRole";

        /**
         * 角色 ID
         */
        public static final String PARA_ROLE_ID = "id";

        /**
         * 角色 ID
         */
        public static final String PARA_ROLE_ID_VALUE = "角色ID";

        /**
         * 角色微服务分页查询当前页
         */
        public static final String PARA_CURRENT_PAGE = "cur";

        /**
         * 角色微服务分页查询当前页
         */
        public static final String PARA_CURRENT_PAGE_VALUE = "当前分页";

        /**
         * 角色微服务分页查询分页大小
         */
        public static final String PARA_PAGE_SIZE = "size";

        /**
         * 角色微服务分页查询分页大小
         */
        public static final String PARA_PAGE_SIZE_VALUE = "分页大小";

        /**
         * 角色信息查询条件
         */
        public static final String PARA_PARAMETERS = "parameters";

        /**
         * 角色信息查询条件
         */
        public static final String PARA_PARAMETERS_VALUE = "查询条件";

        /**
         * 新增、修改角色信息的实体类
         */
        public static final String PARA_ROLE = "role";

        /**
         * 新增、修改角色信息的实体类
         */
        public static final String PARA_ROLE_VALUE = "角色信息";

        /**
         * 参数角色 ID 集合
         */
        public static final String PARA_ROLE_IDS = "ids";

        /**
         * 参数角色 ID 集合
         */
        public static final String PARA_ROLE_IDS_VALUE = "角色ID集合";

        /**
         * 更新后的菜单 ID 集合
         */
        public static final String PARA_UPDATED_MENU_IDS = "updatedMenuIds";

        /**
         * 更新后的菜单 ID 集合
         */
        public static final String PARA_UPDATED_MENU_IDS_VALUE = "菜单ID集合";

        /**
         * 角色微服务 CRUD 请求路径
         */
        public static final String URL_ROLE = "role";

        /**
         * 条件查询角色信息列表请求路径
         */
        public static final String URL_GET_ROLE_LIST = "list";

        /**
         * 获取全部可用的角色信息列表请求路径
         */
        public static final String URL_AVAILABLE_ROLES = "availableRoles";

        /**
         * 提供给用户微服务模块, 远程调用获取可用角色信息列表请求路径
         */
        public static final String URL_AVAILABLE_ROLES_VALUE = "/feignRole/availableRoles";

        /**
         * 根据角色 ID 获取对应的菜单 ID 请求路径
         */
        public static final String URL_MENU_IDS_BY_ROLE_IDS = "menuIdsByRoleIds";

        /**
         * 根据角色 ID 获取对应的菜单 ID 完整请求路径
         */
        public static final String URL_MENU_IDS_BY_ROLE_IDS_VALUE = "/feignRole/menuIdsByRoleIds";

        /**
         * 查询角色-菜单关联关系请求路径
         */
        public static final String URL_ROLE_MENU_BINDING = "roleMenuBinding";

        /**
         * 配置角色-菜单关联关系请求路径
         */
        public static final String URL_CONFIGURE_ROLE_MENU_BINDING = "configureRoleMenuBinding";

        private ModuleRole() {
        }

    }

    /**
     * 系统管理微服务菜单管理模块相关常量定义.
     *
     * @author Lam Tong
     * @version 0.0.1
     * @since 0.0.1
     */
    public static final class ModuleMenu {

        /**
         * 菜单微服务控制器名称
         */
        public static final String CONTROLLER_URL = "menu";

        /**
         * 菜单微服务 OpenFeign 调用服务控制器名称
         */
        public static final String CONTROLLER_FEIGN_URL = "feignMenu";

        /**
         * 参数菜单 ID
         */
        public static final String PARA_MENU_ID = "id";

        /**
         * 参数菜单 ID
         */
        public static final String PARA_MENU_ID_VALUE = "菜单ID";

        /**
         * 参数菜单 ID 集合
         */
        public static final String PARA_MENU_IDS = "ids";

        /**
         * 参数菜单 ID 集合
         */
        public static final String PARA_MENU_IDS_VALUE = "菜单ID";

        /**
         * 新增、修改菜单信息记录参数
         */
        public static final String PARA_MENU = "menu";

        /**
         * 新增、修改菜单信息记录参数
         */
        public static final String PARA_MENU_VALUE = "菜单实体";

        /**
         * 查询所有菜单记录请求子路径
         */
        public static final String URL_GET_ALL_MENUS = "getAllMenus";

        /**
         * 查询所有菜单记录请求完整路径
         */
        public static final String URL_GET_ALL_MENUS_VALUE = "/feignMenu/getAllMenus";

        /**
         * 获取菜单结构树请求路径
         */
        public static final String URL_MENU_TREE = "menuTree";

        /**
         * 菜单信息记录 CRUD 请求路径
         */
        public static final String URL_MENU = "menu";

        /**
         * 对外接口, 查询所有可用菜单记录信息请求路径
         */
        public static final String URL_AVAILABLE_MENUS = "availableMenus";

        /**
         * 提供给角色微服务模块, 查询可用菜单记录信息请求路径
         */
        public static final String URL_AVAILABLE_MENUS_VALUE = "/feignMenu/availableMenus";

        /**
         * 根据指定菜单 ID 查询对应的菜单信息记录请求路径
         */
        public static final String URL_MENUS_BY_IDS = "menusByIds";

        /**
         * 提供给角色微服务模块, 根据菜单 ID 查询菜单信息记录集合并返回.
         */
        public static final String URL_MENUS_BY_IDS_VALUE = "/feignMenu/menusByIds";

        private ModuleMenu() {
        }

    }

    /**
     * 系统管理微服务数据字典管理模块相关常量定义.
     *
     * @author Lam Tong
     * @version 0.0.1
     * @since 0.0.1
     */
    public static final class ModuleDictionary {

        /**
         * 数据字典微服务控制器名称, 面向 Web 提供服务
         */
        public static final String CONTROLLER_URL = "dictionary";

        /**
         * 数据字典微服务控制器名称, 面向 OpenFeign 提供服务
         */
        public static final String CONTROLLER_FEIGN_URL = "feignDictionary";

        /**
         * 数据字典条件查询参数当前分页
         */
        public static final String PARA_CURRENT_PAGE = "cur";

        /**
         * 数据字典条件查询参数当前分页
         */
        public static final String PARA_CURRENT_PAGE_VALUE = "当前分页";

        /**
         * 数据字典条件查询参数分页大小
         */
        public static final String PARA_PAGE_SIZE = "size";

        /**
         * 数据字典条件查询参数分页大小
         */
        public static final String PARA_PAGE_SIZE_VALUE = "分页大小";

        /**
         * 数据字典条件查询参数查询条件
         */
        public static final String PARA_PARAMETERS = "parameters";

        /**
         * 数据字典条件查询参数查询条件
         */
        public static final String PARA_PARAMETERS_VALUE = "分页查询条件";

        /**
         * 新增、修改数据字典信息实体
         */
        public static final String PARA_DICTIONARY = "dictionary";

        /**
         * 新增、修改数据字典信息实体
         */
        public static final String PARA_DICTIONARY_VALUE = "数据字典实体";

        /**
         * 新增、修改数据字典类型信息实体
         */
        public static final String PARA_DICTIONARY_TYPE = "dictionaryType";

        /**
         * 新增、修改数据字典类型信息实体
         */
        public static final String PARA_DICTIONARY_TYPE_VALUE = "数据字典类型实体";

        /**
         * 数据字典 ID
         */
        public static final String PARA_DICTIONARY_ID = "id";

        /**
         * 数据字典 ID
         */
        public static final String PARA_DICTIONARY_ID_VALUE = "数据字典主键";

        /**
         * 数据字典 ID
         */
        public static final String PARA_DICTIONARY_IDS = "ids";

        /**
         * 数据字典 ID
         */
        public static final String PARA_DICTIONARY_IDS_VALUE = "数据字典主键";

        /**
         * 数据字典类型 ID
         */
        public static final String PARA_DICTIONARY_TYPE_ID = "id";

        /**
         * 数据字典类型 ID
         */
        public static final String PARA_DICTIONARY_TYPE_ID_VALUE = "数据字典类型主键";

        /**
         * 数据字典类型 ID
         */
        public static final String PARA_DICTIONARY_TYPE_IDS = "ids";

        /**
         * 数据字典类型 ID
         */
        public static final String PARA_DICTIONARY_TYPE_IDS_VALUE = "数据字典类型主键";

        /**
         * 数据字典类型
         */
        public static final String PARA_TYPE = "type";

        /**
         * 数据字典类型
         */
        public static final String PARA_TYPE_VALUE = "数据字典类型";

        /**
         * 条件查询数据字典请求路径
         */
        public static final String URL_DICTIONARY_LIST = "list";

        /**
         * 数据字典 CRUD 请求路径
         */
        public static final String URL_DICTIONARY = "dictionary";

        /**
         * 条件查询数据字典类型请求路径
         */
        public static final String URL_DICTIONARY_TYPE_LIST = "listType";

        /**
         * 数据字典类型 CRUD 请求路径
         */
        public static final String URL_DICTIONARY_TYPE = "dictionaryType";

        /**
         * 获取系统数据字典类型请求路径
         */
        public static final String URL_DICTIONARY_TYPES = "dictionaryTypes";

        /**
         * 根据数据字典类型获取对应数据字典信息请求路径
         */
        public static final String URL_DICTIONARY_LIST_BY_TYPE = "listByType";

        private ModuleDictionary() {
        }

    }

    /**
     * 系统管理微服务系统配置模块相关常量定义.
     *
     * @author Lam Tong
     * @version 0.0.1
     * @since 0.0.1
     */
    public static final class ModuleConfig {

        /**
         * 系统配置微服务控制器名称, 面向 Web 提供服务
         */
        public static final String CONTROLLER_URL = "config";

        /**
         * 系统配置微服务控制器名称, 面向 OpenFeign 提供服务
         */
        public static final String CONTROLLER_FEIGN_URL = "feignConfig";

        /**
         * 条件查询系统配置信息列表参数当前分页
         */
        public static final String PARA_CURRENT_PAGE = "cur";

        /**
         * 条件查询系统配置信息列表参数当前分页
         */
        public static final String PARA_CURRENT_PAGE_VALUE = "当前分页";

        /**
         * 条件查询系统配置信息列表参数分页大小
         */
        public static final String PARA_PAGE_SIZE = "size";

        /**
         * 条件查询系统配置信息列表参数分页大小
         */
        public static final String PARA_PAGE_SIZE_VALUE = "分页大小";

        /**
         * 条件查询系统配置信息列表参数查询条件
         */
        public static final String PARA_PARAMETERS = "parameters";

        /**
         * 条件查询系统配置信息列表参数查询条件
         */
        public static final String PARA_PARAMETERS_VALUE = "条件查询参数";

        /**
         * 新增、修改系统配置信息实体
         */
        public static final String PARA_CONFIG = "config";

        /**
         * 新增、修改系统配置信息实体
         */
        public static final String PARA_CONFIG_VALUE = "系统配置实体";

        /**
         * 系统配置 ID
         */
        public static final String PARA_CONFIG_ID = "id";

        /**
         * 系统配置 ID
         */
        public static final String PARA_CONFIG_ID_VALUE = "系统配置ID";

        /**
         * 系统配置 ID
         */
        public static final String PARA_CONFIG_IDS = "ids";

        /**
         * 系统配置 ID
         */
        public static final String PARA_CONFIG_IDS_VALUE = "系统配置ID";

        /**
         * 系统配置键
         */
        public static final String PARA_CONFIG_KEY = "configKey";

        /**
         * 系统配置键
         */
        public static final String PARA_CONFIG_KEY_VALUE = "系统配置键";

        /**
         * 条件查询系统配置请求路径
         */
        public static final String URL_CONFIG_LIST = "list";

        /**
         * 系统配置 CRUD 请求路径
         */
        public static final String URL_CONFIG = "config";

        /**
         * 根据配置键查询对应的配置信息请求路径
         */
        public static final String URL_GET_CONFIG = "getConfig";

        private ModuleConfig() {
        }

    }

}
