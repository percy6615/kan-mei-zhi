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
package l1j.jrwz.server.model;

import java.util.List;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.model:
// L1DeleteItemOnGround

public class L1DeleteItemOnGround {
    private class DeleteTimer implements Runnable {
        public DeleteTimer() {
        }

        @Override
        public void run() {
            int time = Config.ALT_ITEM_DELETION_TIME * 60 * 1000 - 10 * 1000;
            for (;;) {
                try {
                    Thread.sleep(time);
                } catch (Exception exception) {
                    _log.warning("L1DeleteItemOnGround error: " + exception);
                    break;
                }
                L1World.getInstance().broadcastPacketToAll(
                        new S_ServerMessage(L1SystemMessageId.$166, "地上的物品", "10秒后删除。")); // \f1%0%s %4%1%3 %2.
                try {
                    Thread.sleep(10000);
                } catch (Exception exception) {
                    _log.warning("L1DeleteItemOnGround error: " + exception);
                    break;
                }
                deleteItem();
                L1World.getInstance().broadcastPacketToAll(
                        new S_ServerMessage(L1SystemMessageId.$166, "地上的物品", "被删除了。")); // \f1%0%s %4%1%3 %2.
            }
        }
    }

    private DeleteTimer _deleteTimer;

    private static final Logger _log = Logger
            .getLogger(L1DeleteItemOnGround.class.getName());

    public L1DeleteItemOnGround() {
    }

    private void deleteItem() {
        int numOfDeleted = 0;
        for (L1Object obj : L1World.getInstance().getObject()) {
            if (!(obj instanceof L1ItemInstance)) {
                continue;
            }

            L1ItemInstance item = (L1ItemInstance) obj;
            if (item.getX() == 0 && item.getY() == 0) { // 地面上のアイテムではなく、誰かの所有物
                continue;
            }
            if (item.getItem().getItemId() == 40515) { // 精霊の石
                continue;
            }
            if (L1HouseLocation.isInHouse(item.getX(), item.getY(),
                    item.getMapId())) { // アジト内
                continue;
            }

            List<L1PcInstance> players = L1World.getInstance()
                    .getVisiblePlayer(item, Config.ALT_ITEM_DELETION_RANGE);
            if (players.isEmpty()) { // 指定範囲内にプレイヤーが居なければ削除
                L1Inventory groundInventory = L1World
                        .getInstance()
                        .getInventory(item.getX(), item.getY(), item.getMapId());
                groundInventory.removeItem(item);
                numOfDeleted++;
            }
        }
        _log.fine("地上的物品自动删除。数量: " + numOfDeleted);
    }

    public void initialize() {
        if (!Config.ALT_ITEM_DELETION_TYPE.equalsIgnoreCase("auto")) {
            return;
        }

        _deleteTimer = new DeleteTimer();
        GeneralThreadPool.getInstance().execute(_deleteTimer); // タイマー開始
    }
}
