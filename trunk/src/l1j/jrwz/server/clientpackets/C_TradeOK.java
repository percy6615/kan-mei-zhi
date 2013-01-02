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
import l1j.jrwz.server.model.L1Trade;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來交易OK的封包
 */
public class C_TradeOK extends ClientBasePacket {

    private static final String C_TRADE_CANCEL = "[C] C_TradeOK";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_TradeOK.class.getName());

    public C_TradeOK(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);

        L1PcInstance player = clientthread.getActiveChar();
        L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance()
                .findObject(player.getTradeID());
        if (trading_partner != null) {
            player.setTradeOk(true);

            if (player.getTradeOk() && trading_partner.getTradeOk()) // 同時都壓OK
            {
                // 檢查身上的空間是否還有 (180 - 16)
                if (player.getInventory().getSize() < (180 - 16)
                        && trading_partner.getInventory().getSize() < (180 - 16)) // お互いのアイテムを相手に渡す
                {
                    L1Trade trade = new L1Trade();
                    trade.TradeOK(player);
                } else // お互いのアイテムを手元に戻す
                {
                    player.sendPackets(new S_ServerMessage(L1SystemMessageId.$263)); // \f1一个角色最多可携带180个道具。
                    trading_partner.sendPackets(new S_ServerMessage(L1SystemMessageId.$263)); // \f1一个角色最多可携带180个道具。
                    L1Trade trade = new L1Trade();
                    trade.TradeCancel(player);
                }
            }
        }
    }

    @Override
    public String getType() {
        return C_TRADE_CANCEL;
    }

}
