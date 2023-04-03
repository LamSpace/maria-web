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

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 数据库迁移前置处理, 根据需求创建对应的数据库.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see SchemaMigrator
 * @since 0.0.1
 */
public class PreSchemaMigrator implements SchemaMigrator {

    private static final Logger logger = LoggerFactory.getLogger(PreSchemaMigrator.class);

    private static final String SQL_TEMPLATE = "CREATE SCHEMA IF NOT EXISTS `DATABASE`";

    /**
     * 根据需求创建对应的数据库并关闭创建数据库时使用的数据源.
     *
     * @return true: 创建数据库成功; false: 创建数据库发生异常
     */
    @Override
    public boolean migrate() {
        if (logger.isInfoEnabled()) {
            logger.info("Prepare to create schemas before migration.");
        }
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(JDBC_URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            for (String schemaName : SchemaMigratorProxy.getSchemaNames()) {
                String sql = SQL_TEMPLATE.replace(DATABASE_REG, schemaName);
                statement.execute(sql);
                if (logger.isInfoEnabled()) {
                    logger.info("Create schema [{}] successfully.", schemaName);
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while creating schemas with cause [{}].", e.getMessage());
            }
            return false;
        } finally {
            dataSource.close();
        }
        if (logger.isInfoEnabled()) {
            logger.info("Finish creating schemas, migration will be executed.");
        }
        return true;
    }

}
