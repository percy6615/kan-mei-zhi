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
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 收到由客戶端傳來離開組隊的封包
 */
public class C_BanParty extends ClientBasePacket {

    private static final String C_BAN_PARTY = "[C] C_BanParty";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_BanParty.class.getName());

    public C_BanParty(byte decrypt[], ClientThread client) throws Exception {
        super(decrypt);
        String s = readS();

        L1PcInstance player = client.getActiveChar();
        if (!player.getParty().isLeader(player)) {
            // 是組對對長
            player.sendPackets(new S_ServerMessage(427)); // 只有領導者才有驅逐隊伍成員的權力。
            return;
        }

        for (L1PcInstance member : player.getParty().getMembers()) {
            if (member.getName().toLowerCase().equals(s.toLowerCase())) {
                player.getParty().kickMember(member);
                return;
            }
        }

        player.sendPackets(new S_ServerMessage(426, s)); // %0%d 不屬於任何隊伍。
    }

    @Override
    public String getType() {
        return C_BAN_PARTY;
    }

}
