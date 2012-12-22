/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.jrwz.server.model.skill;

/**
 * 技能编号.
 */
public class L1SkillId {
    /** 魔法开头. */
    public static final int SKILLS_BEGIN = 1;

    /*
     * Regular Magic Lv1-10
     */
    /** 法师魔法 (初级治愈术) */
    public static final int HEAL = 1; // E: LESSER_HEAL
    /** 法师魔法 (日光术) */
    public static final int LIGHT = 2;
    /** 法师魔法 (防护罩) */
    public static final int SHIELD = 3;
    /** 法师魔法 (光箭) */
    public static final int ENERGY_BOLT = 4;
    /** 法师魔法 (指定传送) */
    public static final int TELEPORT = 5;
    /** 法师魔法 (冰箭) */
    public static final int ICE_DAGGER = 6;
    /** 法师魔法 (风刃) */
    public static final int WIND_CUTTER = 7; // E: WIND_SHURIKEN
    /** 法师魔法 (神圣武器) */
    public static final int HOLY_WEAPON = 8;
    /** 法师魔法 (解毒术) */
    public static final int CURE_POISON = 9;
    /** 法师魔法 (寒冷战栗) */
    public static final int CHILL_TOUCH = 10;
    /** 法师魔法 (毒咒) */
    public static final int CURSE_POISON = 11;
    /** 法师魔法 (拟似魔法武器) */
    public static final int ENCHANT_WEAPON = 12;
    /** 法师魔法 (无所遁形术) */
    public static final int DETECTION = 13;
    /** 法师魔法 (负重强化) */
    public static final int DECREASE_WEIGHT = 14;
    /** 法师魔法 (火箭) */
    public static final int FIRE_ARROW = 15;
    /** 法师魔法 (地狱之牙) */
    public static final int STALAC = 16;
    /** 法师魔法 (极光雷电) */
    public static final int LIGHTNING = 17;
    /** 法师魔法 (起死回生术) */
    public static final int TURN_UNDEAD = 18;
    /** 法师魔法 (中级治愈术) */
    public static final int EXTRA_HEAL = 19; // E: HEAL
    /** 法师魔法 (闇盲咒术) */
    public static final int CURSE_BLIND = 20;
    /** 法师魔法 (铠甲护持) */
    public static final int BLESSED_ARMOR = 21;
    /** 法师魔法 (寒冰气息) */
    public static final int FROZEN_CLOUD = 22;
    /** 法师魔法 (能量感测) */
    public static final int WEAK_ELEMENTAL = 23; // E: REVEAL_WEAKNESS
    // none = 24
    /** 法师魔法 (燃烧的火球) */
    public static final int FIREBALL = 25;
    /** 法师魔法 (通畅气脉术) */
    public static final int PHYSICAL_ENCHANT_DEX = 26; // E: ENCHANT_DEXTERITY
    /** 法师魔法 (坏物术) */
    public static final int WEAPON_BREAK = 27;
    /** 法师魔法 (吸血鬼之吻) */
    public static final int VAMPIRIC_TOUCH = 28;
    /** 法师魔法 (缓速术) */
    public static final int SLOW = 29;
    /** 法师魔法 (岩牢) */
    public static final int EARTH_JAIL = 30;
    /** 法师魔法 (魔法屏障) */
    public static final int COUNTER_MAGIC = 31;
    /** 法师魔法 (冥想术) */
    public static final int MEDITATION = 32;
    /** 法师魔法 (木乃伊的诅咒) */
    public static final int CURSE_PARALYZE = 33;
    /** 法师魔法 (极道落雷) */
    public static final int CALL_LIGHTNING = 34;
    /** 法师魔法 (高级治愈术) */
    public static final int GREATER_HEAL = 35;
    /** 法师魔法 (迷魅术) */
    public static final int TAMING_MONSTER = 36; // E: TAME_MONSTER
    /** 法师魔法 (圣洁之光) */
    public static final int REMOVE_CURSE = 37;
    /** 法师魔法 (冰锥) */
    public static final int CONE_OF_COLD = 38;
    /** 法师魔法 (魔力夺取) */
    public static final int MANA_DRAIN = 39;
    /** 法师魔法 (黑闇之影) */
    public static final int DARKNESS = 40;
    /** 法师魔法 (造尸术) */
    public static final int CREATE_ZOMBIE = 41;
    /** 法师魔法 (体魄强健术) */
    public static final int PHYSICAL_ENCHANT_STR = 42; // E: ENCHANT_MIGHTY
    /** 法师魔法 (加速术) */
    public static final int HASTE = 43;
    /** 法师魔法 (魔法相消术) */
    public static final int CANCELLATION = 44; // E: CANCEL MAGIC
    /** 法师魔法 (地裂术) */
    public static final int ERUPTION = 45;
    /** 法师魔法 (烈炎术) */
    public static final int SUNBURST = 46;
    /** 法师魔法 (弱化术) */
    public static final int WEAKNESS = 47;
    /** 法师魔法 (祝福魔法武器) */
    public static final int BLESS_WEAPON = 48;
    /** 法师魔法 (体力回复术) */
    public static final int HEAL_ALL = 49; // E: HEAL_PLEDGE
    /** 法师魔法 (冰矛围篱) */
    public static final int ICE_LANCE = 50;
    /** 法师魔法 (召唤术) */
    public static final int SUMMON_MONSTER = 51;
    /** 法师魔法 (神圣疾走) */
    public static final int HOLY_WALK = 52;
    /** 法师魔法 (龙卷风) */
    public static final int TORNADO = 53;
    /** 法师魔法 (强力加速术) */
    public static final int GREATER_HASTE = 54;
    /** 法师魔法 (狂暴术) */
    public static final int BERSERKERS = 55;
    /** 法师魔法 (疾病术) */
    public static final int DISEASE = 56;
    /** 法师魔法 (全部治愈术) */
    public static final int FULL_HEAL = 57;
    /** 法师魔法 (火牢) */
    public static final int FIRE_WALL = 58;
    /** 法师魔法 (冰雪暴) */
    public static final int BLIZZARD = 59;
    /** 法师魔法 (隐身术) */
    public static final int INVISIBILITY = 60;
    /** 法师魔法 (返生术) */
    public static final int RESURRECTION = 61;
    /** 法师魔法 (震裂术) */
    public static final int EARTHQUAKE = 62;
    /** 法师魔法 (治愈能量风暴) */
    public static final int LIFE_STREAM = 63;
    /** 法师魔法 (魔法封印) */
    public static final int SILENCE = 64;
    /** 法师魔法 (雷霆风暴) */
    public static final int LIGHTNING_STORM = 65;
    /** 法师魔法 (沉睡之雾) */
    public static final int FOG_OF_SLEEPING = 66;
    /** 法师魔法 (变形术) */
    public static final int SHAPE_CHANGE = 67; // E: POLYMORPH
    /** 法师魔法 (圣结界) */
    public static final int IMMUNE_TO_HARM = 68;
    /** 法师魔法 (集体传送术) */
    public static final int MASS_TELEPORT = 69;
    /** 法师魔法 (火风暴) */
    public static final int FIRE_STORM = 70;
    /** 法师魔法 (药水霜化术) */
    public static final int DECAY_POTION = 71;
    /** 法师魔法 (强力无所遁形术) */
    public static final int COUNTER_DETECTION = 72;
    /** 法师魔法 (创造魔法武器) */
    public static final int CREATE_MAGICAL_WEAPON = 73;
    /** 法师魔法 (流星雨) */
    public static final int METEOR_STRIKE = 74;
    /** 法师魔法 (终极返生术) */
    public static final int GREATER_RESURRECTION = 75;
    /** 法师魔法 (集体缓速术) */
    public static final int MASS_SLOW = 76;
    /** 法师魔法 (究极光裂术) */
    public static final int DISINTEGRATE = 77; // E: DESTROY
    /** 法师魔法 (绝对屏障) */
    public static final int ABSOLUTE_BARRIER = 78;
    /** 法师魔法 (灵魂升华) */
    public static final int ADVANCE_SPIRIT = 79;
    /** 法师魔法 (冰雪飓风) */
    public static final int FREEZING_BLIZZARD = 80;
    // none = 81 - 86
    /*
     * Knight skills
     */
    /** 骑士魔法 (冲击之晕) */
    public static final int SHOCK_STUN = 87; // E: STUN_SHOCK
    /** 骑士魔法 (增幅防御) */
    public static final int REDUCTION_ARMOR = 88;
    /** 骑士魔法 (尖刺盔甲) */
    public static final int BOUNCE_ATTACK = 89;
    /** 骑士魔法 (坚固防护) */
    public static final int SOLID_CARRIAGE = 90;
    /** 骑士魔法 (反击屏障) */
    public static final int COUNTER_BARRIER = 91;
    // none = 92-96
    /*
     * Dark Spirit Magic
     */
    /** 黑暗妖精魔法 (暗隐术) */
    public static final int BLIND_HIDING = 97;
    /** 黑暗妖精魔法 (附加剧毒) */
    public static final int ENCHANT_VENOM = 98;
    /** 黑暗妖精魔法 (影之防护) */
    public static final int SHADOW_ARMOR = 99;
    /** 黑暗妖精魔法 (提炼魔石) */
    public static final int BRING_STONE = 100;
    /** 黑暗妖精魔法 (行走加速) */
    public static final int MOVING_ACCELERATION = 101; // E: PURIFY_STONE
    /** 黑暗妖精魔法 (燃烧斗志) */
    public static final int BURNING_SPIRIT = 102;
    /** 黑暗妖精魔法 (暗黑盲咒) */
    public static final int DARK_BLIND = 103;
    /** 黑暗妖精魔法 (毒性抵抗) */
    public static final int VENOM_RESIST = 104;
    /** 黑暗妖精魔法 (双重破坏) */
    public static final int DOUBLE_BRAKE = 105;
    /** 黑暗妖精魔法 (暗影闪避) */
    public static final int UNCANNY_DODGE = 106;
    /** 黑暗妖精魔法 (暗影之牙) */
    public static final int SHADOW_FANG = 107;
    /** 黑暗妖精魔法 (会心一击) */
    public static final int FINAL_BURN = 108;
    /** 黑暗妖精魔法 (力量提升) */
    public static final int DRESS_MIGHTY = 109;
    /** 黑暗妖精魔法 (敏捷提升) */
    public static final int DRESS_DEXTERITY = 110;
    /** 黑暗妖精魔法 (闪避提升) */
    public static final int DRESS_EVASION = 111;
    // none = 112
    /*
     * Royal Magic
     */
    /** 王族魔法 (精准目标) */
    public static final int TRUE_TARGET = 113;
    /** 王族魔法 (激励士气) */
    public static final int GLOWING_AURA = 114;
    /** 王族魔法 (钢铁士气) */
    public static final int SHINING_AURA = 115;
    /** 王族魔法 (呼唤盟友) */
    public static final int CALL_CLAN = 116; // E: CALL_PLEDGE_MEMBER
    /** 王族魔法 (冲击士气) */
    public static final int BRAVE_AURA = 117;
    /** 王族魔法 (援护盟友) */
    public static final int RUN_CLAN = 118;
    // unknown = 119 - 120
    // none = 121 - 128
    /*
     * Spirit Magic
     */
    /** 妖精魔法 (魔法防御) */
    public static final int RESIST_MAGIC = 129;
    /** 妖精魔法 (心灵转换) */
    public static final int BODY_TO_MIND = 130;
    /** 妖精魔法 (世界树的呼唤) */
    public static final int TELEPORT_TO_MATHER = 131;
    /** 妖精魔法 (三重矢) */
    public static final int TRIPLE_ARROW = 132;
    /** 妖精魔法 (弱化属性) */
    public static final int ELEMENTAL_FALL_DOWN = 133;
    /** 妖精魔法 (镜反射) */
    public static final int COUNTER_MIRROR = 134;
    // none = 135 - 136
    /** 妖精魔法 (净化精神) */
    public static final int CLEAR_MIND = 137;
    /** 妖精魔法 (属性防御) */
    public static final int RESIST_ELEMENTAL = 138;
    // none = 139 - 144
    /** 妖精魔法 (释放元素) */
    public static final int RETURN_TO_NATURE = 145;
    /** 妖精魔法 (魂体转换) */
    public static final int BLOODY_SOUL = 146; // E: BLOOD_TO_SOUL
    /** 妖精魔法 (单属性防御) */
    public static final int ELEMENTAL_PROTECTION = 147; // E:PROTECTION_FROM_ELEMENTAL
    /** 妖精魔法 (火焰武器) */
    public static final int FIRE_WEAPON = 148;
    /** 妖精魔法 (风之神射) */
    public static final int WIND_SHOT = 149;
    /** 妖精魔法 (风之疾走) */
    public static final int WIND_WALK = 150;
    /** 妖精魔法 (大地防护) */
    public static final int EARTH_SKIN = 151;
    /** 妖精魔法 (地面障碍) */
    public static final int ENTANGLE = 152;
    /** 妖精魔法 (魔法消除) */
    public static final int ERASE_MAGIC = 153;
    /** 妖精魔法 (召唤属性精灵) */
    public static final int LESSER_ELEMENTAL = 154; // E:SUMMON_LESSER_ELEMENTAL
    /** 妖精魔法 (烈炎气息) */
    public static final int FIRE_BLESS = 155; // E: BLESS_OF_FIRE
    /** 妖精魔法 (暴风之眼) */
    public static final int STORM_EYE = 156; // E: EYE_OF_STORM
    /** 妖精魔法 (大地屏障) */
    public static final int EARTH_BIND = 157;
    /** 妖精魔法 (生命之泉) */
    public static final int NATURES_TOUCH = 158;
    /** 妖精魔法 (大地的祝福) */
    public static final int EARTH_BLESS = 159; // E: BLESS_OF_EARTH
    /** 妖精魔法 (水之防护) */
    public static final int AQUA_PROTECTER = 160;
    /** 妖精魔法 (封印禁地) */
    public static final int AREA_OF_SILENCE = 161;
    /** 妖精魔法 (召唤强力属性精灵) */
    public static final int GREATER_ELEMENTAL = 162; // E:SUMMON_GREATER_ELEMENTAL
    /** 妖精魔法 (烈炎武器) */
    public static final int BURNING_WEAPON = 163;
    /** 妖精魔法 (生命的祝福) */
    public static final int NATURES_BLESSING = 164;
    /** 妖精魔法 (生命呼唤) */
    public static final int CALL_OF_NATURE = 165; // E: NATURES_MIRACLE
    /** 妖精魔法 (暴风神射) */
    public static final int STORM_SHOT = 166;
    /** 妖精魔法 (风之枷锁) */
    public static final int WIND_SHACKLE = 167;
    /** 妖精魔法 (钢铁防护) */
    public static final int IRON_SKIN = 168;
    /** 妖精魔法 (体能激发) */
    public static final int EXOTIC_VITALIZE = 169;
    /** 妖精魔法 (水之元气) */
    public static final int WATER_LIFE = 170;
    /** 妖精魔法 (属性之火) */
    public static final int ELEMENTAL_FIRE = 171;
    /** 妖精魔法 (暴风疾走) */
    public static final int STORM_WALK = 172;
    /** 妖精魔法 (污浊之水) */
    public static final int POLLUTE_WATER = 173;
    /** 妖精魔法 (精准射击) */
    public static final int STRIKER_GALE = 174;
    /** 妖精魔法 (烈焰之魂) */
    public static final int SOUL_OF_FLAME = 175;
    /** 妖精魔法 (能量激发) */
    public static final int ADDITIONAL_FIRE = 176;
    // none = 177-180
    /*
     * Dragon Knight skills
     */
    /** 龙骑士魔法 (龙之护铠) */
    public static final int DRAGON_SKIN = 181;
    /** 龙骑士魔法 (燃烧击砍) */
    public static final int BURNING_SLASH = 182;
    /** 龙骑士魔法 (护卫毁灭) */
    public static final int GUARD_BRAKE = 183;
    /** 龙骑士魔法 (岩浆喷吐) */
    public static final int MAGMA_BREATH = 184;
    /** 龙骑士魔法 (觉醒：安塔瑞斯) */
    public static final int AWAKEN_ANTHARAS = 185;
    /** 龙骑士魔法 (血之渴望) */
    public static final int BLOODLUST = 186;
    /** 龙骑士魔法 (屠宰者) */
    public static final int FOE_SLAYER = 187;
    /** 龙骑士魔法 (恐惧无助) */
    public static final int RESIST_FEAR = 188;
    /** 龙骑士魔法 (冲击之肤) */
    public static final int SHOCK_SKIN = 189;
    /** 龙骑士魔法 (觉醒：法利昂) */
    public static final int AWAKEN_FAFURION = 190;
    /** 龙骑士魔法 (致命身躯) */
    public static final int MORTAL_BODY = 191;
    /** 龙骑士魔法 (夺命之雷) */
    public static final int THUNDER_GRAB = 192;
    /** 龙骑士魔法 (惊悚死神) */
    public static final int HORROR_OF_DEATH = 193;
    /** 龙骑士魔法 (寒冰喷吐) */
    public static final int FREEZING_BREATH = 194;
    /** 龙骑士魔法 (觉醒：巴拉卡斯) */
    public static final int AWAKEN_VALAKAS = 195;
    // none = 196-200
    /*
     * Illusionist Magic
     */
    /** 幻术士魔法 (镜像) */
    public static final int MIRROR_IMAGE = 201;
    /** 幻术士魔法 (混乱) */
    public static final int CONFUSION = 202;
    /** 幻术士魔法 (暴击) */
    public static final int SMASH = 203;
    /** 幻术士魔法 (幻觉：欧吉) */
    public static final int ILLUSION_OGRE = 204;
    /** 幻术士魔法 (立方：燃烧) */
    public static final int CUBE_IGNITION = 205;
    /** 幻术士魔法 (专注) */
    public static final int CONCENTRATION = 206;
    /** 幻术士魔法 (心灵破坏) */
    public static final int MIND_BREAK = 207;
    /** 幻术士魔法 (骷髅毁坏) */
    public static final int BONE_BREAK = 208;
    /** 幻术士魔法 (幻觉：巫妖) */
    public static final int ILLUSION_LICH = 209;
    /** 幻术士魔法 (立方：地裂) */
    public static final int CUBE_QUAKE = 210;
    /** 幻术士魔法 (耐力) */
    public static final int PATIENCE = 211;
    /** 幻术士魔法 (幻想) */
    public static final int PHANTASM = 212;
    /** 幻术士魔法 (武器破坏者) */
    public static final int ARM_BREAKER = 213;
    /** 幻术士魔法 (幻觉：钻石高仑) */
    public static final int ILLUSION_DIA_GOLEM = 214;
    /** 幻术士魔法 (立方：冲击) */
    public static final int CUBE_SHOCK = 215;
    /** 幻术士魔法 (洞察) */
    public static final int INSIGHT = 216;
    /** 幻术士魔法 (恐慌) */
    public static final int PANIC = 217;
    /** 幻术士魔法 (疼痛的欢愉) */
    public static final int JOY_OF_PAIN = 218;
    /** 幻术士魔法 (幻觉：化身) */
    public static final int ILLUSION_AVATAR = 219;
    /** 幻术士魔法 (立方：和谐) */
    public static final int CUBE_BALANCE = 220;
    /** 技能结尾. */
    public static final int SKILLS_END = 220;
    /*
     * Status
     */
    /** 辅助状态开头. */
    public static final int STATUS_BEGIN = 1000;
    /** 二段加速 */
    public static final int STATUS_BRAVE = 1000;
    /** 一段加速 */
    public static final int STATUS_HASTE = 1001;
    /** 蓝色药水 */
    public static final int STATUS_BLUE_POTION = 1002;
    /** 水中呼吸 */
    public static final int STATUS_UNDERWATER_BREATH = 1003;
    /** 智慧药水 */
    public static final int STATUS_WISDOM_POTION = 1004;
    /** 禁言 */
    public static final int STATUS_CHAT_PROHIBITED = 1005;
    /** 一般毒 */
    public static final int STATUS_POISON = 1006;
    /** 沉默毒 */
    public static final int STATUS_POISON_SILENCE = 1007;
    /** 瘫痪毒(中) */
    public static final int STATUS_POISON_PARALYZING = 1008;
    /** 瘫痪毒(后) */
    public static final int STATUS_POISON_PARALYZED = 1009;
    /** 麻痹毒(木乃伊诅咒)中 */
    public static final int STATUS_CURSE_PARALYZING = 1010;
    /** 麻痹毒(木乃伊诅咒)后 */
    public static final int STATUS_CURSE_PARALYZED = 1011;
    /** 漂浮之眼肉效果 */
    public static final int STATUS_FLOATING_EYE = 1012;
    /** 圣水 */
    public static final int STATUS_HOLY_WATER = 1013;
    /** 神圣的米索莉粉 */
    public static final int STATUS_HOLY_MITHRIL_POWDER = 1014;
    /** 伊娃的圣水 */
    public static final int STATUS_HOLY_WATER_OF_EVA = 1015;
    /** 精灵饼干 */
    public static final int STATUS_ELFBRAVE = 1016;
    /** 生命之树果实 */
    public static final int STATUS_RIBRAVE = 1017;
    /** 立方：燃烧(友方) */
    public static final int STATUS_CUBE_IGNITION_TO_ALLY = 1018;
    /** 立方：燃烧(敌方) */
    public static final int STATUS_CUBE_IGNITION_TO_ENEMY = 1019;
    /** 立方：地裂(友方) */
    public static final int STATUS_CUBE_QUAKE_TO_ALLY = 1020;
    /** 立方：地裂(敌方) */
    public static final int STATUS_CUBE_QUAKE_TO_ENEMY = 1021;
    /** 立方：冲击(友方) */
    public static final int STATUS_CUBE_SHOCK_TO_ALLY = 1022;
    /** 立方：冲击(敌方) */
    public static final int STATUS_CUBE_SHOCK_TO_ENEMY = 1023;
    /**  */
    public static final int STATUS_MR_REDUCTION_BY_CUBE_SHOCK = 1024;
    /** 立方：和谐 */
    public static final int STATUS_CUBE_BALANCE = 1025;
    /** 辅助状态结尾. */
    public static final int STATUS_END = 1025;

