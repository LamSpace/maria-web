-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: maria_nacos
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config_info`
--

DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

LOCK TABLES `config_info` WRITE;
/*!40000 ALTER TABLE `config_info` DISABLE KEYS */;
INSERT INTO `config_info` VALUES (1,'maria-common-config-alpha.yaml','DEFAULT_GROUP','spring:\n  boot:\n    admin:\n      client:\n        url: http://maria-web-service-dashboard:${server.port}\n        register-once: true\n        instance:\n          name: ${spring.application.name}\n          service-base-url: http://${spring.application.name}:${server.port}\n          metadata:\n            username: unknown\n            password: unknown\n  sleuth:\n    sampler:\n      probability: 1.0\n  zipkin:\n    base-url: http://maria-web-zipkin:9411\n    discovery-client-enabled: false\n  cloud:\n    nacos:\n      server-addr: maria-web-nacos:8848\n      username: nacos\n      password: nacos\n      discovery:\n        namespace: d951d3b6-0989-4cbc-a029-d6bb91624e34\n    sentinel:\n      transport:\n        dashboard: maria-web-sentinel-dashboard:8858\n      web-context-unify: false\n\nmanagement:\n  info:\n    env:\n      enabled: true\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: always\n    shutdown:\n      enabled: true','5277f729343a694dc539579c2f0e8ed7','2023-03-01 13:56:31','2023-03-07 21:30:47','nacos','172.19.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用公共配置（一）。','','','yaml','',''),(2,'maria-common-config-beta.yaml','DEFAULT_GROUP','spring:\n  redis:\n    host: maria-web-redis\n    port: 6379\n    database: 0\n    timeout: 10000\n    lettuce:\n      pool:\n        max-active: 8\n        max-wait: -1\n        max-idle: 8\n        min-idle: 0\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  jackson:\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8','faefffb8f0167570a78cacbd0ec21269','2023-03-01 13:59:09','2023-03-01 13:59:09','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用公共配置（二）。',NULL,NULL,'yaml',NULL,''),(3,'maria-common-config-gamma.yaml','DEFAULT_GROUP','spring:\n  aop:\n    auto: true\n  security:\n    user:\n      name: maria\n      password: maria','499bbf0582be0f29b980b5314c9a5757','2023-03-01 14:00:00','2023-03-01 14:00:00','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用公共配置（三）。',NULL,NULL,'yaml',NULL,''),(4,'maria-common-config-delta.yaml','DEFAULT_GROUP','spring:  \n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    druid:\n      url: jdbc:mysql://maria-web-mysql:3306/maria_web?useUnicode=true&characterEncoding=utf-8&useSSL=true&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai\n      username: root\n      password: maria123\n      initial-size: 8\n      min-idle: 8\n      max-active: 30\n      max-wait: 30000\n      time-between-eviction-runs-millis: 60000\n      min-evictable-idle-time-millis: 30000\n      validation-query: select 1 from dual\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      pool-prepared-statements: true\n      max-open-prepared-statements: 20\n      max-pool-prepared-statement-per-connection-size: 20\n      filters: stat,wall,slf4j\n      filter:\n        stat:\n          enabled: true\n          db-type: mysql\n          log-slow-sql: true\n          slow-sql-millis: 2000\n      web-stat-filter:\n        enabled: true\n        exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\"\n      stat-view-servlet:\n        enabled: true\n        url-pattern: /druid/*\n        reset-enable: false\n        login-username:\n        login-password:\n        allow:\n\nmybatis-plus:\n  mapper-locations: classpath*:/mapper/**/*.xml\n  global-config:\n    db-config:\n      id-type: assign_id\n      logic-delete-value: 1\n      logic-delete-field: deleted\n      logic-not-delete-value: 0\n    banner: false','02bf88865ce805baccf79f704970aa4d','2023-03-01 14:00:56','2023-03-01 14:00:56','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用公共配置（四）。',NULL,NULL,'yaml',NULL,''),(5,'maria-common-config-kafka-producer.yaml','DEFAULT_GROUP','spring:\n  kafka:\n    bootstrap-servers: maria-web-kafka:9092\n    producer:\n      retries: 1\n      acks: 1\n      batch-size: 16384\n      buffer-memory: 33554432\n      key-serializer: org.apache.kafka.common.serialization.StringSerializer\n      value-serializer: org.apache.kafka.common.serialization.StringSerializer\n      compression-type: snappy\n      properties:\n        linger.ms: 0','1b7a20cc9b6480f0147b46da01ae674b','2023-03-01 14:01:51','2023-03-01 14:01:51','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用公共配置（五）。',NULL,NULL,'yaml',NULL,''),(6,'maria-web-service-authentication-dev.yaml','DEFAULT_GROUP','spring:\n  application:\n    name: maria-web-service-authentication\n  kafka:\n    producer:\n      client-id: kafka-log-client-authentication','275f659bf7b1b8b9b45f88a8f7c2bb67','2023-03-01 14:03:08','2023-03-07 20:28:02','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用认证中心微服务模块配置。','','','yaml','',''),(7,'maria-web-service-gateway-dev.yaml','DEFAULT_GROUP','spring:\n  application:\n    name: maria-web-service-gateway\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          enabled: true\n      routes:\n        - id: service-authentication\n          uri: lb://maria-web-service-authentication\n          order: 1\n          predicates:\n            - Path=/service-auth/**\n          filters:\n            - StripPrefix=1\n        - id: service-system\n          uri: lb://maria-web-service-system\n          order: 1\n          predicates:\n            - Path=/service-system/**\n          filters:\n            - StripPrefix=1\n        - id: service-socket\n          uri: lb:ws://maria-web-service-socket\n          order: 1\n          predicates:\n            - Path=/socket\n        - id: service-log\n          uri: lb://maria-web-service-log\n          order: 1\n          predicates:\n            - Path=/service-log/**\n          filters:\n            - StripPrefix=1','ea548fbe28954b0bad6fac5abee2c1a3','2023-03-01 14:05:03','2023-03-07 21:42:17','nacos','172.20.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用网关微服务模块配置。','','','yaml','',''),(8,'maria-web-service-system-dev.yaml','DEFAULT_GROUP','logging:\n  level:\n    io.github.lamtong.maria.system.mapper: debug\n\nspring:\n  application:\n    name: maria-web-service-system\n  kafka:\n    producer:\n      client-id: kafka-log-client-system\n  cloud:\n    sentinel:\n      eager: true\n      datasource:\n        flow:\n          nacos:\n            namespace: d951d3b6-0989-4cbc-a029-d6bb91624e34\n            server-addr: maria-web-nacos:8848\n            username: nacos\n            password: nacos\n            data-id: maria-service-system-user-flow-rule\n            group-id: SENTINEL_GROUP\n            data-type: json\n            rule-type: flow\n\nrocketmq:\n  name-server: maria-web-rocketmq-namesrv:9876\n  producer:\n    group: maria-service-system-group','c9bb2ad5d076e2cd98f880b9e7490f77','2023-03-01 14:07:10','2023-03-07 21:23:50','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用系统管理微服务模块配置。','','','yaml','',''),(9,'maria-web-service-socket-dev.yaml','DEFAULT_GROUP','spring:\n  application:\n    name: maria-web-service-socket\n\nrocketmq:\n  name-server: maria-web-rocketmq-namesrv:9876','43eec9391c5145608bc37806808d1dff','2023-03-01 14:08:23','2023-03-07 21:24:10','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用消息推送微服务模块配置。','','','yaml','',''),(10,'maria-web-service-log-dev.yaml','DEFAULT_GROUP','logging:\n  level:\n    io.github.lamtong.maria.log.mapper: debug\n\nspring:\n  application:\n    name: maria-web-service-log\n  kafka:\n    bootstrap-servers: maria-web-kafka:9092\n    consumer:\n      properties:\n        session:\n          timeout:\n            ms: 120000\n        request:\n          timeout:\n            ms: 180000\n      enable-auto-commit: true\n      auto-commit-interval: 1000\n      auto-offset-reset: latest\n      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer\n      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer\n      max-poll-records: 100\n    listener:\n      missing-topics-fatal: false\n      type: batch','4854a4ef264dff131462ef4350050670','2023-03-01 14:09:33','2023-03-07 21:24:23','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用日志管理微服务模块配置。','','','yaml','',''),(11,'maria-service-system-user-flow-rule','SENTINEL_GROUP','[\n    {\n        \"resource\": \"/user/list\",\n        \"limitApp\": \"default\",\n        \"grade\": 0,\n        \"count\": 1,\n        \"clusterMode\": false,\n        \"controlBehavior\": 0,\n        \"strategy\": 0\n    }\n]','f93e00d2d2dc9bf165c5c3600625d5bb','2023-03-01 14:10:43','2023-03-01 14:10:43','nacos','172.18.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用系统管理微服务模块用户管理限流配置。',NULL,NULL,'json',NULL,''),(12,'maria-web-service-job-dev.yaml','DEFAULT_GROUP','spring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://maria-web-mysql:3306/maria_job?useUnicode=true&characterEncoding=utf-8&useSSL=true&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai\n    username: root\n    password: maria123\n  boot:\n    admin:\n      client:\n        url: http://maria-web-service-dashboard:${server.port}\n        instance:\n          service-base-url: http://${spring.application.name}:${server.port}\n          name: ${spring.application.name}\n          metadata:\n            username: unknown\n            password: unknown\n  cloud:\n    nacos:\n      server-addr: maria-web-nacos:8848\n      username: nacos\n      password: nacos\n      discovery:\n        namespace: d951d3b6-0989-4cbc-a029-d6bb91624e34\n\nelasticjob:\n  reg-center:\n    server-lists: maria-web-zookeeper:2181\n    namespace: maria-service-elastic-job\n    base-sleep-time-milliseconds: 1000\n    max-sleep-time-milliseconds: 3000\n    max-retries: 3\n    session-timeout-milliseconds: 60000\n    connection-timeout-milliseconds: 15000\n  jobs:\n    mariaSimpleJob:\n      elastic-job-class: io.github.lamtong.maria.job.jobs.MariaSimpleJob\n      cron: 0/30 * * * * ?\n      sharding-total-count: 2\n      sharding-item-parameters: 0=A,1=B\n      job-error-handler-type: LOG\n      description: Sample of SimpleJob in ElasticJob for Maria\n      time-zone: GMT+8\n      job-parameter: sample\n      monitor-execution: true\n      failover: false\n      misfire: true\n      max-time-diff-seconds: -1\n      reconcile-interval-minutes: 10\n      disabled: false\n      overwrite: false\n      job-sharding-strategy-type: AVG_ALLOCATION\n      job-executor-service-handler-type: CPU\n      job-listener-types:\n        - mariaSimpleJobListener\n  tracing:\n    type: RDB\n\nmanagement:\n  info:\n    env:\n      enabled: true\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: always\n    shutdown:\n      enabled: true\n\nrocketmq:\n  name-server: maria-web-rocketmq-namesrv:9876\n  producer:\n    group: maria-service-job-group','a31f1253dbcbc774efd125a913c0b0b2','2023-03-13 22:42:14','2023-03-14 21:11:36','nacos','172.13.0.1','','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria微服务应用系统任务调度微服务模块配置。','','','yaml','','');
/*!40000 ALTER TABLE `config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_aggr`
--

DROP TABLE IF EXISTS `config_info_aggr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='增加租户字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_aggr`
--

