
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_job_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_detail`;
CREATE TABLE `sys_job_detail`  (
  `id` int(11) NOT NULL COMMENT '主键id',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务组名',
  `job_class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务所在位置',
  `job_cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务执行表达式',
  `job_type` int(11) NULL DEFAULT NULL COMMENT '定时任务启动/停止   0停止  1启动',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '定时任务创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_job_detail
-- ----------------------------
INSERT INTO `sys_job_detail` VALUES (1, 'test', 'group', 'com.example.jobsimpledemo.job.bpArticleJob', '0 33 19 * * ? *', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
