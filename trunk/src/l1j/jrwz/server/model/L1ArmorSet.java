package l1j.jrwz.server.model;

import static l1j.jrwz.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static l1j.jrwz.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static l1j.jrwz.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.jrwz.server.datatables.ArmorSetTable;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.templates.L1ArmorSets;
import l1j.jrwz.server.templates.L1Item;

/**
 * 套装效果:物理、血、魔、魔防.
 */
class AcHpMpBonusEffect implements L1ArmorSetEffect {
    /** 物理防御. */
    private final int _ac;
    /** 增加血量. */
    private final int _addHp;
    /** 增加魔量. */
    private final int _addMp;
    /** 回复血量. */
    private final int _regenHp;
    /** 回复魔量. */
    private final int _regenMp;
    /** 增加魔防. */
    private final int _addMr;

    /**
     * 套装效果:物理、血、魔、魔防.
     * 
     * @param a
     *            - 物理防御
     * @param hp
     *            - 增加血量
     * @param mp
     *            - 增加魔量
     * @param hpr
     *            - 回复血量
     * @param mpr
     *            - 回复魔量
     * @param mr
     *            - 增加魔防
     */
    public AcHpMpBonusEffect(int ac, int addHp, int addMp, int regenHp, int regenMp, int addMr) {
        _ac = ac;
        _addHp = addHp;
        _addMp = addMp;
        _regenHp = regenHp;
        _regenMp = regenMp;
        _addMr = addMr;
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        pc.addAc(-_ac);
        pc.addMaxHp(-_addHp);
        pc.addMaxMp(-_addMp);
        pc.addHpr(-_regenHp);
        pc.addMpr(-_regenMp);
        pc.addMr(-_addMr);
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        pc.addAc(_ac);
        pc.addMaxHp(_addHp);
        pc.addMaxMp(_addMp);
        pc.addHpr(_regenHp);
        pc.addMpr(_regenMp);
        pc.addMr(_addMr);
    }
}

/**
 * 套装效果:水、风、火、地属性.
 */
class DefenseBonusEffect implements L1ArmorSetEffect {
    /** 属性防御:水. */
    private final int _defenseWater;
    /** 属性防御:风. */
    private final int _defenseWind;
    /** 属性防御:火. */
    private final int _defenseFire;
    /** 属性防御:地. */
    private final int _defenseEarth;

    /**
     * 套装效果:水、风、火、地属性.
     * 
     * @param water
     *            - 水属性
     * @param wind
     *            - 风属性
     * @param fire
     *            - 火属性
     * @param earth
     *            - 地属性
     */
    public DefenseBonusEffect(int defenseWater, int defenseWind, int defenseFire, int defenseEarth) {
        _defenseWater = defenseWater;
        _defenseWind = defenseWind;
        _defenseFire = defenseFire;
        _defenseEarth = defenseEarth;
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        pc.addWater(-_defenseWater);
        pc.addWind(-_defenseWind);
        pc.addFire(-_defenseFire);
        pc.addEarth(-_defenseEarth);
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        pc.addWater(_defenseWater);
        pc.addWind(_defenseWind);
        pc.addFire(_defenseFire);
        pc.addEarth(_defenseEarth);
    }
}

/**
 * 套装效果:抽象类及其实现类.
 */
public abstract class L1ArmorSet {
    /** 所有套装. */
    private static ArrayList<L1ArmorSet> _allSet = new ArrayList<L1ArmorSet>();

