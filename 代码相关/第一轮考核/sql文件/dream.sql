/*
Navicat MySQL Data Transfer

Source Server         : 121
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : dream

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2022-04-14 23:48:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `apply`
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicant` varchar(22) NOT NULL,
  `applyltrible` varchar(8) NOT NULL,
  `founder` varchar(22) NOT NULL,
  `status` char(8) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `a_tribe` (`applyltrible`),
  CONSTRAINT `a_tribe` FOREIGN KEY (`applyltrible`) REFERENCES `tribe` (`tribe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of apply
-- ----------------------------

-- ----------------------------
-- Table structure for `likerecord`
-- ----------------------------
DROP TABLE IF EXISTS `likerecord`;
CREATE TABLE `likerecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sponsor` varchar(255) NOT NULL,
  `recipient` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of likerecord
-- ----------------------------

-- ----------------------------
-- Table structure for `notice`
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `context` varchar(255) NOT NULL,
  `date` datetime DEFAULT NULL,
  `receiver` varchar(255) NOT NULL,
  `applicant` varchar(255) NOT NULL,
  `noticeMark` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('1', '部落签到通知', '您好，今天您部落签到了吗^_^', '2022-04-14 00:00:00', '1光', '系统', '1');

-- ----------------------------
-- Table structure for `tribe`
-- ----------------------------
DROP TABLE IF EXISTS `tribe`;
CREATE TABLE `tribe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` char(8) NOT NULL,
  `tribe` varchar(255) NOT NULL,
  `founder` varchar(255) NOT NULL,
  `peonum` int(11) NOT NULL,
  `power` int(11) NOT NULL,
  `money` int(11) NOT NULL,
  `firstdate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_name` (`id`) USING BTREE,
  KEY `tribe` (`tribe`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tribe
-- ----------------------------
INSERT INTO `tribe` VALUES ('1', 'shine', '无1', '无1', '0', '0', '0', '2022-04-01');
INSERT INTO `tribe` VALUES ('2', 'dark', '无2', '无2', '0', '0', '0', '2022-04-01');
INSERT INTO `tribe` VALUES ('3', 'shine', '1光明部落', '1光', '3', '300', '3000', '2022-04-02');
INSERT INTO `tribe` VALUES ('4', 'shine', '2光明部落', '2光', '2', '200', '2000', '2022-04-02');
INSERT INTO `tribe` VALUES ('5', 'shine', '3光明部落', '3光', '1', '100', '1000', '2022-04-03');
INSERT INTO `tribe` VALUES ('6', 'shine', '4光明部落', '4光', '1', '100', '1000', '2022-04-03');
INSERT INTO `tribe` VALUES ('7', 'shine', '5光明部落', '5光', '1', '100', '1000', '2022-04-03');
INSERT INTO `tribe` VALUES ('8', 'dark', '1黑暗部落', '1暗', '3', '300', '3000', '2022-04-02');
INSERT INTO `tribe` VALUES ('9', 'dark', '2黑暗部落', '2暗', '2', '200', '2000', '2022-04-02');
INSERT INTO `tribe` VALUES ('10', 'dark', '3黑暗部落', '3暗', '1', '100', '1000', '2022-04-03');
INSERT INTO `tribe` VALUES ('11', 'dark', '4黑暗部落', '4暗', '1', '100', '1000', '2022-04-03');
INSERT INTO `tribe` VALUES ('12', 'dark', '5黑暗部落', '5暗', '1', '100', '1000', '2022-04-03');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(22) NOT NULL,
  `password` varchar(22) NOT NULL,
  `sex` char(4) NOT NULL,
  `group` char(8) NOT NULL,
  `tribe` varchar(255) DEFAULT NULL,
  `money` int(11) NOT NULL,
  `likes` int(11) NOT NULL,
  `dateMine` bigint(20) NOT NULL,
  `dateTribe` bigint(20) NOT NULL,
  `quitTribeDate` bigint(20) NOT NULL,
  `mark` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_name` (`username`),
  KEY `t_tribe` (`tribe`),
  CONSTRAINT `t_tribe` FOREIGN KEY (`tribe`) REFERENCES `tribe` (`tribe`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '1光', '光', '男', 'shine', '1光明部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('2', '2光', '光', '男', 'shine', '2光明部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('3', '3光', '光', '男', 'shine', '3光明部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('4', '4光', '光', '男', 'shine', '4光明部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('5', '5光', '光', '男', 'shine', '5光明部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('6', '1暗', '暗', '男', 'dark', '1黑暗部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('7', '2暗', '暗', '男', 'dark', '2黑暗部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('8', '3暗', '暗', '男', 'dark', '3黑暗部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('9', '4暗', '暗', '男', 'dark', '4黑暗部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('10', '5暗', '暗', '男', 'dark', '5黑暗部落', '1000', '1', '0', '0', '0', '1');
INSERT INTO `user` VALUES ('11', '1', '1', '男', 'shine', '1光明部落', '1000', '1', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('12', '2', '2', '女', 'shine', '1光明部落', '2000', '2', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('13', '3', '3', '男', 'shine', '2光明部落', '3000', '3', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('14', '4', '4', '女', 'shine', '无1', '4000', '4', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('15', '5', '5', '男', 'shine', '无1', '5000', '5', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('16', '6', '6', '男', 'dark', '1黑暗部落', '1000', '1', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('17', '7', '7', '女', 'dark', '1黑暗部落', '2000', '2', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('18', '8', '8', '女', 'dark', '2黑暗部落', '3000', '3', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('19', '9', '9', '女', 'dark', '无2', '4000', '4', '0', '0', '0', '2');
INSERT INTO `user` VALUES ('20', '10', '10', '男', 'dark', '无2', '5000', '5', '0', '0', '0', '2');
