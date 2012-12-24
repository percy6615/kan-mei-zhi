/* This program is free software; you can redistribute it and/or modify
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
package l1j.jrwz.server.clientpackets;

import static l1j.jrwz.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;
import static l1j.jrwz.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.jrwz.server.model.skill.L1SkillId.MEDITATION;

import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.model.AcceleratorChecker;
import l1j.jrwz.server.model.Dungeon;
import l1j.jrwz.server.model.DungeonRandom;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.trap.L1WorldTraps;
import l1j.jrwz.server.serverpackets.S_MoveCharPacket;
import l1j.jrwz.server.serverpackets.S_SystemMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來移動角色的封包
 */
public class C_MoveChar extends ClientBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_MoveChar.class.getName());

    private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

    private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;

    // 移動
    public C_MoveChar(byte decrypt[], ClientThread client) throws Exception {
        super(decrypt);
        int locx = readH();
        int locy = readH();
        int heading = readC();

        L1PcInstance pc = client.getActiveChar();

        if (pc.isTeleport()) { // 傳送中
            return;
        }

        // 檢查移動的時間間隔
        if (Config.CHECK_MOVE_INTERVAL) {
            int result;
            result = pc.getAcceleratorChecker().checkInterval(
                    AcceleratorChecker.ACT_TYPE.MOVE);
            if (result == AcceleratorChecker.R_DISCONNECTED) {
                return;
            }
        }

        pc.killSkillEffectTimer(MEDITATION);
        pc.setCallClanId(0); // 移动后 召唤盟友技能 无效

        if (!pc.hasSkillEffect(ABSOLUTE_BARRIER)) { // 絕對屏障
            pc.setRegenState(REGENSTATE_MOVE);
        }
        pc.getMap().setPassable(pc.getLocation(), true);

        if (CLIENT_LANGUAGE == 3) { // Taiwan Only
            heading ^= 0x49;
            locx = pc.getX();
            locy = pc.getY();
        }

        locx += HEADING_TABLE_X[heading];
        locy += HEADING_TABLE_Y[heading];

        if (Dungeon.getInstance().dg(locx, locy, pc.getMap().getId(), pc)) { // 傳點
            return;
        }
        if (DungeonRandom.getInstance().dg(locx, locy, pc.getMap().getId(), pc)) { // 取得隨機傳送地點
            return;
        }

        pc.getLocation().set(locx, locy);
        pc.setHeading(heading);
        if (pc.isGmInvis() || pc.isGhost()) {
        } else if (pc.isInvisble()) {
            pc.broadcastPacketForFindInvis(new S_MoveCharPacket(pc), true);
        } else {
            pc.broadcastPacket(new S_MoveCharPacket(pc));
        }

        // sendMapTileLog(pc); //發送信息的目的地瓦（為調查地圖）

        L1WorldTraps.getInstance().onPlayerMoved(pc);

        pc.getMap().setPassable(pc.getLocation(), false);
        // user.UpdateObject(); // 可視範囲内の全オブジェクト更新
    }

    // 地圖編號的研究
    @SuppressWarnings("unused")
    private void sendMapTileLog(L1PcInstance pc) {
        pc.sendPackets(new S_SystemMessage(pc.getMap().toString(
                pc.getLocation())));
    }
}
