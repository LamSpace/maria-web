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
 * 菜单信息记录实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "菜单信息实体", description = "描述菜单基本信息的实体类")
@TableName(value = "tb_menu")
public final class Menu implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 6112820762120311459L;

    @ApiModelProperty(value = "菜单ID", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @NotNull(message = "上级菜单ID不能为空")
    @ApiModelProperty(value = "上级菜单ID", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "parent_id")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty(value = "菜单名称", required = true, dataType = "String", example = "XXX")
    @TableField(value = "menu_name")
    private String menuName;

    @ApiModelProperty(value = "菜单URL, 仅菜单类型为页面时有效", required = true, dataType = "String", example = "/user")
    @TableField(value = "url")
    private String url;

    @ApiModelProperty(value = "菜单权限标志, 仅菜单类型为按钮时有效", required = true, dataType = "String", example = "user::add")
    @TableField(value = "perms")
    private String perms;

    @NotBlank(message = "菜单图标名称不能为空")
    @ApiModelProperty(value = "菜单图标名称", required = true, dataType = "String", example = "el-icon-plus")
    @TableField(value = "icon")
    private String icon;

    @NotNull(message = "菜单类型不能为空")
    @ApiModelProperty(value = "菜单类型, 0表示页面, 1表示按钮", required = true, dataType = "Integer", example = "0")
    @TableField(value = "type")
    private Integer type;

    @ApiModelProperty(value = "菜单排序", required = true, dataType = "Integer", example = "1")
    @TableField(value = "order_num")
    private Integer orderNum;

    @NotNull(message = "菜单启动状态不能为空")
    @ApiModelProperty(value = "菜单启用状态", required = true, dataType = "Integer", example = "1")
    @TableField(value = "status")
    private Integer status;

    @ApiModelProperty(value = "乐观锁标记, 标记记录版本", required = true, dataType = "Integer", example = "0")
    @Version
    @TableField(value = "version")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标记, 1表示已删除, 0表示未删除", required = true, dataType = "Integer", example = "0")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    @ApiModelProperty(value = "菜单记录创建时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "菜单记录创建人员", required = true, dataType = "String", example = "administrator")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty(value = "菜单记录修改时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "菜单记录修改人员", required = true, dataType = "String", example = "administrator")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @ApiModelProperty(value = "菜单是否禁用", required = true, dataType = "boolean", example = "false")
    @TableField(exist = false)
    private boolean disabled;

    @ApiModelProperty(value = "子菜单集合", required = true, dataType = "Menu[]", example = "[]")
    @TableField(exist = false)
    private Menu[] children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Menu[] getChildren() {
        return children;
    }

    public void setChildren(Menu[] children) {
        this.children = children;
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
        Menu other = (Menu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
                && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getPerms() == null ? other.getPerms() == null : this.getPerms().equals(other.getPerms()))
                && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getOrderNum() == null ? other.getOrderNum() == null : this.getOrderNum().equals(other.getOrderNum()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
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
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getPerms() == null) ? 0 : getPerms().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getOrderNum() == null) ? 0 : getOrderNum().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
                ", parentId=" + parentId +
                ", menuName=" + menuName +
                ", url=" + url +
                ", perms=" + perms +
                ", icon=" + icon +
                ", type=" + type +
                ", orderNum=" + orderNum +
                ", status=" + status +
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
