## maria-service-socket

> maria-service-socket 是 **Maria-Web** 的消息推送微服务模块，基于 **WebSocket** ，负责向前端推送消息。
> 消息来源如下：
> 1. 其他模块通过 ***OpenFeign*** 发送给 maria-service-socket 模块的消息；
> 2. 通过 ***RocketMQ*** 发送给 maria-service-socket 模块的消息，***RocketMQ*** 扮演事件总线角色;
> 3. 通过 ***Dubbo*** 发送给 maria-service-socket 模块的消息（todo）。
>
> 主要功能：
> 1. 接受各微服务模块发送至该模块的消息并推送至前端（单播或广播）。
> 2. 检测用户是否直接管理浏览器以登出。
---

> 【提示】：由于 **Maria-Web** 采用的是微服务架构, 其他模块理论上来说都应该支持分布式。
> 且 **Spring Cloud Gateway** 网关组件能够配置 **WebSocket** 的路由转发. 因此 **maria-service-socket** 模块也应该支持分布式。
>
> 通常来说, 建立 **WebSocket** 连接后服务器需要保存 javax.websocket.Session 实例以便后续向前端推送消息.
> 在单体架构下, 可以使用 **Map** 数据结构来保存用户与 javax.websocket.Session 的对应关系.
> 但在微服务架构下, 无法使用 **Redis** 的 **Hash** 类型数据来保存 javax.websocket.Session 实例。
> ### **因为 javax.websocket.Session 不支持序列化**。
>
> 因此, 各 **maria-service-socket** 模块仍然需要使用 **Map** 保存用户与 **javax.websocket.Session**
> 的对应关系且只能保存在某一个 **WebSocket** 服务上.
> 当其他模块向 **maria-service-socket** 模块发送推送消息时, 接收到的消息重新发往 **Redis** 的 **channel** 上, 各
> **WebSocket** 模块采用 **发布/订阅** 模式接收待推送消息。
> * 若消息为广播消息, 则各 **WebSocket** 服务实例均按照自身保存的对应关系推送至前端.
> * 若消息为单播消息, 则各 **WebSocket** 服务实例在接收到消息时需要检查消息的接收者是否存在与自身保存的对应关系中。
---
> 注解 **@ServerEndpoint** 标注的类在 **Spring** 容器中并非以单例形式存在, 容器在每一次建立 **WebSocket** 会话时都会初始化一个实例.
---
