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

package io.github.lamtong.maria.snowflake;

import io.github.lamtong.maria.domain.entity.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 雪花算法生成器, 提供统一方法生成全局唯一 ID.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Generator {

    private static final Map<Class<?>, SnowFlake> GENERATOR_HOLDER = new ConcurrentHashMap<>();

    static {
        Module[] values = Module.values();
        for (Module module : values) {
            GENERATOR_HOLDER.put(module.getClazz(), new SnowFlake(module.getId(), 1));
        }
    }

    /**
     * 根据类名生成全局唯一 ID.
     *
     * @param clazz 指定类名, 需与初始化的类名保持一致
     * @return 全局唯一 ID
     */
    public static long nextId(Class<?> clazz) {
        return GENERATOR_HOLDER.get(clazz).nextId();
    }

    enum Module {

        // 用户管理模块
        USER(0, User.class),

        // 菜单管理模块
        MENU(1, Menu.class),

        //  角色管理模块
        ROLE(2, Role.class),

        // 用户-角色管理模块
        USER_ROLE(3, UserRole.class),

        // 角色-菜单管理模块
        ROLE_MENU(4, RoleMenu.class),

        // 系统配置模块
        CONFIG(5, Config.class),

        // 数据字典类型管理模块
        DICTIONARY_TYPE(6, DictionaryType.class),

        // 数据字典管理模块
        DICTIONARY(7, Dictionary.class);

        private final long id;

        private final Class<?> clazz;

        Module(long id, Class<?> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        public long getId() {
            return id;
        }

        public Class<?> getClazz() {
            return clazz;
        }

    }

}
