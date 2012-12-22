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
import l1j.jrwz.server.model.Dungeon;
import l1j.jrwz.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來進入傳點的封包
 */
public class C_EnterPortal extends ClientBasePacket {

    private static final String C_ENTER_PORTAL = "[C] C_EnterPortal";
    @SuppressWarnings("unused")
    private static Logger _log = Logger
            .getLogger(C_EnterPortal.class.getName());

    public C_EnterPortal(byte abyte0[], ClientThread client) throws Exception {
        super(abyte0);
        int locx = readH();
        int locy = readH();
        L1PcInstance pc = client.getActiveChar();
        if (pc.isTeleport()) { // 傳送中
            return;
        }
        // 取得傳送的點
        Dungeon.getInstance().dg(locx, locy, pc.getMap().getId(), pc);
    }

    @Override
    public String getType() {
        return C_ENTER_PORTAL;
    }
}
