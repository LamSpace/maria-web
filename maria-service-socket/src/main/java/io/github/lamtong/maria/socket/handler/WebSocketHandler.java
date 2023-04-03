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


package io.github.lamtong.maria.socket.handler;

import com.alibaba.fastjson.JSON;
import io.github.lamtong.maria.constant.JwtConstant;
import io.github.lamtong.maria.constant.ServiceSocket;
import io.github.lamtong.maria.constant.StringConstant;
import io.github.lamtong.maria.socket.service.AuthService;
import io.github.lamtong.maria.socket.util.JwtUtil;
import io.github.lamtong.maria.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code WebSocket} 处理实现, 包括会话建立(onOpen)、会话断开(onClose)、会话异常(onError)和接收会话消息(onMessage)等.
 * <p/>
 * 注意: 由 {@link javax.websocket.server.ServerEndpoint } 标注的类在 {@code Spring} 容器下并非单例, 而是采用原型机模式为每一个
 * {@code WebSocket} 连接生成一个实例, 因此最好不要在其他地方使用 {@link org.springframework.beans.factory.annotation.Autowired}
 * 注解在其他地方引用该实例.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Component
@ServerEndpoint(value = ServiceSocket.URL_SOCKET)
public class WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private static final Map<String, Session> SESSION_CONTAINER = new ConcurrentHashMap<>();

    private static RedisTemplate<String, Object> redisTemplate;

    private static AuthService authService;

    /**
     * 向所有在线用户广播消息.
     *
     * @param key     消息键
     * @param message 消息
     */
    public static void broadcast(String key, String message) {
        //noinspection AlibabaCollectionInitShouldAssignCapacity
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", message);
        broadcast(JSON.toJSONString(map));
    }

    /**
     * 向所有在线用户广播消息.
     *
     * @param message 消息
     */
    public static void broadcast(String message) {
        SESSION_CONTAINER.forEach((k, v) -> {
            try {
                v.getBasicRemote().sendText(message);
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error while sending message [{}] to user [{}].", message, k);
                }
            }
        });
    }

    /**
     * 向指定用户发送消息.
     *
     * @param username 指定用户
     * @param message  消息
     * @return true: 向指定用户发送消息成功; 否则返回 false
     */
    public static boolean unicast(String username, String message) {
        boolean result = false;
        Session session = SESSION_CONTAINER.get(username);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
                result = true;
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error while sending message [{}] to user [{}].", message, username);
                }
            }
        }
        return result;
    }

    /**
     * 向指定用户发送消息.
     * <p/>
     * 内部封装消息键和消息并由 {@link #unicast(String, String)} 执行发送.
     *
     * @param username 指定用户
     * @param key      消息键（消息类型）
     * @param message  消息
     * @return true: 向指定用户发送消息成功; 否则返回 false
     */
    public static boolean unicast(String username, String key, String message) {
        //noinspection AlibabaCollectionInitShouldAssignCapacity
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", message);
        return unicast(username, JSON.toJSONString(map));
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        WebSocketHandler.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        WebSocketHandler.authService = authService;
    }

    /**
     * 前端与 {@code WebSocket} 服务器建立连接处理流程.
     * <ol>
     *     <li>判断建立连接时是否传递参数.</li>
     *     <li>判断建立连接时请求参数是否以 'token' 开始.</li>
     *     <li>判断传递的参数是否能够分割成长度为 2 的数组</li>
     *     <li>根据令牌信息查询起是否合法.</li>
     * </ol>
     * 若成功建立连接, 则保存用户名与 {@link javax.websocket.Session} 之间的关联关系.
     *
     * @param session {@link javax.websocket.Session} 回话实例
     */
    @OnOpen
    public void onOpen(Session session) {
        String queryString = session.getQueryString();
        // 若建立连接时未传递令牌信息, 则建立属于非法连接. 提示前端并断开连接.
        if (!StringUtils.hasLength(queryString)) {
            try {
                session.getBasicRemote().sendText("Illegal websocket session without token, and session will be closed.");
                session.close();
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error while sending text with session [{}]", session);
                }
            }
            return;
        }

        // 若建立连接时传递的参数不是以 token 开始, 则属于非法连接, 提示前端并断开连接.
        if (!this.checkTokenPrefix(queryString)) {
            try {
                session.getBasicRemote().sendText("Query string should be started with " + ServiceSocket.TOKEN_PREFIX + ". Session will be closed.");
                session.close();
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error while sending text with session [{}]", session);
                }
            }
            return;
        }

        // 若建立连接时传递参数的长度不为2, 则属于非法连接, 提示前端并断开连接.
        String[] split = queryString.split(StringConstant.EQUAL);
        if (split.length != ServiceSocket.TOKEN_LENGTH) {
            try {
                session.getBasicRemote().sendText("Length of query string should be 2, split by '=' with a key-value pair. Session will be closed.");
                session.close();
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error while sending text with session [{}]", session);
                }
            }
            return;
        }

        try {
            String username = JwtUtil.getUsername(Base64Util.decode(split[1]));
            // 检查建立连接时使用的令牌信息是否合法
            if (!this.checkToken(split[1], username)) {
                try {
                    session.getBasicRemote().sendText("Illegal token, please check. Session will be closed.");
                    session.close();
                } catch (IOException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Error while sending text with session [{}]", session);
                    }
                }
                return;
            }
            // 成功建立连接，保存用户名与 session 之间的绑定关系.
            Session existedSession = SESSION_CONTAINER.put(username, session);
            if (existedSession != null) {
                try {
                    existedSession.close();
                } catch (IOException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Error while closing existed session for user [{}].", username);
                    }
                }
            }
            if (logger.isInfoEnabled()) {
                logger.info("User [{}] log in successfully.", username);
            }
            unicast(username, "Aloha from websocket server.");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error occurred while retrieving username from token [{}].", split[1]);
            }
        }
    }

    /**
     * 用户关闭浏览器断开 {@code WebSocket} 连接时，需要做两件事:
     * <ol>
     *     <li>清除用户认证信息;</li>
     *     <li>清除 {@code WebSocket} 绑定关系.</li>
     * </ol>
     *
     * @param session {@link javax.websocket.Session} 实例
     */
    @OnClose
    public void onClose(Session session) {
        String username = this.getUsername(session);

        // 通知认证中心用户已关闭浏览器登出, 清除认证相关信息.
        authService.logoutNotice(username);

        // 删除 WebSocket 保存的用户回话与用户名的绑定关系.
        SESSION_CONTAINER.remove(username, session);
        try {
            session.close();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while closing session [{}].", session);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("User [{}] disconnected and session is closed.", username);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = this.getUsername(session);
        if (logger.isErrorEnabled()) {
            logger.error("User [{}] disconnected with error, cause: {}.", username, throwable.getMessage());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String username = this.getUsername(session);
        if (logger.isInfoEnabled()) {
            logger.info("Received message [{}] from user [{}].", message, username);
        }
    }

    /**
     * 根据 {@link javax.websocket.Session} 获取用户名.
     *
     * @param session {@link javax.websocket.Session} 实例
     * @return 用户名
     */
    private String getUsername(Session session) {
        String username = "";
        for (Map.Entry<String, Session> entry : SESSION_CONTAINER.entrySet()) {
            if (entry.getValue().equals(session)) {
                username = entry.getKey();
                break;
            }
        }
        return username;
    }

    /**
     * 检查建立 {@code WebSocket} 连接时使用的参数是否以固定字符串开始.
     *
     * @param queryString 建立 {@code WebSocket} 会话使用的请求参数
     * @return true: 以固定字符串开始; 否则返回 false
     */
    private boolean checkTokenPrefix(String queryString) {
        return queryString.startsWith(ServiceSocket.TOKEN_PREFIX);
    }

    /**
     * 检查建立 {@code WebSocket} 连接时使用的令牌是否合法.
     *
     * @param encodedToken 编码后令牌信息
     * @param username     令牌属性用户名
     * @return true: 令牌合法; 否则返回 false
     */
    private boolean checkToken(String encodedToken, String username) {
        String key = JwtConstant.CACHE_NAME.concat(StringConstant.COLON).concat(username);
        String encodeKey = Base64Util.encode(key);
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        String encodeValue = (String) operation.get(encodeKey);
        if (encodeValue == null) {
            return false;
        }
        String token = Base64Util.decode(encodeValue);
        return Base64Util.decode(encodedToken).equals(token);
    }

}
