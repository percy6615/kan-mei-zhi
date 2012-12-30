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

import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_BALANCE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_IGNITION_TO_ALLY;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_IGNITION_TO_ENEMY;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_QUAKE_TO_ALLY;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_QUAKE_TO_ENEMY;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_SHOCK_TO_ALLY;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CUBE_SHOCK_TO_ENEMY;

import java.util.logging.Logger;

import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.WarTimeController;
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.datatables.SkillsTable;
import l1j.jrwz.server.model.L1CastleLocation;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.L1Cube;
import l1j.jrwz.server.model.L1Magic;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.serverpackets.S_DoActionGFX;
import l1j.jrwz.server.serverpackets.S_OwnCharAttrDef;
import l1j.jrwz.server.serverpackets.S_RemoveObject;
import l1j.jrwz.server.serverpackets.S_SkillSound;
import l1j.jrwz.server.templates.L1Npc;

public class L1EffectInstance extends L1NpcInstance {
    class CubeTimer implements Runnable {
        private final L1EffectInstance _effect;

        public CubeTimer(L1EffectInstance effect) {
            _effect = effect;
        }

        @Override
        public void run() {
            while (!_destroyed) {
                try {
                    for (L1Object objects : L1World.getInstance()
                            .getVisibleObjects(_effect, 3)) {
                        if (objects instanceof L1PcInstance) {
                            L1PcInstance pc = (L1PcInstance) objects;
                            if (pc.isDead()) {
                                continue;
                            }
                            L1PcInstance user = getUser(); // Cube使用者
                            if (pc.getId() == user.getId()) {
                                cubeToAlly(pc, _effect);
                                continue;
                            }
                            if (pc.getClanid() != 0
                                    && user.getClanid() == pc.getClanid()) {
                                cubeToAlly(pc, _effect);
                                continue;
                            }
                            if (pc.isInParty() && pc.getParty().isMember(user)) {
                                cubeToAlly(pc, _effect);
                                continue;
                            }
                            if (pc.getZoneType() == 1) { // セーフティーゾーンでは戦争中を除き敵には無効
                                boolean isNowWar = false;
                                int castleId = L1CastleLocation
                                        .getCastleIdByArea(pc);
                                if (castleId > 0) {
                                    isNowWar = WarTimeController.getInstance()
                                            .isNowWar(castleId);
                                }
                                if (!isNowWar) {
                                    continue;
                                }
                                cubeToEnemy(pc, _effect);
                            } else {
                                cubeToEnemy(pc, _effect);
                            }
                        } else if (objects instanceof L1MonsterInstance) {
                            L1MonsterInstance mob = (L1MonsterInstance) objects;
                            if (mob.isDead()) {
                                continue;
                            }
                            cubeToEnemy(mob, _effect);
                        }
                    }
                    Thread.sleep(CUBE_INTERVAL);
                } catch (InterruptedException ignore) {
                    // ignore
                }
            }
        }
    }

    class FwDamageTimer implements Runnable {
        private final L1EffectInstance _effect;

        public FwDamageTimer(L1EffectInstance effect) {
            _effect = effect;
        }

