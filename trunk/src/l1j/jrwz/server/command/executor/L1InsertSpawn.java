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
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.server.datatables.NpcSpawnTable;
import l1j.jrwz.server.datatables.NpcTable;
import l1j.jrwz.server.datatables.SpawnTable;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.templates.L1Npc;
import l1j.jrwz.server.utils.L1SpawnUtil;

public class L1InsertSpawn implements L1CommandExecutor {
    private static Logger _log = Logger
            .getLogger(L1InsertSpawn.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1InsertSpawn();
    }

    private L1InsertSpawn() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        String msg = null;

        try {
            StringTokenizer tok = new StringTokenizer(arg);
            String type = tok.nextToken();
            int npcId = Integer.parseInt(tok.nextToken().trim());
            L1Npc template = NpcTable.getInstance().getTemplate(npcId);

            if (template == null) {
                msg = "找不到符合条件的NPC。";
                return;
            }
            if (type.equals("mob")) {
                if (!template.getImpl().equals("L1Monster")) {
                    msg = "指定的NPC不是L1Monster类型。";
                    return;
                }
                SpawnTable.storeSpawn(pc, template);
            } else if (type.equals("npc")) {
                NpcSpawnTable.getInstance().storeSpawn(pc, template);
            }
            L1SpawnUtil.spawn(pc, npcId, 0, 0);
            msg = new StringBuilder().append(template.get_name())
                    .append(" (" + npcId + ") ").append("新增到数据库中。").toString();
        } catch (Exception e) {
            _log.log(Level.SEVERE, "", e);
            msg = "请输入 : " + cmdName + " mob|npc NPCID 。";
        } finally {
            if (msg != null) {
                pc.sendPackets(new S_SystemMessage(msg));
            }
        }
    }
}
