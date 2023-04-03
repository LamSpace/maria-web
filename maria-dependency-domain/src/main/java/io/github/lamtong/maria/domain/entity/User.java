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
 * 用户信息实体表.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "用户信息实体", description = "描述用户基本信息的实体类")
@TableName(value = "tb_user")
public final class User implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 5837549554580928875L;

    @ApiModelProperty(value = "用户ID", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true, dataType = "String", example = "administrator")
    @TableField(value = "username")
    private String username;

    @NotBlank(message = "用户昵称不能为空")
    @ApiModelProperty(value = "用户昵称", required = true, dataType = "String", example = "超级管理员")
    @TableField(value = "nickname")
    private String nickname;

    @ApiModelProperty(value = "用户密码", required = true, dataType = "String", example = "123")
    @TableField(value = "password")
    private String password;

    @NotNull(message = "用户启用状态不能为空")
    @ApiModelProperty(value = "是否启用该用户, 1表示启用, 0表示禁用", required = true, dataType = "Integer", example = "1")
    @TableField(value = "enabled")
    private Integer enabled;

    @ApiModelProperty(value = "用户账后是否未过期, 1表示未过期, 0表示已过期", required = true, dataType = "Integer", example = "1")
    @TableField(value = "accountnonexpired")
    private Integer accountnonexpired;

    @ApiModelProperty(value = "用户账号是否未锁定, 1表示未锁定, 0表示已锁定", required = true, dataType = "Integer", example = "1")
    @TableField(value = "accountnonlocked")
    private Integer accountnonlocked;

    @ApiModelProperty(value = "用户密码是否未过期, 1表示未过期, 0表示已过期", required = true, dataType = "Integer", example = "1")
    @TableField(value = "credentialsnonexpired")
    private Integer credentialsnonexpired;

    @ApiModelProperty(value = "用户类型, 0表示超级管理员, 1表示普通用户", required = true, dataType = "Integer", example = "0")
    @TableField(value = "type")
    private Integer type;

    @ApiModelProperty(value = "乐观锁标记, 标记记录版本", required = true, dataType = "Integer", example = "0")
    @Version
    @TableField(value = "version")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除标记, 1表示已删除, 0表示未删除", required = true, dataType = "Integer", example = "0")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    @ApiModelProperty(value = "用户记录创建时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "用户记录创建人员", required = true, dataType = "String", example = "administrator")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty(value = "用户记录修改时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "用户记录修改人员", required = true, dataType = "String", example = "administrator")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @ApiModelProperty(value = "用户性别, 1表示男, 0表示女", required = true, dataType = "Integer", example = "1")
    @TableField(value = "gender")
    private Integer gender;

    @ApiModelProperty(value = "用户邮箱地址", required = true, dataType = "String", example = "123@qq.com")
    @TableField(value = "email")
    private String email;

    @ApiModelProperty(value = "用户联系方式", required = true, dataType = "String", example = "18112121212")
    @TableField(value = "telephone")
    private String telephone;

    @ApiModelProperty(value = "用户出生日期", required = true, dataType = "Date", example = "2022-02-02")
    @TableField(value = "birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(value = "用户身份证号", required = true, dataType = "String", example = "510711202202021211")
    @TableField(value = "id_card")
    private String idCard;

    @ApiModelProperty(value = "用户地址", required = true, dataType = "String", example = "XX省份XX市XX区XX街道XX社区")
    @TableField(value = "address")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getAccountnonexpired() {
        return accountnonexpired;
    }

    public void setAccountnonexpired(Integer accountnonexpired) {
        this.accountnonexpired = accountnonexpired;
    }

    public Integer getAccountnonlocked() {
        return accountnonlocked;
    }

    public void setAccountnonlocked(Integer accountnonlocked) {
        this.accountnonlocked = accountnonlocked;
    }

    public Integer getCredentialsnonexpired() {
        return credentialsnonexpired;
    }

    public void setCredentialsnonexpired(Integer credentialsnonexpired) {
        this.credentialsnonexpired = credentialsnonexpired;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
                && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
                && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()))
                && (this.getAccountnonexpired() == null ? other.getAccountnonexpired() == null : this.getAccountnonexpired().equals(other.getAccountnonexpired()))
                && (this.getAccountnonlocked() == null ? other.getAccountnonlocked() == null : this.getAccountnonlocked().equals(other.getAccountnonlocked()))
                && (this.getCredentialsnonexpired() == null ? other.getCredentialsnonexpired() == null : this.getCredentialsnonexpired().equals(other.getCredentialsnonexpired()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
                && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
                && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
                && (this.getIdCard() == null ? other.getIdCard() == null : this.getIdCard().equals(other.getIdCard()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        result = prime * result + ((getAccountnonexpired() == null) ? 0 : getAccountnonexpired().hashCode());
        result = prime * result + ((getAccountnonlocked() == null) ? 0 : getAccountnonlocked().hashCode());
        result = prime * result + ((getCredentialsnonexpired() == null) ? 0 : getCredentialsnonexpired().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getIdCard() == null) ? 0 : getIdCard().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", accountnonexpired=" + accountnonexpired +
                ", accountnonlocked=" + accountnonlocked +
                ", credentialsnonexpired=" + credentialsnonexpired +
                ", type=" + type +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", updateTime=" + updateTime +
                ", updateUser='" + updateUser + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", birthday=" + birthday +
                ", idCard='" + idCard + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
