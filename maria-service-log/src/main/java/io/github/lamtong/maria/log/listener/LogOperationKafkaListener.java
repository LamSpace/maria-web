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

package io.github.lamtong.maria.log.listener;

import com.alibaba.fastjson.JSON;
import io.github.lamtong.maria.constant.ServiceLog;
import io.github.lamtong.maria.domain.entity.LogOperation;
import io.github.lamtong.maria.log.service.LogOperationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * {@code Kafka} 操作日志监听器, 监听并消费操作日志.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Component
public class LogOperationKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(LogOperationKafkaListener.class);

    @Autowired
    private LogOperationService logOperationService;

    /**
     * 监听 {@link ServiceLog#KAFKA_TOPIC_LOG_OPERATION} 主题并批量消费.
     *
     * @param records 批量操作日志消息
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    @KafkaListener(id = ServiceLog.KAFKA_LISTENER_LOG_OPERATION_ID, groupId = ServiceLog.KAFKA_LISTENER_LOG_OPERATION_GROUP,
            errorHandler = "consumerAwareListenerErrorHandler", topics = {ServiceLog.KAFKA_TOPIC_LOG_OPERATION})
    public void onMessage(List<ConsumerRecord<String, String>> records) {
        if (logger.isInfoEnabled()) {
            logger.info("Received message batch with size: {}.", records.size());
        }
        for (ConsumerRecord<String, String> record : records) {
            if (logger.isInfoEnabled()) {
                logger.info("Received message, topic: {}, partition: {}, offset: {}, message: {}",
                        record.topic(), record.partition(), record.offset(), record.value());
            }
            LogOperation log = JSON.parseObject(record.value(), LogOperation.class);
            this.logOperationService.save(log);
        }
        try {
            TimeUnit.SECONDS.sleep(ServiceLog.CONSUME_BATCH_INTERVAL_SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
