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
package l1j.jrwz.server.model.monitor;

import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.model.Instance.L1PcInstance;

public class L1PcGhostMonitor extends L1PcMonitor {

    public L1PcGhostMonitor(int oId) {
        super(oId);
    }

    @Override
    public void execTask(L1PcInstance pc) {
        // endGhostの実行時間が影響ないように
        Runnable r = new L1PcMonitor(pc.getId()) {
            @Override
            public void execTask(L1PcInstance pc) {
                pc.endGhost();
            }
        };
        GeneralThreadPool.getInstance().execute(r);
    }
}
