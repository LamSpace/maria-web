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
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.RoleMenu;
import io.github.lamtong.maria.system.service.RoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色微服务模块提供的供 {@code OpenFeign} 调用的接口, 主要用于接收系统内部认证中心微服务模块的调用.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "角色管理相关功能控制器", tags = {"角色管理(OpenFeign)"})
@RestController
@RequestMapping(value = {ServiceSystem.ModuleRole.CONTROLLER_FEIGN_URL})
public class OpenFeignRoleController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(OpenFeignRoleController.class);

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 面向认证中心微服务模块提供接口, 根据角色 ID 查询对应的菜单 ID 并返回.
     *
     * @param ids 角色 ID 集合
     * @return 菜单 ID 集合
     */
    @ApiOperation(value = "查询菜单ID", notes = "根据角色 ID 查询对应的菜单 ID 并返回.",
            tags = {"角色管理(OpenFeign)"}, httpMethod = RequestMethodConstant.POST, response = List.class)
    @PostMapping(value = {ServiceSystem.ModuleRole.URL_MENU_IDS_BY_ROLE_IDS})
    public List<Long> getMenuIdsByRoleIds(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE_IDS, value = ServiceSystem.ModuleRole.PARA_ROLE_IDS_VALUE)
                                          @RequestBody
                                          @NotEmpty(message = "角色ID不能为空") List<Long> ids) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoleMenu::getRoleId, ids);
        return this.roleMenuService.list(wrapper)
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

}
