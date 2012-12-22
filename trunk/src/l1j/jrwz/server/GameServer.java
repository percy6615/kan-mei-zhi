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
package l1j.jrwz.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Logger;

import l1j.jrwz.Config;
import l1j.jrwz.server.datatables.CastleTable;
import l1j.jrwz.server.datatables.CharacterTable;
import l1j.jrwz.server.datatables.ChatLogTable;
import l1j.jrwz.server.datatables.ClanTable;
import l1j.jrwz.server.datatables.DoorSpawnTable;
import l1j.jrwz.server.datatables.DropItemTable;
import l1j.jrwz.server.datatables.DropTable;
import l1j.jrwz.server.datatables.FurnitureSpawnTable;
import l1j.jrwz.server.datatables.GetBackRestartTable;
import l1j.jrwz.server.datatables.IpTable;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.datatables.MailTable;
import l1j.jrwz.server.datatables.MapsTable;
import l1j.jrwz.server.datatables.MobGroupTable;
import l1j.jrwz.server.datatables.NPCTalkDataTable;
import l1j.jrwz.server.datatables.NpcActionTable;
import l1j.jrwz.server.datatables.NpcChatTable;
import l1j.jrwz.server.datatables.NpcSpawnTable;
import l1j.jrwz.server.datatables.NpcTable;
import l1j.jrwz.server.datatables.PetTable;
import l1j.jrwz.server.datatables.PetTypeTable;
import l1j.jrwz.server.datatables.PolyTable;
import l1j.jrwz.server.datatables.ResolventTable;
import l1j.jrwz.server.datatables.ShopTable;
import l1j.jrwz.server.datatables.SkillsTable;
import l1j.jrwz.server.datatables.SpawnTable;
import l1j.jrwz.server.datatables.SprTable;
import l1j.jrwz.server.datatables.UBSpawnTable;
import l1j.jrwz.server.datatables.WeaponSkillTable;
import l1j.jrwz.server.model.Dungeon;
import l1j.jrwz.server.model.ElementalStoneGenerator;
import l1j.jrwz.server.model.Getback;
import l1j.jrwz.server.model.L1BossCycle;
import l1j.jrwz.server.model.L1CastleLocation;
import l1j.jrwz.server.model.L1DeleteItemOnGround;
import l1j.jrwz.server.model.L1NpcRegenerationTimer;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.gametime.L1GameTimeClock;
import l1j.jrwz.server.model.item.L1TreasureBox;
import l1j.jrwz.server.model.map.L1WorldMap;
import l1j.jrwz.server.model.trap.L1WorldTraps;
import l1j.jrwz.server.utils.SystemUtil;

// Referenced classes of package l1j.jrwz.server:
// ClientThread, Logins, RateTable, IdFactory,
// LoginController, GameTimeController, Announcements,
// MobTable, SpawnTable, SkillsTable, PolyTable,
// TeleportLocations, ShopTable, NPCTalkDataTable, NpcSpawnTable,
// IpTable, Shutdown, NpcTable, MobGroupTable, NpcShoutTable

public class GameServer extends Thread {
    private class ServerShutdownThread extends Thread {
        private final int _secondsCount;

        public ServerShutdownThread(int secondsCount) {
            _secondsCount = secondsCount;
        }

        @Override
        public void run() {
            L1World world = L1World.getInstance();
            try {
                int secondsCount = _secondsCount;
                world.broadcastServerMessage("服务器即将关闭。");
                world.broadcastServerMessage("请玩家移动到安全区域先行登出。");
                while (0 < secondsCount) {
                    if (secondsCount <= 30) {
                        world.broadcastServerMessage("服务器将在" + secondsCount
                                + "秒后关闭，请玩家移动到安全区域先行登出。");
                    } else {
                        if (secondsCount % 60 == 0) {
                            world.broadcastServerMessage("服务器将在" + secondsCount
                                    / 60 + "分钟后关闭。");
                        }
                    }
                    Thread.sleep(1000);
                    secondsCount--;
                }
                shutdown();
            } catch (InterruptedException e) {
                world.broadcastServerMessage("已经取消服务器关机。服务器将会正常运作。");
                return;
            }
        }
    }

