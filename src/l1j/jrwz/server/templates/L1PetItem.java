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

public class L1PetItem {
    private int _itemId;

    private int _hitModifier;

    private int _damageModifier;

    private int _AddAc;

    private int _addStr;

    private int _addCon;

    private int _addDex;

    private int _addInt;

    private int _addWis;

    private int _addHp;

    private int _addMp;

    private int _addSp;

    private int _addMr;

    public L1PetItem() {
    }

    public int getAddAc() {
        return _AddAc;
    }

    public int getAddCon() {
        return _addCon;
    }

    public int getAddDex() {
        return _addDex;
    }

    public int getAddHp() {
        return _addHp;
    }

    public int getAddInt() {
        return _addInt;
    }

    public int getAddMp() {
        return _addMp;
    }

    public int getAddMr() {
        return _addMr;
    }

    public int getAddSp() {
        return _addSp;
    }

    public int getAddStr() {
        return _addStr;
    }

    public int getAddWis() {
        return _addWis;
    }

    public int getDamageModifier() {
        return _damageModifier;
    }

    public int getHitModifier() {
        return _hitModifier;
    }

    public int getItemId() {
        return _itemId;
    }

    public void setAddAc(int i) {
        _AddAc = i;
    }

    public void setAddCon(int i) {
        _addCon = i;
    }

    public void setAddDex(int i) {
        _addDex = i;
    }

    public void setAddHp(int i) {
        _addHp = i;
    }

    public void setAddInt(int i) {
        _addInt = i;
    }

    public void setAddMp(int i) {
        _addMp = i;
    }

    public void setAddMr(int i) {
        _addMr = i;
    }

    public void setAddSp(int i) {
        _addSp = i;
    }

    public void setAddStr(int i) {
        _addStr = i;
    }

    public void setAddWis(int i) {
        _addWis = i;
    }

    public void setDamageModifier(int i) {
        _damageModifier = i;
    }

    public void setHitModifier(int i) {
        _hitModifier = i;
    }

    public void setItemId(int i) {
        _itemId = i;
    }

}
