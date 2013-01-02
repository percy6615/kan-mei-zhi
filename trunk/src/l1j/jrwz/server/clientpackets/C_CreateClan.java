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
import l1j.jrwz.server.datatables.ClanTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來建立血盟的封包
 */
public class C_CreateClan extends ClientBasePacket {

    private static final String C_CREATE_CLAN = "[C] C_CreateClan";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_CreateClan.class.getName());

    public C_CreateClan(byte abyte0[], ClientThread clientthread)
            throws Exception {
        super(abyte0);
        String s = readS();
        @SuppressWarnings("unused")
        int i = s.length();

        L1PcInstance l1pcinstance = clientthread.getActiveChar();
        if (l1pcinstance.isCrown()) { // 是王族
            if (l1pcinstance.getClanid() == 0) {

                for (L1Clan clan : L1World.getInstance().getAllClans()) { // 检查是否有同名的血盟
                    if (clan.getClanName().toLowerCase()
                            .equals(s.toLowerCase())) {
                        l1pcinstance.sendPackets(new S_ServerMessage(L1SystemMessageId.$99)); // \f1那个血盟名称已经存在。
                        return;
                    }
                }
                L1Clan clan = ClanTable.getInstance().createClan(l1pcinstance,
                        s); // 建立血盟
                if (clan != null) {
                    // l1pcinstance.setClanid(clan.getClanId());
                    // l1pcinstance.setClanname(clan.getClanName());
                    l1pcinstance.sendPackets(new S_ServerMessage(L1SystemMessageId.$84, s)); // 创立\f1%0 血盟。
                }
            } else {
                l1pcinstance.sendPackets(new S_ServerMessage(L1SystemMessageId.$86)); // \f1已经创立血盟。
            }
        } else {
            l1pcinstance.sendPackets(new S_ServerMessage(L1SystemMessageId.$85)); // \f1王子和公主才可创立血盟。
        }
    }

    @Override
    public String getType() {
        return C_CREATE_CLAN;
    }

}
