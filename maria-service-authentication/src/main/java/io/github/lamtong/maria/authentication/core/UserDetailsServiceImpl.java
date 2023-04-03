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

package io.github.lamtong.maria.authentication.core;

import io.github.lamtong.maria.authentication.service.SystemService;
import io.github.lamtong.maria.constant.AccountConstant;
import io.github.lamtong.maria.domain.auth.UserDetailsImpl;
import io.github.lamtong.maria.domain.entity.Menu;
import io.github.lamtong.maria.domain.entity.User;
import io.github.lamtong.maria.util.MenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户认证流程, 从数据库中查询用户名以及对应的菜单权限信息并返回.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private SystemService systemService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.systemService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在, 用户名: " + username);
        }
        JwtUsernamePasswordAuthenticationFilter.setUser(user);

        UserDetailsImpl userDetails = new UserDetailsImpl();
        this.copyProperties(user, userDetails);

        List<Menu> menuList = this.loadAuthorities(username);
        JwtUsernamePasswordAuthenticationFilter.setMenus(menuList);

        List<GrantedAuthority> authorities = this.extractAuthorities(menuList);
        userDetails.setAuthorities(authorities);
        return userDetails;
    }

    /**
     * 手动复制属性。
     *
     * @param user        .
     * @param userDetails .
     */
    private void copyProperties(User user, UserDetailsImpl userDetails) {
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setAccountnonexpired(user.getAccountnonexpired());
        userDetails.setAccountnonlocked(user.getAccountnonlocked());
        userDetails.setCredentialsnonexpired(user.getCredentialsnonexpired());
        userDetails.setEnabled(user.getEnabled());
        userDetails.setId(user.getId());
    }

    /**
     * 加载用户菜单权限信息.
     *
     * @param username 指定用户名.
     * @return 指定用户的菜单权限信息
     */
    private List<Menu> loadAuthorities(String username) {
        if (AccountConstant.ADMIN.equals(username)) {
            return this.loadAdministratorAuthorities();
        }
        return this.loadUserAuthorities(username);
    }

    /**
     * 加载超级管理员的菜单权限, 默认加载所有菜单权限.
     *
     * @return 超级管理员的菜单权限
     */
    private List<Menu> loadAdministratorAuthorities() {
        return this.systemService.getAllMenus();
    }

    /**
     * 加载普通用户的菜单权限信息. 加载流程如下:
     * <ol>
     *     <li>根据用户名加载对应的角色 ID（用户微服务模块）.</li>
     *     <li>根据角色 ID 加载对应的菜单 ID（角色微服务）.</li>
     *     <li>根据菜单 ID 加载对应的菜单权限列表并返回.</li>
     * </ol>
     *
     * @param username 指定用户名
     * @return 指定用户的菜单权限信息
     */
    private List<Menu> loadUserAuthorities(String username) {
        List<Long> roleIds = this.systemService.loadRoleIdsByUsername(username);
        List<Long> menuIds = this.systemService.loadMenuIdsByRoleIds(roleIds);
        return this.systemService.getMenusByIds(menuIds);
    }

    /**
     * 从给定菜单列表中抽取权限标志并返回.
     *
     * @param list 采集列表
     * @return 权限标志列表
     */
    private List<GrantedAuthority> extractAuthorities(List<Menu> list) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] permissions = MenuUtil.extractPermissions(list);
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

}
