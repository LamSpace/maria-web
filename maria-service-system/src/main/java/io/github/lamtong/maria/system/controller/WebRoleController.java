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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.lamtong.maria.constant.*;
import io.github.lamtong.maria.domain.entity.*;
import io.github.lamtong.maria.domain.message.MariaMessage;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.annotation.MariaLogger;
import io.github.lamtong.maria.snowflake.Generator;
import io.github.lamtong.maria.system.service.*;
import io.github.lamtong.maria.system.service.impl.RoleTxMessageService;
import io.github.lamtong.maria.util.MenuUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统管理微服务模块角色管理控制器, 向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "角色模块相关功能控制器", tags = {"角色管理(Web)"})
@RestController
@RequestMapping(value = {ServiceSystem.ModuleRole.CONTROLLER_URL})
public class WebRoleController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebRoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @SuppressWarnings(value = {"AlibabaLowerCamelCaseVariableNaming"})
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RoleTxMessageService roleTxMessageService;

    /**
     * 条件查询角色信息列表并返回, 查询条件包括角色名称.
     *
     * @param cur        当前页码
     * @param size       分页大小
     * @param parameters 查询条件
     * @return 条件查询角色信息列表
     */
    @ApiOperation(value = "查询角色信息", notes = "条件查询角色信息列表",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleRole.URL_GET_ROLE_LIST})
    public Res getRoleList(@ApiParam(name = ServiceSystem.ModuleRole.PARA_CURRENT_PAGE, value = ServiceSystem.ModuleRole.PARA_CURRENT_PAGE_VALUE)
                           @RequestParam(value = ServiceSystem.ModuleRole.PARA_CURRENT_PAGE)
                           @NotNull(message = "当前分页不能为空") Long cur,
                           @ApiParam(name = ServiceSystem.ModuleRole.PARA_PAGE_SIZE, value = ServiceSystem.ModuleRole.PARA_PAGE_SIZE_VALUE)
                           @RequestParam(value = ServiceSystem.ModuleRole.PARA_PAGE_SIZE)
                           @NotNull(message = "分页大小不能为空") Long size,
                           @ApiParam(name = ServiceSystem.ModuleRole.PARA_PARAMETERS, value = ServiceSystem.ModuleRole.PARA_PARAMETERS_VALUE)
                           @RequestBody
                           @NotEmpty(message = "查询条件不能为空") Map<String, String> parameters) {
        Page<Role> page = new Page<>(cur, size);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        String name = parameters.get("name").trim();
        wrapper.like(StringUtils.hasLength(name), Role::getName, name);
        Page<Role> rolePage = this.roleService.page(page, wrapper);

        // 向前端推送消息示例
        MariaMessage message = new MariaMessage("角色管理模块向前端推送(RocketMQ)消息演示!");
        this.rocketMQTemplate.convertAndSend(ServiceRocket.TOPIC_DEMO, message);

        return Res.ok()
                .message("条件查询角色信息成功!")
                .data("total", rolePage.getTotal())
                .data("list", rolePage.getRecords());
    }

    /**
     * 新增角色信息并保存.
     *
     * @param role 新增角色信息
     * @return 新增角色信息操作结果
     */
    @MariaLogger(value = "新增角色信息", type = OperationTypeEnum.CREATE)
    @PreAuthorize(value = "hasAuthority('role@add')")
    @ApiOperation(value = "新增角色信息", notes = "新增角色信息",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleRole.URL_ROLE})
    public Res addRole(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE, value = ServiceSystem.ModuleRole.PARA_ROLE_VALUE)
                       @RequestBody
                       @Valid Role role) {
        if (this.roleNameExists(role.getName(), null)) {
            return Res.error().message("存在角色名称相同的角色记录, 请重新填写!");
        }
        role.setId(Generator.nextId(Role.class));
        boolean success = this.roleService.save(role);
        return success ? Res.ok().message("新增角色信息成功!") : Res.error().message("新增角色信息失败!");
    }

    /**
     * 根据角色 ID 修改角色信息. 由于采取了乐观锁来更新记录, 更新操作可能存在失败的情况, 因此采用自旋的方式保证更新操作成功.
     *
     * @param role 修改后的角色信息
     * @return 修改角色信息操作结果
     */
    @MariaLogger(value = "修改角色信息", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('role@update')")
    @ApiOperation(value = "修改角色信息", notes = "根据角色 ID 修改对应的角色信息",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.PUT, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = {ServiceSystem.ModuleRole.URL_ROLE})
    public Res updateRole(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE, value = ServiceSystem.ModuleRole.PARA_ROLE_VALUE)
                          @RequestBody
                          @Valid Role role) {
        if (this.roleNameExists(role.getName(), role.getId())) {
            return Res.error().message("存在角色名称相同的角色记录, 请重新填写!");
        }
        boolean success = this.roleService.updateById(role);
        while (!success) {
            Role newRole = this.roleService.getById(role.getId());
            role.setVersion(newRole.getVersion());
            success = this.roleService.updateById(role);
        }
        return Res.ok().message("修改角色信息成功!");
    }

    /**
     * 根据指定角色 ID 获取对应的角色信息.
     *
     * @param id 角色 ID
     * @return 指定角色信息
     */
    @PreAuthorize(value = "hasAuthority('role@update')")
    @ApiOperation(value = "查询角色信息", notes = "根据角色 ID 查询对应的角色信息",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleRole.URL_ROLE})
    public Res getRole(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE_ID, value = ServiceSystem.ModuleRole.PARA_ROLE_ID_VALUE)
                       @RequestParam(value = ServiceSystem.ModuleRole.PARA_ROLE_ID)
                       @NotNull(message = "角色ID不能为空") Long id) {
        Role role = this.roleService.getById(id);

        // 使用 RocketMQ 模拟事务消息实现分布式事务
        this.roleTxMessageService.createRoleTransaction(role);

        return Res.ok()
                .message("获取角色信息成功!")
                .data("role", role);
    }

    /**
     * 根据角色 ID 批量删除角色信息记录.
     * <p/>
     * 注意: 删除角色时需要删除与之关联的<b>角色</b>-<b>菜单</b>关联关系.
     *
     * @param ids 角色 ID 集合
     * @return 删除角色记录操作结果
     */
    @MariaLogger(value = "批量删除角色信息", type = OperationTypeEnum.DELETE_BATCH)
    @PreAuthorize(value = "hasAuthority('role@delete')")
    @ApiOperation(value = "删除角色信息", notes = "根据角色 ID 批量删除角色信息",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = {ServiceSystem.ModuleRole.URL_ROLE})
    @Transactional(rollbackFor = Exception.class)
    public Res deleteRole(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE_IDS, value = ServiceSystem.ModuleRole.PARA_ROLE_IDS_VALUE)
                          @RequestBody
                          @NotEmpty(message = "角色ID不能为空") List<Long> ids) {
        boolean success = this.roleService.removeByIds(ids);
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoleMenu::getRoleId, ids);
        this.roleMenuService.remove(wrapper);
        return success ? Res.ok().message("删除角色信息成功!") : Res.error().message("删除角色信息失败!");
    }

    /**
     * 获取与指定角色关联的菜单信息以及全部可用的菜单信息集合.
     *
     * @param id 角色 ID
     * @return 与指定角色关联的菜单信息以及全部可用的菜单信息集合
     */
    @PreAuthorize(value = "hasAuthority('role@configure')")
    @ApiOperation(value = "查询角色-菜单关联关系", notes = "根据角色 ID 查询与之关联的菜单信息以及全部菜单信息",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleRole.URL_ROLE_MENU_BINDING})
    public Res getRoleMenuBinding(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE_ID, value = ServiceSystem.ModuleRole.PARA_ROLE_ID_VALUE)
                                  @RequestParam(value = ServiceSystem.ModuleRole.PARA_ROLE_ID)
                                  @NotNull(message = "角色ID不能为空") Long id) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, RoleMenu::getRoleId, id);
        List<Long> assignedMenuIds = this.roleMenuService.list(wrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        List<Menu> assignedMenus = this.getMenusByIds(assignedMenuIds);

        List<String> filteredMenuIds = MenuUtil.filterLeaves(assignedMenus);
        Menu[] menuTree = this.retrieveMenuTree();
        return Res.ok()
                .message("查询角色-菜单关联信息成功!")
                .data("tree", menuTree)
                .data("list", filteredMenuIds);
    }

    /**
     * 配置指定角色与菜单的关联关系.
     * <p/>
     * 注意: 需校验用户修改正在配置与自己绑定的角色的菜单关联信息.
     *
     * @param id             角色 ID
     * @param updatedMenuIds 更新后的菜单 ID 集合
     * @return 配置角色-菜单关联关系操作结果
     */
    @MariaLogger(value = "配置角色-菜单关联关系", type = OperationTypeEnum.CREATE_UPDATE)
    @PreAuthorize(value = "hasAuthority('role@configure')")
    @ApiOperation(value = "配置角色-菜单关联关系", notes = "根据角色 ID 配置其菜单关联关系",
            tags = {"角色管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = {ServiceSystem.ModuleRole.URL_CONFIGURE_ROLE_MENU_BINDING})
    public Res configureRoleMenu(@ApiParam(name = ServiceSystem.ModuleRole.PARA_ROLE_ID, value = ServiceSystem.ModuleRole.PARA_ROLE_ID_VALUE)
                                 @RequestParam(value = ServiceSystem.ModuleRole.PARA_ROLE_ID)
                                 @NotNull(message = "角色ID不能为空") Long id,
                                 @ApiParam(name = ServiceSystem.ModuleRole.PARA_UPDATED_MENU_IDS, value = ServiceSystem.ModuleRole.PARA_UPDATED_MENU_IDS_VALUE)
                                 @RequestBody List<Long> updatedMenuIds) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Long> roleIds = this.getRoleIdByUsername(username);
        if (roleIds.contains(id)) {
            return Res.error().message("您无法配置与自己绑定的角色的菜单信息!");
        }

        List<Long> assignedMenuIds = this.retrieveAssignedMenuIds(id);

        List<Long> removedMenuIds = assignedMenuIds.stream()
                .filter(n -> !updatedMenuIds.contains(n))
                .collect(Collectors.toList());
        List<Long> addedMenuIds = updatedMenuIds.stream()
                .filter(n -> !assignedMenuIds.contains(n))
                .collect(Collectors.toList());

        if (!removedMenuIds.isEmpty()) {
            LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(id != null, RoleMenu::getRoleId, id)
                    .in(RoleMenu::getMenuId, removedMenuIds);
            this.roleMenuService.remove(wrapper);
        }
        if (!addedMenuIds.isEmpty()) {
            List<RoleMenu> list = new ArrayList<>();
            for (Long menuId : addedMenuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setId(Generator.nextId(RoleMenu.class));
                roleMenu.setRoleId(id);
                roleMenu.setMenuId(menuId);
                list.add(roleMenu);
            }
            this.roleMenuService.saveBatch(list);
        }
        return Res.ok()
                .message("配置角色-菜单关联信息成功!");
    }

    /**
     * 检查指定的角色名称是否存在.
     *
     * @param roleName 角色名称
     * @param id       角色 ID
     * @return true: 指定的角色名称已存在; 否则返回 false
     */
    private boolean roleNameExists(String roleName, Long id) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, Role::getId, id)
                .eq(StringUtils.hasLength(roleName), Role::getName, roleName);
        long count = this.roleService.count(wrapper);
        return count != 0;
    }

    /**
     * 获取所有可用菜单信息记录并组织称树结构.
     *
     * @return 树结构的菜单信息集合
     */
    private Menu[] retrieveMenuTree() {
        List<Menu> menus = this.menuService.list();
        return MenuUtil.extractMenuTree(MenuUtil.filterDisabled(menus));
    }

    /**
     * 获取与指定角色关联的菜单 ID.
     *
     * @param roleId 角色 ID
     * @return 与指定角色关联的菜单 ID 集合
     */
    private List<Long> retrieveAssignedMenuIds(Long roleId) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(roleId != null, RoleMenu::getRoleId, roleId);
        return this.roleMenuService.list(wrapper)
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    /**
     * 根据菜单 ID 查询对应的菜单信息并返回.
     * <p/>
     * 注意: 菜单 ID 集合可能为空, 即角色未绑定任何菜单, 需特殊处理婴一下.
     *
     * @param ids 菜单 ID 集合
     * @return 菜单信息集合
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    public List<Menu> getMenusByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        wrapper.in(Menu::getId, ids)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);
        return this.menuService.list(wrapper);
    }

    /**
     * 根据用户名查询对应的角色 ID 集合
     *
     * @param username 用户名
     * @return 角色 ID 集合
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    public List<Long> getRoleIdByUsername(String username) {
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
