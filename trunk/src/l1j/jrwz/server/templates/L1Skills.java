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

public class L1Skills {

    public static final int ATTR_NONE = 0;

    public static final int ATTR_EARTH = 1;

    public static final int ATTR_FIRE = 2;

    public static final int ATTR_WATER = 4;

    public static final int ATTR_WIND = 8;

    public static final int ATTR_RAY = 16;

    public static final int TYPE_PROBABILITY = 1;

    public static final int TYPE_CHANGE = 2;

    public static final int TYPE_CURSE = 4;

    public static final int TYPE_DEATH = 8;

    public static final int TYPE_HEAL = 16;

    public static final int TYPE_RESTORE = 32;

    public static final int TYPE_ATTACK = 64;

    public static final int TYPE_OTHER = 128;

    public static final int TARGET_TO_ME = 0;

    public static final int TARGET_TO_PC = 1;

    public static final int TARGET_TO_NPC = 2;

    public static final int TARGET_TO_CLAN = 4;

    public static final int TARGET_TO_PARTY = 8;

    public static final int TARGET_TO_PET = 16;

    public static final int TARGET_TO_PLACE = 32;

    private int _skillId;

    private String _name;

    private int _skillLevel;

    private int _skillNumber;

    private int _mpConsume;

    private int _hpConsume;

    private int _itmeConsumeId;

    private int _itmeConsumeCount;

    private int _reuseDelay; // 単位：ミリ秒

    private int _buffDuration; // 単位：秒

    private String _target;

    private int _targetTo; // 対象 0:自分 1:PC 2:NPC 4:血盟 8:パーティ 16:ペット 32:場所

    private int _damageValue;

    private int _damageDice;

    private int _damageDiceCount;

    private int _probabilityValue;

    private int _probabilityDice;

    private int _attr;

    private int _type; // タイプ

    private int _lawful;

    private int _ranged;

    private int _area;

    boolean _isThrough;

    private int _id;

    private String _nameId;

    private int _actionId;

    private int _castGfx;

    private int _castGfx2;

    private int _sysmsgIdHappen;

    private int _sysmsgIdStop;

    private int _sysmsgIdFail;

    public int getActionId() {
        return _actionId;
    }

    public int getArea() {
        return _area;
    }

    /**
     * スキルの属性を返す。<br>
     * 0.無属性魔法,1.地魔法,2.火魔法,4.水魔法,8.風魔法,16.光魔法
     */
    public int getAttr() {
        return _attr;
    }

    public int getBuffDuration() {
        return _buffDuration;
    }

    public int getCastGfx() {
        return _castGfx;
    }

    public int getCastGfx2() {
        return _castGfx2;
    }

    public int getDamageDice() {
        return _damageDice;
    }

    public int getDamageDiceCount() {
        return _damageDiceCount;
    }

    public int getDamageValue() {
        return _damageValue;
    }

    public int getHpConsume() {
        return _hpConsume;
    }

    public int getId() {
        return _id;
    }

    public int getItemConsumeCount() {
        return _itmeConsumeCount;
    }

    public int getItemConsumeId() {
        return _itmeConsumeId;
    }

    public int getLawful() {
        return _lawful;
    }

    public int getMpConsume() {
        return _mpConsume;
    }

    public String getName() {
        return _name;
    }

    public String getNameId() {
        return _nameId;
    }

    public int getProbabilityDice() {
        return _probabilityDice;
    }

    public int getProbabilityValue() {
        return _probabilityValue;
    }

    public int getRanged() {
        return _ranged;
    }

    public int getReuseDelay() {
        return _reuseDelay;
    }

    public int getSkillId() {
        return _skillId;
    }

    public int getSkillLevel() {
        return _skillLevel;
    }

    public int getSkillNumber() {
        return _skillNumber;
    }

    public int getSysmsgIdFail() {
        return _sysmsgIdFail;
    }

    public int getSysmsgIdHappen() {
        return _sysmsgIdHappen;
    }

    public int getSysmsgIdStop() {
        return _sysmsgIdStop;
    }

    public String getTarget() {
        return _target;
    }

    public int getTargetTo() {
        return _targetTo;
    }

    /**
     * スキルの作用の種類を返す。<br>
     * 1.確率系,2.エンチャント,4.呪い,8.死,16.治療,32.復活,64.攻撃,128.その他特殊
     */
    public int getType() {
        return _type;
    }

    public boolean isThrough() {
        return _isThrough;
    }

    public void setActionId(int i) {
        _actionId = i;
    }

    public void setArea(int i) {
        _area = i;
    }

    public void setAttr(int i) {
        _attr = i;
    }

    public void setBuffDuration(int i) {
        _buffDuration = i;
    }

    public void setCastGfx(int i) {
        _castGfx = i;
    }

    public void setCastGfx2(int i) {
        _castGfx2 = i;
    }

    public void setDamageDice(int i) {
        _damageDice = i;
    }

    public void setDamageDiceCount(int i) {
        _damageDiceCount = i;
    }

    public void setDamageValue(int i) {
        _damageValue = i;
    }

    public void setHpConsume(int i) {
        _hpConsume = i;
    }

    public void setId(int i) {
        _id = i;
    }

    public void setItemConsumeCount(int i) {
        _itmeConsumeCount = i;
    }

    public void setItemConsumeId(int i) {
        _itmeConsumeId = i;
    }

    public void setLawful(int i) {
        _lawful = i;
    }

    public void setMpConsume(int i) {
        _mpConsume = i;
    }

    public void setName(String s) {
        _name = s;
    }

    public void setNameId(String s) {
        _nameId = s;
    }

    public void setProbabilityDice(int i) {
        _probabilityDice = i;
    }

    public void setProbabilityValue(int i) {
        _probabilityValue = i;
    }

    public void setRanged(int i) {
        _ranged = i;
    }

    public void setReuseDelay(int i) {
        _reuseDelay = i;
    }

    public void setSkillId(int i) {
        _skillId = i;
    }

    public void setSkillLevel(int i) {
        _skillLevel = i;
    }

    public void setSkillNumber(int i) {
        _skillNumber = i;
    }

    public void setSysmsgIdFail(int i) {
        _sysmsgIdFail = i;
    }

    public void setSysmsgIdHappen(int i) {
        _sysmsgIdHappen = i;
    }

    public void setSysmsgIdStop(int i) {
        _sysmsgIdStop = i;
    }

    public void setTarget(String s) {
        _target = s;
    }

    public void setTargetTo(int i) {
        _targetTo = i;
    }

    public void setThrough(boolean flag) {
        _isThrough = flag;
    }

    public void setType(int i) {
        _type = i;
    }

}
