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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.lamtong.maria.constant.DistributedLock;
import io.github.lamtong.maria.constant.OperationTypeEnum;
import io.github.lamtong.maria.constant.RequestMethodConstant;
import io.github.lamtong.maria.constant.ServiceSystem;
import io.github.lamtong.maria.domain.entity.Dictionary;
import io.github.lamtong.maria.domain.entity.DictionaryType;
import io.github.lamtong.maria.domain.response.Res;
import io.github.lamtong.maria.log.annotation.MariaLogger;
import io.github.lamtong.maria.snowflake.Generator;
import io.github.lamtong.maria.system.service.DictionaryService;
import io.github.lamtong.maria.system.service.DictionaryTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * 系统管理微服务模块字典类型和数据字典控制器, 向 Web 提供服务.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Api(value = "数据字典模块相关功能控制器", tags = {"字典类型和数据字典(Web)"})
@RestController
@RequestMapping(value = ServiceSystem.ModuleDictionary.CONTROLLER_URL)
public class WebDictionaryController {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(WebDictionaryController.class);

    @Autowired
    private DictionaryTypeService dictionaryTypeService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * 条件查询数据字典类型信息列表.
     *
     * @param cur        当前分页
     * @param size       分页大小
     * @param parameters 查询条件
     * @return 条件查询结果
     */
    @ApiOperation(value = "查询数据字典类型列表", notes = "条件查询数据字典类型信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_TYPE_LIST})
    public Res getDictionaryTypeList(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_CURRENT_PAGE, value = ServiceSystem.ModuleDictionary.PARA_CURRENT_PAGE_VALUE)
                                     @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_CURRENT_PAGE)
                                     @NotNull(message = "当前分页不能为空") Integer cur,
                                     @ApiParam(name = ServiceSystem.ModuleDictionary.PARA_PAGE_SIZE, value = ServiceSystem.ModuleDictionary.PARA_PAGE_SIZE_VALUE)
                                     @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_PAGE_SIZE)
                                     @NotNull(message = "分页大小不能为空") Integer size,
                                     @ApiParam(name = ServiceSystem.ModuleDictionary.PARA_PARAMETERS, value = ServiceSystem.ModuleDictionary.PARA_PARAMETERS_VALUE)
                                     @RequestBody
                                     @NotEmpty(message = "查询条件不能为空") Map<String, String> parameters) {
        Page<DictionaryType> page = new Page<>(cur, size);
        String dictionaryName = parameters.get("dictionaryName").trim();
        String dictionaryType = parameters.get("dictionaryType").trim();
        String remark = parameters.get("remark").trim();
        LambdaQueryWrapper<DictionaryType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(dictionaryName), DictionaryType::getDictionaryName, dictionaryName)
                .like(StringUtils.hasLength(dictionaryType), DictionaryType::getDictionaryType, dictionaryType)
                .like(StringUtils.hasLength(remark), DictionaryType::getRemark, remark);
        Page<DictionaryType> dictionaryTypePage = this.dictionaryTypeService.page(page, wrapper);
        return Res.ok()
                .message("条件查询数据字典类型信息成功!")
                .data("total", dictionaryTypePage.getTotal())
                .data("list", dictionaryTypePage.getRecords());
    }

    /**
     * 新增数据字典类型.
     *
     * @param dictionaryType 数据字典类型实体
     * @return 新增数据字典类型操作结果
     */
    @MariaLogger(value = "新增数据字典类型", type = OperationTypeEnum.CREATE)
    @PreAuthorize(value = "hasAuthority('dictionaryType@add')")
    @ApiOperation(value = "新增数据字典类型", notes = "新增数据字典类型",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_TYPE})
    public Res addDictionaryType(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_VALUE)
                                 @RequestBody
                                 @Valid DictionaryType dictionaryType) {
        if (this.dictionaryNameOrTypeExists(dictionaryType.getDictionaryName(), dictionaryType.getDictionaryType(), null)) {
            return Res.error().message("存在名称相同的类型建或者类型值, 请重新填写!");
        }
        dictionaryType.setId(Generator.nextId(DictionaryType.class));
        boolean success = this.dictionaryTypeService.save(dictionaryType);
        return success ? Res.ok().message("新增数据字典类型信息成功!") : Res.error().message("新增数据字典类型信息失败!");
    }

    /**
     * 修改数据字典类型.
     * <p/>
     * 注意: 修改数据字典类型时, 若存在与之关联的数据字典记录且数据字典类型已发生变化, 则对应的数据字典的类型同样也需要修改.
     *
     * @param dictionaryType 数据字典类型实体
     * @return 修改数据字典类型操作结果
     */
    @MariaLogger(value = "修改数据字典", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('dictionaryType@update')")
    @ApiOperation(value = "修改数据字典类型", notes = "修改数据字典类型",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.PUT, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_TYPE})
    public Res updateDictionaryType(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_VALUE)
                                    @RequestBody
                                    @Valid DictionaryType dictionaryType) {
        if (this.dictionaryNameOrTypeExists(dictionaryType.getDictionaryName(), dictionaryType.getDictionaryType(), dictionaryType.getId())) {
            return Res.error().message("存在名称相同的类型建或者类型值, 请重新填写!");
        }
        DictionaryType existingRecord = this.dictionaryTypeService.getById(dictionaryType.getId());
        if (!dictionaryType.getDictionaryType().equals(existingRecord.getDictionaryType())) {
            LambdaUpdateWrapper<Dictionary> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Dictionary::getDictionaryType, existingRecord.getDictionaryType())
                    .set(Dictionary::getDictionaryType, dictionaryType.getDictionaryType());
            this.dictionaryService.update(wrapper);
        }
        boolean success = this.dictionaryTypeService.updateById(dictionaryType);
        while (!success) {
            DictionaryType newDictionaryType = this.dictionaryTypeService.getById(dictionaryType.getId());
            dictionaryType.setVersion(newDictionaryType.getVersion());
            success = this.dictionaryTypeService.updateById(dictionaryType);
        }
        return Res.ok().message("修改数据字典类型信息成功!");
    }

    /**
     * 根据数据字典类型主键查询对应的数据字典类型信息.
     *
     * @param id 数据字典类型 ID
     * @return 查询数据字典类型操作结果
     */
    @PreAuthorize(value = "hasAuthority('dictionaryType@update')")
    @ApiOperation(value = "查询数据字典类型", notes = "根据数据字典类型 ID 查询对应的数据字典类型信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_TYPE})
    public Res getDictionaryType(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_ID, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_ID_VALUE)
                                 @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_ID)
                                 @NotNull(message = "数据字典类型ID不能为空") Long id) {
        DictionaryType dictionaryType = this.dictionaryTypeService.getById(id);
        return Res.ok()
                .message("查询数据字典类型信息成功!")
                .data("type", dictionaryType);
    }

    /**
     * 根据数据字典类型 ID 批量删除数据字典类型记录.
     * <p/>
     * 注意: 删除数据字典类型记录时需要同时删除与之关联的数据字典记录.
     *
     * @param ids 数据字典类型 ID
     * @return 删除数据字典类型操作结果
     */
    @MariaLogger(value = "批量删除数据字典类型", type = OperationTypeEnum.DELETE_BATCH)
    @PreAuthorize(value = "hasAuthority('dictionaryType@delete')")
    @ApiOperation(value = "删除数据字典类型", notes = "根据数据字典类型 ID 批量删除数据字典类型记录",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.DELETE, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_TYPE})
    public Res deleteDictionaryTypes(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_IDS, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_TYPE_IDS_VALUE)
                                     @RequestBody
                                     @NotEmpty(message = "数据字典类型ID不能为空") List<Long> ids) {
        // 删除与之关联的数据字典数据
        List<String> types = this.dictionaryTypeService.listByIds(ids)
                .stream()
                .map(DictionaryType::getDictionaryType)
                .collect(Collectors.toList());
        if (!types.isEmpty()) {
            LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Dictionary::getDictionaryType, types);
            this.dictionaryService.remove(wrapper);
        }
        boolean success = this.dictionaryTypeService.removeByIds(ids);
        return success ? Res.ok().message("删除数据字典类型信息成功!") : Res.error().message("删除数据字典类型信息失败!");
    }

    /**
     * 查询已知所有数据字典类型.
     *
     * @return 查询数据字典类型操作结果.
     */
    @ApiOperation(value = "查询数据字典类型", notes = "查询已知所有数据字典类型",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_TYPES})
    public Res getDictionaryTypes() {
        LambdaQueryWrapper<DictionaryType> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DictionaryType::getDictionaryName, DictionaryType::getDictionaryType);
        List<Map<String, Object>> list = this.dictionaryTypeService.listMaps(wrapper);
        return Res.ok()
                .message("查询数据字典类型信息成功!")
                .data("list", list);
    }

    /**
     * 条件查询数据字典信息列表.
     *
     * @param cur        当前分页
     * @param size       分页大小
     * @param parameters 查询条件
     * @return 条件查询数据字典信息操作结果
     */
    @ApiOperation(value = "查询数据字典", notes = "条件查询数据字典信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_LIST})
    public Res getDictionaryList(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_CURRENT_PAGE, value = ServiceSystem.ModuleDictionary.PARA_CURRENT_PAGE_VALUE)
                                 @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_CURRENT_PAGE)
                                 @NotNull(message = "当前分页不能为空") Integer cur,
                                 @ApiParam(name = ServiceSystem.ModuleDictionary.PARA_PAGE_SIZE, value = ServiceSystem.ModuleDictionary.PARA_PAGE_SIZE_VALUE)
                                 @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_PAGE_SIZE)
                                 @NotNull(message = "分页大小不能为空") Integer size,
                                 @ApiParam(name = ServiceSystem.ModuleDictionary.PARA_PARAMETERS, value = ServiceSystem.ModuleDictionary.PARA_PARAMETERS_VALUE)
                                 @RequestBody
                                 @NotEmpty(message = "查询条件不能为空") Map<String, String> parameters) {
        Page<Dictionary> page = new Page<>(cur, size);
        String dictionaryLabel = parameters.get("dictionaryLabel").trim();
        String dictionaryValue = parameters.get("dictionaryValue").trim();
        String dictionaryType = parameters.get("dictionaryType").trim();
        String description = parameters.get("description").trim();
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(dictionaryLabel), Dictionary::getDictionaryLabel, dictionaryLabel)
                .like(StringUtils.hasLength(dictionaryValue), Dictionary::getDictionaryValue, dictionaryValue)
                .eq(StringUtils.hasLength(dictionaryType), Dictionary::getDictionaryType, dictionaryType)
                .like(StringUtils.hasLength(description), Dictionary::getDescription, description);
        Page<Dictionary> dictionaryPage = this.dictionaryService.page(page, wrapper);
        return Res.ok()
                .message("条件查询数据字典信息成功!")
                .data("list", dictionaryPage.getRecords())
                .data("total", dictionaryPage.getTotal());
    }

    /**
     * 新增数据字典信息记录.
     * <p/>
     * 注意: 同种类型的数据字典需要自行增加排序数字, 需要使用分布式锁保证排序数字唯一.
     *
     * @param dictionary 数据字典信息实体
     * @return 添加数据字典操作结果
     */
    @MariaLogger(value = "新增数据字典", type = OperationTypeEnum.CREATE)
    @PreAuthorize(value = "hasAuthority('dictionary@add')")
    @ApiOperation(value = "新增数据字典", notes = "新增数据字典信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.POST, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY})
    public Res addDictionary(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_VALUE)
                             @RequestBody
                             @Valid Dictionary dictionary) {
        if (this.dictionaryLabelExistsWithType(dictionary.getDictionaryLabel(), dictionary.getDictionaryType(), null)) {
            return Res.error().message("该字典类型存在字典标签相同的记录, 请重新填写!");
        }

        boolean success;
        Lock lock = this.redisLockRegistry.obtain(DistributedLock.ADD_DICTIONARY);
        lock.lock();
        try {
            LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.hasLength(dictionary.getDictionaryType()), Dictionary::getDictionaryType, dictionary.getDictionaryType());
            int count = (int) this.dictionaryService.count(wrapper);
            dictionary.setOrderNum(count + 1);
            dictionary.setId(Generator.nextId(Dictionary.class));
            success = this.dictionaryService.save(dictionary);
        } finally {
            lock.unlock();
        }
        return success ? Res.ok().message("新增数据字典信息成功!") : Res.error().message("新增数据字典信息失败!");
    }

    /**
     * 根据数据字典 ID 修改对应的数据字典信息记录.
     *
     * @param dictionary 数据字典信息实体
     * @return 修改数据字典操作结果
     */
    @MariaLogger(value = "修改数据字典", type = OperationTypeEnum.UPDATE)
    @PreAuthorize(value = "hasAuthority('dictionary@update')")
    @ApiOperation(value = "修改数据字典", notes = "修改数据字典信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.PUT, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY})
    public Res updateDictionary(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_VALUE)
                                @RequestBody
                                @Valid Dictionary dictionary) {
        if (this.dictionaryLabelExistsWithType(dictionary.getDictionaryLabel(), dictionary.getDictionaryType(), dictionary.getId())) {
            return Res.error().message("该字典类型存在字典标签相同的记录, 请重新填写!");
        }
        boolean success = this.dictionaryService.updateById(dictionary);
        while (!success) {
            Dictionary newDictionary = this.dictionaryService.getById(dictionary.getId());
            dictionary.setVersion(newDictionary.getVersion());
            success = this.dictionaryService.updateById(dictionary);
        }
        return Res.ok().message("修改数据字典信息成功!");
    }

    /**
     * 根据数据字典 ID 查询对应的数据字典信息记录.
     *
     * @param id 数据字典 ID
     * @return 查询数据字典操作结果
     */
    @PreAuthorize(value = "hasAuthority('dictionary@update')")
    @ApiOperation(value = "查询数据字典", notes = "根据数据字典 ID 查询对应的数据字典信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY})
    public Res getDictionary(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_ID, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_ID_VALUE)
                             @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_ID)
                             @NotNull(message = "数据字典ID不能为空") Long id) {
        Dictionary dictionary = this.dictionaryService.getById(id);
        return Res.ok()
                .message("查询数据字典信息成功!")
                .data("dictionary", dictionary);
    }

    /**
     * 根据数据字典 ID 批量删除数据字典记录.
     *
     * @param ids 数据字典 ID
     * @return 删除数据字典操作结果
     */
    @MariaLogger(value = "批量删除数据字典", type = OperationTypeEnum.DELETE_BATCH)
    @PreAuthorize(value = "hasAuthority('dictionary@delete')")
    @DeleteMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY})
    public Res deleteDictionaries(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_IDS, value = ServiceSystem.ModuleDictionary.PARA_DICTIONARY_IDS_VALUE)
                                  @RequestBody
                                  @NotEmpty(message = "数据字典ID不能为空") List<Long> ids) {
        boolean success = this.dictionaryService.removeByIds(ids);
        return success ? Res.ok().message("删除数据字典信息成功!") : Res.error().message("删除数据字典信息失败!");
    }

    /**
     * 根据指定的数据字典类型查询数据字典信息.
     *
     * @param type 数据字典类型
     * @return 查询数据字典操作结果
     */
    @ApiOperation(value = "查询数据字典", notes = "根据数据字典类型查询对应的数据字典信息",
            tags = {"字典类型和数据字典(Web)"}, httpMethod = RequestMethodConstant.GET, response = Res.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = {ServiceSystem.ModuleDictionary.URL_DICTIONARY_LIST_BY_TYPE})
    public Res getDictionaryListByType(@ApiParam(name = ServiceSystem.ModuleDictionary.PARA_TYPE, value = ServiceSystem.ModuleDictionary.PARA_TYPE_VALUE)
                                       @RequestParam(value = ServiceSystem.ModuleDictionary.PARA_TYPE)
                                       @NotBlank(message = "数据字典类型不能为空") String type) {
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(type), Dictionary::getDictionaryType, type);
        List<Dictionary> list = this.dictionaryService.list(wrapper);
        return Res.ok()
                .message("查询数据字典信息成功!")
                .data("list", list);
    }

    /**
     * 检查数据字典类型的键或者值是否已经存在.
     *
     * @param name 数据字典类型键
     * @param type 数据字典类型值
     * @return true: 已存在; 否则返回 false
     */
    private boolean dictionaryNameOrTypeExists(String name, String type, Long id) {
        LambdaQueryWrapper<DictionaryType> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, DictionaryType::getId, id)
                .and(queryWrapper -> queryWrapper.eq(StringUtils.hasLength(name), DictionaryType::getDictionaryName, name)
                        .or(queryWrapper1 -> queryWrapper1.eq(StringUtils.hasLength(type), DictionaryType::getDictionaryType, type)));
        return this.dictionaryTypeService.count(wrapper) != 0;
    }

    /**
     * 检查特定类型数据字典的标签是否已存在.
     *
     * @param label 数据字典标签
     * @param type  数据字典类型
     * @param id    数据字典主键
     * @return true: 已存在; 否则返回 false
     */
    private boolean dictionaryLabelExistsWithType(String label, String type, Long id) {
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, Dictionary::getId, id)
                .eq(StringUtils.hasLength(type), Dictionary::getDictionaryType, type)
                .eq(StringUtils.hasLength(label), Dictionary::getDictionaryLabel, label);
        return this.dictionaryService.count(wrapper) != 0;
    }

}
