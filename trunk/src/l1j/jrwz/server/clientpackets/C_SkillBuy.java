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
import l1j.jrwz.server.serverpackets.S_SkillBuy;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來買魔法的封包
 */
public class C_SkillBuy extends ClientBasePacket {

    private static final String C_SKILL_BUY = "[C] C_SkillBuy";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_SkillBuy.class.getName());

    public C_SkillBuy(byte abyte0[], ClientThread clientthread)
            throws Exception {
        super(abyte0);

        int i = readD();

        L1PcInstance pc = clientthread.getActiveChar();
        if (pc.isGhost()) {
            return;
        }
        pc.sendPackets(new S_SkillBuy(i, pc));
        /*
         * int type = player.get_type(); int lvl = player.get_level();
         * 
         * switch(type) { case 0: // 君主 if(lvl >= 10) { player.sendPackets(new
         * S_SkillBuy(i, player)); } break;
         * 
         * case 1: // ナイト if(lvl >= 50) { player.sendPackets(new S_SkillBuy(i,
         * player)); } break;
         * 
         * case 2: // エルフ if(lvl >= 8) { player.sendPackets(new S_SkillBuy(i,
         * player)); } break;
         * 
         * case 3: // WIZ if(lvl >= 4) { player.sendPackets(new S_SkillBuy(i,
         * player)); } break;
         * 
         * case 4: //DE if(lvl >= 12) { player.sendPackets(new S_SkillBuy(i,
         * player)); } break;
         * 
         * default: break; }
         */
    }

    @Override
    public String getType() {
        return C_SKILL_BUY;
    }

}
