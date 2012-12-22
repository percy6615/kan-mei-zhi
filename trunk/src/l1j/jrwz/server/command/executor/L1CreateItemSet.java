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
import java.util.StringTokenizer;

import l1j.jrwz.server.GMCommandsConfig;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.templates.L1Item;
import l1j.jrwz.server.templates.L1ItemSetItem;

/**
 * GM指令：創立套裝
 */
public class L1CreateItemSet implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1CreateItemSet.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1CreateItemSet();
    }

    private L1CreateItemSet() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            String name = new StringTokenizer(arg).nextToken();
            List<L1ItemSetItem> list = GMCommandsConfig.ITEM_SETS.get(name);
            if (list == null) {
                pc.sendPackets(new S_SystemMessage(name + " 是未定义的套装。"));
                return;
            }
            for (L1ItemSetItem item : list) {
                L1Item temp = ItemTable.getInstance().getTemplate(item.getId());
                if (!temp.isStackable() && 0 != item.getEnchant()) {
                    for (int i = 0; i < item.getAmount(); i++) {
                        L1ItemInstance inst = ItemTable.getInstance()
                                .createItem(item.getId());
                        inst.setEnchantLevel(item.getEnchant());
                        pc.getInventory().storeItem(inst);
                    }
                } else {
                    pc.getInventory().storeItem(item.getId(), item.getAmount());
                }
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 .itemset 套装名称。"));
        }
    }
}
