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

import java.util.logging.Logger;

import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.WarTimeController;
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.model.L1Attack;
import l1j.jrwz.server.model.L1CastleLocation;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1War;
import l1j.jrwz.server.model.L1WarSpawn;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.serverpackets.S_DoActionGFX;
import l1j.jrwz.server.serverpackets.S_NPCPack;
import l1j.jrwz.server.serverpackets.S_RemoveObject;
import l1j.jrwz.server.templates.L1Npc;

public class L1TowerInstance extends L1NpcInstance {
    class Death implements Runnable {
        L1Character lastAttacker = _lastattacker;
        L1Object object = L1World.getInstance().findObject(getId());
        L1TowerInstance npc = (L1TowerInstance) object;

        @Override
        public void run() {
            setCurrentHpDirect(0);
            setDead(true);
            setStatus(ActionCodes.ACTION_TowerDie);
            int targetobjid = npc.getId();

            npc.getMap().setPassable(npc.getLocation(), true);

            npc.broadcastPacket(new S_DoActionGFX(targetobjid,
                    ActionCodes.ACTION_TowerDie));

            // クラウンをspawnする
            if (!isSubTower()) {
                L1WarSpawn warspawn = new L1WarSpawn();
                warspawn.SpawnCrown(_castle_id);
            }
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1TowerInstance.class
            .getName());

    private L1Character _lastattacker;
    private int _castle_id;
    private int _crackStatus;

    public L1TowerInstance(L1Npc template) {
        super(template);
    }

    @Override
    public void deleteMe() {
        _destroyed = true;
        if (getInventory() != null) {
            getInventory().clearItems();
        }
        allTargetClear();
        _master = null;
        L1World.getInstance().removeVisibleObject(this);
        L1World.getInstance().removeObject(this);
        for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
            pc.removeKnownObject(this);
            pc.sendPackets(new S_RemoveObject(this));
        }
        removeAllKnownObjects();
    }

    public boolean isSubTower() {
        return (getNpcTemplate().get_npcId() == 81190
                || getNpcTemplate().get_npcId() == 81191
                || getNpcTemplate().get_npcId() == 81192 || getNpcTemplate()
                .get_npcId() == 81193);
    }

    @Override
    public void onAction(L1PcInstance player) {
        if (getCurrentHp() > 0 && !isDead()) {
            L1Attack attack = new L1Attack(player, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                attack.addPcPoisonAttack(player, this);
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        }
    }

    @Override
    public void onPerceive(L1PcInstance perceivedFrom) {
        perceivedFrom.addKnownObject(this);
        perceivedFrom.sendPackets(new S_NPCPack(this));
    }

    @Override
    public void receiveDamage(L1Character attacker, int damage) { // 攻撃でＨＰを減らすときはここを使用
        if (_castle_id == 0) { // 初期設定で良いがいい場所がない
            if (isSubTower()) {
                _castle_id = L1CastleLocation.ADEN_CASTLE_ID;
            } else {
                _castle_id = L1CastleLocation.getCastleId(getX(), getY(),
                        getMapId());
            }
        }

        if (_castle_id > 0
                && WarTimeController.getInstance().isNowWar(_castle_id)) { // 戦争時間内

            // アデン城のメインタワーはサブタワーが3つ以上破壊されている場合のみ攻撃可能
            if (_castle_id == L1CastleLocation.ADEN_CASTLE_ID && !isSubTower()) {
                int subTowerDeadCount = 0;
                for (L1Object l1object : L1World.getInstance().getObject()) {
                    if (l1object instanceof L1TowerInstance) {
                        L1TowerInstance tower = (L1TowerInstance) l1object;
                        if (tower.isSubTower() && tower.isDead()) {
                            subTowerDeadCount++;
                            if (subTowerDeadCount == 4) {
                                break;
                            }
                        }
                    }
                }
                if (subTowerDeadCount < 3) {
                    return;
                }
            }

            L1PcInstance pc = null;
            if (attacker instanceof L1PcInstance) {
                pc = (L1PcInstance) attacker;
            } else if (attacker instanceof L1PetInstance) {
                pc = (L1PcInstance) ((L1PetInstance) attacker).getMaster();
            } else if (attacker instanceof L1SummonInstance) {
                pc = (L1PcInstance) ((L1SummonInstance) attacker).getMaster();
            }
            if (pc == null) {
                return;
            }

            // 布告しているかチェック。但し、城主が居ない場合は布告不要
            boolean existDefenseClan = false;
            for (L1Clan clan : L1World.getInstance().getAllClans()) {
                int clanCastleId = clan.getCastleId();
                if (clanCastleId == _castle_id) {
                    existDefenseClan = true;
                    break;
                }
            }
            boolean isProclamation = false;
            // 全戦争リストを取得
            for (L1War war : L1World.getInstance().getWarList()) {
                if (_castle_id == war.GetCastleId()) { // 今居る城の戦争
                    isProclamation = war.CheckClanInWar(pc.getClanname());
                    break;
                }
            }
            if (existDefenseClan == true && isProclamation == false) { // 城主が居て、布告していない場合
                return;
            }

            if (getCurrentHp() > 0 && !isDead()) {
                int newHp = getCurrentHp() - damage;
                if (newHp <= 0 && !isDead()) {
                    setCurrentHpDirect(0);
                    setDead(true);
                    setStatus(ActionCodes.ACTION_TowerDie);
                    _lastattacker = attacker;
                    _crackStatus = 0;
                    Death death = new Death();
                    GeneralThreadPool.getInstance().execute(death);
                    // Death(attacker);
                }
                if (newHp > 0) {
                    setCurrentHp(newHp);
                    if ((getMaxHp() * 1 / 4) > getCurrentHp()) {
                        if (_crackStatus != 3) {
                            broadcastPacket(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_TowerCrack3));
                            setStatus(ActionCodes.ACTION_TowerCrack3);
                            _crackStatus = 3;
                        }
                    } else if ((getMaxHp() * 2 / 4) > getCurrentHp()) {
                        if (_crackStatus != 2) {
                            broadcastPacket(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_TowerCrack2));
                            setStatus(ActionCodes.ACTION_TowerCrack2);
                            _crackStatus = 2;
                        }
                    } else if ((getMaxHp() * 3 / 4) > getCurrentHp()) {
                        if (_crackStatus != 1) {
                            broadcastPacket(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_TowerCrack1));
                            setStatus(ActionCodes.ACTION_TowerCrack1);
                            _crackStatus = 1;
                        }
                    }
                }
            } else if (!isDead()) { // 念のため
                setDead(true);
                setStatus(ActionCodes.ACTION_TowerDie);
                _lastattacker = attacker;
                Death death = new Death();
                GeneralThreadPool.getInstance().execute(death);
                // Death(attacker);
            }
        }
    }

    @Override
    public void setCurrentHp(int i) {
        int currentHp = i;
        if (currentHp >= getMaxHp()) {
            currentHp = getMaxHp();
        }
        setCurrentHpDirect(currentHp);
    }

}
