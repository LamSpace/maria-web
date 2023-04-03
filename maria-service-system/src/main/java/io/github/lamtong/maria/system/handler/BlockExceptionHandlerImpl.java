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

package io.github.lamtong.maria.system.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import io.github.lamtong.maria.constant.CharSetConstant;
import io.github.lamtong.maria.domain.response.Res;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义 {@code Sentinel} 限流异常处理.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@Component
public class BlockExceptionHandlerImpl implements BlockExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(BlockExceptionHandlerImpl.class);

    /**
     * 自定义 {@code sentinel} 限流异常信息处理流程, 向前端返回 {@code Json} 形式的异常信息.
     *
     * @param request  Servlet request 请求实例
     * @param response Servlet response 响应实例
     * @param e        the block exception sentinel 阻塞异常
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       BlockException e) {
        response.setCharacterEncoding(CharSetConstant.UTF8);
        if (logger.isInfoEnabled()) {
            logger.info("Sentinel Blocked...");
        }

        Res res = Res.error();
        if (e instanceof FlowException) {
            res.message("发生限流异常, 请稍后再试!");
        } else if (e instanceof DegradeException) {
            res.message("发生降级异常, 请确保被调服务正常运行!");
        } else if (e instanceof ParamFlowException) {
            res.message("发生热点参数异常, 请稍后再试!");
        } else if (e instanceof AuthorityException) {
            res.message("发生授权异常, 请确保调用过程权限正常!");
        } else if (e instanceof SystemBlockException) {
            res.message("发生系统负载保护异常, 请稍后再试!");
        } else {
            res.message("发生未知种类异常!");
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(res));
        } catch (IOException ioException) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while writing blocked response, cause: {}.", ioException.getMessage());
            }
        }
    }

}
