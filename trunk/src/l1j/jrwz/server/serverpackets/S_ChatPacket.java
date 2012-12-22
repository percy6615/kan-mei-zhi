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

import l1j.jrwz.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.jrwz.server.serverpackets:
// ServerBasePacket

public class S_ChatPacket extends ServerBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_ChatPacket.class.getName());
    private static final String _S__1F_NORMALCHATPACK = "[S] S_ChatPacket";
    private byte[] _byte = null;

    public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {

        if (type == 0) { // 一般
            writeC(opcode);
            writeC(type);
            if (pc.isInvisble()) {
                writeD(0);
            } else {
                writeD(pc.getId());
            }
            writeS(pc.getName() + ": " + chat);
        } else if (type == 2) { // 大喊
            writeC(opcode);
            writeC(type);
            if (pc.isInvisble()) {
                writeD(0);
            } else {
                writeD(pc.getId());
            }
            writeS("<" + pc.getName() + "> " + chat);
            writeH(pc.getX());
            writeH(pc.getY());
        } else if (type == 3) { // 全体
            writeC(opcode);
            writeC(type);
            if (pc.isGm() == true) {
                writeS("[******] " + chat);
            } else {
                writeS("[" + pc.getName() + "] " + chat);
            }
        } else if (type == 4) { // 血盟
            writeC(opcode);
            writeC(type);
            writeS("{" + pc.getName() + "} " + chat);
        } else if (type == 9) { // 密语频道 (发送)
            writeC(opcode);
            writeC(type);
            writeS("-> (" + pc.getName() + ") " + chat);
        } else if (type == 11) { // 组队
            writeC(opcode);
            writeC(type);
            writeS("(" + pc.getName() + ") " + chat);
        } else if (type == 12) { // 交易
            writeC(opcode);
            writeC(type);
            writeS("[" + pc.getName() + "] " + chat);
        } else if (type == 13) { // 联盟
            writeC(opcode);
            writeC(type);
            writeS("{{" + pc.getName() + "}} " + chat);
        } else if (type == 14) { // 组队频道 (聊天)
            writeC(opcode);
            writeC(type);
            if (pc.isInvisble()) {
                writeD(0);
            } else {
                writeD(pc.getId());
            }
            writeS("(" + pc.getName() + ") " + chat);
        } else if (type == 16) { // 密语频道
            writeC(opcode);
            writeS(pc.getName());
            writeS(chat);
        }
    }

    @Override
    public byte[] getContent() {
        if (null == _byte) {
            _byte = _bao.toByteArray();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return _S__1F_NORMALCHATPACK;
    }

}
