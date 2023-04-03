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
import io.github.lamtong.maria.constant.DistributedLock;
import io.github.lamtong.maria.constant.OperationTypeEnum;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.Menu;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.annotation.MariaLogger;
import io.github.lamtong.maria.snowflake.Generator;
import io.github.lamtong.maria.system.service.MenuService;
import io.github.lamtong.maria.util.MenuUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * 系统管理微服务模块菜单管理控制器, 向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Api(value = "菜单模块相关功能控制器", tags = {"菜单管理(Web)"})
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping(value = {ServiceSystem.ModuleMenu.CONTROLLER_URL})
public class WebMenuController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebMenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * 查询菜单目录树并返回.
     *
     * @return 菜单目录树
     */
    @ApiOperation(value = "查询菜单树", notes = "查询所有菜单信息并以树结构形式返回",
            tags = {"菜单管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleMenu.URL_MENU_TREE})
    public Res getMenuTree() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = this.menuService.list(wrapper);
        Menu[] tree = MenuUtil.extractMenuTree(list);
        this.addRootNode(list);
        return Res.ok()
                .message("查询菜单信息成功!")
                .data("tree", tree)
                .data("list", this.filter(list));
    }

    /**
     * 新增菜单记录信息.
     * <p/>
     * 注意: 需要使用分布式锁来保证菜单记录的 orderNum 在其上级菜单管理范围之内是唯一的.
     *
     * @param menu 新增的菜单信息
     * @return 新增菜单信息操作结果
     */
    @MariaLogger(value = "新增菜单信息", type = OperationTypeEnum.CREATE)
    @PreAuthorize(value = "hasAuthority('menu@add')")
    @ApiOperation(value = "新增菜单信息", notes = "新增菜单信息",
            tags = {"菜单管理(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleMenu.URL_MENU})
    public Res addMenu(@ApiParam(name = ServiceSystem.ModuleMenu.PARA_MENU, value = ServiceSystem.ModuleMenu.PARA_MENU_VALUE)
                       @RequestBody
                       @Valid Menu menu) {
        if (this.menuNameExists(menu.getMenuName(), null)) {
            return Res.error().message("存在菜单名称相同的菜单记录, 请重新填写!");
        }
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menu.getParentId());

        boolean success;
        Lock lock = this.redisLockRegistry.obtain(DistributedLock.ADD_MENU);
        lock.lock();
        try {
            long total = this.menuService.count(wrapper);
            menu.setId(Generator.nextId(Menu.class));
            menu.setOrderNum(((int) total) + 1);
            success = this.menuService.save(menu);
        } finally {
            lock.unlock();
        }
        return success ? Res.ok().message("新增菜单信息成功!") : Res.error().message("新增菜单信息失败!");
    }

    /**
     * 修改菜单记录信息.
     *
     * @param menu 更改后菜单信息
     * @return 修改菜单信息操作结果
     */
    @MariaLogger(value = "修改菜单信息", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('menu@update')")
    @ApiOperation(value = "修改菜单信息", notes = "根据指定菜单 ID 修改对应的信息",
            tags = {"菜单管理(Web)"}, httpMethod = RequestMethodConstant.PUT, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = {ServiceSystem.ModuleMenu.URL_MENU})
    public Res updateMenu(@ApiParam(name = ServiceSystem.ModuleMenu.PARA_MENU, value = ServiceSystem.ModuleMenu.PARA_MENU_VALUE)
                          @RequestBody
                          @Valid Menu menu) {
        if (this.menuNameExists(menu.getMenuName(), menu.getId())) {
            return Res.error().message("存在菜单名称相同的菜单记录, 请重新填写!");
        }
        boolean success = this.menuService.updateById(menu);
        while (!success) {
            Menu newMenu = this.menuService.getById(menu.getId());
            menu.setVersion(newMenu.getVersion());
            success = this.menuService.save(menu);
        }
        return Res.ok().message("修改菜单信息成功!");
    }

    /**
     * 查询菜单信息.
     *
     * @param id 菜单 ID
     * @return 查询菜单信息操作结果
     */
    @PreAuthorize(value = "hasAuthority('menu@update')")
    @ApiOperation(value = "查询菜单信息", notes = "根据菜单 ID 查询对应的信息并返回",
            tags = {"菜单管理(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleMenu.URL_MENU})
    public Res getMenu(@ApiParam(name = ServiceSystem.ModuleMenu.PARA_MENU_ID, value = ServiceSystem.ModuleMenu.PARA_MENU_ID_VALUE)
                       @RequestParam(value = ServiceSystem.ModuleMenu.PARA_MENU_ID)
                       @NotNull(message = "菜单ID不能为空") Long id) {
        Menu menu = this.menuService.getById(id);
        return Res.ok()
                .message("获取菜单信息成功!")
                .data("menu", menu);
    }

    /**
     * 批量删除菜单记录信息.
     * <p/>
     * 注意: 由于菜单可能关联子菜单, 故迭代删除菜单及其子菜单
     *
     * @param ids 菜单 ID 集合
     * @return 批量删除菜单记录信息操作结果
     */
    @MariaLogger(value = "批量删除菜单信息", type = OperationTypeEnum.DELETE_BATCH)
    @PreAuthorize(value = "hasAuthority('menu@delete')")
    @ApiOperation(value = "删除菜单信息", notes = "根据菜单 ID 删除对应的信息",
            tags = {"菜单管理(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = {ServiceSystem.ModuleMenu.URL_MENU})
    public Res deleteMenu(@ApiParam(name = ServiceSystem.ModuleMenu.PARA_MENU_IDS, value = ServiceSystem.ModuleMenu.PARA_MENU_IDS_VALUE)
                          @RequestBody
                          @NotEmpty(message = "菜单ID不能为空") List<Long> ids) {
        List<Long> tracedMenuIds = this.traceMenus(ids);
        boolean success = this.menuService.removeByIds(tracedMenuIds);
        return success ? Res.ok().message("删除菜单信息成功!") : Res.error().message("删除菜单信息失败!");
    }

    /**
     * 向菜单信息记录列表中添加一个根节点.
     *
     * @param list 菜单信息记录列表
     * @return 菜单信息记录列表
     */
    @SuppressWarnings("UnusedReturnValue")
    private List<Menu> addRootNode(List<Menu> list) {
        Menu root = new Menu();
        root.setId(0L);
        root.setMenuName("根菜单");
        root.setType(0);
        list.add(root);
        return list;
    }

    /**
     * 从所有菜单信息记录列表中过滤掉按钮级别菜单.
     *
     * @param list 菜单信息记录列表
     * @return 过滤掉按钮级别后的菜单信息记录列表
     */
    private List<Menu> filter(List<Menu> list) {
        return list.stream()
                .filter(m -> m.getType() == 0)
                .collect(Collectors.toList());
    }

    /**
     * 检查指定的菜单名称是否存在.
     *
     * @param menuName 指定菜单名称
     * @param id       菜单 ID
     * @return true: 指定菜单名称已存在; 否则返回 false
     */
    private boolean menuNameExists(String menuName, Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, Menu::getId, id)
                .eq(StringUtils.hasLength(menuName), Menu::getMenuName, menuName);
        long count = this.menuService.count(wrapper);
        return count != 0;
    }

    /**
     * 根据指定 ID 集合查询其子节点 ID 并返回.
     *
     * @param ids 指定菜单 ID 集合
     * @return 指定菜单 ID 及其子节点 ID 集合.
     */
    private List<Long> traceMenus(List<Long> ids) {
        List<Long> ans = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        for (Long id : ids) {
            queue.offer(id);
        }
        while (!queue.isEmpty()) {
            Long id = queue.poll();
            ans.add(id);
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getParentId, id);
            List<Menu> menus = this.menuService.list(wrapper);
            for (Menu menu : menus) {
                queue.offer(menu.getId());
            }
        }
        return ans;
    }

}
