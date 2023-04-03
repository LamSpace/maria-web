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

package io.github.lamtong.maria.log.annotation;

import io.github.lamtong.maria.constant.OperationTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * {@code Maria-Web} 微服务架构自定义 {@code 操作日志} 注解.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface MariaLogger {

    /**
     * 任意注解默认属性
     *
     * @return 操作日志描述
     */
    @AliasFor(value = "description")
    String value() default "";

    /**
     * 操作日志描述信息
     *
     * @return 操作日志描述信息
     */
    String description() default "";

    /**
     * 操作日志类型
     *
     * @return 操作日志类型
     */
    OperationTypeEnum type() default OperationTypeEnum.NULL;

}
