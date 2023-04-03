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

package io.github.lamtong.maria.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 初始化应用程序模块入口.
 * <br/>
 * 初始化应用程序需要同时初始化多个数据库(Schema), 传统 <code>Flyway</code> 的使用方式依赖 {@code Spring} 的数据源配置,
 * 前提条件是必须建立数据源配置 {@code url} 对应的数据库(Schema), 否则数据源配置会报错, 因此传统的使用方式并不适合.
 * <br/>
 * 为了实现手动创建数据库并初始化, 需要在 {@code SpringBoot} 启动时排除数据源自动配置类 {@link DataSourceAutoConfiguration}
 * 和 {@code Flyway}自动配置类 {@link FlywayAutoConfiguration}.
 * <br/>
 * 由于初始化程序只在整个程序启动时才有用，程序成功启动后即可退出初始化程序, 因此设置该初始化程序在成功启动 <em>10分钟</em> 停止.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class InitApplication {

    private static final Logger logger = LoggerFactory.getLogger(InitApplication.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(InitApplication.class, args);
//        if (logger.isInfoEnabled()) {
//            logger.info("Application started successfully at [{}], and will be shut down after 10 minutes", FORMAT.format(new Date()));
//        }
//        try {
//            TimeUnit.MINUTES.sleep(10);
//        } catch (InterruptedException e) {
//            if (logger.isErrorEnabled()) {
//                logger.error("Error while sleeping to shut down application with cause [{}].", e.getMessage());
//            }
//        }
//        if (logger.isInfoEnabled()) {
//            logger.info("Prepare to shut down application at [{}].", FORMAT.format(new Date()));
//        }
//        context.close();
    }

}
