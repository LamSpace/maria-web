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

package io.github.lamtong.maria.socket.controller;

import com.alibaba.fastjson.JSON;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSocket;
import io.github.lamtong.maria.domain.message.SocketMessage;
import io.github.lamtong.maria.domain.response.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code WebSocket} 模块控制器, 面向 OpenFeign 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "WebSocket 推送相关功能控制器", tags = {"WebSocket 推送(OpenFeign)"})
@RestController
@RequestMapping(value = {ServiceSocket.CONTROLLER_FEIGN_URL})
public class OpenFeignSocketController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(OpenFeignSocketController.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 以键值对的形式向指定用户发送消息.
     *
     * @param username 指定用户名
     * @param key      消息键
     * @param value    消息值
     * @return 发送消息操作结果
     */
    @ApiOperation(value = "向指定用户发送消息", notes = "以键值对的形式向指定用户发送消息",
            tags = {"WebSocket 推送(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = Res.class)
    @GetMapping(value = {ServiceSocket.URL_UNICAST_KEY_VALUE_PAIR})
    public Res unicastWithKeyValuePair(@ApiParam(name = ServiceSocket.PARA_USERNAME, value = ServiceSocket.PARA_USERNAME_VALUE)
                                       @RequestParam(value = ServiceSocket.PARA_USERNAME) String username,
                                       @ApiParam(name = ServiceSocket.PARA_KEY, value = ServiceSocket.PARA_KEY_VALUE)
                                       @RequestParam(value = ServiceSocket.PARA_KEY) String key,
                                       @ApiParam(name = ServiceSocket.PARA_VALUE, value = ServiceSocket.PARA_VALUE_VALUE)
                                       @RequestParam(value = ServiceSocket.PARA_VALUE) String value) {
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setType(SocketMessage.MessageType.UNICAST);
        socketMessage.setUsername(username);
        //noinspection AlibabaCollectionInitShouldAssignCapacity
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value);
        socketMessage.setMessage(JSON.toJSONString(map));
        this.redisTemplate.convertAndSend(ServiceSocket.MESSAGE_TOPIC, socketMessage);
        return Res.ok().message("向用户[" + username + "]发送消息成功!");
    }

    /**
     * 以字符串的形式向指定用户发送消息.
     *
     * @param username 指定用户名
     * @param message  消息
     * @return 发送消息操作结果
     */
    @ApiOperation(value = "向指定用户发送消息", notes = "以字符串的形式向指定用户发送消息",
            tags = {"WebSocket 推送(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = Res.class)
    @GetMapping(value = {ServiceSocket.URL_UNICAST_MESSAGE})
    public Res unicastWithMessage(@ApiParam(name = ServiceSocket.PARA_USERNAME, value = ServiceSocket.PARA_USERNAME_VALUE)
                                  @RequestParam(value = ServiceSocket.PARA_USERNAME) String username,
                                  @ApiParam(name = ServiceSocket.PARA_MESSAGE, value = ServiceSocket.PARA_MESSAGE_VALUE)
                                  @RequestParam(value = ServiceSocket.PARA_MESSAGE) String message) {
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setType(SocketMessage.MessageType.UNICAST);
        socketMessage.setUsername(username);
        socketMessage.setMessage(message);
        this.redisTemplate.convertAndSend(ServiceSocket.MESSAGE_TOPIC, socketMessage);
        return Res.ok().message("向用户[" + username + "]发送消息成功!");
    }

    /**
     * 以键值对的形式广播消息.
     *
     * @param key   消息键
     * @param value 消息值
     * @return 发送消息操作结果
     */
    @ApiOperation(value = "广播消息", notes = "以键值对的形式广播消息",
            tags = {"WebSocket 推送(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = Res.class)
    @GetMapping(value = {ServiceSocket.URL_BROADCAST_KEY_VALUE_PAIR})
    public Res broadcastWithKeyValuePair(@ApiParam(name = ServiceSocket.PARA_KEY, value = ServiceSocket.PARA_KEY_VALUE)
                                         @RequestParam(value = ServiceSocket.PARA_KEY) String key,
                                         @ApiParam(name = ServiceSocket.PARA_VALUE, value = ServiceSocket.PARA_VALUE_VALUE)
                                         @RequestParam(value = ServiceSocket.PARA_VALUE) String value) {
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setType(SocketMessage.MessageType.BROADCAST);
        //noinspection AlibabaCollectionInitShouldAssignCapacity
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value);
        socketMessage.setMessage(JSON.toJSONString(map));
        this.redisTemplate.convertAndSend(ServiceSocket.MESSAGE_TOPIC, socketMessage);
        return Res.ok().message("广播消息成功!");
    }

    /**
     * 以字符串的形式广播消息.
     *
     * @param message 消息
     * @return 发送消息操作结果
     */
    @ApiOperation(value = "广播消息", notes = "以字符串的形式广播消息",
            tags = {"WebSocket 推送(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = Res.class)
    @GetMapping(value = {ServiceSocket.URL_BROADCAST_MESSAGE})
    public Res broadcastWithMessage(@ApiParam(name = ServiceSocket.PARA_MESSAGE, value = ServiceSocket.PARA_MESSAGE_VALUE)
                                    @RequestParam(value = ServiceSocket.PARA_MESSAGE) String message) {
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setType(SocketMessage.MessageType.BROADCAST);
        socketMessage.setMessage(message);
        this.redisTemplate.convertAndSend(ServiceSocket.MESSAGE_TOPIC, JSON.toJSONString(socketMessage));
        return Res.ok().message("广播消息成功!");
    }

}
