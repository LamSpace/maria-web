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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志信息实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "登录认证日志信息实体类", description = "描述用户登录认证行为日志基本信息的实体类")
@TableName(value = "tb_log_login")
public final class LogLogin implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = -488091862132039604L;

    @ApiModelProperty(value = "登录日志ID", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名", required = true, dataType = "String", example = "xxx")
    @TableField(value = "username")
    private String username;

    @ApiModelProperty(value = "登录认证时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "login_time")
    private Date loginTime;

    @ApiModelProperty(value = "登录认证是否成功", required = true, dataType = "Integer", example = "1")
    @TableField(value = "success")
    private Integer success;

    @ApiModelProperty(value = "登录认证失败原因", required = true, dataType = "String", example = "密码错误")
    @TableField(value = "exception")
    private String exception;

    @ApiModelProperty(value = "用户登录认证时的远程IP地址", required = true, dataType = "String", example = "127.0.0.1")
    @TableField(value = "ip")
    private String ip;

    @ApiModelProperty(value = "用户操作时的浏览器名称", required = true, dataType = "String", example = "Chrome")
    @TableField(value = "browser_name")
    private String browserName;

    @ApiModelProperty(value = "用户操作时的浏览器类型", required = true, dataType = "String", example = "Chrome")
    @TableField(value = "browser_type")
    private String browserType;

    @ApiModelProperty(value = "用户操作时的浏览器版本", required = true, dataType = "String", example = "0.0.0.21")
    @TableField(value = "browser_version")
    private String browserVersion;

    @ApiModelProperty(value = "用户操作时的浏览厂商", required = true, dataType = "String", example = "Google")
    @TableField(value = "browser_manufacturer")
    private String browserManufacturer;

    @ApiModelProperty(value = "用户操作时的操作系统名称", required = true, dataType = "String", example = "Linux")
    @TableField(value = "os_name")
    private String osName;

    @ApiModelProperty(value = "用户操作时的操作系统厂商", required = true, dataType = "String", example = "Ubuntu Inc.")
    @TableField(value = "os_manufacturer")
    private String osManufacturer;

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

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getBrowserManufacturer() {
        return browserManufacturer;
    }

    public void setBrowserManufacturer(String browserManufacturer) {
        this.browserManufacturer = browserManufacturer;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsManufacturer() {
        return osManufacturer;
    }

    public void setOsManufacturer(String osManufacturer) {
        this.osManufacturer = osManufacturer;
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
        LogLogin other = (LogLogin) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getLoginTime() == null ? other.getLoginTime() == null : this.getLoginTime().equals(other.getLoginTime()))
                && (this.getSuccess() == null ? other.getSuccess() == null : this.getSuccess().equals(other.getSuccess()))
                && (this.getException() == null ? other.getException() == null : this.getException().equals(other.getException()))
                && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
                && (this.getBrowserName() == null ? other.getBrowserName() == null : this.getBrowserName().equals(other.getBrowserName()))
                && (this.getBrowserType() == null ? other.getBrowserType() == null : this.getBrowserType().equals(other.getBrowserType()))
                && (this.getBrowserVersion() == null ? other.getBrowserVersion() == null : this.getBrowserVersion().equals(other.getBrowserVersion()))
                && (this.getBrowserManufacturer() == null ? other.getBrowserManufacturer() == null : this.getBrowserManufacturer().equals(other.getBrowserManufacturer()))
                && (this.getOsName() == null ? other.getOsName() == null : this.getOsName().equals(other.getOsName()))
                && (this.getOsManufacturer() == null ? other.getOsManufacturer() == null : this.getOsManufacturer().equals(other.getOsManufacturer()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getLoginTime() == null) ? 0 : getLoginTime().hashCode());
        result = prime * result + ((getSuccess() == null) ? 0 : getSuccess().hashCode());
        result = prime * result + ((getException() == null) ? 0 : getException().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getBrowserName() == null) ? 0 : getBrowserName().hashCode());
        result = prime * result + ((getBrowserType() == null) ? 0 : getBrowserType().hashCode());
        result = prime * result + ((getBrowserVersion() == null) ? 0 : getBrowserVersion().hashCode());
        result = prime * result + ((getBrowserManufacturer() == null) ? 0 : getBrowserManufacturer().hashCode());
        result = prime * result + ((getOsName() == null) ? 0 : getOsName().hashCode());
        result = prime * result + ((getOsManufacturer() == null) ? 0 : getOsManufacturer().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", username=" + username +
                ", loginTime=" + loginTime +
                ", success=" + success +
                ", exception=" + exception +
                ", ip=" + ip +
                ", browserName=" + browserName +
                ", browserType=" + browserType +
                ", browserVersion=" + browserVersion +
                ", browserManufacturer=" + browserManufacturer +
                ", osName=" + osName +
                ", osManufacturer=" + osManufacturer +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }

}
