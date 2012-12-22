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
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_Message_YN;
import l1j.jrwz.server.utils.FaceToFace;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來交易的封包
 */
public class C_Trade extends ClientBasePacket {

    private static final String C_TRADE = "[C] C_Trade";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Trade.class.getName());

    public C_Trade(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);

        L1PcInstance player = clientthread.getActiveChar();
        if (player.isGhost()) {
            return;
        }
        L1PcInstance target = FaceToFace.faceToFace(player);
        if (target != null) {
            if (!target.isParalyzed()) {
                player.setTradeID(target.getId()); // 相手のオブジェクトIDを保存しておく
                target.setTradeID(player.getId());
                target.sendPackets(new S_Message_YN(252, player.getName())); // %0%s 要与你交易。是否接受？(Y/N)
            }
        }
    }

    @Override
    public String getType() {
        return C_TRADE;
    }
}
