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
 * along with this program; if not, Delete to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.jrwz.server.clientpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.datatables.BoardTable;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1BoardInstance;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 收到由客戶端傳送刪除公告欄的封包
 */
public class C_BoardDelete extends ClientBasePacket {

    private static final String C_BOARD_DELETE = "[C] C_BoardDelete";
    @SuppressWarnings("unused")
    private static Logger _log = Logger
            .getLogger(C_BoardDelete.class.getName());

    public C_BoardDelete(byte decrypt[], ClientThread client) {
        super(decrypt);
        int objId = readD();
        int topicId = readD();
        L1Object obj = L1World.getInstance().findObject(objId);
        L1BoardInstance board = (L1BoardInstance) obj;
        if (board != null) {
            BoardTable.getInstance().deleteTopic(topicId);
        }
    }

    @Override
    public String getType() {
        return C_BOARD_DELETE;
    }

}
