/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT,
 * THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.jrwz.configure;

import java.util.Calendar;

import l1j.jrwz.configure.Annotations.Configures;
import l1j.jrwz.configure.CustomLoaders.WarTimeLoader;
import l1j.jrwz.configure.CustomLoaders.WarTimeUnitLoader;
import l1j.jrwz.server.utils.IntRange;

/**
 * Class <code>Config</code> 服务器设置(外部属性文件).
 * 
 * @author jrwz
 * @version 2012-6-3上午01:23:04
 * @see l1j.jrwz.configure
 * @since JDK1.7
 */
public final class Config {

    // -------------------------------------------------------------------------
    // 配置文件路径 /** Configuration files */
    // -------------------------------------------------------------------------
    /** 服务器配置文件路径. */
    private static final String SERVER = "./config/服务器设置.properties";
    /** 数据库配置文件路径. */
    private static final String SQL = "./config/sql.properties";
    /** 倍率配置文件路径. */
    private static final String RATES = "./config/倍率设置.properties";
    /** 进阶配置文件路径. */
    private static final String ALT = "./config/进阶设置.properties";
    /** 角色配置文件路径. */
    private static final String CHAR = "./config/角色设置.properties";
    /** 战斗特化配置文件路径. */
    private static final String FIGHT = "./config/战斗特化设置.properties";
    /** 纪录配置文件路径. */
    private static final String RECORD = "./config/记录设置.properties";
    /** 其他配置文件路径. */
    private static final String OTHER = "./config/其他设置.properties";
    /** 调整测试配置文件路径. */
    private static final String CHECK = "./config/调整测试设置.properties";

    /** 调试:是否开启检测回溯. */
    @Configures(file = CHECK, key = "Lock")
    public static boolean CHECK_LOCK = false;

    /** 调试:是否开启封包发送错误提示. */
    @Configures(file = CHECK, key = "Packets")
    public static boolean PACKETS = false;

    /** 调试:是否开启奇岩 食人妖精竞赛. */
    @Configures(file = CHECK, key = "isBugBearRace")
    public static boolean BUG_BEAR_RACE = false;

    /** 调试/侦错模式. */
    @Configures(file = SERVER, key = "DebugMode")
    public static boolean DEBUG = false;

    /** 控制台是否显示封包. */
    @Configures(file = SERVER, key = "PrintPacket")
    public static boolean PACKET = false;

    // 线程设定
    /** . */
    public static int THREAD_P_EFFECTS;
    /** . */
    public static int THREAD_P_GENERAL;
    /** . */
    public static int AI_MAX_THREAD;
    /** . */
    @Configures(file = SERVER, key = "GeneralThreadPoolType")
    public static int THREAD_P_TYPE_GENERAL = 0;
    /** . */
    @Configures(file = SERVER, key = "GeneralThreadPoolSize")
    public static int THREAD_P_SIZE_GENERAL = 0;

