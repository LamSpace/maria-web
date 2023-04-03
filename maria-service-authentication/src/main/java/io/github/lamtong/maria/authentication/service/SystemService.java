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

package io.github.lamtong.maria.authentication.service;

import io.github.lamtong.maria.authentication.config.OpenFeignConfig;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.Menu;
import io.github.lamtong.maria.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程调用系统管理微服务模块.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@FeignClient(value = ServiceSystem.SERVICE_NAME, configuration = OpenFeignConfig.class)
public interface SystemService {

    /**
     * 调用远程微服务模块, 根据用户登录账号查询指定用户信息是否存在，并返回.
     *
     * @param username 指定用户登录名.
     * @return 指定用户信息.
     */
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_FIND_BY_USERNAME_VALUE})
    User findByUsername(@RequestParam(value = ServiceSystem.ModuleUser.PARA_USERNAME) String username);

    /**
     * 调用远程微服务模块, 根据用户 ID 查询用户密码信息并返回.
     *
     * @param id 用户 ID
     * @return 用户密码信息
     */
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_GET_PASSWORD_BY_USER_ID_VALUE})
    String getPasswordUserById(@RequestParam(value = ServiceSystem.ModuleUser.PARA_USER_ID) Long id);

    /**
     * 调用远程微服务模块, 根据用户名查询对应的角色 ID 并返回.
     *
     * @param username 用户名
     * @return 角色 ID 列表
     */
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_ROLE_ID_BY_USERNAME_VALUE})
    List<Long> loadRoleIdsByUsername(@RequestParam(value = ServiceSystem.ModuleUser.PARA_USERNAME) String username);

    /**
     * 调用远程微服务模块, 根据角色 ID 集合获取对应的菜单 ID 集合并返回.
     *
     * @param ids 菜单 ID 集合
     * @return 菜单 ID 集合
     */
    @PostMapping(value = {ServiceSystem.ModuleRole.URL_MENU_IDS_BY_ROLE_IDS_VALUE})
    List<Long> loadMenuIdsByRoleIds(@RequestBody List<Long> ids);

    /**
     * 获取所有菜单权限信息, 此操作仅限于超级管理员认证.
     *
     * @return 所有菜单权限信息.
     */
    @GetMapping(value = {ServiceSystem.ModuleMenu.URL_GET_ALL_MENUS_VALUE})
    List<Menu> getAllMenus();

    /**
     * 远程调用菜单微服务模块, 根据菜单 ID 查询对应的菜单信息.
     *
     * @param ids 菜单 ID 集合
     * @return 菜单信息集合
     */
    @PostMapping(value = {ServiceSystem.ModuleMenu.URL_MENUS_BY_IDS_VALUE})
    List<Menu> getMenusByIds(@RequestBody List<Long> ids);

}
