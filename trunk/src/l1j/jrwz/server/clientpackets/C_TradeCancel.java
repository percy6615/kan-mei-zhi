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
import l1j.jrwz.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來交易取消的封包
 */
public class C_TradeCancel extends ClientBasePacket {

    private static final String C_TRADE_CANCEL = "[C] C_TradeCancel";
    @SuppressWarnings("unused")
    private static Logger _log = Logger
            .getLogger(C_TradeCancel.class.getName());

    public C_TradeCancel(byte abyte0[], ClientThread clientthread)
            throws Exception {
        super(abyte0);

        L1PcInstance player = clientthread.getActiveChar();
        L1Trade trade = new L1Trade();
        trade.TradeCancel(player);
    }

    @Override
    public String getType() {
        return C_TRADE_CANCEL;
    }

}
