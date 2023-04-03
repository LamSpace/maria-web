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

package io.github.lamtong.maria.domain.message;

import java.io.Serializable;

/**
 * {@code RocketMQ} 消息实体类. 主要用于各微服务模块向 {@code WebSocket} 发送消息, 进而将消息推送至前端.
 * <p/>
 * 消息总共分为两大类:
 * <ol>
 *     <li>广播消息: 无指定用户.</li>
 *     <li>单播消息: 有指定用户.</li>
 * </ol>
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class MariaMessage implements Serializable {

    private static final long serialVersionUID = -2549095182758323126L;

    private boolean broadcast;

    private String username;

    private String message;

    private MariaMessage() {
    }

    public MariaMessage(String message) {
        this(true, null, message);
    }

    public MariaMessage(String username, String message) {
        this(false, username, message);
    }

    public MariaMessage(boolean broadcast, String username, String message) {
        this.broadcast = broadcast;
        this.username = username;
        this.message = message;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MariaMessage{" +
                "broadcast=" + broadcast +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
