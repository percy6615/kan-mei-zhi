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
package l1j.jrwz.server;

import java.util.logging.Logger;

import l1j.jrwz.server.datatables.UBTable;
import l1j.jrwz.server.model.L1UltimateBattle;

/**
 * 无限大战时间控制器.
 */
public class UbTimeController implements Runnable {
    private static Logger _log = Logger.getLogger(UbTimeController.class
            .getName());

    private static UbTimeController _instance;

    public static UbTimeController getInstance() {
        if (_instance == null) {
            _instance = new UbTimeController();
        }
        return _instance;
    }

    private void checkUbTime() {
        for (L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
            if (ub.checkUbTime() && !ub.isActive()) {
                ub.start(); // 无限大战开始
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                checkUbTime(); // 开始检查无限大战的时间
                Thread.sleep(15000);
            }
        } catch (Exception e1) {
            _log.warning(e1.getMessage());
        }
    }

}
