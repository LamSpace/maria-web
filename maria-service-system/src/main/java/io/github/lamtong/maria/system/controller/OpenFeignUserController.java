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

package io.github.lamtong.maria.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.lamtong.maria.constant.AccountConstant;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.User;
import io.github.lamtong.maria.domain.entity.UserRole;
import io.github.lamtong.maria.system.service.UserRoleService;
import io.github.lamtong.maria.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户模块提供的供 {@code OpenFeign} 调用的接口, 主要用于接收系统内部 {@code 认证中心} 微服务模块的调用.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "用户管理相关功能控制器", tags = {"用户管理(OpenFeign)"})
@RestController
@RequestMapping(value = {ServiceSystem.ModuleUser.CONTROLLER_FEIGN_URL})
public class OpenFeignUserController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(OpenFeignUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 根据指定用户名查询对应的用户信息并返回.
     *
     * @param username 指定用户名
     * @return 指定用户信息
     */
    @ApiOperation(value = "查询用户信息", notes = "根据用户名查询对应的用户信息并返回认证中心微服务",
            tags = {"用户管理(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = User.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_FIND_BY_USERNAME})
    public User findByUsername(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USERNAME, value = ServiceSystem.ModuleUser.PARA_USERNAME_VALUE)
                               @RequestParam(value = ServiceSystem.ModuleUser.PARA_USERNAME)
                               @NotBlank(message = "用户账号不能为空") String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        return this.userService.getOne(wrapper);
    }

    /**
     * 根据用户 ID 查询用户的密码信息并返回.
     *
     * @param id 用户 ID
     * @return 用户密码
     */
    @ApiOperation(value = "查询用户密码", notes = "根据用户 ID 查询对应的密码并返回认证中心微服务",
            tags = {"用户管理(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = String.class)
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_GET_PASSWORD_BY_USER_ID})
    public String getPasswordUserById(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER_ID, value = ServiceSystem.ModuleUser.PARA_USER_ID_VALUE)
                                      @RequestParam(value = ServiceSystem.ModuleUser.PARA_USER_ID)
                                      @NotNull(message = "用户ID不能为空") Long id) {
        return this.userService.getById(id).getPassword();
    }

    /**
     * 根据用户名查询对应的角色 ID 集合
     *
     * @param username 用户名
     * @return 角色 ID 集合
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    @ApiOperation(value = "查询用户角色ID", notes = "根据用户名查询用户的角色 ID 集合并返回角色微服务",
            tags = {"用户管理(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = List.class)
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_ROLE_ID_BY_USERNAME})
    public List<Long> getRoleIdByUsername(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USERNAME, value = ServiceSystem.ModuleUser.PARA_USERNAME_VALUE)
                                          @RequestParam(value = ServiceSystem.ModuleUser.PARA_USERNAME)
                                          @NotBlank(message = "用户账号不能为空") String username) {
        if (AccountConstant.ADMIN.equals(username)) {
            List<Long> list = new ArrayList<>();
            list.add(-1L);
            return list;
        }
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        Long userId = this.userService.getOne(userWrapper).getId();
        LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(userId != null, UserRole::getUserId, userId);
        return this.userRoleService.list(userRoleWrapper)
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

}
