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
import java.util.Objects;

/**
 * {@code WebSocket} 模块接收的推送消息.
 * <p/>
 * PS: 由于这个消息类不会出现在前端, 因此该类暂时不添加 {@code Swagger} 相关注释.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class SocketMessage implements Serializable {

    private static final long serialVersionUID = -5064671205074721688L;

    private MessageType type;

    private String username;

    private String message;

    public SocketMessage() {
    }

    public SocketMessage(MessageType type, String username, String message) {
        this.type = type;
        this.username = username;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocketMessage that = (SocketMessage) o;
        return type == that.type && Objects.equals(username, that.username) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, username, message);
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     *
     */
    public enum MessageType {

        // 广播消息
        BROADCAST(0, "广播消息"),

        // 单播消息
        UNICAST(1, "单播消息");

        private int type;

        private String name;

        MessageType(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "MessageType{" +
                    "type=" + type +
                    ", name='" + name + '\'' +
                    '}';
        }

    }

}

