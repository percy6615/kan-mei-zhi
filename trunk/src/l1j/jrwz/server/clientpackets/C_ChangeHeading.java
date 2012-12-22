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
package l1j.jrwz.server.clientpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_ChangeHeading;

/**
 * 收到由客戶端傳來改變面向的封包
 */
public class C_ChangeHeading extends ClientBasePacket {
    private static final String C_CHANGE_HEADING = "[C] C_ChangeHeading";
    private static Logger _log = Logger.getLogger(C_ChangeHeading.class
            .getName());

    public C_ChangeHeading(byte[] decrypt, ClientThread client) {
        super(decrypt);
        int heading = readC();

        L1PcInstance pc = client.getActiveChar();

        pc.setHeading(heading);

        _log.finest("Change Heading : " + pc.getHeading());

        if (pc.isGmInvis() || pc.isGhost()) {
        } else if (pc.isInvisble()) {
            pc.broadcastPacketForFindInvis(new S_ChangeHeading(pc), true);
        } else {
            pc.broadcastPacket(new S_ChangeHeading(pc));
        }
    }

    @Override
    public String getType() {
        return C_CHANGE_HEADING;
    }
}
