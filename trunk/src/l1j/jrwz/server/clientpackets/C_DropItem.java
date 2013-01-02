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
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

/**
 * 處理收到由客戶端傳來丟道具到地上的封包
 */
public class C_DropItem extends ClientBasePacket {
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_DropItem.class.getName());
    private static final String C_DROP_ITEM = "[C] C_DropItem";

    public C_DropItem(byte[] decrypt, ClientThread client) throws Exception {
        super(decrypt);
        int x = readH();
        int y = readH();
        int objectId = readD();
        int count = readD();

        L1PcInstance pc = client.getActiveChar();
        if (pc.isGhost()) {
            return;
        }

        L1ItemInstance item = pc.getInventory().getItem(objectId);
        if (item != null) {
            if (!item.getItem().isTradable()) {
                // \f1%0%d是不可轉移的…
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem()
                        .getName()));
                return;
            }

            Object[] petlist = pc.getPetList().values().toArray();
            for (Object petObject : petlist) {
                if (petObject instanceof L1PetInstance) {
                    L1PetInstance pet = (L1PetInstance) petObject;
                    if (item.getId() == pet.getItemObjId()) {
                        // \f1%0%d是不可轉移的…
                        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem()
                                .getName()));
                        return;
                    }
                }
            }

            if (item.isEquipped()) {
                // \f1你不能夠放棄此樣物品。
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$125));
                return;
            }
            if (item.getBless() >= 128) { // 封印的裝備
                // \f1%0%d是不可轉移的…
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem()
                        .getName()));
                return;
            }

            pc.getInventory().tradeItem(item, count,
                    L1World.getInstance().getInventory(x, y, pc.getMapId()));
            pc.turnOnOffLight();
        }
    }

    @Override
    public String getType() {
        return C_DROP_ITEM;
    }
}
