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
 * Author: ChrisLiu.2007.06.30
 */
package l1j.jrwz.server.clientpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_Pledge;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來血盟的封包
 */
public class C_Pledge extends ClientBasePacket {

    private static final String C_PLEDGE = "[C] C_Pledge";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Pledge.class.getName());

    public C_Pledge(byte abyte0[], ClientThread clientthread) {
        super(abyte0);
        L1PcInstance pc = clientthread.getActiveChar();

        if (pc.getClanid() > 0) {
            L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
            if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
                pc.sendPackets(new S_Pledge("pledgeM", pc.getId(), clan
                        .getClanName(), clan.getOnlineMembersFPWithRank(), clan
                        .getAllMembersFPWithRank()));
            } else {
                pc.sendPackets(new S_Pledge("pledge", pc.getId(), clan
                        .getClanName(), clan.getOnlineMembersFP()));
            }
        } else {
            pc.sendPackets(new S_ServerMessage(1064)); // 不属于血盟。
            // pc.sendPackets(new S_Pledge("pledge", pc.getId()));
        }
    }

    @Override
    public String getType() {
        return C_PLEDGE;
    }

}
