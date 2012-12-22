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
package l1j.jrwz.server.command.executor;

import java.util.StringTokenizer;

import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SkillSound;
import l1j.jrwz.server.serverpackets.S_SystemMessage;

/**
 * GM指令：castgfxid
 */
public class L1CastGfx implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1CastGfx.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1CastGfx();
    }

    private L1CastGfx() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer stringtokenizer = new StringTokenizer(arg);
            int sprid = Integer.parseInt(stringtokenizer.nextToken());

            pc.sendPackets(new S_SkillSound(pc.getId(), sprid));
            pc.broadcastPacket(new S_SkillSound(pc.getId(), sprid));
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 " + cmdName + " castgfxid。"));
        }
    }
}
