## Maria-Web

### 项目介绍

**Maria-Web** 是基于 **Spring Cloud Alibaba** 搭建的微服务应用，主要包含以下模块：

* **maria-service-dashboard**：基于 **Spring Actuator** 搭建的应用。
    * 监控各微服务模块运行状态。
* **maria-service-gateway**：网关微服务模块。
    * 转发前端请求至各微服务模块。
    * 接受服务端响应并返回值前端。
* **maria-service-authentication**：认证中心微服务模块。
    * 认证用户身份。
    * 授权。
* **maria-service-system**：系统管理微服务模块。
    * 用户管理。
    * 角色管理。
    * 菜单管理。
    * 系统配置管理。
    * 字典类型管理。
    * 数据字典管理。
* **maria-service-socket**：基于 **WebSocket** 搭建的应用。
    * 推送通知消息。
* **maria-service-job**：基于 **ElasticJob-Lite** 搭建的弹性分布式调度应用。
    * 配置并执行任务调用。
* **maria-service-log**：日志管理微服务模块。
    * 登录日志管理。
    * 操作日志管理。
    * 运行日志管理。
* **maria-ui**：基于 **Vue2.x** 搭建的 **SPA** 模块，部署于 Nginx 服务器，实现反向代理与负载均衡。
    * 系统展示。
    * 接受请求并转发至网关。
    * 返回系统响应结果。

---

### 模块命名规范

**Maria-Web** 项目各模块搭建原则如下：

* 所有微服务模块均以 **maria-service-** 开头。
* 所有微服务模块依赖均以 **maria-dependency-** 开头。

---

### 依赖添加规范

**Maria-Web** 各微服务应用按照一般规则添加依赖，即在父工程对依赖版本进行管理，在子模块中引入。此外：

* **Spring** 相关依赖在引入时添加属性 **\<optional>true\<optional>** 标签并在引用依赖的工程中统一引入。
* **Maria-Web** 依赖全部添加 **\<optional>true\<optional>** 标签并在引用依赖的工程中统一引入。
* 非 **Spring** 相关依赖在模块依赖中添加，不添加 **\<optional>true\<optional>** 标签，由各模块引入并间接引入到各个微服务模块中。

---
