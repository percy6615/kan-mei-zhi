/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : db_jrwz

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-11-21 18:33:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `mail`
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `type` int(10) unsigned NOT NULL DEFAULT '0',
  `sender` varchar(16) DEFAULT NULL,
  `receiver` varchar(16) DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `read_status` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `subject` blob,
  `content` blob,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail
-- ----------------------------
