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

package io.github.lamtong.maria.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典类型信息实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "数据字典类型信息实体", description = "描述数据字典类型信息的实体类")
@TableName(value = "tb_dictionary_type")
public final class DictionaryType implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = -3231041822950297252L;

    @ApiModelProperty(value = "数据字典类型主键", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @NotBlank(message = "数据字典类型名称不能为空")
    @ApiModelProperty(value = "数据字典类型名称", required = true, dataType = "String", example = "XXX")
    @TableField(value = "dictionary_name")
    private String dictionaryName;

    @NotBlank(message = "数据字典类型不能为空")
    @ApiModelProperty(value = "数据字典类型", required = true, dataType = "String", example = "XXX")
    @TableField(value = "dictionary_type")
    private String dictionaryType;

    @NotNull(message = "数据字典类型状态不能为空")
    @ApiModelProperty(value = "数据字典类型状态", required = true, dataType = "Integer", example = "1")
    @TableField(value = "enabled")
    private Integer enabled;

    @ApiModelProperty(value = "数据字典类型备注", required = true, dataType = "String", example = "xxx")
    @TableField(value = "remark")
    private String remark;

    @ApiModelProperty(value = "乐观锁标记字段", required = true, dataType = "Integer", example = "0")
    @Version
    @TableField(value = "version")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标记字典", required = true, dataType = "Integer", example = "0")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    @ApiModelProperty(value = "记录创建时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "记录创建人员", required = true, dataType = "String", example = "administrator")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty(value = "记录修改时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "记录修改人员", required = true, dataType = "String", example = "administrator")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public String getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(String dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        DictionaryType other = (DictionaryType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getDictionaryName() == null ? other.getDictionaryName() == null : this.getDictionaryName().equals(other.getDictionaryName()))
                && (this.getDictionaryType() == null ? other.getDictionaryType() == null : this.getDictionaryType().equals(other.getDictionaryType()))
                && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDictionaryName() == null) ? 0 : getDictionaryName().hashCode());
        result = prime * result + ((getDictionaryType() == null) ? 0 : getDictionaryType().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", dictionaryName=" + dictionaryName +
                ", dictionaryType=" + dictionaryType +
                ", enabled=" + enabled +
                ", remark=" + remark +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }

}