    // -------------------------------------------------------------------------
    // 游戏服务器相关 /** Server Settings */
    // -------------------------------------------------------------------------
    /** 服务器主机(IP). */
    @Configures(file = SERVER, key = "GameserverHostname")
    public static String GAME_SERVER_HOST_NAME = "*";
    /** 服务器端口. */
    @Configures(file = SERVER, key = "GameserverPort")
    public static int GAME_SERVER_PORT = 2000;
    /** 时区设置. */
    @Configures(file = SERVER, key = "TimeZone")
    public static String TIME_ZONE = "Etc/GMT+8";
    /** 客户端语系. */
    @Configures(file = SERVER, key = "ClientLanguage")
    public static int CLIENT_LANGUAGE = 5;
    /** 客户端编码. */
    public static String CLIENT_LANGUAGE_CODE;
    /** 编码的清单. */
    private static String[] LANGUAGE_CODE_ARRAY = { "UTF8", "EUCKR", "UTF8", "BIG5", "SJIS", "GBK" };
    /** DNS 反向验证. */
    @Configures(file = SERVER, key = "HostnameLookups")
    public static boolean HOSTNAME_LOOKUPS = false;
    /** 客户端无动作时自动断线时间. */
    @Configures(file = SERVER, key = "AutomaticKick")
    public static int AUTOMATIC_KICK = 10;
    /** 自动创建帐号. */
    @Configures(file = SERVER, key = "AutoCreateAccounts")
    public static boolean AUTO_CREATE_ACCOUNTS = false;
    /** 最高在线玩家数量. */
    @Configures(file = SERVER, key = "MaximumOnlineUsers")
    public static int MAX_ONLINE_USERS = 30;
    /** 生成地图快取档案. */
    @Configures(file = SERVER, key = "CacheMapFiles")
    public static boolean CACHE_MAP_FILES = false;
    /** V2 地图 (测试用). */
    @Configures(file = SERVER, key = "LoadV2MapFiles")
    public static boolean LOAD_V2_MAP_FILES = false;
    /** 加速器侦测 (移动间隔). */
    @Configures(file = SERVER, key = "CheckMoveInterval")
    public static boolean CHECK_MOVE_INTERVAL = false;
    /** 加速器侦测 (攻击间隔). */
    @Configures(file = SERVER, key = "CheckAttackInterval")
    public static boolean CHECK_ATTACK_INTERVAL = false;
    /** 加速器侦测 (技能使用间隔). */
    @Configures(file = SERVER, key = "CheckSpellInterval")
    public static boolean CHECK_SPELL_INTERVAL = false;
    /** 设定不正常封包数值,满足条件则切断连线. */
    @Configures(file = SERVER, key = "InjusticeCount")
    public static int INJUSTICE_COUNT = 10;
    /** 设定如果参杂正常封包在不正常封包中,数值满足时 InjusticeCount归 0. */
    @Configures(file = SERVER, key = "JusticeCount")
    public static int JUSTICE_COUNT = 4;
    /** 加速器检查严密度. */
    @Configures(file = SERVER, key = "CheckStrictness")
    public static int CHECK_STRICTNESS = 102;
    /** 加速处罚机制. */
    @Configures(file = SERVER, key = "Punishment")
    public static int ILLEGAL_SPEEDUP_PUNISHMENT = 0;
    /** 伺服器自动存档时间间隔 (单位: 秒). */
    @Configures(file = SERVER, key = "AutosaveInterval")
    public static int AUTOSAVE_INTERVAL = 1200;
    /** 定时自动储存角色装备资料时间间隔 (单位: 秒). */
    @Configures(file = SERVER, key = "AutosaveIntervalOfInventory")
    public static int AUTOSAVE_INTERVAL_INVENTORY = 300;
    /** 技能计数器实施类型. */
    @Configures(file = SERVER, key = "SkillTimerImplType")
    public static int SKILLTIMER_IMPLTYPE = 1;
    /** NpcAI的实施类型. */
    @Configures(file = SERVER, key = "NpcAIImplType")
    public static int NPCAI_IMPLTYPE = 1;
    /** 远程登录控制伺服器. */
    @Configures(file = SERVER, key = "TelnetServer")
    public static boolean TELNET_SERVER = false;
    /** 远程控制的Port号码. */
    @Configures(file = SERVER, key = "TelnetServerPort")
    public static int TELNET_SERVER_PORT = 23;
    /** 发送到一个范围的信息给客户端对像. */
    @Configures(file = SERVER, key = "PcRecognizeRange")
    public static int PC_RECOGNIZE_RANGE = 20;
    /** 人物资讯统一管理(F5~12快捷键和人物血条位置等). */
    @Configures(file = SERVER, key = "CharacterConfigInServerSide")
    public static boolean CHARACTER_CONFIG_IN_SERVER_SIDE = false;
    /** 双开(同IP同时连线). */
    @Configures(file = SERVER, key = "Allow2PC")
    public static boolean ALLOW_2PC = false;
    /** 允许降等的水平范围（检测死亡降等范围）. */
    @Configures(file = SERVER, key = "LevelDownRange")
    public static int LEVEL_DOWN_RANGE = 0;
    /** 瞬移控制. */
    @Configures(file = SERVER, key = "SendPacketBeforeTeleport")
    public static boolean SEND_PACKET_BEFORE_TELEPORT = false;
    /** CMD互动指令. */
    @Configures(file = SERVER, key = "CmdActive")
    public static boolean CMD_ACTIVE = false;
    /** 循环时间设置 (单位:分钟). */
    @Configures(file = SERVER, key = "AnnouncementsCycleTime")
    public static int Announcements_Cycle_Time = 30;
    /** 自动显示公告修改时间. */
    @Configures(file = SERVER, key = "AnnounceTimeDisplay")
    public static boolean Announcements_Cycle_Modify_Time = false;