        @Override
        public void run() {
            while (!_destroyed) {
                try {
                    for (L1Object objects : L1World.getInstance()
                            .getVisibleObjects(_effect, 0)) {
                        if (objects instanceof L1PcInstance) {
                            L1PcInstance pc = (L1PcInstance) objects;
                            if (pc.isDead()) {
                                continue;
                            }
                            if (pc.getZoneType() == 1) {
                                boolean isNowWar = false;
                                int castleId = L1CastleLocation
                                        .getCastleIdByArea(pc);
                                if (castleId > 0) {
                                    isNowWar = WarTimeController.getInstance()
                                            .isNowWar(castleId);
                                }
                                if (!isNowWar) {
                                    continue;
                                }
                            }
                            L1Magic magic = new L1Magic(_effect, pc);
                            int damage = magic.calcPcFireWallDamage();
                            if (damage == 0) {
                                continue;
                            }
                            pc.sendPackets(new S_DoActionGFX(pc.getId(),
                                    ActionCodes.ACTION_Damage));
                            pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
                                    ActionCodes.ACTION_Damage));
                            pc.receiveDamage(_effect, damage, false);
                        } else if (objects instanceof L1MonsterInstance) {
                            L1MonsterInstance mob = (L1MonsterInstance) objects;
                            if (mob.isDead()) {
                                continue;
                            }
                            L1Magic magic = new L1Magic(_effect, mob);
                            int damage = magic.calcNpcFireWallDamage();
                            if (damage == 0) {
                                continue;
                            }
                            mob.broadcastPacket(new S_DoActionGFX(mob.getId(),
                                    ActionCodes.ACTION_Damage));
                            mob.receiveDamage(_effect, damage);
                        }
                    }
                    Thread.sleep(FW_DAMAGE_INTERVAL);
                } catch (InterruptedException ignore) {
                    // ignore
                }
            }
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1EffectInstance.class
            .getName());
    private static final int FW_DAMAGE_INTERVAL = 1000;

    private static final int CUBE_INTERVAL = 500; // キューブ範囲内に居るキャラクターをチェックする間隔

    private static final int CUBE_TIME = 8000; // 効果時間8秒?

    private L1PcInstance _pc;

    private int _skillId;

    public L1EffectInstance(L1Npc template) {
        super(template);

        int npcId = getNpcTemplate().get_npcId();
        if (npcId == 81157) { // FW
            GeneralThreadPool.getInstance().schedule(
                    new FwDamageTimer(this), 0);
        } else if (npcId == 80149 // キューブ[イグニション]
                || npcId == 80150 // キューブ[クエイク]
                || npcId == 80151 // キューブ[ショック]
                || npcId == 80152) { // キューブ[バランス]
            GeneralThreadPool.getInstance().schedule(
                    new CubeTimer(this), 0);
        }
    }

    private void cubeToAlly(L1Character cha, L1Character effect) {
        int npcId = getNpcTemplate().get_npcId();
        int castGfx = SkillsTable.getInstance().getTemplate(getSkillId())
                .getCastGfx();
        L1PcInstance pc = null;

        if (npcId == 80149) { // キューブ[イグニション]
            if (!cha.hasSkillEffect(STATUS_CUBE_IGNITION_TO_ALLY)) {
                cha.addFire(30);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_OwnCharAttrDef(pc));
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx));
                cha.setSkillEffect(STATUS_CUBE_IGNITION_TO_ALLY, CUBE_TIME);
            }
        } else if (npcId == 80150) { // キューブ[クエイク]
            if (!cha.hasSkillEffect(STATUS_CUBE_QUAKE_TO_ALLY)) {
                cha.addEarth(30);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_OwnCharAttrDef(pc));
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx));
                cha.setSkillEffect(STATUS_CUBE_QUAKE_TO_ALLY, CUBE_TIME);
            }
        } else if (npcId == 80151) { // キューブ[ショック]
            if (!cha.hasSkillEffect(STATUS_CUBE_SHOCK_TO_ALLY)) {
                cha.addWind(30);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_OwnCharAttrDef(pc));
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx));
                cha.setSkillEffect(STATUS_CUBE_SHOCK_TO_ALLY, CUBE_TIME);
            }
        } else if (npcId == 80152) { // キューブ[バランス]
            if (!cha.hasSkillEffect(STATUS_CUBE_BALANCE)) {
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx));
                cha.setSkillEffect(STATUS_CUBE_BALANCE, CUBE_TIME);
                L1Cube cube = new L1Cube(effect, cha, STATUS_CUBE_BALANCE);
                cube.begin();
            }
        }
    }

    private void cubeToEnemy(L1Character cha, L1Character effect) {
        int npcId = getNpcTemplate().get_npcId();
        int castGfx2 = SkillsTable.getInstance().getTemplate(getSkillId())
                .getCastGfx2();
        L1PcInstance pc = null;
        if (npcId == 80149) { // キューブ[イグニション]
            if (!cha.hasSkillEffect(STATUS_CUBE_IGNITION_TO_ENEMY)) {
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx2));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx2));
                cha.setSkillEffect(STATUS_CUBE_IGNITION_TO_ENEMY, CUBE_TIME);
                L1Cube cube = new L1Cube(effect, cha,
                        STATUS_CUBE_IGNITION_TO_ENEMY);
                cube.begin();
            }
        } else if (npcId == 80150) { // キューブ[クエイク]
            if (!cha.hasSkillEffect(STATUS_CUBE_QUAKE_TO_ENEMY)) {
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx2));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx2));
                cha.setSkillEffect(STATUS_CUBE_QUAKE_TO_ENEMY, CUBE_TIME);
                L1Cube cube = new L1Cube(effect, cha,
                        STATUS_CUBE_QUAKE_TO_ENEMY);
                cube.begin();
            }
        } else if (npcId == 80151) { // キューブ[ショック]
            if (!cha.hasSkillEffect(STATUS_CUBE_SHOCK_TO_ENEMY)) {
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx2));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx2));
                cha.setSkillEffect(STATUS_CUBE_SHOCK_TO_ENEMY, CUBE_TIME);
                L1Cube cube = new L1Cube(effect, cha,
                        STATUS_CUBE_SHOCK_TO_ENEMY);
                cube.begin();
            }
        } else if (npcId == 80152) { // キューブ[バランス]
            if (!cha.hasSkillEffect(STATUS_CUBE_BALANCE)) {
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_SkillSound(pc.getId(), castGfx2));
                }
                cha.broadcastPacket(new S_SkillSound(cha.getId(), castGfx2));
                cha.setSkillEffect(STATUS_CUBE_BALANCE, CUBE_TIME);
                L1Cube cube = new L1Cube(effect, cha, STATUS_CUBE_BALANCE);
                cube.begin();
            }
        }
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

    public int getSkillId() {
        return _skillId;
    }

    public L1PcInstance getUser() {
        return _pc;
    }

    @Override
    public void onAction(L1PcInstance pc) {
    }

    public void setSkillId(int i) {
        _skillId = i;
    }

    public void setUser(L1PcInstance pc) {
        _pc = pc;
    }

}
