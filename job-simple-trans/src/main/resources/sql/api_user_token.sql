

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_user_token
-- ----------------------------
DROP TABLE IF EXISTS `api_user_token`;
CREATE TABLE `api_user_token`  (
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '授权开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '授权结束时间',
  `auth_time` datetime(0) NULL DEFAULT NULL COMMENT '授权时间',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Token令牌',
  `token_create_time` datetime(0) NULL DEFAULT NULL COMMENT 'Token生成时间',
  `token_end_time` datetime(0) NULL DEFAULT NULL COMMENT 'Token失效时间',
  `token_count` int(10) NULL DEFAULT NULL COMMENT '当日token生成次数',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `last_update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后更新人',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户token记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_user_token
-- ----------------------------
INSERT INTO `api_user_token` VALUES ('08bb513323fc4f66887172605a9883f6', NULL, NULL, NULL, '4b905e4ac7b3ef34099e1da1cab924ac', NULL, '2021-06-05 00:00:00', 1, '2021-06-04 00:40:59', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
