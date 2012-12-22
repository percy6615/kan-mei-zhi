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
package l1j.jrwz.server.templates;

public class L1Weapon extends L1Item {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int _range = 0; // ● 射程範囲

    private int _hitModifier = 0; // ● 命中率補正

    private int _dmgModifier = 0; // ● ダメージ補正

    private int _doubleDmgChance; // ● DB、クロウの発動確率

    private int _magicDmgModifier = 0; // ● 攻撃魔法のダメージ補正

    private int _canbedmg = 0; // ● 損傷の有無

    public L1Weapon() {
    }

    @Override
    public int get_canbedmg() {
        return _canbedmg;
    }

    @Override
    public int getDmgModifier() {
        return _dmgModifier;
    }

    @Override
    public int getDoubleDmgChance() {
        return _doubleDmgChance;
    }

    @Override
    public int getHitModifier() {
        return _hitModifier;
    }

    @Override
    public int getMagicDmgModifier() {
        return _magicDmgModifier;
    }

    @Override
    public int getRange() {
        return _range;
    }

    @Override
    public boolean isTwohandedWeapon() {
        int weapon_type = getType();

        boolean bool = (weapon_type == 3 || weapon_type == 4
                || weapon_type == 5 || weapon_type == 11 || weapon_type == 12
                || weapon_type == 15 || weapon_type == 16 || weapon_type == 18);

        return bool;
    }

    public void set_canbedmg(int i) {
        _canbedmg = i;
    }

    public void setDmgModifier(int i) {
        _dmgModifier = i;
    }

    public void setDoubleDmgChance(int i) {
        _doubleDmgChance = i;
    }

    public void setHitModifier(int i) {
        _hitModifier = i;
    }

    public void setMagicDmgModifier(int i) {
        _magicDmgModifier = i;
    }

    public void setRange(int i) {
        _range = i;
    }
}
