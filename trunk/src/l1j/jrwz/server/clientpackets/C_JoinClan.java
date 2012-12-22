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

package l1j.jrwz.server.clientpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_Message_YN;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.utils.FaceToFace;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來加入血盟的封包
 */
public class C_JoinClan extends ClientBasePacket {

    private static final String C_JOIN_CLAN = "[C] C_JoinClan";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_JoinClan.class.getName());

    public C_JoinClan(byte abyte0[], ClientThread clientthread)
            throws Exception {
        super(abyte0);

        L1PcInstance pc = clientthread.getActiveChar();
        if (pc.isGhost()) {
            return;
        }

        L1PcInstance target = FaceToFace.faceToFace(pc);
        if (target != null) {
            JoinClan(pc, target);
        }
    }

    @Override
    public String getType() {
        return C_JOIN_CLAN;
    }

    private void JoinClan(L1PcInstance player, L1PcInstance target) {
        if (!target.isCrown()) { // 如果面對的對象不是王族
            player.sendPackets(new S_ServerMessage(92, target.getName())); // \f1%0%d 不是王子或公主。
            return;
        }

        int clan_id = target.getClanid();
        String clan_name = target.getClanname();
        if (clan_id == 0) { // 面對的對象沒有創立血盟
            player.sendPackets(new S_ServerMessage(90, target.getName())); // \f1%0%d 尚未创建血盟。
            return;
        }

        L1Clan clan = L1World.getInstance().getClan(clan_name);
        if (clan == null) {
            return;
        }

        if (target.getId() != clan.getLeaderId()) { // 面對的對象不是盟主
            player.sendPackets(new S_ServerMessage(92, target.getName())); // \f1%0%d 不是王子或公主。
            return;
        }

        if (player.getClanid() != 0) { // 已經加入血盟
            if (player.isCrown()) { // 自己是盟主
                String player_clan_name = player.getClanname();
                L1Clan player_clan = L1World.getInstance().getClan(
                        player_clan_name);
                if (player_clan == null) {
                    return;
                }

                if (player.getId() != player_clan.getLeaderId()) { // 已經加入其他血盟
                    player.sendPackets(new S_ServerMessage(89)); // \f1你已拥有所属血盟。
                    return;
                }

                if (player_clan.getCastleId() != 0 || // 有城堡或有血盟小屋
                        player_clan.getHouseId() != 0) {
                    player.sendPackets(new S_ServerMessage(665)); // \f1拥有城堡与血盟小屋的状态下无法解散血盟。
                    return;
                }
            } else {
                player.sendPackets(new S_ServerMessage(89)); // \f1你已拥有所属血盟。
                return;
            }
        }

        target.setTempID(player.getId()); // 暫時保存面對的人的ID
        target.sendPackets(new S_Message_YN(97, player.getName())); // %0%s 想加入你的血盟。你接受吗？(Y/N)
    }
}
