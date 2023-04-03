## maria-zipkin

> maria-zipkin 是 ***Maria-Web*** 的 **zipkin** 构建模块。
>
> 之所以需要自己构建 zipkin 模块，是因为 docker 提供的 zipkin 镜像使用的是 java17，直接运行无法连接数据库。

> 本次构建采用的 zipkin 版本是 2.24，由于包体积较大，因此提交代码时会忽略该文件，需要手动下载。

> 【注意】：MySQL 实例刚启动时，若此时 zipkin 直接连接 MySQL 会提示报错，是因为 zipkin 连接 MySQL
> 时使用的是 MariaDB 的数据库驱动。但是若 MySQL 已被其他应用连接，则 zipkin 可以正常访问，即构建的 zipkin 服务需要在
> 其他应用程序之后启动。
>
> 在本次微服务应用搭建过程中，假定 zipkin 服务是在 nacos 服务之后启动的。
---
