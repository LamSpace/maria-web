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
import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典信息实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "数据字典信息实体", description = "描述数据字典信息的实体类")
@TableName(value = "tb_dictionary")
public final class Dictionary implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 4737565610093053223L;

    @ApiModelProperty(value = "数据字典主键", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @NotBlank(message = "数据字典标签不能为空")
    @ApiModelProperty(value = "数据字典标签", required = true, dataType = "String", example = "xxx")
    @TableField(value = "dictionary_label")
    private String dictionaryLabel;

    @NotBlank(message = "数据字典键值不能为空")
    @ApiModelProperty(value = "数据字典键值", required = true, dataType = "String", example = "xxx")
    @TableField(value = "dictionary_value")
    private String dictionaryValue;

    @NotBlank(message = "数据字典类型不能为空")
    @ApiModelProperty(value = "数据字典类型", required = true, dataType = "String", example = "xxx")
    @TableField(value = "dictionary_type")
    private String dictionaryType;

    @ApiModelProperty(value = "数据字典排序", required = true, dataType = "Integer", example = "1")
    @TableField(value = "order_num")
    private Integer orderNum;

    @ApiModelProperty(value = "数据字典描述信息", required = true, dataType = "String", example = "xxx")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "数据字典备注信息", required = true, dataType = "String", example = "xxx")
    @TableField(value = "remark")
    private String remark;

    @ApiModelProperty(value = "数据字典前端样式, 暂不支持", dataType = "String", example = "xxx")
    @TableField(value = "css_class")
    private String cssClass;

    @ApiModelProperty(value = "是否默认, 暂不支持", dataType = "Integer", example = "0")
    @TableField(value = "is_default")
    private Integer isDefault;

    @ApiModelProperty(value = "数据字典状态", required = true, dataType = "Integer", example = "1")
    @TableField(value = "enabled")
    private Integer enabled;

    @ApiModelProperty(value = "乐观锁标记字段", required = true, dataType = "Integer", example = "0")
    @Version
    @TableField(value = "version")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标记字段", required = true, dataType = "Integer", example = "0")
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

    public String getDictionaryLabel() {
        return dictionaryLabel;
    }

    public void setDictionaryLabel(String dictionaryLabel) {
        this.dictionaryLabel = dictionaryLabel;
    }

    public String getDictionaryValue() {
        return dictionaryValue;
    }

    public void setDictionaryValue(String dictionaryValue) {
        this.dictionaryValue = dictionaryValue;
    }

    public String getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(String dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
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
        Dictionary other = (Dictionary) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getDictionaryLabel() == null ? other.getDictionaryLabel() == null : this.getDictionaryLabel().equals(other.getDictionaryLabel()))
                && (this.getDictionaryValue() == null ? other.getDictionaryValue() == null : this.getDictionaryValue().equals(other.getDictionaryValue()))
                && (this.getDictionaryType() == null ? other.getDictionaryType() == null : this.getDictionaryType().equals(other.getDictionaryType()))
                && (this.getOrderNum() == null ? other.getOrderNum() == null : this.getOrderNum().equals(other.getOrderNum()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getCssClass() == null ? other.getCssClass() == null : this.getCssClass().equals(other.getCssClass()))
                && (this.getIsDefault() == null ? other.getIsDefault() == null : this.getIsDefault().equals(other.getIsDefault()))
                && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()))
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
        result = prime * result + ((getDictionaryLabel() == null) ? 0 : getDictionaryLabel().hashCode());
        result = prime * result + ((getDictionaryValue() == null) ? 0 : getDictionaryValue().hashCode());
        result = prime * result + ((getDictionaryType() == null) ? 0 : getDictionaryType().hashCode());
        result = prime * result + ((getOrderNum() == null) ? 0 : getOrderNum().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCssClass() == null) ? 0 : getCssClass().hashCode());
        result = prime * result + ((getIsDefault() == null) ? 0 : getIsDefault().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
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
                ", dictionaryLabel=" + dictionaryLabel +
                ", dictionaryValue=" + dictionaryValue +
                ", dictionaryType=" + dictionaryType +
                ", orderNum=" + orderNum +
                ", description=" + description +
                ", remark=" + remark +
                ", cssClass=" + cssClass +
                ", isDefault=" + isDefault +
                ", enabled=" + enabled +
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
