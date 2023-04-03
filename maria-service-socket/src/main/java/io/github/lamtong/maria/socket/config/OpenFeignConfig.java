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

package io.github.lamtong.maria.socket.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.lamtong.maria.constant.JwtConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * {@code WebSocket} 模块客户端配置, 发送远程调用请求时添加指定属性.
 * <p/>
 * PS: 可能会用到.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class OpenFeignConfig implements RequestInterceptor {

    @SuppressWarnings(value = "unused")
    private static final Logger logger = LoggerFactory.getLogger(OpenFeignConfig.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 这里与其他地方的配置形式稍有不同, 主要是因为 WebSocket 推送模块发起的调用有可能不是接收来自前端的调用请求, 并不会携带令牌信息.
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            requestTemplate.header(JwtConstant.TOKEN_HEADER, request.getHeader(JwtConstant.TOKEN_HEADER));
        }
    }

}
