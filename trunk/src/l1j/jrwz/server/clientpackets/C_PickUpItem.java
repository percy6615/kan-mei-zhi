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
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.model.L1Inventory;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.model.item.L1ItemId;
import l1j.jrwz.server.serverpackets.S_AttackPacket;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

/**
 * 處理收到由客戶端傳來撿起道具的封包
 */
public class C_PickUpItem extends ClientBasePacket {

    private static final String C_PICK_UP_ITEM = "[C] C_PickUpItem";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_PickUpItem.class.getName());

    public C_PickUpItem(byte decrypt[], ClientThread client) throws Exception {
        super(decrypt);
        int x = readH();
        int y = readH();
        int objectId = readD();
        int pickupCount = readD();

        L1PcInstance pc = client.getActiveChar();
        if (pc.isDead() || pc.isGhost()) {
            return;
        }

        if (pc.isInvisble()) { // 隱身狀態
            return;
        }
        if (pc.isInvisDelay()) { // 還在解除隱身的延遲
            return;
        }

        L1Inventory groundInventory = L1World.getInstance().getInventory(x, y,
                pc.getMapId());
        L1Object object = groundInventory.getItem(objectId);

        if (object != null && !pc.isDead()) {
            L1ItemInstance item = (L1ItemInstance) object;
            if (item.getItemOwnerId() != 0
                    && pc.getId() != item.getItemOwnerId()) {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$623)); // 道具取得失败。
                return;
            }
            if (pc.getLocation().getTileLineDistance(item.getLocation()) > 3) {
                return;
            }

            if (item.getItem().getItemId() == L1ItemId.ADENA) {
                L1ItemInstance inventoryItem = pc.getInventory().findItemId(
                        L1ItemId.ADENA);
                int inventoryItemCount = 0;
                if (inventoryItem != null) {
                    inventoryItemCount = inventoryItem.getCount();
                }
                // 超過20億
                if ((long) inventoryItemCount + (long) pickupCount > 2000000000L) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$166, // \f1%0%s %4%1%3 %2.
                            "你身上的金币已经超过", "2,000,000,000了，所以不能捡起金币。"));
                    return;
                }
            }

            if (pc.getInventory().checkAddItem( // 檢查容量與重量
                    item, pickupCount) == L1Inventory.OK) {
                if (item.getX() != 0 && item.getY() != 0) { // ワールドマップ上のアイテム
                    groundInventory.tradeItem(item, pickupCount,
                            pc.getInventory());
                    pc.turnOnOffLight();

                    pc.sendPackets(new S_AttackPacket(pc, objectId,
                            ActionCodes.ACTION_Pickup));
                    if (!pc.isGmInvis()) {
                        pc.broadcastPacket(new S_AttackPacket(pc, objectId,
                                ActionCodes.ACTION_Pickup));
                    }
                }
            }
        }
    }

    @Override
    public String getType() {
        return C_PICK_UP_ITEM;
    }
}
