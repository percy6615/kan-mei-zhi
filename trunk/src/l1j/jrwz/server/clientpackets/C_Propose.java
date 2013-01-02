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
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_Message_YN;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.utils.FaceToFace;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來結婚的封包
 */
public class C_Propose extends ClientBasePacket {

    private static final String C_PROPOSE = "[C] C_Propose";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Propose.class.getName());

    public C_Propose(byte abyte0[], ClientThread clientthread) {
        super(abyte0);
        int c = readC();

        L1PcInstance pc = clientthread.getActiveChar();
        if (c == 0) { // /propose（/結婚）
            if (pc.isGhost()) {
                return;
            }
            L1PcInstance target = FaceToFace.faceToFace(pc);
            if (target != null) {
                if (pc.getPartnerId() != 0) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$657)); // \f1你己经结婚。
                    return;
                }
                if (target.getPartnerId() != 0) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$658)); // \f1你的对象已经结婚了。
                    return;
                }
                if (pc.get_sex() == target.get_sex()) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$661)); // \f1结婚对象性别必须和您不同。
                    return;
                }
                if (pc.getX() >= 33974 && pc.getX() <= 33976
                        && pc.getY() >= 33362 && pc.getY() <= 33365
                        && pc.getMapId() == 4 && target.getX() >= 33974
                        && target.getX() <= 33976 && target.getY() >= 33362
                        && target.getY() <= 33365 && target.getMapId() == 4) {
                    target.setTempID(pc.getId()); // 暫時儲存對象的角色ID
                    target.sendPackets(new S_Message_YN(654, pc.getName())); // %0 向你求婚，你答应吗?
                }
            }
        } else if (c == 1) { // /divorce（/離婚）
            if (pc.getPartnerId() == 0) {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$662)); // \f1你目前未婚。
                return;
            }
            pc.sendPackets(new S_Message_YN(653, "")); // 若你离婚，你的结婚戒指将会消失。你决定要离婚吗？(Y/N)
        }
    }

    @Override
    public String getType() {
        return C_PROPOSE;
    }
}
