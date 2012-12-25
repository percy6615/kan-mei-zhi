/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : db_jrwz

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-11-21 18:30:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `castle`
-- ----------------------------
DROP TABLE IF EXISTS `castle`;
CREATE TABLE `castle` (
  `castle_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL DEFAULT '',
  `war_time` datetime DEFAULT NULL,
  `tax_rate` int(11) NOT NULL DEFAULT '0',
  `public_money` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`castle_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of castle
-- ----------------------------
INSERT INTO `castle` VALUES ('1', '肯特城', '2009-10-06 19:41:01', '10', '4973002');
INSERT INTO `castle` VALUES ('2', '妖魔城', '2009-10-06 19:41:01', '10', '1205');
INSERT INTO `castle` VALUES ('3', '风木城', '2009-10-06 19:41:01', '10', '7790772');
INSERT INTO `castle` VALUES ('4', '奇岩城', '2009-10-06 19:41:01', '10', '2858838');
INSERT INTO `castle` VALUES ('5', '海音城', '2009-10-06 19:41:01', '10', '0');
INSERT INTO `castle` VALUES ('6', '侏儒城', '2009-10-06 19:41:01', '10', '259622');
INSERT INTO `castle` VALUES ('7', '亚丁城', '2009-10-06 19:41:01', '10', '1765500');
INSERT INTO `castle` VALUES ('8', '狄亚得要塞', '2009-10-06 19:41:01', '10', '53117156');
