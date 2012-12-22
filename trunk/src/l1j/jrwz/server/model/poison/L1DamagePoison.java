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
package l1j.jrwz.server.model.poison;

import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_POISON;

import java.util.logging.Logger;

import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.Instance.L1MonsterInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;

public class L1DamagePoison extends L1Poison {
    private class NormalPoisonTimer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(_damageSpan);
                } catch (InterruptedException e) {
                    break;
                }

                if (!_target.hasSkillEffect(STATUS_POISON)) {
                    break;
                }
                if (_target instanceof L1PcInstance) {
                    L1PcInstance player = (L1PcInstance) _target;
                    player.receiveDamage(_attacker, _damage, false);
                    if (player.isDead()) { // 死亡したら解毒処理
                        break;
                    }
                } else if (_target instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) _target;
                    mob.receiveDamage(_attacker, _damage);
                    if (mob.isDead()) { // 死亡しても解毒しない
                        return;
                    }
                }
            }
            cure(); // 解毒処理
        }
    }

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1DamagePoison.class
            .getName());

    public static boolean doInfection(L1Character attacker, L1Character cha,
            int damageSpan, int damage) {
        if (!isValidTarget(cha)) {
            return false;
        }

        cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage));
        return true;
    }

    private Thread _timer;
    private final L1Character _attacker;
    private final L1Character _target;

    private final int _damageSpan;

    private final int _damage;

    private L1DamagePoison(L1Character attacker, L1Character cha,
            int damageSpan, int damage) {
        _attacker = attacker;
        _target = cha;
        _damageSpan = damageSpan;
        _damage = damage;

        doInfection();
    }

    @Override
    public void cure() {
        if (_timer != null) {
            _timer.interrupt(); // 毒タイマー解除
        }

        _target.setPoisonEffect(0);
        _target.killSkillEffectTimer(STATUS_POISON);
        _target.setPoison(null);
    }

    private void doInfection() {
        _target.setSkillEffect(STATUS_POISON, 30000);
        _target.setPoisonEffect(1);

        if (isDamageTarget(_target)) {
            _timer = new NormalPoisonTimer();
            GeneralThreadPool.getInstance().execute(_timer); // 通常毒タイマー開始
        }
    }

    @Override
    public int getEffectId() {
        return 1;
    }

    boolean isDamageTarget(L1Character cha) {
        return (cha instanceof L1PcInstance)
                || (cha instanceof L1MonsterInstance);
    }
}
