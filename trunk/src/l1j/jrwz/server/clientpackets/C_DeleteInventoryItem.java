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
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來刪除身上道具的封包
 */
public class C_DeleteInventoryItem extends ClientBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_DeleteInventoryItem.class
            .getName());
    private static final String C_DELETE_INVENTORY_ITEM = "[C] C_DeleteInventoryItem";

    public C_DeleteInventoryItem(byte[] decrypt, ClientThread client) {
        super(decrypt);
        int itemObjectId = readD();
        L1PcInstance pc = client.getActiveChar();
        L1ItemInstance item = pc.getInventory().getItem(itemObjectId);

        // 沒有要刪除的道具
        if (item == null) {
            return;
        }

        if (item.getItem().isCantDelete()) {
            // \f1你不能夠放棄此樣物品。
            pc.sendPackets(new S_ServerMessage(125));
            return;
        }

        Object[] petlist = pc.getPetList().values().toArray();
        for (Object petObject : petlist) {
            if (petObject instanceof L1PetInstance) {
                L1PetInstance pet = (L1PetInstance) petObject;
                if (item.getId() == pet.getItemObjId()) {
                    // \f1%0%d是不可轉移的…
                    pc.sendPackets(new S_ServerMessage(210, item.getItem()
                            .getName()));
                    return;
                }
            }
        }

        if (item.isEquipped()) {
            // \f1你不能够放弃此样物品。
            pc.sendPackets(new S_ServerMessage(125));
            return;
        }
        if (item.getBless() >= 128) { // 封印された装備
            // \f1%0%d 是不可丢弃或转移的
            pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
            return;
        }

        pc.getInventory().removeItem(item, item.getCount());
        pc.turnOnOffLight();
    }

    @Override
    public String getType() {
        return C_DELETE_INVENTORY_ITEM;
    }
}
