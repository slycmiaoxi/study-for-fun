

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_yurnero
-- ----------------------------
DROP TABLE IF EXISTS `api_yurnero`;
CREATE TABLE `api_yurnero`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `api_test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_yurnero
-- ----------------------------
INSERT INTO `api_yurnero` VALUES (1, 't1', 't2');
INSERT INTO `api_yurnero` VALUES (2, 't2', 't4');

SET FOREIGN_KEY_CHECKS = 1;
