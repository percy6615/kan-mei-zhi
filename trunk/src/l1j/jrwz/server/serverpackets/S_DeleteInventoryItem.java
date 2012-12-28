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

package l1j.jrwz.server.serverpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1ItemInstance;

public class S_DeleteInventoryItem extends ServerBasePacket {

    private static final String S_DELETE_INVENTORY_ITEM = "[S] S_DeleteInventoryItem";

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_DeleteInventoryItem.class
            .getName());

    /**
     * インベントリからアイテムを削除する。
     * 
     * @param item
     *            - 削除するアイテム
     */
    public S_DeleteInventoryItem(L1ItemInstance item) {
        if (item != null) {
            writeC(Opcodes.S_OPCODE_DELETEINVENTORYITEM);
            writeD(item.getId());
        }
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }

    @Override
    public String getType() {
        return S_DELETE_INVENTORY_ITEM;
    }
}
