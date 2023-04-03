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
 * 操作日志信息实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "操作日志信息实体类", description = "描述用户操作行为日志基本信息的实体类")
@TableName(value = "tb_log_operation")
public final class LogOperation implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = -7468887652562129002L;

    @ApiModelProperty(value = "操作日志ID", required = true, dataType = "Long", example = "1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作记录主键", required = true, dataType = "Long", example = "-1L")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "record_id")
    private Long recordId;

    @ApiModelProperty(value = "操作人员用户名", required = true, dataType = "String", example = "xxx")
    @TableField(value = "username")
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "操作时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @TableField(value = "operation_time")
    private Date operationTime;

    @ApiModelProperty(value = "用户操作行为", required = true, dataType = "String", example = "xxx")
    @TableField(value = "operation")
    private String operation;

    @ApiModelProperty(value = "用户操作执行方法", required = true, dataType = "String", example = "com.xxx.hello")
    @TableField(value = "method")
    private String method;

    @ApiModelProperty(value = "用户操作执行参数", required = true, dataType = "String", example = "{}")
    @TableField(value = "parameters")
    private String parameters;

    @ApiModelProperty(value = "用户操作行为描述", required = true, dataType = "String", example = "xxx")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "用户操作是否成功", required = true, dataType = "Integer", example = "1")
    @TableField(value = "success")
    private Integer success;

    @ApiModelProperty(value = "用户操作行为异常原因", required = true, dataType = "String", example = "xxx")
    @TableField(value = "exception")
    private String exception;

    @ApiModelProperty(value = "用户操作时的远程IP地址", required = true, dataType = "String", example = "127.0.0.1")
    @TableField(value = "user_ip")
    private String userIp;

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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
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
        LogOperation other = (LogOperation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getRecordId() == null ? other.getRecordId() == null : this.getRecordId().equals(other.getRecordId()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getOperationTime() == null ? other.getOperationTime() == null : this.getOperationTime().equals(other.getOperationTime()))
                && (this.getOperation() == null ? other.getOperation() == null : this.getOperation().equals(other.getOperation()))
                && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
                && (this.getParameters() == null ? other.getParameters() == null : this.getParameters().equals(other.getParameters()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getSuccess() == null ? other.getSuccess() == null : this.getSuccess().equals(other.getSuccess()))
                && (this.getException() == null ? other.getException() == null : this.getException().equals(other.getException()))
                && (this.getUserIp() == null ? other.getUserIp() == null : this.getUserIp().equals(other.getUserIp()))
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
        result = prime * result + ((getRecordId() == null) ? 0 : getRecordId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getOperationTime() == null) ? 0 : getOperationTime().hashCode());
        result = prime * result + ((getOperation() == null) ? 0 : getOperation().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getParameters() == null) ? 0 : getParameters().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getSuccess() == null) ? 0 : getSuccess().hashCode());
        result = prime * result + ((getException() == null) ? 0 : getException().hashCode());
        result = prime * result + ((getUserIp() == null) ? 0 : getUserIp().hashCode());
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
                ", recordId=" + recordId +
                ", username=" + username +
                ", operationTime=" + operationTime +
                ", operation=" + operation +
                ", method=" + method +
                ", parameters=" + parameters +
                ", description=" + description +
                ", success=" + success +
                ", exception=" + exception +
                ", userIp=" + userIp +
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
