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

package io.github.lamtong.maria.init.migrate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Maria-Web} 微服务应用程序定时任务调度模块事件追踪的数据库迁移类.
 * <p/>
 * 由于 {@code ElasticJob-Lite} 支持自动初始化表结构, 因此数据库初始化模块无任何需要执行的 SQL，仅创建数据库模式名即可.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see SchemaMigrator
 * @see SchemaNameProvider
 * @since 0.0.1
 */
public class MariaJobSchemaMigrator implements SchemaMigrator, SchemaNameProvider {

    private static final Logger logger = LoggerFactory.getLogger(MariaJobSchemaMigrator.class);

    private static final String SCHEMA_NAME = "maria_job";

    @Override
    public boolean migrate() {
        if (logger.isInfoEnabled()) {
            logger.info("Migration for schema [{}] works successfully.", SCHEMA_NAME);
        }
        return true;
    }

    @Override
    public String schemaName() {
        return SCHEMA_NAME;
    }

}
