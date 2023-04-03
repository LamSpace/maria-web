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

package io.github.lamtong.maria.job.jobs;

import io.github.lamtong.maria.constant.ServiceRocket;
import io.github.lamtong.maria.domain.message.MariaMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义 {@code ElasticJob-Lite} 的简单任务实现并注入到 {@code Spring IOC} 容器中.
 * <p/>
 * 由于该任务调度微服务模块是样例程序, 因此只展示 {@code ElasticJob-Lite} 的简单任务, 并不包含如下类型定时任务调度：
 * <ol>
 *     <li>数据流任务调度 {@link DataflowJob}</li>
 *     <li>SCRIPT 任务调度</li>
 *     <li>HTTP 任务调度</li>
 * </ol>
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see SimpleJob
 * @see DataflowJob
 * @see ShardingContext
 * @since 0.0.1
 */
@Component
public class MariaSimpleJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(MariaSimpleJob.class);

    @SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection", "AlibabaLowerCamelCaseVariableNaming"})
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void execute(ShardingContext shardingContext) {
        String jobName = shardingContext.getJobName();
        String shardingParameter = shardingContext.getShardingParameter();
        int shardingItem = shardingContext.getShardingItem();
        String jobParameter = shardingContext.getJobParameter();
        String taskId = shardingContext.getTaskId();
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        if (logger.isInfoEnabled()) {
            logger.info("Executing SimpleJob of ElasticJob-Lite, total = [{}], taskId = [{}], " +
                            "jobName = [{}], jobParameter = [{}], shardingItem = [{}], shardingParameter = [{}].",
                    shardingTotalCount, taskId, jobName, jobParameter, shardingItem, shardingParameter);
            MariaMessage message = new MariaMessage(String.format("任务调度示例调度任务(RocketMQ), 分片=[%d].", shardingItem));
            this.rocketMQTemplate.convertAndSend(ServiceRocket.TOPIC_ELASTIC_JOB_DEMO, message);
        }
    }

}
