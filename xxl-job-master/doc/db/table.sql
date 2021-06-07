

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_task_datetime`
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_datetime`;
CREATE TABLE `sys_task_datetime` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `page_index` int(11) DEFAULT NULL COMMENT '页码',
  `api_name` varchar(50) DEFAULT NULL COMMENT '接口名称',
  `execute_time` datetime DEFAULT NULL COMMENT '创建时间',
  `after_min` int(11) DEFAULT NULL COMMENT '接口提前量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_task_datetime
-- ----------------------------
INSERT INTO `sys_task_datetime` VALUES ('1', '1', 'token', '2021-06-07 19:29:58', null);
INSERT INTO `sys_task_datetime` VALUES ('2', '1', 'leesin', '2021-06-07 20:23:25', null);
INSERT INTO `sys_task_datetime` VALUES ('3', '1', 'Yurnero', '2021-06-07 20:24:24', null);

-- ----------------------------
-- Table structure for `sys_task_token`
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_token`;
CREATE TABLE `sys_task_token` (
  `id` int(11) NOT NULL COMMENT '主键',
  `token` varchar(50) DEFAULT NULL COMMENT '接口验证密钥',
  `date_time` datetime DEFAULT NULL COMMENT '获取token时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_task_token
-- ----------------------------
INSERT INTO `sys_task_token` VALUES ('1', 'e3712ca1fcfe08c4cdcf7f1d665ddc18', '2021-06-07 19:29:58');

-- ----------------------------
-- Table structure for `xxl_job_group`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_group`;
CREATE TABLE `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
INSERT INTO `xxl_job_group` VALUES ('1', 'xxl-job-executor', '示例执行器', '0', 'http://192.168.18.217:9999/', '2021-06-07 21:10:46');

-- ----------------------------
-- Table structure for `xxl_job_info`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_info`;
CREATE TABLE `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
INSERT INTO `xxl_job_info` VALUES ('1', '1', '测试token', '2018-11-03 22:21:31', '2021-06-07 19:12:02', 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'tokenJobHandler', '', 'SERIAL_EXECUTION', '0', '0', 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '', '0', '0', '0');
INSERT INTO `xxl_job_info` VALUES ('2', '1', '测试盲僧接口', '2021-06-07 20:04:04', '2021-06-07 20:04:04', 'xxl', '', 'CRON', '0/1 * * * * ? *', 'DO_NOTHING', 'FIRST', 'leesinJobHandler', '', 'SERIAL_EXECUTION', '0', '0', 'BEAN', '', 'GLUE代码初始化', '2021-06-07 20:04:04', '', '0', '0', '0');
INSERT INTO `xxl_job_info` VALUES ('3', '1', '测试剑圣接口', '2021-06-07 20:24:00', '2021-06-07 20:24:00', 'xxl', '', 'CRON', '0/0 * * * * ? *', 'DO_NOTHING', 'FIRST', 'yurneroJobHandler', '', 'SERIAL_EXECUTION', '0', '0', 'BEAN', '', 'GLUE代码初始化', '2021-06-07 20:24:00', '', '0', '0', '0');

-- ----------------------------
-- Table structure for `xxl_job_lock`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_lock`;
CREATE TABLE `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');

-- ----------------------------
-- Table structure for `xxl_job_log`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log`;
CREATE TABLE `xxl_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
INSERT INTO `xxl_job_log` VALUES ('1', '1', '1', null, 'tokenJobHandler', '', null, '0', '2021-06-07 19:14:01', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', null, '0', null, '1');
INSERT INTO `xxl_job_log` VALUES ('2', '1', '1', null, 'tokenJobHandler', '', null, '0', '2021-06-07 19:14:01', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', null, '0', null, '1');
INSERT INTO `xxl_job_log` VALUES ('3', '1', '1', 'http://192.168.18.217:9999/', 'tokenJobHandler', '', null, '0', '2021-06-07 19:18:43', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：500<br>msg：xxl-rpc remoting error(Read timed out), for url : http://192.168.18.217:9999/run', '2021-06-07 19:20:27', '0', '', '1');
INSERT INTO `xxl_job_log` VALUES ('4', '1', '1', 'http://192.168.18.217:9999/', 'tokenJobHandler', '', null, '0', '2021-06-07 19:26:20', '200', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：200<br>msg：null', '2021-06-07 20:21:25', '500', '任务结果丢失，标记失败', '1');
INSERT INTO `xxl_job_log` VALUES ('5', '1', '1', 'http://192.168.18.217:9999/', 'tokenJobHandler', '', null, '0', '2021-06-07 19:29:14', '200', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：200<br>msg：null', '2021-06-07 20:21:25', '500', '任务结果丢失，标记失败', '1');
INSERT INTO `xxl_job_log` VALUES ('6', '1', '2', 'http://192.168.18.217:9999/', 'leesinJobHandler', '', null, '0', '2021-06-07 20:04:14', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：500<br>msg：xxl-rpc remoting error(Read timed out), for url : http://192.168.18.217:9999/run', '2021-06-07 20:05:34', '0', '', '1');
INSERT INTO `xxl_job_log` VALUES ('7', '1', '2', 'http://192.168.18.217:9999/', 'leesinJobHandler', '', null, '0', '2021-06-07 20:06:19', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：500<br>msg：xxl-rpc remoting error(Read timed out), for url : http://192.168.18.217:9999/run', '2021-06-07 20:07:34', '0', '', '1');
INSERT INTO `xxl_job_log` VALUES ('8', '1', '2', 'http://192.168.18.217:9999/', 'leesinJobHandler', '', null, '0', '2021-06-07 20:10:12', '200', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：200<br>msg：null', '2021-06-07 20:21:25', '500', '任务结果丢失，标记失败', '1');
INSERT INTO `xxl_job_log` VALUES ('9', '1', '2', 'http://192.168.18.217:9999/', 'leesinJobHandler', '', null, '0', '2021-06-07 20:17:32', '200', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：200<br>msg：null', '2021-06-07 20:17:37', '0', '', '0');
INSERT INTO `xxl_job_log` VALUES ('10', '1', '2', null, 'leesinJobHandler', '', null, '0', '2021-06-07 20:22:08', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', null, '0', null, '1');
INSERT INTO `xxl_job_log` VALUES ('11', '1', '2', null, 'leesinJobHandler', '', null, '0', '2021-06-07 20:22:24', '500', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', null, '0', null, '1');
INSERT INTO `xxl_job_log` VALUES ('12', '1', '2', 'http://192.168.18.217:9999/', 'leesinJobHandler', '', null, '0', '2021-06-07 20:22:48', '200', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：200<br>msg：null', '2021-06-07 20:23:26', '0', '', '0');
INSERT INTO `xxl_job_log` VALUES ('13', '1', '3', 'http://192.168.18.217:9999/', 'yurneroJobHandler', '', null, '0', '2021-06-07 20:24:07', '200', '任务触发类型：手动触发<br>调度机器：192.168.18.217<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.18.217:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.18.217:9999/<br>code：200<br>msg：null', '2021-06-07 20:24:24', '0', '', '0');

-- ----------------------------
-- Table structure for `xxl_job_log_report`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log_report`;
CREATE TABLE `xxl_job_log_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_log_report
-- ----------------------------
INSERT INTO `xxl_job_log_report` VALUES ('1', '2021-06-07 00:00:00', '3', '0', '10', null);
INSERT INTO `xxl_job_log_report` VALUES ('2', '2021-06-06 00:00:00', '0', '0', '0', null);
INSERT INTO `xxl_job_log_report` VALUES ('3', '2021-06-05 00:00:00', '0', '0', '0', null);

-- ----------------------------
-- Table structure for `xxl_job_logglue`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_logglue`;
CREATE TABLE `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_logglue
-- ----------------------------

-- ----------------------------
-- Table structure for `xxl_job_registry`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_registry`;
CREATE TABLE `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_registry
-- ----------------------------
INSERT INTO `xxl_job_registry` VALUES ('4', 'EXECUTOR', 'xxl-job-executor', 'http://192.168.18.217:9999/', '2021-06-07 21:10:32');

-- ----------------------------
-- Table structure for `xxl_job_user`
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_user`;
CREATE TABLE `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
INSERT INTO `xxl_job_user` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '1', null);
