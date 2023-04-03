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

package io.github.lamtong.maria.system.service.impl;

import io.github.lamtong.maria.constant.ServiceRocket;
import io.github.lamtong.maria.domain.entity.Role;
import io.github.lamtong.maria.domain.transaction.RoleTransaction;
import io.github.lamtong.maria.system.mapper.RoleTransactionMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 角色事务消息实现类. 使用 {@code RocketMQ} 的事务消息模拟实现分布式事务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Service
public class RoleTxMessageService {

    private static final Logger logger = LoggerFactory.getLogger(RoleTxMessageService.class);

    @SuppressWarnings(value = {"AlibabaLowerCamelCaseVariableNaming"})
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RoleTransactionMapper roleTransactionMapper;

    /**
     * 向 {@code RocketMQ} 发送半事务消息.
     *
     * @param role 事务消息
     */
    public void createRoleTransaction(Role role) {
        String id = UUID.randomUUID().toString();
        this.rocketMQTemplate.sendMessageInTransaction(ServiceRocket.TOPIC_TX_ROLE_MESSAGE,
                MessageBuilder.withPayload(role).setHeader(ServiceRocket.ROLE_MESSAGE_TX_ID, id).build(),
                role);
    }

    /**
     * 执行本地事务, 并更新本地事务状态.
     * <p/>
     * 使用 {@code RocketMQ} 事务消息实现分布式事务的前提是判断事务消息的唯一 ID 是否存在. 若存在则表示本地事务执行成功;
     * 否则表示本地事务执行不成功.
     *
     * @param id   事务唯一 ID
     * @param role 事务消息信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void processRole(String id, Role role) {
        // 模拟消息半事务消息信息, 可能产生事务回滚.
        if (logger.isInfoEnabled()) {
            logger.info("Emulating processing transaction message: [{}].", role);
        }
        RoleTransaction transaction = new RoleTransaction();
        transaction.setTransactionId(id);
        transaction.setTransactionMessage(role.toString());
        transaction.setCreateTime(new Date());
        transaction.setCreateUser("");
        this.roleTransactionMapper.insert(transaction);
    }

}