    /** GM辅助状态开头. */
    public static final int GMSTATUS_BEGIN = 2000;
    /** 隐身状态 */
    public static final int GMSTATUS_INVISIBLE = 2000;
    /** 怪物血条 */
    public static final int GMSTATUS_HPBAR = 2001;
    /** 显示陷阱 */
    public static final int GMSTATUS_SHOWTRAPS = 2002;
    /** 搜寻隐身 */
    public static final int GMSTATUS_FINDINVIS = 2003;
    /** GM辅助状态结尾. */
    public static final int GMSTATUS_END = 2003;

    /** 料理制作中. */
    public static final int COOKING_NOW = 2999;
    /** 料理状态开头. */
    public static final int COOKING_BEGIN = 3000;
    /** 漂浮之眼肉排 */
    public static final int COOKING_1_0_N = 3000;
    /** 烤熊肉 */
    public static final int COOKING_1_1_N = 3001;
    /** 煎饼 */
    public static final int COOKING_1_2_N = 3002;
    /** 烤蚂蚁腿起司 */
    public static final int COOKING_1_3_N = 3003;
    /** 水果沙拉 */
    public static final int COOKING_1_4_N = 3004;
    /** 水果糖醋肉 */
    public static final int COOKING_1_5_N = 3005;
    /** 烤山猪肉串 */
    public static final int COOKING_1_6_N = 3006;
    /** 蘑菇汤 */
    public static final int COOKING_1_7_N = 3007;
    /** 特别的漂浮之眼肉排 */
    public static final int COOKING_1_0_S = 3008;
    /** 特别的烤熊肉 */
    public static final int COOKING_1_1_S = 3009;
    /** 特别的煎饼 */
    public static final int COOKING_1_2_S = 3010;
    /** 特别的烤蚂蚁腿起司 */
    public static final int COOKING_1_3_S = 3011;
    /** 特别的水果沙拉 */
    public static final int COOKING_1_4_S = 3012;
    /** 特别的水果糖醋肉 */
    public static final int COOKING_1_5_S = 3013;
    /** 特别的烤山猪肉串 */
    public static final int COOKING_1_6_S = 3014;
    /** 特别的蘑菇汤 */
    public static final int COOKING_1_7_S = 3015;
    /** 鱼子酱 */
    public static final int COOKING_2_0_N = 3016;
    /** 鳄鱼肉排 */
    public static final int COOKING_2_1_N = 3017;
    /** 龙龟蛋饼干 */
    public static final int COOKING_2_2_N = 3018;
    /** 烤奇异鹦鹉 */
    public static final int COOKING_2_3_N = 3019;
    /** 毒蝎串烧 */
    public static final int COOKING_2_4_N = 3020;
    /** 炖伊莱克顿 */
    public static final int COOKING_2_5_N = 3021;
    /** 蜘蛛腿串烧 */
    public static final int COOKING_2_6_N = 3022;
    /** 蟹肉汤 */
    public static final int COOKING_2_7_N = 3023;
    /** 特别的鱼子酱 */
    public static final int COOKING_2_0_S = 3024;
    /** 特别的鳄鱼肉排 */
    public static final int COOKING_2_1_S = 3025;
    /** 特别的龙龟蛋饼干 */
    public static final int COOKING_2_2_S = 3026;
    /** 特别的烤奇异鹦鹉 */
    public static final int COOKING_2_3_S = 3027;
    /** 特别的毒蝎串烧 */
    public static final int COOKING_2_4_S = 3028;
    /** 特别的炖伊莱克顿 */
    public static final int COOKING_2_5_S = 3029;
    /** 特别的蜘蛛腿串烧 */
    public static final int COOKING_2_6_S = 3030;
    /** 特别的蟹肉汤 */
    public static final int COOKING_2_7_S = 3031;
    /** 烤奎斯坦修的螯 */
    public static final int COOKING_3_0_N = 3032;
    /** 烤格利芬肉 */
    public static final int COOKING_3_1_N = 3033;
    /** 亚力安的尾巴肉排 */
    public static final int COOKING_3_2_N = 3034;
    /** 烤巨王龟肉 */
    public static final int COOKING_3_3_N = 3035;
    /** 幼龙翅膀串烧 */
    public static final int COOKING_3_4_N = 3036;
    /** 烤飞龙肉 */
    public static final int COOKING_3_5_N = 3037;
    /** 炖深海鱼肉 */
    public static final int COOKING_3_6_N = 3038;
    /** 邪恶蜥蜴蛋汤 */
    public static final int COOKING_3_7_N = 3039;
    /** 特别的烤奎斯坦修的螯 */
    public static final int COOKING_3_0_S = 3040;
    /** 特别的烤格利芬肉 */
    public static final int COOKING_3_1_S = 3041;
    /** 特别的亚力安的尾巴肉排 */
    public static final int COOKING_3_2_S = 3042;
    /** 特别的烤巨王龟肉 */
    public static final int COOKING_3_3_S = 3043;
    /** 特别的幼龙翅膀串烧 */
    public static final int COOKING_3_4_S = 3044;
    /** 特别的烤飞龙肉 */
    public static final int COOKING_3_5_S = 3045;
    /** 特别的炖深海鱼肉 */
    public static final int COOKING_3_6_S = 3046;
    /** 特别的邪恶蜥蜴蛋汤 */
    public static final int COOKING_3_7_S = 3047;
    /** 料理状态结尾. */
    public static final int COOKING_END = 3047;
    /** 束缚术. */
    public static final int STATUS_FREEZE = 10071;
    /** 木乃伊的咀咒2. */
    public static final int CURSE_PARALYZE2 = 10101;
    /** 可攻击炎魔的力量. */
    public static final int STATUS_CURSE_BARLOG = 1015;
    /** 可攻击火焰之影的力量. */
    public static final int STATUS_CURSE_YAHEE = 1014;

}
