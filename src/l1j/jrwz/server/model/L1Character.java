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

package l1j.jrwz.server.model;

import static l1j.jrwz.server.model.skill.L1SkillId.BLIND_HIDING;
import static l1j.jrwz.server.model.skill.L1SkillId.GMSTATUS_FINDINVIS;
import static l1j.jrwz.server.model.skill.L1SkillId.INVISIBILITY;
import static l1j.jrwz.server.model.skill.L1SkillId.LIGHT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import l1j.jrwz.server.model.Instance.L1DollInstance;
import l1j.jrwz.server.model.Instance.L1FollowerInstance;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.map.L1Map;
import l1j.jrwz.server.model.poison.L1Poison;
import l1j.jrwz.server.model.skill.L1SkillTimer;
import l1j.jrwz.server.model.skill.L1SkillTimerCreator;
import l1j.jrwz.server.serverpackets.S_Light;
import l1j.jrwz.server.serverpackets.S_Poison;
import l1j.jrwz.server.serverpackets.S_RemoveObject;
import l1j.jrwz.server.serverpackets.ServerBasePacket;
import l1j.jrwz.server.types.Point;
import l1j.jrwz.server.utils.IntRange;

// Referenced classes of package l1j.jrwz.server.model:
// L1Object, Die, L1PcInstance, L1MonsterInstance,
// L1World, ActionFailed
/**
 * 人物.
 */
public class L1Character extends L1Object {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(L1Character.class
            .getName());

    private L1Poison _poison = null;
    private boolean _paralyzed;
    private boolean _sleeped;

    private final Map<Integer, L1NpcInstance> _petlist = new HashMap<Integer, L1NpcInstance>();
    private final Map<Integer, L1DollInstance> _dolllist = new HashMap<Integer, L1DollInstance>();
    private final Map<Integer, L1SkillTimer> _skillEffect = new HashMap<Integer, L1SkillTimer>();
    private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<Integer, L1ItemDelay.ItemDelayTimer>();
    private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap<Integer, L1FollowerInstance>();

    private int _currentHp;

    private int _currentMp;

    L1Paralysis _paralysis;

    private boolean _isSkillDelay = false;
    /** ● 经验值. */
    private int _exp;

    // ■■■■■■■■■■ L1PcInstanceへ移動するプロパティ ■■■■■■■■■■
    private final List<L1Object> _knownObjects = new CopyOnWriteArrayList<L1Object>();

    private final List<L1PcInstance> _knownPlayer = new CopyOnWriteArrayList<L1PcInstance>();
    /** ● 名称. */
    private String _name;
    /** ● 等级. */
    private int _level;
    /** ● 最高血量（1～32767）. */
    private int _maxHp = 0;
    /** ● 真实的最高血量. */
    private int _trueMaxHp = 0;
    /** ● 最高魔量（0～32767）. */
    private int _maxMp = 0;
    /** ● 真实的最高魔量. */
    private int _trueMaxMp = 0;
    /** ● 物理防御（-128～127）. */
    private int _ac = 0;
    /** ● 真实的物理防御. */
    private int _trueAc = 0;
    /** ● 力量值（1～127）. */
    private byte _str = 0;
    /** ● 真实的力量值. */
    private int _trueStr = 0;
    /** ● 体质值（1～127）. */
    private byte _con = 0;
    /** ● 真实的体质值. */
    private int _trueCon = 0;
    /** ● 敏捷值（1～127）. */
    private byte _dex = 0;
    /** ● 真实的敏捷值. */
    private int _trueDex = 0;
    /** ● 魅力值（1～127）. */
    private byte _cha = 0;
    /** ● 真实的魅力值. */
    private int _trueCha = 0;
    /** ● 智力值（1～127）. */
    private byte _int = 0;
    /** ● 真实的智力值. */
    private int _trueInt = 0;
    /** ● 精神值（1～127）. */
    private byte _wis = 0;
    /** ● 真实的精神值. */
    private int _trueWis = 0;
    /** ● 风属性防御（-128～127）. */
    private int _wind = 0;
    /** ● 真实的风属性防御. */
    private int _trueWind = 0;
    /** ● 水属性防御（-128～127）. */
    private int _water = 0;
    /** ● 真实的水属性防御. */
    private int _trueWater = 0;
    /** ● 火属性防御（-128～127）. */
    private int _fire = 0;
    /** ● 真实的火属性防御. */
    private int _trueFire = 0;
    /** ● 地属性防御（-128～127）. */
    private int _earth = 0;
    /** ● 真实的地属性防御. */
    private int _trueEarth = 0;
    /** 增加属性种类. */
    private int _addAttrKind;
    /** 昏迷耐性. */
    private int _registStun = 0;
    /** 真实的昏迷耐性. */
    private int _trueRegistStun = 0;
    /** 石化耐性. */
    private int _registStone = 0;
    /** 真实的石化耐性. */
    private int _trueRegistStone = 0;
    /** 睡眠耐性. */
    private int _registSleep = 0;
    /** 真实的睡眠耐性. */
    private int _trueRegistSleep = 0;
    /** 冻结耐性. */
    private int _registFreeze = 0;
    /** 真实的冻结耐性. */
    private int _trueRegistFreeze = 0;
    /** 支撑耐性. */
    private int _registSustain = 0;
    /** 真实的支撑耐性. */
    private int _trueRegistSustain = 0;
    /** 暗闇耐性. */
    private int _registBlind = 0;
    /** 真实的暗闇耐性. */
    private int _trueRegistBlind = 0;
    /** ● 伤害校正（-128～127）. */
    private int _dmgup = 0;
    /** ● 真实的伤害校正. */
    private int _trueDmgup = 0;
    /** ● 弓伤害校正（-128～127）. */
    private int _bowDmgup = 0;
    /** ● 真实的弓伤害校正. */
    private int _trueBowDmgup = 0;
    /** ● 命中校正（-128～127）. */
    private int _hitup = 0;
    /** ● 真实的命中校正. */
    private int _trueHitup = 0;
    /** ● 弓命中校正（-128～127）. */
    private int _bowHitup = 0;
    /** ● 真实的弓命中校正. */
    private int _trueBowHitup = 0;
    /** ● 魔法防御（0～）. */
    private int _mr = 0;
    /** ● 真实的魔法防御. */
    private int _trueMr = 0;
    /** ● 魔法攻击. */
    private int _sp = 0;
    /** ● 是否死亡状态. */
    private boolean _isDead;
    /** ● 状态. */
    private int _status;
    /** ● 封号. */
    private String _title;
    /** ● 正义值. */
    private int _lawful;
    /** ● 面向 0.左上 1.上 2.右上 3.右 4.右下 5.下 6.左下 7.左. */
    private int _heading;
    /** ● 移动速度 0.正常 1.加速 2.缓速. */
    private int _moveSpeed;
    /** ● 勇敢状态 0.正常 1.勇敢. */
    private int _braveSpeed;