    // -------------------------------------------------------------------------
    // 游戏数据库相关 /** SQL Settings */
    // -------------------------------------------------------------------------
    /** 数据库驱动程序. */
    @Configures(file = SQL, key = "Driver")
    public static String DB_DRIVER = "com.mysql.jdbc.Driver";
    /** 数据库路径. */
    @Configures(file = SQL, key = "URL")
    public static String DB_URL = "jdbc:mysql://localhost/kanmeizhi?useUnicode=true&characterEncoding=utf8";
    /** 数据库账号. */
    @Configures(file = SQL, key = "Login")
    public static String DB_LOGIN = "root";
    /** 数据库密码. */
    @Configures(file = SQL, key = "Password")
    public static String DB_PASSWORD = "root";
    /** 数据库资源泄漏检测. */
    @Configures(file = SQL, key = "EnableDatabaseResourceLeaksDetection")
    public static boolean DETECT_DB_RESOURCE_LEAKS = false;
    /** MySQL定时自动备份. */
    @Configures(file = SQL, key = "MysqlAutoBackup")
    public static int MYSQL_AUTO_BACKUP = 0;
    /** 备份的输出SQL是否启用GZip压缩. */
    @Configures(file = SQL, key = "CompressGzip")
    public static boolean COMPRESS_G_ZIP = false;
    // -------------------------------------------------------------------------
    // 游戏倍率相关 /** Rates Settings */
    // -------------------------------------------------------------------------
    /** 经验值倍率. */
    @Configures(file = RATES, key = "RateXp")
    public static double RATE_XP = 1.0;
    /** 正义值倍率. */
    @Configures(file = RATES, key = "RateLawful")
    public static double RATE_LA = 1.0;
    /** 友好度倍率. */
    @Configures(file = RATES, key = "RateKarma")
    public static double RATE_KARMA = 1.0;
    /** 掉落金钱倍率. */
    @Configures(file = RATES, key = "RateDropAdena")
    public static double RATE_DROP_ADENA = 1.0;
    /** 掉落物品倍率. */
    @Configures(file = RATES, key = "RateDropItems")
    public static double RATE_DROP_ITEMS = 1.0;
    /** 冲武器成功率. */
    @Configures(file = RATES, key = "EnchantChanceWeapon")
    public static int ENCHANT_CHANCE_WEAPON = 1;
    /** 冲防具成功率. */
    @Configures(file = RATES, key = "EnchantChanceArmor")
    public static int ENCHANT_CHANCE_ARMOR = 1;
    /** 属性强化成功率. */
    @Configures(file = RATES, key = "AttrEnchantChance")
    public static int ATTR_ENCHANT_CHANCE = 10;
    /** 角色负重倍率. */
    @Configures(file = RATES, key = "RateWeightLimit")
    public static double RATE_WEIGHT_LIMIT = 1.0;
    /** 宠物负重倍率. */
    @Configures(file = RATES, key = "RateWeightLimitforPet")
    public static double RATE_WEIGHT_LIMIT_PET = 1.0;
    /** 商店贩卖价格倍率. */
    @Configures(file = RATES, key = "RateShopSellingPrice")
    public static double RATE_SHOP_SELLING_PRICE = 1.0;
    /** 商店收购价格倍率. */
    @Configures(file = RATES, key = "RateShopPurchasingPrice")
    public static double RATE_SHOP_PURCHASING_PRICE = 1.0;
    /** 航海日志合成几率. */
    @Configures(file = RATES, key = "CreateChanceDiary")
    public static int CREATE_CHANCE_DIARY = 33;
    /** 净化的部分. */
    @Configures(file = RATES, key = "CreateChanceRecollection")
    public static int CREATE_CHANCE_RECOLLECTION = 90;
    /** 神秘药水. */
    @Configures(file = RATES, key = "CreateChanceMysterious")
    public static int CREATE_CHANCE_MYSTERIOUS = 90;
    /** 被加工了的宝石. */
    @Configures(file = RATES, key = "CreateChanceProcessing")
    public static int CREATE_CHANCE_PROCESSING = 90;
    /** 被加工了的钻石. */
    @Configures(file = RATES, key = "CreateChanceProcessingDiamond")
    public static int CREATE_CHANCE_PROCESSING_DIAMOND = 90;
    /** 完整的召唤球. */
    @Configures(file = RATES, key = "CreateChanceDantes")
    public static int CREATE_CHANCE_DANTES = 50;
    /** 不起眼的古老项链. */
    @Configures(file = RATES, key = "CreateChanceAncientAmulet")
    public static int CREATE_CHANCE_ANCIENT_AMULET = 90;
    /** 封印的历史书. */
    @Configures(file = RATES, key = "CreateChanceHistory")
    public static int CREATE_CHANCE_HISTORY_BOOK = 50;
    /** 附魔石类型. */
    @Configures(file = RATES, key = "MagicStoneAttr")
    public static int MAGIC_STONE_TYPE = 50;
    /** 附魔石阶级. */
    @Configures(file = RATES, key = "MagicStoneLevel")
    public static int MAGIC_STONE_LEVEL = 50;

