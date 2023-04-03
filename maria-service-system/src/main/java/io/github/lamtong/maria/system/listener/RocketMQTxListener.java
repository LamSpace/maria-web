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

package io.github.lamtong.maria.system.listener;

import io.github.lamtong.maria.constant.ServiceRocket;
import io.github.lamtong.maria.domain.entity.Role;
import io.github.lamtong.maria.domain.transaction.RoleTransaction;
import io.github.lamtong.maria.system.mapper.RoleTransactionMapper;
import io.github.lamtong.maria.system.service.impl.RoleTxMessageService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 模拟事务消息监听器. 当半事务消息发送后监听事务消息, 执行本地事务并提交本地事务执行状态. 同时实现事务消息回查逻辑.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection", "AlibabaClassNamingShouldBeCamel"})
@Component
@RocketMQTransactionListener
public class RocketMQTxListener implements RocketMQLocalTransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQTxListener.class);

    @Autowired
    private RoleTxMessageService roleTxMessageService;

    @Autowired
    private RoleTransactionMapper roleTransactionMapper;

    /**
     * 执行本地事务并更新本地事务执行状态.
     *
     * @param msg 半事务消息
     * @param arg 事务消息信息
     * @return 本地事务消息执行状态
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            String id = (String) msg.getHeaders().get(ServiceRocket.ROLE_MESSAGE_TX_ID);
            Role role = (Role) arg;
            this.roleTxMessageService.processRole(id, role);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while executing local transaction with cause: [{}].", e.getMessage());
            }
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 本地事务回查实现, 根据事务消息的唯一 ID 查询对应的本地事务是否成功执行.
     *
     * @param msg 半事务消息.
     * @return 本地事务消息执行状态
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String id = (String) msg.getHeaders().get(ServiceRocket.ROLE_MESSAGE_TX_ID);
        RoleTransaction roleTransaction = this.roleTransactionMapper.selectById(id);
        if (roleTransaction != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

}
