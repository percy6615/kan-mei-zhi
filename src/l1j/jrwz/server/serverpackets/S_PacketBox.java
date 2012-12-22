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
package l1j.jrwz.server.serverpackets;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.logging.Logger;

import l1j.jrwz.Config;
import l1j.jrwz.server.Account;
import l1j.jrwz.server.Opcodes;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;

/**
 * 封包盒子:部分技能图标(右上角Icon)以及特效、对话窗口、系统提示信息等等.
 */
public class S_PacketBox extends ServerBasePacket {
    private static final String S_PACKETBOX = "[S] S_PacketBox";

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_PacketBox.class.getName());

    private byte[] _byte = null;

    // *** S_107 sub code list ***

    // 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:城名9 ...
    /** C(id) H(?): %s 的攻城战开始。 */
    public static final int MSG_WAR_BEGIN = 0;

    /** C(id) H(?): %s 的攻城战结束。 */
    public static final int MSG_WAR_END = 1;

    /** C(id) H(?): %s 的攻城战正在进行中。 */
    public static final int MSG_WAR_GOING = 2;

    /** -: 已经掌握了城堡的主导权。 (音乐变化) */
    public static final int MSG_WAR_INITIATIVE = 3;

    /** -: 已占领城堡。 */
    public static final int MSG_WAR_OCCUPY = 4;

    /** ?: 结束决斗。 (音乐变化) */
    public static final int MSG_DUEL = 5;

    /** C(count): SMS您没有发送出任何简讯。/ 您共发出 %d 通天堂简讯。 */
    public static final int MSG_SMS_SENT = 6;

    /** -: 俩人的结婚在所有人的祝福下完成。 (音乐变化) */
    public static final int MSG_MARRIED = 9;

    /** C(weight): 重量(30段阶) */
    public static final int WEIGHT = 10;

    /** C(food): 饱食度(30段阶) */
    public static final int FOOD = 11;

    /** C(0) C(level): 等级%d以下才能使用此道具。 (0~49以外不显示) */
    public static final int MSG_LEVEL_OVER = 12;

    /** UB情报HTML */
    public static final int HTML_UB = 14;

    /**
     * C(id)<br>
     * 1:感觉到在身上有的精灵力量被空气中融化。<br>
     * 2:忽然全身充满了%s的灵力。火。<br>
     * 3:忽然全身充满了%s的灵力。水。<br>
     * 4:忽然全身充满了%s的灵力。风。<br>
     * 5:忽然全身充满了%s的灵力。地。<br>
     */
    public static final int MSG_ELF = 15;

    /** C(count) S(name)...: 开启拒绝名单 */
    public static final int ADD_EXCLUDE2 = 17;

    /** S(name): 增加到拒绝名单 */
    public static final int ADD_EXCLUDE = 18;

    /** S(name): 移除出拒绝名单 */
    public static final int REM_EXCLUDE = 19;

    /** 技能图标 */
    public static final int ICONS1 = 20;

    /** 技能图标 */
    public static final int ICONS2 = 21;

    /** 光系技能图标 */
    public static final int ICON_AURA = 22;

    /** S(name): 新村长由%s选出。 */
    public static final int MSG_TOWN_LEADER = 23;

    /**
     * C(id): 你的阶级变更为%s。<br>
     * id - 1:见习 2:一般 3:守护骑士
     */
    public static final int MSG_RANK_CHANGED = 27;

    /** D(?) S(name) S(clanname): %s 血盟的 %s打败了反王 %s 血盟成为新主人。 */
    public static final int MSG_WIN_LASTAVARD = 30;

    /** -: \f1你觉得舒服多了。 */
    public static final int MSG_FEEL_GOOD = 31;

    /** 不明。C_30客户端会传回一个封包 */
    public static final int SOMETHING1 = 33;

    /** H(time): 状态图示:蓝水。 */
    public static final int ICON_BLUEPOTION = 34;

    /** H(time): 状态图示:变身。 */
    public static final int ICON_POLYMORPH = 35;

    /** H(time): 状态图示:禁言。 */
    public static final int ICON_CHATBAN = 36;

    /** 不明。C_7パケットが飛ぶ。C_7はペットのメニューを開いたときにも飛ぶ。 */
    public static final int SOMETHING2 = 37;

    /** 血盟成员清单HTML */
    public static final int HTML_CLAN1 = 38;

    /** H(time): 状态图示:圣结界 */
    public static final int ICON_I2H = 40;

    /** 更新角色使用的快捷键 */
    public static final int CHARACTER_CONFIG = 41;

    /** 角色选择画面 */
    public static final int LOGOUT = 42;

    /** 战斗中无法重新开始。 */
    public static final int MSG_CANT_LOGOUT = 43;

    /**
     * C(count) D(time) S(name) S(info):<br>
     * [CALL] ボタンのついたウィンドウが表示される。これはBOTなどの不正者チェックに
     * 使われる機能らしい。名前をダブルクリックするとC_RequestWhoが飛び、クライアントの
     * フォルダにbot_list.txtが生成される。名前を選択して+キーを押すと新しいウィンドウが開く。
     */
    public static final int CALL_SOMETHING = 45;

    /**
     * C(id): 大圆形竞技场、混沌的大战<br>
     * id - 1:开始 2:取消 3:结束
     */
    public static final int MSG_COLOSSEUM = 49;

    /** 血盟情报HTML */
    public static final int HTML_CLAN2 = 51;

    /** 料理清单 */
    public static final int COOK_WINDOW = 52;

    /** C(type) H(time): 状态图示:料理 */
    public static final int ICON_COOKING = 53;

    /** 鱼上钩的图示 */
    public static final int FISHING = 55;

    public S_PacketBox(int subCode) {
        writeC(Opcodes.S_OPCODE_PACKETBOX);
        writeC(subCode);

        switch (subCode) {
            case MSG_WAR_INITIATIVE:
            case MSG_WAR_OCCUPY:
            case MSG_MARRIED:
            case MSG_FEEL_GOOD:
            case MSG_CANT_LOGOUT:
            case LOGOUT:
            case FISHING:
                break;
            case CALL_SOMETHING:
                callSomething();
            default:
                break;
        }
    }

    public S_PacketBox(int subCode, int value) {
        writeC(Opcodes.S_OPCODE_PACKETBOX);
        writeC(subCode);

        switch (subCode) {
            case ICON_BLUEPOTION:
            case ICON_CHATBAN:
            case ICON_I2H:
            case ICON_POLYMORPH:
                writeH(value); // time
                break;
            case MSG_WAR_BEGIN:
            case MSG_WAR_END:
            case MSG_WAR_GOING:
                writeC(value); // castle id
                writeH(0); // ?
                break;
            case MSG_SMS_SENT:
            case WEIGHT:
            case FOOD:
                writeC(value);
                break;
            case MSG_ELF:
            case MSG_RANK_CHANGED:
            case MSG_COLOSSEUM:
                writeC(value); // msg id
                break;
            case MSG_LEVEL_OVER:
                writeC(0); // ?
                writeC(value); // 0-49以外は表示されない
                break;
            case COOK_WINDOW:
                writeC(0xdb); // ?
                writeC(0x31);
                writeC(0xdf);
                writeC(0x02);
                writeC(0x01);
                writeC(value); // level
                break;
            default:
                break;
        }
    }

    public S_PacketBox(int subCode, int type, int time) {
        writeC(Opcodes.S_OPCODE_PACKETBOX);
        writeC(subCode);

        switch (subCode) {
            case ICON_COOKING:
                if (type != 7) {
                    writeC(0x0c);
                    writeC(0x0c);
                    writeC(0x0c);
                    writeC(0x12);
                    writeC(0x0c);
                    writeC(0x09);
                    writeC(0x00);
                    writeC(0x00);
                    writeC(type);
                    writeC(0x24);
                    writeH(time);
                    writeH(0x00);
                } else {
                    writeC(0x0c);
                    writeC(0x0c);
                    writeC(0x0c);
                    writeC(0x12);
                    writeC(0x0c);
                    writeC(0x09);
                    writeC(0xc8);
                    writeC(0x00);
                    writeC(type);
                    writeC(0x26);
                    writeH(time);
                    writeC(0x3e);
                    writeC(0x87);
                }
                break;
            case MSG_DUEL:
                writeD(type); // 对方ID
                writeD(time); // 自己ID
                break;
            default:
                break;
        }
    }

    public S_PacketBox(int subCode, int id, String name, String clanName) {
        writeC(Opcodes.S_OPCODE_PACKETBOX);
        writeC(subCode);

        switch (subCode) {
            case MSG_WIN_LASTAVARD:
                writeD(id); // 血盟ID或者什么？
                writeS(name);
                writeS(clanName);
                break;
            default:
                break;
        }
    }

    public S_PacketBox(int subCode, Object[] names) {
        writeC(Opcodes.S_OPCODE_PACKETBOX);
        writeC(subCode);

        switch (subCode) {
            case ADD_EXCLUDE2:
                writeC(names.length);
                for (Object name : names) {
                    writeS(name.toString());
                }
                break;
            default:
                break;
        }
    }

    public S_PacketBox(int subCode, String name) {
        writeC(Opcodes.S_OPCODE_PACKETBOX);
        writeC(subCode);

        switch (subCode) {
            case ADD_EXCLUDE:
            case REM_EXCLUDE:
            case MSG_TOWN_LEADER:
                writeS(name);
                break;
            default:
                break;
        }
    }

    private void callSomething() {
        Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers()
                .iterator();

        writeC(L1World.getInstance().getAllPlayers().size());

        while (itr.hasNext()) {
            L1PcInstance pc = itr.next();
            Account acc = Account.load(pc.getAccountName());

            // 時間情報 とりあえずログイン時間を入れてみる
            if (acc == null) {
                writeD(0);
            } else {
                Calendar cal = Calendar.getInstance(TimeZone
                        .getTimeZone(Config.TIME_ZONE));
                long lastactive = acc.getLastActive().getTime();
                cal.setTimeInMillis(lastactive);
                cal.set(Calendar.YEAR, 1970);
                int time = (int) (cal.getTimeInMillis() / 1000);
                writeD(time); // JST 1970 1/1 09:00 が基準
            }

            // キャラ情報
            writeS(pc.getName()); // 半角12字まで
            writeS(pc.getClanname()); // []内に表示される文字列。半角12字まで
        }
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }

        return _byte;
    }

    @Override
    public String getType() {
        return S_PACKETBOX;
    }
}
