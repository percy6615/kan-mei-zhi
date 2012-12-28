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

import static l1j.jrwz.server.model.skill.L1SkillId.FOG_OF_SLEEPING;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.datatables.DropTable;
import l1j.jrwz.server.datatables.NPCTalkDataTable;
import l1j.jrwz.server.model.L1Attack;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.L1NpcTalkData;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.serverpackets.S_ChangeHeading;
import l1j.jrwz.server.serverpackets.S_DoActionGFX;
import l1j.jrwz.server.serverpackets.S_NPCTalkReturn;
import l1j.jrwz.server.serverpackets.S_NpcChatPacket;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.templates.L1Npc;
import l1j.jrwz.server.utils.CalcExp;

public class L1GuardianInstance extends L1NpcInstance {
    class Death implements Runnable {
        L1Character lastAttacker = _lastattacker;

        @Override
        public void run() {
            setDeathProcessing(true);
            setCurrentHpDirect(0);
            setDead(true);
            setStatus(ActionCodes.ACTION_Die);
            int targetobjid = getId();
            getMap().setPassable(getLocation(), true);
            broadcastPacket(new S_DoActionGFX(targetobjid,
                    ActionCodes.ACTION_Die));

            L1PcInstance player = null;
            if (lastAttacker instanceof L1PcInstance) {
                player = (L1PcInstance) lastAttacker;
            } else if (lastAttacker instanceof L1PetInstance) {
                player = (L1PcInstance) ((L1PetInstance) lastAttacker)
                        .getMaster();
            } else if (lastAttacker instanceof L1SummonInstance) {
                player = (L1PcInstance) ((L1SummonInstance) lastAttacker)
                        .getMaster();
            }
            if (player != null) {
                ArrayList<L1Character> targetList = _hateList
                        .toTargetArrayList();
                ArrayList<Integer> hateList = _hateList.toHateArrayList();
                int exp = getExp();
                CalcExp.calcExp(player, targetobjid, targetList, hateList, exp);

                ArrayList<L1Character> dropTargetList = _dropHateList
                        .toTargetArrayList();
                ArrayList<Integer> dropHateList = _dropHateList
                        .toHateArrayList();
                try {
                    DropTable.getInstance().dropShare(_npc, dropTargetList,
                            dropHateList);
                } catch (Exception e) {
                    _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
                // カルマは止めを刺したプレイヤーに設定。ペットorサモンで倒した場合も入る。
                player.addKarma((int) (getKarma() * Config.RATE_KARMA));
            }
            setDeathProcessing(false);

            setKarma(0);
            setExp(0);
            allTargetClear();

            startDeleteTimer();
        }
    }

    public class RestMonitor extends TimerTask {
        @Override
        public void run() {
            setRest(false);
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private static Logger _log = Logger.getLogger(L1GuardianInstance.class
            .getName());

    private Random _random = new Random();

    private L1GuardianInstance _npc = this;

    private L1Character _lastattacker;

    private static final long REST_MILLISEC = 10000;

    private static final Timer _restTimer = new Timer(true);

    private RestMonitor _monitor;

    /**
     * @param template
     */
    public L1GuardianInstance(L1Npc template) {
        super(template);
    }

    public void doFinalAction(L1PcInstance player) {
    }

    @Override
    public void onAction(L1PcInstance player) {
        if (player.getType() == 2 && player.getCurrentWeapon() == 0
                && player.isElf()) {
            L1Attack attack = new L1Attack(player, this);

            if (attack.calcHit()) {
                if (getNpcTemplate().get_npcId() == 70848) { // エント
                    int chance = _random.nextInt(100) + 1;
                    if (chance <= 10) {
                        player.getInventory().storeItem(40506, 1);
                        player.sendPackets(new S_ServerMessage(143, "$755",
                                "$794")); // \f1%0が%1をくれました。
                    } else if (chance <= 60 && chance > 10) {
                        player.getInventory().storeItem(40507, 1);
                        player.sendPackets(new S_ServerMessage(143, "$755",
                                "$763")); // \f1%0が%1をくれました。
                    } else if (chance <= 70 && chance > 60) {
                        player.getInventory().storeItem(40505, 1);
                        player.sendPackets(new S_ServerMessage(143, "$755",
                                "$770")); // \f1%0が%1をくれました。
                    }
                }
                if (getNpcTemplate().get_npcId() == 70850) { // パン
                    int chance = _random.nextInt(100) + 1;
                    if (chance <= 30) {
                        player.getInventory().storeItem(40519, 5);
                        player.sendPackets(new S_ServerMessage(143, "$753",
                                "$760" + " (" + 5 + ")")); // \f1%0が%1をくれました。
                    }
                }
                if (getNpcTemplate().get_npcId() == 70846) { // アラクネ
                    int chance = _random.nextInt(100) + 1;
                    if (chance <= 30) {
                        player.getInventory().storeItem(40503, 1);
                        player.sendPackets(new S_ServerMessage(143, "$752",
                                "$769")); // \f1%0が%1をくれました。
                    }
                }
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addPcPoisonAttack(player, this);
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        } else if (getCurrentHp() > 0 && !isDead()) {
            L1Attack attack = new L1Attack(player, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addPcPoisonAttack(player, this);
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        }
    }

    @Override
    public void onFinalAction(L1PcInstance player, String action) {
    }

    @Override
    public void onNpcAI() {
        if (isAiRunning()) {
            return;
        }
        setActived(false);
        startAI();
    }

    @Override
    public void onTalkAction(L1PcInstance player) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(
                getNpcTemplate().get_npcId());
        L1Object object = L1World.getInstance().findObject(getId());
        L1NpcInstance target = (L1NpcInstance) object;
        String htmlid = null;
        String[] htmldata = null;

        if (talking != null) {
            int pcx = player.getX(); // PCのX座標
            int pcy = player.getY(); // PCのY座標
            int npcx = target.getX(); // NPCのX座標
            int npcy = target.getY(); // NPCのY座標

            if (pcx == npcx && pcy < npcy) {
                setHeading(0);
            } else if (pcx > npcx && pcy < npcy) {
                setHeading(1);
            } else if (pcx > npcx && pcy == npcy) {
                setHeading(2);
            } else if (pcx > npcx && pcy > npcy) {
                setHeading(3);
            } else if (pcx == npcx && pcy > npcy) {
                setHeading(4);
            } else if (pcx < npcx && pcy > npcy) {
                setHeading(5);
            } else if (pcx < npcx && pcy == npcy) {
                setHeading(6);
            } else if (pcx < npcx && pcy < npcy) {
                setHeading(7);
            }
            broadcastPacket(new S_ChangeHeading(this));

            // html表示パケット送信
            if (htmlid != null) { // htmlidが指定されている場合
                if (htmldata != null) { // html指定がある場合は表示
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid,
                            htmldata));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
                }
            } else {
                if (player.getLawful() < -1000) { // プレイヤーがカオティック
                    player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
                }
            }
            // 動かないようにする
            synchronized (this) {
                if (_monitor != null) {
                    _monitor.cancel();
                }
                setRest(true);
                _monitor = new RestMonitor();
                _restTimer.schedule(_monitor, REST_MILLISEC);
            }
        }
    }

