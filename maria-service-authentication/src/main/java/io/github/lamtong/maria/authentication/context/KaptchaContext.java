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

package io.github.lamtong.maria.authentication.context;

import io.github.lamtong.maria.constant.ServiceAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 校验码上下文, 保存系统登录页面的校验码和会话之间的关系.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings(value = {"SpringJavaAutowiredFieldsWarningInspection"})
@Component
public class KaptchaContext implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(KaptchaContext.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 感知会话被创建.
     *
     * @param se 会话创建事件对象
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        if (logger.isInfoEnabled()) {
            logger.info("sessionId = {} creates a new session.", sessionId);
        }
    }

    /**
     * 保存会话与验证码之间的关系.
     *
     * @param sessionId 会话ID
     * @param kaptcha   验证码
     */
    public void put(String sessionId, String kaptcha) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.put(ServiceAuthentication.REDIS_KAPTCHA_CONTAINER_KEY, sessionId, kaptcha);
        if (logger.isInfoEnabled()) {
            logger.info("sessionId = {} creates new kaptcha text [{}].", sessionId, kaptcha);
        }
    }

    /**
     * 删除会话与验证码之间的关系, 通常用于验证成功后置操作.
     *
     * @param sessionId 会话ID
     */
    public void remove(String sessionId) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.delete(ServiceAuthentication.REDIS_KAPTCHA_CONTAINER_KEY, sessionId);
        if (logger.isInfoEnabled()) {
            logger.info("sessionId = {} removes kaptcha text.", sessionId);
        }
    }

    /**
     * 检查会话与验证码的对应关系是否存在.
     *
     * @param sessionId 会话ID
     * @param kaptcha   验证码
     * @return true: 会话与验证码的对应关系存在; 否则返回 false.
     */
    public boolean contains(String sessionId, String kaptcha) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        Object o = hash.get(ServiceAuthentication.REDIS_KAPTCHA_CONTAINER_KEY, sessionId);
        if (o == null) {
            return false;
        }
        if (!(o instanceof String)) {
            return false;
        }
        String cachedKaptcha = (String) o;
        return cachedKaptcha.equals(kaptcha);
    }

}