    // ■■■■■■■■■■ 属性 ■■■■■■■■■■
    /** ● 临时角色图形ＩＤ. */
    private int _tempCharGfx;
    /** ● 图形ＩＤ. */
    private int _gfxid;
    /** 友好度. */
    private int _karma;
    /** ● 光照范围. */
    private int _chaLightSize;
    /** ● 光照范围(S_OwnCharPack用). */
    private int _ownLightSize;

    public L1Character() {
        _level = 1;
    }

    /**
     * 增加冻结耐性.
     * 
     * @param i
     *            - 冻结耐性
     */
    public void add_regist_freeze(int i) {
        _trueRegistFreeze += i;
        if (_trueRegistFreeze > 127) {
            _registFreeze = 127;
        } else if (_trueRegistFreeze < -128) {
            _registFreeze = -128;
        } else {
            _registFreeze = _trueRegistFreeze;
        }
    }

    public void addAc(int i) {
        setAc(_trueAc + i);
    }

    public void addBowDmgup(int i) {
        _trueBowDmgup += i;
        if (_trueBowDmgup >= 127) {
            _bowDmgup = 127;
        } else if (_trueBowDmgup <= -128) {
            _bowDmgup = -128;
        } else {
            _bowDmgup = _trueBowDmgup;
        }
    }

    public void addBowHitup(int i) {
        _trueBowHitup += i;
        if (_trueBowHitup >= 127) {
            _bowHitup = 127;
        } else if (_trueBowHitup <= -128) {
            _bowHitup = -128;
        } else {
            _bowHitup = _trueBowHitup;
        }
    }

    public void addCha(int i) {
        setCha(_trueCha + i);
    }

    public void addCon(int i) {
        setCon(_trueCon + i);
    }

    public void addDex(int i) {
        setDex(_trueDex + i);
    }

    public void addDmgup(int i) {
        _trueDmgup += i;
        if (_trueDmgup >= 127) {
            _dmgup = 127;
        } else if (_trueDmgup <= -128) {
            _dmgup = -128;
        } else {
            _dmgup = _trueDmgup;
        }
    }

    /**
     * 增加魔法娃娃.
     * 
     * @param doll
     *            - 要增加的娃娃、L1DollInstance对象。
     */
    public void addDoll(L1DollInstance doll) {
        _dolllist.put(doll.getId(), doll);
    }

    public void addEarth(int i) {
        _trueEarth += i;
        if (_trueEarth >= 127) {
            _earth = 127;
        } else if (_trueEarth <= -128) {
            _earth = -128;
        } else {
            _earth = _trueEarth;
        }
    }

    public void addFire(int i) {
        _trueFire += i;
        if (_trueFire >= 127) {
            _fire = 127;
        } else if (_trueFire <= -128) {
            _fire = -128;
        } else {
            _fire = _trueFire;
        }
    }

    /**
     * 增加跟随者.
     * 
     * @param follower
     *            - 要增加的跟随者、L1FollowerInstance对象。
     */
    public void addFollower(L1FollowerInstance follower) {
        _followerlist.put(follower.getId(), follower);
    }

    public void addHitup(int i) {
        _trueHitup += i;
        if (_trueHitup >= 127) {
            _hitup = 127;
        } else if (_trueHitup <= -128) {
            _hitup = -128;
        } else {
            _hitup = _trueHitup;
        }
    }

