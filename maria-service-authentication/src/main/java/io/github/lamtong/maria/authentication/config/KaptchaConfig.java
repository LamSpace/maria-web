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

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import io.github.lamtong.maria.constant.KaptchaConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 校验码 {@code Kaptcha} 配置类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class KaptchaConfig {

    /**
     * 设置校验码长度、宽度、校验字符长度以及校验码字符集并返回一个校验码生成器.
     *
     * @return 校验码生成器实例.
     */
    @Bean
    public Producer producer() {
        Properties properties = new Properties();
        properties.setProperty(KaptchaConstant.IMAGE_WIDTH, KaptchaConstant.IMAGE_WIDTH_VALUE);
        properties.setProperty(KaptchaConstant.IMAGE_HEIGHT, KaptchaConstant.IMAGE_HEIGHT_VALUE);
        properties.setProperty(KaptchaConstant.TEXT_CHAR_STRING, KaptchaConstant.TEXT_CHAR_STRING_VALUE);
        properties.setProperty(KaptchaConstant.TEXT_CHAR_LENGTH, KaptchaConstant.TEXT_CHAR_LENGTH_VALUE);
        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
