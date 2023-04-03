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

package io.github.lamtong.maria.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceLog;
import io.github.lamtong.maria.domain.entity.LogLogin;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.service.LogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * 日志管理微服务模块登录日志管理控制器, 面向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "日志管理模块登录认证日志相关功能控制器", tags = {"登录日志(Web)"})
@RestController
@RequestMapping(value = {ServiceLog.CONTROLLER_LOG_LOGIN_URL})
public class WebLogLoginController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebLogLoginController.class);

    @Autowired
    private LogLoginService logLoginService;

    /**
     * 条件查询登录认证日志信息列表并返回, 查询条件包括用户名、用户地址、登录是否成功以及登录时间.
     *
     * @param cur        当前页
     * @param size       分页大小
     * @param parameters 查询条件
     * @return 条件查询登录认证日志信息列表
     * @throws ParseException 解析时间范围字符串出错时抛出异常
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    @ApiOperation(value = "条件查询登录日志信息列表", notes = "条件查询登录认证日志信息列表并返回",
            tags = {"登录日志(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceLog.URL_LIST})
    public Res getLoginLogList(@ApiParam(name = ServiceLog.PARA_CURRENT_PAGE, value = ServiceLog.PARA_CURRENT_PAGE_VALUE)
                               @RequestParam(value = ServiceLog.PARA_CURRENT_PAGE)
                               @NotNull(message = "当前分页不能为空") Long cur,
                               @ApiParam(name = ServiceLog.PARA_PAGE_SIZE, value = ServiceLog.PARA_PAGE_SIZE_VALUE)
                               @RequestParam(value = ServiceLog.PARA_PAGE_SIZE)
                               @NotNull(message = "分页大小不能为空") Long size,
                               @ApiParam(name = ServiceLog.PARA_PARAMETERS, value = ServiceLog.PARA_PARAMETERS_VALUE)
                               @RequestBody
                               @NotEmpty(message = "查询条件不能为空") Map<String, String> parameters) throws ParseException {
        String username = parameters.get("username").trim();
        String ip = parameters.get("ip").trim();
        String success = parameters.get("success").trim();
        String start = parameters.get("start").trim();
        String end = parameters.get("end").trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Page<LogLogin> page = new Page<>(cur, size);
        LambdaQueryWrapper<LogLogin> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(username), LogLogin::getUsername, username)
                .like(StringUtils.hasLength(ip), LogLogin::getIp, ip);
        if (StringUtils.hasLength(success) && !ServiceLog.DEFAULT_SUCCESS_ALL.equals(success)) {
            wrapper.eq(LogLogin::getSuccess, Integer.parseInt(success));
        }
        if (StringUtils.hasLength(start) && StringUtils.hasLength(end)) {
            wrapper.between(LogLogin::getLoginTime, format.parse(start), format.parse(end));
        }
        Page<LogLogin> logPage = this.logLoginService.page(page, wrapper);
        return Res.ok()
                .message("条件查询登录认证日志信息成功!")
                .data("total", logPage.getTotal())
                .data("list", logPage.getRecords());
    }

    /**
     * 物理删除登录认证日志信息.
     *
     * @return 删除登录认证日志操作结果
     */
    @ApiOperation(value = "删除登录认证日志", notes = "物理删除登录认证日志信息",
            tags = {"登录日志(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = {ServiceLog.URL_DELETE_ALL_LOGS})
    public Res deleteLogs() {
        this.logLoginService.deleteLogs();
        return Res.ok()
                .message("删除登录日志成功!");
    }

}
