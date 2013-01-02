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
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.datatables.DropTable;
import l1j.jrwz.server.datatables.NPCTalkDataTable;
import l1j.jrwz.server.datatables.UBTable;
import l1j.jrwz.server.model.L1Attack;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.L1Location;
import l1j.jrwz.server.model.L1NpcTalkData;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1Teleport;
import l1j.jrwz.server.model.L1UltimateBattle;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_DoActionGFX;
import l1j.jrwz.server.serverpackets.S_NPCPack;
import l1j.jrwz.server.serverpackets.S_NPCTalkReturn;
import l1j.jrwz.server.serverpackets.S_RemoveObject;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.serverpackets.S_SkillBrave;
import l1j.jrwz.server.templates.L1Npc;
import l1j.jrwz.server.utils.CalcExp;

public class L1MonsterInstance extends L1NpcInstance {

    class Death implements Runnable {
        L1Character _lastAttacker;

        public Death(L1Character lastAttacker) {
            _lastAttacker = lastAttacker;
        }

        @Override
        public void run() {
            setDeathProcessing(true);
            setCurrentHpDirect(0);
            setDead(true);
            setStatus(ActionCodes.ACTION_Die);

            getMap().setPassable(getLocation(), true);

            broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));

            startChat(CHAT_TIMING_DEAD);

            distributeExpDropKarma(_lastAttacker);
            giveUbSeal();

            setDeathProcessing(false);

            setExp(0);
            setKarma(0);
            allTargetClear();

            startDeleteTimer();
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static Logger _log = Logger.getLogger(L1MonsterInstance.class
            .getName());

    private static Random _random = new Random();

