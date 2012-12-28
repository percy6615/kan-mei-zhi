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

import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1PcInstance;

public class S_Exp extends ServerBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_Exp.class.getName());

    private static final String S_EXP = "[S] S_Exp";

    /**
     * レベルと経験値データを送る。
     * 
     * @param pc
     *            - PC
     */
    public S_Exp(L1PcInstance pc) {
        writeC(Opcodes.S_OPCODE_EXP);
        writeC(pc.getLevel());
        writeD(pc.getExp());

        // writeC(Opcodes.S_OPCODE_EXP);
        // writeC(0x39);// level
        // writeD(_objid);// ??
        // writeC(0x0A);// ??
        // writeH(getexp);// min exp
        // writeH(getexpreward);// max exp
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }

    @Override
    public String getType() {
        return S_EXP;
    }
}
