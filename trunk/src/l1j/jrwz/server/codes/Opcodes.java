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
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.jrwz.server.codes;

/**
 * 封包代码 - 提供者:LovieAlice.
 * 
 * @3.52 TW <b>12042501 Lin.bin Opcodes</b>
 */
public final class Opcodes {
    // 3.52C Client Packet （客户端代码）
    /** 请求 增加记忆坐标. */
    public static final int C_OPCODE_BOOKMARK = 1;
    /** 城堡宝库(请求存入资金). */
    public static final int C_OPCODE_DEPOSIT = 2;
    /** 请求 执行线上人物列表命令(GM管理选单)传送至指定的外挂使用者身旁. */
    public static final int C_OPCODE_CALL = 3;
    /** 请求 驱逐血盟成员. */
    public static final int C_OPCODE_BANCLAN = 4;
    /** 请求 删除布告栏内容. */
    public static final int C_OPCODE_BOARDDELETE = 5;
    /** 请求 阅读单个布告栏内容. */
    public static final int C_OPCODE_BOARDREAD = 6;
    /** 请求 脱离队伍. */
    public static final int C_OPCODE_LEAVEPARTY = 7;
    /** 请求 查看下一页公布栏讯息. */
    public static final int C_OPCODE_BOARDNEXT = 8;
    /** 请求 使用远程攻击. */
    public static final int C_OPCODE_ARROWATTACK = 11;
    /** 请求 使用物品. */
    public static final int C_OPCODE_USEITEM = 12;
    /** 请求 取得列表中的物品. */
    public static final int C_OPCODE_RESULT = 13;
    /** 请求 决斗. */
    public static final int C_OPCODE_FIGHT = 14;
    /** 请求 踢出队伍. */
    public static final int C_OPCODE_BANPARTY = 18;
    /** 请求 决定下次围城时间(官方已取消使用)-->修正城堡总管全部功能. */
    public static final int C_OPCODE_CHANGEWARTIME = 20;
    /** 请求 增加交易物品(双方交易). */
    public static final int C_OPCODE_TRADEADDITEM = 21;
    /** 请求 退出幽灵模式(观看模式). */
    public static final int C_OPCODE_EXIT_GHOST = 24;
    /** 请求 交易(双方交易). */
    public static final int C_OPCODE_TRADE = 27;
    /** 请求 赋予封号(/title). */
    public static final int C_OPCODE_TITLE = 28;
    /** 请求 门的控制(开关)/宝箱的开启. */
    public static final int C_OPCODE_DOOR = 29;
    /** 请求 传送 (进入地监). */
    public static final int C_OPCODE_ENTERPORTAL = 31;
    /** 请求 学习魔法清单(金币). */
    public static final int C_OPCODE_SKILLBUY = 32;
    /** 请求 雇请佣兵列表(购买佣兵完成). */
    public static final int C_OPCODE_HIRESOLDIER = 33;
    /** 请求 学习魔法清单(材料). */
    public static final int C_OPCODE_SKILLBUYITEM = 34;
    /** 请求 结婚(/propose). */
    public static final int C_OPCODE_PROPOSE = 35;
    /** 请求 使用宠物装备. */
    public static final int C_OPCODE_USEPETITEM = 36;
    /** 请求 改变角色面向. */
    public static final int C_OPCODE_CHANGEHEADING = 37;
    /** 请求 开设个人商店. */
    public static final int C_OPCODE_SHOP = 38;
    /** 请求 传送位置&视窗失焦. */
    public static final int C_OPCODE_SENDLOCATION = 39;
    /** 请求 使用一般聊天频道. */
    public static final int C_OPCODE_CHAT = 40;
    /** 请求 上传盟徽. */
    public static final int C_OPCODE_EMBLEM = 41;
    /** 请求 删除好友. */
    public static final int C_OPCODE_DELBUDDY = 42;
    /** 请求 完成学习魔法(材料). */
    public static final int C_OPCODE_SKILLBUYOKITEM = 44;
    /** 请求 更新血盟数据(例如盟标). */
    public static final int C_OPCODE_CLAN = 45;
    /** 请求 删除记忆坐标. */
    public static final int C_OPCODE_BOOKMARKDELETE = 46;
    /** 请求 移动角色. */
    public static final int C_OPCODE_MOVECHAR = 47;
    /** 请求 查询好友名单. */
    public static final int C_OPCODE_BUDDYLIST = 48;
    /** 请求 死亡后重新开始. */
    public static final int C_OPCODE_RESTART = 49;
    /** 请求 完成交易(双方交易). */
    public static final int C_OPCODE_TRADEADDOK = 54;
    /** 未使用 - 请求 简讯服务(传送简讯). */
    public static final int C_OPCODE_MSG = 55;
    /** 请求 删除物品. */
    public static final int C_OPCODE_DELETEINVENTORYITEM = 56;
    /** 请求 创造角色. */
    public static final int C_OPCODE_NEWCHAR = 57;
    /** 请求 纪录快捷键. */
    public static final int C_OPCODE_CHARACTERCONFIG = 59;
    /** 请求 回到登入画面. */
    public static final int C_OPCODE_RETURNTOLOGIN = 60;
    /** 请求 修理损坏的道具. */
    public static final int C_OPCODE_SELECTLIST = 62;
    /** 请求 查询队伍成员名单. */
    public static final int C_OPCODE_PARTYLIST = 63;
    /** 请求 管理城堡治安. */
    public static final int C_OPCODE_CASTLESECURITY = 64;
    /** 请求 进入游戏. */
    public static final int C_OPCODE_LOGINTOSERVER = 65;
    /** 请求 切换角色. */
    public static final int C_OPCODE_CHANGECHAR = 66;
    /** 未使用 - 请求 自动登入伺服器(使用乐豆自动登录). */
    public static final int C_OPCODE_AUTO = 68;
    /** 请求 返回角色选择画面&离线. */
    public static final int C_OPCODE_BACK_CHAR_SELECTION_SCREEN = 68;
    /** 请求 更新时间/连线状态. */
    public static final int C_OPCODE_KEEPALIVE = 69;
    /** 请求 变更仓库密码 && 送出仓库密码. */
    public static final int C_OPCODE_WAREHOUSELOCK = 70;
    /** 城堡宝库(请求领出资金). */
    public static final int C_OPCODE_DRAWAL = 71;
    /** 请求 物件对话视窗数量选取结果. */
    public static final int C_OPCODE_AMOUNT = 72;
    /** 请求 新增好友. */
    public static final int C_OPCODE_ADDBUDDY = 73;
    /** 请求 执行物件对话视窗动作/结果. */
    public static final int C_OPCODE_NPCACTION = 74;
    /** 请求 查询在线游戏人数(/who). */
    public static final int C_OPCODE_WHO = 75;
    /** 请求 完成学习魔法(金币). */
    public static final int C_OPCODE_SKILLBUYOK = 76;
    /** 请求 更新周围物件(传送后解除传送锁定). */
    public static final int C_OPCODE_TELEPORT = 77;
    /** 请求 钓鱼收竿. */
    public static final int C_OPCODE_FISHCLICK = 78;
    /** 请求 角色攻击. */
    public static final int C_OPCODE_ATTACK = 79;
    /** 请求 登入伺服器. */
    public static final int C_OPCODE_LOGINPACKET = 80;
    /** 请求 变更领地税率. */
    public static final int C_OPCODE_TAXRATE = 81;
    /** 请求 选取观看频道or配置角色设定. */
    public static final int C_OPCODE_LOGINTOSERVEROK = 83;
    /** 请求 查询血盟成员. */
    public static final int C_OPCODE_PLEDGE = 84;
    /** 请求 宠物回报选单. */
    public static final int C_OPCODE_PETMENU = 85;
    /** 请求 物件对话视窗. */
    public static final int C_OPCODE_NPCTALK = 87;
    /** 请求 请求验证客户端版本. */
    public static final int C_OPCODE_CLIENTVERSION = 89;
    /** 请求 打开邮箱. */
    public static final int C_OPCODE_MAIL = 90;
    /** 请求 查询损坏的道具(维修物品清单). */
    public static final int C_OPCODE_FIX_WEAPON_LIST = 91;
    /** 请求 下一步(伺服器公告视窗后 显示角色列表). */
    public static final int C_OPCODE_COMMONCLICK = 92;
    /** 请求 攻击指定物件(宠物&召唤). */
    public static final int C_OPCODE_SELECTTARGET = 94;
    /** 请求 丢弃物品(丢弃至地面). */
    public static final int C_OPCODE_DROPITEM = 95;
    /** 请求 取消交易(双方交易). */
    public static final int C_OPCODE_TRADEADDCANCEL = 96;
    /** 请求 使用技能. */
    public static final int C_OPCODE_USESKILL = 98;
    /** 请求 离开游戏. */
    public static final int C_OPCODE_QUITGAME = 99;
    /** 请求 查询PK次数(/checkpk). */
    public static final int C_OPCODE_CHECKPK = 100;
    /** 请求 给予角色血盟阶级(/rank 人物 见习). */
    public static final int C_OPCODE_RANK = 101;
    /** 请求 宣战/投降/休战. */
    public static final int C_OPCODE_WAR = 102;
    /** 请求 座标异常重整(回溯检测用). */
    public static final int C_OPCODE_MOVELOCK = 103;
    /** 请求 使用广播聊天频道. */
    public static final int C_OPCODE_CHATGLOBAL = 104;
    /** 请求 脱离血盟. */
    public static final int C_OPCODE_LEAVECLANE = 105;
    /** 请求 读取 布告栏/拍卖公告 讯息列表(浏览布告栏). */
    public static final int C_OPCODE_BOARD = 106;
    /** 请求 使用密语聊天频道. */
    public static final int C_OPCODE_CHATWHISPER = 108;
    /** 请求 加入血盟. */
    public static final int C_OPCODE_JOINCLAN = 109;
    /** 请求 删除角色. */
    public static final int C_OPCODE_DELETECHAR = 111;
    /** 请求 角色表情动作. */
    public static final int C_OPCODE_EXTCOMMAND = 112;
    /** 请求 创立血盟. */
    public static final int C_OPCODE_CREATECLAN = 115;
    /** 请求 使用拒绝名单(开启指定人物讯息). */
    public static final int C_OPCODE_EXCLUDE = 117;
    /** 请求 购买指定的个人商店商品(商品清单). */
    public static final int C_OPCODE_PRIVATESHOPLIST = 120;
    /** 请求 捡取物品. */
    public static final int C_OPCODE_PICKUPITEM = 121;
    /** 请求 重置角色属性点. */
    public static final int C_OPCODE_CHARRESET = 124;
    /** 请求 下船. */
    public static final int C_OPCODE_SHIP = 125;
    /** 请求 给予物品. */
    public static final int C_OPCODE_GIVEITEM = 126;
    /** 请求 写入新的公布栏讯息. */
    public static final int C_OPCODE_BOARDWRITE = 128;
    /** 请求 点选项目的结果(Y/N). */
    public static final int C_OPCODE_ATTR = 129;
    /** 请求 邀请加入队伍/创立队伍. */
    public static final int C_OPCODE_CREATEPARTY = 130;
    /** 请求 聊天队伍对话控制(命令/chatparty). */
    public static final int C_OPCODE_CAHTPARTY = 131;

