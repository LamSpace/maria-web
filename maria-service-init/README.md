## maria-service-init

> maria-service-init 是 **Maria-Web** 的初始化应用程序，目前主要功能如下：
> * 初始化 **Maria-Web** 微服务应用数据库。
> * 初始化 **Nacos** 注册中心数据库。
> * 初始化 **Zipkin** 依赖的数据库。
---

### 实现原理

> maria-service-init 依赖 ***Flyway*** 实现应用程序自动初始化功能，因此需要添加 ***flyway-core*** 和 ***flyway-mysql***
> 相关依赖。
> 此外初始化需要连接数据库，使用 **mybatis-plus-boot-starter** 提供的 HikariDataSource 连接数据库。
---