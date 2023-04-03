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

package io.github.lamtong.maria.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 使用公共模块记录操作日志配置类.
 * <p/>
 * 该配置的主要作用是导入注解 {@link io.github.lamtong.maria.log.annotation.MariaLogger} 的切面处理流程.
 * 因为默认情况下, 其他模块应用在启动的时候无法扫描到该切面导致注解不生效, 因此这里需要手动导入一下.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Import(value = {io.github.lamtong.maria.log.aspect.MariaLoggerAspect.class})
@Configuration
public class LogConfig {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(LogConfig.class);

}
