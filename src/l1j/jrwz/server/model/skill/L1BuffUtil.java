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
package l1j.jrwz.server.model.skill;

import static l1j.jrwz.server.model.skill.L1SkillId.BLOODLUST;
import static l1j.jrwz.server.model.skill.L1SkillId.HOLY_WALK;
import static l1j.jrwz.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_RIBRAVE;
import static l1j.jrwz.server.model.skill.L1SkillId.WIND_WALK;

import java.util.logging.Logger;

import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SkillBrave;
import l1j.jrwz.server.serverpackets.S_SkillHaste;
import l1j.jrwz.server.serverpackets.S_SkillSound;

public class L1BuffUtil {
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1BuffUtil.class.getName());

    public static void brave(L1PcInstance pc, int timeMillis) {
        if (pc.hasSkillEffect(STATUS_ELFBRAVE)) { // エルヴンワッフルとは重複しない
            pc.killSkillEffectTimer(STATUS_ELFBRAVE);
            pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
            pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            pc.setBraveSpeed(0);
        }
        if (pc.hasSkillEffect(HOLY_WALK)) { // ホーリーウォークとは重複しない
            pc.killSkillEffectTimer(HOLY_WALK);
            pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
            pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            pc.setBraveSpeed(0);
        }
        if (pc.hasSkillEffect(MOVING_ACCELERATION)) { // ムービングアクセレーションとは重複しない
            pc.killSkillEffectTimer(MOVING_ACCELERATION);
            pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
            pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            pc.setBraveSpeed(0);
        }
        if (pc.hasSkillEffect(WIND_WALK)) { // ウィンドウォークとは重複しない
            pc.killSkillEffectTimer(WIND_WALK);
            pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
            pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            pc.setBraveSpeed(0);
        }
        if (pc.hasSkillEffect(STATUS_RIBRAVE)) { // ユグドラの実とは重複しない
            pc.killSkillEffectTimer(STATUS_RIBRAVE);
            // XXX ユグドラの実のアイコンを消す方法が不明
            pc.setBraveSpeed(0);
        }
        if (pc.hasSkillEffect(BLOODLUST)) { // ブラッドラストとは重複しない
            pc.killSkillEffectTimer(BLOODLUST);
            pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
            pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            pc.setBraveSpeed(0);
        }

        pc.setSkillEffect(STATUS_BRAVE, timeMillis);

        int objId = pc.getId();
        pc.sendPackets(new S_SkillBrave(objId, 1, timeMillis / 1000));
        pc.broadcastPacket(new S_SkillBrave(objId, 1, 0));
        pc.sendPackets(new S_SkillSound(objId, 751));
        pc.broadcastPacket(new S_SkillSound(objId, 751));
        pc.setBraveSpeed(1);
    }

    public static void haste(L1PcInstance pc, int timeMillis) {
        pc.setSkillEffect(STATUS_HASTE, timeMillis);

        int objId = pc.getId();
        pc.sendPackets(new S_SkillHaste(objId, 1, timeMillis / 1000));
        pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
        pc.sendPackets(new S_SkillSound(objId, 191));
        pc.broadcastPacket(new S_SkillSound(objId, 191));
        pc.setMoveSpeed(1);
    }
}
