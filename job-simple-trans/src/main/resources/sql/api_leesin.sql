

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_leesin
-- ----------------------------
DROP TABLE IF EXISTS `api_leesin`;
CREATE TABLE `api_leesin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `api_test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of api_leesin
-- ----------------------------
INSERT INTO `api_leesin` VALUES (1, '11', '12');
INSERT INTO `api_leesin` VALUES (2, '21', '22');

SET FOREIGN_KEY_CHECKS = 1;
