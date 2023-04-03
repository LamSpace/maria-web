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

package io.github.lamtong.maria.util;

import io.github.lamtong.maria.domain.entity.Menu;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * 菜单权限记录工具类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class MenuUtil {

    private MenuUtil() {
    }

    /**
     * 从指定的菜单权限记录列表中提取对应的菜单权限标志符集合并返回.
     *
     * @param list 采集记录列表
     * @return 菜单权限标志符集合
     */
    public static String[] extractPermissions(List<Menu> list) {
        return list.stream()
                .filter(menu -> menu.getType() == 1)
                .filter(menu -> menu.getStatus() == 1)
                .map(Menu::getPerms)
                .toArray(String[]::new);
    }

    /**
     * 将给定的菜单记录集合抽取成一棵菜单树并返回, 默认包含按钮级别菜单.
     *
     * @param list 指定菜单记录集合
     * @return 菜单树
     */
    public static Menu[] extractMenuTree(List<Menu> list) {
        return extractMenuTree(list, true);
    }

    /**
     * 将给定的菜单记录集合抽取成一棵菜单树并返回.
     *
     * @param list          指定菜单记录集合
     * @param includeButton 是否包含按钮级别菜单. 若包含菜单, 则主要用于在菜单列表中展示.
     * @return 菜单树
     */
    public static Menu[] extractMenuTree(List<Menu> list, boolean includeButton) {
        list.forEach(menu -> menu.setDisabled(menu.getStatus() == 0));
        Queue<Menu> queue = new LinkedList<>();
        Menu root = new Menu();
        root.setId(0L);
        queue.offer(root);
        while (!queue.isEmpty()) {
            Menu menu = queue.poll();
            Menu[] children = getChildren(list, menu.getId(), includeButton);
            menu.setChildren(children);
            for (Menu child : children) {
                queue.offer(child);
            }
        }
        return root.getChildren().clone();
    }

    /**
     * 根据条件从给定菜单记录集合中查询指定父节点的直接子节点并以数组形式返回.
     *
     * @param list          指定菜单记录集合.
     * @param parentId      父节点ID
     * @param includeButton 是否包含按钮菜单.
     * @return 直接子节点菜单集合
     */
    private static Menu[] getChildren(List<Menu> list, long parentId, boolean includeButton) {
        Predicate<Menu> condition;
        if (includeButton) {
            condition = menu -> menu.getParentId() == parentId;
        } else {
            condition = menu -> menu.getParentId() == parentId && menu.getType() == 0;
        }
        return list.stream().filter(condition).toArray(Menu[]::new);
    }

    /**
     * 过滤菜单信息记录集合中菜单状态标记为 <em>禁用</em> 的菜单信息记录及其子菜单.
     *
     * @param list 原始菜单信息记录集合
     * @return 过滤掉禁用菜单的记录集合
     */
    public static List<Menu> filterDisabled(List<Menu> list) {
        Queue<Menu> queue = new LinkedList<>();
        for (Menu menu : list) {
            if (menu.getStatus() == 0) {
                queue.offer(menu);
            }
        }
        while (!queue.isEmpty()) {
            Menu menu = queue.poll();
            list.remove(menu);
            list.forEach(m -> {
                if (m.getParentId().equals(menu.getId())) {
                    queue.offer(m);
                }
            });
        }
        return list;
    }

    /**
     * 接收菜单信息记录集合, 过滤出其叶子结点并返回叶子结点的 ID（由于前端可能造成溢出, 因此 ID 以字符串的形式返回）。
     *
     * @param list 菜单信息记录集合
     * @return 叶子结点 ID 集合
     */
    public static List<String> filterLeaves(List<Menu> list) {
        List<String> ids = new LinkedList<>();
        for (Menu menu : list) {
            boolean flag = true;
            for (Menu m : list) {
                if (m.getParentId().equals(menu.getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ids.add(String.valueOf(menu.getId()));
            }
        }
        return ids;
    }

}
