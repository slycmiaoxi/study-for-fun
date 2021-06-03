

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `api_user_auth`;
CREATE TABLE `api_user_auth`  (
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `service_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'api_user_auth' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_user_auth
-- ----------------------------
INSERT INTO `api_user_auth` VALUES ('08bb513323fc4f66887172605a9883f6', '1');
INSERT INTO `api_user_auth` VALUES ('08bb513323fc4f66887172605a9883f6', '2');

SET FOREIGN_KEY_CHECKS = 1;
