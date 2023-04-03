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

package io.github.lamtong.maria.job.listeners;

import com.alibaba.fastjson.JSON;
import org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener;
import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.apache.shardingsphere.elasticjob.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * {@code ElasticJob-Lite} 分布式任务调度作业监听器, 绑定该监听器到具体的作业上,
 * 即可在作业开始之前和结束之后执行响应逻辑.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see ElasticJobListener
 * @see AbstractDistributeOnceElasticJobListener
 * @see ShardingContexts
 * @since 0.0.1
 */
public class MariaSimpleJobListener implements ElasticJobListener {

    private static final Logger logger = LoggerFactory.getLogger(MariaSimpleJobListener.class);

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        if (logger.isInfoEnabled()) {
            logger.info("Prepare to execute simple job of ElasticJob-Lite, {}.", this.parseShardingContexts(shardingContexts));
        }
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        if (logger.isInfoEnabled()) {
            logger.info("Finish to execute simple job of ElasticJob-Lite, {}", this.parseShardingContexts(shardingContexts));
        }
    }

    @Override
    public String getType() {
        return "mariaSimpleJobListener";
    }

    /**
     * 解析 {@link ShardingContexts} 实例并生成对应的字符串, 用户日志打印.
     *
     * @param shardingContexts {@link ShardingContexts} 实例
     * @return {@link ShardingContexts} 实例对应的字符串形式
     */
    private String parseShardingContexts(ShardingContexts shardingContexts) {
        String taskId = shardingContexts.getTaskId();
        int totalCount = shardingContexts.getShardingTotalCount();
        String jobName = shardingContexts.getJobName();
        String jobParameter = shardingContexts.getJobParameter();
        Map<Integer, String> shardingItemParameters = shardingContexts.getShardingItemParameters();
        return String.format("totalCount = [%d], taskId = [%s], jobName = [%s], jobParameter = [%s], itemParameters = [%s].",
                totalCount, taskId, jobName, jobParameter, JSON.toJSONString(shardingItemParameters));
    }

}
