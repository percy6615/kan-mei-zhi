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

import java.util.List;

import l1j.jrwz.server.command.L1Commands;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.templates.L1Command;

/**
 * GM指令：取得所有指令
 */
public class L1CommandHelp implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1CommandHelp.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1CommandHelp();
    }

    private L1CommandHelp() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        List<L1Command> list = L1Commands.availableCommandList(pc
                .getAccessLevel());
        pc.sendPackets(new S_SystemMessage(join(list, ", ")));
    }

    private String join(List<L1Command> list, String with) {
        StringBuilder result = new StringBuilder();
        for (L1Command cmd : list) {
            if (result.length() > 0) {
                result.append(with);
            }
            result.append(cmd.getName());
        }
        return result.toString();
    }
}
