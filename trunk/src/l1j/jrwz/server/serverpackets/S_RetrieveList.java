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

import java.io.IOException;

import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;

public class S_RetrieveList extends ServerBasePacket {
    public S_RetrieveList(int objid, L1PcInstance pc) {
        if (pc.getInventory().getSize() < 180) {
            int size = pc.getDwarfInventory().getSize();
            if (size > 0) {
                writeC(Opcodes.S_OPCODE_SHOWRETRIEVELIST);
                writeD(objid);
                writeH(size);
                writeC(3); // 個人倉庫
                for (Object itemObject : pc.getDwarfInventory().getItems()) {
                    L1ItemInstance item = (L1ItemInstance) itemObject;
                    writeD(item.getId());
                    writeC(0);
                    writeH(item.get_gfxid());
                    writeC(item.getBless());
                    writeD(item.getCount());
                    writeC(item.isIdentified() ? 1 : 0);
                    writeS(item.getViewName());
                }
            }
            // クライアントに適当なメッセージ見つからなかったので非表示
            /*
             * else { l1pcinstance.sendPackets(new
             * S_SystemMessage("何もお預かりしていません。")); }
             */
        } else {
            pc.sendPackets(new S_ServerMessage(263)); // \f1一人のキャラクターが持って歩けるアイテムは最大180個までです。
        }
    }

    @Override
    public byte[] getContent() throws IOException {
        return getBytes();
    }

}
