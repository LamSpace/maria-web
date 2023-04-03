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
 * 系统配置信息实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "系统配置信息实体", description = "描述系统配置信息的实体类")
@TableName(value = "tb_config")
public final class Config implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 6132529492800481940L;

    @ApiModelProperty(value = "系统配置信息记录主键", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @NotBlank(message = "系统配置键不能为空")
    @ApiModelProperty(value = "系统配置键", required = true, dataType = "String", example = "xxx")
    @TableField(value = "config_key")
    private String configKey;

    @NotBlank(message = "系统配置项不能为空")
    @ApiModelProperty(value = "系统配置项名称", required = true, dataType = "String", example = "xxx")
    @TableField(value = "config_name")
    private String configName;

    @NotBlank(message = "系统配置项值不能为空")
    @ApiModelProperty(value = "系统配置项值", required = true, dataType = "String", example = "xxx")
    @TableField(value = "config_value")
    private String configValue;

    @ApiModelProperty(value = "系统配置项描述信息", required = true, dataType = "String", example = "xxx")
    @TableField(value = "config_description")
    private String configDescription;

    @ApiModelProperty(value = "乐观锁标记字段", required = true, dataType = "Integer", example = "0")
    @Version
    @TableField(value = "version")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标记字段", required = true, dataType = "Integer", example = "0")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    @NotNull(message = "系统配置项是否允许被修改不能为空")
    @ApiModelProperty(value = "系统配置记录是否允许被修改", required = true, dataType = "Integer", example = "0")
    @TableField(value = "updatable")
    private Integer updatable;

    @NotNull(message = "系统配置项是否允许被删除不能为空")
    @ApiModelProperty(value = "系统配置记录是否允许被删除", required = true, dataType = "Integer", example = "0")
    @TableField(value = "deletable")
    private Integer deletable;

    @ApiModelProperty(value = "系统配置记录备注信息", required = true, dataType = "String", example = "xxx")
    @TableField(value = "remark")
    private String remark;

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

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
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

    public Integer getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Integer updatable) {
        this.updatable = updatable;
    }

    public Integer getDeletable() {
        return deletable;
    }

    public void setDeletable(Integer deletable) {
        this.deletable = deletable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        Config other = (Config) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getConfigKey() == null ? other.getConfigKey() == null : this.getConfigKey().equals(other.getConfigKey()))
                && (this.getConfigName() == null ? other.getConfigName() == null : this.getConfigName().equals(other.getConfigName()))
                && (this.getConfigValue() == null ? other.getConfigValue() == null : this.getConfigValue().equals(other.getConfigValue()))
                && (this.getConfigDescription() == null ? other.getConfigDescription() == null : this.getConfigDescription().equals(other.getConfigDescription()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getUpdatable() == null ? other.getUpdatable() == null : this.getUpdatable().equals(other.getUpdatable()))
                && (this.getDeletable() == null ? other.getDeletable() == null : this.getDeletable().equals(other.getDeletable()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
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
        result = prime * result + ((getConfigKey() == null) ? 0 : getConfigKey().hashCode());
        result = prime * result + ((getConfigName() == null) ? 0 : getConfigName().hashCode());
        result = prime * result + ((getConfigValue() == null) ? 0 : getConfigValue().hashCode());
        result = prime * result + ((getConfigDescription() == null) ? 0 : getConfigDescription().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getUpdatable() == null) ? 0 : getUpdatable().hashCode());
        result = prime * result + ((getDeletable() == null) ? 0 : getDeletable().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
                ", configKey=" + configKey +
                ", configName=" + configName +
                ", configValue=" + configValue +
                ", configDescription=" + configDescription +
                ", version=" + version +
                ", deleted=" + deleted +
                ", updatable=" + updatable +
                ", deletable=" + deletable +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }

}
