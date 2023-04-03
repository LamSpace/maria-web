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

package io.github.lamtong.maria.authentication.util;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * {@code Spring Security} 工具类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * 从当前 {@link  SecurityContextHolder} 上下文中获取当前已认证用户名.
     *
     * @return 已认证用户名
     */
    public static String currentUser() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

}
