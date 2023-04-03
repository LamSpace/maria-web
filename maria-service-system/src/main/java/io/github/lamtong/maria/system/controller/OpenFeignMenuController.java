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
import io.github.lamtong.maria.domain.entity.Menu;
import io.github.lamtong.maria.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统管理微服务提供的供 {@code OpenFeign} 调用的接口, 主要用于接收系统内部认证中心微服务模块的调用.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "菜单管理相关功能控制器", tags = {"菜单管理(OpenFeign)"})
@RestController
@RequestMapping(value = {ServiceSystem.ModuleMenu.CONTROLLER_FEIGN_URL})
public class OpenFeignMenuController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(OpenFeignMenuController.class);

    @Autowired
    private MenuService menuService;

    /**
     * 面向认证中心微服务提供服务, 查询所有菜单记录并以列表形式返回.
     *
     * @return 所有菜单记录
     */
    @ApiOperation(value = "查询菜单信息", notes = "查询所有菜单信息并以列表形式返回认证中心微服务",
            tags = {"菜单管理(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = List.class)
    @GetMapping(value = {ServiceSystem.ModuleMenu.URL_GET_ALL_MENUS})
    public List<Menu> getAllMenus() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        return this.menuService.list(wrapper);
    }

    /**
     * 根据菜单 ID 查询对应的信息.
     *
     * @param ids 菜单 ID 集合
     * @return 对应的菜单信息集合
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    @ApiOperation(value = "查询菜单信息", notes = "根据菜单 ID 查询对应的菜单信息并返回给角色微服务",
            tags = {"菜单管理(OpenFeign)"}, httpMethod = RequestMethodConstant.POST, response = List.class)
    @PostMapping(value = {ServiceSystem.ModuleMenu.URL_MENUS_BY_IDS})
    public List<Menu> getMenusByIds(@ApiParam(name = ServiceSystem.ModuleMenu.PARA_MENU_IDS, value = ServiceSystem.ModuleMenu.PARA_MENU_IDS_VALUE)
                                    @RequestBody List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        wrapper.in(Menu::getId, ids)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);
        return this.menuService.list(wrapper);
    }

}
