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

package l1j.jrwz.server.serverpackets;

import l1j.jrwz.server.Opcodes;

// Referenced classes of package l1j.jrwz.server.serverpackets:
// ServerBasePacket

public class S_Paralysis extends ServerBasePacket {
    /** 你的身体完全麻痹了 */
    public static final int TYPE_PARALYSIS = 1;
    /** 你的身体完全麻痹了 */
    public static final int TYPE_PARALYSIS2 = 2;
    /** 睡眠状态 */
    public static final int TYPE_SLEEP = 3;
    /** 冻结状态 */
    public static final int TYPE_FREEZE = 4;
    /** 昏迷状态 */
    public static final int TYPE_STUN = 5;
    /** 束缚状态 */
    public static final int TYPE_BIND = 6;
    /** 解除传送锁定状态 */
    public static final int TYPE_TELEPORT_UNLOCK = 7;

    private static final String _S__2F_PARALYSIS = "[S] S_Paralysis";

    public S_Paralysis(int type, boolean flag) {
        writeC(Opcodes.S_OPCODE_PARALYSIS);
        if (type == TYPE_PARALYSIS) // 你的身体完全麻痹了。
        {
            if (flag == true) {
                writeC(2);
            } else {
                writeC(3);
            }
        } else if (type == TYPE_PARALYSIS2) // 你的身体完全麻痹了。
        {
            if (flag == true) {
                writeC(4);
            } else {
                writeC(5);
            }
        } else if (type == TYPE_TELEPORT_UNLOCK) // 解除传送锁定
        {
            writeC(7);
        } else if (type == TYPE_SLEEP) // 你开始沉睡。
        {
            if (flag == true) {
                writeC(10);
            } else {
                writeC(11);
            }
        } else if (type == TYPE_FREEZE) // 被冻结了。
        {
            if (flag == true) {
                writeC(12);
            } else {
                writeC(13);
            }
        } else if (type == TYPE_STUN) // 处于昏迷状态。
        {
            if (flag == true) {
                writeC(22);
            } else {
                writeC(23);
            }
        } else if (type == TYPE_BIND) // 双脚受困无法移动。
        {
            if (flag == true) {
                writeC(24);
            } else {
                writeC(25);
            }
        }
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }

    @Override
    public String getType() {
        return _S__2F_PARALYSIS;
    }
}
