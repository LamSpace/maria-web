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
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Maria-Web} 微服务应用程序依赖的服务注册中心和配置中心 {@code Nacos} 数据库迁移类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see SchemaMigrator
 * @see SchemaNameProvider
 * @since 0.0.1
 */
public class MariaNacosSchemaMigrator implements SchemaMigrator, SchemaNameProvider {

    private static final Logger logger = LoggerFactory.getLogger(MariaNacosSchemaMigrator.class);

    private static final String SCHEMA_NAME = "maria_nacos";

    private static final String DB_LOCATION = "maria/nacos";

    @SuppressWarnings(value = {"Duplicates"})
    @Override
    public boolean migrate() {
        if (logger.isInfoEnabled()) {
            logger.info("Migrating tables for schema [{}].", SCHEMA_NAME);
        }
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(JDBC_TEMPLATE.replace(DATABASE_REG, SCHEMA_NAME));
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        Configuration configuration = this.constructConfiguration(DB_LOCATION, dataSource);
        Flyway flyway = new Flyway(configuration);
        try {
            flyway.migrate();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while migrating schema [{}] with cause [{}].", SCHEMA_NAME, e.getMessage());
            }
            return false;
        } finally {
            dataSource.close();
        }
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
