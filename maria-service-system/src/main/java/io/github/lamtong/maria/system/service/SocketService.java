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

package io.github.lamtong.maria.system.service;

import io.github.lamtong.maria.constant.ServiceSocket;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.system.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用 {@code WebSocket} 推送模块, 通过 {@code WebSocket} 向前端推送提示消息.
 *
 * @author Lam Tong
 * @version 0.0.1
 */
@FeignClient(value = ServiceSocket.SERVICE_NAME, configuration = OpenFeignConfig.class)
public interface SocketService {

    /**
     * 以键值对的形式向指定用户发送消息.
     *
     * @param username 用户名
     * @param key      消息键
     * @param value    消息值
     * @return 发送消息结果
     */
    @GetMapping(value = {ServiceSocket.URL_UNICAST_KEY_VALUE_PAIR_VALUE})
    Res unicastWithKeyValuePair(@RequestParam(value = ServiceSocket.PARA_USERNAME) String username,
                                @RequestParam(value = ServiceSocket.PARA_KEY) String key,
                                @RequestParam(value = ServiceSocket.PARA_VALUE) String value);

    /**
     * 以字符串的形式向指定用户发送消息.
     *
     * @param username 指定用户名
     * @param message  消息
     * @return 发送消息操作结果
     */
    @GetMapping(value = {ServiceSocket.URL_UNICAST_MESSAGE_VALUE})
    Res unicastWithMessage(@RequestParam(value = ServiceSocket.PARA_USERNAME) String username,
                           @RequestParam(value = ServiceSocket.PARA_MESSAGE) String message);

    /**
     * 以键值对的形式广播消息.
     *
     * @param key   消息键
     * @param value 消息值
     * @return 发送消息操作结果
     */
    @GetMapping(value = {ServiceSocket.URL_BROADCAST_KEY_VALUE_PAIR_VALUE})
    Res broadcastWithKeyValuePair(@RequestParam(value = ServiceSocket.PARA_KEY) String key,
                                  @RequestParam(value = ServiceSocket.PARA_VALUE) String value);

    /**
     * 以字符串的形式广播消息.
     *
     * @param message 消息
     * @return 发送消息操作结果
     */
    @GetMapping(value = {ServiceSocket.URL_BROADCAST_MESSAGE_VALUE})
    Res broadcastWithMessage(@RequestParam(value = ServiceSocket.PARA_MESSAGE) String message);

}
