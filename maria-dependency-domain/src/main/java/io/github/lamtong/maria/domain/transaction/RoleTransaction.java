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

package io.github.lamtong.maria.domain.transaction;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色事务消息信息实体类.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "角色事务消息实体", description = "描述角色事务消息信息的实体类")
@TableName(value = "tb_role_transaction")
public final class RoleTransaction implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 3054538420466193813L;

    @ApiModelProperty(value = "角色事务消息主键", required = true, dataType = "String", example = "{UUID}")
    @TableId(value = "transaction_id", type = IdType.INPUT)
    private String transactionId;

    @ApiModelProperty(value = "角色事务消息", required = true, dataType = "String", example = "xxx")
    @TableField(value = "transaction_message")
    private String transactionMessage;

    @ApiModelProperty(value = "角色事务消息创建时间", required = true, dataType = "Date", example = "2022-02-02 02:02:02")
    @TableField(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "角色事务消息创建人员", required = true, dataType = "String", example = "xxx")
    @TableField(value = "create_user")
    private String createUser;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionMessage() {
        return transactionMessage;
    }

    public void setTransactionMessage(String transactionMessage) {
        this.transactionMessage = transactionMessage;
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
        RoleTransaction other = (RoleTransaction) that;
        return (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
                && (this.getTransactionMessage() == null ? other.getTransactionMessage() == null : this.getTransactionMessage().equals(other.getTransactionMessage()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getTransactionMessage() == null) ? 0 : getTransactionMessage().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", transactionId=" + transactionId +
                ", transactionMessage=" + transactionMessage +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }

}