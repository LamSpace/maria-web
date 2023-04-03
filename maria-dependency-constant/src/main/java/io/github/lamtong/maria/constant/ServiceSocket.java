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

package io.github.lamtong.maria.constant;

/**
 * 消息推送微服务模块相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ServiceSocket {

    /**
     * {@code WebSocket} 应用名称，需与 {@code spring.application.name} 保持一直
     */
    public static final String SERVICE_NAME = "maria-web-service-socket";

    /**
     * {@code WebSocket} 服务器建立连接请求路径
     */
    public static final String URL_SOCKET = "/socket";

    /**
     * {@code WebSocket} 模块接受系统内部其他模块调用请求接收控制器名称
     */
    public static final String CONTROLLER_FEIGN_URL = "feignSocket";

    /**
     * 建立 {@code WebSocket} 连接时参数的长度
     */
    public static final int TOKEN_LENGTH = 2;

    /**
     * 建立 {@code WebSocket} 连接时传递参数令牌的前缀.
     */
    public static final String TOKEN_PREFIX = "token";

    /**
     * 参数用户名
     */
    public static final String PARA_USERNAME = "username";

    /**
     * 参数用户名
     */
    public static final String PARA_USERNAME_VALUE = "用户名";

    /**
     * 参数消息键
     */
    public static final String PARA_KEY = "key";

    /**
     * 参数消息键
     */
    public static final String PARA_KEY_VALUE = "消息键";

    /**
     * 参数消息值
     */
    public static final String PARA_VALUE = "value";

    /**
     * 参数消息值
     */
    public static final String PARA_VALUE_VALUE = "消息值";

    /**
     * 参数消息
     */
    public static final String PARA_MESSAGE = "message";

    /**
     * 参数消息
     */
    public static final String PARA_MESSAGE_VALUE = "消息";

    /**
     * 以键值对的形式向指定用户发送消息请求路径
     */
    public static final String URL_UNICAST_KEY_VALUE_PAIR = "unicastPair";

    /**
     * 以键值对的形式向指定用户发送消息完整请求路径
     */
    public static final String URL_UNICAST_KEY_VALUE_PAIR_VALUE = "/feignSocket/unicastPair";

    /**
     * 以字符串的形式向指定用户发送消息请求路径
     */
    public static final String URL_UNICAST_MESSAGE = "unicastMessage";

    /**
     * 以字符串的形式向指定用户发送消息请完整求路径
     */
    public static final String URL_UNICAST_MESSAGE_VALUE = "/feignSocket/unicastMessage";

    /**
     * 以键值对的形式广播消息请求路径
     */
    public static final String URL_BROADCAST_KEY_VALUE_PAIR = "broadcastPair";

    /**
     * 以键值对的形式广播消息完整请求路径
     */
    public static final String URL_BROADCAST_KEY_VALUE_PAIR_VALUE = "/feignSocket/broadcastPair";

    /**
     * 以字符串的形式广播消息请求路径
     */
    public static final String URL_BROADCAST_MESSAGE = "broadcastMessage";

    /**
     * 以字符串的形式广播消息完整请求路径
     */
    public static final String URL_BROADCAST_MESSAGE_VALUE = "/feignSocket/broadcastMessage";

    /**
     * 采用发布/订阅模式时 {@code Channel} 的主题
     */
    public static final String MESSAGE_TOPIC = "socket.message";

    private ServiceSocket() {
    }

}