    @Override
    public void receiveDamage(L1Character attacker, int damage) { // 攻撃でＨＰを減らすときはここを使用
        if (attacker instanceof L1PcInstance && damage > 0) {
            L1PcInstance pc = (L1PcInstance) attacker;
            if (pc.getType() == 2 && // 素手ならダメージなし
                    pc.getCurrentWeapon() == 0) {
            } else {
                if (getCurrentHp() > 0 && !isDead()) {
                    if (damage >= 0) {
                        setHate(attacker, damage);
                    }
                    if (damage > 0) {
                        removeSkillEffect(FOG_OF_SLEEPING);
                    }
                    onNpcAI();
                    // 仲間意識をもつモンスターのターゲットに設定
                    serchLink(pc, getNpcTemplate().get_family());
                    if (damage > 0) {
                        pc.setPetTarget(this);
                    }

                    int newHp = getCurrentHp() - damage;
                    if (newHp <= 0 && !isDead()) {
                        setCurrentHpDirect(0);
                        setDead(true);
                        setStatus(ActionCodes.ACTION_Die);
                        _lastattacker = attacker;
                        Death death = new Death();
                        GeneralThreadPool.getInstance().execute(death);
                    }
                    if (newHp > 0) {
                        setCurrentHp(newHp);
                    }
                } else if (!isDead()) { // 念のため
                    setDead(true);
                    setStatus(ActionCodes.ACTION_Die);
                    _lastattacker = attacker;
                    Death death = new Death();
                    GeneralThreadPool.getInstance().execute(death);
                }
            }
        }
    }

    @Override
    public void searchTarget() {
        // ターゲット検索
        L1PcInstance targetPlayer = null;

        for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
            if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm()
                    || pc.isGhost()) {
                continue;
            }
            if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // インビジチェック
                if (!pc.isElf()) { // エルフ以外
                    targetPlayer = pc;
                    wideBroadcastPacket(new S_NpcChatPacket(this, "$804", 2)); // エルフ以外の者よ、命が惜しければ早くここから去れ。ここは神聖な場所だ。
                    break;
                } else if (pc.isElf() && pc.isWantedForElf()) {
                    targetPlayer = pc;
                    wideBroadcastPacket(new S_NpcChatPacket(this, "$815", 1)); // 同族を殺したものは、己の血でその罪をあがなうことになるだろう。
                    break;
                }
            }
        }
        if (targetPlayer != null) {
            _hateList.add(targetPlayer, 0);
            _target = targetPlayer;
        }
    }

    @Override
    public void setCurrentHp(int i) {
        int currentHp = i;
        if (currentHp >= getMaxHp()) {
            currentHp = getMaxHp();
        }
        setCurrentHpDirect(currentHp);

        if (getMaxHp() > getCurrentHp()) {
            startHpRegeneration();
        }
    }

    @Override
    public void setCurrentMp(int i) {
        int currentMp = i;
        if (currentMp >= getMaxMp()) {
            currentMp = getMaxMp();
        }
        setCurrentMpDirect(currentMp);

        if (getMaxMp() > getCurrentMp()) {
            startMpRegeneration();
        }
    }

    // リンクの設定
    @Override
    public void setLink(L1Character cha) {
        if (cha != null && _hateList.isEmpty()) { // ターゲットがいない場合のみ追加
            _hateList.add(cha, 0);
            checkTarget();
        }
    }
}
