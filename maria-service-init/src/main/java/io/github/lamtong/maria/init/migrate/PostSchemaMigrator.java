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
import java.util.concurrent.TimeUnit;

/**
 * 数据库迁移后置处理. 由于在 {@code Docker} 环境下数据库实例启动后 {@code Nacos} 服务始终无法连接数据库导致启动失败,
 * 因此需要手动查询 {@code Nacos} 数据库来唤醒数据库以便 {@code Nacos} 连接数据库实例.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see SchemaMigrator
 * @since 0.0.1
 */
public class PostSchemaMigrator implements SchemaMigrator {

    private static final Logger logger = LoggerFactory.getLogger(PostSchemaMigrator.class);

    private static final String SCHEMA_NAME = "maria_nacos";

    private static final int MAX_COUNT = 3;

    @SuppressWarnings(value = {"Duplicates"})
    @Override
    public boolean migrate() {
        if (logger.isInfoEnabled()) {
            logger.info("Prepare to wake up schema [{}], being ready for [Nacos].", SCHEMA_NAME);
        }
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(JDBC_TEMPLATE.replace(DATABASE_REG, SCHEMA_NAME));
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            for (int i = 0; i < MAX_COUNT; i++) {
                statement.execute("SELECT * FROM config_info");
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while executing query operation on schema [{}].", SCHEMA_NAME);
            }
            return false;
        } finally {
            dataSource.close();
        }
        if (logger.isInfoEnabled()) {
            logger.info("Operations to wake up schema [{}] works successfully.", SCHEMA_NAME);
        }
        return true;
    }

}
