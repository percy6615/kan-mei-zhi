/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : db_jrwz

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-11-21 18:32:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `getback_restart`
-- ----------------------------
DROP TABLE IF EXISTS `getback_restart`;
CREATE TABLE `getback_restart` (
  `area` int(10) NOT NULL DEFAULT '0',
  `note` varchar(50) DEFAULT NULL,
  `locx` int(10) NOT NULL DEFAULT '0',
  `locy` int(10) NOT NULL DEFAULT '0',
  `mapid` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`area`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of getback_restart
-- ----------------------------
INSERT INTO `getback_restart` VALUES ('5', '去往古鲁丁的船', '32631', '32983', '0');
INSERT INTO `getback_restart` VALUES ('6', '去往说话之岛的船', '32543', '32728', '4');
INSERT INTO `getback_restart` VALUES ('70', '遗忘之岛', '32828', '32848', '70');
INSERT INTO `getback_restart` VALUES ('75', '欧瑞 象牙塔1层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('76', '欧瑞 象牙塔2层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('77', '欧瑞 象牙塔3层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('78', '欧瑞 象牙塔4层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('79', '欧瑞 象牙塔5层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('80', '欧瑞 象牙塔6层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('81', '欧瑞 象牙塔7层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('82', '欧瑞 象牙塔8层', '34047', '32283', '4');
INSERT INTO `getback_restart` VALUES ('83', '去往遗忘之岛的船', '33426', '33499', '4');
INSERT INTO `getback_restart` VALUES ('84', '去往海音的船', '32936', '33057', '70');
INSERT INTO `getback_restart` VALUES ('88', '奇岩竞技场', '33442', '32797', '0');
INSERT INTO `getback_restart` VALUES ('91', '说话之岛竞技场', '32580', '32931', '4');
INSERT INTO `getback_restart` VALUES ('92', '古鲁丁竞技场', '32612', '32734', '0');
INSERT INTO `getback_restart` VALUES ('95', '银骑士竞技场', '33080', '33392', '4');
INSERT INTO `getback_restart` VALUES ('98', '威顿竞技场', '33705', '32504', '4');
INSERT INTO `getback_restart` VALUES ('101', '傲慢之塔1楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('102', '傲慢之塔2楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('103', '傲慢之塔3楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('104', '傲慢之塔4楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('105', '傲慢之塔5楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('106', '傲慢之塔6楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('107', '傲慢之塔7楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('108', '傲慢之塔8楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('109', '傲慢之塔9楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('110', '傲慢之塔10楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('111', '傲慢之塔11楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('112', '傲慢之塔12楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('113', '傲慢之塔13楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('114', '傲慢之塔14楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('115', '傲慢之塔15楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('116', '傲慢之塔16楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('117', '傲慢之塔17楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('118', '傲慢之塔18楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('119', '傲慢之塔19楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('120', '傲慢之塔20楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('121', '傲慢之塔21楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('122', '傲慢之塔22楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('123', '傲慢之塔23楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('124', '傲慢之塔24楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('125', '傲慢之塔25楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('126', '傲慢之塔26楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('127', '傲慢之塔27楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('128', '傲慢之塔28楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('129', '傲慢之塔29楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('130', '傲慢之塔30楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('131', '傲慢之塔31楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('132', '傲慢之塔32楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('133', '傲慢之塔33楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('134', '傲慢之塔34楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('135', '傲慢之塔35楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('136', '傲慢之塔36楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('137', '傲慢之塔37楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('138', '傲慢之塔38楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('139', '傲慢之塔39楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('140', '傲慢之塔40楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('141', '傲慢之塔41楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('142', '傲慢之塔42楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('143', '傲慢之塔43楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('144', '傲慢之塔44楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('145', '傲慢之塔45楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('146', '傲慢之塔46楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('147', '傲慢之塔47楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('148', '傲慢之塔48楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('149', '傲慢之塔49楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('150', '傲慢之塔50楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('151', '傲慢之塔51楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('152', '傲慢之塔52楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('153', '傲慢之塔53楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('154', '傲慢之塔54楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('155', '傲慢之塔55楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('156', '傲慢之塔56楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('157', '傲慢之塔57楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('158', '傲慢之塔58楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('159', '傲慢之塔59楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('160', '傲慢之塔60楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('161', '傲慢之塔61楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('162', '傲慢之塔62楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('163', '傲慢之塔63楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('164', '傲慢之塔64楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('165', '傲慢之塔65楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('166', '傲慢之塔66楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('167', '傲慢之塔67楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('168', '傲慢之塔68楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('169', '傲慢之塔69楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('170', '傲慢之塔70楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('171', '傲慢之塔71楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('172', '傲慢之塔72楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('173', '傲慢之塔73楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('174', '傲慢之塔74楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('175', '傲慢之塔75楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('176', '傲慢之塔76楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('177', '傲慢之塔77楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('178', '傲慢之塔78楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('179', '傲慢之塔79楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('180', '傲慢之塔80楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('181', '傲慢之塔81楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('182', '傲慢之塔82楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('183', '傲慢之塔83楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('184', '傲慢之塔84楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('185', '傲慢之塔85楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('186', '傲慢之塔86楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('187', '傲慢之塔87楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('188', '傲慢之塔88楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('189', '傲慢之塔89楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('190', '傲慢之塔90楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('191', '傲慢之塔91楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('192', '傲慢之塔92楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('193', '傲慢之塔93楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('194', '傲慢之塔94楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('195', '傲慢之塔95楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('196', '傲慢之塔96楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('197', '傲慢之塔97楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('198', '傲慢之塔98楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('199', '傲慢之塔99楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('200', '傲慢之塔100楼', '32781', '32816', '101');
INSERT INTO `getback_restart` VALUES ('303', '梦幻之岛', '33976', '32936', '4');
INSERT INTO `getback_restart` VALUES ('306', '黑暗妖精试炼地监', '32896', '32663', '4');
INSERT INTO `getback_restart` VALUES ('446', '去往隐藏之港的船', '32297', '33087', '440');
INSERT INTO `getback_restart` VALUES ('447', '去往海贼岛的船', '32750', '32874', '445');
INSERT INTO `getback_restart` VALUES ('451', '1层 集会场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('452', '1楼 突击队训练场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('453', '1楼 魔兽君王之室', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('454', '1楼 魔兽调教场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('455', '1楼 魔兽训练场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('456', '1楼 魔兽召唤室', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('460', '2楼 黑魔法修练场', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('461', '2楼 黑魔法研究室', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('462', '2楼 法令军王之室', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('463', '2楼 法令军王的书房', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('464', '2楼 暗黑精灵召唤室', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('465', '2楼 暗黑精灵栖息地', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('466', '2楼 暗黑精灵研究室', '32667', '32863', '457');
INSERT INTO `getback_restart` VALUES ('470', '3楼 恶灵祭坛', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('471', '3楼 恶灵之主祭坛', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('472', '3楼 佣兵训练场', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('473', '3楼 冥法军训练场', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('474', '3楼 欧姆实验室', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('475', '3楼 冥法军王之室', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('476', '3楼 中央控制室', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('477', '3楼 恶灵之主佣兵室', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('478', '3楼 控制室走廊', '32671', '32855', '467');
INSERT INTO `getback_restart` VALUES ('490', '地下训练场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('491', '地下通道', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('492', '暗杀军王之室', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('493', '地下控制室', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('494', '地下处刑场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('495', '地下竞技场', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('496', '地下监狱', '32744', '32818', '450');
INSERT INTO `getback_restart` VALUES ('530', '4楼 格兰肯神殿', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('531', '4楼 长老之室', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('532', '4楼 庭园广场', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('533', '4楼 长老会议厅', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('534', '4楼 长老会议厅', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('535', '黑暗妖精圣地', '32744', '32792', '536');
INSERT INTO `getback_restart` VALUES ('550', '船舶之墓 海面', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('551', '船舶之墓 船舱1F', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('552', '船舶之墓 船舱1F(水中)', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('554', '船舶之墓 船舱2F', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('555', '船舶之墓 船舱2F(水中)', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('557', '船舶之墓 船舱', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('558', '船舶之墓 深海', '32844', '32694', '550');
INSERT INTO `getback_restart` VALUES ('600', '欲望洞穴入口', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('601', '欲望洞穴大厅', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('608', '火焰之影实验室', '34053', '32284', '4');
INSERT INTO `getback_restart` VALUES ('777', '不死魔族抛弃之地', '34043', '32184', '4');
INSERT INTO `getback_restart` VALUES ('778', '原生魔族抛弃之地地面', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('779', '原生魔族抛弃之地海底', '32608', '33178', '4');
INSERT INTO `getback_restart` VALUES ('780', '底比斯 沙漠', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('781', '底比斯 金字塔内部', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('782', '底比斯 欧西里斯祭坛', '33966', '33253', '4');
INSERT INTO `getback_restart` VALUES ('783', '提卡尔 废墟村落', '32795', '32751', '783');
INSERT INTO `getback_restart` VALUES ('784', '提卡尔 库库尔坎祭坛', '32795', '32751', '783');
INSERT INTO `getback_restart` VALUES ('1002', '被遗忘的龙之谷：侏儒部落', '33705', '32504', '4');
INSERT INTO `getback_restart` VALUES ('1005', '屠龙副本：安塔瑞斯洞穴', '33710', '32521', '4');
INSERT INTO `getback_restart` VALUES ('1011', '屠龙副本：法利昂洞穴', '33710', '32521', '4');
INSERT INTO `getback_restart` VALUES ('5124', '钓鱼池', '32815', '32809', '5124');
INSERT INTO `getback_restart` VALUES ('5125', '宠物战战场', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5131', '宠物战战场', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5132', '宠物战战场', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5133', '宠物战战场', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5134', '宠物战战场', '32628', '32781', '4');
INSERT INTO `getback_restart` VALUES ('5140', '幽灵之家', '32624', '32813', '4');
INSERT INTO `getback_restart` VALUES ('5143', '宠物竞速赛', '32628', '32772', '4');
INSERT INTO `getback_restart` VALUES ('5300', '新钓鱼池', '32608', '32772', '4');
INSERT INTO `getback_restart` VALUES ('5301', '新钓鱼池', '32608', '32772', '4');
INSERT INTO `getback_restart` VALUES ('5302', '新钓鱼池', '32608', '32772', '4');
INSERT INTO `getback_restart` VALUES ('16384', '旅馆房间：说话之岛', '32599', '32931', '0');
INSERT INTO `getback_restart` VALUES ('16896', '旅馆房间：说话之岛', '32599', '32931', '0');
INSERT INTO `getback_restart` VALUES ('17408', '旅馆房间：古鲁丁', '32631', '32761', '4');
INSERT INTO `getback_restart` VALUES ('17920', '旅馆房间：古鲁丁', '32631', '32761', '4');
INSERT INTO `getback_restart` VALUES ('18432', '旅馆房间：奇岩', '33437', '32790', '4');
INSERT INTO `getback_restart` VALUES ('18944', '旅馆房间：奇岩', '33437', '32790', '4');
INSERT INTO `getback_restart` VALUES ('19456', '旅馆房间：欧瑞', '34067', '32254', '4');
INSERT INTO `getback_restart` VALUES ('19968', '旅馆房间：欧瑞', '34067', '32254', '4');
INSERT INTO `getback_restart` VALUES ('20480', '旅馆房间：风木', '32627', '33167', '4');
INSERT INTO `getback_restart` VALUES ('20992', '旅馆房间：风木', '32627', '33167', '4');
INSERT INTO `getback_restart` VALUES ('21504', 'SKT Hotel', '33115', '33379', '4');
INSERT INTO `getback_restart` VALUES ('22016', 'SKT Hotel', '33115', '33379', '4');
INSERT INTO `getback_restart` VALUES ('22528', '旅馆房间：海音', '33604', '33276', '4');
INSERT INTO `getback_restart` VALUES ('23040', '旅馆房间：海音', '33604', '33276', '4');
