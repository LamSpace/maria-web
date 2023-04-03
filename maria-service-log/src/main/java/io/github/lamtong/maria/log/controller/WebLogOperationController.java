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
import io.github.lamtong.maria.constant.OperationTypeEnum;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceLog;
import io.github.lamtong.maria.domain.entity.LogOperation;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.service.LogOperationService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志管理微服务模块操作日志管理控制器, 面向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "日志管理模块操作日志相关功能控制器", tags = {"操作日志(Web)"})
@RestController
@RequestMapping(value = {ServiceLog.CONTROLLER_LOG_OPERATION_URL})
public class WebLogOperationController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebLogOperationController.class);

    @Autowired
    private LogOperationService logOperationService;

    /**
     * 获取操作日志类型名与类型值的映射关系列表.
     *
     * @return 操作日志类型名与类型值的映射关系
     */
    @ApiOperation(value = "获取操作日志类型名与类型值的映射关系", notes = "获取操作日志类型名与类型值的映射关系",
            tags = {"操作日志(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceLog.URL_OPERATION_MAPPING})
    public Res getOperationMapping() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : OperationTypeEnum.Mapper.mappings().entrySet()) {
            //noinspection AlibabaCollectionInitShouldAssignCapacity
            Map<String, String> map = new HashMap<>();
            map.put("key", entry.getKey());
            map.put("value", entry.getValue());
            list.add(map);
        }
        return Res.ok()
                .message("获取操作日志类型映射关系信息成功!")
                .data("list", list);
    }

    /**
     * 条件查询操作日志信息列表并返回.
     *
     * @param cur        当前页
     * @param size       分页大小
     * @param parameters 查询参数
     * @return 条件查询分页信息
     * @throws ParseException 解析时间范围字符串出错时抛出异常
     */
    @SuppressWarnings(value = {"DuplicatedCode"})
    @ApiOperation(value = "条件查询操作日志信息列表", notes = "条件查询操作日志信息列表并返回",
            tags = {"操作日志(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceLog.URL_LIST})
    public Res getOperationLogList(@ApiParam(name = ServiceLog.PARA_CURRENT_PAGE, value = ServiceLog.PARA_CURRENT_PAGE_VALUE)
                                   @RequestParam(value = ServiceLog.PARA_CURRENT_PAGE)
                                   @NotNull(message = "当前分页不能为空") Long cur,
                                   @ApiParam(name = ServiceLog.PARA_PAGE_SIZE, value = ServiceLog.PARA_PAGE_SIZE_VALUE)
                                   @RequestParam(value = ServiceLog.PARA_PAGE_SIZE)
                                   @NotNull(message = "分页大小不能为空") Long size,
                                   @ApiParam(name = ServiceLog.PARA_PARAMETERS, value = ServiceLog.PARA_PARAMETERS_VALUE)
                                   @RequestBody
                                   @NotEmpty(message = "查询条件不能为空") Map<String, String> parameters) throws ParseException {
        String username = parameters.get("username").trim();
        String success = parameters.get("success").trim();
        String operation = parameters.get("operation").trim();
        String userIp = parameters.get("userIp").trim();
        String start = parameters.get("start").trim();
        String end = parameters.get("end").trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Page<LogOperation> page = new Page<>(cur, size);
        LambdaQueryWrapper<LogOperation> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(username), LogOperation::getUsername, username)
                .like(StringUtils.hasLength(userIp), LogOperation::getUserIp, userIp)
                .eq(StringUtils.hasLength(operation), LogOperation::getOperation, operation);
        if (StringUtils.hasLength(success) && !ServiceLog.DEFAULT_SUCCESS_ALL.equals(success)) {
            wrapper.eq(LogOperation::getSuccess, Integer.parseInt(success));
        }
        if (StringUtils.hasLength(start) && StringUtils.hasLength(end)) {
            wrapper.between(LogOperation::getOperationTime, format.parse(start), format.parse(end));
        }
        Page<LogOperation> logPage = this.logOperationService.page(page, wrapper);
        return Res.ok()
                .message("条件查询操作日志信息成功!")
                .data("total", logPage.getTotal())
                .data("list", logPage.getRecords());
    }


    /**
     * 根据记录主键查询该记录被操作的历史痕迹.
     *
     * @param id 记录主键
     * @return 记录历史痕迹
     */
    @ApiOperation(value = "查询操作日志痕迹", notes = "根据记录主键查询记录操作根据",
            tags = {"操作日志(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceLog.URL_TRACE_BY_ID})
    public Res trace(@ApiParam(name = ServiceLog.PARA_RECORD_ID, value = ServiceLog.PARA_RECORD_ID_VALUE)
                     @RequestParam(value = ServiceLog.PARA_RECORD_ID)
                     @NotNull(message = "操作日志ID不能为空") Long id) {
        LambdaQueryWrapper<LogOperation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, LogOperation::getRecordId, id)
                .orderByAsc(LogOperation::getOperationTime);
        List<LogOperation> list = this.logOperationService.list(wrapper);
        return Res.ok()
                .message("查询操作日志痕迹成功!")
                .data("list", list);
    }

    /**
     * 物理删除操作日志信息.
     *
     * @return 删除操作日志操作结果
     */
    @ApiOperation(value = "删除操作日志", notes = "物理删除操作日志信息",
            tags = {"操作日志(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = {ServiceLog.URL_DELETE_ALL_LOGS})
    public Res deleteLogs() {
        this.logOperationService.deleteLogs();
        return Res.ok()
                .message("删除操作日志成功!");
    }

}