    // -------------------------------------------------------------------------
    // 游戏进阶相关 /** AltSettings Settings */
    // -------------------------------------------------------------------------
    /** 全体聊天最低等级限制. */
    @Configures(file = ALT, key = "GlobalChatLevel")
    public static int GLOBAL_CHAT_LEVEL = 30;
    /** 密语最低等级限制. */
    @Configures(file = ALT, key = "WhisperChatLevel")
    public static int WHISPER_CHAT_LEVEL = 5;
    /** 自动取得道具的方式. */
    @Configures(file = ALT, key = "AutoLoot")
    public static int AUTO_LOOT = 2;
    /** 道具掉落的范围大小. */
    @Configures(file = ALT, key = "LootingRange")
    public static int LOOTING_RANGE = 3;
    /** Non-PvP设定. */
    @Configures(file = ALT, key = "NonPvP")
    public static boolean ALT_NONPVP = false;
    /** GM是否显示伤害讯息. */
    @Configures(file = ALT, key = "AttackMessageOn")
    public static boolean ALT_ATKMSG = false;
    /** 自己更改称号. */
    @Configures(file = ALT, key = "ChangeTitleByOneself")
    public static boolean CHANGE_TITLE_BY_ONESELF = false;
    /** 血盟人数上限. */
    @Configures(file = ALT, key = "MaxClanMember")
    public static int MAX_CLAN_MEMBER = 0;
    /** 血盟联盟系统. */
    @Configures(file = ALT, key = "ClanAlliance")
    public static boolean CLAN_ALLIANCE = false;
    /** 组队人数上限 . */
    @Configures(file = ALT, key = "MaxPT")
    public static int MAX_PT = 8;
    /** 组队聊天人数上限. */
    @Configures(file = ALT, key = "MaxChatPT")
    public static int MAX_CHAT_PT = 8;
    /** 攻城战中红人死亡后是否会受到处罚. */
    @Configures(file = ALT, key = "SimWarPenalty")
    public static boolean SIM_WAR_PENALTY = false;
    /** 重新登入时是否在出生地. */
    @Configures(file = ALT, key = "GetBack")
    public static boolean GET_BACK = false;
    /** 地图上地面道具删除设置. */
    @Configures(file = ALT, key = "ItemDeletionType")
    public static String ALT_ITEM_DELETION_TYPE = "auto";
    /** 物品在地面自动清除掉的时间. */
    @Configures(file = ALT, key = "ItemDeletionTime")
    public static int ALT_ITEM_DELETION_TIME = 30;
    /** 人物周围不清除物品范围大小. */
    @Configures(file = ALT, key = "ItemDeletionRange")
    public static int ALT_ITEM_DELETION_RANGE = 5;
    /** 是否开启GM商店. */
    @Configures(file = ALT, key = "GMshop")
    public static boolean ALT_GMSHOP = false;
    /** GM商店编号最小值. */
    @Configures(file = ALT, key = "GMshopMinID")
    public static int ALT_GMSHOP_MIN_ID = 0xffffffff;
    /** GM商店编号最大值. */
    @Configures(file = ALT, key = "GMshopMaxID")
    public static int ALT_GMSHOP_MAX_ID = 0xffffffff;
    /** 南瓜怪任务开关. */
    @Configures(file = ALT, key = "HalloweenIvent")
    public static boolean ALT_HALLOWEENIVENT = false;
    /** 日本特典道具NPC开关. */
    @Configures(file = ALT, key = "JpPrivileged")
    public static boolean ALT_JPPRIVILEGED = false;
    /** 说话卷轴任务开关. */
    @Configures(file = ALT, key = "TalkingScrollQuest")
    public static boolean ALT_TALKINGSCROLLQUEST = false;
    /** /who 指令是否可以使用. */
    @Configures(file = ALT, key = "WhoCommand")
    public static boolean ALT_WHO_COMMAND = false;
//    /** /who 指令如何显示. 0：系统广播；1弹出框 */
//    @Configures(file = ALT, key = "WhoShowType ")
//    public static int ALT_WHO_SHOW_TYPE = 0;
    /** 99级是否可以获得返生药水. */
    @Configures(file = ALT, key = "RevivalPotion")
    public static boolean ALT_REVIVAL_POTION = false;
    /** 攻城战时间. */
    @Configures(file = ALT, key = "WarTime", loader = WarTimeLoader.class)
    public static int ALT_WAR_TIME = 2;
    /** 攻城战时间单位. */
    @Configures(file = ALT, key = "WarTime", loader = WarTimeUnitLoader.class)
    public static int ALT_WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
    /** 攻城日的间隔. */
    @Configures(file = ALT, key = "WarInterval", loader = WarTimeLoader.class)
    public static int ALT_WAR_INTERVAL = 4;
    /** 攻城日的间隔单位. */
    @Configures(file = ALT, key = "WarInterval", loader = WarTimeUnitLoader.class)
    public static int ALT_WAR_INTERVAL_UNIT = Calendar.DATE;
    /** 城堡纳税倍率. */
    // XXX
    // @Configures(file = ALT, key = "")
    // public static int ALT_RATE_OF_DUTY;
    /** 范围性怪物刷新. */
    @Configures(file = ALT, key = "SpawnHomePoint")
    public static boolean SPAWN_HOME_POINT = false;
    /** 怪物刷新的范围大小. */
    @Configures(file = ALT, key = "SpawnHomePointRange")
    public static int SPAWN_HOME_POINT_RANGE = 8;
    /** 怪物出生点设定最小. */
    @Configures(file = ALT, key = "SpawnHomePointCount")
    public static int SPAWN_HOME_POINT_COUNT = 2;
    /** 怪物出生点设定的最大. */
    @Configures(file = ALT, key = "SpawnHomePointDelay")
    public static int SPAWN_HOME_POINT_DELAY = 100;
    /** 服务器启动时Boss是否出现. */
    @Configures(file = ALT, key = "InitBossSpawn")
    public static boolean INIT_BOSS_SPAWN = false;
    /** 妖精森林 元素石 的数量. */
    @Configures(file = ALT, key = "ElementalStoneAmount")
    public static int ELEMENTAL_STONE_AMOUNT = 300;
    /** 盟屋税金的支付期限(日). */
    @Configures(file = ALT, key = "HouseTaxInterval")
    public static int HOUSE_TAX_INTERVAL = 10;
    /** 魔法娃娃召唤数量上限. */
    @Configures(file = ALT, key = "MaxDollCount")
    public static int MAX_DOLL_COUNT = 1;
    /** 释放元素技能的使用. */
    @Configures(file = ALT, key = "ReturnToNature")
    public static boolean RETURN_TO_NATURE = false;
    /** NPC(召唤, 宠物)身上可以持有的最大物品数量. */
    @Configures(file = ALT, key = "MaxNpcItem")
    public static int MAX_NPC_ITEM = 8;
    /** 个人仓库物品上限数量. */
    @Configures(file = ALT, key = "MaxPersonalWarehouseItem")
    public static int MAX_PERSONAL_WAREHOUSE_ITEM = 150;
    /** 血盟仓库物品上限数量. */
    @Configures(file = ALT, key = "MaxClanWarehouseItem")
    public static int MAX_CLAN_WAREHOUSE_ITEM = 200;
    /** 角色等级30以上，删除角色是否要等待7天. */
    @Configures(file = ALT, key = "DeleteCharacterAfter7Days")
    public static boolean DELETE_CHARACTER_AFTER_7DAYS = false;
    /** NPC死亡后尸体消失时间（秒）. */
    @Configures(file = ALT, key = "NpcDeletionTime")
    public static int NPC_DELETION_TIME = 10;
    /** 预设角色数量. */
    @Configures(file = ALT, key = "DefaultCharacterSlot")
    public static int DEFAULT_CHARACTER_SLOT = 6;
    /** 妖精森林NPC道具重置时间. */
    @Configures(file = ALT, key = "GDropItemTime")
    public static int GDROPITEM_TIME = 10;

