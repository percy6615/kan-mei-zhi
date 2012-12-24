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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.datatables.CharBuffTable;
import l1j.jrwz.server.encryptions.ClientIdExistsException;
import l1j.jrwz.server.encryptions.LineageEncryption;
import l1j.jrwz.server.encryptions.LineageKeys;
import l1j.jrwz.server.model.Getback;
import l1j.jrwz.server.model.L1Trade;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1DollInstance;
import l1j.jrwz.server.model.Instance.L1FollowerInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.Instance.L1SummonInstance;
import l1j.jrwz.server.serverpackets.S_Disconnect;
import l1j.jrwz.server.serverpackets.S_PacketBox;
import l1j.jrwz.server.serverpackets.S_SummonPack;
import l1j.jrwz.server.serverpackets.ServerBasePacket;
import l1j.jrwz.server.types.UByte8;
import l1j.jrwz.server.types.UChar8;
import l1j.jrwz.server.utils.StreamUtil;
import l1j.jrwz.server.utils.SystemUtil;

// Referenced classes of package l1j.jrwz.server:
// PacketHandler, Logins, IpTable, LoginController,
// ClanTable, IdFactory
//
public class ClientThread implements Runnable, PacketOutput {

    // 定時監控客戶端
    class ClientThreadObserver extends TimerTask {
        private int _checkct = 1;

        private final int _disconnectTimeMillis;

        public ClientThreadObserver(int disconnectTimeMillis) {
            _disconnectTimeMillis = disconnectTimeMillis;
        }

        public void packetReceived() {
            _checkct++;
        }

        @Override
        public void run() {
            try {
                if (_csocket == null) {
                    cancel();
                    return;
                }

                if (_checkct > 0) {
                    _checkct = 0;
                    return;
                }

                if (_activeChar == null // 選角色之前
                        || _activeChar != null && !_activeChar.isPrivateShop()) { // 正在個人商店
                    kick();
                    _log.warning("一定时间内沒有收到封包回应，所以强制切断 (" + _hostname
                            + ") 的连线。");
                    cancel();
                    return;
                }
            } catch (Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                cancel();
            }
        }

        public void start() {
            _observerTimer.scheduleAtFixedRate(ClientThreadObserver.this, 0,
                    _disconnectTimeMillis);
        }
    }

    // 帳號處理的程序
    class HcPacket implements Runnable {
        private final Queue<byte[]> _queue;

        private final PacketHandler _handler;

        public HcPacket() {
            _queue = new ConcurrentLinkedQueue<byte[]>();
            _handler = new PacketHandler(ClientThread.this);
        }

        public HcPacket(int capacity) {
            _queue = new LinkedBlockingQueue<byte[]>(capacity);
            _handler = new PacketHandler(ClientThread.this);
        }

        public void requestWork(byte data[]) {
            _queue.offer(data);
        }

        @Override
        public void run() {
            byte[] data;
            while (_csocket != null) {
                data = _queue.poll();
                if (data != null) {
                    try {
                        _handler.handlePacket(data, _activeChar);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                }
            }
            return;
        }
    }

    private static Logger _log = Logger.getLogger(ClientThread.class.getName());

    private InputStream _in;

    private OutputStream _out;

    private PacketHandler _handler;

    private Account _account;

    private L1PcInstance _activeChar;

    private String _ip;

    private String _hostname;

    private Socket _csocket;

    private int _loginStatus = 0;

