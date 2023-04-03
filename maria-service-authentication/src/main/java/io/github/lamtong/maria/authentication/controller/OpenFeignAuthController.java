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

package io.github.lamtong.maria.authentication.controller;

import io.github.lamtong.maria.constant.JwtConstant;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceAuthentication;
import io.github.lamtong.maria.constant.StringConstant;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.util.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证中心微服务模块控制器, 面向 OpenFeign 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "认证模块相关功能控制器", tags = {"认证中心(OpenFeign)"})
@RestController
@RequestMapping(value = {ServiceAuthentication.CONTROLLER_FEIGN_URL})
public class OpenFeignAuthController {

    private static final Logger logger = LoggerFactory.getLogger(OpenFeignAuthController.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 接收 {@code WebSocket} 推送模块发送的用户管理浏览器退出登录通知, 清除该用户已认证的相关信息.
     *
     * @param username 退出登录用户的用户名.
     * @return 接收退出登录操作结果
     */
    @ApiOperation(value = "接收退出登录", notes = "接收 WebSocket 推送模块通知指定用户关闭浏览器退出登录",
            tags = {"认证中心(OpenFeign)"}, httpMethod = RequestMethodConstant.GET, response = Res.class)
    @GetMapping(value = {ServiceAuthentication.URL_LOGOUT_NOTICE})
    public Res logoutNotice(@ApiParam(name = ServiceAuthentication.PARA_USERNAME, value = ServiceAuthentication.PARA_USERNAME_VALUE)
                            @RequestParam(value = ServiceAuthentication.PARA_USERNAME) String username) {
        String key = JwtConstant.CACHE_NAME.concat(StringConstant.COLON).concat(username);
        String encodeKey = Base64Util.encode(key);
        Boolean delete = this.redisTemplate.delete(encodeKey);
        if (logger.isInfoEnabled()) {
            logger.info("User [{}] closes browser and quits.", username);
        }
        return Boolean.TRUE.equals(delete) ? Res.ok().message("删除用户认证信息成功!") : Res.error().message("删除用户认证信息失败!");
    }

}