    // -------------------------------------------------------------------------
    // 游戏角色相关 /** Character Settings */
    // -------------------------------------------------------------------------
    /** 王族 HP 上限. */
    @Configures(file = CHAR, key = "PrinceMaxHP")
    public static int PRINCE_MAX_HP = 1000;
    /** 王族 MP 上限. */
    @Configures(file = CHAR, key = "PrinceMaxMP")
    public static int PRINCE_MAX_MP = 800;
    /** 骑士 HP 上限. */
    @Configures(file = CHAR, key = "KnightMaxHP")
    public static int KNIGHT_MAX_HP = 1400;
    /** 骑士 MP 上限. */
    @Configures(file = CHAR, key = "KnightMaxMP")
    public static int KNIGHT_MAX_MP = 600;
    /** 精灵 HP 上限. */
    @Configures(file = CHAR, key = "ElfMaxHP")
    public static int ELF_MAX_HP = 1000;
    /** 精灵 MP 上限. */
    @Configures(file = CHAR, key = "ElfMaxMP")
    public static int ELF_MAX_MP = 900;
    /** 法师 HP 上限. */
    @Configures(file = CHAR, key = "WizardMaxHP")
    public static int WIZARD_MAX_HP = 800;
    /** 法师 MP 上限. */
    @Configures(file = CHAR, key = "WizardMaxMP")
    public static int WIZARD_MAX_MP = 1200;
    /** 黑暗精灵 HP 上限. */
    @Configures(file = CHAR, key = "DarkelfMaxHP")
    public static int DARKELF_MAX_HP = 1000;
    /** 黑暗精灵 MP 上限. */
    @Configures(file = CHAR, key = "DarkelfMaxMP")
    public static int DARKELF_MAX_MP = 900;
    /** 龙骑士 HP 上限. */
    @Configures(file = CHAR, key = "DragonKnightMaxHP")
    public static int DRAGONKNIGHT_MAX_HP = 1400;
    /** 龙骑士 MP 上限. */
    @Configures(file = CHAR, key = "DragonKnightMaxMP")
    public static int DRAGONKNIGHT_MAX_MP = 600;
    /** 幻术师 HP 上限. */
    @Configures(file = CHAR, key = "IllusionistMaxHP")
    public static int ILLUSIONIST_MAX_HP = 900;
    /** 幻术师 MP 上限. */
    @Configures(file = CHAR, key = "IllusionistMaxMP")
    public static int ILLUSIONIST_MAX_MP = 1100;
    @Configures(file = CHAR, key = "Lv50Exp")
    public static int LV50_EXP = 1;
    @Configures(file = CHAR, key = "Lv51Exp")
    public static int LV51_EXP = 1;
    @Configures(file = CHAR, key = "Lv52Exp")
    public static int LV52_EXP = 1;
    @Configures(file = CHAR, key = "Lv53Exp")
    public static int LV53_EXP = 1;
    @Configures(file = CHAR, key = "Lv54Exp")
    public static int LV54_EXP = 1;
    @Configures(file = CHAR, key = "Lv55Exp")
    public static int LV55_EXP = 1;
    @Configures(file = CHAR, key = "Lv56Exp")
    public static int LV56_EXP = 1;
    @Configures(file = CHAR, key = "Lv57Exp")
    public static int LV57_EXP = 1;
    @Configures(file = CHAR, key = "Lv58Exp")
    public static int LV58_EXP = 1;
    @Configures(file = CHAR, key = "Lv59Exp")
    public static int LV59_EXP = 1;
    @Configures(file = CHAR, key = "Lv60Exp")
    public static int LV60_EXP = 1;
    @Configures(file = CHAR, key = "Lv61Exp")
    public static int LV61_EXP = 1;
    @Configures(file = CHAR, key = "Lv62Exp")
    public static int LV62_EXP = 1;
    @Configures(file = CHAR, key = "Lv63Exp")
    public static int LV63_EXP = 1;
    @Configures(file = CHAR, key = "Lv64Exp")
    public static int LV64_EXP = 1;
    @Configures(file = CHAR, key = "Lv65Exp")
    public static int LV65_EXP = 1;
    @Configures(file = CHAR, key = "Lv66Exp")
    public static int LV66_EXP = 1;
    @Configures(file = CHAR, key = "Lv67Exp")
    public static int LV67_EXP = 1;
    @Configures(file = CHAR, key = "Lv68Exp")
    public static int LV68_EXP = 1;
    @Configures(file = CHAR, key = "Lv69Exp")
    public static int LV69_EXP = 1;
    @Configures(file = CHAR, key = "Lv70Exp")
    public static int LV70_EXP = 1;
    @Configures(file = CHAR, key = "Lv71Exp")
    public static int LV71_EXP = 1;
    @Configures(file = CHAR, key = "Lv72Exp")
    public static int LV72_EXP = 1;
    @Configures(file = CHAR, key = "Lv73Exp")
    public static int LV73_EXP = 1;
    @Configures(file = CHAR, key = "Lv74Exp")
    public static int LV74_EXP = 1;
    @Configures(file = CHAR, key = "Lv75Exp")
    public static int LV75_EXP = 1;
    @Configures(file = CHAR, key = "Lv76Exp")
    public static int LV76_EXP = 1;
    @Configures(file = CHAR, key = "Lv77Exp")
    public static int LV77_EXP = 1;
    @Configures(file = CHAR, key = "Lv78Exp")
    public static int LV78_EXP = 1;
    @Configures(file = CHAR, key = "Lv79Exp")
    public static int LV79_EXP = 1;
    @Configures(file = CHAR, key = "Lv80Exp")
    public static int LV80_EXP = 1;
    @Configures(file = CHAR, key = "Lv81Exp")
    public static int LV81_EXP = 1;
    @Configures(file = CHAR, key = "Lv82Exp")
    public static int LV82_EXP = 1;
    @Configures(file = CHAR, key = "Lv83Exp")
    public static int LV83_EXP = 1;
    @Configures(file = CHAR, key = "Lv84Exp")
    public static int LV84_EXP = 1;
    @Configures(file = CHAR, key = "Lv85Exp")
    public static int LV85_EXP = 1;
    @Configures(file = CHAR, key = "Lv86Exp")
    public static int LV86_EXP = 1;
    @Configures(file = CHAR, key = "Lv87Exp")
    public static int LV87_EXP = 1;
    @Configures(file = CHAR, key = "Lv88Exp")
    public static int LV88_EXP = 1;
    @Configures(file = CHAR, key = "Lv89Exp")
    public static int LV89_EXP = 1;
    @Configures(file = CHAR, key = "Lv90Exp")
    public static int LV90_EXP = 1;
    @Configures(file = CHAR, key = "Lv91Exp")
    public static int LV91_EXP = 1;
    @Configures(file = CHAR, key = "Lv92Exp")
    public static int LV92_EXP = 1;
    @Configures(file = CHAR, key = "Lv93Exp")
    public static int LV93_EXP = 1;
    @Configures(file = CHAR, key = "Lv94Exp")
    public static int LV94_EXP = 1;
    @Configures(file = CHAR, key = "Lv95Exp")
    public static int LV95_EXP = 1;
    @Configures(file = CHAR, key = "Lv96Exp")
    public static int LV96_EXP = 1;
    @Configures(file = CHAR, key = "Lv97Exp")
    public static int LV97_EXP = 1;
    @Configures(file = CHAR, key = "Lv98Exp")
    public static int LV98_EXP = 1;
    @Configures(file = CHAR, key = "Lv99Exp")
    public static int LV99_EXP = 1;
    @Configures(file = CHAR, key = "Lv100Exp")
    public static int LV100_EXP = 1;
    @Configures(file = CHAR, key = "Lv101Exp")
    public static int LV101_EXP = 1;
    @Configures(file = CHAR, key = "Lv102Exp")
    public static int LV102_EXP = 1;
    @Configures(file = CHAR, key = "Lv103Exp")
    public static int LV103_EXP = 1;
    @Configures(file = CHAR, key = "Lv104Exp")
    public static int LV104_EXP = 1;
    @Configures(file = CHAR, key = "Lv105Exp")
    public static int LV105_EXP = 1;
    @Configures(file = CHAR, key = "Lv106Exp")
    public static int LV106_EXP = 1;
    @Configures(file = CHAR, key = "Lv107Exp")
    public static int LV107_EXP = 1;
    @Configures(file = CHAR, key = "Lv108Exp")
    public static int LV108_EXP = 1;
    @Configures(file = CHAR, key = "Lv109Exp")
    public static int LV109_EXP = 1;
    @Configures(file = CHAR, key = "Lv110Exp")
    public static int LV110_EXP = 1;

