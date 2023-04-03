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


package io.github.lamtong.maria.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.lamtong.maria.domain.entity.LogLogin;
import io.github.lamtong.maria.log.mapper.LogLoginMapper;
import io.github.lamtong.maria.log.service.LogLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录认证操作服务接口实现.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Service
public class LogLoginServiceImpl extends ServiceImpl<LogLoginMapper, LogLogin>
        implements LogLoginService {

    @SuppressWarnings(value = {"unused"})
    private static final Logger logger = LoggerFactory.getLogger(LogLoginServiceImpl.class);

    @Autowired
    private LogLoginMapper logLoginMapper;

    @Override
    public void deleteLogs() {
        this.logLoginMapper.truncateTable();
    }

}
