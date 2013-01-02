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

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_PacketBox;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.serverpackets.S_WhoCharinfo;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來查詢線上人數的封包
 */
public class C_Who extends ClientBasePacket {

    private static final String C_WHO = "[C] C_Who";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Who.class.getName());

    public C_Who(byte[] decrypt, ClientThread client) {
        super(decrypt);
        if (!Config.ALT_WHO_COMMAND) {
            return;
        }
        String s = readS();
        L1PcInstance find = L1World.getInstance().getPlayer(s);
        L1PcInstance pc = client.getActiveChar();
        if (find != null) {
            S_WhoCharinfo s_whocharinfo = new S_WhoCharinfo(find);
            pc.sendPackets(s_whocharinfo);
        } else if (s != null && !"".equals(s)) {
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$109, s));
        } else {
            String amount = String.valueOf(L1World.getInstance().getAllPlayers().size());
            // S_WhoAmount s_whoamount = new S_WhoAmount(amount);
            // pc.sendPackets(s_whoamount);
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$81,amount));
            if (pc.isGm()) { // GM点选玩家清单可瞬移到玩家身边
                pc.sendPackets(new S_PacketBox(S_PacketBox.CALL_SOMETHING));
            }
        }
    }

    @Override
    public String getType() {
        return C_WHO;
    }
}
