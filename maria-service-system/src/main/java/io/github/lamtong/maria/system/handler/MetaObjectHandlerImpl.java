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

package io.github.lamtong.maria.system.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 自动填充实现逻辑, 自动填充记录的创建时间、创建人员、修改时间、修改人员等信息.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Component
public class MetaObjectHandlerImpl implements MetaObjectHandler {

    private static final Logger logger = LoggerFactory.getLogger(MetaObjectHandlerImpl.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        if (logger.isInfoEnabled()) {
            logger.info("Starts insert autofill.");
        }
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);

        String username = this.retrieveUser();
        this.setFieldValByName("createUser", username, metaObject);
        this.setFieldValByName("updateUser", username, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (logger.isInfoEnabled()) {
            logger.info("Starts update autofill.");
        }
        this.setFieldValByName("updateTime", new Date(), metaObject);

        String username = this.retrieveUser();
        this.setFieldValByName("updateUser", username, metaObject);
    }

    /**
     * 获取当前操作用户.
     *
     * @return 当前操作用户信息.
     */
    private String retrieveUser() {
        String username = "系统默认";
        try {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            // ignore
        }
        return username;
    }

}
