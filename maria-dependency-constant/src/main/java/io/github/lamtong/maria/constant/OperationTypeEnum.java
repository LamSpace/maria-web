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

package io.github.lamtong.maria.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志类型枚举.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public enum OperationTypeEnum {

    /**
     * 默认操作类型为无
     */
    NULL("无"),

    // 查询操作, 一般不记录查询操作
    QUERY("查询"),

    // 新增操作
    CREATE("新增"),

    // 修改操作
    UPDATE("修改"),

    // 新增或修改操作, 该操作若不存在则新增, 如存在则修改
    CREATE_UPDATE("新增/修改"),

    // 删除操作, 根据记录主键删除
    DELETE("删除"),

    // 批量删除操作, 默认根据记录主键删除
    DELETE_BATCH("批量删除");

    /**
     * 操作日志类型值
     */
    private final String value;

    OperationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "OperationTypeEnum{" +
                "value='" + value + '\'' +
                '}';
    }

    /**
     * 操作日志类型映射器.
     * <p/>
     * 通常情况下, 使用自定义注解记录操作日志行为时, 操作类型都是枚举值, 而记录的值则需要翻译成对应的文字描述信息.
     * 因此这里设计一个映射器将操作日志类型枚举值翻译成文字信息.
     *
     * @author Lam Tong
     * @version 0.0.1
     * @since 0.0.1
     */
    public static final class Mapper {
        private static final Map<String, String> MAPPING = new HashMap<>();

        static {
            for (OperationTypeEnum type : OperationTypeEnum.values()) {
                MAPPING.put(type.name(), type.getValue());
            }
        }

        private Mapper() {
        }

        /**
         * 将操作日志类型枚举值映射成对应的文字描述.
         *
         * @param operationTypeName 操作日志类型枚举值
         * @return 对应的文字描述
         */
        public static String map(String operationTypeName) {
            return MAPPING.get(operationTypeName);
        }

        /**
         * 获取操作日志类型映射器全部映射关系
         *
         * @return 操作日志类型全部映射关系
         */
        public static Map<String, String> mappings() {
            return MAPPING;
        }

    }

}
