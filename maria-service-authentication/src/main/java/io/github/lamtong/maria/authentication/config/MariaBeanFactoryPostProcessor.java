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

package io.github.lamtong.maria.authentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义 {@code BeanFactoryPostProcessor}, 避免项目同时使用 {@code Feign Client} 和 {@code Spring Boot Admin} 时应用关闭显示错误告警.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Component
public class MariaBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(MariaBeanFactoryPostProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition bd1 = beanFactory.getBeanDefinition("feignContext");
        bd1.setDependsOn("registrationListener");
    }

}
