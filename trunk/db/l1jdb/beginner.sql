/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : db_jrwz

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-11-21 18:30:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `beginner`
-- ----------------------------
DROP TABLE IF EXISTS `beginner`;
CREATE TABLE `beginner` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `item_id` int(6) NOT NULL DEFAULT '0',
  `count` int(10) NOT NULL DEFAULT '0',
  `charge_count` int(10) NOT NULL DEFAULT '0',
  `enchantlvl` int(6) NOT NULL DEFAULT '0',
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `activate` char(1) NOT NULL DEFAULT 'A',
  `bless` int(11) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of beginner
-- ----------------------------
INSERT INTO `beginner` VALUES ('1', '40308', '10000', '0', '0', '金币', 'A', '1');
INSERT INTO `beginner` VALUES ('2', '40005', '1', '0', '0', '蜡烛', 'A', '1');
INSERT INTO `beginner` VALUES ('3', '40641', '1', '0', '0', '说话卷轴', 'A', '1');
INSERT INTO `beginner` VALUES ('4', '40383', '1', '0', '0', '地图:歌唱之岛', 'P', '1');
INSERT INTO `beginner` VALUES ('5', '40378', '1', '0', '0', '地图:妖精森林', 'E', '1');
INSERT INTO `beginner` VALUES ('6', '40380', '1', '0', '0', '地图:银骑士村庄', 'E', '1');
INSERT INTO `beginner` VALUES ('7', '40384', '1', '0', '0', '地图:隐藏之谷', 'K', '1');
INSERT INTO `beginner` VALUES ('8', '40383', '1', '0', '0', '地图:歌唱之岛', 'W', '1');
INSERT INTO `beginner` VALUES ('9', '40389', '1', '0', '0', '地图:沉默洞穴', 'D', '1');
INSERT INTO `beginner` VALUES ('10', '40383', '1', '0', '0', '地图:歌唱之岛', 'D', '1');