    /*
     * 这里的初始化会觉得是丑什么的〜〜V
     */
    static {
        L1ArmorSetImpl impl;

        for (L1ArmorSets armorSets : ArmorSetTable.getInstance().getAllList()) {
            try {

                impl = new L1ArmorSetImpl(getArray(armorSets.getSets(), ","));
                if (armorSets.getPolyId() != -1) {
                    impl.addEffect(new PolymorphEffect(armorSets.getPolyId()));
                }
                impl.addEffect(new AcHpMpBonusEffect(armorSets.getAc(), armorSets.getHp(), armorSets.getMp(), armorSets
                        .getHpr(), armorSets.getMpr(), armorSets.getMr()));
                impl.addEffect(new StatBonusEffect(armorSets.getStr(), armorSets.getDex(), armorSets.getCon(),
                        armorSets.getWis(), armorSets.getCha(), armorSets.getIntl()));
                impl.addEffect(new DefenseBonusEffect(armorSets.getDefenseWater(), armorSets.getDefenseWind(),
                        armorSets.getDefenseFire(), armorSets.getDefenseWind()));
                _allSet.add(impl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 取得所有套装.
     * 
     * @return 所有套装
     */
    public static ArrayList<L1ArmorSet> getAllSet() {
        return _allSet;
    }

    /**
     * 取得阵列.
     * 
     * @param s
     *            - 解析的字串
     * @param sToken
     *            - 分隔符
     * @return 阵列
     */
    private static int[] getArray(String s, String sToken) {
        StringTokenizer st = new StringTokenizer(s, sToken);
        int size = st.countTokens();
        String temp = null;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            temp = st.nextToken();
            array[i] = Integer.parseInt(temp);
        }
        return array;
    }

    /**
     * 取消效果.
     * 
     * @param pc
     *            - 对象
     */
    public abstract void cancelEffect(L1PcInstance pc);

    /**
     * 给予效果.
     * 
     * @param pc
     *            - 对象
     */
    public abstract void giveEffect(L1PcInstance pc);

    /**
     * 是否装备的为戒指.
     * 
     * @param pc
     *            - 对象
     * @return true = 是、 false = 否
     */
    public abstract boolean isEquippedRingOfArmorSet(L1PcInstance pc);

    /**
     * 是否套装的零件.
     * 
     * @param id
     *            - 零件ID
     * @return true = 是、 false = 否
     */
    public abstract boolean isPartOfSet(int id);

    /**
     * 是否有效果.
     * 
     * @param pc
     *            - 对象
     * @return 套装效果
     */
    public abstract boolean isValid(L1PcInstance pc);
}

/**
 * 套装效果接口.
 */
interface L1ArmorSetEffect {
    /**
     * 套装效果:取消.
     * 
     * @param pc
     *            - 对象
     */
    public void cancelEffect(L1PcInstance pc);

    /**
     * 套装效果:给予.
     * 
     * @param pc
     *            - 对象
     */
    public void giveEffect(L1PcInstance pc);
}

/**
 * 套装效果入口.
 */
class L1ArmorSetImpl extends L1ArmorSet {
    /** 套装编号阵列. */
    private final int _ids[];
    /** 套装效果. */
    private final ArrayList<L1ArmorSetEffect> _effects;
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1ArmorSetImpl.class.getName());

    /**
     * 套装效果入口.
     * 
     * @param ids
     *            - 套装编号阵列
     */
    protected L1ArmorSetImpl(int ids[]) {
        _ids = ids;
        _effects = new ArrayList<L1ArmorSetEffect>();
    }

    /**
     * 增加效果.
     * 
     * @param effect
     *            - 效果
     */
    public void addEffect(L1ArmorSetEffect effect) {
        _effects.add(effect);
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        for (L1ArmorSetEffect effect : _effects) {
            effect.cancelEffect(pc);
        }
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        for (L1ArmorSetEffect effect : _effects) {
            effect.giveEffect(pc);
        }
    }

    @Override
    public boolean isEquippedRingOfArmorSet(L1PcInstance pc) {
        L1PcInventory pcInventory = pc.getInventory();
        L1ItemInstance armor = null;
        boolean isSetContainRing = false;

        // セット装備にリングが含まれているか調べる
        for (int id : _ids) {
            armor = pcInventory.findItemId(id);
            if (armor.getItem().getType2() == L1Item.TYPE2_ARMOR && armor.getItem().getType() == 9) { // ring
                isSetContainRing = true;
                break;
            }
        }

        // リングを2つ装備していて、それが両方セット装備か調べる
        if (armor != null && isSetContainRing) {
            int itemId = armor.getItem().getItemId();
            if (pcInventory.getTypeEquipped(2, 9) == 2) {
                L1ItemInstance ring[] = new L1ItemInstance[2];
                ring = pcInventory.getRingEquipped();
                if (ring[0].getItem().getItemId() == itemId && ring[1].getItem().getItemId() == itemId) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isPartOfSet(int id) {
        for (int i : _ids) {
            if (id == i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean isValid(L1PcInstance pc) {
        return pc.getInventory().checkEquipped(_ids);
    }

    /**
     * 删除效果.
     * 
     * @param effect
     *            - 效果
     */
    public void removeEffect(L1ArmorSetEffect effect) {
        _effects.remove(effect);
    }

}

/**
 * 套装效果:变身.
 */
class PolymorphEffect implements L1ArmorSetEffect {
    /** 变身编号. */
    private int _gfxId;

    /**
     * 套装效果:变身.
     * 
     * @param gfxId
     *            - 变身编号
     */
    public PolymorphEffect(int gfxId) {
        _gfxId = gfxId;
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId == AWAKEN_ANTHARAS || awakeSkillId == AWAKEN_FAFURION || awakeSkillId == AWAKEN_VALAKAS) {
            pc.sendPackets(new S_ServerMessage(1384)); // 目前状态中无法变身。
            return;
        }
        if (_gfxId == 6080) {
            if (pc.get_sex() == 0) {
                _gfxId = 6094;
            }
        }
        if (pc.getTempCharGfx() != _gfxId) {
            return;
        }
        L1PolyMorph.undoPoly(pc);
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId == AWAKEN_ANTHARAS || awakeSkillId == AWAKEN_FAFURION || awakeSkillId == AWAKEN_VALAKAS) {
            pc.sendPackets(new S_ServerMessage(1384)); // 目前状态中无法变身。
            return;
        }
        if (_gfxId == 6080 || _gfxId == 6094) {
            if (pc.get_sex() == 0) {
                _gfxId = 6094;
            } else {
                _gfxId = 6080;
            }
            if (!isRemainderOfCharge(pc)) { // 没有剩余次数
                return;
            }
        }
        L1PolyMorph.doPoly(pc, _gfxId, 0, L1PolyMorph.MORPH_BY_ITEMMAGIC);
    }

    /**
     * 是否有剩余次数.
     * 
     * @param pc
     *            - 对象
     * @return 剩余次数
     */
    private boolean isRemainderOfCharge(L1PcInstance pc) {
        boolean isRemainderOfCharge = false;
        if (pc.getInventory().checkItem(20383, 1)) { // 军马头盔
            L1ItemInstance item = pc.getInventory().findItemId(20383);
            if (item != null) {
                if (item.getChargeCount() != 0) {
                    isRemainderOfCharge = true;
                }
            }
        }
        return isRemainderOfCharge;
    }

}

/**
 * 套装效果:六属性(力、敏、体、精、魅、智).
 */
class StatBonusEffect implements L1ArmorSetEffect {
    /** 力量. */
    private final int _str;
    /** 敏捷. */
    private final int _dex;
    /** 体质. */
    private final int _con;
    /** 精神. */
    private final int _wis;
    /** 魅力. */
    private final int _cha;
    /** 智力. */
    private final int _intl;

    /**
     * 套装效果:六属性(力、敏、体、精、魅、智).
     * 
     * @param str
     *            - 力量
     * @param dex
     *            - 敏捷
     * @param con
     *            - 体质
     * @param wis
     *            - 精神
     * @param cha
     *            - 魅力
     * @param intl
     *            - 智力
     */
    public StatBonusEffect(int str, int dex, int con, int wis, int cha, int intl) {
        _str = str;
        _dex = dex;
        _con = con;
        _wis = wis;
        _cha = cha;
        _intl = intl;
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        pc.addStr((byte) -_str);
        pc.addDex((byte) -_dex);
        pc.addCon((byte) -_con);
        pc.addWis((byte) -_wis);
        pc.addCha((byte) -_cha);
        pc.addInt((byte) -_intl);
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        pc.addStr((byte) _str);
        pc.addDex((byte) _dex);
        pc.addCon((byte) _con);
        pc.addWis((byte) _wis);
        pc.addCha((byte) _cha);
        pc.addInt((byte) _intl);
    }
}