    // private static final byte[] FIRST_PACKET = { 10, 0, 38, 58, -37, 112, 46,
    // 90, 120, 0 }; // for Episode5
    // private static final byte[] FIRST_PACKET =
    // { (byte) 0x12, (byte) 0x00, (byte) 0x14, (byte) 0x1D,
    // (byte) 0x82,
    // (byte) 0xF1,
    // (byte) 0x0C, // = 0x0cf1821d
    // (byte) 0x87, (byte) 0x7D, (byte) 0x75, (byte) 0x7D,
    // (byte) 0xA1, (byte) 0x3B, (byte) 0x62, (byte) 0x2C,
    // (byte) 0x5E, (byte) 0x3E, (byte) 0x9F }; // for Episode 6
    // private static final byte[] FIRST_PACKET = { 2.70C
    // (byte) 0xb1, (byte) 0x3c, (byte) 0x2c, (byte) 0x28,
    // (byte) 0xf6, (byte) 0x65, (byte) 0x1d, (byte) 0xdd,
    // (byte) 0x56, (byte) 0xe3, (byte) 0xef };
    private static final byte[] FIRST_PACKET = { // 3.0
    (byte) 0xec, (byte) 0x64, (byte) 0x3e, (byte) 0x0d, (byte) 0xc0,
            (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x08,
            (byte) 0x00 };

    public static void quitGame(L1PcInstance pc) {
        // 如果死掉回到城中，設定飽食度
        if (pc.isDead()) {
            int[] loc = Getback.GetBack_Location(pc, true);
            pc.setX(loc[0]);
            pc.setY(loc[1]);
            pc.setMap((short) loc[2]);
            pc.setCurrentHp(pc.getLevel());
            pc.set_food(40);
        }

        // 終止交易
        if (pc.getTradeID() != 0) { // トレード中
            L1Trade trade = new L1Trade();
            trade.TradeCancel(pc);
        }

        // 終止決鬥
        if (pc.getFightId() != 0) {
            pc.setFightId(0);
            L1PcInstance fightPc = (L1PcInstance) L1World.getInstance()
                    .findObject(pc.getFightId());
            if (fightPc != null) {
                fightPc.setFightId(0);
                fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
            }
        }

        // 離開組隊
        if (pc.isInParty()) { // 如果有組隊
            pc.getParty().leaveMember(pc);
        }

        // TODO: 離開聊天組隊(?)
        if (pc.isInChatParty()) { // 如果在聊天組隊中(?)
            pc.getChatParty().leaveMember(pc);
        }

        // 移除世界地圖上的寵物
        // 變更召喚怪物的名稱
        Object[] petList = pc.getPetList().values().toArray();
        for (Object petObject : petList) {
            if (petObject instanceof L1PetInstance) {
                L1PetInstance pet = (L1PetInstance) petObject;
                pet.dropItem();
                pc.getPetList().remove(pet.getId());
                pet.deleteMe();
            }
            if (petObject instanceof L1SummonInstance) {
                L1SummonInstance summon = (L1SummonInstance) petObject;
                for (L1PcInstance visiblePc : L1World.getInstance()
                        .getVisiblePlayer(summon)) {
                    visiblePc.sendPackets(new S_SummonPack(summon, visiblePc,
                            false));
                }
            }
        }

        // 移除世界地圖上的魔法娃娃
        Object[] dollList = pc.getDollList().values().toArray();
        for (Object dollObject : dollList) {
            L1DollInstance doll = (L1DollInstance) dollObject;
            doll.deleteDoll();
        }

        // 重新建立跟隨者
        Object[] followerList = pc.getFollowerList().values().toArray();
        for (Object followerObject : followerList) {
            L1FollowerInstance follower = (L1FollowerInstance) followerObject;
            follower.setParalyzed(true);
            follower.spawn(follower.getNpcTemplate().get_npcId(),
                    follower.getX(), follower.getY(), follower.getHeading(),
                    follower.getMapId());
            follower.deleteMe();
        }

        // 儲存魔法狀態
        CharBuffTable.DeleteBuff(pc);
        CharBuffTable.SaveBuff(pc);
        pc.clearSkillEffectTimer();

        // 停止玩家的偵測
        pc.stopEtcMonitor();
        // 設定線上狀態為下線
        pc.setOnlineStatus(0);
        try {
            pc.save();
            pc.saveInventory();
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    // TODO: 翻譯
    // ClientThreadによる一定間隔自動セーブを制限する為のフラグ（true:制限 false:制限無し）
    // 現在はC_LoginToServerが実行された際にfalseとなり、
    // C_NewCharSelectが実行された際にtrueとなる
    private boolean _charRestart = true;

    private LineageKeys _clkey;

    private long _lastSavedTime = System.currentTimeMillis();

    private long _lastSavedTime_inventory = System.currentTimeMillis();

    private int _kick = 0;

    private static final int M_CAPACITY = 3; // 一邊移動的最大封包量

    private static final int H_CAPACITY = 2;// 一方接受的最高限額所需的行動

    private static Timer _observerTimer = new Timer();

    /**
     * for Test
     */
    protected ClientThread() {
    }

    public ClientThread(Socket socket) throws IOException {
        _csocket = socket;
        _ip = socket.getInetAddress().getHostAddress();
        if (Config.HOSTNAME_LOOKUPS) {
            _hostname = socket.getInetAddress().getHostName();
        } else {
            _hostname = _ip;
        }
        _in = socket.getInputStream();
        _out = new BufferedOutputStream(socket.getOutputStream());

        // PacketHandler 初始化
        _handler = new PacketHandler(this);
    }

    public void CharReStart(boolean flag) {
        _charRestart = flag;
    }

    public void close() throws IOException {
        _csocket.close();
    }

    private void doAutoSave() throws Exception {
        if (_activeChar == null || _charRestart) {
            return;
        }
        try {
            // 自動儲存角色資料
            if (Config.AUTOSAVE_INTERVAL * 1000 < System.currentTimeMillis()
                    - _lastSavedTime) {
                _activeChar.save();
                _lastSavedTime = System.currentTimeMillis();
            }

            // 自動儲存身上道具資料
            if (Config.AUTOSAVE_INTERVAL_INVENTORY * 1000 < System
                    .currentTimeMillis() - _lastSavedTime_inventory) {
                _activeChar.saveInventory();
                _lastSavedTime_inventory = System.currentTimeMillis();
            }
        } catch (Exception e) {
            _log.warning("Client autosave failure.");
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw e;
        }
    }

    public Account getAccount() {
        return _account;
    }

    public String getAccountName() {
        if (_account == null) {
            return null;
        }
        return _account.getName();
    }

    public L1PcInstance getActiveChar() {
        return _activeChar;
    }

    public String getHostname() {
        return _hostname;
    }

    public String getIp() {
        return _ip;
    }

    public void kick() {
        sendPacket(new S_Disconnect());
        _kick = 1;
        StreamUtil.close(_out, _in);
    }

    private byte[] readPacket() throws Exception {
        try {
            int hiByte = _in.read();
            int loByte = _in.read();
            if (loByte < 0) {
                throw new RuntimeException();
            }
            int dataLength = (loByte * 256 + hiByte) - 2;

            byte data[] = new byte[dataLength];

            int readSize = 0;

            for (int i = 0; i != -1 && readSize < dataLength; readSize += i) {
                i = _in.read(data, readSize, dataLength - readSize);
            }

            if (readSize != dataLength) {
                _log.warning("Incomplete Packet is sent to the server, closing connection.");
                throw new RuntimeException();
            }

            return LineageEncryption.decrypt(data, dataLength, _clkey);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void run() {
        _log.info("(" + _hostname + ") 连接到服务器。");
        System.out.println("使用了 " + SystemUtil.getUsedMemoryMB() + " MB 的内存");
        System.out.println("等待客户端连接...");

        Socket socket = _csocket;
        /*
         * TODO: 翻譯
         * クライアントからのパケットをある程度制限する。 理由：不正の誤検出が多発する恐れがあるため
         * ex1.サーバに過負荷が掛かっている場合、負荷が落ちたときにクライアントパケットを一気に処理し、結果的に不正扱いとなる。
         * ex2.サーバ側のネットワーク（下り）にラグがある場合、クライアントパケットが一気に流れ込み、結果的に不正扱いとなる。
         * ex3.クライアント側のネットワーク（上り）にラグがある場合、以下同様。
         * 
         * 無制限にする前に不正検出方法を見直す必要がある。
         */
        HcPacket movePacket = new HcPacket(M_CAPACITY);
        HcPacket hcPacket = new HcPacket(H_CAPACITY);
        GeneralThreadPool.getInstance().execute(movePacket);
        GeneralThreadPool.getInstance().execute(hcPacket);

        ClientThreadObserver observer = new ClientThreadObserver(
                Config.AUTOMATIC_KICK * 60 * 1000); // 自動斷線的時間（單位:毫秒）

        // 是否啟用自動踢人
        if (Config.AUTOMATIC_KICK > 0) {
            observer.start();
        }

        try {
            // long seed = 0x5cc690ecL; // 2.70C
            long seed = 0x7c98bdfa; // 3.0
            byte Bogus = (byte) (FIRST_PACKET.length + 7);
            _out.write(Bogus & 0xFF);
            _out.write(Bogus >> 8 & 0xFF);
            // _out.write(0x20); // 2.70C
            _out.write(0x7d); // 3.0
            _out.write((byte) (seed & 0xFF));
            _out.write((byte) (seed >> 8 & 0xFF));
            _out.write((byte) (seed >> 16 & 0xFF));
            _out.write((byte) (seed >> 24 & 0xFF));

            _out.write(FIRST_PACKET);
            _out.flush();
            try {
                // long seed = 0x2e70db3aL; // for Episode5
                // long seed = 0x0cf1821dL; // for Episode6
                _clkey = LineageEncryption.initKeys(socket, seed);
            } catch (ClientIdExistsException e) {
            }

            while (true) {
                doAutoSave();

                byte data[] = null;
                try {
                    data = readPacket();
                } catch (Exception e) {
                    break;
                }
                // _log.finest("[C]\n" + new
                // ByteArrayUtil(data).dumpToString());

                int opcode = data[0] & 0xFF;

                // 處理多重登入
                if (opcode == Opcodes.C_OPCODE_COMMONCLICK
                        || opcode == Opcodes.C_OPCODE_CHANGECHAR) {
                    _loginStatus = 1;
                }
                if (opcode == Opcodes.C_OPCODE_LOGINTOSERVER) {
                    if (_loginStatus != 1) {
                        continue;
                    }
                }
                if (opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK
                        || opcode == Opcodes.C_OPCODE_RETURNTOLOGIN) {
                    _loginStatus = 0;
                }

                if (opcode != Opcodes.C_OPCODE_KEEPALIVE) {
                    // C_OPCODE_KEEPALIVE以外の何かしらのパケットを受け取ったらObserverへ通知
                    observer.packetReceived();
                }
                // TODO: 翻譯
                // 如果目前角色為 null はキャラクター選択前なのでOpcodeの取捨選択はせず全て実行
                if (_activeChar == null) {
                    _handler.handlePacket(data, _activeChar);
                    continue;
                }

                // TODO: 翻譯
                // 以降、PacketHandlerの処理状況がClientThreadに影響を与えないようにする為の処理
                // 目的はOpcodeの取捨選択とClientThreadとPacketHandlerの切り離し

                // 要處理的 OPCODE
                // 切換角色、丟道具到地上、刪除身上道具
                if (opcode == Opcodes.C_OPCODE_CHANGECHAR
                        || opcode == Opcodes.C_OPCODE_DROPITEM
                        || opcode == Opcodes.C_OPCODE_DELETEINVENTORYITEM) {
                    _handler.handlePacket(data, _activeChar);
                } else if (opcode == Opcodes.C_OPCODE_MOVECHAR) {
                    // 為了確保即時的移動，將移動的封包獨立出來處理
                    movePacket.requestWork(data);
                } else {
                    // 處理其他數據的傳遞
                    hcPacket.requestWork(data);
                }
            }
        } catch (Throwable e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            try {
                if (_activeChar != null) {
                    quitGame(_activeChar);

                    synchronized (_activeChar) {
                        // 從線上中登出角色
                        _activeChar.logout();
                        setActiveChar(null);
                    }
                }

                // 送出斷線的封包
                sendPacket(new S_Disconnect());

                StreamUtil.close(_out, _in);
            } catch (Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            } finally {
                LoginController.getInstance().logout(this);
            }
        }
        _csocket = null;
        _log.fine("Server thread[C] stopped");
        if (_kick < 1) {
            _log.info("(" + getAccountName() + ":" + _hostname + ") 连线终止。");
            System.out.println("使用了 " + SystemUtil.getUsedMemoryMB()
                    + " MB 的内存");
            System.out.println("等待客户端连接...");
        }
        return;
    }

    @Override
    public void sendPacket(ServerBasePacket packet) {
        synchronized (this) {
            try {
                byte abyte0[] = packet.getContent();
                char ac[] = new char[abyte0.length];
                ac = UChar8.fromArray(abyte0);
                ac = LineageEncryption.encrypt(ac, _clkey);
                abyte0 = UByte8.fromArray(ac);
                int j = abyte0.length + 2;

                _out.write(j & 0xff);
                _out.write(j >> 8 & 0xff);
                _out.write(abyte0);
                _out.flush();
            } catch (Exception e) {
            }
        }
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public void setActiveChar(L1PcInstance pc) {
        _activeChar = pc;
    }
}
