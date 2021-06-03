

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_auth
-- ----------------------------
DROP TABLE IF EXISTS `api_auth`;
CREATE TABLE `api_auth`  (
  `service_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务ID',
  `service_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `service_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务说明',
  `service_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务相对路径',
  `status` int(4) NULL DEFAULT NULL COMMENT '服务状态',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_auth
-- ----------------------------
INSERT INTO `api_auth` VALUES ('1', 'leesin', '盲僧接口', '/api/study/getinformation/leesinlist', 1);
INSERT INTO `api_auth` VALUES ('2', 'yurnero', '剑圣接口', '/api/study/getinformation/yurnerolist', 1);

SET FOREIGN_KEY_CHECKS = 1;
