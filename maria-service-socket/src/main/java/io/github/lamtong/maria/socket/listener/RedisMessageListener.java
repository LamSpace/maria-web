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

package io.github.lamtong.maria.socket.listener;

import com.alibaba.fastjson.JSON;
import io.github.lamtong.maria.domain.message.SocketMessage;
import io.github.lamtong.maria.socket.handler.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * {@code WebSocket Message} 监听器. 接收 {@code WebSocket} 模块待推送的消息实体并推送至前端.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Component
public class RedisMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    @Autowired
    private Jackson2JsonRedisSerializer<Object> serializer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Object deserialized = this.serializer.deserialize(message.getBody());
            SocketMessage socketMessage = JSON.parseObject(String.valueOf(deserialized), SocketMessage.class);
            if (socketMessage == null) {
                return;
            }
            switch (socketMessage.getType()) {
                case UNICAST:
                    this.processUnicastMessage(socketMessage);
                    break;
                case BROADCAST:
                    this.processBroadcastMessage(socketMessage);
                    break;
                default:
                    if (logger.isErrorEnabled()) {
                        logger.error("Error socket message type, type = [{}] with name [{}]",
                                socketMessage.getType().getType(), socketMessage.getType().getName());
                    }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while processing message [{}]", message);
            }
        }
    }

    /**
     * 单播消息处理流程.
     *
     * @param socketMessage 单播消息
     */
    private void processUnicastMessage(SocketMessage socketMessage) {
        WebSocketHandler.unicast(socketMessage.getUsername(), socketMessage.getMessage());
    }

    /**
     * 广播消息处理流程.
     *
     * @param socketMessage 广播消息
     */
    private void processBroadcastMessage(SocketMessage socketMessage) {
        WebSocketHandler.broadcast(socketMessage.getMessage());
    }

}

