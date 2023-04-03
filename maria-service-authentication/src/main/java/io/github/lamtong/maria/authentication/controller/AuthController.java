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

import com.google.code.kaptcha.Producer;
import io.github.lamtong.maria.authentication.context.KaptchaContext;
import io.github.lamtong.maria.authentication.service.SystemService;
import io.github.lamtong.maria.constant.*;
import io.github.lamtong.maria.domain.response.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * 认证微服务认证相关操作控制器.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Api(value = "认证模块相关功能控制器", tags = {"认证中心(Web)"})
@RestController
@RequestMapping(value = {ServiceAuthentication.CONTROLLER_URL})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private KaptchaContext kaptchaContext;

    @Autowired
    private Producer producer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemService systemService;

    /**
     * 获取校验码. 当用户打开首页准备登录时, 为每个用户创建一个校验码并与其会话 ID 关联, 向前端返回校验码图片.
     *
     * @param request  获取校验码请求
     * @param response 获取校验码响应
     * @return 空字符串. 主要是因为 Sleuth 做链路追踪需要每个方法都有返回值， 因此这里返回一个空字符串来结束链路追踪.
     */
    @ApiOperation(value = "获取验证码", notes = "向 web 前端返回随机生成的校验码图片并写入 Response 流中",
            tags = {"认证中心(Web)"}, httpMethod = RequestMethodConstant.GET, response = String.class)
    @GetMapping(value = {ServiceAuthentication.URL_KAPTCHA})
    public String getVerifyCode(HttpServletRequest request,
                                HttpServletResponse response) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        response.setContentType(ContentTypeConstant.IMAGE_JPEG_VALUE);
        String kaptchaText = this.producer.createText();
        this.kaptchaContext.put(sessionId, kaptchaText);
        BufferedImage image = this.producer.createImage(kaptchaText);
        try (ServletOutputStream os = response.getOutputStream()) {
            ImageIO.write(image, FileSuffixConstant.JPG, os);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Exception occurs when writing image for session id [{}], exception cause: {}.",
                        sessionId, e.getLocalizedMessage());
            }
        }
        return StringConstant.BLANK;
    }

    /**
     * 校验用户登录密码, 用于首页用户锁屏解锁.
     *
     * @param userId   用户 ID
     * @param password 用户锁屏解锁时输入的密码
     * @return 锁屏解锁操作结果
     */
    @ApiOperation(value = "验证用户密码", notes = "根据用户 ID 验证其输入的密码是否正确, 用于解锁锁屏",
            tags = {"认证中心(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceAuthentication.URL_VERIFY_PASSWORD})
    public Res verifyPassword(@ApiParam(name = ServiceAuthentication.PARA_USER_ID, value = ServiceAuthentication.PARA_USER_ID_VALUE)
                              @RequestParam(value = ServiceAuthentication.PARA_USER_ID) Long userId,
                              @ApiParam(name = ServiceAuthentication.PARA_PASSWORD, value = ServiceAuthentication.PARA_PASSWORD_VALUE)
                              @RequestParam(value = ServiceAuthentication.PARA_PASSWORD) String password) {
        String pwd = this.systemService.getPasswordUserById(userId);
        return Res.ok()
                .data("equals", this.passwordEncoder.matches(password, pwd))
                .message("校验用户密码信息成功!");
    }

}
