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

package io.github.lamtong.maria.job.decorator;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.elasticjob.tracing.rdb.datasource.JDBCParameterDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC 数据源装饰器, 用于定义 {@code ElasticJob-Lite} 作业事件追踪持久化时使用.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see JDBCParameterDecorator
 * @see HikariDataSource
 * @since 0.0.1
 */
public class DataSourceDecorator implements JDBCParameterDecorator<HikariDataSource> {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceDecorator.class);

    @Override
    public HikariDataSource decorate(HikariDataSource dataSource) {
        if (logger.isInfoEnabled()) {
            logger.info("Using HikariDataSource to decorate datasource for tracing jobs.");
        }
        return dataSource;
    }

    /**
     * 返回 {@link HikariDataSource} 类型.
     *
     * @return {@link HikariDataSource} 类型
     */
    @Override
    public Class<HikariDataSource> getType() {
        if (logger.isInfoEnabled()) {
            logger.info("Get type of class HikariDataSource.");
        }
        return HikariDataSource.class;
    }

}
