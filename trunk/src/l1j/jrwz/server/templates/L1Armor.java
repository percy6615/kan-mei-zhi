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

public class L1Armor extends L1Item {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int _ac = 0; // ● ＡＣ

    private int _damageReduction = 0; // ● ダメージ軽減

    private int _weightReduction = 0; // ● 重量軽減

    private int _hitModifierByArmor = 0; // ● 命中率補正

    private int _dmgModifierByArmor = 0; // ● ダメージ補正

    private int _bowHitModifierByArmor = 0; // ● 弓の命中率補正

    private int _bowDmgModifierByArmor = 0; // ● 弓のダメージ補正

    private int _defense_water = 0; // ● 水の属性防御

    private int _defense_wind = 0; // ● 風の属性防御

    private int _defense_fire = 0; // ● 火の属性防御

    private int _defense_earth = 0; // ● 土の属性防御

    private int _regist_stun = 0; // ● スタン耐性

    private int _regist_stone = 0; // ● 石化耐性

    private int _regist_sleep = 0; // ● 睡眠耐性

    private int _regist_freeze = 0; // ● 凍結耐性

    private int _regist_sustain = 0; // ● ホールド耐性

    private int _regist_blind = 0; // ● 暗闇耐性

    public L1Armor() {
    }

    @Override
    public int get_ac() {
        return _ac;
    }

    @Override
    public int get_defense_earth() {
        return this._defense_earth;
    }

    @Override
    public int get_defense_fire() {
        return this._defense_fire;
    }

    @Override
    public int get_defense_water() {
        return this._defense_water;
    }

    @Override
    public int get_defense_wind() {
        return this._defense_wind;
    }

    @Override
    public int get_regist_blind() {
        return this._regist_blind;
    }

    @Override
    public int get_regist_freeze() {
        return this._regist_freeze;
    }

    @Override
    public int get_regist_sleep() {
        return this._regist_sleep;
    }

    @Override
    public int get_regist_stone() {
        return this._regist_stone;
    }

    @Override
    public int get_regist_stun() {
        return this._regist_stun;
    }

    @Override
    public int get_regist_sustain() {
        return this._regist_sustain;
    }

    @Override
    public int getBowDmgModifierByArmor() {
        return _bowDmgModifierByArmor;
    }

    @Override
    public int getBowHitModifierByArmor() {
        return _bowHitModifierByArmor;
    }

    @Override
    public int getDamageReduction() {
        return _damageReduction;
    }

    @Override
    public int getDmgModifierByArmor() {
        return _dmgModifierByArmor;
    }

    @Override
    public int getHitModifierByArmor() {
        return _hitModifierByArmor;
    }

    @Override
    public int getWeightReduction() {
        return _weightReduction;
    }

    public void set_ac(int i) {
        this._ac = i;
    }

    public void set_defense_earth(int i) {
        _defense_earth = i;
    }

    public void set_defense_fire(int i) {
        _defense_fire = i;
    }

    public void set_defense_water(int i) {
        _defense_water = i;
    }

    public void set_defense_wind(int i) {
        _defense_wind = i;
    }

    public void set_regist_blind(int i) {
        _regist_blind = i;
    }

    public void set_regist_freeze(int i) {
        _regist_freeze = i;
    }

    public void set_regist_sleep(int i) {
        _regist_sleep = i;
    }

    public void set_regist_stone(int i) {
        _regist_stone = i;
    }

    public void set_regist_stun(int i) {
        _regist_stun = i;
    }

    public void set_regist_sustain(int i) {
        _regist_sustain = i;
    }

    public void setBowDmgModifierByArmor(int i) {
        _bowDmgModifierByArmor = i;
    }

    public void setBowHitModifierByArmor(int i) {
        _bowHitModifierByArmor = i;
    }

    public void setDamageReduction(int i) {
        _damageReduction = i;
    }

    public void setDmgModifierByArmor(int i) {
        _dmgModifierByArmor = i;
    }

    public void setHitModifierByArmor(int i) {
        _hitModifierByArmor = i;
    }

    public void setWeightReduction(int i) {
        _weightReduction = i;
    }

}
