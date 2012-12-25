/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : db_jrwz

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-11-21 18:36:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `trap`
-- ----------------------------
DROP TABLE IF EXISTS `trap`;
CREATE TABLE `trap` (
  `id` int(8) NOT NULL,
  `note` varchar(64) DEFAULT NULL,
  `type` varchar(64) NOT NULL,
  `gfxId` int(4) NOT NULL,
  `isDetectionable` tinyint(1) NOT NULL,
  `base` int(4) NOT NULL,
  `dice` int(4) NOT NULL,
  `diceCount` int(4) NOT NULL,
  `poisonType` char(1) NOT NULL DEFAULT 'n',
  `poisonDelay` int(4) NOT NULL DEFAULT '0',
  `poisonTime` int(4) NOT NULL DEFAULT '0',
  `poisonDamage` int(4) NOT NULL DEFAULT '0',
  `monsterNpcId` int(4) NOT NULL DEFAULT '0',
  `monsterCount` int(4) NOT NULL DEFAULT '0',
  `teleportX` int(4) NOT NULL DEFAULT '0',
  `teleportY` int(4) NOT NULL DEFAULT '0',
  `teleportMapId` int(4) NOT NULL DEFAULT '0',
  `skillId` int(4) NOT NULL DEFAULT '0',
  `skillTimeSeconds` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trap
-- ----------------------------
INSERT INTO `trap` VALUES ('1', '伤害陷阱:捕兽夹', 'L1DamageTrap', '1065', '1', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('2', '治愈陷阱:小魔法阵', 'L1HealingTrap', '1074', '0', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('3', '药水陷阱:毒', 'L1PoisonTrap', '1066', '1', '0', '0', '0', 'd', '0', '5000', '10', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('4', '药水陷阱:沉默毒', 'L1PoisonTrap', '1066', '1', '0', '0', '0', 's', '0', '0', '10', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('5', '药水陷阱:麻痹毒', 'L1PoisonTrap', '1066', '1', '0', '0', '0', 'p', '5000', '5000', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('6', '伤害陷阱:流星', 'L1DamageTrap', '1085', '1', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('7', '伤害陷阱:针(刺)', 'L1DamageTrap', '1070', '1', '10', '10', '1', '-', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('8', '怪物陷阱:TOI4F变种杨果里恩', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45348', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('9', '怪物陷阱:TOI8F奇美拉', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45407', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('10', '怪物陷阱:TOI14F思克巴', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45394', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('11', '怪物陷阱:TOI18F邪恶多眼怪', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45406', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('12', '怪物陷阱:TOI24F恐怖的火炎蛋', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45384', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('13', '怪物陷阱:TOI28F恐怖的地狱犬', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45471', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('14', '怪物陷阱:TOI34F残暴的骷髅枪兵', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45403', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('15', '怪物陷阱:TOI38F残暴的史巴托', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45455', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('16', '怪物陷阱:TOI44F火焰之灵魂', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45514', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('17', '怪物陷阱:TOI48F骨龙', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45522', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('18', '怪物陷阱:TOI54F受诅咒的艾尔摩士兵', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45524', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('19', '怪物陷阱:TOI64F冷酷冰原老虎', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45528', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('20', '怪物陷阱:TOI74F暗黑黑骑士', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45503', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('21', '怪物陷阱:TOI78F暗黑火焰弓箭手', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45532', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('22', '怪物陷阱:TOI84F小幻象眼魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45586', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('23', '怪物陷阱:TOI94F风精灵王', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45621', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('24', '怪物陷阱:IT4F高仑钢铁怪', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45372', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('25', '怪物陷阱:IT4F密密', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45141', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('26', '怪物陷阱:IT4F活铠甲', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45322', '2', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('27', '怪物陷阱:IT5F高仑钢铁怪', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45372', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('28', '怪物陷阱:IT5F密密', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45141', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('29', '怪物陷阱:IT5F活铠甲', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45322', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('30', '怪物陷阱:IT6F影魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45162', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('31', '怪物陷阱:IT6F死神', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45221', '3', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('32', '怪物陷阱:IT7F影魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45162', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('33', '怪物陷阱:IT7F死神', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45221', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('34', '怪物陷阱:IT8F高仑钢铁怪', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45372', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('35', '怪物陷阱:IT8F影魔', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45162', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('36', '怪物陷阱:IT8F死神', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45221', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('37', '怪物陷阱:IT8F活铠甲', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '45322', '5', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('38', '怪物陷阱:福朗克迷宫海贼骷髅士兵', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46057', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('39', '怪物陷阱:福朗克迷宫海贼骷髅首领', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46058', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('40', '怪物陷阱:福朗克迷宫海贼骷髅刀手', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46059', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('41', '怪物陷阱:福朗克迷宫海贼骷髅', 'L1MonsterTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '46056', '4', '0', '0', '0', '0', '0');
INSERT INTO `trap` VALUES ('42', '传送陷阱:福朗克迷宫出发点', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '4', '32679', '32742', '482', '0', '0');
INSERT INTO `trap` VALUES ('43', '传送陷阱:迪哥的废弃监狱出发点', 'L1TeleportTrap', '0', '0', '0', '0', '0', 'n', '0', '0', '0', '0', '0', '32736', '32800', '483', '0', '0');
INSERT INTO `trap` VALUES ('44', '技能陷阱:幽灵之家木乃伊的诅咒', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '33', '0');
INSERT INTO `trap` VALUES ('45', '技能陷阱:幽灵之家闇盲咒术', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '20', '5');
INSERT INTO `trap` VALUES ('46', '技能陷阱:幽灵之家缓速术', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '29', '5');
INSERT INTO `trap` VALUES ('47', '技能陷阱:幽灵之家加速术', 'L1SkillTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '0', '0', '0', '43', '5');
INSERT INTO `trap` VALUES ('48', '传送陷阱:幽灵之家', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32730', '32829', '5140', '0', '0');
INSERT INTO `trap` VALUES ('49', '传送陷阱:幽灵之家', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32749', '32813', '5140', '0', '0');
INSERT INTO `trap` VALUES ('50', '传送陷阱:幽灵之家', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32747', '32867', '5140', '0', '0');
INSERT INTO `trap` VALUES ('51', '传送陷阱:幽灵之家', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32785', '32819', '5140', '0', '0');
INSERT INTO `trap` VALUES ('52', '传送陷阱:幽灵之家', 'L1TeleportTrap', '0', '0', '0', '0', '0', '-', '0', '0', '0', '0', '0', '32797', '32869', '5140', '0', '0');
