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

import java.util.Random;

import l1j.jrwz.configure.Config;

/**
 * 计算统计各属性对角色的影响
 */
public class CalcStat {

    private static Random rnd = new Random();

    /**
     * 追加敏捷对AC的影响
     * 
     * @param level
     * @param dex
     * @return acBonus
     */
    public static int calcAc(final int level, final int dex) {
        int acBonus = 10;
        if (dex <= 9) {
            acBonus -= level / 8;
        } else if ((dex >= 10) && (dex <= 12)) {
            acBonus -= level / 7;
        } else if ((dex >= 13) && (dex <= 15)) {
            acBonus -= level / 6;
        } else if ((dex >= 16) && (dex <= 17)) {
            acBonus -= level / 5;
        } else if (dex >= 18) {
            acBonus -= level / 4;
        }
        return acBonus;
    }

    public static int calcDiffMr(final int wis, final int diff) {
        return calcStatMr(wis + diff) - calcStatMr(wis);
    }

    /**
     * 计算各职业LVUP时的HP上升值
     * 
     * @param charType
     *            职业
     * @param baseMaxHp
     *            目前的最大HP
     * @param baseCon
     *            体质
     * @param originalHpup
     * @return HP上升后的数值
     */
    public static int calcStatHp(final int charType, final int baseMaxHp,
            final int baseCon, final int originalHpup) {
        int randomhp = 0;
        if (baseCon > 15) {
            randomhp = (baseCon - 15);
        }
        if (charType == 0) { // 王族
            randomhp += (11 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.PRINCE_MAX_HP) {
                randomhp = (Config.PRINCE_MAX_HP - baseMaxHp);
            }
        } else if (charType == 1) { // 骑士
            randomhp += (17 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.KNIGHT_MAX_HP) {
                randomhp = (Config.KNIGHT_MAX_HP - baseMaxHp);
            }
        } else if (charType == 2) { // 精灵
            randomhp += (10 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.ELF_MAX_HP) {
                randomhp = (Config.ELF_MAX_HP - baseMaxHp);
            }
        } else if (charType == 3) { // 法师
            randomhp += (7 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.WIZARD_MAX_HP) {
                randomhp = (Config.WIZARD_MAX_HP - baseMaxHp);
            }
        } else if (charType == 4) { // 黑暗精灵
            randomhp += (10 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.DARKELF_MAX_HP) {
                randomhp = (Config.DARKELF_MAX_HP - baseMaxHp);
            }
        } else if (charType == 5) { // 龙骑士
            randomhp += (13 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.DRAGONKNIGHT_MAX_HP) {
                randomhp = (Config.DRAGONKNIGHT_MAX_HP - baseMaxHp);
            }
        } else if (charType == 6) { // 幻术师
            randomhp += (9 + rnd.nextInt(2)); // 初期值分追加

            if (baseMaxHp + randomhp > Config.ILLUSIONIST_MAX_HP) {
                randomhp = (Config.ILLUSIONIST_MAX_HP - baseMaxHp);
            }
        }

        randomhp += originalHpup;

        if (randomhp < 0) {
            randomhp = 0;
        }
        return randomhp;
    }

