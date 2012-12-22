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
package l1j.jrwz.server.model.trap;

import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.storage.TrapStorage;
import l1j.jrwz.server.utils.Dice;

public class L1HealingTrap extends L1Trap {
    private final Dice _dice;
    private final int _base;
    private final int _diceCount;

    public L1HealingTrap(TrapStorage storage) {
        super(storage);

        _dice = new Dice(storage.getInt("dice"));
        _base = storage.getInt("base");
        _diceCount = storage.getInt("diceCount");
    }

    @Override
    public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
        sendEffect(trapObj);

        int pt = _dice.roll(_diceCount) + _base;

        trodFrom.healHp(pt);
    }
}
