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

package io.github.lamtong.maria.socket.service;

import io.github.lamtong.maria.constant.ServiceAuthentication;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.socket.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用认证中心微服务模块.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@FeignClient(value = ServiceAuthentication.SERVICE_NAME, configuration = OpenFeignConfig.class)
public interface AuthService {

    /**
     * 通知认证中心模块用户以关闭浏览器的方式退出, 认证中心删除与该用户相关的认证信息.
     *
     * @param username 指定用户名
     * @return 通知退出操作结果.
     */
    @GetMapping(value = {ServiceAuthentication.URL_LOGOUT_NOTICE_VALUE})
    Res logoutNotice(@RequestParam(value = ServiceAuthentication.PARA_USERNAME) String username);

}
