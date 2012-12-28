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

import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_WISDOM_POTION;

import java.util.logging.Logger;

import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1PcInstance;

public class S_SPMR extends ServerBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_SPMR.class.getName());
    private static final String S_SPMR = "[S] S_S_SPMR";

    private byte[] _byte = null;

    public S_SPMR(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(Opcodes.S_OPCODE_SPMR);
        // ウィズダムポーションのSPはS_SkillBrave送信時に更新されるため差し引いておく
        if (pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
            writeC(pc.getSp() - pc.getTrueSp() - 2); // 装備増加したSP
        } else {
            writeC(pc.getSp() - pc.getTrueSp()); // 装備増加したSP
        }
        writeC(pc.getTrueMr() - pc.getBaseMr()); // 装備や魔法で増加したMR
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return S_SPMR;
    }
}
