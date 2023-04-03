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
 * 日志管理微服务相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ServiceLog {

    /**
     * 登录认证日志管理控制器名称
     */
    public static final String CONTROLLER_LOG_LOGIN_URL = "logLogin";

    /**
     * 操作日志管理控制器名称
     */
    public static final String CONTROLLER_LOG_OPERATION_URL = "logOperation";

    /**
     * 登录认证日志 {@code Kafka} 主题名称
     */
    public static final String KAFKA_TOPIC_LOG_LOGIN = "log.login";

    /**
     * 操作日志 {@code Kafka} 主题名称
     */
    public static final String KAFKA_TOPIC_LOG_OPERATION = "log.operation";

    /**
     * 登录认证日志消息监听器 ID
     */
    public static final String KAFKA_LISTENER_LOG_LOGIN_ID = "log.login.listener";

    /**
     * 登录认证日志消息消费者组 ID
     */
    public static final String KAFKA_LISTENER_LOG_LOGIN_GROUP = "log.login.listener.group";

    /**
     * 操作日志消息监听器 ID
     */
    public static final String KAFKA_LISTENER_LOG_OPERATION_ID = "log.operation.listener";

    /**
     * 操作日志消费者组 ID
     */
    public static final String KAFKA_LISTENER_LOG_OPERATION_GROUP = "log.operation.listener.group";

    /**
     * 从 {@code Kafka} 消息队列批量消费日志消息间隔, 单位: 秒
     */
    public static final int CONSUME_BATCH_INTERVAL_SECONDS = 10;

    /**
     * 登录操作日志是否成功默认值
     */
    public static final String DEFAULT_SUCCESS_ALL = "-1";

    /**
     * 分页查询条件之当前分页
     */
    public static final String PARA_CURRENT_PAGE = "cur";

    /**
     * 分页查询条件之当前分页
     */
    public static final String PARA_CURRENT_PAGE_VALUE = "当前分页";

    /**
     * 分页查询条件之分页大小
     */
    public static final String PARA_PAGE_SIZE = "size";

    /**
     * 分页查询条件之分页大小
     */
    public static final String PARA_PAGE_SIZE_VALUE = "分页大小";

    /**
     * 分页查询条件之查询条件
     */
    public static final String PARA_PARAMETERS = "parameters";

    /**
     * 分页查询条件之查询条件
     */
    public static final String PARA_PARAMETERS_VALUE = "查询条件";

    /**
     * 操作日志记录主键
     */
    public static final String PARA_RECORD_ID = "id";

    /**
     * 操作日志记录主键
     */
    public static final String PARA_RECORD_ID_VALUE = "记录主键";

    /**
     * 登录认证日志、操作日志和运行日志分页查询请求路径
     */
    public static final String URL_LIST = "list";

    /**
     * 查询操作日志类型映射关系请求路径
     */
    public static final String URL_OPERATION_MAPPING = "operationMap";

    /**
     * 根据记录主键ID查询记录操作痕迹请求路径
     */
    public static final String URL_TRACE_BY_ID = "trace";

    /**
     * 物理删除登录认证日志与操作日志请求路径
     */
    public static final String URL_DELETE_ALL_LOGS = "deleteAll";

    private ServiceLog() {
    }

}