    /**
     * <b>计算各职业LVUP时的MP上升值</b>
     * 
     * @param charType
     *            职业
     * @param baseMaxMp
     *            目前的最大MP
     * @param baseWis
     *            精神
     * @param originalMpup
     * @return MP上升后的数值
     */
    public static int calcStatMp(final int charType, final int baseMaxMp,
            final short baseWis, final int originalMpup) {
        int randommp = 0;
        int seedY = 0;
        int seedZ = 0;
        if ((baseWis < 9) || ((baseWis > 9) && (baseWis < 12))) {
            seedY = 2;
        } else if ((baseWis == 9) || ((baseWis >= 12) && (baseWis <= 17))) {
            seedY = 3;
        } else if (((baseWis >= 18) && (baseWis <= 23)) || (baseWis == 25)
                || (baseWis == 26) || (baseWis == 29) || (baseWis == 30)
                || (baseWis == 33) || (baseWis == 34)) {
            seedY = 4;
        } else if ((baseWis == 24) || (baseWis == 27) || (baseWis == 28)
                || (baseWis == 31) || (baseWis == 32) || (baseWis >= 35)) {
            seedY = 5;
        }

        if ((baseWis >= 7) && (baseWis <= 9)) {
            seedZ = 0;
        } else if ((baseWis >= 10) && (baseWis <= 14)) {
            seedZ = 1;
        } else if ((baseWis >= 15) && (baseWis <= 20)) {
            seedZ = 2;
        } else if ((baseWis >= 21) && (baseWis <= 24)) {
            seedZ = 3;
        } else if ((baseWis >= 25) && (baseWis <= 28)) {
            seedZ = 4;
        } else if ((baseWis >= 29) && (baseWis <= 32)) {
            seedZ = 5;
        } else if (baseWis >= 33) {
            seedZ = 6;
        }

        randommp = rnd.nextInt(seedY) + 1 + seedZ;

        if (charType == 0) { // 王族
            if (baseMaxMp + randommp > Config.PRINCE_MAX_MP) {
                randommp = Config.PRINCE_MAX_MP - baseMaxMp;
            }
        } else if (charType == 1) { // 骑士
            randommp = (randommp * 2 / 3);
            if (baseMaxMp + randommp > Config.KNIGHT_MAX_MP) {
                randommp = Config.KNIGHT_MAX_MP - baseMaxMp;
            }
        } else if (charType == 2) { // 精灵
            randommp = (int) (randommp * 1.5);

            if (baseMaxMp + randommp > Config.ELF_MAX_MP) {
                randommp = Config.ELF_MAX_MP - baseMaxMp;
            }
        } else if (charType == 3) { // 法师
            randommp *= 2;

            if (baseMaxMp + randommp > Config.WIZARD_MAX_MP) {
                randommp = Config.WIZARD_MAX_MP - baseMaxMp;
            }
        } else if (charType == 4) { // 黑暗精灵
            randommp = (int) (randommp * 1.5);

            if (baseMaxMp + randommp > Config.DARKELF_MAX_MP) {
                randommp = Config.DARKELF_MAX_MP - baseMaxMp;
            }
        } else if (charType == 5) { // 龙骑士
            randommp = (randommp * 2 / 3);

            if (baseMaxMp + randommp > Config.DRAGONKNIGHT_MAX_MP) {
                randommp = Config.DRAGONKNIGHT_MAX_MP - baseMaxMp;
            }
        } else if (charType == 6) { // 幻术师
            randommp = (randommp * 5 / 3);

            if (baseMaxMp + randommp > Config.ILLUSIONIST_MAX_MP) {
                randommp = Config.ILLUSIONIST_MAX_MP - baseMaxMp;
            }
        }

        randommp += originalMpup;

        if (randommp < 0) {
            randommp = 0;
        }
        return randommp;
    }

    /**
     * <b> 追加精神对抗魔的影响 </b>
     * 
     * @param wis
     *            精神点数
     * @return mrBonus 抗魔值
     */
    public static int calcStatMr(final int wis) {
        int mrBonus = 0;
        if (wis <= 14) {
            mrBonus = 0;
        } else if ((wis >= 15) && (wis <= 16)) {
            mrBonus = 3;
        } else if (wis == 17) {
            mrBonus = 6;
        } else if (wis == 18) {
            mrBonus = 10;
        } else if (wis == 19) {
            mrBonus = 15;
        } else if (wis == 20) {
            mrBonus = 21;
        } else if (wis == 21) {
            mrBonus = 28;
        } else if (wis == 22) {
            mrBonus = 37;
        } else if (wis == 23) {
            mrBonus = 47;
        } else if ((wis >= 24) && (wis <= 29)) {
            mrBonus = 50;
        } else if ((wis >= 30) && (wis <= 34)) {
            mrBonus = 52;
        } else if ((wis >= 35) && (wis <= 39)) {
            mrBonus = 55;
        } else if ((wis >= 40) && (wis <= 43)) {
            mrBonus = 59;
        } else if ((wis >= 44) && (wis <= 46)) {
            mrBonus = 62;
        } else if ((wis >= 47) && (wis <= 49)) {
            mrBonus = 64;
        } else if (wis == 50) {
            mrBonus = 65;
        } else {
            mrBonus = 75; // XXX 抗魔值待定
        }
        return mrBonus;
    }

    private CalcStat() {

    }
}
