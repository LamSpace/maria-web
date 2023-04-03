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

package io.github.lamtong.maria.socket.config;

import io.github.lamtong.maria.constant.ServiceSocket;
import io.github.lamtong.maria.socket.listener.RedisMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * {@code Redis} 消息监听器相关配置类. 配置监听消费 {@code WebSocket Message}.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class RedisMessageConfig {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(RedisMessageConfig.class);

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisMessageListener redisMessageListener) {
        return new MessageListenerAdapter(redisMessageListener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer container(LettuceConnectionFactory factory,
                                                   MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic(ServiceSocket.MESSAGE_TOPIC));
        return container;
    }

}
