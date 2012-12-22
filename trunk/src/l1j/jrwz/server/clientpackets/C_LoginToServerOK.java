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

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來登入到伺服器OK的封包
 */
public class C_LoginToServerOK extends ClientBasePacket {

    private static final String C_LOGIN_TO_SERVER_OK = "[C] C_LoginToServerOK";

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_LoginToServerOK.class
            .getName());

    public C_LoginToServerOK(byte[] decrypt, ClientThread client) {
        super(decrypt);

        int type = readC();
        int button = readC();

        L1PcInstance pc = client.getActiveChar();

        if (type == 255) { // 全體聊天 && 密語
            if (button == 95 || button == 127) {
                pc.setShowWorldChat(true); // open
                pc.setCanWhisper(true); // open
            } else if (button == 91 || button == 123) {
                pc.setShowWorldChat(true); // open
                pc.setCanWhisper(false); // close
            } else if (button == 94 || button == 126) {
                pc.setShowWorldChat(false); // close
                pc.setCanWhisper(true); // open
            } else if (button == 90 || button == 122) {
                pc.setShowWorldChat(false); // close
                pc.setCanWhisper(false); // close
            }
        } else if (type == 0) { // 全體聊天
            if (button == 0) { // close
                pc.setShowWorldChat(false);
            } else if (button == 1) { // open
                pc.setShowWorldChat(true);
            }
        } else if (type == 2) { // 密語
            if (button == 0) { // close
                pc.setCanWhisper(false);
            } else if (button == 1) { // open
                pc.setCanWhisper(true);
            }
        } else if (type == 6) { // 交易頻道
            if (button == 0) { // close
                pc.setShowTradeChat(false);
            } else if (button == 1) { // open
                pc.setShowTradeChat(true);
            }
        }
    }

    @Override
    public String getType() {
        return C_LOGIN_TO_SERVER_OK;
    }
}
