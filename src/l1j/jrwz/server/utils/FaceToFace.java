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

package l1j.jrwz.server.utils;

import java.util.List;
import java.util.logging.Logger;

import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.utils:
// FaceToFace

/**
 * 面对面.
 */
public class FaceToFace {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(FaceToFace.class.getName());

    public static L1PcInstance faceToFace(L1PcInstance pc) {
        int pcX = pc.getX();
        int pcY = pc.getY();
        int pcHeading = pc.getHeading();
        List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(pc,
                1);

        if (players.size() == 0) { // 如果1格范围内没有任何人
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$93)); // \f1你注视的地方没有人。
            return null;
        }
        for (L1PcInstance target : players) {
            int targetX = target.getX();
            int targetY = target.getY();
            int targetHeading = target.getHeading();
            if (pcHeading == 0 && pcX == targetX && pcY == (targetY + 1)) {
                if (targetHeading == 4) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 1 && pcX == (targetX - 1)
                    && pcY == (targetY + 1)) {
                if (targetHeading == 5) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 2 && pcX == (targetX - 1) && pcY == targetY) {
                if (targetHeading == 6) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 3 && pcX == (targetX - 1)
                    && pcY == (targetY - 1)) {
                if (targetHeading == 7) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 4 && pcX == targetX && pcY == (targetY - 1)) {
                if (targetHeading == 0) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 5 && pcX == (targetX + 1)
                    && pcY == (targetY - 1)) {
                if (targetHeading == 1) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 6 && pcX == (targetX + 1) && pcY == targetY) {
                if (targetHeading == 2) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            } else if (pcHeading == 7 && pcX == (targetX + 1)
                    && pcY == (targetY + 1)) {
                if (targetHeading == 3) {
                    return target;
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$91, target.getName())); // \f1%0%s 没有面对着你。
                    return null;
                }
            }
        }
        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$93)); // \f1你注视的地方没有人。
        return null;
    }

    private FaceToFace() {
    }
}
