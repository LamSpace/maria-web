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

/**
 * 系统自定义注解记录用户操作行为日志相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class LogOpConstant {

    /**
     * 操作日志行为名称
     */
    public static final String OP = "OPERATION";

    /**
     * 操作日志行为描述
     */
    public static final String DES = "description";

    /**
     * 操作日志参数
     */
    public static final String PARAMETERS = "parameters";

    /**
     * 操作日志匹配记录主键
     */
    public static final String ID_PATTERN = "id=\\d+";

    /**
     * 操作日志批量操作记录主键名称
     */
    public static final String IDS = "ids";

    /**
     * 操作日志匹配记录主键长度
     */
    public static final int ID_LENGTH = 2;

    /**
     * 操作日志从请求中获取用户代理信息头部属性
     */
    public static final String REQUEST_HEADER_AGENT = "User-Agent";

    private LogOpConstant() {
    }

}