    // -------------------------------------------------------------------------
    // 游戏战斗特化相关 /** Fights Settings */
    // -------------------------------------------------------------------------
    /** 启动战斗特化系统. */
    @Configures(file = FIGHT, key = "FightIsActive")
    public static boolean FIGHT_IS_ACTIVE = false;
    /** 新手保护系统(遭遇的守护). */
    @Configures(file = FIGHT, key = "NoviceProtectionIsActive")
    public static boolean NOVICE_PROTECTION_IS_ACTIVE = false;
    /** 被归类为新手的等级上限. */
    @Configures(file = FIGHT, key = "NoviceMaxLevel")
    public static int NOVICE_MAX_LEVEL = 20;
    /** 启动新手保护机制. */
    @Configures(file = FIGHT, key = "ProtectionLevelRange")
    public static int NOVICE_PROTECTION_LEVEL_RANGE = 10;

    // -------------------------------------------------------------------------
    // 游戏记录相关 /** Record Settings */
    // -------------------------------------------------------------------------
    /** 武器强化. */
    @Configures(file = RECORD, key = "LoggingWeaponEnchant")
    public static int LOGGING_WEAPON_ENCHANT = 0;
    /** 防具强化. */
    @Configures(file = RECORD, key = "LoggingArmorEnchant")
    public static int LOGGING_ARMOR_ENCHANT = 0;
    /** 一般频道. */
    @Configures(file = RECORD, key = "LoggingChatNormal")
    public static boolean LOGGING_CHAT_NORMAL = false;
    /** 密语频道. */
    @Configures(file = RECORD, key = "LoggingChatWhisper")
    public static boolean LOGGING_CHAT_WHISPER = false;
    /** 大喊频道. */
    @Configures(file = RECORD, key = "LoggingChatShout")
    public static boolean LOGGING_CHAT_SHOUT = false;
    /** 广播频道. */
    @Configures(file = RECORD, key = "LoggingChatWorld")
    public static boolean LOGGING_CHAT_WORLD = false;
    /** 血盟频道. */
    @Configures(file = RECORD, key = "LoggingChatClan")
    public static boolean LOGGING_CHAT_CLAN = false;
    /** 组队频道 . */
    @Configures(file = RECORD, key = "LoggingChatParty")
    public static boolean LOGGING_CHAT_PARTY = false;
    /** 联盟频道. */
    @Configures(file = RECORD, key = "LoggingChatCombined")
    public static boolean LOGGING_CHAT_COMBINED = false;
    /** 聊天队伍频道. */
    @Configures(file = RECORD, key = "LoggingChatChatParty")
    public static boolean LOGGING_CHAT_CHAT_PARTY = false;
    /** 交易纪录. */
    @Configures(file = RECORD, key = "writeTradeLog")
    public static boolean writeTradeLog = false;
    /** 记录加速器讯息. */
    @Configures(file = RECORD, key = "writeRobotsLog")
    public static boolean writeRobotsLog = false;
    /** 丢弃物品纪录. */
    @Configures(file = RECORD, key = "writeDropLog")
    public static boolean writeDropLog = false;

