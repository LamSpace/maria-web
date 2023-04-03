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
 * {@code RocketMQ} 消息队列相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ServiceRocket {

    /**
     * 示例主题
     */
    public static final String TOPIC_DEMO = "topic-demo";

    /**
     * 示例消费者组
     */
    public static final String GROUP_DEMO = "group-demo";

    /**
     * {@code ElasticJob-Lite} 分布式定时任务调度示例主题
     */
    public static final String TOPIC_ELASTIC_JOB_DEMO = "topic-elastic-job-demo";

    /**
     * {@code ElasticJob-Lite} 分布式定时任务调度示例消费者组
     */
    public static final String GROUP_ELASTIC_JOB_DEMO = "group-elastic-job-demo";

    /**
     * 模拟事务消息主题
     */
    public static final String TOPIC_TX_ROLE_MESSAGE = "topic-tx-role-message";

    /**
     * 模拟事务消息消费者组
     */
    public static final String GROUP_TX_ROLE_MESSAGE = "group-tx-role-message";

    /**
     * 模拟事务消息头
     */
    public static final String ROLE_MESSAGE_TX_ID = "role-tx-id";

    private ServiceRocket() {
    }

}
