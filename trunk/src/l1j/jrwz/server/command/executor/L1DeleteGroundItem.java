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

import java.util.ArrayList;

import l1j.jrwz.server.datatables.FurnitureSpawnTable;
import l1j.jrwz.server.datatables.LetterTable;
import l1j.jrwz.server.datatables.PetTable;
import l1j.jrwz.server.model.L1Inventory;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1FurnitureInstance;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;

/**
 * GM指令：刪除地上道具
 */
public class L1DeleteGroundItem implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1DeleteGroundItem.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1DeleteGroundItem();
    }

    private L1DeleteGroundItem() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        for (L1Object l1object : L1World.getInstance().getObject()) {
            if (l1object instanceof L1ItemInstance) {
                L1ItemInstance l1iteminstance = (L1ItemInstance) l1object;
                if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) { // 地面上のアイテムではなく、誰かの所有物
                    continue;
                }

                ArrayList<L1PcInstance> players = L1World.getInstance()
                        .getVisiblePlayer(l1iteminstance, 0);
                if (0 == players.size()) {
                    L1Inventory groundInventory = L1World.getInstance()
                            .getInventory(l1iteminstance.getX(),
                                    l1iteminstance.getY(),
                                    l1iteminstance.getMapId());
                    int itemId = l1iteminstance.getItem().getItemId();
                    if (itemId == 40314 || itemId == 40316) { // ペットのアミュレット
                        PetTable.getInstance()
                                .deletePet(l1iteminstance.getId());
                    } else if (itemId >= 49016 && itemId <= 49025) { // 便箋
                        LetterTable lettertable = new LetterTable();
                        lettertable.deleteLetter(l1iteminstance.getId());
                    } else if (itemId >= 41383 && itemId <= 41400) { // 家具
                        if (l1object instanceof L1FurnitureInstance) {
                            L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
                            if (furniture.getItemObjId() == l1iteminstance
                                    .getId()) { // 既に引き出している家具
                                FurnitureSpawnTable.getInstance()
                                        .deleteFurniture(furniture);
                            }
                        }
                    }
                    groundInventory.deleteItem(l1iteminstance);
                    L1World.getInstance().removeVisibleObject(l1iteminstance);
                    L1World.getInstance().removeObject(l1iteminstance);
                }
            }
        }
        L1World.getInstance().broadcastServerMessage("地上的垃圾被GM清除了。");
    }
}
