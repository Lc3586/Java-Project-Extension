/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.44 MySQL 8.0 (test)
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 192.168.1.44:3307
 Source Schema         : test4java

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 04/04/2023 17:55:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_blob_entity
-- ----------------------------
DROP TABLE IF EXISTS `test_blob_entity`;
CREATE TABLE `test_blob_entity`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `bytes` longblob NULL COMMENT '文件数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测试读写文件数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_clob_entity
-- ----------------------------
DROP TABLE IF EXISTS `test_clob_entity`;
CREATE TABLE `test_clob_entity`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文本数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测试读写长文本数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_general_entity
-- ----------------------------
DROP TABLE IF EXISTS `test_general_entity`;
CREATE TABLE `test_general_entity`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `char` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字符',
  `string` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字符串',
  `byte` tinyint NULL DEFAULT NULL COMMENT '8位整数',
  `short` smallint NULL DEFAULT NULL COMMENT '16位整数',
  `integer` int NULL DEFAULT NULL COMMENT '32位整数',
  `long` bigint NULL DEFAULT NULL COMMENT '64位整数',
  `float` float(38, 30) NULL DEFAULT NULL COMMENT '单精度浮点数',
  `double` double(38, 30) NULL DEFAULT NULL COMMENT '双精度浮点数',
  `decimal` decimal(38, 30) NULL DEFAULT NULL COMMENT '高精度浮点数',
  `boolean` bit(1) NULL DEFAULT NULL COMMENT '布尔',
  `date` date NULL DEFAULT NULL COMMENT '日期',
  `time` time NULL DEFAULT NULL COMMENT '时间',
  `datetime` datetime(6) NULL DEFAULT NULL COMMENT '日期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测试读写常规数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test_identity_entity
-- ----------------------------
DROP TABLE IF EXISTS `test_identity_entity`;
CREATE TABLE `test_identity_entity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `no` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 828622383484112982 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测试自增主键' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
