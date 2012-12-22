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
package l1j.jrwz.server.command.executor;

import static l1j.jrwz.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static l1j.jrwz.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.jrwz.server.model.skill.L1SkillId.AQUA_PROTECTER;
import static l1j.jrwz.server.model.skill.L1SkillId.BERSERKERS;
import static l1j.jrwz.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.jrwz.server.model.skill.L1SkillId.BOUNCE_ATTACK;
import static l1j.jrwz.server.model.skill.L1SkillId.BRAVE_AURA;
import static l1j.jrwz.server.model.skill.L1SkillId.BURNING_SPIRIT;
import static l1j.jrwz.server.model.skill.L1SkillId.BURNING_WEAPON;
import static l1j.jrwz.server.model.skill.L1SkillId.CLEAR_MIND;
import static l1j.jrwz.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static l1j.jrwz.server.model.skill.L1SkillId.DOUBLE_BRAKE;
import static l1j.jrwz.server.model.skill.L1SkillId.DRESS_EVASION;
import static l1j.jrwz.server.model.skill.L1SkillId.ELEMENTAL_FIRE;
import static l1j.jrwz.server.model.skill.L1SkillId.ELEMENTAL_PROTECTION;
import static l1j.jrwz.server.model.skill.L1SkillId.ENCHANT_VENOM;
import static l1j.jrwz.server.model.skill.L1SkillId.EXOTIC_VITALIZE;
import static l1j.jrwz.server.model.skill.L1SkillId.GLOWING_AURA;
import static l1j.jrwz.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static l1j.jrwz.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.jrwz.server.model.skill.L1SkillId.LIGHT;
import static l1j.jrwz.server.model.skill.L1SkillId.MEDITATION;
import static l1j.jrwz.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.jrwz.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.jrwz.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static l1j.jrwz.server.model.skill.L1SkillId.RESIST_MAGIC;
import static l1j.jrwz.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static l1j.jrwz.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static l1j.jrwz.server.model.skill.L1SkillId.UNCANNY_DODGE;
import static l1j.jrwz.server.model.skill.L1SkillId.VENOM_RESIST;
import static l1j.jrwz.server.model.skill.L1SkillId.WATER_LIFE;

import java.util.StringTokenizer;

import l1j.jrwz.server.datatables.SkillsTable;
import l1j.jrwz.server.model.L1PolyMorph;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.skill.L1BuffUtil;
import l1j.jrwz.server.model.skill.L1SkillUse;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.templates.L1Skills;

/**
 * GM指令：給對象所有魔法
 */
public class L1AllBuff implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1AllBuff.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1AllBuff();
    }

    private L1AllBuff() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        int[] allBuffSkill = { LIGHT, DECREASE_WEIGHT, PHYSICAL_ENCHANT_DEX,
                MEDITATION, PHYSICAL_ENCHANT_STR, BLESS_WEAPON, BERSERKERS,
                IMMUNE_TO_HARM, ADVANCE_SPIRIT, REDUCTION_ARMOR, BOUNCE_ATTACK,
                SOLID_CARRIAGE, ENCHANT_VENOM, BURNING_SPIRIT, VENOM_RESIST,
                DOUBLE_BRAKE, UNCANNY_DODGE, DRESS_EVASION, GLOWING_AURA,
                BRAVE_AURA, RESIST_MAGIC, CLEAR_MIND, ELEMENTAL_PROTECTION,
                AQUA_PROTECTER, BURNING_WEAPON, IRON_SKIN, EXOTIC_VITALIZE,
                WATER_LIFE, ELEMENTAL_FIRE, SOUL_OF_FLAME, ADDITIONAL_FIRE };
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String name = st.nextToken();
            L1PcInstance target = L1World.getInstance().getPlayer(name);
            if (target == null) {
                pc.sendPackets(new S_ServerMessage(73, name)); // \f1%0はゲームをしていません。
                return;
            }

            L1BuffUtil.haste(target, 3600 * 1000);
            L1BuffUtil.brave(target, 3600 * 1000);
            L1PolyMorph.doPoly(target, 5641, 7200, L1PolyMorph.MORPH_BY_GM);
            for (int element : allBuffSkill) {
                L1Skills skill = SkillsTable.getInstance().getTemplate(element);
                new L1SkillUse().handleCommands(target, element,
                        target.getId(), target.getX(), target.getY(), null,
                        skill.getBuffDuration() * 1000, L1SkillUse.TYPE_GMBUFF);
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 .allBuff 玩家名称。"));
        }
    }
}
