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
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.datatables.CharBuffTable;
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
                    _log.warning("一定时间内沒有收到封包回应，所以强制切断 (" + _hostname + ") 的连线。");
                    cancel();
                    return;
                }
            } catch (Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                cancel();
            }
        }

        public void start() {
            _observerTimer.scheduleAtFixedRate(ClientThreadObserver.this, 0, _disconnectTimeMillis);
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
    // private static final byte[] FIRST_PACKET = { // 3.0
    // (byte) 0xec, (byte) 0x64, (byte) 0x3e, (byte) 0x0d, (byte) 0xc0, (byte)
    // 0x82, (byte) 0x00, (byte) 0x00,
    // (byte) 0x02, (byte) 0x08, (byte) 0x00 };
    private static final byte[] FIRST_PACKET = {
            // 3.52 TW 12042501 Lin.bin
            (byte) 0xf9, (byte) 0xb0, (byte) 0x2a, (byte) 0x73, (byte) 0x01, (byte) 0x80, (byte) 0xff, (byte) 0xce,
            (byte) 0xc6, (byte) 0xc1, (byte) 0xfa };

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
            L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
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
                for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
                    visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
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
            follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(),
                    follower.getHeading(), follower.getMapId());
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

    private long _lastSavedTime = System.currentTimeMillis();

    private long _lastSavedTime_inventory = System.currentTimeMillis();

    private Cipher _cipher;

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
            if (Config.AUTOSAVE_INTERVAL * 1000 < System.currentTimeMillis() - _lastSavedTime) {
                _activeChar.save();
                _lastSavedTime = System.currentTimeMillis();
            }

            // 自動儲存身上道具資料
            if (Config.AUTOSAVE_INTERVAL_INVENTORY * 1000 < System.currentTimeMillis() - _lastSavedTime_inventory) {
                _activeChar.saveInventory();
                _lastSavedTime_inventory = System.currentTimeMillis();
            }
        } catch (Exception e) {
            _log.warning("Client autosave failure.");
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw e;
        }
    }

    /**
     * 填充16进制.
     * 
     * @param data
     *            - 封包资料
     * @param digits
     *            - 位数
     * 
     * @return 16进制数据
     */
    private String fillHex(final int data, final int digits) {
        String number = Integer.toHexString(data);

        for (int i = number.length(); i < digits; i++) {
            number = "0" + number;
        }
        return number;
    }

    /** 取得账号 */
    public Account getAccount() {
        return this._account;
    }

    /** 取得帐号名称 */
    public String getAccountName() {
        if (this._account == null) {
            return null;
        }
        return this._account.getName();
    }

    /** 取得在线角色 */
    public L1PcInstance getActiveChar() {
        return this._activeChar;
    }

    /** 取得主机名 */
    public String getHostname() {
        return this._hostname;
    }

    /** 取得 IP */
    public String getIp() {
        return this._ip;
    }

    public void kick() {
        sendPacket(new S_Disconnect());
        _kick = 1;
        StreamUtil.close(_out, _in);
    }

    /**
     * 打印封包资料(向控制台输出封包).
     * 
     * @param data
     *            - 封包资料
     * @param len
     *            - 封包长度
     * @return 结果
     */
    private String printData(final byte[] data, final int len) {
        final StringBuffer result = new StringBuffer();
        int counter = 0;
        for (int i = 0; i < len; i++) {
            if (counter % 16 == 0) {
                result.append(this.fillHex(i, 4) + ": ");
            }
            result.append(this.fillHex(data[i] & 255, 2) + " ");
            counter++;
            if (counter == 16) {
                result.append("   ");
                int charpoint = i - 15;
                for (int a = 0; a < 16; a++) {
                    final int t1 = data[charpoint++];
                    if ((t1 > 31) && (t1 < 128)) {
                        result.append((char) t1);
                    } else {
                        result.append('.');
                    }
                }
                result.append("\n");
                counter = 0;
            }
        }

        final int rest = data.length % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; i++) {
                result.append("   ");
            }

            int charpoint = data.length - rest;
            for (int a = 0; a < rest; a++) {
                final int t1 = data[charpoint++];
                if ((t1 > 31) && (t1 < 128)) {
                    result.append((char) t1);
                } else {
                    result.append('.');
                }
            }

            result.append("\n");
        }
        return result.toString();
    }

    /**
     * 启用数据包输出到控制台窗口.
     * 
     * @param type
     *            - 类型(true:服务端、false:客户端)
     * @param op
     *            - 封包
     * @param data
     *            - 封包资料
     */
    public final void printPacket(final boolean type, final int op, final byte... data) {
        if (type) {
            System.out.println("[server] " + "OP: " + op + " Size: " + data.length);
        } else {
            System.out.println("[client] " + "OP: " + op + " Size: " + data.length);
        }
        System.out.println(this.printData(data, data.length));
    }

    /** 读取数据包 */
    private byte[] readPacket() throws Exception {
        try {
            final int hiByte = this._in.read();
            final int loByte = this._in.read();
            if ((loByte < 0) || (hiByte < 0)) {
                throw new RuntimeException();
            }
            final int dataLength = ((loByte << 8) + hiByte) - 2; // * 256
            if ((dataLength <= 0) || (dataLength > 65533)) {
                throw new RuntimeException();
            }
            final byte[] data = new byte[dataLength];

            int readSize = 0;

            for (int i = 0; (i != -1) && (readSize < dataLength); readSize += i) {
                i = this._in.read(data, readSize, dataLength - readSize);
            }

            if (readSize != dataLength) {
                _log.warning("不完整的数据包发送到服务器，关闭连接。");
                throw new RuntimeException();
            }

            return this._cipher.decrypt(data);
        } catch (final Exception e) {
            throw e;
        }
    }

    @Override
    public void run() {

        /*
         * TODO: 翻译 クライアントからのパケットをある程度制限する。 理由：不正の误检出が多発する恐れがあるため
         * ex1.サーバに过负荷が挂かっている场合、负荷が落ちたときにクライアントパケットを一气に处理し、结果的に不正扱いとなる。
         * ex2.サーバ侧のネットワーク（下り）にラグがある场合、クライアントパケットが一气に流れ迂み、结果的に不正扱いとなる。
         * ex3.クライアント侧のネットワーク（上り）にラグがある场合、以下同样。 无制限にする前に不正检出方法を见直す必要がある。
         */
        final HcPacket movePacket = new HcPacket(M_CAPACITY);
        final HcPacket hcPacket = new HcPacket(H_CAPACITY);
        GeneralThreadPool.getInstance().execute(movePacket);
        GeneralThreadPool.getInstance().execute(hcPacket);

        String keyHax = "";
        int key = 0;
        byte bogus = 0;
        try {
            /** 采取乱数取seed */
            final Random random = new Random();
            final int keys = 0x58bfa78e; // Lin.ver 12011702 351C_TW
            keyHax = Integer.toHexString((random.nextInt(keys)) + 1);
            key = Integer.parseInt(keyHax, 16);
            bogus = (byte) (FIRST_PACKET.length + 7);

            this._out.write(bogus & 0xFF);
            this._out.write(bogus >> 8 & 0xFF);
            this._out.write(Opcodes.S_OPCODE_INITPACKET); // 3.51C Taiwan Server
            this._out.write((byte) (key & 0xFF));
            this._out.write((byte) (key >> 8 & 0xFF));
            this._out.write((byte) (key >> 16 & 0xFF));
            this._out.write((byte) (key >> 24 & 0xFF));

            this._out.write(FIRST_PACKET);
            this._out.flush();
        } catch (final Throwable e) {
            try {
                _log.info("异常用户端(" + this._hostname + ") 连结到伺服器, 已中断该连线。");
                StreamUtil.close(this._out, this._in);
                if (this._csocket != null) {
                    this._csocket.close();
                    this._csocket = null;
                }
                return;
            } catch (final Throwable ex) {
                return;
            }
        } finally {
        }
        try {
            _log.info("(" + this._hostname + ") 连结到伺服器。");
            SystemUtil.getUsedMemory(); // 内存使用率
            System.out.println("等待客户端连接...");
            final ClientThreadObserver observer = new ClientThreadObserver(Config.AUTOMATIC_KICK * 60 * 1000); // 自动断线的时间（单位:毫秒）

            // 是否启用自动踢人
            if (Config.AUTOMATIC_KICK > 0) {
                observer.start();
            }
            this._cipher = new Cipher(key);

            while (true) {
                this.doAutoSave();

                byte[] data = null;
                try {
                    data = this.readPacket();
                } catch (final Exception e) {
                    break;
                }
                // _log.finest("[C]\n" + new
                // ByteArrayUtil(data).dumpToString());

                final int opcode = data[0] & 0xFF;

                if (Config.PACKET) {
                    this.printPacket(false, opcode, data);
                }

                // 处理多重登入
                if ((opcode == Opcodes.C_OPCODE_COMMONCLICK) || (opcode == Opcodes.C_OPCODE_CHANGECHAR)) {
                    this._loginStatus = 1;
                }
                if (opcode == Opcodes.C_OPCODE_LOGINTOSERVER) {
                    if (this._loginStatus != 1) {
                        continue;
                    }
                }
                if ((opcode == Opcodes.C_OPCODE_LOGINTOSERVEROK) || (opcode == Opcodes.C_OPCODE_RETURNTOLOGIN)) {
                    this._loginStatus = 0;
                }

                if (opcode != Opcodes.C_OPCODE_KEEPALIVE) {
                    // C_OPCODE_KEEPALIVE以外の何かしらのパケットを受け取ったらObserverへ通知
                    observer.packetReceived();
                }
                // TODO: 翻译
                // 如果目前角色为 null はキャラクター选択前なのでOpcodeの取舍选択はせず全て实行
                if (this._activeChar == null) {
                    this._handler.handlePacket(data, this._activeChar);
                    continue;
                }

                // TODO: 翻译
                // 以降、PacketHandlerの处理状况がClientThreadに影响を与えないようにする为の处理
                // 目的はOpcodeの取舍选択とClientThreadとPacketHandlerの切り离し

                // 要处理的 OPCODE
                // 切换角色、丢道具到地上、删除身上道具
                if ((opcode == Opcodes.C_OPCODE_CHANGECHAR) || (opcode == Opcodes.C_OPCODE_DROPITEM)
                        || (opcode == Opcodes.C_OPCODE_DELETEINVENTORYITEM)) {
                    this._handler.handlePacket(data, this._activeChar);
                } else if (opcode == Opcodes.C_OPCODE_MOVECHAR) {
                    // 为了确保即时的移动，将移动的封包独立出来处理
                    movePacket.requestWork(data);
                } else {
                    // 处理其他数据的传递
                    hcPacket.requestWork(data);
                }
            }
        } catch (final Throwable e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            try {
                if (this._activeChar != null) {
                    quitGame(this._activeChar);
                    synchronized (this._activeChar) {
                        // 从线上中登出角色
                        this._activeChar.logout();
                        this.setActiveChar(null);
                    }
                }
                // 玩家离线时, online=0
                if (this.getAccount() != null) {
//                    Account.online(this.getAccount(), false);
                }

                // 送出断线的封包
                // this.sendPacket(new S_Disconnect());

                StreamUtil.close(this._out, this._in);
                if (this._csocket != null) {
                    this._csocket.close();
                    this._csocket = null;
                }
            } catch (final Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            } finally {
                LoginController.getInstance().logout(this);
            }
        }
        this._csocket = null;
        _log.fine("Server thread[C] stopped");
        if (this._kick < 1) {
            _log.info("(" + this.getAccountName() + ":" + this._hostname + ")连线终止。");
            // System.out.println("使用了 " + SystemUtil.getUsedMemoryMB()
            // + " MB 的记忆体");
            SystemUtil.getUsedMemory();
            System.out.println("等待客户端连接...");
            if (this.getAccount() != null) {
//                Account.online(this.getAccount(), false);
            }
        }
        return;
    }

    @Override
    /** 发送数据包 */
    public void sendPacket(final ServerBasePacket packet) {
        synchronized (this) {
            try {
                final byte[] content = packet.getContent();
                final byte[] data = Arrays.copyOf(content, content.length);
                if (data.length <= 0) {
                    return;
                }
                if (Config.PACKET) {
                    final int opcode = data[0] & 0xFF;
                    this.printPacket(true, opcode, data);
                }
                this._cipher.encrypt(data);
                final int length = data.length + 2;

                this._out.write(length & 0xff);
                this._out.write(length >> 8 & 0xff);
                this._out.write(data);
                this._out.flush();
            } catch (final Exception e) {
                if (Config.PACKETS) {
                    // this._out.close(); // 关闭此输出流并释放与此流有关的所有系统资源。
                    StreamUtil.close(this._out, this._in);
                    // 此处抛出的异常貌似不会造成影响
                    _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /** 设置账号 */
    public void setAccount(final Account account) {
        this._account = account;
    }

    /** 设置在线角色 */
    public void setActiveChar(final L1PcInstance pc) {
        this._activeChar = pc;
    }
}