    private static void openDoorInCrystalCave(int doorId) {
        for (L1Object object : L1World.getInstance().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getDoorId() == doorId) {
                    door.open();
                }
            }
        }
    }

    private static void openDoorWhenNpcDied(L1NpcInstance npc) {
        int[] npcId = { 46143, 46144, 46145, 46146, 46147, 46148, 46149, 46150,
                46151, 46152 };
        int[] doorId = { 5001, 5002, 5003, 5004, 5005, 5006, 5007, 5008, 5009,
                5010 };

        for (int i = 0; i < npcId.length; i++) {
            if (npc.getNpcTemplate().get_npcId() == npcId[i]) {
                openDoorInCrystalCave(doorId[i]);
            }
        }
    }

    private boolean _storeDroped; // ドロップアイテムの読込が完了したか

    // ターゲットを探す
    public static int[][] _classGfxId = { { 0, 1 }, { 48, 61 }, { 37, 138 },
            { 734, 1186 }, { 2786, 2796 } };

    private int _ubSealCount = 0; // UBで倒された時、参加者に与えられる勇者の証の個数

    private int _ubId = 0; // UBID

    public L1MonsterInstance(L1Npc template) {
        super(template);
        _storeDroped = false;
    }

    private void distributeDrop() {
        ArrayList<L1Character> dropTargetList = _dropHateList
                .toTargetArrayList();
        ArrayList<Integer> dropHateList = _dropHateList.toHateArrayList();
        try {
            int npcId = getNpcTemplate().get_npcId();
            if (npcId != 45640 || (npcId == 45640 && getTempCharGfx() == 2332)) {
                DropTable.getInstance().dropShare(L1MonsterInstance.this,
                        dropTargetList, dropHateList);
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void distributeExpDropKarma(L1Character lastAttacker) {
        if (lastAttacker == null) {
            return;
        }
        L1PcInstance pc = null;
        if (lastAttacker instanceof L1PcInstance) {
            pc = (L1PcInstance) lastAttacker;
        } else if (lastAttacker instanceof L1PetInstance) {
            pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
        } else if (lastAttacker instanceof L1SummonInstance) {
            pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
        }

        if (pc != null) {
            ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
            ArrayList<Integer> hateList = _hateList.toHateArrayList();
            int exp = getExp();
            CalcExp.calcExp(pc, getId(), targetList, hateList, exp);
            // 死亡した場合はドロップとカルマも分配、死亡せず変身した場合はEXPのみ
            if (isDead()) {
                distributeDrop();
                giveKarma(pc);
            }
        } else if (lastAttacker instanceof L1EffectInstance) { // FWが倒した場合
            ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
            ArrayList<Integer> hateList = _hateList.toHateArrayList();
            // ヘイトリストにキャラクターが存在する
            if (hateList.size() != 0) {
                // 最大ヘイトを持つキャラクターが倒したものとする
                int maxHate = 0;
                for (int i = hateList.size() - 1; i >= 0; i--) {
                    if (maxHate < hateList.get(i)) {
                        maxHate = (hateList.get(i));
                        lastAttacker = targetList.get(i);
                    }
                }
                if (lastAttacker instanceof L1PcInstance) {
                    pc = (L1PcInstance) lastAttacker;
                } else if (lastAttacker instanceof L1PetInstance) {
                    pc = (L1PcInstance) ((L1PetInstance) lastAttacker)
                            .getMaster();
                } else if (lastAttacker instanceof L1SummonInstance) {
                    pc = (L1PcInstance) ((L1SummonInstance) lastAttacker)
                            .getMaster();
                }
                if (pc != null) {
                    int exp = getExp();
                    CalcExp.calcExp(pc, getId(), targetList, hateList, exp);
                    // 死亡した場合はドロップとカルマも分配、死亡せず変身した場合はEXPのみ
                    if (isDead()) {
                        distributeDrop();
                        giveKarma(pc);
                    }
                }
            }
        }
    }

    public int getUbId() {
        return _ubId;
    }

    public int getUbSealCount() {
        return _ubSealCount;
    }

    private void giveKarma(L1PcInstance pc) {
        int karma = getKarma();
        if (karma != 0) {
            int karmaSign = Integer.signum(karma);
            int pcKarmaLevel = pc.getKarmaLevel();
            int pcKarmaLevelSign = Integer.signum(pcKarmaLevel);
            // カルマ背信行為は5倍
            if (pcKarmaLevelSign != 0 && karmaSign != pcKarmaLevelSign) {
                karma *= 5;
            }
            // カルマは止めを刺したプレイヤーに設定。ペットorサモンで倒した場合も入る。
            pc.addKarma((int) (karma * Config.RATE_KARMA));
        }
    }

    private void giveUbSeal() {
        if (getUbSealCount() != 0) { // UBの勇者の証
            L1UltimateBattle ub = UBTable.getInstance().getUb(getUbId());
            if (ub != null) {
                for (L1PcInstance pc : ub.getMembersArray()) {
                    if (pc != null && !pc.isDead() && !pc.isGhost()) {
                        L1ItemInstance item = pc.getInventory().storeItem(
                                41402, getUbSealCount());
                        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$403, item
                                .getLogName())); // %0を手に入れました。
                    }
                }
            }
        }
    }

    private void hide() {
        int npcid = getNpcTemplate().get_npcId();
        if (npcid == 45061 // カーズドスパルトイ
                || npcid == 45161 // スパルトイ
                || npcid == 45181 // スパルトイ
                || npcid == 45455) { // デッドリースパルトイ
            if (getMaxHp() / 3 > getCurrentHp()) {
                int rnd = _random.nextInt(10);
                if (1 > rnd) {
                    allTargetClear();
                    setHiddenStatus(HIDDEN_STATUS_SINK);
                    broadcastPacket(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Hide));
                    setStatus(13);
                    broadcastPacket(new S_NPCPack(this));
                }
            }
        } else if (npcid == 45682) { // アンタラス
            if (getMaxHp() / 3 > getCurrentHp()) {
                int rnd = _random.nextInt(50);
                if (1 > rnd) {
                    allTargetClear();
                    setHiddenStatus(HIDDEN_STATUS_SINK);
                    broadcastPacket(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_AntharasHide));
                    setStatus(20);
                    broadcastPacket(new S_NPCPack(this));
                }
            }
        } else if (npcid == 45067 // バレーハーピー
                || npcid == 45264 // ハーピー
                || npcid == 45452 // ハーピー
                || npcid == 45090 // バレーグリフォン
                || npcid == 45321 // グリフォン
                || npcid == 45445) { // グリフォン
            if (getMaxHp() / 3 > getCurrentHp()) {
                int rnd = _random.nextInt(10);
                if (1 > rnd) {
                    allTargetClear();
                    setHiddenStatus(HIDDEN_STATUS_FLY);
                    broadcastPacket(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Moveup));
                    setStatus(4);
                    broadcastPacket(new S_NPCPack(this));
                }
            }
        } else if (npcid == 45681) { // リンドビオル
            if (getMaxHp() / 3 > getCurrentHp()) {
                int rnd = _random.nextInt(50);
                if (1 > rnd) {
                    allTargetClear();
                    setHiddenStatus(HIDDEN_STATUS_FLY);
                    broadcastPacket(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Moveup));
                    setStatus(11);
                    broadcastPacket(new S_NPCPack(this));
                }
            }
        } else if (npcid == 46107 // テーベ マンドラゴラ(白)
                || npcid == 46108) { // テーベ マンドラゴラ(黒)
            if (getMaxHp() / 4 > getCurrentHp()) {
                int rnd = _random.nextInt(10);
                if (1 > rnd) {
                    allTargetClear();
                    setHiddenStatus(HIDDEN_STATUS_SINK);
                    broadcastPacket(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Hide));
                    setStatus(13);
                    broadcastPacket(new S_NPCPack(this));
                }
            }
        }
    }

    public void initHide() {
        // 出現直後の隠れる動作
        // 潜るMOBは一定の確率で地中に潜った状態に、
        // 飛ぶMOBは飛んだ状態にしておく
        int npcid = getNpcTemplate().get_npcId();
        if (npcid == 45061 // カーズドスパルトイ
                || npcid == 45161 // スパルトイ
                || npcid == 45181 // スパルトイ
                || npcid == 45455) { // デッドリースパルトイ
            int rnd = _random.nextInt(3);
            if (1 > rnd) {
                setHiddenStatus(HIDDEN_STATUS_SINK);
                setStatus(13);
            }
        } else if (npcid == 45045 // クレイゴーレム
                || npcid == 45126 // ストーンゴーレム
                || npcid == 45134 // ストーンゴーレム
                || npcid == 45281) { // ギランストーンゴーレム
            int rnd = _random.nextInt(3);
            if (1 > rnd) {
                setHiddenStatus(HIDDEN_STATUS_SINK);
                setStatus(4);
            }
        } else if (npcid == 45067 // バレーハーピー
                || npcid == 45264 // ハーピー
                || npcid == 45452 // ハーピー
                || npcid == 45090 // バレーグリフォン
                || npcid == 45321 // グリフォン
                || npcid == 45445) { // グリフォン
            setHiddenStatus(HIDDEN_STATUS_FLY);
            setStatus(4);
        } else if (npcid == 45681) { // リンドビオル
            setHiddenStatus(HIDDEN_STATUS_FLY);
            setStatus(11);
        } else if (npcid == 46107 // テーベ マンドラゴラ(白)
                || npcid == 46108) { // テーベ マンドラゴラ(黒)
            int rnd = _random.nextInt(3);
            if (1 > rnd) {
                setHiddenStatus(HIDDEN_STATUS_SINK);
                setStatus(13);
            }
        } else if (npcid >= 46125 && npcid <= 46128) {
            setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_ICE);
            setStatus(4);
        }
    }

    public void initHideForMinion(L1NpcInstance leader) {
        // グループに属するモンスターの出現直後の隠れる動作（リーダーと同じ動作にする）
        int npcid = getNpcTemplate().get_npcId();
        if (leader.getHiddenStatus() == HIDDEN_STATUS_SINK) {
            if (npcid == 45061 // カーズドスパルトイ
                    || npcid == 45161 // スパルトイ
                    || npcid == 45181 // スパルトイ
                    || npcid == 45455) { // デッドリースパルトイ
                setHiddenStatus(HIDDEN_STATUS_SINK);
                setStatus(13);
            } else if (npcid == 45045 // クレイゴーレム
                    || npcid == 45126 // ストーンゴーレム
                    || npcid == 45134 // ストーンゴーレム
                    || npcid == 45281) { // ギランストーンゴーレム
                setHiddenStatus(HIDDEN_STATUS_SINK);
                setStatus(4);
            } else if (npcid == 46107 // テーベ マンドラゴラ(白)
                    || npcid == 46108) { // テーベ マンドラゴラ(黒)
                setHiddenStatus(HIDDEN_STATUS_SINK);
                setStatus(13);
            }
        } else if (leader.getHiddenStatus() == HIDDEN_STATUS_FLY) {
            if (npcid == 45067 // バレーハーピー
                    || npcid == 45264 // ハーピー
                    || npcid == 45452 // ハーピー
                    || npcid == 45090 // バレーグリフォン
                    || npcid == 45321 // グリフォン
                    || npcid == 45445) { // グリフォン
                setHiddenStatus(HIDDEN_STATUS_FLY);
                setStatus(4);
            } else if (npcid == 45681) { // リンドビオル
                setHiddenStatus(HIDDEN_STATUS_FLY);
                setStatus(11);
            }
        } else if (npcid >= 46125 && npcid <= 46128) {
            setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_ICE);
            setStatus(4);
        }
    }

    public boolean is_storeDroped() {
        return _storeDroped;
    }

    @Override
    public void onAction(L1PcInstance pc) {
        if (getCurrentHp() > 0 && !isDead()) {
            L1Attack attack = new L1Attack(pc, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addPcPoisonAttack(pc, this);
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        }
    }

    // アイテム使用処理
    @Override
    public void onItemUse() {
        if (!isActived() && _target != null) {
            useItem(USEITEM_HASTE, 40); // ４０％の確率でヘイストポーション使用

            // アイテムじゃないけどドッペル処理
            if (getNpcTemplate().is_doppel() && _target instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance) _target;
                setName(_target.getName());
                setNameId(_target.getName());
                setTitle(_target.getTitle());
                setTempLawful(_target.getLawful());
                setTempCharGfx(targetPc.getClassId());
                setGfxId(targetPc.getClassId());
                setPassispeed(640);
                setAtkspeed(900); // 正確な値がわからん
                for (L1PcInstance pc : L1World.getInstance()
                        .getRecognizePlayer(this)) {
                    pc.sendPackets(new S_RemoveObject(this));
                    pc.removeKnownObject(this);
                    pc.updateObject();
                }
            }
        }
        if (getCurrentHp() * 100 / getMaxHp() < 40) { // ＨＰが４０％きったら
            useItem(USEITEM_HEAL, 50); // ５０％の確率で回復ポーション使用
        }
    }

    @Override
    public void onNpcAI() {
        if (isAiRunning()) {
            return;
        }
        if (!_storeDroped) // 無駄なオブジェクトＩＤを発行しないようにここでセット
        {
            DropTable.getInstance().setDrop(this, getInventory());
            getInventory().shuffle();
            _storeDroped = true;
        }
        setActived(false);
        startAI();
    }

    @Override
    public void onPerceive(L1PcInstance perceivedFrom) {
        perceivedFrom.addKnownObject(this);
        if (0 < getCurrentHp()) {
            if (getHiddenStatus() == HIDDEN_STATUS_SINK
                    || getHiddenStatus() == HIDDEN_STATUS_ICE) {
                perceivedFrom.sendPackets(new S_DoActionGFX(getId(),
                        ActionCodes.ACTION_Hide));
            } else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
                perceivedFrom.sendPackets(new S_DoActionGFX(getId(),
                        ActionCodes.ACTION_Moveup));
            }
            perceivedFrom.sendPackets(new S_NPCPack(this));
            onNpcAI(); // モンスターのＡＩを開始
            if (getBraveSpeed() == 1) { // ちゃんとした方法がわからない
                perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1, 600000));
            }
        } else {
            perceivedFrom.sendPackets(new S_NPCPack(this));
        }
    }

    @Override
    public void onTalkAction(L1PcInstance pc) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(
                getNpcTemplate().get_npcId());
        if (pc.getLawful() < -1000) { // プレイヤーがカオティック
            pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
        }
    }

    /**
     * 距離が5以上離れているpcを距離3～4の位置に引き寄せる。
     * 
     * @param pc
     */
    private void recall(L1PcInstance pc) {
        if (getMapId() != pc.getMapId()) {
            return;
        }
        if (getLocation().getTileLineDistance(pc.getLocation()) > 4) {
            for (int count = 0; count < 10; count++) {
                L1Location newLoc = getLocation().randomLocation(3, 4, false);
                if (glanceCheck(newLoc.getX(), newLoc.getY())) {
                    L1Teleport.teleport(pc, newLoc.getX(), newLoc.getY(),
                            getMapId(), 5, true);
                    break;
                }
            }
        }
    }

    @Override
    public void receiveDamage(L1Character attacker, int damage) { // 攻撃でＨＰを減らすときはここを使用
        if (getCurrentHp() > 0 && !isDead()) {
            if (getHiddenStatus() == HIDDEN_STATUS_SINK
                    || getHiddenStatus() == HIDDEN_STATUS_FLY) {
                return;
            }
            if (damage >= 0) {
                if (!(attacker instanceof L1EffectInstance)) { // FWはヘイトなし
                    setHate(attacker, damage);
                }
            }
            if (damage > 0) {
                removeSkillEffect(FOG_OF_SLEEPING);
            }

            onNpcAI();

            if (attacker instanceof L1PcInstance) { // 仲間意識をもつモンスターのターゲットに設定
                serchLink((L1PcInstance) attacker, getNpcTemplate()
                        .get_family());
            }

            if (attacker instanceof L1PcInstance && damage > 0) {
                L1PcInstance player = (L1PcInstance) attacker;
                player.setPetTarget(this);

                if (getNpcTemplate().get_npcId() == 45681 // リンドビオル
                        || getNpcTemplate().get_npcId() == 45682 // アンタラス
                        || getNpcTemplate().get_npcId() == 45683 // パプリオン
                        || getNpcTemplate().get_npcId() == 45684) // ヴァラカス
                {
                    recall(player);
                }
            }

            int newHp = getCurrentHp() - damage;
            if (newHp <= 0 && !isDead()) {
                int transformId = getNpcTemplate().getTransformId();
                // 変身しないモンスター
                if (transformId == -1) {
                    setCurrentHpDirect(0);
                    setDead(true);
                    setStatus(ActionCodes.ACTION_Die);
                    openDoorWhenNpcDied(this);
                    Death death = new Death(attacker);
                    GeneralThreadPool.getInstance().execute(death);
                    // Death(attacker);
                } else { // 変身するモンスター
                    // distributeExpDropKarma(attacker);
                    transform(transformId);
                }
            }
            if (newHp > 0) {
                setCurrentHp(newHp);
                hide();
            }
        } else if (!isDead()) { // 念のため
            setDead(true);
            setStatus(ActionCodes.ACTION_Die);
            Death death = new Death(attacker);
            GeneralThreadPool.getInstance().execute(death);
            // Death(attacker);
        }
    }

    @Override
    public void ReceiveManaDamage(L1Character attacker, int mpDamage) { // 攻撃でＭＰを減らすときはここを使用
        if (mpDamage > 0 && !isDead()) {
            // int Hate = mpDamage / 10 + 10; // 注意！計算適当 ダメージの１０分の１＋ヒットヘイト１０
            // setHate(attacker, Hate);
            setHate(attacker, mpDamage);

            onNpcAI();

            if (attacker instanceof L1PcInstance) { // 仲間意識をもつモンスターのターゲットに設定
                serchLink((L1PcInstance) attacker, getNpcTemplate()
                        .get_family());
            }

            int newMp = getCurrentMp() - mpDamage;
            if (newMp < 0) {
                newMp = 0;
            }
            setCurrentMp(newMp);
        }
    }

    @Override
    public void searchTarget() {
        // ターゲット捜索
        L1PcInstance targetPlayer = null;

        for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
            if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm()
                    || pc.isMonitor() || pc.isGhost()) {
                continue;
            }

            // 闘技場内は変身／未変身に限らず全てアクティブ
            int mapId = getMapId();
            if (mapId == 88 || mapId == 98 || mapId == 92 || mapId == 91
                    || mapId == 95) {
                if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // インビジチェック
                    targetPlayer = pc;
                    break;
                }
            }

            if (getNpcId() == 45600) { // カーツ
                if (pc.isCrown() || pc.isDarkelf()
                        || pc.getTempCharGfx() != pc.getClassId()) { // 未変身の君主、DEにはアクティブ
                    targetPlayer = pc;
                    break;
                }
            }

            // どちらかの条件を満たす場合、友好と見なされ先制攻撃されない。
            // ・モンスターのカルマがマイナス値（バルログ側モンスター）でPCのカルマレベルが1以上（バルログ友好）
            // ・モンスターのカルマがプラス値（ヤヒ側モンスター）でPCのカルマレベルが-1以下（ヤヒ友好）
            if ((getNpcTemplate().getKarma() < 0 && pc.getKarmaLevel() >= 1)
                    || (getNpcTemplate().getKarma() > 0 && pc.getKarmaLevel() <= -1)) {
                continue;
            }
            // 見棄てられた者たちの地 カルマクエストの変身中は、各陣営のモンスターから先制攻撃されない
            if (pc.getTempCharGfx() == 6034 && getNpcTemplate().getKarma() < 0
                    || pc.getTempCharGfx() == 6035
                    && getNpcTemplate().getKarma() > 0
                    || pc.getTempCharGfx() == 6035
                    && getNpcTemplate().get_npcId() == 46070
                    || pc.getTempCharGfx() == 6035
                    && getNpcTemplate().get_npcId() == 46072) {
                continue;
            }

            if (!getNpcTemplate().is_agro() && !getNpcTemplate().is_agrososc()
                    && getNpcTemplate().is_agrogfxid1() < 0
                    && getNpcTemplate().is_agrogfxid2() < 0) { // 完全なノンアクティブモンスター
                if (pc.getLawful() < -1000) { // プレイヤーがカオティック
                    targetPlayer = pc;
                    break;
                }
                continue;
            }

            if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) { // インビジチェック
                if (pc.hasSkillEffect(67)) { // 変身してる
                    if (getNpcTemplate().is_agrososc()) { // 変身に対してアクティブ
                        targetPlayer = pc;
                        break;
                    }
                } else if (getNpcTemplate().is_agro()) { // アクティブモンスター
                    targetPlayer = pc;
                    break;
                }

                // 特定のクラスorグラフィックＩＤにアクティブ
                if (getNpcTemplate().is_agrogfxid1() >= 0
                        && getNpcTemplate().is_agrogfxid1() <= 4) { // クラス指定
                    if (_classGfxId[getNpcTemplate().is_agrogfxid1()][0] == pc
                            .getTempCharGfx()
                            || _classGfxId[getNpcTemplate().is_agrogfxid1()][1] == pc
                                    .getTempCharGfx()) {
                        targetPlayer = pc;
                        break;
                    }
                } else if (pc.getTempCharGfx() == getNpcTemplate()
                        .is_agrogfxid1()) { // グラフィックＩＤ指定
                    targetPlayer = pc;
                    break;
                }

                if (getNpcTemplate().is_agrogfxid2() >= 0
                        && getNpcTemplate().is_agrogfxid2() <= 4) { // クラス指定
                    if (_classGfxId[getNpcTemplate().is_agrogfxid2()][0] == pc
                            .getTempCharGfx()
                            || _classGfxId[getNpcTemplate().is_agrogfxid2()][1] == pc
                                    .getTempCharGfx()) {
                        targetPlayer = pc;
                        break;
                    }
                } else if (pc.getTempCharGfx() == getNpcTemplate()
                        .is_agrogfxid2()) { // グラフィックＩＤ指定
                    targetPlayer = pc;
                    break;
                }
            }
        }
        if (targetPlayer != null) {
            _hateList.add(targetPlayer, 0);
            _target = targetPlayer;
        }
    }

    public void set_storeDroped(boolean flag) {
        _storeDroped = flag;
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

    public void setUbId(int i) {
        _ubId = i;
    }

    public void setUbSealCount(int i) {
        _ubSealCount = i;
    }

    @Override
    protected void transform(int transformId) {
        super.transform(transformId);

        // DROPの再設定
        getInventory().clearItems();
        DropTable.getInstance().setDrop(this, getInventory());
        getInventory().shuffle();
    }
}
