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
 * 分布式锁相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class DistributedLock {

    /**
     * 使用 {@code Redis} 实现的分布式锁
     */
    public static final String TYPE_REDIS = "redisLock";

    /**
     * 使用 {@code Zookeeper} 实现的分布式锁
     */
    public static final String TYPE_ZOOKEEPER = "zookeeperLock";

    /**
     * 添加菜单时使用的分布式名称
     */
    public static final String ADD_MENU = "addMenu";

    /**
     * 添加数据字典时使用的分布式锁
     */
    public static final String ADD_DICTIONARY = "addDictionary";

    private DistributedLock() {
    }

}
