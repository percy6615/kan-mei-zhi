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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_Disconnect;

public class L1PcDeleteTimer extends TimerTask {
    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(L1PcDeleteTimer.class
            .getName());

    private final L1PcInstance _pc;

    public L1PcDeleteTimer(L1PcInstance pc) {
        _pc = pc;
    }

    public void begin() {
        Timer timer = new Timer();
        timer.schedule(this, 10 * 60 * 1000);
    }

    @Override
    public void run() {
        _pc.sendPackets(new S_Disconnect());
        this.cancel();
    }

}