    // -------------------------------------------------------------------------
    // 其他设置相关 /** OtherSettings Settings */
    // -------------------------------------------------------------------------
    /** 是否新建角色即为GM. */
    @Configures(file = OTHER, key = "NewCreateRoleSetGM")
    public static boolean NEW_CREATE_ROLE_SET_GM = false;
    /** 是否显示NpcId. */
    @Configures(file = OTHER, key = "ShowNpcId")
    public static boolean SHOW_NPC_ID = false;
    /** 升级血魔满. */
    @Configures(file = OTHER, key = "LvUpHpMpFull")
    public static boolean LV_UP_HP_MP_FULL = false;
    /** 伺服器重启时间. */
    @Configures(file = OTHER, key = "RestartTime")
    public static int REST_TIME = 720;
    /** 整点报时. */
    @Configures(file = OTHER, key = "HourlyChime")
    public static boolean HOURLY_CHIME = false;

    /** 给予系统－是否在线一段时间给予物品 True=是, False=否. */
    @Configures(file = OTHER, key = "giftOpen")
    public static boolean GIFT_OPEN = true;
    /** 给予系统－间隔多久给予. */
    @Configures(file = OTHER, key = "giftTime")
    public static int GIFT_TIME = 1;
    /** 给予系统－给予物品ID. */
    @Configures(file = OTHER, key = "giftItemID")
    public static int GIFT_ITEM_ID = 40010;
    /** 给予系统－给予物品ID数量. */
    @Configures(file = OTHER, key = "giftCount")
    public static int GIFT_COUNT = 1;

