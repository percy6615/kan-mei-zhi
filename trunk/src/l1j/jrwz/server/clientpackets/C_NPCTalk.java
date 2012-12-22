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
import l1j.jrwz.server.datatables.NpcActionTable;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.npc.L1NpcHtml;
import l1j.jrwz.server.model.npc.action.L1NpcAction;
import l1j.jrwz.server.serverpackets.S_NPCTalkReturn;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket, C_NPCTalk

/**
 * 處理收到由客戶端傳來NPC講話的封包
 */
public class C_NPCTalk extends ClientBasePacket {

    private static final String C_NPC_TALK = "[C] C_NPCTalk";
    private static Logger _log = Logger.getLogger(C_NPCTalk.class.getName());

    public C_NPCTalk(byte abyte0[], ClientThread client) throws Exception {
        super(abyte0);
        int objid = readD();
        L1Object obj = L1World.getInstance().findObject(objid);
        L1PcInstance pc = client.getActiveChar();
        if (obj != null && pc != null) {
            L1NpcAction action = NpcActionTable.getInstance().get(pc, obj);
            if (action != null) {
                L1NpcHtml html = action.execute("", pc, obj, new byte[0]);
                if (html != null) {
                    pc.sendPackets(new S_NPCTalkReturn(obj.getId(), html));
                }
                return;
            }
            obj.onTalkAction(pc);
        } else {
            _log.severe("不正确的NPC objid=" + objid);
        }
    }

    @Override
    public String getType() {
        return C_NPC_TALK;
    }
}
