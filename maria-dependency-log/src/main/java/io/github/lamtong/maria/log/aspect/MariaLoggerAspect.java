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

package io.github.lamtong.maria.log.aspect;

import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import io.github.lamtong.maria.constant.LogOpConstant;
import io.github.lamtong.maria.constant.OperationTypeEnum;
import io.github.lamtong.maria.constant.ServiceLog;
import io.github.lamtong.maria.constant.StringConstant;
import io.github.lamtong.maria.domain.entity.LogOperation;
import io.github.lamtong.maria.log.annotation.MariaLogger;
import io.github.lamtong.maria.util.RemoteIpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Aspect
@Component
public final class MariaLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(MariaLoggerAspect.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Pointcut(value = "@annotation(io.github.lamtong.maria.log.annotation.MariaLogger))")
    public void aspect() {
    }

    @AfterReturning(value = "aspect()")
    public void afterReturning(JoinPoint point) {
        this.log(point, null);
    }

    @AfterThrowing(value = "aspect()", throwing = "e")
    public void afterThrowing(JoinPoint point, Throwable e) {
        this.log(point, e);
    }

    /**
     * 解析并记录用户操作日志.
     *
     * @param point 切面入口
     * @param e     异常信息（如果有, 否则为 null）
     */
    @SuppressWarnings(value = {"DuplicatedCode", "AlibabaMethodTooLong"})
    private void log(JoinPoint point, Throwable e) {
        Map<String, Object> methodMetadata = this.getMethodMetadata(point);
        String username = this.getCurrentUsername();
        String methodName = this.getCurrentMethodName(point);
        String operation = (String) methodMetadata.get(LogOpConstant.OP);
        String description = (String) methodMetadata.get(LogOpConstant.DES);
        //noinspection unchecked
        Map<String, Object> parameterObjectMap = (Map<String, Object>) methodMetadata.get(LogOpConstant.PARAMETERS);
        String parameters = parameterObjectMap.toString();

        LogOperation logOperation = new LogOperation();
        Matcher matcher = Pattern.compile(LogOpConstant.ID_PATTERN).matcher(parameters);
        if (matcher.find()) {
            String s = matcher.group();
            String[] split = s.split(StringConstant.EQUAL);
            if (split.length == LogOpConstant.ID_LENGTH) {
                logOperation.setRecordId(Long.parseLong(split[1]));
            } else {
                logOperation.setRecordId(-1L);
            }
        } else {
            logOperation.setRecordId(-1L);
        }
        logOperation.setUsername(username);
        logOperation.setOperationTime(new Date());
        logOperation.setOperation(operation);
        logOperation.setMethod(methodName);
        logOperation.setDescription(description);
        logOperation.setParameters(parameters);
        if (e == null) {
            logOperation.setSuccess(1);
            logOperation.setException(StringConstant.BLANK);
        } else {
            logOperation.setSuccess(0);
            logOperation.setException(e.getMessage());
        }

        // 从 Http 请求中获取远程地址、浏览器和操作系统相关信息.
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            String remoteIp = RemoteIpUtil.getIp(request);
            logOperation.setUserIp(remoteIp);

            String agent = request.getHeader(LogOpConstant.REQUEST_HEADER_AGENT);
            UserAgent userAgent = UserAgent.parseUserAgentString(agent);
            Browser browser = userAgent.getBrowser();

            logOperation.setBrowserName(browser.getName());
            logOperation.setBrowserType(browser.getBrowserType().getName());
            logOperation.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
            logOperation.setBrowserManufacturer(browser.getManufacturer().getName());

            OperatingSystem os = userAgent.getOperatingSystem();
            logOperation.setOsName(os.getName());
            logOperation.setOsManufacturer(os.getManufacturer().getName());
        }

        // 若是批量处理操作, 则将批量操作拆分成单独的操作并将操作类型修改为对应的单独操作类型
        if (OperationTypeEnum.DELETE_BATCH.name().equals(operation)) {
            // 若是批量删除操作, 则将操作类型需改为删除且将记录拆分成多条操作日志并填入对应的记录主键。
            if (logger.isInfoEnabled()) {
                logger.info("Detected [{}] operation, convert into multiple single-operations.", OperationTypeEnum.DELETE_BATCH.name());
            }
            //noinspection unchecked
            List<Long> list = (List<Long>) parameterObjectMap.get(LogOpConstant.IDS);
            for (Long recordId : list) {
                LogOperation log = new LogOperation();

                // 拷贝原操作日志属性至新操作日志实例
                BeanUtils.copyProperties(logOperation, log);
                log.setRecordId(recordId);
                log.setOperation(OperationTypeEnum.DELETE.name());
                // 将批量操作字样去除
                log.setDescription(log.getDescription().replace("批量", ""));

                // 捕捉到用户操作并记录操作日志后, 需要将操作日志发送到 Kafka 以便日志模块后续使用
                if (logger.isInfoEnabled()) {
                    logger.info("Record operation log [{}].", log);
                }
                this.kafkaTemplate.send(ServiceLog.KAFKA_TOPIC_LOG_OPERATION, JSON.toJSONString(log));
            }
            return;
        }

        if (logger.isInfoEnabled()) {
            // 将操作日志发送到 Kafka 以便日志模块后续使用
            logger.info("Record operation log [{}].", logOperation);
        }
        this.kafkaTemplate.send(ServiceLog.KAFKA_TOPIC_LOG_OPERATION, JSON.toJSONString(logOperation));
    }

    /**
     * 获取注解标注的方法相关元数据信息.
     *
     * @param point 切面入口
     * @return 方法元数据
     */
    private Map<String, Object> getMethodMetadata(JoinPoint point) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        String operation = "";
        String description = "";
        try {
            // 注解标注的目标类
            String target = point.getTarget().getClass().getName();
            // 注解标注的方法
            String methodName = point.getSignature().getName();
            // 注解标注的方法入参
            Object[] args = point.getArgs();
            Class<?> targetClass = Class.forName(target);
            Method[] targetClassMethods = targetClass.getMethods();
            // 遍历目标类的方法, 找出当前注解标注的方法
            for (Method method : targetClassMethods) {
                if (method.getName().equals(methodName)) {
                    // 获取当前方法的形参列表（此时还无法获取形参列表名称）
                    Parameter[] parameters = method.getParameters();
                    // 若形参与实参列表长度一致, 则记录形参与入参之间的关联关系
                    if (parameters.length == args.length) {
                        // 获取形参列表参数名称
                        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
                        for (int i = 0; i < parameters.length; i++) {
                            params.put(parameterNames[i], args[i]);
                        }
                        MariaLogger mariaLogger = method.getAnnotation(MariaLogger.class);
                        operation = mariaLogger.type().name();
                        description = mariaLogger.description();
                        if (StringUtils.isEmpty(description)) {
                            description = mariaLogger.value();
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while parsing method metadata with cause [{}].", e.getMessage());
            }
        }
        map.put(LogOpConstant.PARAMETERS, params);
        map.put(LogOpConstant.OP, operation);
        map.put(LogOpConstant.DES, description);
        return map;
    }

    /**
     * 获取当前注解标注的方法全限定名.
     *
     * @param point 切面入口
     * @return 方法权限定名
     */
    private String getCurrentMethodName(JoinPoint point) {
        return point.getTarget()
                .getClass()
                .getName()
                .concat(StringConstant.DOT)
                .concat(point.getSignature().getName());
    }

    /**
     * 获取当前正在执行操作的用户名.
     *
     * @return 用户名
     */
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

}