    // -------------------------------------------------------------------------
    // 其他设定 /** Other files */
    // -------------------------------------------------------------------------
    /** 吸收每个 NPC 的 MP 上限. */
    public static final int MANA_DRAIN_LIMIT_PER_NPC = 40;
    /** 每一次攻击吸收的 MP 上限(玛那、钢铁玛那）. */
    public static final int MANA_DRAIN_LIMIT_PER_SOM_ATTACK = 9;

    /**
     * 加载客户端编码.
     */
    private static void clientLanguageCodeLoad() {
        CLIENT_LANGUAGE_CODE = LANGUAGE_CODE_ARRAY[CLIENT_LANGUAGE];
    }

    /**
     * 加载.
     */
    public static void load() {
        new ConfigureLoader().load(Config.class);
        clientLanguageCodeLoad();
        validate();
    }

    /**
     * 验证.
     */
    private static void validate() {
        if (!IntRange.includes(Config.ALT_ITEM_DELETION_RANGE, 0, 5)) {
            throw new IllegalStateException("ItemDeletionRange 的设定值超出( 0 ~ 5 )范围。");
        }

        if (!IntRange.includes(Config.ALT_ITEM_DELETION_TIME, 1, 35791)) {
            throw new IllegalStateException("ItemDeletionTime 的设定值超出( 1 ~ 35791 )范围。");
        }
    }

    /** 服务器设置(外部属性文件). */
    private Config() {
    }

}