    // 未知
    /** 未使用 - 请求设置城内治安管理OK. */
    public static final int C_OPCODE_SETCASTLESECURITY = -1; // XXX
    /** 请求 配置已雇用的士兵. */
    public static final int C_OPCODE_PUTSOLDIER = -3; // XXX
    /** 未使用 - 请求更新周围物件(坐标点/洞穴点切换进出后). */
    public static final int C_OPCODE_TELEPORT2 = -4; // XXX
    /** 未使用 - 请求 配置已雇用的士兵OK. */
    public static final int C_OPCODE_PUTHIRESOLDIER = -5; // XXX
    /** 请求 选择 变更攻城时间. */
    public static final int C_OPCODE_SELECTWARTIME = -6; // XXX
    /** 请求 配置城墙上的弓箭手OK. */
    public static final int C_OPCODE_PUTBOWSOLDIER = -7; // XXX
    /** 未使用 - 请求 进入游戏(确定服务器登入讯息). */
    public static final int C_OPCODE_COMMONINFO = -9; // XXX

    // 3.52C Server Packet （服务端代码）
    /** 选取物品数量. */
    public static final int S_OPCODE_INPUTAMOUNT = 0;
    /** 交易状态(双方交易)交易是否成功. */
    public static final int S_OPCODE_TRADESTATUS = 1;
    /** 物品增加(背包). */
    public static final int S_OPCODE_ADDITEM = 2;
    /** 物品删除(背包). */
    public static final int S_OPCODE_DELETEINVENTORYITEM = 3;
    /** 宣告进入游戏. */
    public static final int S_OPCODE_LOGINTOGAME = 4;
    /** 角色移除(立即or非立即). */
    public static final int S_OPCODE_DETELECHAROK = 5;
    /** 角色盟徽(下载). */
    public static final int S_OPCODE_EMBLEM = 6;
    /** 将死亡的对象复活. */
    public static final int S_OPCODE_RESURRECTION = 7;
    /** 传送锁定(洞穴点坐标点)进入传送点. */
    public static final int S_OPCODE_TELEPORTLOCK = 8;
    /** 布告栏列表(讯息阅读). */
    public static final int S_OPCODE_BOARDREAD = 9;
    /** 封包盒子(多功能封包). */
    public static final int S_OPCODE_PACKETBOX = 10;
    /** 封包盒子(多功能封包). */
    public static final int S_OPCODE_ACTIVESPELLS = 10;
    /** 封包盒子(多功能封包). */
    public static final int S_OPCODE_SKILLICONGFX = 10;
    /** 封包盒子(多功能封包). */
    public static final int S_OPCODE_UNKNOWN2 = 10;
    /** 魔法效果:物件隐形or现形. */
    public static final int S_OPCODE_INVIS = 11;
    /** 角色列表. */
    public static final int S_OPCODE_CHARAMOUNT = 12;
    /** 聊天频道(一般or大喊). */
    public static final int S_OPCODE_NORMALCHAT = 13;
    /** 魔法效果:敏捷提升. */
    public static final int S_OPCODE_DEXUP = 14;
    /** 更新正义值. */
    public static final int S_OPCODE_LAWFUL = 15;
    /** 传送锁定(NPC瞬间移动)传送术或瞬间移动卷轴. */
    public static final int S_OPCODE_TELEPORT = 16;
    /** 服务器讯息(行数/行数,附加字串). */
    public static final int S_OPCODE_SERVERMSG = 17;
    /** 物件移动. */
    public static final int S_OPCODE_MOVEOBJECT = 18;
    /** 城堡宝库(存入资金). */
    public static final int S_OPCODE_DEPOSIT = 19;
    /** 设置围城时间. */
    public static final int S_OPCODE_WARTIME = 20;
    /** 城堡宝库(取出资金). */
    public static final int S_OPCODE_DRAWAL = 21;
    /** 魔法效果:暗盲咒术. */
    public static final int S_OPCODE_CURSEBLIND = 22;
    /** 选择一个目标. */
    public static final int S_OPCODE_SELECTTARGET = 23;
    /** 商店收购清单 NPC物品购买清单(人物卖出). */
    public static final int S_OPCODE_SHOWSHOPSELLLIST = 24;
    /** 佣兵配置清单(已有的). */
    public static final int S_OPCODE_PUTSOLDIER = 25;
    /** 画面中间红色讯息. */
    public static final int S_OPCODE_BLUEMESSAGE = 26;
    /** 设置税收. */
    public static final int S_OPCODE_TAXRATE = 28;
    /** 伺服器版本. */
    public static final int S_OPCODE_SERVERVERSION = 29;
    /** 角色皇冠. */
    public static final int S_OPCODE_CASTLEMASTER = 30;
    /** 魔法购买清单(金币). */
    public static final int S_OPCODE_SKILLBUY = 31;
    /** 更新物件外型. */
    public static final int S_OPCODE_POLY = 32;
    /** 邮件系统. */
    public static final int S_OPCODE_MAIL = 33;
    /** 移动锁定(座标异常重整)(疑似开加速器则会用这个封包将玩家锁定). */
    public static final int S_OPCODE_CHARLOCK = 34;
    /** 魔法or物品效果图示:勇敢药水类. */
    public static final int S_OPCODE_SKILLBRAVE = 36;
    /** 血盟战争讯息(编号,血盟名称,目标血盟名称). */
    public static final int S_OPCODE_WAR = 37;
    /** 损坏武器清单. */
    public static final int S_OPCODE_SELECTLIST = 38;
    /** 未使用 - 服务器登入讯息(使用string.tbl). */
    public static final int S_OPCODE_COMMONINFO = 40; // XXX
    /** 聊天频道(广播). */
    public static final int S_OPCODE_GLOBALCHAT = 40;
    /** 角色资讯(属性与能力值). */
    public static final int S_OPCODE_OWNCHARSTATUS = 41;
    /** 增加交易物品(双方交易). */
    public static final int S_OPCODE_TRADEADDITEM = 43;
    /** 立即中断连线. */
    public static final int S_OPCODE_DISCONNECT = 44;
    /** 物品状态更新(背包). */
    public static final int S_OPCODE_ITEMSTATUS = 45;
    /** 物品可用次数(背包). */
    public static final int S_OPCODE_ITEMAMOUNT = 45;
    /** 角色能力状态(力量,敏捷等). */
    public static final int S_OPCODE_OWNCHARSTATUS2 = 46;
    /** 物品名单(仓库). */
    public static final int S_OPCODE_SHOWRETRIEVELIST = 47;
    /** 魔法效果:防御类. */
    public static final int S_OPCODE_SKILLICONSHIELD = 48;
    /** 切换物件外观动作(长时间). */
    public static final int S_OPCODE_CHARVISUALUPDATE = 49;
    /** 魔法购买清单(材料). */
    public static final int S_OPCODE_SKILLBUYITEM = 50;
    /** 物件血条. */
    public static final int S_OPCODE_HPMETER = 51;
    /** 增加魔法进魔法名单. */
    public static final int S_OPCODE_ADDSKILL = 52;
    /** 产生动画 [自身]. */
    public static final int S_OPCODE_SKILLSOUNDGFX = 53;
    /** 物件属性(门). */
    public static final int S_OPCODE_ATTRIBUTE = 54;
    /** 物件攻击(伤害力变更封包类型为 writeH(0x0000)). */
    public static final int S_OPCODE_ATTACKPACKET = 56;
    /** NPC聊天频道(一般or大喊). */
    public static final int S_OPCODE_NPCSHOUT = 57;
    /** 物品名单(背包)插入批次道具. */
    public static final int S_OPCODE_INVLIST = 58;
    /** 魔法效果:水底呼吸图示. */
    public static final int S_OPCODE_BLESSOFEVA = 59;
    /** 交易封包(双方交易). */
    public static final int S_OPCODE_TRADE = 60;
    /** 戒指封包. */
    public static final int S_OPCODE_ABILITY = 61;
    /** 角色创造结果. */
    public static final int S_OPCODE_NEWCHARWRONG = 62;
    /** 角色封号. */
    public static final int S_OPCODE_CHARTITLE = 64;
    /** 更新游戏天气. */
    public static final int S_OPCODE_WEATHER = 65;
    /** 范围魔法. */
    public static final int S_OPCODE_RANGESKILLS = 66;
    /** 创造角色成功(新创). */
    public static final int S_OPCODE_NEWCHARPACK = 67;
    /** 未使用 - 学习魔法材料不足. */
    public static final int S_OPCODE_ITEMERROR = 68;
    /** 魔法效果:力量提升. */
    public static final int S_OPCODE_STRUP = 69;
    /** 选项确认视窗(Yes/No). */
    public static final int S_OPCODE_YES_NO = 70;
    /** 更新角色所在的地图. */
    public static final int S_OPCODE_MAPID = 71;
    /** 更新现在的地图 （水中）. */
    public static final int S_OPCODE_UNDERWATER = 71;
    /** 更新物件亮度. */
    public static final int S_OPCODE_LIGHT = 73;
    /** 播放音效. */
    public static final int S_OPCODE_SOUND = 74;
    /** 角色重置升级能力. */
    public static final int S_OPCODE_CHARRESET = 76;
    /** 宠物控制介面移除. */
    public static final int S_OPCODE_PETCTRL = 76;
    /** 商店贩售清单 NPC物品贩卖清单(人物买入). */
    public static final int S_OPCODE_SHOWSHOPBUYLIST = 77;
    /** 魔法效果:中毒. */
    public static final int S_OPCODE_POISON = 78;
    /** 更新物品显示名称(背包). */
    public static final int S_OPCODE_ITEMNAME = 80;
    /** 特别变身封包 - NPC改变外型. */
    public static final int S_OPCODE_SPOLY = 81;
    /** 更新物件面向. */
    public static final int S_OPCODE_CHANGEHEADING = 83;
    /** 更新角色防御属性. */
    public static final int S_OPCODE_OWNCHARATTRDEF = 84;
    /** 更新经验值. */
    public static final int S_OPCODE_EXP = 85;
    /** 更新MP显示(魔力与最大魔力). */
    public static final int S_OPCODE_MPUPDATE = 86;
    /** 角色名称变紫色. */
    public static final int S_OPCODE_PINKNAME = 87;
    /** 公告视窗. */
    public static final int S_OPCODE_COMMONNEWS = 88;
    /** 布告栏(对话视窗). */
    public static final int S_OPCODE_BOARD = 89;
    /** 角色记忆坐标名单/插入记忆座标. */
    public static final int S_OPCODE_BOOKMARKS = 90;
    /** 执行物件外观动作(短时间). */
    public static final int S_OPCODE_DOACTIONGFX = 91;
    /** 物品属性色彩状态(背包)祝福・诅咒状态变化. */
    public static final int S_OPCODE_ITEMCOLOR = 92;
    /** 更新当前游戏时间. */
    public static final int S_OPCODE_GAMETIME = 93;
    /** 初始化演算法. */
    public static final int S_OPCODE_INITPACKET = 95;
    /** 使用地图道具. */
    public static final int S_OPCODE_USEMAP = 97;
    /** 盟屋拍卖公告栏列表(血盟小屋名单). */
    public static final int S_OPCODE_HOUSELIST = 98;
    /** 更新魔攻与魔防. */
    public static final int S_OPCODE_SPMR = 100;
    /** 产生动画 [座标]. */
    public static final int S_OPCODE_EFFECTLOCATION = 102;
    /** 角色列表资讯. */
    public static final int S_OPCODE_CHARLIST = 103;
    /** 物品资讯讯息(鉴定). */
    public static final int S_OPCODE_IDENTIFYDESC = 107;
    /** 移除指定的魔法. */
    public static final int S_OPCODE_DELSKILL = 108;
    /** 交易商店清单(个人商店贩卖或购买). */
    public static final int S_OPCODE_PRIVATESHOPLIST = 109;
    /** 登入状态. */
    public static final int S_OPCODE_LOGINRESULT = 110;
    /** 物件封包. */
    public static final int S_OPCODE_CHARPACK = 112;
    /** 物件封包 (地面道具). */
    public static final int S_OPCODE_DROPITEM = 112;
    /** 聊天频道(密语). */
    public static final int S_OPCODE_WHISPERCHAT = 114;
    /** 魔法效果:精准目标. */
    public static final int S_OPCODE_TRUETARGET = 116;
    /** 魔法效果:醉酒/海浪波浪/三段加速. */
    public static final int S_OPCODE_LIQUOR = 117;
    /** 魔法效果:诅咒/麻痹类. */
    public static final int S_OPCODE_PARALYSIS = 118;
    /** 物件删除. */
    public static final int S_OPCODE_REMOVE_OBJECT = 121;
    /** 魔法or物品产生的效果:加速类. */
    public static final int S_OPCODE_SKILLHASTE = 122;
    /** 更新HP显示(体力与最大体力). */
    public static final int S_OPCODE_HPUPDATE = 123;
    /** 血盟小屋地图 [地点]. */
    public static final int S_OPCODE_HOUSEMAP = 124;
    /** 产生对话视窗(NPC). */
    public static final int S_OPCODE_SHOWHTML = 125;
    /** 雇请佣兵(佣兵购买视窗). */
    public static final int S_OPCODE_HIRESOLDIER = 126;
    /** 更新物件名称. */
    public static final int S_OPCODE_CHANGENAME = 127;

    // 未知
    /** 未使用 - 修理武器清单. */
    public static final int S_OPCODE_FIX_WEAPON_MENU = -10;
    /** 未使用 - 配置城墙上的弓箭手列表(佣兵购买视窗). */
    public static final int S_OPCODE_PUTBOWSOLDIERLIST = -11;
    /** 未使用 - 阅读邮件(旧). */
    public static final int S_OPCODE_LETTER = -12;
    /** 可配置排列佣兵数(HTML)(EX:雇用的总佣兵数:XX 可排列的佣兵数:XX ). */
    public static final int S_OPCODE_PUTHIRESOLDIER = -13;
    /** 未使用 - 更新血盟数据. */
    public static final int S_OPCODE_UPDATECLANID = -14;
    /** 未使用 - 物件新增主人. */
    public static final int S_OPCODE_NEWMASTER = -15;
    /** Ping Time. */
    public static final int S_OPCODE_PINGTIME = -16;
    /** 未使用 - 强制登出人物. */
    public static final int S_OPCODE_CHAROUT = -17;
    /** 未使用 - 画面中红色讯息(登入来源). */
    public static final int S_OPCODE_REDMESSAGE = -18;

    /** 封包代码. */
    private Opcodes() {
    }

}
