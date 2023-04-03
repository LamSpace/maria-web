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

import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

/**
 * 数据库迁移接口, 定义数据库迁移操作以及相关属性.
 * <br/>
 * 该接口假设所有应用使用的数据库全部均位于一个数据库实例, 即 {@link #JDBC_URL} 和 {@link #JDBC_TEMPLATE} 所指定的数据库实例.
 * 若各应用程序使用的数据库并非同一个数据库实例, 则需要将该接口提供的所有属性全部移植到各具体数据库迁移类中.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see PreSchemaMigrator
 * @see PostSchemaMigrator
 * @see MariaWebSchemaMigrator
 * @see MariaNacosSchemaMigrator
 * @see MariaZipkinSchemaMigrator
 * @see SchemaMigratorProxy
 * @since 0.0.1
 */
public interface SchemaMigrator {

    /**
     * 数据库用户名.
     */
    String USERNAME = "root";

    /**
     * 数据库密码.
     */
    String PASSWORD = "maria123";

    /**
     * 基础数据库地址, 未指定具体数据库. 由于在 compose 组合的容器内中 MySQL 数据库服务有对应的服务名，因此采用服务名作为数据库主机地址.
     */
    String JDBC_URL = "jdbc:mysql://maria-web-mysql:3306";

    /**
     * 数据库地址模板, 包含待替换数据库匹配模式. 由于在 compose 组合的容器内中 MySQL 数据库服务有对应的服务名，因此采用服务名作为数据库主机地址.
     */
    String JDBC_TEMPLATE = "jdbc:mysql://maria-web-mysql:3306/DATABASE?useUnicode=true&characterEncoding=utf-8&useSSL=true&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";

    /**
     * 数据库匹配模式.
     */
    String DATABASE_REG = "DATABASE";

    /**
     * 迁移数据库.
     *
     * @return true: 迁移数据库成功; false: 迁移数据库失败或者出现异常
     */
    boolean migrate();

    /**
     * 创建 {@code Flyway} 配置类实例.
     *
     * @param schemaLocation 数据库初始化脚本位置
     * @param dataSource     数据源配置信息
     * @return {@code Flyway} 配置类
     */
    default Configuration constructConfiguration(String schemaLocation,
                                                 DataSource dataSource) {
        ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setBaselineOnMigrate(true);
        configuration.setTable("flyway_schema_history");
        configuration.setEncoding(StandardCharsets.UTF_8);
        configuration.setOutOfOrder(true);
        configuration.setLocationsAsStrings(schemaLocation);
        configuration.setDataSource(dataSource);
        configuration.setPlaceholderReplacement(false);
        return configuration;
    }

}
