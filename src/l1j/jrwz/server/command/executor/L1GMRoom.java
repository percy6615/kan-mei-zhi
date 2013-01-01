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

import l1j.jrwz.server.GMCommandsConfig;
import l1j.jrwz.server.model.L1Location;
import l1j.jrwz.server.model.L1Teleport;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;

public class L1GMRoom implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1GMRoom.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1GMRoom();
    }

    private L1GMRoom() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int i = 0;
            try {
                i = Integer.parseInt(arg);
            } catch (NumberFormatException e) {
            }

            if (i == 1) {
                L1Teleport.teleport(pc, 32737, 32796, 99, 5, false);
            } else if (i == 2) {
                L1Teleport.teleport(pc, 32734, 32799, 17100, 5, false); // 17100!?
            } else if (i == 3) {
                L1Teleport.teleport(pc, 32644, 32955, 0, 5, false);
            } else if (i == 4) {
                L1Teleport.teleport(pc, 33429, 32814, 4, 5, false);
            } else if (i == 5) {
                L1Teleport.teleport(pc, 32894, 32535, 300, 5, false);
            } else {
                L1Location loc = GMCommandsConfig.ROOMS.get(arg.toLowerCase());
                if (loc == null) {
                    pc.sendPackets(new S_SystemMessage(arg + " 未定义的Room。"));
                    return;
                }
                L1Teleport.teleport(pc, loc.getX(), loc.getY(),
                        loc.getMapId(), 5, false);
            }
        } catch (Exception exception) {
            pc.sendPackets(new S_SystemMessage(
                    ".gmroom1～.gmroom5 or .gmroom name 请输入。"));
        }
    }
}