    private ServerSocket _serverSocket;
    private static Logger _log = Logger.getLogger(GameServer.class.getName());

    public static GameServer getInstance() {
        if (_instance == null) {
            _instance = new GameServer();
        }
        return _instance;
    }

    private int _port;

    // private Logins _logins;
    private LoginController _loginController;

    private int chatlvl;

    private static GameServer _instance;

    private ServerShutdownThread _shutdownThread = null;

    private GameServer() {
        super("GameServer");
    }

    public synchronized void abortShutdown() {
        if (_shutdownThread == null) {
            // 如果正在關閉
            // TODO 可能要有錯誤通知之類的
            return;
        }

        _shutdownThread.interrupt();
        _shutdownThread = null;
    }

    /**
     * 踢掉世界地圖中所有的玩家與儲存資料。
     */
    public void disconnectAllCharacters() {
        Collection<L1PcInstance> players = L1World.getInstance()
                .getAllPlayers();
        for (L1PcInstance pc : players) {
            pc.getNetConnection().setActiveChar(null);
            pc.getNetConnection().kick();
        }
        // 踢除所有在線上的玩家
        for (L1PcInstance pc : players) {
            ClientThread.quitGame(pc);
            L1World.getInstance().removeObject(pc);
        }
    }

    public void initialize() throws Exception {
        String s = Config.GAME_SERVER_HOST_NAME;
        double rateXp = Config.RATE_XP;
        double LA = Config.RATE_LA;
        double rateKarma = Config.RATE_KARMA;
        double rateDropItems = Config.RATE_DROP_ITEMS;
        double rateDropAdena = Config.RATE_DROP_ADENA;

        // TODO 考慮是否要將常顯示的訊息做 Locale 化，不然每次都要改來改去
        // L1Message.getInstance();

        chatlvl = Config.GLOBAL_CHAT_LEVEL;
        _port = Config.GAME_SERVER_PORT;
        if (!"*".equals(s)) {
            InetAddress inetaddress = InetAddress.getByName(s);
            inetaddress.getHostAddress();
            _serverSocket = new ServerSocket(_port, 50, inetaddress);
            System.out.println("服务器成功建立在 port " + _port);
        } else {
            _serverSocket = new ServerSocket(_port);
            System.out.println("服务器成功建立在 port " + _port);
        }
        System.out.println("=================================================");
        System.out.println("经验值: " + (rateXp) + " 倍");
        System.out.println("正义值: " + (LA) + " 倍");
        System.out.println("友好度: " + (rateKarma) + " 倍");
        System.out.println("掉宝率: " + (rateDropItems) + " 倍");
        System.out.println("取得金币倍率: " + (rateDropAdena) + " 倍");
        System.out.println("全体聊天最低等级: " + (chatlvl));
        if (Config.ALT_NONPVP) { // Non-PvP設定
            System.out.println("Non-PvP设置: 无效（可以PvP）");
        } else {
            System.out.println("Non-PvP设置: 有效（不可以PvP）");
        }
        int maxOnlineUsers = Config.MAX_ONLINE_USERS;
        System.out.println("连线人数上限: " + (maxOnlineUsers) + " 人");
        System.out.println("=================================================");
        IdFactory.getInstance();
        L1WorldMap.getInstance();
        _loginController = LoginController.getInstance();
        _loginController.setMaxAllowedOnlinePlayers(maxOnlineUsers);

        // 讀取所有角色名稱
        CharacterTable.getInstance().loadAllCharName();

        // 初始化角色的上線狀態
        CharacterTable.clearOnlineStatus();

        // 初始化遊戲時間
        L1GameTimeClock.init();

        // 初始化無限大戰
        UbTimeController ubTimeContoroller = UbTimeController.getInstance();
        GeneralThreadPool.getInstance().execute(ubTimeContoroller);

        // 初始化攻城
        WarTimeController warTimeController = WarTimeController.getInstance();
        GeneralThreadPool.getInstance().execute(warTimeController);

        // 設定精靈石的產生
        if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
            ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator
                    .getInstance();
            GeneralThreadPool.getInstance().execute(elementalStoneGenerator);
        }

