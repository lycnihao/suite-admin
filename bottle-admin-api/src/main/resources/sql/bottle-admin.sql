/*
 Navicat Premium Data Transfer

 Source Server         : bottle-admin
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 8.134.120.64:3306
 Source Schema         : bottle-admin

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 29/11/2022 22:28:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `component` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `keep_alive` bit(1) NOT NULL,
  `name` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `parent_id` bigint(0) NOT NULL,
  `path` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `redirect` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(0) NOT NULL,
  `title` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES (1, '2022-11-03 12:52:53.594000', '2022-11-03 12:52:53.594000', 'LAYOUT', 'DashboardOutlined', b'1', 'dashboard', 0, '/dashboard', '/dashboard/console', 1, 'Dashboard', 1);
INSERT INTO `t_permission` VALUES (2, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', '/dashboard/workplace/workplace', NULL, b'1', 'dashboard_workplace', 1, 'workplace', NULL, 0, '工作台', 2);
INSERT INTO `t_permission` VALUES (3, '2022-11-03 12:52:53.594000', '2022-11-03 12:52:53.594000', 'LAYOUT', 'SecurityScanOutlined', b'1', 'auth', 0, '/auth', '/auth/user', 2, '权限管理', 1);
INSERT INTO `t_permission` VALUES (4, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', '/system/user/user', NULL, b'1', 'system_user', 3, 'user', NULL, 0, '用户管理', 2);
INSERT INTO `t_permission` VALUES (5, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', '/system/role/role', NULL, b'1', 'system_role', 3, 'role', NULL, 0, '角色管理', 2);
INSERT INTO `t_permission` VALUES (6, '2022-11-03 12:52:53.594000', '2022-11-03 12:52:53.594000', 'LAYOUT', 'ControlOutlined', b'1', 'system', 0, '/system', '/system/menu', 3, '系统管理', 1);
INSERT INTO `t_permission` VALUES (7, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', '/system/menu/menu', NULL, b'1', 'system_menu', 6, 'menu', NULL, 0, '菜单权限', 2);
INSERT INTO `t_permission` VALUES (8, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_user_list', 4, '/user/list', NULL, 0, '用户列表', 3);
INSERT INTO `t_permission` VALUES (9, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_user_upd', 4, '/user/update', NULL, 0, '修改用户', 3);
INSERT INTO `t_permission` VALUES (10, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_user_del', 4, '/user/delete', NULL, 0, '删除用户', 3);
INSERT INTO `t_permission` VALUES (11, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_user_add', 4, '/user/add', NULL, 0, '添加用户', 3);
INSERT INTO `t_permission` VALUES (12, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_role_list', 5, '/role/list', NULL, 0, '角色列表', 3);
INSERT INTO `t_permission` VALUES (13, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_role_upd', 5, '/role/update', NULL, 0, '修改角色', 3);
INSERT INTO `t_permission` VALUES (14, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_role_del', 5, '/role/delete', NULL, 0, '删除角色', 3);
INSERT INTO `t_permission` VALUES (15, '2022-11-03 12:52:53.806000', '2022-11-03 12:52:53.806000', NULL, NULL, b'1', 'system_role_add', 5, '/role/add', NULL, 0, '添加角色', 3);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `description` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, '2022-11-03 12:52:53.165000', '2022-11-03 12:52:53.165000', NULL, '管理员', 'ADMIN');
INSERT INTO `t_role` VALUES (2, '2022-11-03 12:52:53.272000', '2022-11-27 14:49:35.533000', '', '用户', 'USER');
INSERT INTO `t_role` VALUES (3, '2022-11-27 15:01:22.695000', '2022-11-27 15:01:37.253000', '测试.', '测试角色', 'USER_1');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `permission_id` bigint(0) NOT NULL,
  `role_id` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES (1, '2022-11-03 12:52:53.916000', '2022-11-03 12:52:53.916000', 1, 1);
INSERT INTO `t_role_permission` VALUES (2, '2022-11-03 12:52:54.020000', '2022-11-03 12:52:54.020000', 2, 1);
INSERT INTO `t_role_permission` VALUES (3, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 3, 1);
INSERT INTO `t_role_permission` VALUES (4, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 4, 1);
INSERT INTO `t_role_permission` VALUES (5, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 5, 1);
INSERT INTO `t_role_permission` VALUES (6, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 6, 1);
INSERT INTO `t_role_permission` VALUES (7, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 7, 1);
INSERT INTO `t_role_permission` VALUES (8, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 8, 1);
INSERT INTO `t_role_permission` VALUES (9, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 9, 1);
INSERT INTO `t_role_permission` VALUES (10, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 10, 1);
INSERT INTO `t_role_permission` VALUES (11, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 11, 1);
INSERT INTO `t_role_permission` VALUES (12, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 12, 1);
INSERT INTO `t_role_permission` VALUES (13, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 13, 1);
INSERT INTO `t_role_permission` VALUES (14, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 14, 1);
INSERT INTO `t_role_permission` VALUES (15, '2022-11-03 12:52:54.124000', '2022-11-03 12:52:54.124000', 15, 1);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `avatar` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(0) NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `deleted_flag` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '2022-11-03 12:52:52.749000', '2022-11-03 12:52:52.749000', '', '', 'test@example.com', 'admin', '{bcrypt}$2a$10$7Cl1vUXc5tdQXVNrQEWd9uhTt7vLjasiJZxI60rjrZZK5/YFL9CpS', 1, 'admin', 0);
INSERT INTO `t_user` VALUES (2, '2022-11-03 12:52:53.052000', '2022-11-17 12:47:04.379000', '', '', 'test1@example.com	', 'user', '{bcrypt}$2a$10$6aDkci0dbnrsxsS.bFf35uh4VD2BJ6Kt1nBciuiz1sx8EHBy3TvQy', 1, 'user', 1);
INSERT INTO `t_user` VALUES (3, '2022-11-18 11:55:53.656000', '2022-11-21 13:41:44.917000', '', '', 'user1@example.com', 'user1', '{bcrypt}$2a$10$h4uadB0nLH1jRBYbLQZOge5VKF0XgezDvBf46U2vQGTn40R8yusqG', 1, 'user1', 0);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `update_time` datetime(6) NULL DEFAULT NULL,
  `role_id` bigint(0) NOT NULL,
  `user_id` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, '2022-11-03 12:52:53.379000', '2022-11-03 12:52:53.379000', 1, 1);
INSERT INTO `t_user_role` VALUES (2, '2022-11-03 12:52:53.483000', '2022-11-03 12:52:53.483000', 2, 2);
INSERT INTO `t_user_role` VALUES (4, '2022-11-18 11:55:53.727000', '2022-11-18 11:55:53.727000', 2, 3);

SET FOREIGN_KEY_CHECKS = 1;
