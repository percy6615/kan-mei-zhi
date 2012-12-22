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

import l1j.jrwz.server.datatables.NpcTable;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1SummonInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.templates.L1Npc;

public class L1Summon implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1Summon.class.getName());

    public static L1Summon getInstance() {
        return new L1Summon();
    }

    private L1Summon() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer tok = new StringTokenizer(arg);
            String nameid = tok.nextToken();
            int npcid = 0;
            try {
                npcid = Integer.parseInt(nameid);
            } catch (NumberFormatException e) {
                npcid = NpcTable.getInstance().findNpcIdByNameWithoutSpace(
                        nameid);
                if (npcid == 0) {
                    pc.sendPackets(new S_SystemMessage("找不到符合条件的NPC 。"));
                    return;
                }
            }
            int count = 1;
            if (tok.hasMoreTokens()) {
                count = Integer.parseInt(tok.nextToken());
            }
            L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
            for (int i = 0; i < count; i++) {
                L1SummonInstance summonInst = new L1SummonInstance(npc, pc);
                summonInst.setPetcost(0);
            }
            nameid = NpcTable.getInstance().getTemplate(npcid).get_name();
            pc.sendPackets(new S_SystemMessage(nameid + "(ID:" + npcid + ") ("
                    + count + ") 召唤了。"));
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入" + cmdName
                    + " npcid|name [数量] 。"));
        }
    }
}
