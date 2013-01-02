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
import l1j.jrwz.server.model.L1ChatParty;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_Party;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來的聊天組隊封包
 */
public class C_ChatParty extends ClientBasePacket {

    private static final String C_CHAT_PARTY = "[C] C_ChatParty";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_ChatParty.class.getName());

    public C_ChatParty(byte abyte0[], ClientThread clientthread) {
        super(abyte0);

        L1PcInstance pc = clientthread.getActiveChar();
        if (pc.isGhost()) {
            return;
        }

        int type = readC();
        if (type == 0) { // /chatbanish 的命令
            String name = readS();

            if (!pc.isInChatParty()) {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$425)); // 您并没有参加任何队伍。
                return;
            }
            if (!pc.getChatParty().isLeader(pc)) {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$427)); // 只有领导者才有驱逐队伍成员的权力。
                return;
            }
            L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
            if (targetPc == null) {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$109)); // 没有叫%0 的人。
                return;
            }
            if (pc.getId() == targetPc.getId()) {
                return;
            }

            for (L1PcInstance member : pc.getChatParty().getMembers()) {
                if (member.getName().toLowerCase().equals(name.toLowerCase())) {
                    pc.getChatParty().kickMember(member);
                    return;
                }
            }
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$426, name)); // %0%d 不属于任何队伍。
        } else if (type == 1) { // /chatoutparty 的命令
            if (pc.isInChatParty()) {
                pc.getChatParty().leaveMember(pc);
            }
        } else if (type == 2) { // /chatparty 的命令
            L1ChatParty chatParty = pc.getChatParty();
            if (pc.isInChatParty()) {
                pc.sendPackets(new S_Party("party", pc.getId(), chatParty
                        .getLeader().getName(), chatParty.getMembersNameList()));
            } else {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$425)); // 您并没有参加任何队伍。
                // pc.sendPackets(new S_Party("party", pc.getId()));
            }
        }
    }

    @Override
    public String getType() {
        return C_CHAT_PARTY;
    }

}
