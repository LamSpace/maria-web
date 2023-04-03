-- noinspection SqlNoDataSourceInspectionForFile

-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: maria_web
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
-- Table structure for table `tb_config`
--

DROP TABLE IF EXISTS `tb_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_config` (
  `id` bigint NOT NULL COMMENT '主键',
  `config_key` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '系统配置键',
  `config_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '系统配置名称',
  `config_value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '系统配置值',
  `config_description` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '系统配置描述信息',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁标记',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记，1表示已删除，0表示未删除',
  `updatable` int DEFAULT '0' COMMENT '系统配置是否允许被修改，1表示允许被修改，0表示禁止被修改',
  `deletable` int NOT NULL DEFAULT '0' COMMENT '系统配置是否允许被删除，1表示允许被删除，0表示禁止被删除',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '系统配置备注信息',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_config`
--

LOCK TABLES `tb_config` WRITE;
/*!40000 ALTER TABLE `tb_config` DISABLE KEYS */;
INSERT INTO `tb_config` VALUES (94111250380361728,'DruidSuffix','Druid监控地址后缀','/druid/index.html','描述微服务模块 Druid 监控地址的后缀。',0,0,0,0,'','2022-10-07 14:39:18','admin','2022-10-07 14:39:18','admin'),(94111826249912320,'SwaggerSuffix','Swagger文档地址后缀','/swagger-ui/index.html','描述微服务模块 Swagger 文档地址的后缀。',0,0,0,0,'','2022-10-07 14:41:35','admin','2022-10-07 14:41:35','admin');
/*!40000 ALTER TABLE `tb_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_dictionary`
--

DROP TABLE IF EXISTS `tb_dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_dictionary` (
  `id` bigint NOT NULL COMMENT '主键',
  `dictionary_label` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典标签',
  `dictionary_value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典键值',
  `dictionary_type` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典类型',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '数据字典排序',
  `description` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典描述信息',
  `remark` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典备注信息',
  `css_class` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典在前端的样式名称，暂不支持',
  `is_default` int NOT NULL DEFAULT '1' COMMENT '数据字典是否默认，1表示默认，0表示非默认',
  `enabled` int NOT NULL DEFAULT '1' COMMENT '数据字典状态，1表示启用，0表示禁用',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁标记',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记，1表示已删除，0表示未删除',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dictionary`
--

LOCK TABLES `tb_dictionary` WRITE;
/*!40000 ALTER TABLE `tb_dictionary` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_dictionary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_dictionary_type`
--

DROP TABLE IF EXISTS `tb_dictionary_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_dictionary_type` (
  `id` bigint NOT NULL COMMENT '主键',
  `dictionary_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典类型名称',
  `dictionary_type` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典类型',
  `enabled` int NOT NULL DEFAULT '1' COMMENT '数据字典类型状态，1表示启用，0表示禁用',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '数据字典类型备注信息',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁标记',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记，1表示已删除，0表示未删除',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典类型信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dictionary_type`
--

LOCK TABLES `tb_dictionary_type` WRITE;
/*!40000 ALTER TABLE `tb_dictionary_type` DISABLE KEYS */;
INSERT INTO `tb_dictionary_type` VALUES (94109325392089088,'Druid监控地址','DruidAddress',1,'由于系统配置已添加 Druid 监控地址后缀，因此微服务模块 Druid 监控只需要填写协议、主机和端口信息即可。',1,0,'2022-10-07 14:31:39','admin','2022-10-07 14:45:08','admin'),(94109406262464512,'Swagger文档地址','SwaggerAddress',1,'由于系统配置已添加 Swagger 文档地址后缀，因此微服务模块 Swagger 文档只需要添加协议、主机和端口信息即可。',1,0,'2022-10-07 14:31:58','admin','2022-10-07 14:46:38','admin');
/*!40000 ALTER TABLE `tb_dictionary_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_log_login`
--

DROP TABLE IF EXISTS `tb_log_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_log_login` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '登录日志主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录用户名',
  `login_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '用户登录时间',
  `success` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户登录是否成功, 1表示成功, 0表示失败',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户登录失败异常原因',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '127.0.0.1' COMMENT '用户登录时的IP地址',
  `browser_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录时的浏览器名称',
  `browser_type` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录时的浏览器类型',
  `browser_version` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录时的浏览器版本',
  `browser_manufacturer` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录时的浏览器厂商',
  `os_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录时的操作系统名称',
  `os_manufacturer` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录时的操作系统厂商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户登录日志信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_log_login`
--

LOCK TABLES `tb_log_login` WRITE;
/*!40000 ALTER TABLE `tb_log_login` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_log_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_log_operation`
--

DROP TABLE IF EXISTS `tb_log_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_log_operation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志记录主键',
  `record_id` bigint NOT NULL DEFAULT '-1' COMMENT '操作记录主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人员用户名',
  `operation_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '用户执行操作时间',
  `operation` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作行为',
  `method` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作执行方法',
  `parameters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户操作执行参数',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作描述',
  `success` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户操作是否成功, 1表示成功, 0表示失败',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户操作异常原因',
  `user_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '127.0.0.1' COMMENT '用户操作时的IP地址',
  `browser_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作时的浏览器名称',
  `browser_type` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作时的浏览器类型',
  `browser_version` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作时的浏览器版本',
  `browser_manufacturer` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作时的浏览器厂商',
  `os_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作时的操作系统名称',
  `os_manufacturer` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户操作时的操作系统厂商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统操作日志信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_log_operation`
--

LOCK TABLES `tb_log_operation` WRITE;
/*!40000 ALTER TABLE `tb_log_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_log_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_menu`
--

DROP TABLE IF EXISTS `tb_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_menu` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint NOT NULL COMMENT '上级菜单主键',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
  `url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单URL地址，仅当菜单类型为页面时有效',
  `perms` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单权限标志符号，仅当菜单类型为按钮时有效',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单图标样式',
  `type` int NOT NULL DEFAULT '0' COMMENT '菜单类型，0表示页面，1表示按钮',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '菜单排序',
  `status` int NOT NULL DEFAULT '1' COMMENT '菜单状态，1表示启用，0表示禁用',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁标记',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记，1表示已删除，0表示未删除',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_menu`
--

LOCK TABLES `tb_menu` WRITE;
/*!40000 ALTER TABLE `tb_menu` DISABLE KEYS */;
INSERT INTO `tb_menu` VALUES (94102954050719744,0,'系统管理','','','el-icon-setting',0,1,1,1,0,'2022-10-07 14:06:20','admin','2022-10-07 14:11:17','admin'),(94103260339769344,94102954050719744,'用户管理','/user','','el-icon-user',0,1,1,0,0,'2022-10-07 14:07:33','admin','2022-10-07 14:07:33','admin'),(94103348126552064,94102954050719744,'角色管理','/role','','el-icon-user-solid',0,2,1,0,0,'2022-10-07 14:07:54','admin','2022-10-07 14:07:54','admin'),(94103441546285056,94102954050719744,'菜单管理','/menu','','el-icon-s-custom',0,3,1,0,0,'2022-10-07 14:08:16','admin','2022-10-07 14:08:16','admin'),(94103608127262720,94102954050719744,'系统配置','/config','','el-icon-s-tools',0,4,1,1,0,'2022-10-07 14:08:56','admin','2022-10-07 14:09:12','admin'),(94103863501656064,94102954050719744,'字典类型','/dictionaryType','','el-icon-s-data',0,5,1,1,0,'2022-10-07 14:09:57','admin','2022-10-07 14:25:34','admin'),(94104049074442240,94102954050719744,'数据字典','/dictionary','','el-icon-s-claim',0,6,1,0,0,'2022-10-07 14:10:41','admin','2022-10-07 14:10:41','admin'),(94105673666793472,94103260339769344,'添加用户','','user@add','el-icon-plus',1,1,1,0,0,'2022-10-07 14:17:08','admin','2022-10-07 14:17:08','admin'),(94105839526350848,94103260339769344,'修改用户','','user@update','el-icon-edit',1,2,1,0,0,'2022-10-07 14:17:48','admin','2022-10-07 14:17:48','admin'),(94105926776262656,94103260339769344,'删除用户','','user@delete','el-icon-delete',1,3,1,0,0,'2022-10-07 14:18:09','admin','2022-10-07 14:18:09','admin'),(94106077423079424,94103260339769344,'重置密码','','user@reset','el-icon-unlock',1,4,1,1,0,'2022-10-07 14:18:45','admin','2022-10-07 14:19:03','admin'),(94106281740210176,94103260339769344,'配置用户','','user@configure','el-icon-s-tools',1,5,1,0,0,'2022-10-07 14:19:33','admin','2022-10-07 14:19:33','admin'),(94106494806659072,94103348126552064,'添加角色','','role@add','el-icon-plus',1,1,1,0,0,'2022-10-07 14:20:24','admin','2022-10-07 14:20:24','admin'),(94106587437862912,94103348126552064,'修改角色','','role@update','el-icon-edit',1,2,1,0,0,'2022-10-07 14:20:46','admin','2022-10-07 14:20:46','admin'),(94106667528097792,94103348126552064,'删除角色','','role@delete','el-icon-delete',1,3,1,0,0,'2022-10-07 14:21:05','admin','2022-10-07 14:21:05','admin'),(94106843563036672,94103348126552064,'配置角色','','role@configure','el-icon-s-tools',1,4,1,0,0,'2022-10-07 14:21:47','admin','2022-10-07 14:21:47','admin'),(94106991173177344,94103441546285056,'添加菜单','','menu@add','el-icon-plus',1,1,1,0,0,'2022-10-07 14:22:22','admin','2022-10-07 14:22:22','admin'),(94107086492930048,94103441546285056,'修改菜单','','menu@update','el-icon-edit',1,2,1,0,0,'2022-10-07 14:22:45','admin','2022-10-07 14:22:45','admin'),(94107176720797696,94103441546285056,'删除菜单','','menu@delete','el-icon-delete',1,3,1,0,0,'2022-10-07 14:23:07','admin','2022-10-07 14:23:07','admin'),(94107358900391936,94103608127262720,'添加配置','','config@add','el-icon-plus',1,1,1,0,0,'2022-10-07 14:23:50','admin','2022-10-07 14:23:50','admin'),(94107439250673664,94103608127262720,'修改配置','','config@update','el-icon-edit',1,2,1,0,0,'2022-10-07 14:24:09','admin','2022-10-07 14:24:09','admin'),(94107516933378048,94103608127262720,'删除配置','','config@delete','el-icon-delete',1,3,1,0,0,'2022-10-07 14:24:28','admin','2022-10-07 14:24:28','admin'),(94107740431060992,94103863501656064,'添加类型','','dictionaryType@add','el-icon-plus',1,1,1,0,0,'2022-10-07 14:25:21','admin','2022-10-07 14:25:21','admin'),(94107958912356352,94103863501656064,'修改类型','','dictionaryType@update','el-icon-edit',1,2,1,0,0,'2022-10-07 14:26:13','admin','2022-10-07 14:26:13','admin'),(94108095663443968,94103863501656064,'删除类型','','dictionaryType@delete','el-icon-delete',1,3,1,0,0,'2022-10-07 14:26:46','admin','2022-10-07 14:26:46','admin'),(94108414577348608,94104049074442240,'添加字典','','dictionary@add','el-icon-plus',1,1,1,0,0,'2022-10-07 14:28:02','admin','2022-10-07 14:28:02','admin'),(94108548950265856,94104049074442240,'修改字典','','dictionary@update','el-icon-edit',1,2,1,0,0,'2022-10-07 14:28:34','admin','2022-10-07 14:28:34','admin'),(94108613060202496,94104049074442240,'删除字典','','dictionary@delete','el-icon-delete',1,3,1,0,0,'2022-10-07 14:28:49','admin','2022-10-07 14:28:49','admin'),(97340185741758464,0,'日志管理','','','el-icon-tickets',0,3,1,0,0,'2022-10-16 12:29:56','admin','2022-10-16 12:29:56','admin'),(97340356030500864,97340185741758464,'登录日志','/loginLog','','el-icon-document-checked',0,1,1,0,0,'2022-10-16 12:30:37','admin','2022-10-16 12:30:37','admin'),(97340599933472768,97340185741758464,'操作日志','/operationLog','','el-icon-document',0,2,1,0,0,'2022-10-16 12:31:35','admin','2022-10-16 12:31:35','admin'),(97340826329419776,97340185741758464,'运行日志','/runtimeLog','','el-icon-s-ticket',0,3,1,0,0,'2022-10-16 12:32:29','admin','2022-10-16 12:32:29','admin');
/*!40000 ALTER TABLE `tb_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `enabled` int NOT NULL DEFAULT '1' COMMENT '角色状态，1表示启用，0表示禁用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色备注信息',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁标记',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_menu`
--

DROP TABLE IF EXISTS `tb_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role_menu` (
  `id` bigint NOT NULL COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色主键',
  `menu_id` bigint NOT NULL COMMENT '菜单主键',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色-菜单关联信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_menu`
--

LOCK TABLES `tb_role_menu` WRITE;
/*!40000 ALTER TABLE `tb_role_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_transaction`
--

DROP TABLE IF EXISTS `tb_role_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role_transaction` (
  `transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '事务信息记录主键',
  `transaction_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '分布式事务消息',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '分布式事务消息创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分布式事务消息创建人员',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色事务信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_transaction`
--

LOCK TABLES `tb_role_transaction` WRITE;
/*!40000 ALTER TABLE `tb_role_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_role_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
  `enabled` int NOT NULL DEFAULT '1' COMMENT '用户状态，1表示启用，0表示禁用',
  `accountnonexpired` int NOT NULL DEFAULT '1' COMMENT '用户账号是否过期，1表示已过期，0表示未过期',
  `accountnonlocked` int NOT NULL DEFAULT '1' COMMENT '用户账号是否锁定，1表示已锁定，0表示未锁定',
  `credentialsnonexpired` int NOT NULL DEFAULT '1' COMMENT '用户密码是否过期，1表示已过期，0表示未过期',
  `type` int NOT NULL DEFAULT '1' COMMENT '用户类型，0表示超级管理员，1表示普通用户',
  `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁标记',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  `gender` int NOT NULL DEFAULT '1' COMMENT '用户性别，1表示男性，0表示女性',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系方式',
  `birthday` date NOT NULL DEFAULT '2022-02-02' COMMENT '出生日期',
  `id_card` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `address` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'admin','超级管理员','$2a$10$V483BczV.gkef.2gG.T73Op9OY3VcBACs2YpRek.UEWbTK/MzcWeC',1,1,1,1,0,0,0,'2022-02-02 02:02:02','','2022-02-02 02:02:02','',1,'lemonlovepy@163.com','13112344321','2022-02-02','511711199901010101','XX市XX区XX街道');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_role`
--

DROP TABLE IF EXISTS `tb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_role` (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户主键',
  `role_id` bigint NOT NULL COMMENT '角色主键',
  `create_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录创建人员',
  `update_time` datetime NOT NULL DEFAULT '2022-02-02 02:02:02' COMMENT '记录修改时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '记录修改人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户-角色关联信息实体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-08 14:03:58
