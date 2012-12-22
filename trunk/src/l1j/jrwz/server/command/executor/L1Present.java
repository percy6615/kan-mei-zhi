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

import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.L1DwarfInventory;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.templates.L1Item;

public class L1Present implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1Present.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1Present();
    }

    private L1Present() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String account = st.nextToken();
            int itemid = Integer.parseInt(st.nextToken(), 10);
            int enchant = Integer.parseInt(st.nextToken(), 10);
            int count = Integer.parseInt(st.nextToken(), 10);

            L1Item temp = ItemTable.getInstance().getTemplate(itemid);
            if (temp == null) {
                pc.sendPackets(new S_SystemMessage("不存在的道具编号。"));
                return;
            }

            L1DwarfInventory.present(account, itemid, enchant, count);
            pc.sendPackets(new S_SystemMessage(temp.getIdentifiedNameId()
                    + " 数量 " + count + " 个发送出去了。", true));
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 : "
                    + ".present 帐号 道具编号 数量 强化等级。（* 等于所有帐号）"));
        }
    }
}
