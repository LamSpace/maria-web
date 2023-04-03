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

package io.github.lamtong.maria.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义响应信息实体类, 包含操作是否成功、响应码、响应消息以及响应数据.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@ApiModel(value = "响应信息实体", description = "描述后端向前端返回响应结果的实体类")
public final class Res {

    @ApiModelProperty(value = "操作是否成功", required = true, dataType = "Boolean", example = "true")
    private boolean success;

    @ApiModelProperty(value = "操作结果响应码", required = true, dataType = "Integer", example = "200")
    private Integer code;

    @ApiModelProperty(value = "操作结果消息", required = true, dataType = "String", example = "成功")
    private String message;

    @ApiModelProperty(value = "操作返回的数据", required = true, dataType = "Map", example = "[]")
    private Map<String, Object> data = new HashMap<>();

    private Res() {
    }

    public static Res empty() {
        return new Res();
    }

    public static Res ok() {
        Res res = new Res();
        ResCode resCode = ResCode.SUCCESS;
        res.setSuccess(true);
        res.setCode(resCode.getCode());
        res.setMessage(resCode.getMessage());
        return res;
    }

    public static Res error() {
        return error(ResCode.ERROR);
    }

    public static Res error(ResCode code) {
        Res res = new Res();
        res.setSuccess(false);
        res.setCode(code.getCode());
        res.setMessage(code.getMessage());
        return res;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Res success(boolean success) {
        this.setSuccess(success);
        return this;
    }

    public Res code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Res message(String message) {
        this.setMessage(message);
        return this;
    }

    public Res data(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    public Res data(Map<String, Object> data) {
        this.getData().putAll(data);
        return this;
    }

    @Override
    public String toString() {
        return "Res{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
