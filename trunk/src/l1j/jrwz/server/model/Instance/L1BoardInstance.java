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

package l1j.jrwz.server.model.Instance;

import java.util.logging.Logger;

import l1j.jrwz.server.serverpackets.S_Board;
import l1j.jrwz.server.serverpackets.S_BoardRead;
import l1j.jrwz.server.templates.L1Npc;

public class L1BoardInstance extends L1NpcInstance {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1BoardInstance.class
            .getName());

    public L1BoardInstance(L1Npc template) {
        super(template);
    }

    @Override
    public void onAction(L1PcInstance player) {
        player.sendPackets(new S_Board(this));
    }

    public void onAction(L1PcInstance player, int number) {
        player.sendPackets(new S_Board(this, number));
    }

    public void onActionRead(L1PcInstance player, int number) {
        player.sendPackets(new S_BoardRead(number));
    }
}
