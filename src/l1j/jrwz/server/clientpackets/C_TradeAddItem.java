/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be trading_partnerful,
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
import l1j.jrwz.server.model.L1Inventory;
import l1j.jrwz.server.model.L1Trade;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來增加交易物品的封包
 */
public class C_TradeAddItem extends ClientBasePacket {
    private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_TradeAddItem.class
            .getName());

    public C_TradeAddItem(byte abyte0[], ClientThread client) throws Exception {
        super(abyte0);

        int itemid = readD();
        int itemcount = readD();
        L1PcInstance pc = client.getActiveChar();
        L1Trade trade = new L1Trade();
        L1ItemInstance item = pc.getInventory().getItem(itemid);
        if (!item.getItem().isTradable()) {
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem().getName())); // \f1%0%d 是不可丢弃或转移的
            return;
        }
        if (item.getBless() >= 128) { // 封印的裝備
            // \f1%0%d 是不可丢弃或转移的
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem().getName()));
            return;
        }
        Object[] petlist = pc.getPetList().values().toArray();
        for (Object petObject : petlist) {
            if (petObject instanceof L1PetInstance) {
                L1PetInstance pet = (L1PetInstance) petObject;
                if (item.getId() == pet.getItemObjId()) {
                    // \f1%0%d 是不可丢弃或转移的
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem()
                            .getName()));
                    return;
                }
            }
        }

        L1PcInstance tradingPartner = (L1PcInstance) L1World.getInstance()
                .findObject(pc.getTradeID());
        if (tradingPartner == null) {
            return;
        }
        if (pc.getTradeOk()) {
            return;
        }
        if (tradingPartner.getInventory().checkAddItem(item, itemcount) != L1Inventory.OK) { // 檢查容量與重量
            tradingPartner.sendPackets(new S_ServerMessage(L1SystemMessageId.$270)); // \f1当你负担过重时不能交易。
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$271)); // \f1对方携带的物品过重，无法交易。
            return;
        }

        trade.TradeAddItem(pc, itemid, itemcount);
    }

    @Override
    public String getType() {
        return C_TRADE_ADD_ITEM;
    }
}