        // 初始化 HomeTown 時間
        HomeTownTimeController.getInstance();

        // 初始化盟屋拍賣
        AuctionTimeController auctionTimeController = AuctionTimeController
                .getInstance();
        GeneralThreadPool.getInstance().execute(auctionTimeController);

        // 初始化盟屋的稅金
        HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController
                .getInstance();
        GeneralThreadPool.getInstance().execute(houseTaxTimeController);

        // 初始化釣魚
        FishingTimeController fishingTimeController = FishingTimeController
                .getInstance();
        GeneralThreadPool.getInstance().execute(fishingTimeController);

        // 初始化 NPC 聊天
        NpcChatTimeController npcChatTimeController = NpcChatTimeController
                .getInstance();
        GeneralThreadPool.getInstance().execute(npcChatTimeController);

        // 初始化 Light
        LightTimeController lightTimeController = LightTimeController
                .getInstance();
        GeneralThreadPool.getInstance().execute(lightTimeController);

        Announcements.getInstance();
        NpcTable.getInstance();
        L1DeleteItemOnGround deleteitem = new L1DeleteItemOnGround();
        deleteitem.initialize();

        if (!NpcTable.getInstance().isInitialized()) {
            throw new Exception("Could not initialize the npc table");
        }
        SpawnTable.getInstance();
        MobGroupTable.getInstance();
        SkillsTable.getInstance();
        PolyTable.getInstance();
        ItemTable.getInstance();
        DropTable.getInstance();
        DropItemTable.getInstance();
        ShopTable.getInstance();
        NPCTalkDataTable.getInstance();
        L1World.getInstance();
        L1WorldTraps.getInstance();
        Dungeon.getInstance();
        NpcSpawnTable.getInstance();
        IpTable.getInstance();
        MapsTable.getInstance();
        UBSpawnTable.getInstance();
        PetTable.getInstance();
        ClanTable.getInstance();
        CastleTable.getInstance();
        L1CastleLocation.setCastleTaxRate(); // 必須在 CastleTable 初始化之後
        GetBackRestartTable.getInstance();
        DoorSpawnTable.getInstance();
        GeneralThreadPool.getInstance();
        L1NpcRegenerationTimer.getInstance();
        ChatLogTable.getInstance();
        WeaponSkillTable.getInstance();
        NpcActionTable.load();
        GMCommandsConfig.load();
        Getback.loadGetBack();
        PetTypeTable.load();
        L1BossCycle.load();
        L1TreasureBox.load();
        SprTable.getInstance();
        ResolventTable.getInstance();
        FurnitureSpawnTable.getInstance();
        NpcChatTable.getInstance();
        MailTable.getInstance();

        System.out.println("初始化完毕");
        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        this.start();
    }

    @Override
    public void run() {
        System.out.println("使用了 " + SystemUtil.getUsedMemoryMB() + " MB 的内存");
        // 這是一個 Locale 的範例
        // System.out.println(L1Message._memoryUse + SystemUtil.getUsedMemoryMB() + "MB");
        System.out.println("等待客户端连接中...");
        while (true) {
            try {
                Socket socket = _serverSocket.accept();
                System.out.println("从 " + socket.getInetAddress() + " 试图连线");
                String host = socket.getInetAddress().getHostAddress();
                if (IpTable.getInstance().isBannedIp(host)) {
                    _log.info("banned IP(" + host + ")");
                } else {
                    ClientThread client = new ClientThread(socket);
                    GeneralThreadPool.getInstance().execute(client);
                }
            } catch (IOException ioexception) {
            }
        }
    }

    public void shutdown() {
        disconnectAllCharacters();
        System.exit(0);
    }

    public synchronized void shutdownWithCountdown(int secondsCount) {
        if (_shutdownThread != null) {
            // 如果正在關閉
            // TODO 可能要有錯誤通知之類的
            return;
        }
        _shutdownThread = new ServerShutdownThread(secondsCount);
        GeneralThreadPool.getInstance().execute(_shutdownThread);
    }
}
