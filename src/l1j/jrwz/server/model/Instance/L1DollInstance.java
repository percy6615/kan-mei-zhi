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

package l1j.jrwz.server.model.Instance;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.IdFactory;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.serverpackets.S_DollPack;
import l1j.jrwz.server.serverpackets.S_SkillSound;
import l1j.jrwz.server.templates.L1Npc;

public class L1DollInstance extends L1NpcInstance {
    // 時間計測用
    class DollTimer implements Runnable {
        @Override
        public void run() {
            if (_destroyed) { // 既に破棄されていないかチェック
                return;
            }
            deleteDoll();
        }
    }

    private static final long serialVersionUID = 1L;
    public static final int DOLLTYPE_BUGBEAR = 0;
    public static final int DOLLTYPE_SUCCUBUS = 1;
    public static final int DOLLTYPE_WAREWOLF = 2;
    public static final int DOLLTYPE_ELDER = 3;
    public static final int DOLLTYPE_CRUSTANCEAN = 4;
    public static final int DOLLTYPE_GOLEM = 5;

    public static final int DOLL_TIME = 1800000;
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1DollInstance.class
            .getName());
    private static Random _random = new Random();
    private int _dollType;

    private int _itemObjId;

    public L1DollInstance(L1Npc template, L1PcInstance master, int dollType,
            int itemObjId) {
        super(template);
        setId(IdFactory.getInstance().nextId());

        setDollType(dollType);
        setItemObjId(itemObjId);
        GeneralThreadPool.getInstance().schedule(new DollTimer(),
                DOLL_TIME);

        setMaster(master);
        setX(master.getX() + _random.nextInt(5) - 2);
        setY(master.getY() + _random.nextInt(5) - 2);
        setMap(master.getMapId());
        setHeading(5);
        setLightSize(template.getLightSize());

        L1World.getInstance().storeObject(this);
        L1World.getInstance().addVisibleObject(this);
        for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
            onPerceive(pc);
        }
        master.addDoll(this);
        if (!isAiRunning()) {
            startAI();
        }
        if (isMpRegeneration()) {
            master.startMpRegenerationByDoll();
        }
    }

    public void deleteDoll() {
        if (isMpRegeneration()) {
            ((L1PcInstance) _master).stopMpRegenerationByDoll();
        }
        _master.getDollList().remove(getId());
        deleteMe();
    }

    public int getDamageByDoll() {
        int damage = 0;
        int dollType = getDollType();
        if (dollType == DOLLTYPE_WAREWOLF || dollType == DOLLTYPE_CRUSTANCEAN) {
            int chance = _random.nextInt(100) + 1;
            if (chance <= 3) {
                damage = 15;
                if (_master instanceof L1PcInstance) {
                    L1PcInstance pc = (L1PcInstance) _master;
                    pc.sendPackets(new S_SkillSound(_master.getId(), 6319));
                }
                _master.broadcastPacket(new S_SkillSound(_master.getId(), 6319));
            }
        }
        return damage;
    }

    public int getDamageReductionByDoll() {
        int damageReduction = 0;
        int dollType = getDollType();
        if (dollType == DOLLTYPE_GOLEM) {
            int chance = _random.nextInt(100) + 1;
            if (chance <= 4) {
                damageReduction = 15;
            }
        }
        return damageReduction;
    }

    public int getDollType() {
        return _dollType;
    }

    public int getItemObjId() {
        return _itemObjId;
    }

    public int getWeightReductionByDoll() {
        int weightReduction = 0;
        int dollType = getDollType();
        if (dollType == DOLLTYPE_BUGBEAR) {
            weightReduction = 20;
        }
        return weightReduction;
    }

    public boolean isMpRegeneration() {
        boolean isMpRegeneration = false;
        int dollType = getDollType();
        if (dollType == DOLLTYPE_SUCCUBUS || dollType == DOLLTYPE_ELDER) {
            isMpRegeneration = true;
        }
        return isMpRegeneration;
    }

    // ターゲットがいない場合の処理
    @Override
    public boolean noTarget() {
        if (_master.isDead()) {
            deleteDoll();
            return true;
        } else if (_master != null && _master.getMapId() == getMapId()) {
            if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
                int dir = moveDirection(_master.getX(), _master.getY());
                if (dir == -1) {
                    if (!isAiRunning()) {
                        startAI();
                    }
                    return true;
                } else {
                    setDirectionMove(dir);
                    setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
                }
            }
        } else {
            deleteDoll();
            return true;
        }
        return false;
    }

    @Override
    public void onGetItem(L1ItemInstance item) {
        if (getNpcTemplate().get_digestitem() > 0) {
            setDigestItem(item);
        }
        if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
            useItem(USEITEM_HASTE, 100);
        }
    }

    @Override
    public void onItemUse() {
        if (!isActived()) {
            // １００％の確率でヘイストポーション使用
            useItem(USEITEM_HASTE, 100);
        }
    }

    @Override
    public void onPerceive(L1PcInstance perceivedFrom) {
        perceivedFrom.addKnownObject(this);
        perceivedFrom.sendPackets(new S_DollPack(this, perceivedFrom));
    }

    public void setDollType(int i) {
        _dollType = i;
    }

    public void setItemObjId(int i) {
        _itemObjId = i;
    }

}