    public void addInt(int i) {
        setInt(_trueInt + i);
    }

    /**
     * 增加道具延迟.
     * 
     * @param delayId
     *            - 道具的延迟ID。 正常为0、隐身斗篷、炎魔的血光斗篷为1。
     * @param timer
     *            - 道具的延迟时间、L1ItemDelay.ItemDelayTimer对象。
     */
    public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) {
        _itemdelay.put(delayId, timer);
    }

    /**
     * 增加认识新的对象.
     * 
     * @param obj
     *            - 要认识的新对象
     */
    public void addKnownObject(L1Object obj) {
        if (!_knownObjects.contains(obj)) {
            _knownObjects.add(obj);
            if (obj instanceof L1PcInstance) {
                _knownPlayer.add((L1PcInstance) obj);
            }
        }
    }

    public synchronized void addLawful(int i) {
        _lawful += i;
        if (_lawful > 32767) {
            _lawful = 32767;
        } else if (_lawful < -32768) {
            _lawful = -32768;
        }
    }

    public void addMaxHp(int i) {
        setMaxHp(_trueMaxHp + i);
    }

    public void addMaxMp(int i) {
        setMaxMp(_trueMaxMp + i);
    }

    public void addMr(int i) {
        _trueMr += i;
        if (_trueMr <= 0) {
            _mr = 0;
        } else {
            _mr = _trueMr;
        }
    }

    /**
     * 增加宠物(包括召唤、造尸等)
     * 
     * @param npc
     *            - 要增加的Npc、L1NpcInstance对象。
     */
    public void addPet(L1NpcInstance npc) {
        _petlist.put(npc.getId(), npc);
    }

    public void addRegistBlind(int i) {
        _trueRegistBlind += i;
        if (_trueRegistBlind > 127) {
            _registBlind = 127;
        } else if (_trueRegistBlind < -128) {
            _registBlind = -128;
        } else {
            _registBlind = _trueRegistBlind;
        }
    }

    public void addRegistSleep(int i) {
        _trueRegistSleep += i;
        if (_trueRegistSleep > 127) {
            _registSleep = 127;
        } else if (_trueRegistSleep < -128) {
            _registSleep = -128;
        } else {
            _registSleep = _trueRegistSleep;
        }
    }

    public void addRegistStone(int i) {
        _trueRegistStone += i;
        if (_trueRegistStone > 127) {
            _registStone = 127;
        } else if (_trueRegistStone < -128) {
            _registStone = -128;
        } else {
            _registStone = _trueRegistStone;
        }
    }

    public void addRegistStun(int i) {
        _trueRegistStun += i;
        if (_trueRegistStun > 127) {
            _registStun = 127;
        } else if (_trueRegistStun < -128) {
            _registStun = -128;
        } else {
            _registStun = _trueRegistStun;
        }
    }

    public void addRegistSustain(int i) {
        _trueRegistSustain += i;
        if (_trueRegistSustain > 127) {
            _registSustain = 127;
        } else if (_trueRegistSustain < -128) {
            _registSustain = -128;
        } else {
            _registSustain = _trueRegistSustain;
        }
    }

    /**
     * 增加技能效果.
     * 
     * @param skillId
     *            - 要增加的技能ID。
     * @param timeMillis
     *            效果的持续时间。无限制为0。
     */
    private void addSkillEffect(int skillId, int timeMillis) {
        L1SkillTimer timer = null;
        if (0 < timeMillis) {
            timer = L1SkillTimerCreator.create(this, skillId, timeMillis);
            timer.begin();
        }
        _skillEffect.put(skillId, timer);
    }

    public void addSp(int i) {
        _sp += i;
    }

    public void addStr(int i) {
        setStr(_trueStr + i);
    }

    public void addWater(int i) {
        _trueWater += i;
        if (_trueWater >= 127) {
            _water = 127;
        } else if (_trueWater <= -128) {
            _water = -128;
        } else {
            _water = _trueWater;
        }
    }

    public void addWind(int i) {
        _trueWind += i;
        if (_trueWind >= 127) {
            _wind = 127;
        } else if (_trueWind <= -128) {
            _wind = -128;
        } else {
            _wind = _trueWind;
        }
    }

    public void addWis(int i) {
        setWis(_trueWis + i);
    }

    /**
     * 向角色的可见范围内发送封包.
     * 
     * @param packet
     *            - 要发送的封包、ServerBasePacket对象。
     */
    public void broadcastPacket(ServerBasePacket packet) {
        for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
            pc.sendPackets(packet);
        }
    }

    /**
     * キャラクターの可視範囲に居るプレイヤーへ、パケットを送信する。 ただしターゲットの画面内には送信しない。
     * 
     * @param packet
     *            送信するパケットを表すServerBasePacketオブジェクト。
     */
    public void broadcastPacketExceptTargetSight(ServerBasePacket packet,
            L1Character target) {
        for (L1PcInstance pc : L1World.getInstance()
                .getVisiblePlayerExceptTargetSight(this, target)) {
            pc.sendPackets(packet);
        }
    }

    /**
     * キャラクターの可視範囲でインビジを見破れるor見破れないプレイヤーを区別して、パケットを送信する。
     * 
     * @param packet
     *            送信するパケットを表すServerBasePacketオブジェクト。
     * @param isFindInvis
     *            true : 見破れるプレイヤーにだけパケットを送信する。 false : 見破れないプレイヤーにだけパケットを送信する。
     */
    public void broadcastPacketForFindInvis(ServerBasePacket packet,
            boolean isFindInvis) {
        for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
            if (isFindInvis) {
                if (pc.hasSkillEffect(GMSTATUS_FINDINVIS)) {
                    pc.sendPackets(packet);
                }
            } else {
                if (!pc.hasSkillEffect(GMSTATUS_FINDINVIS)) {
                    pc.sendPackets(packet);
                }
            }
        }
    }

    /**
     * 从角色、删除所有技能效果计时器。技能的效果不会被删除。
     */
    public void clearSkillEffectTimer() {
        for (L1SkillTimer timer : _skillEffect.values()) {
            if (timer != null) {
                timer.kill();
            }
        }
        _skillEffect.clear();
    }

    /**
     * 解除麻痹.
     */
    public void cureParalaysis() {
        if (_paralysis != null) {
            _paralysis.cure();
        }
    }

    /**
     * 解毒.
     */
    public void curePoison() {
        if (_poison == null) {
            return;
        }
        _poison.cure();
    }

    public int getAc() {
        return _ac;
    }

    public int getAddAttrKind() {
        return _addAttrKind;
    }

    public int getBowDmgup() {
        return _bowDmgup;
    } // 当他被使用

    public int getBowHitup() {
        return _bowHitup;
    } // 当他被使用

    public int getBraveSpeed() {
        return _braveSpeed;
    }

    public byte getCha() {
        return _cha;
    }

    public int getChaLightSize() {
        if (isInvisble()) {
            return 0;
        }
        return _chaLightSize;
    }

    public byte getCon() {
        return _con;
    }

    /**
     * 取得当前的HP.
     * 
     * @return 当前的HP
     */
    public int getCurrentHp() {
        return _currentHp;
    }

    /**
     * 取得当前的MP.
     * 
     * @return 当前的MP
     */
    public int getCurrentMp() {
        return _currentMp;
    }

    public byte getDex() {
        return _dex;
    }

    public int getDmgup() {
        return _dmgup;
    } // 当他被使用

    /**
     * 取得角色的魔法娃娃列表.
     * 
     * @return 魔法娃娃列表、HashMap对象。这个对象的Key对象ID、Value的L1DollInstance。
     */
    public Map<Integer, L1DollInstance> getDollList() {
        return _dolllist;
    }

    public int getEarth() {
        return _earth;
    } // 当他被使用

    /**
     * 取得角色的经验值.
     * 
     * @return 经验值。
     */
    public int getExp() {
        return _exp;
    }

    public int getFire() {
        return _fire;
    } // 当他被使用

    /**
     * 取得跟随者列表.
     * 
     * @return 跟随者列表、HashMap对象。这个对象的Key对象ID、Value的L1FollowerInstance。
     */
    public Map<Integer, L1FollowerInstance> getFollowerList() {
        return _followerlist;
    }

    /**
     * 取得角色的正面坐标.
     * 
     * @return 前面的坐标
     */
    public int[] getFrontLoc() {
        int[] loc = new int[2];
        int x = getX();
        int y = getY();
        int heading = getHeading();
        if (heading == 0) {
            y--;
        } else if (heading == 1) {
            x++;
            y--;
        } else if (heading == 2) {
            x++;
        } else if (heading == 3) {
            x++;
            y++;
        } else if (heading == 4) {
            y++;
        } else if (heading == 5) {
            x--;
            y++;
        } else if (heading == 6) {
            x--;
        } else if (heading == 7) {
            x--;
            y--;
        }
        loc[0] = x;
        loc[1] = y;
        return loc;
    }

    public int getGfxId() {
        return _gfxid;
    }

    public int getHeading() {
        return _heading;
    }

    public int getHitup() {
        return _hitup;
    } // 当它被使用

    public byte getInt() {
        return _int;
    }

    /**
     * 取得角色的背包内道具.
     * 
     * @return 角色的背包内道具、L1Inventory物件。
     */
    public L1Inventory getInventory() {
        return null;
    }

    /**
     * 取得角色的道具延迟计时器、L1ItemDelay.ItemDelayTimer
     * 
     * @param delayId
     *            - 要检查的道具的延迟ID。 正常为0、隐身斗篷、炎魔的血光斗篷为1。
     * @return 道具的延迟时间ID、L1ItemDelay.ItemDelayTimer。
     */
    public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) {
        return _itemdelay.get(delayId);
    }

    /**
     * 取得角色的友好度.
     * 
     * @return 友好度。
     */
    public int getKarma() {
        return _karma;
    }

    /**
     * 取得所认识的全部对象.
     * 
     * @return 认识的全部对象、L1Object被储存在ArrayList。
     */
    public List<L1Object> getKnownObjects() {
        return _knownObjects;
    }

    /**
     * 取得所认识的全部角色.
     * 
     * @return 认识的全部角色、L1PcInstance被储存在ArrayList。
     */
    public List<L1PcInstance> getKnownPlayers() {
        return _knownPlayer;
    }

    public int getLawful() {
        return _lawful;
    }

    public synchronized int getLevel() {
        return _level;
    }

    public int getMagicBonus() {
        int i = getInt();
        if (i <= 5) {
            return -2;
        } else if (i <= 8) {
            return -1;
        } else if (i <= 11) {
            return 0;
        } else if (i <= 14) {
            return 1;
        } else if (i <= 17) {
            return 2;
        } else if (i <= 24) {
            return i - 15;
        } else if (i <= 35) {
            return 10;
        } else if (i <= 42) {
            return 11;
        } else if (i <= 49) {
            return 12;
        } else if (i <= 50) {
            return 13;
        } else {
            return i - 25;
        }
    }

    public int getMagicLevel() {
        return getLevel() / 4;
    }

    public int getMaxHp() {
        return _maxHp;
    }

    public int getMaxMp() {
        return _maxMp;
    }

    public int getMoveSpeed() {
        return _moveSpeed;
    }

    public int getMr() {
        if (hasSkillEffect(153) == true) {
            return _mr / 4;
        } else {
            return _mr;
        }
    } // 当它被使用

    public String getName() {
        return _name;
    }

    public int getOwnLightSize() {
        return _ownLightSize;
    }

    public L1Paralysis getParalysis() {
        return _paralysis;
    }

    /**
     * 取得角色的宠物列表.
     * 
     * @return 集合内角色的宠物列表、HashMap对象。这个对象的Key对象ID、Value的L1NpcInstance。
     */
    public Map<Integer, L1NpcInstance> getPetList() {
        return _petlist;
    }

    /**
     * 取得角色的中毒状态.
     * 
     * @return 角色的中毒状态、L1Poison物件
     */
    public L1Poison getPoison() {
        return _poison;
    }

    public int getRegistBlind() {
        return _registBlind;
    } // 当它被使用

    public int getRegistFreeze() {
        return _registFreeze;
    } // 当它被使用

    public int getRegistSleep() {
        return _registSleep;
    } // 当它被使用

    public int getRegistStone() {
        return _registStone;
    } // 当它被使用

    public int getRegistStun() {
        return _registStun;
    } // 当它被使用

    public int getRegistSustain() {
        return _registSustain;
    } // 当它被使用

    /**
     * 取得角色的技能效果持续时间.
     * 
     * @param skillId
     *            - 要检查的效果的技能ID
     * @return 技能效果剩余时间(秒)。技能效果持续时间无限则为-1。
     */
    public int getSkillEffectTimeSec(int skillId) {
        L1SkillTimer timer = _skillEffect.get(skillId);
        if (timer == null) {
            return -1;
        }
        return timer.getRemainingTime();
    }

    public int getSp() {
        return getTrueSp() + _sp;
    }

    public int getStatus() {
        return _status;
    }

    public byte getStr() {
        return _str;
    }

    public int getTempCharGfx() {
        return _tempCharGfx;
    }

    public String getTitle() {
        return _title;
    }

    public int getTrueMr() {
        return _trueMr;
    } // 当它被设置

    public int getTrueSp() {
        return getMagicLevel() + getMagicBonus();
    }

    public int getWater() {
        return _water;
    } // 当它被使用

    public int getWind() {
        return _wind;
    } // 当它被使用

    public byte getWis() {
        return _wis;
    }

    /**
     * 取得角色所在的区域(战斗、一般、安全).
     * 
     * @return 1：安全区域、-1：战斗区域、0：一般区域
     */
    public int getZoneType() {
        if (getMap().isSafetyZone(getLocation())) {
            return 1;
        } else if (getMap().isCombatZone(getLocation())) {
            return -1;
        } else { // 一般区域
            return 0;
        }
    }

    /**
     * 指定的目标坐标直线距离上是否有障碍物.
     * 
     * @param tx
     *            - 目标的X坐标
     * @param ty
     *            - 目标的Y坐标
     * @return True：有、False：无
     */
    public boolean glanceCheck(int tx, int ty) {
        L1Map map = getMap();
        int chx = getX();
        int chy = getY();
        int arw = 0;
        for (int i = 0; i < 15; i++) {
            if ((chx == tx && chy == ty) || (chx + 1 == tx && chy - 1 == ty)
                    || (chx + 1 == tx && chy == ty)
                    || (chx + 1 == tx && chy + 1 == ty)
                    || (chx == tx && chy + 1 == ty)
                    || (chx - 1 == tx && chy + 1 == ty)
                    || (chx - 1 == tx && chy == ty)
                    || (chx - 1 == tx && chy - 1 == ty)
                    || (chx == tx && chy - 1 == ty)) {
                break;

            } else if (chx < tx && chy == ty) {
                // if (!map.isArrowPassable(chx, chy, 2)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chx++;
            } else if (chx == tx && chy < ty) {
                // if (!map.isArrowPassable(chx, chy, 4)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chy++;
            } else if (chx > tx && chy == ty) {
                // if (!map.isArrowPassable(chx, chy, 6)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chx--;
            } else if (chx == tx && chy > ty) {
                // if (!map.isArrowPassable(chx, chy, 0)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chy--;
            } else if (chx < tx && chy > ty) {
                // if (!map.isArrowPassable(chx, chy, 1)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chx++;
                chy--;
            } else if (chx < tx && chy < ty) {
                // if (!map.isArrowPassable(chx, chy, 3)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chx++;
                chy++;
            } else if (chx > tx && chy < ty) {
                // if (!map.isArrowPassable(chx, chy, 5)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chx--;
                chy++;
            } else if (chx > tx && chy > ty) {
                // if (!map.isArrowPassable(chx, chy, 7)) {
                if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                    return false;
                }
                chx--;
                chy--;
            }
        }
        if (arw == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查角色是否有特定的道具延迟.
     * 
     * @param delayId
     *            - 要检查的延迟ID。 正常为0、隐身斗篷、炎魔的血光斗篷为1。
     * @return True：是、False：否
     */
    public boolean hasItemDelay(int delayId) {
        return _itemdelay.containsKey(delayId);
    }

    /**
     * 检查角色是否有特定的技能效果.
     * 
     * @param skillId
     *            - 要检查的技能ID
     * @return True：是、False：否
     */
    public boolean hasSkillEffect(int skillId) {
        return _skillEffect.containsKey(skillId);
    }

    public void healHp(int pt) {
        setCurrentHp(getCurrentHp() + pt);
    }

    /**
     * 指定的坐标是否能攻击.
     * 
     * @param x
     *            - X坐标
     * @param y
     *            - Y坐标
     * @param range
     *            - 攻击范围(タイル数)
     * @return True：可以、False：不能
     */
    public boolean isAttackPosition(int x, int y, int range) {
        if (range >= 7) { // 远距离武器(７格以上的情况 考虑可能出屏幕对角线)
            if (getLocation().getTileDistance(new Point(x, y)) > range) {
                return false;
            }
        } else { // 近距离武器
            if (getLocation().getTileLineDistance(new Point(x, y)) > range) {
                return false;
            }
        }
        return glanceCheck(x, y);
    }

    /**
     * 是否死亡.
     * 
     * @return True：是、False：否
     */
    public boolean isDead() {
        return _isDead;
    }

    /**
     * 是否隐身.
     * 
     * @return True：是、False：否
     */
    public boolean isInvisble() {
        return (hasSkillEffect(INVISIBILITY) || hasSkillEffect(BLIND_HIDING));
    }

    /**
     * 是否为麻痹状态.
     * 
     * @return True：是、False：否
     */
    public boolean isParalyzed() {
        return _paralyzed;
    }

    /**
     * 是否在技能延迟中.
     * 
     * @return True：是、False：否
     */
    public boolean isSkillDelay() {
        return _isSkillDelay;
    }

    /**
     * 是否睡眠状态.
     * 
     * @return True：是、False：否
     */
    public boolean isSleeped() {
        return _sleeped;
    }

    /**
     * 从角色、删除技能效果计时器。 技能效果不会被删除。
     * 
     * @param skillId
     *            - 要删除的技能ＩＤ
     */
    public void killSkillEffectTimer(int skillId) {
        L1SkillTimer timer = _skillEffect.remove(skillId);
        if (timer != null) {
            timer.kill();
        }
    }

    /**
     * 指定的对象是否已被角色识别.
     * 
     * @param obj
     *            - 对象
     * @return True：认识、False：不认识。 对象为自己，则返回False
     */
    public boolean knownsObject(L1Object obj) {
        return _knownObjects.contains(obj);
    }

    /**
     * 从角色、删除所有认识的对象.
     */
    public void removeAllKnownObjects() {
        _knownObjects.clear();
        _knownPlayer.clear();
    }

    /**
     * 从角色删除魔法娃娃.
     * 
     * @param doll
     *            - 要删除的娃娃、L1DollInstance物件
     */
    public void removeDoll(L1DollInstance doll) {
        _dolllist.remove(doll.getId());
    }

    /**
     * 从角色删除跟随者.
     * 
     * @param follower
     *            - 要删除的跟随者、L1FollowerInstance物件
     */
    public void removeFollower(L1FollowerInstance follower) {
        _followerlist.remove(follower.getId());
    }

    /**
     * 从角色、删除道具延迟.
     * 
     * @param delayId
     *            道具延迟ID。 如果是正常的：0、隐身斗篷、炎魔的血光斗篷：1。
     */
    public void removeItemDelay(int delayId) {
        _itemdelay.remove(delayId);
    }

    /**
     * 从角色、删除已经认识的对象.
     * 
     * @param obj
     *            - 要删除的对象
     */
    public void removeKnownObject(L1Object obj) {
        _knownObjects.remove(obj);
        if (obj instanceof L1PcInstance) {
            _knownPlayer.remove(obj);
        }
    }

    /**
     * 从角色删除、宠物、召唤怪物、驯服的怪物、创建的僵尸.
     * 
     * @param npc
     *            - 要删除的Npc、L1NpcInstance物件。
     */
    public void removePet(L1NpcInstance npc) {
        _petlist.remove(npc.getId());
    }

    /**
     * 从角色、删除技能效果.
     * 
     * @param skillId
     *            - 要删除的技能ID
     */
    public void removeSkillEffect(int skillId) {
        L1SkillTimer timer = _skillEffect.remove(skillId);
        if (timer != null) {
            timer.end();
        }
    }

    /**
     * 角色的复活.
     * 
     * @param hp
     *            - 复活后的HP
     */
    public void resurrect(int hp) {
        if (!isDead()) {
            return;
        }
        if (hp <= 0) {
            hp = 1;
        }
        setCurrentHp(hp);
        setDead(false);
        setStatus(0);
        L1PolyMorph.undoPoly(this);
        for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
            pc.sendPackets(new S_RemoveObject(this));
            pc.removeKnownObject(this);
            pc.updateObject();
        }
    }

    public void setAc(int i) {
        _trueAc = i;
        _ac = IntRange.ensure(i, -128, 127);
    }

    public void setAddAttrKind(int i) {
        _addAttrKind = i;
    }

    public void setBraveSpeed(int i) {
        _braveSpeed = i;
    }

    public void setCha(int i) {
        _trueCha = i;
        _cha = (byte) IntRange.ensure(i, 1, 127);
    }

    public void setChaLightSize(int i) {
        _chaLightSize = i;
    }

    public void setCon(int i) {
        _trueCon = i;
        _con = (byte) IntRange.ensure(i, 1, 127);
    }

    /**
     * 设置人物当前的HP.
     * 
     * @param i
     *            - 人物的新HP
     */
    // 特殊な処理がある場合はこっちをオーバライド（パケット送信等）
    public void setCurrentHp(int i) {
        _currentHp = i;
        if (_currentHp >= getMaxHp()) {
            _currentHp = getMaxHp();
        }
    }

    /**
     * 设置人物当前的HP.
     * 
     * @param i
     *            - 人物的新HP
     */
    public void setCurrentHpDirect(int i) {
        _currentHp = i;
    }

    /**
     * 设置人物当前的MP.
     * 
     * @param i
     *            - 人物的新MP
     */
    // 特殊な処理がある場合はこっちをオーバライド（パケット送信等）
    public void setCurrentMp(int i) {
        _currentMp = i;
        if (_currentMp >= getMaxMp()) {
            _currentMp = getMaxMp();
        }
    }

    /**
     * 设置人物当前的MP.
     * 
     * @param i
     *            - 人物的新MP
     */
    public void setCurrentMpDirect(int i) {
        _currentMp = i;
    }

    public void setDead(boolean flag) {
        _isDead = flag;
    }

    public void setDex(int i) {
        _trueDex = i;
        _dex = (byte) IntRange.ensure(i, 1, 127);
    }

    /**
     * 设置角色保存的经验值.
     * 
     * @param exp
     *            - 经验值
     */
    public void setExp(int exp) {
        _exp = exp;
    }

    public void setGfxId(int i) {
        _gfxid = i;
    }

    public void setHeading(int i) {
        _heading = i;
    }

    public void setInt(int i) {
        _trueInt = i;
        _int = (byte) IntRange.ensure(i, 1, 127);
    }

    /**
     * 设置角色保存的友好度.
     * 
     * @param karma
     *            - 友好度
     */
    public void setKarma(int karma) {
        _karma = karma;
    }

    public void setLawful(int i) {
        _lawful = i;
    }

    public synchronized void setLevel(long level) {
        _level = (int) level;
    }

    public void setMaxHp(int hp) {
        _trueMaxHp = hp;
        _maxHp = IntRange.ensure(_trueMaxHp, 1, 32767);
        _currentHp = Math.min(_currentHp, _maxHp);
    }

    public void setMaxMp(int mp) {
        _trueMaxMp = mp;
        _maxMp = IntRange.ensure(_trueMaxMp, 0, 32767);
        _currentMp = Math.min(_currentMp, _maxMp);
    }

    public void setMoveSpeed(int i) {
        _moveSpeed = i;
    }

    public void setMr(int i) {
        _trueMr = i;
        if (_trueMr <= 0) {
            _mr = 0;
        } else {
            _mr = _trueMr;
        }
    }

    public void setName(String s) {
        _name = s;
    }

    public void setOwnLightSize(int i) {
        _ownLightSize = i;
    }

    public void setParalaysis(L1Paralysis p) {
        _paralysis = p;
    }

    /**
     * 设置角色的麻痹状态.
     * 
     * @param flag
     *            - True or False
     */
    public void setParalyzed(boolean flag) {
        _paralyzed = flag;
    }

    /**
     * 设置角色的中毒.
     * 
     * @param poison
     *            - 代表毒、L1Poison物件
     */
    public void setPoison(L1Poison poison) {
        _poison = poison;
    }

    /**
     * 设置角色的中毒效果.
     * 
     * @param effectId
     *            - 效果ID
     * @see S_Poison#S_Poison(int, int)
     */
    public void setPoisonEffect(int effectId) {
        broadcastPacket(new S_Poison(getId(), effectId));
    }

    /**
     * 设置角色的技能延迟.
     * 
     * @param flag
     *            - True or False
     */
    public void setSkillDelay(boolean flag) {
        _isSkillDelay = flag;
    }

    /**
     * 设置角色的技能效果.<br>
     * 如果技能没有重复、则增加一个新的技能效果。<br>
     * 如果技能重复、则优先用持续时间较长的。
     * 
     * @param skillId
     *            - 设置效果的技能ID
     * @param timeMillis
     *            - 设置效果的持续时间。无限制为0
     */
    public void setSkillEffect(int skillId, int timeMillis) {
        if (hasSkillEffect(skillId)) {
            int remainingTimeMills = getSkillEffectTimeSec(skillId) * 1000;

            // 剩余的时间是有限的、效果时间无限制的情况下则覆盖。
            if (remainingTimeMills >= 0
                    && (remainingTimeMills < timeMillis || timeMillis == 0)) {
                killSkillEffectTimer(skillId);
                addSkillEffect(skillId, timeMillis);
            }
        } else {
            addSkillEffect(skillId, timeMillis);
        }
    }

    /**
     * 设置角色的睡眠状态.
     * 
     * @param flag
     *            - True or False
     */
    public void setSleeped(boolean flag) {
        _sleeped = flag;
    }

    public void setStatus(int i) {
        _status = i;
    }

    public void setStr(int i) {
        _trueStr = i;
        _str = (byte) IntRange.ensure(i, 1, 127);
    }

    public void setTempCharGfx(int i) {
        _tempCharGfx = i;
    }

    public void setTitle(String s) {
        _title = s;
    }

    public void setWis(int i) {
        _trueWis = i;
        _wis = (byte) IntRange.ensure(i, 1, 127);
    }

    /**
     * 指定的目标的方向.
     * 
     * @param tx
     *            - 目标的X坐标
     * @param ty
     *            - 目标的Y坐标
     * @return 目标的方向
     */
    public int targetDirection(int tx, int ty) {
        float dis_x = Math.abs(getX() - tx); // Ｘ方向のターゲットまでの距離
        float dis_y = Math.abs(getY() - ty); // Ｙ方向のターゲットまでの距離
        float dis = Math.max(dis_x, dis_y); // ターゲットまでの距離
        if (dis == 0) {
            return getHeading(); // 返回同一位置所面对的方向
        }
        int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 上下左右がちょっと優先な丸め
        int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 上下左右がちょっと優先な丸め

        int dir_x = 0;
        int dir_y = 0;
        if (getX() < tx) {
            dir_x = 1;
        }
        if (getX() > tx) {
            dir_x = -1;
        }
        if (getY() < ty) {
            dir_y = 1;
        }
        if (getY() > ty) {
            dir_y = -1;
        }

        if (avg_x == 0) {
            dir_x = 0;
        }
        if (avg_y == 0) {
            dir_y = 0;
        }

        if (dir_x == 1 && dir_y == -1) {
            return 1; // 上
        }
        if (dir_x == 1 && dir_y == 0) {
            return 2; // 右上
        }
        if (dir_x == 1 && dir_y == 1) {
            return 3; // 右
        }
        if (dir_x == 0 && dir_y == 1) {
            return 4; // 右下
        }
        if (dir_x == -1 && dir_y == 1) {
            return 5; // 下
        }
        if (dir_x == -1 && dir_y == 0) {
            return 6; // 左下
        }
        if (dir_x == -1 && dir_y == -1) {
            return 7; // 左
        }
        if (dir_x == 0 && dir_y == -1) {
            return 0; // 左上
        }
        return getHeading(); // ここにはこない。はず
    }

    /**
     * 开关灯.
     */
    public void turnOnOffLight() {
        int lightSize = 0;
        if (this instanceof L1NpcInstance) {
            L1NpcInstance npc = (L1NpcInstance) this;
            lightSize = npc.getLightSize(); // npc.sql的灯光大小
        }
        if (hasSkillEffect(LIGHT)) {
            lightSize = 14;
        }

        for (L1ItemInstance item : getInventory().getItems()) {
            if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light系列道具
                int itemlightSize = item.getItem().getLightRange();
                if (itemlightSize != 0 && item.isNowLighting()) {
                    if (itemlightSize > lightSize) {
                        lightSize = itemlightSize;
                    }
                }
            }
        }

        if (this instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) this;
            pc.sendPackets(new S_Light(pc.getId(), lightSize));
        }
        if (!isInvisble()) {
            broadcastPacket(new S_Light(getId(), lightSize));
        }

        setOwnLightSize(lightSize); // S_OwnCharPack灯光范围
        setChaLightSize(lightSize); // S_OtherCharPack, S_NPCPack灯光范围
    }

    /**
     * 向角色发送封包(50格内).
     * 
     * @param packet
     *            - 要发送的封包、ServerBasePacket物件。
     */
    public void wideBroadcastPacket(ServerBasePacket packet) {
        for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 50)) {
            pc.sendPackets(packet);
        }
    }

}