LOCK TABLES `config_info_aggr` WRITE;
/*!40000 ALTER TABLE `config_info_aggr` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_aggr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_beta`
--

DROP TABLE IF EXISTS `config_info_beta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info_beta';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_beta`
--

LOCK TABLES `config_info_beta` WRITE;
/*!40000 ALTER TABLE `config_info_beta` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_beta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--

LOCK TABLES `config_info_tag` WRITE;
/*!40000 ALTER TABLE `config_info_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_tags_relation`
--

DROP TABLE IF EXISTS `config_tags_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_tag_relation';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_tags_relation`
--

LOCK TABLES `config_tags_relation` WRITE;
/*!40000 ALTER TABLE `config_tags_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_tags_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_capacity`
--

DROP TABLE IF EXISTS `group_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='集群、各Group容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_capacity`
--

LOCK TABLES `group_capacity` WRITE;
/*!40000 ALTER TABLE `group_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `his_config_info`
--

DROP TABLE IF EXISTS `his_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='多租户改造';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `his_config_info`
--

LOCK TABLES `his_config_info` WRITE;
/*!40000 ALTER TABLE `his_config_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `his_config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('nacos','ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_capacity`
--

DROP TABLE IF EXISTS `tenant_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='租户容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_capacity`
--

LOCK TABLES `tenant_capacity` WRITE;
/*!40000 ALTER TABLE `tenant_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_info`
--

DROP TABLE IF EXISTS `tenant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='tenant_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_info`
--

LOCK TABLES `tenant_info` WRITE;
/*!40000 ALTER TABLE `tenant_info` DISABLE KEYS */;
INSERT INTO `tenant_info` VALUES (1,'1','d951d3b6-0989-4cbc-a029-d6bb91624e34','maria','maria微服务命名空间','nacos',1677649840220,1677649840220);
/*!40000 ALTER TABLE `tenant_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-14 13:22:22
