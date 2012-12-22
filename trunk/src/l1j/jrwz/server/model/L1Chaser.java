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

import static l1j.jrwz.server.model.skill.L1SkillId.IMMUNE_TO_HARM;

import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.server.ActionCodes;
import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_DoActionGFX;
import l1j.jrwz.server.serverpackets.S_EffectLocation;

public class L1Chaser extends TimerTask {
    private static Logger _log = Logger.getLogger(L1Chaser.class.getName());

    private static final Random _random = new Random();
    private ScheduledFuture<?> _future = null;
    private int _timeCounter = 0;
    private final L1PcInstance _pc;
    private final L1Character _cha;

    public L1Chaser(L1PcInstance pc, L1Character cha) {
        _cha = cha;
        _pc = pc;
    }

    public void attack() {
        double damage = getDamage(_pc, _cha);
        if (_cha.getCurrentHp() - (int) damage <= 0 && _cha.getCurrentHp() != 1) {
            damage = _cha.getCurrentHp() - 1;
        } else if (_cha.getCurrentHp() == 1) {
            damage = 0;
        }
        S_EffectLocation packet = new S_EffectLocation(_cha.getX(),
                _cha.getY(), 7025);
        _pc.sendPackets(packet);
        _pc.broadcastPacket(packet);
        if (_cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) _cha;
            pc.sendPackets(new S_DoActionGFX(pc.getId(),
                    ActionCodes.ACTION_Damage));
            pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
                    ActionCodes.ACTION_Damage));
            pc.receiveDamage(_pc, damage, false);
        } else if (_cha instanceof L1NpcInstance) {
            L1NpcInstance npc = (L1NpcInstance) _cha;
            npc.broadcastPacket(new S_DoActionGFX(npc.getId(),
                    ActionCodes.ACTION_Damage));
            npc.receiveDamage(_pc, (int) damage);
        }
    }

    public void begin() {
        // 効果時間が8秒のため、4秒毎のスキルの場合処理時間を考慮すると実際には1回しか効果が現れない
        // よって開始時間を0.9秒後に設定しておく
        _future = GeneralThreadPool.getInstance().scheduleAtFixedRate(this, 0,
                1000);
    }

    public double getDamage(L1PcInstance pc, L1Character cha) {
        double dmg = 0;
        int spByItem = pc.getSp() - pc.getTrueSp();
        int intel = pc.getInt();
        int charaIntelligence = pc.getInt() + spByItem - 12;
        double coefficientA = 1.0 + 3.0 / 32.0 * charaIntelligence;
        if (coefficientA < 1) {
            coefficientA = 1.0;
        }
        double coefficientB = 0;
        if (intel > 18) {
            coefficientB = (intel + 2.0) / intel;
        } else if (intel <= 12) {
            coefficientB = 12.0 * 0.065;
        } else {
            coefficientB = intel * 0.065;
        }
        double coefficientC = 0;
        if (intel <= 12) {
            coefficientC = 12;
        } else {
            coefficientC = intel;
        }
        dmg = (_random.nextInt(6) + 1 + 7) * coefficientA * coefficientB / 10.5
                * coefficientC * 2.0;

        dmg = L1WeaponSkill.calcDamageReduction(pc, cha, dmg, 0);

        if (cha.hasSkillEffect(IMMUNE_TO_HARM)) {
            dmg /= 2.0;
        }

        return dmg;
    }

    @Override
    public void run() {
        try {
            if (_cha == null || _cha.isDead()) {
                stop();
                return;
            }
            attack();
            _timeCounter++;
            if (_timeCounter >= 3) {
                stop();
                return;
            }
        } catch (Throwable e) {
            _log.log(Level.WARNING, e.getLocalizedMessage(), e);
        }
    }

    public void stop() {
        if (_future != null) {
            _future.cancel(false);
        }
    }

}
