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
import io.github.lamtong.maria.constant.AccountConstant;
import io.github.lamtong.maria.constant.OperationTypeEnum;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.Role;
import io.github.lamtong.maria.domain.entity.User;
import io.github.lamtong.maria.domain.entity.UserRole;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.annotation.MariaLogger;
import io.github.lamtong.maria.snowflake.Generator;
import io.github.lamtong.maria.system.service.RoleService;
import io.github.lamtong.maria.system.service.SocketService;
import io.github.lamtong.maria.system.service.UserRoleService;
import io.github.lamtong.maria.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统管理微服务模块用户管理控制器, 向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "用户模块相关功能控制器", tags = {"用户管理(Web)"})
@RestController
@RequestMapping(value = {ServiceSystem.ModuleUser.CONTROLLER_URL})
public class WebUserController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings(value = {"unused"})
    @Autowired
    private SocketService socketService;

    /**
     * 分页条件查询用户信息列表, 查询条件包括用户账号、用户昵称、用户邮箱、联系方式、身份证号以及用户地址.
     *
     * @param cur        当前页
     * @param size       分页大小
     * @param parameters 查询条件
     * @return 条件查询用户信息列表
     */
    @ApiOperation(value = "条件查询用户信息列表", notes = "条件查询用户信息列表并返回.",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleUser.URL_GET_USER_LIST})
    public Res getUserList(@ApiParam(name = ServiceSystem.ModuleUser.PARA_CURRENT_PAGE, value = ServiceSystem.ModuleUser.PARA_CURRENT_PAGE_VALUE)
                           @RequestParam(value = ServiceSystem.ModuleUser.PARA_CURRENT_PAGE)
                           @NotNull(message = "当前分页不能为空") Long cur,
                           @ApiParam(name = ServiceSystem.ModuleUser.PARA_PAGE_SIZE, value = ServiceSystem.ModuleUser.PARA_PAGE_SIZE_VALUE)
                           @RequestParam(value = ServiceSystem.ModuleUser.PARA_PAGE_SIZE)
                           @NotNull(message = "分页大小不能为空") Long size,
                           @ApiParam(name = ServiceSystem.ModuleUser.PARA_PAGE_CONDITION, value = ServiceSystem.ModuleUser.PARA_PAGE_CONDITION_VALUE)
                           @RequestBody
                           @NotEmpty(message = "查询条件不能为空") Map<String, String> parameters) throws ParseException {
        Page<User> page = new Page<>(cur, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String username = parameters.get("username").trim();
        String nickname = parameters.get("nickname").trim();
        String email = parameters.get("email").trim();
        String telephone = parameters.get("telephone").trim();
        String idCardNumber = parameters.get("idCard").trim();
        String address = parameters.get("address").trim();
        String start = parameters.get("start").trim();
        String end = parameters.get("end").trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 查询用户信息列表时需排除超级管理员
        wrapper.ne(User::getUsername, AccountConstant.ADMIN)
                .like(StringUtils.hasLength(username), User::getUsername, username)
                .like(StringUtils.hasLength(nickname), User::getNickname, nickname)
                .like(StringUtils.hasLength(email), User::getEmail, email)
                .like(StringUtils.hasLength(telephone), User::getTelephone, telephone)
                .like(StringUtils.hasLength(idCardNumber), User::getIdCard, idCardNumber)
                .like(StringUtils.hasLength(address), User::getAddress, address);
        if (StringUtils.hasLength(start) && StringUtils.hasLength(end)) {
            wrapper.between(User::getBirthday, format.parse(start), format.parse(end));
        }
        Page<User> userPage = this.userService.page(page, wrapper);

        // 向前端推送消息示例
        this.socketService.broadcastWithMessage("用户管理模块向前端推送(OpenFeign)消息演示!");

        return Res.ok()
                .message("条件查询用户信息成功!")
                .data("total", userPage.getTotal())
                .data("list", userPage.getRecords());
    }

    /**
     * 新增用户信息并保存至数据库.
     *
     * @param user 用户信息
     * @return 新增用户信息操作结果
     */
    @MariaLogger(value = "新增用户信息", type = OperationTypeEnum.CREATE)
    @PreAuthorize(value = "hasAuthority('user@add')")
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息.",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleUser.URL_USER})
    public Res addUser(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER, value = ServiceSystem.ModuleUser.PARA_USER_VALUE)
                       @RequestBody
                       @Valid User user) {
        if (this.usernameExists(user.getUsername(), null)) {
            return Res.error().message("存在用户账号相同的用户记录, 请重新填写!");
        }
        user.setId(Generator.nextId(User.class));
        user.setPassword(this.passwordEncoder.encode(ServiceSystem.ModuleUser.DEFAULT_PASSWORD));
        boolean success = this.userService.save(user);
        return success ? Res.ok().message("新增用户信息成功!") : Res.error().message("新增用户信息失败!");
    }

    /**
     * 根据用户 ID 修改用户信息. 由于采取了乐观锁来更新记录, 更新操作可能存在失败的情况, 因此采用自旋的方式保证更新操作成功.
     *
     * @param user 修改后用户信息
     * @return 修改用户信息操作结果
     */
    @MariaLogger(value = "修改用户信息", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('user@update')")
    @ApiOperation(value = "修改用户信息", notes = "根据用户 ID 修改指定用户信息",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.PUT, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = {ServiceSystem.ModuleUser.URL_USER})
    public Res updateUser(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER, value = ServiceSystem.ModuleUser.PARA_USER_VALUE)
                          @RequestBody
                          @Valid User user) {
        if (this.usernameExists(user.getUsername(), user.getId())) {
            return Res.error().message("存在用户账号相同的用户记录, 请重新填写!");
        }
        boolean success = this.userService.updateById(user);
        while (!success) {
            User newUser = this.userService.getById(user.getId());
            user.setVersion(newUser.getVersion());
            success = this.userService.updateById(user);
        }
        return Res.ok().message("修改用户信息成功!");
    }

    /**
     * 根据用户 ID 查询用户信息并返回.
     *
     * @param id 用户 ID
     * @return 用户信息
     */
    @PreAuthorize(value = "hasAuthority('user@update')")
    @ApiOperation(value = "查询用户信息", notes = "根据用户 ID 查询指定用户信息",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_USER})
    public Res getUser(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER_ID, value = ServiceSystem.ModuleUser.PARA_USER_ID_VALUE)
                       @RequestParam(value = ServiceSystem.ModuleUser.PARA_USER_ID)
                       @NotNull(message = "用户ID不能为空") Long id) {
        User user = this.userService.getById(id);
        return Res.ok()
                .message("查询用户信息成功!")
                .data("user", user);
    }

    /**
     * 根据用户 ID 批量删除指定用户信息记录.
     * <p/>
     * 注意: 删除用户时需要删除与之相关的<b>用户</b>-<b>角色</b>关联关系.
     *
     * @param ids 用户 ID 数组.
     * @return 批量删除用户信息操作结果
     */
    @MariaLogger(value = "批量删除用户记录", type = OperationTypeEnum.DELETE_BATCH)
    @PreAuthorize(value = "hasAuthority('user@delete')")
    @ApiOperation(value = "删除用户信息", notes = "根据用户 ID 批量删除用户信息记录",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = {ServiceSystem.ModuleUser.URL_USER})
    @Transactional(rollbackFor = Exception.class)
    public Res deleteUsers(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER_IDS, value = ServiceSystem.ModuleUser.PARA_USER_IDS_VALUE)
                           @RequestBody
                           @NotEmpty(message = "用户ID不能为空") List<Long> ids) {
        boolean success = this.userService.removeByIds(ids);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRole::getUserId, ids);
        this.userRoleService.remove(wrapper);
        return success ? Res.ok().message("删除用户信息成功!") : Res.error().message("删除用户信息失败!");
    }

    /**
     * 重置指定用户密码信息.
     *
     * @param id 用户 ID
     * @return 重置用户密码操作结果
     */
    @MariaLogger(value = "重置用户密码", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('user@reset')")
    @ApiOperation(value = "重置用户密码", notes = "根据用户 ID 重置指定用户密码",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_RESET_PASSWORD})
    public Res resetPassword(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER_ID, value = ServiceSystem.ModuleUser.PARA_USER_ID_VALUE)
                             @RequestParam(value = ServiceSystem.ModuleUser.PARA_USER_ID)
                             @NotNull(message = "用户ID不能为空") Long id) {
        User user = new User();
        user.setId(id);
        user.setPassword(this.passwordEncoder.encode(ServiceSystem.ModuleUser.DEFAULT_PASSWORD));
        boolean success = this.userService.saveOrUpdate(user);
        return success ? Res.ok().message("重置用户密码成功!") : Res.error().message("重置用户密码失败!");
    }

    /**
     * 根据指定用户 ID 查询与之关联的角色 ID 集合, 同时查询可用角色信息列表.
     *
     * @param id 用户 ID
     * @return 与指定用户绑定的角色 ID 集合与可用角色信息列表
     */
    @PreAuthorize(value = "hasAuthority('user@configure')")
    @ApiOperation(value = "查询用户-角色关联信息", notes = "根据用户 ID 查询对应的角色关联关系",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleUser.URL_USER_ROLE_BINDING})
    public Res getUserRoleBinding(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER_ID, value = ServiceSystem.ModuleUser.PARA_USER_ID_VALUE)
                                  @RequestParam(value = ServiceSystem.ModuleUser.PARA_USER_ID)
                                  @NotNull(message = "用户ID不能为空") Long id) {
        List<String> assignedRoleIds = this.retrieveAssignedRoleIds(id);
        List<Role> availableRoles = this.retrieveAvailableRoles();
        return Res.ok()
                .message("查询用户-角色关联信息成功!")
                .data("list", availableRoles)
                .data("assigned", assignedRoleIds);
    }

    /**
     * 配置用户与角色的绑定关系. 采用增量方式配置用户-角色关联信息, 即从用户已配置角色关联关系中删除未出现在更新后关联关系中的绑定关系，
     * 同时新增更新后关联关系中未出现在已配置关联关系中的绑定关系.
     *
     * @param id             用户 ID
     * @param updatedRoleIds 重新分配的角色 ID 集合
     * @return 配置用户-角色绑定关系操作结果
     */
    @MariaLogger(value = "配置用户-角色关联关系", type = OperationTypeEnum.CREATE_UPDATE)
    @PreAuthorize(value = "hasAuthority('user@configure')")
    @ApiOperation(value = "配置用户-角色关联信息", notes = "根据用户 ID 配置其对应的角色关联关系",
            tags = {"用户管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = {ServiceSystem.ModuleUser.URL_CONFIGURE_USER_ROLE_BINDING})
    public Res configureUserRole(@ApiParam(name = ServiceSystem.ModuleUser.PARA_USER_ID, value = ServiceSystem.ModuleUser.PARA_USER_ID_VALUE)
                                 @RequestParam(value = ServiceSystem.ModuleUser.PARA_USER_ID)
                                 @NotNull(message = "用户ID不能为空") Long id,
                                 @ApiParam(name = ServiceSystem.ModuleUser.PARA_UPDATED_ROLE_IDS, value = ServiceSystem.ModuleUser.PARA_UPDATED_ROLE_IDS_VALUE)
                                 @RequestBody List<Long> updatedRoleIds) {
        String username = this.retrieveUsernameById(id);
        String currentUser = this.retrieveCurrentUser();
        if (currentUser.equals(username)) {
            return Res.error().message("您无法配置与自己绑定的角色信息!");
        }

        List<Long> previousRoleIds = this.retrieveAssignedUserRoleBindings(id);

        List<Long> removedRoleIds = previousRoleIds.stream()
                .filter(n -> !updatedRoleIds.contains(n))
                .collect(Collectors.toList());
        List<Long> addedRoleIds = updatedRoleIds.stream()
                .filter(n -> !previousRoleIds.contains(n))
                .collect(Collectors.toList());

        if (!removedRoleIds.isEmpty()) {
            LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(id != null, UserRole::getUserId, id)
                    .in(UserRole::getRoleId, removedRoleIds);
            this.userRoleService.remove(wrapper);
        }
        if (!addedRoleIds.isEmpty()) {
            List<UserRole> list = new ArrayList<>();
            for (Long roleId : addedRoleIds) {
                UserRole userRole = new UserRole();
                userRole.setId(Generator.nextId(UserRole.class));
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                list.add(userRole);
            }
            this.userRoleService.saveBatch(list);
        }
        return Res.ok()
                .message("配置用户-角色关联信息成功！");
    }

    /**
     * 检查指定用户名（username）是否存在.
     *
     * @param username 指定用户名
     * @param id       用户 ID
     * @return true: 指定用户名存在; 否则返回 false
     */
    private boolean usernameExists(String username, Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, User::getId, id)
                .eq(StringUtils.hasLength(username), User::getUsername, username);
        long count = this.userService.count(wrapper);
        return count != 0;
    }

    /**
     * 根据指定用户 ID 获取对应的已分配角色 ID.
     *
     * @param userId 用户 ID
     * @return 已分配的角色 ID
     */
    private List<String> retrieveAssignedRoleIds(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, UserRole::getUserId, userId);
        return this.userRoleService.list(wrapper)
                .stream()
                .map(UserRole::getRoleId)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 调用远程角色微服务模块, 获取全部可用角色信息列表.
     *
     * @return 可用角色信息列表
     */
    private List<Role> retrieveAvailableRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getEnabled, 1);
        return this.roleService.list(wrapper);
    }

    /**
     * 根据用户 ID 查询用户对应的用户名.
     *
     * @param id 用户 ID
     * @return 用户名
     */
    private String retrieveUsernameById(Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, User::getId, id);
        return this.userService.getOne(wrapper).getUsername();
    }

    /**
     * 获取当前执行操作的用户名.
     *
     * @return 当前正在执行操作的用户名.
     */
    private String retrieveCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获取指定用户已配置的用户-角色关联关系.
     *
     * @param userId 用户 ID
     * @return 用户已配置的用户-角色关联关系
     */
    private List<Long> retrieveAssignedUserRoleBindings(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, UserRole::getUserId, userId);
        return this.userRoleService.list(wrapper)
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

}
