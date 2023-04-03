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

package io.github.lamtong.maria.log.config;

import io.github.lamtong.maria.constant.ServiceLog;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;

/**
 * {@code Kafka} 主题相关信息配置类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class KafkaTopicConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicConfig.class);

    /**
     * 默认分区数
     */
    private static final int DEFAULT_PARTITIONS = 1;

    /**
     * 默认副本数
     */
    private static final short DEFAULT_REPLICATION_FACTORY = 1;

    /**
     * 分区数
     */
    private static final int PARTITIONS = DEFAULT_PARTITIONS;

    /**
     * 副本数
     */
    private static final short REPLICATION_FACTORY = DEFAULT_REPLICATION_FACTORY;

    /**
     * 在 {@code Kafka} 消息队列中创建主题, 接收登录认证日志.
     *
     * @return 登录认证日志主题实例
     */
    @Bean
    public NewTopic logLoginTopic() {
        return new NewTopic(ServiceLog.KAFKA_TOPIC_LOG_LOGIN, PARTITIONS, REPLICATION_FACTORY);
    }

    /**
     * 在 {@code Kafka} 消息队列中创建主题, 接收操作日志.
     *
     * @return 操作日志主题实例
     */
    @Bean
    public NewTopic logOperationTopic() {
        return new NewTopic(ServiceLog.KAFKA_TOPIC_LOG_OPERATION, PARTITIONS, REPLICATION_FACTORY);
    }

    /**
     * 自定义消费者消费消息发生异常处理器, 默认打印相关信息.
     *
     * @return 消费异常处理逻辑实例
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return (message, e, consumer) -> {
            if (logger.isErrorEnabled()) {
                logger.error("Error while consuming message [{}] for consumer [{}] with cause: {}.",
                        message.getPayload(), consumer.groupMetadata().memberId(), e.getMessage());
            }
            //noinspection ConstantConditions
            return null;
        };
    }

}
