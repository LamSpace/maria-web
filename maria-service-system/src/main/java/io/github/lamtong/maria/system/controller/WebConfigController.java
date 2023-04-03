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

package io.github.lamtong.maria.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.lamtong.maria.constant.OperationTypeEnum;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.Config;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.annotation.MariaLogger;
import io.github.lamtong.maria.snowflake.Generator;
import io.github.lamtong.maria.system.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统管理微服务模块系统配置控制器, 面向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "系统配置模块相关功能控制器", tags = {"系统配置(Web)"})
@RestController
@RequestMapping(value = ServiceSystem.ModuleConfig.CONTROLLER_URL)
public class WebConfigController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebConfigController.class);

    @Autowired
    private ConfigService configService;

    /**
     * 条件查询系统配置信息列表并返回.
     *
     * @param cur        当前分页
     * @param size       分页大小
     * @param parameters 查询条件
     * @return Res 实例
     */
    @ApiOperation(value = "查询系统配置信息列表", notes = "条件查询系统配置信息列表",
            tags = {"系统配置(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = ServiceSystem.ModuleConfig.URL_CONFIG_LIST)
    public Res getConfigList(@ApiParam(name = ServiceSystem.ModuleConfig.PARA_CURRENT_PAGE, value = ServiceSystem.ModuleConfig.PARA_CURRENT_PAGE_VALUE)
                             @RequestParam(value = ServiceSystem.ModuleConfig.PARA_CURRENT_PAGE)
                             @NotNull(message = "当前分页不能为空") Integer cur,
                             @ApiParam(name = ServiceSystem.ModuleConfig.PARA_PAGE_SIZE, value = ServiceSystem.ModuleConfig.PARA_PAGE_SIZE_VALUE)
                             @RequestParam(value = ServiceSystem.ModuleConfig.PARA_PAGE_SIZE)
                             @NotNull(message = "分页大小不能为空") Integer size,
                             @ApiParam(name = ServiceSystem.ModuleConfig.PARA_PARAMETERS, value = ServiceSystem.ModuleConfig.PARA_PARAMETERS_VALUE)
                             @RequestBody
                             @NotEmpty(message = "查询参数不能为空") Map<String, String> parameters) {
        Page<Config> page = new Page<>(cur, size);
        String configKey = parameters.get("configKey").trim();
        String configName = parameters.get("configName").trim();
        String configDescription = parameters.get("configDescription").trim();
        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(configKey), Config::getConfigKey, configKey)
                .like(StringUtils.hasLength(configName), Config::getConfigName, configName)
                .like(StringUtils.hasLength(configDescription), Config::getConfigDescription, configDescription);
        Page<Config> configPage = this.configService.page(page, wrapper);
        return Res.ok()
                .message("条件查询系统配置信息成功!")
                .data("list", configPage.getRecords())
                .data("total", configPage.getTotal());
    }

    /**
     * 新增系统配置信息.
     *
     * @param config 系统配置信息
     * @return 新增系统配置操作结果
     */
    @MariaLogger(value = "新增系统配置", type = OperationTypeEnum.CREATE)
    @PreAuthorize(value = "hasAuthority('config@add')")
    @ApiOperation(value = "新增系统配置", notes = "新增系统配置信息",
            tags = {"系统配置(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = ServiceSystem.ModuleConfig.URL_CONFIG)
    public Res addConfig(@ApiParam(name = ServiceSystem.ModuleConfig.PARA_CONFIG, value = ServiceSystem.ModuleConfig.PARA_CONFIG_VALUE)
                         @RequestBody
                         @Valid Config config) {
        if (configKeyExists(config.getConfigKey(), null)) {
            return Res.error().message("存在配置键相同的系统配置记录, 请重新填写!");
        }
        config.setId(Generator.nextId(Config.class));
        boolean success = this.configService.save(config);
        return success ? Res.ok().message("新增系统配置信息成功!") : Res.error().message("新增系统配置信息失败!");
    }

    /**
     * 根据系统配置 ID 修改对应的系统配置.
     * <p/>
     * 注意: 修改前需要检查该配置项是否允许被修改.
     *
     * @param config 系统配置信息
     * @return 修改系统配置操作结果
     */
    @MariaLogger(value = "修改系统配置", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('config@update')")
    @ApiOperation(value = "修改系统配置", notes = "根据系统配置 ID 修改对应的系统配置信息.",
            tags = {"系统配置(Web)"}, httpMethod = RequestMethodConstant.PUT, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = ServiceSystem.ModuleConfig.URL_CONFIG)
    public Res updateConfig(@ApiParam(name = ServiceSystem.ModuleConfig.PARA_CONFIG, value = ServiceSystem.ModuleConfig.PARA_CONFIG_VALUE)
                            @RequestBody
                            @Valid Config config) {
        if (config.getUpdatable() == 0) {
            return Res.error().message("系统配置不可修改, 修改系统配置信息失败!");
        }
        if (configKeyExists(config.getConfigKey(), config.getId())) {
            return Res.error().message("存在配置键相同的系统配置记录, 请重新填写!");
        }
        boolean success = this.configService.updateById(config);
        while (!success) {
            Config newConfig = this.configService.getById(config.getId());
            config.setVersion(newConfig.getVersion());
            success = this.configService.updateById(config);
        }
        return Res.ok().message("修改系统配置信息成功!");
    }

    /**
     * 根据系统配置 ID 查询系统配置详情.
     *
     * @param id 系统配置 ID
     * @return 查询系统配置操作结果
     */
    @PreAuthorize(value = "hasAuthority('config@update')")
    @ApiOperation(value = "查询系统配置", notes = "根据系统配置 ID 查询对应的系统配置详情.",
            tags = {"系统配置(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = ServiceSystem.ModuleConfig.URL_CONFIG)
    public Res getConfig(@ApiParam(name = ServiceSystem.ModuleConfig.PARA_CONFIG_ID, value = ServiceSystem.ModuleConfig.PARA_CONFIG_ID_VALUE)
                         @RequestParam(value = ServiceSystem.ModuleConfig.PARA_CONFIG_ID)
                         @NotNull(message = "系统配置项ID不能为空") Long id) {
        Config config = this.configService.getById(id);
        return Res.ok()
                .message("查询系统配置信息成功!")
                .data("config", config);
    }

    /**
     * 根据系统配置 ID 批量删除系统配置信息.
     * <p/>
     * 注意: 删除操作仅删除可悲删除的系统配置信息, 不可删除的系统配置跳过.
     *
     * @param ids 系统配置 ID
     * @return 批量删除系统配置操作结果
     */
    @MariaLogger(value = "批量删除系统配置", type = OperationTypeEnum.DELETE_BATCH)
    @PreAuthorize(value = "hasAuthority('config@delete')")
    @ApiOperation(value = "批量删除系统配置", notes = "根据系统配置 ID 批量删除系统配置信息.",
            tags = {"系统配置(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = ServiceSystem.ModuleConfig.URL_CONFIG)
    public Res deleteConfig(@ApiParam(name = ServiceSystem.ModuleConfig.PARA_CONFIG_IDS, value = ServiceSystem.ModuleConfig.PARA_CONFIG_IDS_VALUE)
                            @RequestBody
                            @NotEmpty(message = "系统配置项ID不能为控股") List<Long> ids) {
        int sizeOld = ids.size();
        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(!ids.isEmpty(), Config::getId, ids);
        ids = this.configService.list(wrapper)
                .stream()
                .filter(config -> config.getDeletable() == 1)
                .map(Config::getId)
                .collect(Collectors.toList());
        int sizeNew = ids.size();
        boolean success = this.configService.removeByIds(ids);
        return success ? Res.ok().message(sizeOld == sizeNew ? "删除系统配置信息成功!" :
                "删除部分系统配置信息成功, " + (sizeOld - sizeNew) + "条系统配置信息未删除.") : Res.error().message("删除系统配置信息失败!");
    }

    /**
     * 根据系统配置键查询系统配置信息.
     *
     * @param configKey 系统配置键
     * @return 查询系统配置操作结果
     */
    @ApiOperation(value = "查询系统配置", notes = "根据系统配置键查询对应的系统配置信息",
            tags = {"系统配置(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = ServiceSystem.ModuleConfig.URL_GET_CONFIG)
    public Res getConfigByConfigKey(@ApiParam(name = ServiceSystem.ModuleConfig.PARA_CONFIG_KEY, value = ServiceSystem.ModuleConfig.PARA_CONFIG_KEY_VALUE)
                                    @RequestParam(value = ServiceSystem.ModuleConfig.PARA_CONFIG_KEY)
                                    @NotEmpty(message = "系统配置键不能为空") String configKey) {
        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(configKey), Config::getConfigKey, configKey);
        Config config = this.configService.getOne(wrapper);
        return Res.ok()
                .message("查询系统配置信息成功!")
                .data("config", config);
    }

    /**
     * 检查指定的系统配置键是否存在.
     *
     * @param configKey 系统配置键
     * @return true: 系统配置键不存在; 否则返回 false
     */
    private boolean configKeyExists(String configKey, Long id) {
        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, Config::getId, id)
                .eq(StringUtils.hasLength(configKey), Config::getConfigKey, configKey);
        return this.configService.count(wrapper) != 0;
    }

}
