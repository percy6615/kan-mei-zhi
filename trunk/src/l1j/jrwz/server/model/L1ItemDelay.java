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

import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.templates.L1EtcItem;
import l1j.jrwz.server.templates.L1Item;

// Referenced classes of package l1j.jrwz.server.model:
// L1ItemDelay

public class L1ItemDelay {

    static class ItemDelayTimer implements Runnable {
        private final int _delayId;
        // private int _delayTime;
        private final L1Character _cha;

        public ItemDelayTimer(L1Character cha, int id, int time) {
            _cha = cha;
            _delayId = id;
            // _delayTime = time;
        }

        @Override
        public void run() {
            stopDelayTimer(_delayId);
        }

        public void stopDelayTimer(int delayId) {
            _cha.removeItemDelay(delayId);
        }
    }

    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(L1ItemDelay.class.getName());

    public static void onItemUse(ClientThread client, L1ItemInstance item) {
        int delayId = 0;
        int delayTime = 0;

        L1PcInstance pc = client.getActiveChar();

        if (item.getItem().getType2() == L1Item.TYPE2_ETC) {
            // 种类：其他物品
            delayId = ((L1EtcItem) item.getItem()).get_delayid();
            delayTime = ((L1EtcItem) item.getItem()).get_delaytime();
        } else if (item.getItem().getType2() == L1Item.TYPE2_WEAPON) {
            // 种类：武器
            return;
        } else if (item.getItem().getType2() == L1Item.TYPE2_ARMOR) {
            // 种类：防具

            if (item.getItem().getItemId() == 20077 || item.getItem().getItemId() == 20062
                    || item.getItem().getItemId() == 120077) {
                // 隐身斗篷、炎魔的血光斗篷
                if (item.isEquipped() && !pc.isInvisble()) {
                    pc.beginInvisTimer();
                }
            } else {
                return;
            }
        }

        ItemDelayTimer timer = new ItemDelayTimer(pc, delayId, delayTime);

        pc.addItemDelay(delayId, timer);
        GeneralThreadPool.getInstance().schedule(timer, delayTime);
    }

    private L1ItemDelay() {
    }

}
