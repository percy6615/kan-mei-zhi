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

import static l1j.jrwz.server.model.skill.L1SkillId.BLIND_HIDING;
import static l1j.jrwz.server.model.skill.L1SkillId.BLOODLUST;
import static l1j.jrwz.server.model.skill.L1SkillId.CANCELLATION;
import static l1j.jrwz.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.jrwz.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static l1j.jrwz.server.model.skill.L1SkillId.DRESS_EVASION;
import static l1j.jrwz.server.model.skill.L1SkillId.ENTANGLE;
import static l1j.jrwz.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static l1j.jrwz.server.model.skill.L1SkillId.GMSTATUS_FINDINVIS;
import static l1j.jrwz.server.model.skill.L1SkillId.GMSTATUS_HPBAR;
import static l1j.jrwz.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.jrwz.server.model.skill.L1SkillId.HASTE;
import static l1j.jrwz.server.model.skill.L1SkillId.HOLY_WALK;
import static l1j.jrwz.server.model.skill.L1SkillId.ILLUSION_AVATAR;
import static l1j.jrwz.server.model.skill.L1SkillId.INVISIBILITY;
import static l1j.jrwz.server.model.skill.L1SkillId.JOY_OF_PAIN;
import static l1j.jrwz.server.model.skill.L1SkillId.MASS_SLOW;
import static l1j.jrwz.server.model.skill.L1SkillId.MORTAL_BODY;
import static l1j.jrwz.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static l1j.jrwz.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.jrwz.server.model.skill.L1SkillId.SLOW;
import static l1j.jrwz.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_RIBRAVE;
import static l1j.jrwz.server.model.skill.L1SkillId.STRIKER_GALE;
import static l1j.jrwz.server.model.skill.L1SkillId.WIND_WALK;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.PacketOutput;
import l1j.jrwz.server.WarTimeController;
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.command.executor.L1HpBar;
import l1j.jrwz.server.datatables.CharacterTable;
import l1j.jrwz.server.datatables.ExpTable;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.AcceleratorChecker;
import l1j.jrwz.server.model.HpRegeneration;
import l1j.jrwz.server.model.L1Attack;
import l1j.jrwz.server.model.L1CastleLocation;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.L1ChatParty;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1DwarfForElfInventory;
import l1j.jrwz.server.model.L1DwarfInventory;
import l1j.jrwz.server.model.L1EquipmentSlot;
import l1j.jrwz.server.model.L1ExcludingList;
import l1j.jrwz.server.model.L1Inventory;
import l1j.jrwz.server.model.L1Karma;
import l1j.jrwz.server.model.L1Magic;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1Party;
import l1j.jrwz.server.model.L1PcDeleteTimer;
import l1j.jrwz.server.model.L1PcInventory;
import l1j.jrwz.server.model.L1PinkName;
import l1j.jrwz.server.model.L1Quest;
import l1j.jrwz.server.model.L1Teleport;
import l1j.jrwz.server.model.L1TownLocation;
import l1j.jrwz.server.model.L1War;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.MpReductionByAwake;
import l1j.jrwz.server.model.MpRegeneration;
import l1j.jrwz.server.model.MpRegenerationByDoll;
import l1j.jrwz.server.model.classes.L1ClassFeature;
import l1j.jrwz.server.model.gametime.L1GameTimeCarrier;
import l1j.jrwz.server.model.monitor.L1PcAutoUpdate;
import l1j.jrwz.server.model.monitor.L1PcExpMonitor;
import l1j.jrwz.server.model.monitor.L1PcGhostMonitor;
import l1j.jrwz.server.model.monitor.L1PcHellMonitor;
import l1j.jrwz.server.model.monitor.L1PcInvisDelay;
import l1j.jrwz.server.model.skill.L1SkillUse;
import l1j.jrwz.server.serverpackets.S_BlueMessage;
import l1j.jrwz.server.serverpackets.S_CastleMaster;
import l1j.jrwz.server.serverpackets.S_ChangeShape;
import l1j.jrwz.server.serverpackets.S_Disconnect;
import l1j.jrwz.server.serverpackets.S_DoActionGFX;
import l1j.jrwz.server.serverpackets.S_DoActionShop;
import l1j.jrwz.server.serverpackets.S_Exp;
import l1j.jrwz.server.serverpackets.S_HPMeter;
import l1j.jrwz.server.serverpackets.S_HPUpdate;
import l1j.jrwz.server.serverpackets.S_Invis;
import l1j.jrwz.server.serverpackets.S_Lawful;
import l1j.jrwz.server.serverpackets.S_Liquor;
import l1j.jrwz.server.serverpackets.S_MPUpdate;
import l1j.jrwz.server.serverpackets.S_OtherCharPacks;
import l1j.jrwz.server.serverpackets.S_OwnCharStatus;
import l1j.jrwz.server.serverpackets.S_PacketBox;
import l1j.jrwz.server.serverpackets.S_Poison;
import l1j.jrwz.server.serverpackets.S_RemoveObject;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.serverpackets.S_SkillIconGFX;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.serverpackets.S_bonusstats;
import l1j.jrwz.server.serverpackets.ServerBasePacket;
import l1j.jrwz.server.templates.L1BookMark;
import l1j.jrwz.server.templates.L1Item;
import l1j.jrwz.server.templates.L1PrivateShopBuyList;
import l1j.jrwz.server.templates.L1PrivateShopSellList;
import l1j.jrwz.server.utils.CalcStat;

// Referenced classes of package l1j.jrwz.server.model:
// L1Character, L1DropTable, L1Object, L1ItemInstance,
// L1World
//

public class L1PcInstance extends L1Character {
    private class Death implements Runnable {
        L1Character _lastAttacker;

        Death(L1Character cha) {
            _lastAttacker = cha;
        }

        @Override
        public void run() {
            L1Character lastAttacker = _lastAttacker;
            _lastAttacker = null;
            setCurrentHp(0);
            setGresValid(false); // EXPロストするまでG-RES無効

            while (isTeleport()) { // テレポート中なら終わるまで待つ
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
            }

            stopHpRegeneration();
            stopMpRegeneration();

            int targetobjid = getId();
            getMap().setPassable(getLocation(), true);

            // エンチャントを解除する
            // 変身状態も解除されるため、キャンセレーションをかけてから変身状態に戻す
            int tempchargfx = 0;
            if (hasSkillEffect(SHAPE_CHANGE)) {
                tempchargfx = getTempCharGfx();
                setTempCharGfxAtDead(tempchargfx);
            } else {
                setTempCharGfxAtDead(getClassId());
            }

            // キャンセレーションをエフェクトなしでかける
            L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(L1PcInstance.this, CANCELLATION, getId(),
                    getX(), getY(), null, 0, L1SkillUse.TYPE_LOGIN);

            // シャドウ系変身中に死亡するとクライアントが落ちるため暫定対応
            if (tempchargfx == 5727 || tempchargfx == 5730
                    || tempchargfx == 5733 || tempchargfx == 5736) {
                tempchargfx = 0;
            }
            if (tempchargfx != 0) {
                sendPackets(new S_ChangeShape(getId(), tempchargfx));
                broadcastPacket(new S_ChangeShape(getId(), tempchargfx));
            } else {
                // シャドウ系変身中に攻撃しながら死亡するとクライアントが落ちるためディレイを入れる
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }

            sendPackets(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));
            broadcastPacket(new S_DoActionGFX(targetobjid,
                    ActionCodes.ACTION_Die));

            if (lastAttacker != L1PcInstance.this) {
                // セーフティーゾーン、コンバットゾーンで最後に殺したキャラが
                // プレイヤーorペットだったら、ペナルティなし
                if (getZoneType() != 0) {
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
                        // 戦争中に戦争エリアに居る場合は例外
                        if (!isInWarAreaAndWarTime(L1PcInstance.this, player)) {
                            return;
                        }
                    }
                }

                boolean sim_ret = simWarResult(lastAttacker); // 模擬戦
                if (sim_ret == true) { // 模擬戦中ならペナルティなし
                    return;
                }
            }

            if (!getMap().isEnabledDeathPenalty()) {
                return;
            }

            // 決闘中ならペナルティなし
            L1PcInstance fightPc = null;
            if (lastAttacker instanceof L1PcInstance) {
                fightPc = (L1PcInstance) lastAttacker;
            }
            if (fightPc != null) {
                if (getFightId() == fightPc.getId()
                        && fightPc.getFightId() == getId()) { // 決闘中
                    setFightId(0);
                    sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
                    fightPc.setFightId(0);
                    fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
                            0, 0));
                    return;
                }
            }

            deathPenalty(); // EXPロスト

            setGresValid(true); // EXPロストしたらG-RES有効

            if (getExpRes() == 0) {
                setExpRes(1);
            }

            // ガードに殺された場合のみ、PKカウントを減らしガードに攻撃されなくなる
            if (lastAttacker instanceof L1GuardInstance) {
                if (get_PKcount() > 0) {
                    set_PKcount(get_PKcount() - 1);
                }
                setLastPk(null);
            }
            if (lastAttacker instanceof L1GuardianInstance) {
                if (getPkCountForElf() > 0) {
                    setPkCountForElf(getPkCountForElf() - 1);
                }
                setLastPkForElf(null);
            }

            // 一定の確率でアイテムをDROP
            // アライメント32000以上で0%、以降-1000毎に0.4%
            // アライメントが0未満の場合は-1000毎に0.8%
            // アライメント-32000以下で最高51.2%のDROP率
            int lostRate = (int) (((getLawful() + 32768D) / 1000D - 65D) * 4D);
            if (lostRate < 0) {
                lostRate *= -1;
                if (getLawful() < 0) {
                    lostRate *= 2;
                }
                int rnd = _random.nextInt(1000) + 1;
                if (rnd <= lostRate) {
                    int count = 1;
                    if (getLawful() <= -30000) {
                        count = _random.nextInt(4) + 1;
                    } else if (getLawful() <= -20000) {
                        count = _random.nextInt(3) + 1;
                    } else if (getLawful() <= -10000) {
                        count = _random.nextInt(2) + 1;
                    } else if (getLawful() < 0) {
                        count = _random.nextInt(1) + 1;
                    }
                    caoPenaltyResult(count);
                }
            }

            boolean castle_ret = castleWarResult(); // 攻城戦
            if (castle_ret == true) { // 攻城戦中で旗内なら赤ネームペナルティなし
                return;
            }

            // 最後に殺したキャラがプレイヤーだったら、赤ネームにする
            L1PcInstance player = null;
            if (lastAttacker instanceof L1PcInstance) {
                player = (L1PcInstance) lastAttacker;
            }
            if (player != null) {
                if (getLawful() >= 0 && isPinkName() == false) {
                    boolean isChangePkCount = false;
                    boolean isChangePkCountForElf = false;
                    // アライメントが30000未満の場合はPKカウント増加
                    if (player.getLawful() < 30000) {
                        player.set_PKcount(player.get_PKcount() + 1);
                        isChangePkCount = true;
                        if (player.isElf() && isElf()) {
                            player.setPkCountForElf(player.getPkCountForElf() + 1);
                            isChangePkCountForElf = true;
                        }
                    }
                    player.setLastPk();
                    if (player.isElf() && isElf()) {
                        player.setLastPkForElf();
                    }

                    // アライメント処理
                    // 公式の発表および各LVでのPKからつじつまの合うように変更
                    // （PK側のLVに依存し、高LVほどリスクも高い）
                    // 48あたりで-8kほど DKの時点で10k強
                    // 60で約20k強 65で30k弱
                    int lawful;

                    if (player.getLevel() < 50) {
                        lawful = -1
                                * (int) ((Math.pow(player.getLevel(), 2) * 4));
                    } else {
                        lawful = -1
                                * (int) ((Math.pow(player.getLevel(), 3) * 0.08));
                    }
                    // もし(元々のアライメント-1000)が計算後より低い場合
                    // 元々のアライメント-1000をアライメント値とする
                    // （連続でPKしたときにほとんど値が変わらなかった記憶より）
                    // これは上の式よりも自信度が低いうろ覚えですので
                    // 明らかにこうならない！という場合は修正お願いします
                    if ((player.getLawful() - 1000) < lawful) {
                        lawful = player.getLawful() - 1000;
                    }

                    if (lawful <= -32768) {
                        lawful = -32768;
                    }
                    player.setLawful(lawful);

                    S_Lawful s_lawful = new S_Lawful(player.getId(),
                            player.getLawful());
                    player.sendPackets(s_lawful);
                    player.broadcastPacket(s_lawful);

                    if (isChangePkCount && player.get_PKcount() >= 5
                            && player.get_PKcount() < 10) {
                        // あなたのPK回数が%0になりました。回数が%1になると地獄行きです。
                        player.sendPackets(new S_BlueMessage(551, String
                                .valueOf(player.get_PKcount()), "10"));
                    } else if (isChangePkCount && player.get_PKcount() >= 10) {
                        player.beginHell(true);
                    }
                } else {
                    setPinkName(false);
                }
            }
            _pcDeleteTimer = new L1PcDeleteTimer(L1PcInstance.this);
            _pcDeleteTimer.begin();
        }
    }

    private static final long serialVersionUID = 1L;
    public static final int CLASSID_KNIGHT_MALE = 61;
    public static final int CLASSID_KNIGHT_FEMALE = 48;
    public static final int CLASSID_ELF_MALE = 138;
    public static final int CLASSID_ELF_FEMALE = 37;
    public static final int CLASSID_WIZARD_MALE = 734;
    public static final int CLASSID_WIZARD_FEMALE = 1186;
    public static final int CLASSID_DARK_ELF_MALE = 2786;
    public static final int CLASSID_DARK_ELF_FEMALE = 2796;
    public static final int CLASSID_PRINCE = 0;
    public static final int CLASSID_PRINCESS = 1;
    public static final int CLASSID_DRAGON_KNIGHT_MALE = 6658;
    public static final int CLASSID_DRAGON_KNIGHT_FEMALE = 6661;
    public static final int CLASSID_ILLUSIONIST_MALE = 6671;

    public static final int CLASSID_ILLUSIONIST_FEMALE = 6650;

    private static Random _random = new Random();
    private short _hpr = 0;

    private short _trueHpr = 0;

    private short _mpr = 0;

    private short _trueMpr = 0;
    public short _originalHpr = 0; // ● オリジナルCON HPR

    public short _originalMpr = 0; // ● オリジナルWIS MPR

    private static final long INTERVAL_AUTO_UPDATE = 300;

    private ScheduledFuture<?> _autoUpdateFuture;

    private static final long INTERVAL_EXP_MONITOR = 500;

    private ScheduledFuture<?> _expMonitorFuture;

    private final ArrayList<Integer> skillList = new ArrayList<Integer>();

    private L1ClassFeature _classFeature = null;

    private int _PKcount; // ● PKカウント

    private int _PkCountForElf; // ● PKカウント(エルフ用)

    private int _clanid; // ● クランＩＤ

    private String clanname; // ● クラン名

    private int _clanRank; // ● クラン内のランク(血盟君主、ガーディアン、一般、見習い)

    private byte _sex; // ● 性別

    private final ArrayList<L1PrivateShopSellList> _sellList = new ArrayList<L1PrivateShopSellList>();

    private final ArrayList<L1PrivateShopBuyList> _buyList = new ArrayList<L1PrivateShopBuyList>();

    private byte[] _shopChat;

    private boolean _isPrivateShop = false;
    private boolean _isTradingInPrivateShop = false;

    private int _partnersPrivateShopItemCount = 0; // 閲覧中の個人商店のアイテム数
    private PacketOutput _out;

    public long _oldTime = 0; // 連続魔法ダメージの軽減に使用する

    private int _originalEr = 0; // ● オリジナルDEX ER補正

    private static Logger _log = Logger.getLogger(L1PcInstance.class.getName());

    private ClientThread _netConnection;

    private int _classId;

    private int _type;

    private int _exp;

    private final L1Karma _karma = new L1Karma();

    private boolean _gm;

    private boolean _monitor;

    private boolean _gmInvis;

    private short _accessLevel;

    private int _currentWeapon;

    private final L1PcInventory _inventory;

    private final L1DwarfInventory _dwarf;

    private final L1DwarfForElfInventory _dwarfForElf;

    private final L1Inventory _tradewindow;

    private L1ItemInstance _weapon;

    private L1Party _party;

    private L1ChatParty _chatParty;

    private int _partyID;

    private int _tradeID;

    private boolean _tradeOk;

    private int _tempID;

    private boolean _isTeleport = false;

    private boolean _isDrink = false;

    private boolean _isGres = false;

    private boolean _isPinkName = false;

    private final ArrayList<L1BookMark> _bookmarks;

    private final L1Quest _quest;

    private MpRegeneration _mpRegen;

    private MpRegenerationByDoll _mpRegenByDoll;

    private MpReductionByAwake _mpReductionByAwake;

    private HpRegeneration _hpRegen;

    private static Timer _regenTimer = new Timer(true);

    private boolean _mpRegenActive;

    private boolean _mpRegenActiveByDoll;

    private boolean _mpReductionActiveByAwake;

    private boolean _hpRegenActive;

    private final L1EquipmentSlot _equipSlot;

    private L1PcDeleteTimer _pcDeleteTimer;

    private String _accountName; // ● アカウントネーム

    private short _baseMaxHp = 0; // ● ＭＡＸＨＰベース（1～32767）

    private short _baseMaxMp = 0; // ● ＭＡＸＭＰベース（0～32767）

    private int _baseAc = 0; // ● ＡＣベース（-128～127）

    private int _originalAc = 0; // ● オリジナルDEX ＡＣ補正

    private byte _baseStr = 0; // ● ＳＴＲベース（1～127）

    private byte _baseCon = 0; // ● ＣＯＮベース（1～127）

    private byte _baseDex = 0; // ● ＤＥＸベース（1～127）

    private byte _baseCha = 0; // ● ＣＨＡベース（1～127）

    private byte _baseInt = 0; // ● ＩＮＴベース（1～127）

    private byte _baseWis = 0; // ● ＷＩＳベース（1～127）

    private int _originalStr = 0; // ● オリジナル STR

    private int _originalCon = 0; // ● オリジナル CON

    private int _originalDex = 0; // ● オリジナル DEX

    private int _originalCha = 0; // ● オリジナル CHA

    private int _originalInt = 0; // ● オリジナル INT

    private int _originalWis = 0; // ● オリジナル WIS

    private int _originalDmgup = 0; // ● オリジナルSTR ダメージ補正

    private int _originalBowDmgup = 0; // ● オリジナルDEX 弓ダメージ補正

    private int _originalHitup = 0; // ● オリジナルSTR 命中補正

    private int _originalBowHitup = 0; // ● オリジナルDEX 命中補正

    private int _originalMr = 0; // ● オリジナルWIS 魔法防御

    private int _originalMagicHit = 0; // ● オリジナルINT 魔法命中

    private int _originalMagicCritical = 0; // ● オリジナルINT 魔法クリティカル

    private int _originalMagicConsumeReduction = 0; // ● オリジナルINT 消費MP軽減

    private int _originalMagicDamage = 0; // ● オリジナルINT 魔法ダメージ

    private int _originalHpup = 0; // ● オリジナルCON HP上昇値補正

    private int _originalMpup = 0; // ● オリジナルWIS MP上昇値補正

    private int _baseDmgup = 0; // ● ダメージ補正ベース（-128～127）

    private int _baseBowDmgup = 0; // ● 弓ダメージ補正ベース（-128～127）

    private int _baseHitup = 0; // ● 命中補正ベース（-128～127）

    private int _baseBowHitup = 0; // ● 弓命中補正ベース（-128～127）

    private int _baseMr = 0; // ● 魔法防御ベース（0～）

    private int _advenHp; // ● // アドバンスド スピリッツで増加しているＨＰ

    private int _advenMp; // ● // アドバンスド スピリッツで増加しているＭＰ

    private int _highLevel; // ● 過去最高レベル

    private int _bonusStats; // ● 割り振ったボーナスステータス

    private int _elixirStats; // ● エリクサーで上がったステータス

    private int _elfAttr; // ● エルフの属性

    private int _expRes; // ● EXP復旧

    private int _partnerId; // ● 結婚相手

    private int _onlineStatus; // ● オンライン状態

    private int _homeTownId; // ● ホームタウン

    private int _contribution; // ● 貢献度

    // 地獄に滞在する時間（秒）
    private int _hellTime;

    private boolean _banned; // ● 凍結

    private int _food; // ● 満腹度

    public static final int REGENSTATE_NONE = 4;

    public static final int REGENSTATE_MOVE = 2;

    public static final int REGENSTATE_ATTACK = 1;

    public static L1PcInstance load(String charName) {
        L1PcInstance result = null;
        try {
            result = CharacterTable.getInstance().loadCharacter(charName);
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return result;
    }

    private int invisDelayCounter = 0;

    private final Object _invisTimerMonitor = new Object();

    private static final long DELAY_INVIS = 3000L;

    private boolean _ghost = false; // ゴースト

    private boolean _ghostCanTalk = true; // NPCに話しかけられるか

    private boolean _isReserveGhost = false; // ゴースト解除準備

    private ScheduledFuture<?> _ghostFuture;

    private int _ghostSaveLocX = 0;

    private int _ghostSaveLocY = 0;

    private short _ghostSaveMapId = 0;

    private int _ghostSaveHeading = 0;

    private ScheduledFuture<?> _hellFuture;

    private Timestamp _lastPk;

    private Timestamp _lastPkForElf;

    private Timestamp _deleteTime; // キャラクター削除までの時間

    private int _weightReduction = 0;

    private int _originalStrWeightReduction = 0; // ● オリジナルSTR 重量軽減

    private int _originalConWeightReduction = 0; // ● オリジナルCON 重量軽減

    private int _hasteItemEquipped = 0;

    private int _damageReductionByArmor = 0; // 防具によるダメージ軽減

    private int _hitModifierByArmor = 0; // 防具による命中率補正

    private int _dmgModifierByArmor = 0; // 防具によるダメージ補正

    private int _bowHitModifierByArmor = 0; // 防具による弓の命中率補正

    private int _bowDmgModifierByArmor = 0; // 防具による弓のダメージ補正

    private boolean _gresValid; // G-RESが有効か

    private long _fishingTime = 0;

    private boolean _isFishing = false;

    private boolean _isFishingReady = false;

    private int _cookingId = 0;

    private int _dessertId = 0;

    private final L1ExcludingList _excludingList = new L1ExcludingList();

    // -- 加速器検知機能 --
    private final AcceleratorChecker _acceleratorChecker = new AcceleratorChecker(
            this);

    /**
     * テレポート先の座標
     */
    private int _teleportX = 0;

    private int _teleportY = 0;

    private int _teleportMapId = 0;

    private int _teleportHeading = 0;

    private int _tempCharGfxAtDead;

    private boolean _isCanWhisper = true;

    private boolean _isShowTradeChat = true;

    private boolean _isShowWorldChat = true;

    private int _fightId;

    private byte _chatCount = 0;

    private long _oldChatTimeInMillis = 0L;

    private int _callClanId;

    private int _callClanHeading;

    private boolean _isInCharReset = false;

    private int _tempLevel = 1;

    private int _tempMaxLevel = 1;
    private int _awakeSkillId = 0;
    private boolean _isSummonMonster = false;
    private boolean _isShapeChange = false;

    public L1PcInstance() {
        _accessLevel = 0;
        _currentWeapon = 0;
        _inventory = new L1PcInventory(this);
        _dwarf = new L1DwarfInventory(this);
        _dwarfForElf = new L1DwarfForElfInventory(this);
        _tradewindow = new L1Inventory();
        _bookmarks = new ArrayList<L1BookMark>();
        _quest = new L1Quest(this);
        _equipSlot = new L1EquipmentSlot(this); // コンストラクタでthisポインタを渡すのは安全だろうか・・・
    }

    public void addBaseCha(byte i) {
        i += _baseCha;
        if (i >= 127) {
            i = 127;
        } else if (i < 1) {
            i = 1;
        }
        addCha((byte) (i - _baseCha));
        _baseCha = i;
    }

    public void addBaseCon(byte i) {
        i += _baseCon;
        if (i >= 127) {
            i = 127;
        } else if (i < 1) {
            i = 1;
        }
        addCon((byte) (i - _baseCon));
        _baseCon = i;
    }

    public void addBaseDex(byte i) {
        i += _baseDex;
        if (i >= 127) {
            i = 127;
        } else if (i < 1) {
            i = 1;
        }
        addDex((byte) (i - _baseDex));
        _baseDex = i;
    }

    public void addBaseInt(byte i) {
        i += _baseInt;
        if (i >= 127) {
            i = 127;
        } else if (i < 1) {
            i = 1;
        }
        addInt((byte) (i - _baseInt));
        _baseInt = i;
    }

    public void addBaseMaxHp(short i) {
        i += _baseMaxHp;
        if (i >= 32767) {
            i = 32767;
        } else if (i < 1) {
            i = 1;
        }
        addMaxHp(i - _baseMaxHp);
        _baseMaxHp = i;
    }

    public void addBaseMaxMp(short i) {
        i += _baseMaxMp;
        if (i >= 32767) {
            i = 32767;
        } else if (i < 0) {
            i = 0;
        }
        addMaxMp(i - _baseMaxMp);
        _baseMaxMp = i;
    }

    public void addBaseStr(byte i) {
        i += _baseStr;
        if (i >= 127) {
            i = 127;
        } else if (i < 1) {
            i = 1;
        }
        addStr((byte) (i - _baseStr));
        _baseStr = i;
    }

    public void addBaseWis(byte i) {
        i += _baseWis;
        if (i >= 127) {
            i = 127;
        } else if (i < 1) {
            i = 1;
        }
        addWis((byte) (i - _baseWis));
        _baseWis = i;
    }

    public void addBookMark(L1BookMark book) {
        _bookmarks.add(book);
    }

    public void addBowDmgModifierByArmor(int i) {
        _bowDmgModifierByArmor += i;
    }

    public void addBowHitModifierByArmor(int i) {
        _bowHitModifierByArmor += i;
    }

    public synchronized void addContribution(int contribution) {
        _contribution += contribution;
    }

    public void addDamageReductionByArmor(int i) {
        _damageReductionByArmor += i;
    }

    public void addDmgModifierByArmor(int i) {
        _dmgModifierByArmor += i;
    }

    public synchronized void addExp(int exp) {
        _exp += exp;
        if (_exp > ExpTable.MAX_EXP) {
            _exp = ExpTable.MAX_EXP;
        }
    }

    public void addHasteItemEquipped(int i) {
        _hasteItemEquipped += i;
    }

    public void addHitModifierByArmor(int i) {
        _hitModifierByArmor += i;
    }

    public void addHpr(int i) {
        _trueHpr += i;
        _hpr = (short) Math.max(0, _trueHpr);
    }

    public void addInvisDelayCounter(int counter) {
        synchronized (_invisTimerMonitor) {
            invisDelayCounter += counter;
        }
    }

    public void addKarma(int i) {
        synchronized (_karma) {
            _karma.add(i);
        }
    }

    public void addMpr(int i) {
        _trueMpr += i;
        _mpr = (short) Math.max(0, _trueMpr);
    }

    public void addWeightReduction(int i) {
        _weightReduction += i;
    }

    public void beginExpMonitor() {
        _expMonitorFuture = GeneralThreadPool.getInstance()
                .pcScheduleAtFixedRate(new L1PcExpMonitor(getId()), 0L,
                        INTERVAL_EXP_MONITOR);
    }

    public void beginGameTimeCarrier() {
        new L1GameTimeCarrier(this).start();
    }

    public void beginGhost(int locx, int locy, int mapid, boolean canTalk) {
        beginGhost(locx, locy, mapid, canTalk, 0);
    }

    public void beginGhost(int locx, int locy, int mapid, boolean canTalk,
            int sec) {
        if (isGhost()) {
            return;
        }
        setGhost(true);
        _ghostSaveLocX = getX();
        _ghostSaveLocY = getY();
        _ghostSaveMapId = getMapId();
        _ghostSaveHeading = getHeading();
        setGhostCanTalk(canTalk);
        L1Teleport.teleport(this, locx, locy, mapid, 5, true);
        if (sec > 0) {
            _ghostFuture = GeneralThreadPool.getInstance().pcSchedule(
                    new L1PcGhostMonitor(getId()), sec * 1000);
        }
    }

    public void beginHell(boolean isFirst) {
        // 地獄以外に居るときは地獄へ強制移動
        if (getMapId() != 666) {
            int locx = 32701;
            int locy = 32777;
            int mapid = 666;
            L1Teleport.teleport(this, locx, locy, mapid, 5, false);
        }

        if (isFirst) {
            if (get_PKcount() <= 10) {
                setHellTime(300);
            } else {
                setHellTime(300 * (get_PKcount() - 10) + 300);
            }
            // あなたのPK回数が%0になり、地獄に落とされました。あなたはここで%1分間反省しなければなりません。
            sendPackets(new S_BlueMessage(552, String.valueOf(get_PKcount()),
                    String.valueOf(getHellTime() / 60)));
        } else {
            // あなたは%0秒間ここにとどまらなければなりません。
            sendPackets(new S_BlueMessage(637, String.valueOf(getHellTime())));
        }
        if (_hellFuture == null) {
            _hellFuture = GeneralThreadPool.getInstance()
                    .pcScheduleAtFixedRate(new L1PcHellMonitor(getId()), 0L,
                            1000L);
        }
    }

    public void beginInvisTimer() {
        addInvisDelayCounter(1);
        GeneralThreadPool.getInstance().pcSchedule(new L1PcInvisDelay(getId()),
                DELAY_INVIS);
    }

    private void caoPenaltyResult(int count) {
        for (int i = 0; i < count; i++) {
            L1ItemInstance item = getInventory().CaoPenalty();

            if (item != null) {
                getInventory().tradeItem(
                        item,
                        item.isStackable() ? item.getCount() : 1,
                        L1World.getInstance().getInventory(getX(), getY(),
                                getMapId()));
                sendPackets(new S_ServerMessage(638, item.getLogName())); // %0を失いました。
            } else {
            }
        }
    }

    public boolean castleWarResult() {
        if (getClanid() != 0 && isCrown()) { // クラン所属中プリのチェック
            L1Clan clan = L1World.getInstance().getClan(getClanname());
            // 全戦争リストを取得
            for (L1War war : L1World.getInstance().getWarList()) {
                int warType = war.GetWarType();
                boolean isInWar = war.CheckClanInWar(getClanname());
                boolean isAttackClan = war.CheckAttackClan(getClanname());
                if (getId() == clan.getLeaderId() && // 血盟主で攻撃側で攻城戦中
                        warType == 1 && isInWar && isAttackClan) {
                    String enemyClanName = war.GetEnemyClanName(getClanname());
                    if (enemyClanName != null) {
                        war.CeaseWar(getClanname(), enemyClanName); // 終結
                    }
                    break;
                }
            }
        }

        int castleId = 0;
        boolean isNowWar = false;
        castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0) { // 旗内に居る
            isNowWar = WarTimeController.getInstance().isNowWar(castleId);
        }
        return isNowWar;
    }

    public void checkChatInterval() {
        long nowChatTimeInMillis = System.currentTimeMillis();
        if (_chatCount == 0) {
            _chatCount++;
            _oldChatTimeInMillis = nowChatTimeInMillis;
            return;
        }

        long chatInterval = nowChatTimeInMillis - _oldChatTimeInMillis;
        if (chatInterval > 2000) {
            _chatCount = 0;
            _oldChatTimeInMillis = 0;
        } else {
            if (_chatCount >= 3) {
                setSkillEffect(STATUS_CHAT_PROHIBITED, 120 * 1000);
                sendPackets(new S_SkillIconGFX(36, 120));
                sendPackets(new S_ServerMessage(153)); // \f3迷惑なチャット流しをしたので、今後2分間チャットを行うことはできません。
                _chatCount = 0;
                _oldChatTimeInMillis = 0;
            }
            _chatCount++;
        }
    }

    public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
        L1PcInstance targetpc = null;
        if (target instanceof L1PcInstance) {
            targetpc = (L1PcInstance) target;
        } else if (target instanceof L1PetInstance) {
            targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();
        } else if (target instanceof L1SummonInstance) {
            targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
        }
        if (targetpc == null) {
            return false; // 相手がPC、サモン、ペット以外
        }
        if (!Config.ALT_NONPVP) { // Non-PvP設定
            if (getMap().isCombatZone(getLocation())) {
                return false;
            }

            // 全戦争リストを取得
            for (L1War war : L1World.getInstance().getWarList()) {
                if (pc.getClanid() != 0 && targetpc.getClanid() != 0) { // 共にクラン所属中
                    boolean same_war = war.CheckClanInSameWar(pc.getClanname(),
                            targetpc.getClanname());
                    if (same_war == true) { // 同じ戦争に参加中
                        return false;
                    }
                }
            }
            // Non-PvP設定でも戦争中は布告なしで攻撃可能
            if (target instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance) target;
                if (isInWarAreaAndWarTime(pc, targetPc)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void clearSkillMastery() {
        skillList.clear();
    }

    public void death(L1Character lastAttacker) {
        synchronized (this) {
            if (isDead()) {
                return;
            }
            setDead(true);
            setStatus(ActionCodes.ACTION_Die);
        }
        GeneralThreadPool.getInstance().execute(new Death(lastAttacker));

    }

    public void deathPenalty() {
        int oldLevel = getLevel();
        int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        int exp = 0;
        if (oldLevel >= 1 && oldLevel < 11) {
            exp = 0;
        } else if (oldLevel >= 11 && oldLevel < 45) {
            exp = (int) (needExp * 0.1);
        } else if (oldLevel == 45) {
            exp = (int) (needExp * 0.09);
        } else if (oldLevel == 46) {
            exp = (int) (needExp * 0.08);
        } else if (oldLevel == 47) {
            exp = (int) (needExp * 0.07);
        } else if (oldLevel == 48) {
            exp = (int) (needExp * 0.06);
        } else if (oldLevel >= 49) {
            exp = (int) (needExp * 0.05);
        }

        if (exp == 0) {
            return;
        }
        addExp(-exp);
    }

    public void delBlindHiding() {
        // 魔法接続時間終了はこちら
        killSkillEffectTimer(BLIND_HIDING);
        sendPackets(new S_Invis(getId(), 0));
        broadcastPacket(new S_OtherCharPacks(this));
    }

    public void delInvis() {
        // 魔法接続時間内はこちらを利用
        if (hasSkillEffect(INVISIBILITY)) { // インビジビリティ
            killSkillEffectTimer(INVISIBILITY);
            sendPackets(new S_Invis(getId(), 0));
            broadcastPacket(new S_OtherCharPacks(this));
        }
        if (hasSkillEffect(BLIND_HIDING)) { // ブラインド ハイディング
            killSkillEffectTimer(BLIND_HIDING);
            sendPackets(new S_Invis(getId(), 0));
            broadcastPacket(new S_OtherCharPacks(this));
        }
    }

    public void endGhost() {
        setGhost(false);
        setGhostCanTalk(true);
        setReserveGhost(false);
    }

    public void endHell() {
        if (_hellFuture != null) {
            _hellFuture.cancel(false);
            _hellFuture = null;
        }
        // 地獄から脱出したら火田村へ帰還させる。
        int[] loc = L1TownLocation
                .getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
        L1Teleport.teleport(this, loc[0], loc[1], (short) loc[2], 5, true);
        try {
            save();
        } catch (Exception ignore) {
            // ignore
        }
    }

    public int get_food() {
        return _food;
    }

    public int get_PKcount() {
        return _PKcount;
    }

    public byte get_sex() {
        return _sex;
    }

    public AcceleratorChecker getAcceleratorChecker() {
        return _acceleratorChecker;
    }

    public short getAccessLevel() {
        return _accessLevel;
    }

    public String getAccountName() {
        return _accountName;
    }

    public int getAdvenHp() {
        return _advenHp;
    }

    public int getAdvenMp() {
        return _advenMp;
    }

    public int getAwakeSkillId() {
        return _awakeSkillId;
    }

    public int getBaseAc() {
        return _baseAc;
    }

    public int getBaseBowDmgup() {
        return _baseBowDmgup;
    }

    public int getBaseBowHitup() {
        return _baseBowHitup;
    }

    public byte getBaseCha() {
        return _baseCha;
    }

    public byte getBaseCon() {
        return _baseCon;
    }

    public byte getBaseDex() {
        return _baseDex;
    }

    public int getBaseDmgup() {
        return _baseDmgup;
    }

    public int getBaseHitup() {
        return _baseHitup;
    }

    public byte getBaseInt() {
        return _baseInt;
    }

    public short getBaseMaxHp() {
        return _baseMaxHp;
    }

    public short getBaseMaxMp() {
        return _baseMaxMp;
    }

    public int getBaseMr() {
        return _baseMr;
    }

    public byte getBaseStr() {
        return _baseStr;
    }

    public byte getBaseWis() {
        return _baseWis;
    }

    public int getBonusStats() {
        return _bonusStats;
    }

    public L1BookMark getBookMark(int id) {
        for (int i = 0; i < _bookmarks.size(); i++) {
            L1BookMark element = _bookmarks.get(i);
            if (element.getId() == id) {
                return element;
            }

        }
        return null;
    }

    public L1BookMark getBookMark(String name) {
        for (int i = 0; i < _bookmarks.size(); i++) {
            L1BookMark element = _bookmarks.get(i);
            if (element.getName().equalsIgnoreCase(name)) {
                return element;
            }

        }
        return null;
    }

    public int getBookMarkSize() {
        return _bookmarks.size();
    }

    public int getBowDmgModifierByArmor() {
        return _bowDmgModifierByArmor;
    }

    public int getBowHitModifierByArmor() {
        return _bowHitModifierByArmor;
    }

    public ArrayList<L1PrivateShopBuyList> getBuyList() {
        return _buyList;
    }

    public int getCallClanHeading() {
        return _callClanHeading;
    }

    public int getCallClanId() {
        return _callClanId;
    }

    public L1ChatParty getChatParty() {
        return _chatParty;
    }

    // 参照を持つようにしたほうがいいかもしれない
    public L1Clan getClan() {
        return L1World.getInstance().getClan(getClanname());
    }

    public int getClanid() {
        return _clanid;
    }

    public String getClanname() {
        return clanname;
    }

    public int getClanRank() {
        return _clanRank;
    }

    public L1ClassFeature getClassFeature() {
        return _classFeature;
    }

    public int getClassId() {
        return _classId;
    }

    public int getContribution() {
        return _contribution;
    }

    public int getCookingId() {
        return _cookingId;
    }

    public int getCurrentWeapon() {
        return _currentWeapon;
    }

    public int getDamageReductionByArmor() {
        return _damageReductionByArmor;
    }

    public Timestamp getDeleteTime() {
        return _deleteTime;
    }

    public int getDessertId() {
        return _dessertId;
    }

    public int getDmgModifierByArmor() {
        return _dmgModifierByArmor;
    }

    public L1DwarfForElfInventory getDwarfForElfInventory() {
        return _dwarfForElf;
    }

    public L1DwarfInventory getDwarfInventory() {
        return _dwarf;
    }

    public int getElfAttr() {
        return _elfAttr;
    }

    public int getElixirStats() {
        return _elixirStats;
    }

    public L1EquipmentSlot getEquipSlot() {
        return _equipSlot;
    }

    public int getEr() {
        if (hasSkillEffect(STRIKER_GALE)) {
            return 0;
        }

        int er = 0;
        if (isKnight()) {
            er = getLevel() / 4; // ナイト
        } else if (isCrown() || isElf()) {
            er = getLevel() / 8; // 君主・エルフ
        } else if (isDarkelf()) {
            er = getLevel() / 6; // ダークエルフ
        } else if (isWizard()) {
            er = getLevel() / 10; // ウィザード
        } else if (isDragonKnight()) {
            er = getLevel() / 7; // ドラゴンナイト
        } else if (isIllusionist()) {
            er = getLevel() / 9; // イリュージョニスト
        }

        er += (getDex() - 8) / 2;

        er += getOriginalEr();

        if (hasSkillEffect(DRESS_EVASION)) {
            er += 12;
        }
        if (hasSkillEffect(SOLID_CARRIAGE)) {
            er += 15;
        }
        return er;
    }

    public L1ExcludingList getExcludingList() {
        return _excludingList;
    }

    @Override
    public synchronized int getExp() {
        return _exp;
    }

    public int getExpRes() {
        return _expRes;
    }

    public int getFightId() {
        return _fightId;
    }

    public long getFishingTime() {
        return _fishingTime;
    }

    public int getHasteItemEquipped() {
        return _hasteItemEquipped;
    }

    public int getHellTime() {
        return _hellTime;
    }

    public int getHighLevel() {
        return _highLevel;
    }

    public int getHitModifierByArmor() {
        return _hitModifierByArmor;
    }

    public int getHomeTownId() {
        return _homeTownId;
    }

    public short getHpr() {
        return _hpr;
    }

    @Override
    public L1PcInventory getInventory() {
        return _inventory;
    }

    @Override
    public int getKarma() {
        return _karma.get();
    }

    public int getKarmaLevel() {
        return _karma.getLevel();
    }

    public int getKarmaPercent() {
        return _karma.getPercent();
    }

    /**
     * プレイヤーの最終PK時間を返す。
     * 
     * @return _lastPk
     * 
     */
    public Timestamp getLastPk() {
        return _lastPk;
    }

    public Timestamp getLastPkForElf() {
        return _lastPkForElf;
    }

    @Override
    public int getMagicLevel() {
        return getClassFeature().getMagicLevel(getLevel());
    }

    public double getMaxWeight() {
        int str = getStr();
        int con = getCon();
        double maxWeight = 150 * (Math.floor(0.6 * str + 0.4 * con + 1));

        double weightReductionByArmor = getWeightReduction(); // 防具による重量軽減
        weightReductionByArmor /= 100;

        double weightReductionByDoll = 0; // マジックドールによる重量軽減
        Object[] dollList = getDollList().values().toArray();
        for (Object dollObject : dollList) {
            L1DollInstance doll = (L1DollInstance) dollObject;
            weightReductionByDoll += doll.getWeightReductionByDoll();
        }
        weightReductionByDoll /= 100;

        int weightReductionByMagic = 0;
        if (hasSkillEffect(DECREASE_WEIGHT)) { // ディクリースウェイト
            weightReductionByMagic = 180;
        }

        double originalWeightReduction = 0; // オリジナルステータスによる重量軽減
        originalWeightReduction += 0.04 * (getOriginalStrWeightReduction() + getOriginalConWeightReduction());

        double weightReduction = 1 + weightReductionByArmor
                + weightReductionByDoll + originalWeightReduction;

        maxWeight *= weightReduction;

        maxWeight += weightReductionByMagic;

        maxWeight *= Config.RATE_WEIGHT_LIMIT; // ウェイトレートを掛ける

        return maxWeight;
    }

    public short getMpr() {
        return _mpr;
    }

    public ClientThread getNetConnection() {
        return _netConnection;
    }

    public int getOnlineStatus() {
        return _onlineStatus;
    }

    public int getOriginalAc() {

        return _originalAc;
    }

    public int getOriginalBowDmgup() {

        return _originalBowDmgup;
    }

    public int getOriginalBowHitup() {

        return _originalHitup;
    }

    public int getOriginalCha() {
        return _originalCha;
    }

    public int getOriginalCon() {
        return _originalCon;
    }

    public int getOriginalConWeightReduction() {

        return _originalConWeightReduction;
    }

    public int getOriginalDex() {
        return _originalDex;
    }

    public int getOriginalDmgup() {

        return _originalDmgup;
    }

    public int getOriginalEr() {

        return _originalEr;
    }

    public int getOriginalHitup() {

        return _originalHitup;
    }

    public short getOriginalHpr() {

        return _originalHpr;
    }

    public int getOriginalHpup() {

        return _originalHpup;
    }

    public int getOriginalInt() {
        return _originalInt;
    }

    public int getOriginalMagicConsumeReduction() {

        return _originalMagicConsumeReduction;
    }

    public int getOriginalMagicCritical() {

        return _originalMagicCritical;
    }

    public int getOriginalMagicDamage() {

        return _originalMagicDamage;
    }

    public int getOriginalMagicHit() {

        return _originalMagicHit;
    }

    public short getOriginalMpr() {

        return _originalMpr;
    }

    public int getOriginalMpup() {

        return _originalMpup;
    }

    public int getOriginalMr() {

        return _originalMr;
    }

    public int getOriginalStr() {
        return _originalStr;
    }

    public int getOriginalStrWeightReduction() {

        return _originalStrWeightReduction;
    }

    public int getOriginalWis() {
        return _originalWis;
    }

    public int getPartnerId() {
        return _partnerId;
    }

    public int getPartnersPrivateShopItemCount() {
        return _partnersPrivateShopItemCount;
    }

    public L1Party getParty() {
        return _party;
    }

    public int getPartyID() {
        return _partyID;
    }

    public int getPkCountForElf() {
        return _PkCountForElf;
    }

    public L1Quest getQuest() {
        return _quest;
    }

    public ArrayList<L1PrivateShopSellList> getSellList() {
        return _sellList;
    }

    public byte[] getShopChat() {
        return _shopChat;
    }

    private L1PcInstance getStat() {
        return null;
    }

    public int getTeleportHeading() {
        return _teleportHeading;
    }

    public int getTeleportMapId() {
        return _teleportMapId;
    }

    public int getTeleportX() {
        return _teleportX;
    }

    public int getTeleportY() {
        return _teleportY;
    }

    public int getTempCharGfxAtDead() {
        return _tempCharGfxAtDead;
    }

    public int getTempID() {
        return _tempID;
    }

    public int getTempLevel() {
        return _tempLevel;
    }

    public int getTempMaxLevel() {
        return _tempMaxLevel;
    }

    public int getTradeID() {
        return _tradeID;
    }

    public boolean getTradeOk() {
        return _tradeOk;
    }

    public L1Inventory getTradeWindowInventory() {
        return _tradewindow;
    }

    public int getType() {
        return _type;
    }

    public L1ItemInstance getWeapon() {
        return _weapon;
    }

    public int getWeightReduction() {
        return _weightReduction;
    }

    @Override
    public void healHp(int pt) {
        super.healHp(pt);

        sendPackets(new S_HPUpdate(this));
    }

    public boolean isBanned() {
        return _banned;
    }

    public boolean isBrave() {
        return hasSkillEffect(STATUS_BRAVE);
    }

    public boolean isCanWhisper() {
        return _isCanWhisper;
    }

    public boolean isCrown() {
        return (getClassId() == CLASSID_PRINCE || getClassId() == CLASSID_PRINCESS);
    }

    public boolean isDarkelf() {
        return (getClassId() == CLASSID_DARK_ELF_MALE || getClassId() == CLASSID_DARK_ELF_FEMALE);
    }

    public boolean isDragonKnight() {
        return (getClassId() == CLASSID_DRAGON_KNIGHT_MALE || getClassId() == CLASSID_DRAGON_KNIGHT_FEMALE);
    }

    public boolean isDrink() {
        return _isDrink;
    }

    public boolean isElf() {
        return (getClassId() == CLASSID_ELF_MALE || getClassId() == CLASSID_ELF_FEMALE);
    }

    public boolean isElfBrave() {
        return hasSkillEffect(STATUS_ELFBRAVE);
    }

    public boolean isFastAttackable() {
        return hasSkillEffect(BLOODLUST);
    }

    public boolean isFastMovable() {
        return (hasSkillEffect(HOLY_WALK)
                || hasSkillEffect(MOVING_ACCELERATION)
                || hasSkillEffect(WIND_WALK) || hasSkillEffect(STATUS_RIBRAVE));
    }

    public boolean isFishing() {
        return _isFishing;
    }

    public boolean isFishingReady() {
        return _isFishingReady;
    }

    public boolean isGhost() {
        return _ghost;
    }

    public boolean isGhostCanTalk() {
        return _ghostCanTalk;
    }

    public boolean isGm() {
        return _gm;
    }

    public boolean isGmInvis() {
        return _gmInvis;
    }

    public boolean isGres() {
        return _isGres;
    }

    public boolean isGresValid() {
        return _gresValid;
    }

    public boolean isHaste() {
        return (hasSkillEffect(STATUS_HASTE) || hasSkillEffect(HASTE)
                || hasSkillEffect(GREATER_HASTE) || getMoveSpeed() == 1);
    }

    public boolean isIllusionist() {
        return (getClassId() == CLASSID_ILLUSIONIST_MALE || getClassId() == CLASSID_ILLUSIONIST_FEMALE);
    }

    public boolean isInCharReset() {
        return _isInCharReset;
    }

    public boolean isInChatParty() {
        return getChatParty() != null;
    }

    public boolean isInParty() {
        return getParty() != null;
    }

    public boolean isInvisDelay() {
        return (invisDelayCounter > 0);
    }

    private boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
        // pcとtargetが戦争中に戦争エリアに居るか
        int castleId = L1CastleLocation.getCastleIdByArea(pc);
        int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
        if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId) {
            if (WarTimeController.getInstance().isNowWar(castleId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKnight() {
        return (getClassId() == CLASSID_KNIGHT_MALE || getClassId() == CLASSID_KNIGHT_FEMALE);
    }

    public boolean isMonitor() {
        return _monitor;
    }

    public boolean isPinkName() {
        return _isPinkName;
    }

    public boolean isPrivateShop() {
        return _isPrivateShop;
    }

    public boolean isReserveGhost() {
        return _isReserveGhost;
    }

    public boolean isShapeChange() {
        return _isShapeChange;
    }

    public boolean isShowTradeChat() {
        return _isShowTradeChat;
    }

    public boolean isShowWorldChat() {
        return _isShowWorldChat;
    }

    public boolean isSkillMastery(int skillid) {
        return skillList.contains(skillid);
    }

    public boolean isSummonMonster() {
        return _isSummonMonster;
    }

    public boolean isTeleport() {
        return _isTeleport;
    }

    public boolean isTradingInPrivateShop() {
        return _isTradingInPrivateShop;
    }

    /**
     * プレイヤーが手配中であるかを返す。
     * 
     * @return 手配中であれば、true
     */
    public boolean isWanted() {
        if (_lastPk == null) {
            return false;
        } else if (System.currentTimeMillis() - _lastPk.getTime() > 24 * 3600 * 1000) {
            setLastPk(null);
            return false;
        }
        return true;
    }

    public boolean isWantedForElf() {
        if (_lastPkForElf == null) {
            return false;
        } else if (System.currentTimeMillis() - _lastPkForElf.getTime() > 24 * 3600 * 1000) {
            setLastPkForElf(null);
            return false;
        }
        return true;
    }

    public boolean isWizard() {
        return (getClassId() == CLASSID_WIZARD_MALE || getClassId() == CLASSID_WIZARD_FEMALE);
    }

    private void levelDown(int gap) {
        resetLevel();

        for (int i = 0; i > gap; i--) {
            // レベルダウン時はランダム値をそのままマイナスする為に、base値に0を設定
            short randomHp = CalcStat.calcStatHp(getType(), 0, getBaseCon(),
                    getOriginalHpup());
            short randomMp = CalcStat.calcStatMp(getType(), 0, getBaseWis(),
                    getOriginalMpup());
            addBaseMaxHp((short) -randomHp);
            addBaseMaxMp((short) -randomMp);
        }
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        if (Config.LEVEL_DOWN_RANGE != 0) {
            if (getHighLevel() - getLevel() >= Config.LEVEL_DOWN_RANGE) {
                sendPackets(new S_ServerMessage(64)); // ワールドとの接続が切断されました。
                sendPackets(new S_Disconnect());
                _log.info(String.format("レベルダウンの許容範囲を超えたため%sを強制切断しました。",
                        getName()));
            }
        }

        try {
            // DBにキャラクター情報を書き込む
            save();
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        sendPackets(new S_OwnCharStatus(this));
    }

    private void levelUp(int gap) {
        resetLevel();

        // 復活のポーション
        if (getLevel() == 99 && Config.ALT_REVIVAL_POTION) {
            try {
                L1Item l1item = ItemTable.getInstance().getTemplate(43000);
                if (l1item != null) {
                    getInventory().storeItem(43000, 1);
                    sendPackets(new S_ServerMessage(403, l1item.getName()));
                } else {
                    sendPackets(new S_SystemMessage("復活のポーションの入手に失敗しました。"));
                }
            } catch (Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                sendPackets(new S_SystemMessage("復活のポーションの入手に失敗しました。"));
            }
        }

        for (int i = 0; i < gap; i++) {
            short randomHp = CalcStat.calcStatHp(getType(), getBaseMaxHp(),
                    getBaseCon(), getOriginalHpup());
            short randomMp = CalcStat.calcStatMp(getType(), getBaseMaxMp(),
                    getBaseWis(), getOriginalMpup());
            addBaseMaxHp(randomHp);
            addBaseMaxMp(randomMp);
        }
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        if (getLevel() > getHighLevel()) {
            setHighLevel(getLevel());
        }

        try {
            // DBにキャラクター情報を書き込む
            save();
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        // ボーナスステータス
        if (getLevel() >= 51 && getLevel() - 50 > getBonusStats()) {
            if ((getBaseStr() + getBaseDex() + getBaseCon() + getBaseInt()
                    + getBaseWis() + getBaseCha()) < 210) {
                sendPackets(new S_bonusstats(getId(), 1));
            }
        }
        sendPackets(new S_OwnCharStatus(this));
        if (getLevel() >= 52) { // 指定レベル
            if (getMapId() == 777) { // 見捨てられた者たちの地(影の神殿)
                L1Teleport.teleport(this, 34043, 32184, (short) 4, 5, true); // 象牙の塔前
            } else if (getMapId() == 778 || getMapId() == 779) { // 見捨てられた者たちの地(欲望の洞窟)
                L1Teleport.teleport(this, 32608, 33178, (short) 4, 5, true); // WB
            }
        }
    }

    public void logout() {
        L1World world = L1World.getInstance();
        if (getClanid() != 0) // クラン所属
        {
            L1Clan clan = world.getClan(getClanname());
            if (clan != null) {
                if (clan.getWarehouseUsingChar() == getId()) // 自キャラがクラン倉庫使用中
                {
                    clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
                }
            }
        }
        notifyPlayersLogout(getKnownPlayers());
        world.removeVisibleObject(this);
        world.removeObject(this);
        notifyPlayersLogout(world.getRecognizePlayer(this));
        _inventory.clearItems();
        _dwarf.clearItems();
        removeAllKnownObjects();
        stopHpRegeneration();
        stopMpRegeneration();
        setDead(true); // 使い方おかしいかもしれないけど、ＮＰＣに消滅したことをわからせるため
        setNetConnection(null);
        setPacketOutput(null);
    }

    public void makeReadyEndGhost() {
        setReserveGhost(true);
        L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY,
                _ghostSaveMapId, _ghostSaveHeading, true);
    }

    /**
     * 指定されたプレイヤー群にログアウトしたことを通知する
     * 
     * @param playersList
     *            通知するプレイヤーの配列
     */
    private void notifyPlayersLogout(List<L1PcInstance> playersArray) {
        for (L1PcInstance player : playersArray) {
            if (player.knownsObject(this)) {
                player.removeKnownObject(this);
                player.sendPackets(new S_RemoveObject(this));
            }
        }
    }

    @Override
    public void onAction(L1PcInstance attacker) {
        // XXX:NullPointerException回避。onActionの引数の型はL1Characterのほうが良い？
        if (attacker == null) {
            return;
        }
        // テレポート処理中
        if (isTeleport()) {
            return;
        }
        // 攻撃される側または攻撃する側がセーフティーゾーン
        if (getZoneType() == 1 || attacker.getZoneType() == 1) {
            // 攻撃モーション送信
            L1Attack attack_mortion = new L1Attack(attacker, this);
            attack_mortion.action();
            return;
        }

        if (checkNonPvP(this, attacker) == true) {
            // 攻撃モーション送信
            L1Attack attack_mortion = new L1Attack(attacker, this);
            attack_mortion.action();
            return;
        }

        if (getCurrentHp() > 0 && !isDead()) {
            attacker.delInvis();

            boolean isCounterBarrier = false;
            L1Attack attack = new L1Attack(attacker, this);
            if (attack.calcHit()) {
                if (hasSkillEffect(COUNTER_BARRIER)) {
                    L1Magic magic = new L1Magic(this, attacker);
                    boolean isProbability = magic
                            .calcProbabilityMagic(COUNTER_BARRIER);
                    boolean isShortDistance = attack.isShortDistance();
                    if (isProbability && isShortDistance) {
                        isCounterBarrier = true;
                    }
                }
                if (!isCounterBarrier) {
                    attacker.setPetTarget(this);

                    attack.calcDamage();
                    attack.calcStaffOfMana();
                    attack.addPcPoisonAttack(attacker, this);
                    attack.addChaserAttack();
                }
            }
            if (isCounterBarrier) {
                attack.actionCounterBarrier();
                attack.commitCounterBarrier();
            } else {
                attack.action();
                attack.commit();
            }
        }
    }

    public void onChangeExp() {
        int level = ExpTable.getLevelByExp(getExp());
        int char_level = getLevel();
        int gap = level - char_level;
        if (gap == 0) {
            // sendPackets(new S_OwnCharStatus(this));
            sendPackets(new S_Exp(this));
            return;
        }

        // レベルが変化した場合
        if (gap > 0) {
            levelUp(gap);
        } else if (gap < 0) {
            levelDown(gap);
        }
    }

    @Override
    public void onPerceive(L1PcInstance perceivedFrom) {
        if (isGmInvis() || isGhost()) {
            return;
        }
        if (isInvisble() && !perceivedFrom.hasSkillEffect(GMSTATUS_FINDINVIS)) {
            return;
        }

        perceivedFrom.addKnownObject(this);
        perceivedFrom.sendPackets(new S_OtherCharPacks(this, perceivedFrom
                .hasSkillEffect(GMSTATUS_FINDINVIS))); // 自分の情報を送る
        if (isInParty() && getParty().isMember(perceivedFrom)) { // PTメンバーならHPメーターも送る
            perceivedFrom.sendPackets(new S_HPMeter(this));
        }

        if (isPrivateShop()) {
            perceivedFrom.sendPackets(new S_DoActionShop(getId(),
                    ActionCodes.ACTION_Shop, getShopChat()));
        }

        if (isCrown()) { // 君主
            L1Clan clan = L1World.getInstance().getClan(getClanname());
            if (clan != null) {
                if (getId() == clan.getLeaderId() // 血盟主で城主クラン
                        && clan.getCastleId() != 0) {
                    perceivedFrom.sendPackets(new S_CastleMaster(clan
                            .getCastleId(), getId()));
                }
            }
        }
    }

    public void receiveDamage(L1Character attacker, double damage,
            boolean isMagicDamage) { // 攻撃でＨＰを減らすときはここを使用
        if (getCurrentHp() > 0 && !isDead()) {
            if (attacker != this) {
                if (!(attacker instanceof L1EffectInstance)
                        && !knownsObject(attacker)
                        && attacker.getMapId() == this.getMapId()) {
                    attacker.onPerceive(this);
                }
            }

            if (isMagicDamage == true) { // 連続魔法ダメージによる軽減
                long nowTime = System.currentTimeMillis();
                long interval = nowTime - _oldTime;

                if (damage < 0) {
                    damage = 0;
                } else {
                    if (2000 > interval && interval >= 1900) {
                        damage = (damage * (100 - (10 / 3))) / 100;
                    } else if (1900 > interval && interval >= 1800) {
                        damage = (damage * (100 - 2 * (10 / 3))) / 100;
                    } else if (1800 > interval && interval >= 1700) {
                        damage = (damage * (100 - 3 * (10 / 3))) / 100;
                    } else if (1700 > interval && interval >= 1600) {
                        damage = (damage * (100 - 4 * (10 / 3))) / 100;
                    } else if (1600 > interval && interval >= 1500) {
                        damage = (damage * (100 - 5 * (10 / 3))) / 100;
                    } else if (1500 > interval && interval >= 1400) {
                        damage = (damage * (100 - 6 * (10 / 3))) / 100;
                    } else if (1400 > interval && interval >= 1300) {
                        damage = (damage * (100 - 7 * (10 / 3))) / 100;
                    } else if (1300 > interval && interval >= 1200) {
                        damage = (damage * (100 - 8 * (10 / 3))) / 100;
                    } else if (1200 > interval && interval >= 1100) {
                        damage = (damage * (100 - 9 * (10 / 3))) / 100;
                    } else if (1100 > interval && interval >= 1000) {
                        damage = (damage * (100 - 10 * (10 / 3))) / 100;
                    } else if (1000 > interval && interval >= 900) {
                        damage = (damage * (100 - 11 * (10 / 3))) / 100;
                    } else if (900 > interval && interval >= 800) {
                        damage = (damage * (100 - 12 * (10 / 3))) / 100;
                    } else if (800 > interval && interval >= 700) {
                        damage = (damage * (100 - 13 * (10 / 3))) / 100;
                    } else if (700 > interval && interval >= 600) {
                        damage = (damage * (100 - 14 * (10 / 3))) / 100;
                    } else if (600 > interval && interval >= 500) {
                        damage = (damage * (100 - 15 * (10 / 3))) / 100;
                    } else if (500 > interval && interval >= 400) {
                        damage = (damage * (100 - 16 * (10 / 3))) / 100;
                    } else if (400 > interval && interval >= 300) {
                        damage = (damage * (100 - 17 * (10 / 3))) / 100;
                    } else if (300 > interval && interval >= 200) {
                        damage = (damage * (100 - 18 * (10 / 3))) / 100;
                    } else if (200 > interval && interval >= 100) {
                        damage = (damage * (100 - 19 * (10 / 3))) / 100;
                    } else if (100 > interval && interval >= 0) {
                        damage = (damage * (100 - 20 * (10 / 3))) / 100;
                    }

                    if (damage < 1) {
                        damage = 0;
                    }

                    _oldTime = nowTime; // 次回のために時間を保存
                }
            }
            if (damage > 0) {
                delInvis();
                if (attacker instanceof L1PcInstance) {
                    L1PinkName.onAction(this, attacker);
                }
                if (attacker instanceof L1PcInstance
                        && ((L1PcInstance) attacker).isPinkName()) {
                    // ガードが画面内にいれば、攻撃者をガードのターゲットに設定する
                    for (L1Object object : L1World.getInstance()
                            .getVisibleObjects(attacker)) {
                        if (object instanceof L1GuardInstance) {
                            L1GuardInstance guard = (L1GuardInstance) object;
                            guard.setTarget(((L1PcInstance) attacker));
                        }
                    }
                }
                removeSkillEffect(FOG_OF_SLEEPING);
            }

            if (hasSkillEffect(MORTAL_BODY) && getId() != attacker.getId()) {
                int rnd = _random.nextInt(100) + 1;
                if (damage > 0 && rnd <= 10) {
                    if (attacker instanceof L1PcInstance) {
                        L1PcInstance attackPc = (L1PcInstance) attacker;
                        attackPc.sendPackets(new S_DoActionGFX(
                                attackPc.getId(), ActionCodes.ACTION_Damage));
                        attackPc.broadcastPacket(new S_DoActionGFX(attackPc
                                .getId(), ActionCodes.ACTION_Damage));
                        attackPc.receiveDamage(this, 30, false);
                    } else if (attacker instanceof L1NpcInstance) {
                        L1NpcInstance attackNpc = (L1NpcInstance) attacker;
                        attackNpc.broadcastPacket(new S_DoActionGFX(attackNpc
                                .getId(), ActionCodes.ACTION_Damage));
                        attackNpc.receiveDamage(this, 30);
                    }
                }
            }
            if (attacker.hasSkillEffect(JOY_OF_PAIN)
                    && getId() != attacker.getId()) {
                int nowDamage = getMaxHp() - getCurrentHp();
                if (nowDamage > 0) {
                    if (attacker instanceof L1PcInstance) {
                        L1PcInstance attackPc = (L1PcInstance) attacker;
                        attackPc.sendPackets(new S_DoActionGFX(
                                attackPc.getId(), ActionCodes.ACTION_Damage));
                        attackPc.broadcastPacket(new S_DoActionGFX(attackPc
                                .getId(), ActionCodes.ACTION_Damage));
                        attackPc.receiveDamage(this, (nowDamage / 5), false);
                    } else if (attacker instanceof L1NpcInstance) {
                        L1NpcInstance attackNpc = (L1NpcInstance) attacker;
                        attackNpc.broadcastPacket(new S_DoActionGFX(attackNpc
                                .getId(), ActionCodes.ACTION_Damage));
                        attackNpc.receiveDamage(this, (nowDamage / 5));
                    }
                }
            }
            if (getInventory().checkEquipped(145) // バーサーカーアックス
                    || getInventory().checkEquipped(149)) { // ミノタウルスアックス
                damage *= 1.5; // 被ダメ1.5倍
            }
            if (hasSkillEffect(ILLUSION_AVATAR)) {
                damage *= 1.5; // 被ダメ1.5倍
            }
            int newHp = getCurrentHp() - (int) (damage);
            if (newHp > getMaxHp()) {
                newHp = getMaxHp();
            }
            if (newHp <= 0) {
                if (isGm()) {
                    setCurrentHp(getMaxHp());
                } else {
                    death(attacker);
                }
            }
            if (newHp > 0) {
                setCurrentHp(newHp);
            }
        } else if (!isDead()) { // 念のため
            System.out.println("警告：ＨＰ减少运算错误。※将按照ＨＰ０处理。");
            death(attacker);
        }
    }

    // 魔法のダメージの場合はここを使用 (ここで魔法ダメージ軽減処理) attr:0.無属性魔法,1.地魔法,2.火魔法,3.水魔法,4.風魔法
    public void receiveDamage(L1Character attacker, int damage, int attr) {
        int player_mr = getMr();
        int rnd = _random.nextInt(100) + 1;
        if (player_mr >= rnd) {
            damage /= 2;
        }
        receiveDamage(attacker, damage, false);
    }

    public void receiveManaDamage(L1Character attacker, int mpDamage) { // 攻撃でＭＰを減らすときはここを使用
        if (mpDamage > 0 && !isDead()) {
            delInvis();
            if (attacker instanceof L1PcInstance) {
                L1PinkName.onAction(this, attacker);
            }
            if (attacker instanceof L1PcInstance
                    && ((L1PcInstance) attacker).isPinkName()) {
                // ガードが画面内にいれば、攻撃者をガードのターゲットに設定する
                for (L1Object object : L1World.getInstance().getVisibleObjects(
                        attacker)) {
                    if (object instanceof L1GuardInstance) {
                        L1GuardInstance guard = (L1GuardInstance) object;
                        guard.setTarget(((L1PcInstance) attacker));
                    }
                }
            }

            int newMp = getCurrentMp() - mpDamage;
            if (newMp > getMaxMp()) {
                newMp = getMaxMp();
            }

            if (newMp <= 0) {
                newMp = 0;
            }
            setCurrentMp(newMp);
        }
    }

    public void reduceCurrentHp(double d, L1Character l1character) {
        getStat().reduceCurrentHp(d, l1character);
    }

    public void refresh() {
        resetLevel();
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseMr();
        resetBaseAc();
        resetOriginalHpup();
        resetOriginalMpup();
        resetOriginalDmgup();
        resetOriginalBowDmgup();
        resetOriginalHitup();
        resetOriginalBowHitup();
        resetOriginalMr();
        resetOriginalMagicHit();
        resetOriginalMagicCritical();
        resetOriginalMagicConsumeReduction();
        resetOriginalMagicDamage();
        resetOriginalAc();
        resetOriginalEr();
        resetOriginalHpr();
        resetOriginalMpr();
        resetOriginalStrWeightReduction();
        resetOriginalConWeightReduction();
    }

    public void removeBookMark(L1BookMark book) {
        _bookmarks.remove(book);
    }

    public void removeHasteSkillEffect() {
        if (hasSkillEffect(SLOW)) {
            removeSkillEffect(SLOW);
        }
        if (hasSkillEffect(MASS_SLOW)) {
            removeSkillEffect(MASS_SLOW);
        }
        if (hasSkillEffect(ENTANGLE)) {
            removeSkillEffect(ENTANGLE);
        }
        if (hasSkillEffect(HASTE)) {
            removeSkillEffect(HASTE);
        }
        if (hasSkillEffect(GREATER_HASTE)) {
            removeSkillEffect(GREATER_HASTE);
        }
        if (hasSkillEffect(STATUS_HASTE)) {
            removeSkillEffect(STATUS_HASTE);
        }
    }

    // 範囲外になった認識済みオブジェクトを除去
    private void removeOutOfRangeObjects() {
        for (L1Object known : getKnownObjects()) {
            if (known == null) {
                continue;
            }

            if (Config.PC_RECOGNIZE_RANGE == -1) {
                if (!getLocation().isInScreen(known.getLocation())) { // 画面外
                    removeKnownObject(known);
                    sendPackets(new S_RemoveObject(known));
                }
            } else {
                if (getLocation().getTileLineDistance(known.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                    removeKnownObject(known);
                    sendPackets(new S_RemoveObject(known));
                }
            }
        }
    }

    public void removeSkillMastery(int skillid) {
        if (skillList.contains(skillid)) {
            skillList.remove((Object) skillid);
        }
    }

    /**
     * キャラクターステータスからACを再計算して設定する 初期設定時、LVUP,LVDown時などに呼び出す
     */
    public void resetBaseAc() {
        int newAc = CalcStat.calcAc(getLevel(), getBaseDex());
        addAc(newAc - _baseAc);
        _baseAc = newAc;
    }

    /**
     * LVによる命中ボーナスを設定する LVが変動した場合などに呼び出せば再計算される
     * 
     * @return
     */
    public void resetBaseDmgup() {
        int newBaseDmgup = 0;
        int newBaseBowDmgup = 0;
        if (isKnight() || isDarkelf() || isDragonKnight()) { // ナイト、ダークエルフ、ドラゴンナイト
            newBaseDmgup = getLevel() / 10;
            newBaseBowDmgup = 0;
        } else if (isElf()) { // エルフ
            newBaseDmgup = 0;
            newBaseBowDmgup = getLevel() / 10;
        }
        addDmgup(newBaseDmgup - _baseDmgup);
        addBowDmgup(newBaseBowDmgup - _baseBowDmgup);
        _baseDmgup = newBaseDmgup;
        _baseBowDmgup = newBaseBowDmgup;
    }

    /**
     * LVによる命中ボーナスを設定する LVが変動した場合などに呼び出せば再計算される
     * 
     * @return
     */
    public void resetBaseHitup() {
        int newBaseHitup = 0;
        int newBaseBowHitup = 0;
        if (isCrown()) {
            newBaseHitup = getLevel() / 5;
            newBaseBowHitup = getLevel() / 5;
        } else if (isKnight()) {
            newBaseHitup = getLevel() / 3;
            newBaseBowHitup = getLevel() / 3;
        } else if (isElf()) {
            newBaseHitup = getLevel() / 5;
            newBaseBowHitup = getLevel() / 5;
        } else if (isDarkelf()) {
            newBaseHitup = getLevel() / 3;
            newBaseBowHitup = getLevel() / 3;
        } else if (isDragonKnight()) {
            newBaseHitup = getLevel() / 3;
            newBaseBowHitup = getLevel() / 3;
        } else if (isIllusionist()) {
            newBaseHitup = getLevel() / 5;
            newBaseBowHitup = getLevel() / 5;
        }
        addHitup(newBaseHitup - _baseHitup);
        addBowHitup(newBaseBowHitup - _baseBowHitup);
        _baseHitup = newBaseHitup;
        _baseBowHitup = newBaseBowHitup;
    }

    /**
     * キャラクターステータスから素のMRを再計算して設定する 初期設定時、スキル使用時やLVUP,LVDown時に呼び出す
     */
    public void resetBaseMr() {
        int newMr = 0;
        if (isCrown()) {
            newMr = 10;
        } else if (isElf()) {
            newMr = 25;
        } else if (isWizard()) {
            newMr = 15;
        } else if (isDarkelf()) {
            newMr = 10;
        } else if (isDragonKnight()) {
            newMr = 18;
        } else if (isIllusionist()) {
            newMr = 20;
        }
        newMr += CalcStat.calcStatMr(getWis()); // WIS分のMRボーナス
        newMr += getLevel() / 2; // LVの半分だけ追加
        addMr(newMr - _baseMr);
        _baseMr = newMr;
    }

    /**
     * EXPから現在のLvを再計算して設定する 初期設定時、死亡時やLVUP時に呼び出す
     */
    public void resetLevel() {
        setLevel(ExpTable.getLevelByExp(_exp));

        if (_hpRegen != null) {
            _hpRegen.updateLevel();
        }
    }

    public void resetOriginalAc() {
        int originalDex = getOriginalDex();
        if (isCrown()) {
            if (originalDex >= 12 && originalDex <= 14) {
                _originalAc = 1;
            } else if (originalDex == 15 || originalDex == 16) {
                _originalAc = 2;
            } else if (originalDex >= 17) {
                _originalAc = 3;
            } else {
                _originalAc = 0;
            }
        } else if (isKnight()) {
            if (originalDex == 13 || originalDex == 14) {
                _originalAc = 1;
            } else if (originalDex >= 15) {
                _originalAc = 3;
            } else {
                _originalAc = 0;
            }
        } else if (isElf()) {
            if (originalDex >= 15 && originalDex <= 17) {
                _originalAc = 1;
            } else if (originalDex == 18) {
                _originalAc = 2;
            } else {
                _originalAc = 0;
            }
        } else if (isDarkelf()) {
            if (originalDex >= 17) {
                _originalAc = 1;
            } else {
                _originalAc = 0;
            }
        } else if (isWizard()) {
            if (originalDex == 8 || originalDex == 9) {
                _originalAc = 1;
            } else if (originalDex >= 10) {
                _originalAc = 2;
            } else {
                _originalAc = 0;
            }
        } else if (isDragonKnight()) {
            if (originalDex == 12 || originalDex == 13) {
                _originalAc = 1;
            } else if (originalDex >= 14) {
                _originalAc = 2;
            } else {
                _originalAc = 0;
            }
        } else if (isIllusionist()) {
            if (originalDex == 11 || originalDex == 12) {
                _originalAc = 1;
            } else if (originalDex >= 13) {
                _originalAc = 2;
            } else {
                _originalAc = 0;
            }
        }

        addAc(0 - _originalAc);
    }

    public void resetOriginalBowDmgup() {
        int originalDex = getOriginalDex();
        if (isCrown()) {
            if (originalDex >= 13) {
                _originalBowDmgup = 1;
            } else {
                _originalBowDmgup = 0;
            }
        } else if (isKnight()) {
            _originalBowDmgup = 0;
        } else if (isElf()) {
            if (originalDex >= 14 && originalDex <= 16) {
                _originalBowDmgup = 2;
            } else if (originalDex >= 17) {
                _originalBowDmgup = 3;
            } else {
                _originalBowDmgup = 0;
            }
        } else if (isDarkelf()) {
            if (originalDex == 18) {
                _originalBowDmgup = 2;
            } else {
                _originalBowDmgup = 0;
            }
        } else if (isWizard()) {
            _originalBowDmgup = 0;
        } else if (isDragonKnight()) {
            _originalBowDmgup = 0;
        } else if (isIllusionist()) {
            _originalBowDmgup = 0;
        }
    }

    public void resetOriginalBowHitup() {
        int originalDex = getOriginalDex();
        if (isCrown()) {
            _originalBowHitup = 0;
        } else if (isKnight()) {
            _originalBowHitup = 0;
        } else if (isElf()) {
            if (originalDex >= 13 && originalDex <= 15) {
                _originalBowHitup = 2;
            } else if (originalDex >= 16) {
                _originalBowHitup = 3;
            } else {
                _originalBowHitup = 0;
            }
        } else if (isDarkelf()) {
            if (originalDex == 17) {
                _originalBowHitup = 1;
            } else if (originalDex == 18) {
                _originalBowHitup = 2;
            } else {
                _originalBowHitup = 0;
            }
        } else if (isWizard()) {
            _originalBowHitup = 0;
        } else if (isDragonKnight()) {
            _originalBowHitup = 0;
        } else if (isIllusionist()) {
            _originalBowHitup = 0;
        }
    }

    public void resetOriginalConWeightReduction() {
        int originalCon = getOriginalCon();
        if (isCrown()) {
            if (originalCon >= 11) {
                _originalConWeightReduction = 1;
            } else {
                _originalConWeightReduction = 0;
            }
        } else if (isKnight()) {
            if (originalCon >= 15) {
                _originalConWeightReduction = 1;
            } else {
                _originalConWeightReduction = 0;
            }
        } else if (isElf()) {
            if (originalCon >= 15) {
                _originalConWeightReduction = 2;
            } else {
                _originalConWeightReduction = 0;
            }
        } else if (isDarkelf()) {
            if (originalCon >= 9) {
                _originalConWeightReduction = 1;
            } else {
                _originalConWeightReduction = 0;
            }
        } else if (isWizard()) {
            if (originalCon == 13 || originalCon == 14) {
                _originalConWeightReduction = 1;
            } else if (originalCon >= 15) {
                _originalConWeightReduction = 2;
            } else {
                _originalConWeightReduction = 0;
            }
        } else if (isDragonKnight()) {
            _originalConWeightReduction = 0;
        } else if (isIllusionist()) {
            if (originalCon == 17) {
                _originalConWeightReduction = 1;
            } else if (originalCon == 18) {
                _originalConWeightReduction = 2;
            } else {
                _originalConWeightReduction = 0;
            }
        }
    }

    public void resetOriginalDmgup() {
        int originalStr = getOriginalStr();
        if (isCrown()) {
            if (originalStr >= 15 && originalStr <= 17) {
                _originalDmgup = 1;
            } else if (originalStr >= 18) {
                _originalDmgup = 2;
            } else {
                _originalDmgup = 0;
            }
        } else if (isKnight()) {
            if (originalStr == 18 || originalStr == 19) {
                _originalDmgup = 2;
            } else if (originalStr == 20) {
                _originalDmgup = 4;
            } else {
                _originalDmgup = 0;
            }
        } else if (isElf()) {
            if (originalStr == 12 || originalStr == 13) {
                _originalDmgup = 1;
            } else if (originalStr >= 14) {
                _originalDmgup = 2;
            } else {
                _originalDmgup = 0;
            }
        } else if (isDarkelf()) {
            if (originalStr >= 14 && originalStr <= 17) {
                _originalDmgup = 1;
            } else if (originalStr == 18) {
                _originalDmgup = 2;
            } else {
                _originalDmgup = 0;
            }
        } else if (isWizard()) {
            if (originalStr == 10 || originalStr == 11) {
                _originalDmgup = 1;
            } else if (originalStr >= 12) {
                _originalDmgup = 2;
            } else {
                _originalDmgup = 0;
            }
        } else if (isDragonKnight()) {
            if (originalStr >= 15 && originalStr <= 17) {
                _originalDmgup = 1;
            } else if (originalStr >= 18) {
                _originalDmgup = 3;
            } else {
                _originalDmgup = 0;
            }
        } else if (isIllusionist()) {
            if (originalStr == 13 || originalStr == 14) {
                _originalDmgup = 1;
            } else if (originalStr >= 15) {
                _originalDmgup = 2;
            } else {
                _originalDmgup = 0;
            }
        }
    }

    public void resetOriginalEr() {
        int originalDex = getOriginalDex();
        if (isCrown()) {
            if (originalDex == 14 || originalDex == 15) {
                _originalEr = 1;
            } else if (originalDex == 16 || originalDex == 17) {
                _originalEr = 2;
            } else if (originalDex == 18) {
                _originalEr = 3;
            } else {
                _originalEr = 0;
            }
        } else if (isKnight()) {
            if (originalDex == 14 || originalDex == 15) {
                _originalEr = 1;
            } else if (originalDex == 16) {
                _originalEr = 3;
            } else {
                _originalEr = 0;
            }
        } else if (isElf()) {
            _originalEr = 0;
        } else if (isDarkelf()) {
            if (originalDex >= 16) {
                _originalEr = 2;
            } else {
                _originalEr = 0;
            }
        } else if (isWizard()) {
            if (originalDex == 9 || originalDex == 10) {
                _originalEr = 1;
            } else if (originalDex == 11) {
                _originalEr = 2;
            } else {
                _originalEr = 0;
            }
        } else if (isDragonKnight()) {
            if (originalDex == 13 || originalDex == 14) {
                _originalEr = 1;
            } else if (originalDex >= 15) {
                _originalEr = 2;
            } else {
                _originalEr = 0;
            }
        } else if (isIllusionist()) {
            if (originalDex == 12 || originalDex == 13) {
                _originalEr = 1;
            } else if (originalDex >= 14) {
                _originalEr = 2;
            } else {
                _originalEr = 0;
            }
        }
    }

    public void resetOriginalHitup() {
        int originalStr = getOriginalStr();
        if (isCrown()) {
            if (originalStr >= 16 && originalStr <= 18) {
                _originalHitup = 1;
            } else if (originalStr >= 19) {
                _originalHitup = 2;
            } else {
                _originalHitup = 0;
            }
        } else if (isKnight()) {
            if (originalStr == 17 || originalStr == 18) {
                _originalHitup = 2;
            } else if (originalStr >= 19) {
                _originalHitup = 4;
            } else {
                _originalHitup = 0;
            }
        } else if (isElf()) {
            if (originalStr == 13 || originalStr == 14) {
                _originalHitup = 1;
            } else if (originalStr >= 15) {
                _originalHitup = 2;
            } else {
                _originalHitup = 0;
            }
        } else if (isDarkelf()) {
            if (originalStr >= 15 && originalStr <= 17) {
                _originalHitup = 1;
            } else if (originalStr == 18) {
                _originalHitup = 2;
            } else {
                _originalHitup = 0;
            }
        } else if (isWizard()) {
            if (originalStr == 11 || originalStr == 12) {
                _originalHitup = 1;
            } else if (originalStr >= 13) {
                _originalHitup = 2;
            } else {
                _originalHitup = 0;
            }
        } else if (isDragonKnight()) {
            if (originalStr >= 14 && originalStr <= 16) {
                _originalHitup = 1;
            } else if (originalStr >= 17) {
                _originalHitup = 3;
            } else {
                _originalHitup = 0;
            }
        } else if (isIllusionist()) {
            if (originalStr == 12 || originalStr == 13) {
                _originalHitup = 1;
            } else if (originalStr == 14 || originalStr == 15) {
                _originalHitup = 2;
            } else if (originalStr == 16) {
                _originalHitup = 3;
            } else if (originalStr >= 17) {
                _originalHitup = 4;
            } else {
                _originalHitup = 0;
            }
        }
    }

    public void resetOriginalHpr() {
        int originalCon = getOriginalCon();
        if (isCrown()) {
            if (originalCon == 13 || originalCon == 14) {
                _originalHpr = 1;
            } else if (originalCon == 15 || originalCon == 16) {
                _originalHpr = 2;
            } else if (originalCon == 17) {
                _originalHpr = 3;
            } else if (originalCon == 18) {
                _originalHpr = 4;
            } else {
                _originalHpr = 0;
            }
        } else if (isKnight()) {
            if (originalCon == 16 || originalCon == 17) {
                _originalHpr = 2;
            } else if (originalCon == 18) {
                _originalHpr = 4;
            } else {
                _originalHpr = 0;
            }
        } else if (isElf()) {
            if (originalCon == 14 || originalCon == 15) {
                _originalHpr = 1;
            } else if (originalCon == 16) {
                _originalHpr = 2;
            } else if (originalCon >= 17) {
                _originalHpr = 3;
            } else {
                _originalHpr = 0;
            }
        } else if (isDarkelf()) {
            if (originalCon == 11 || originalCon == 12) {
                _originalHpr = 1;
            } else if (originalCon >= 13) {
                _originalHpr = 2;
            } else {
                _originalHpr = 0;
            }
        } else if (isWizard()) {
            if (originalCon == 17) {
                _originalHpr = 1;
            } else if (originalCon == 18) {
                _originalHpr = 2;
            } else {
                _originalHpr = 0;
            }
        } else if (isDragonKnight()) {
            if (originalCon == 16 || originalCon == 17) {
                _originalHpr = 1;
            } else if (originalCon == 18) {
                _originalHpr = 3;
            } else {
                _originalHpr = 0;
            }
        } else if (isIllusionist()) {
            if (originalCon == 14 || originalCon == 15) {
                _originalHpr = 1;
            } else if (originalCon >= 16) {
                _originalHpr = 2;
            } else {
                _originalHpr = 0;
            }
        }
    }

    /**
     * 初期ステータスから現在のボーナスを再計算して設定する 初期設定時、再配分時に呼び出す
     */
    public void resetOriginalHpup() {
        int originalCon = getOriginalCon();
        if (isCrown()) {
            if (originalCon == 12 || originalCon == 13) {
                _originalHpup = 1;
            } else if (originalCon == 14 || originalCon == 15) {
                _originalHpup = 2;
            } else if (originalCon >= 16) {
                _originalHpup = 3;
            } else {
                _originalHpup = 0;
            }
        } else if (isKnight()) {
            if (originalCon == 15 || originalCon == 16) {
                _originalHpup = 1;
            } else if (originalCon >= 17) {
                _originalHpup = 3;
            } else {
                _originalHpup = 0;
            }
        } else if (isElf()) {
            if (originalCon >= 13 && originalCon <= 17) {
                _originalHpup = 1;
            } else if (originalCon == 18) {
                _originalHpup = 2;
            } else {
                _originalHpup = 0;
            }
        } else if (isDarkelf()) {
            if (originalCon == 10 || originalCon == 11) {
                _originalHpup = 1;
            } else if (originalCon >= 12) {
                _originalHpup = 2;
            } else {
                _originalHpup = 0;
            }
        } else if (isWizard()) {
            if (originalCon == 14 || originalCon == 15) {
                _originalHpup = 1;
            } else if (originalCon >= 16) {
                _originalHpup = 2;
            } else {
                _originalHpup = 0;
            }
        } else if (isDragonKnight()) {
            if (originalCon == 15 || originalCon == 16) {
                _originalHpup = 1;
            } else if (originalCon >= 17) {
                _originalHpup = 3;
            } else {
                _originalHpup = 0;
            }
        } else if (isIllusionist()) {
            if (originalCon == 13 || originalCon == 14) {
                _originalHpup = 1;
            } else if (originalCon >= 15) {
                _originalHpup = 2;
            } else {
                _originalHpup = 0;
            }
        }
    }

    public void resetOriginalMagicConsumeReduction() {
        int originalInt = getOriginalInt();
        if (isCrown()) {
            if (originalInt == 11 || originalInt == 12) {
                _originalMagicConsumeReduction = 1;
            } else if (originalInt >= 13) {
                _originalMagicConsumeReduction = 2;
            } else {
                _originalMagicConsumeReduction = 0;
            }
        } else if (isKnight()) {
            if (originalInt == 9 || originalInt == 10) {
                _originalMagicConsumeReduction = 1;
            } else if (originalInt >= 11) {
                _originalMagicConsumeReduction = 2;
            } else {
                _originalMagicConsumeReduction = 0;
            }
        } else if (isElf()) {
            _originalMagicConsumeReduction = 0;
        } else if (isDarkelf()) {
            if (originalInt == 13 || originalInt == 14) {
                _originalMagicConsumeReduction = 1;
            } else if (originalInt >= 15) {
                _originalMagicConsumeReduction = 2;
            } else {
                _originalMagicConsumeReduction = 0;
            }
        } else if (isWizard()) {
            _originalMagicConsumeReduction = 0;
        } else if (isDragonKnight()) {
            _originalMagicConsumeReduction = 0;
        } else if (isIllusionist()) {
            if (originalInt == 14) {
                _originalMagicConsumeReduction = 1;
            } else if (originalInt >= 15) {
                _originalMagicConsumeReduction = 2;
            } else {
                _originalMagicConsumeReduction = 0;
            }
        }
    }

    public void resetOriginalMagicCritical() {
        int originalInt = getOriginalInt();
        if (isCrown()) {
            _originalMagicCritical = 0;
        } else if (isKnight()) {
            _originalMagicCritical = 0;
        } else if (isElf()) {
            if (originalInt == 14 || originalInt == 15) {
                _originalMagicCritical = 2;
            } else if (originalInt >= 16) {
                _originalMagicCritical = 4;
            } else {
                _originalMagicCritical = 0;
            }
        } else if (isDarkelf()) {
            _originalMagicCritical = 0;
        } else if (isWizard()) {
            if (originalInt == 15) {
                _originalMagicCritical = 2;
            } else if (originalInt == 16) {
                _originalMagicCritical = 4;
            } else if (originalInt == 17) {
                _originalMagicCritical = 6;
            } else if (originalInt == 18) {
                _originalMagicCritical = 8;
            } else {
                _originalMagicCritical = 0;
            }
        } else if (isDragonKnight()) {
            _originalMagicCritical = 0;
        } else if (isIllusionist()) {
            _originalMagicCritical = 0;
        }
    }

    public void resetOriginalMagicDamage() {
        int originalInt = getOriginalInt();
        if (isCrown()) {
            _originalMagicDamage = 0;
        } else if (isKnight()) {
            _originalMagicDamage = 0;
        } else if (isElf()) {
            _originalMagicDamage = 0;
        } else if (isDarkelf()) {
            _originalMagicDamage = 0;
        } else if (isWizard()) {
            if (originalInt >= 13) {
                _originalMagicDamage = 1;
            } else {
                _originalMagicDamage = 0;
            }
        } else if (isDragonKnight()) {
            if (originalInt == 13 || originalInt == 14) {
                _originalMagicDamage = 1;
            } else if (originalInt == 15 || originalInt == 16) {
                _originalMagicDamage = 2;
            } else if (originalInt == 17) {
                _originalMagicDamage = 3;
            } else {
                _originalMagicDamage = 0;
            }
        } else if (isIllusionist()) {
            if (originalInt == 16) {
                _originalMagicDamage = 1;
            } else if (originalInt == 17) {
                _originalMagicDamage = 2;
            } else {
                _originalMagicDamage = 0;
            }
        }
    }

    public void resetOriginalMagicHit() {
        int originalInt = getOriginalInt();
        if (isCrown()) {
            if (originalInt == 12 || originalInt == 13) {
                _originalMagicHit = 1;
            } else if (originalInt >= 14) {
                _originalMagicHit = 2;
            } else {
                _originalMagicHit = 0;
            }
        } else if (isKnight()) {
            if (originalInt == 10 || originalInt == 11) {
                _originalMagicHit = 1;
            } else if (originalInt == 12) {
                _originalMagicHit = 2;
            } else {
                _originalMagicHit = 0;
            }
        } else if (isElf()) {
            if (originalInt == 13 || originalInt == 14) {
                _originalMagicHit = 1;
            } else if (originalInt >= 15) {
                _originalMagicHit = 2;
            } else {
                _originalMagicHit = 0;
            }
        } else if (isDarkelf()) {
            if (originalInt == 12 || originalInt == 13) {
                _originalMagicHit = 1;
            } else if (originalInt >= 14) {
                _originalMagicHit = 2;
            } else {
                _originalMagicHit = 0;
            }
        } else if (isWizard()) {
            if (originalInt >= 14) {
                _originalMagicHit = 1;
            } else {
                _originalMagicHit = 0;
            }
        } else if (isDragonKnight()) {
            if (originalInt == 12 || originalInt == 13) {
                _originalMagicHit = 2;
            } else if (originalInt == 14 || originalInt == 15) {
                _originalMagicHit = 3;
            } else if (originalInt >= 16) {
                _originalMagicHit = 4;
            } else {
                _originalMagicHit = 0;
            }
        } else if (isIllusionist()) {
            if (originalInt >= 13) {
                _originalMagicHit = 1;
            } else {
                _originalMagicHit = 0;
            }
        }
    }

    public void resetOriginalMpr() {
        int originalWis = getOriginalWis();
        if (isCrown()) {
            if (originalWis == 13 || originalWis == 14) {
                _originalMpr = 1;
            } else if (originalWis >= 15) {
                _originalMpr = 2;
            } else {
                _originalMpr = 0;
            }
        } else if (isKnight()) {
            if (originalWis == 11 || originalWis == 12) {
                _originalMpr = 1;
            } else if (originalWis == 13) {
                _originalMpr = 2;
            } else {
                _originalMpr = 0;
            }
        } else if (isElf()) {
            if (originalWis >= 15 && originalWis <= 17) {
                _originalMpr = 1;
            } else if (originalWis == 18) {
                _originalMpr = 2;
            } else {
                _originalMpr = 0;
            }
        } else if (isDarkelf()) {
            if (originalWis >= 13) {
                _originalMpr = 1;
            } else {
                _originalMpr = 0;
            }
        } else if (isWizard()) {
            if (originalWis == 14 || originalWis == 15) {
                _originalMpr = 1;
            } else if (originalWis == 16 || originalWis == 17) {
                _originalMpr = 2;
            } else if (originalWis == 18) {
                _originalMpr = 3;
            } else {
                _originalMpr = 0;
            }
        } else if (isDragonKnight()) {
            if (originalWis == 15 || originalWis == 16) {
                _originalMpr = 1;
            } else if (originalWis >= 17) {
                _originalMpr = 2;
            } else {
                _originalMpr = 0;
            }
        } else if (isIllusionist()) {
            if (originalWis >= 14 && originalWis <= 16) {
                _originalMpr = 1;
            } else if (originalWis >= 17) {
                _originalMpr = 2;
            } else {
                _originalMpr = 0;
            }
        }
    }

    public void resetOriginalMpup() {
        int originalWis = getOriginalWis();
        {
            if (isCrown()) {
                if (originalWis >= 16) {
                    _originalMpup = 1;
                } else {
                    _originalMpup = 0;
                }
            } else if (isKnight()) {
                _originalMpup = 0;
            } else if (isElf()) {
                if (originalWis >= 14 && originalWis <= 16) {
                    _originalMpup = 1;
                } else if (originalWis >= 17) {
                    _originalMpup = 2;
                } else {
                    _originalMpup = 0;
                }
            } else if (isDarkelf()) {
                if (originalWis >= 12) {
                    _originalMpup = 1;
                } else {
                    _originalMpup = 0;
                }
            } else if (isWizard()) {
                if (originalWis >= 13 && originalWis <= 16) {
                    _originalMpup = 1;
                } else if (originalWis >= 17) {
                    _originalMpup = 2;
                } else {
                    _originalMpup = 0;
                }
            } else if (isDragonKnight()) {
                if (originalWis >= 13 && originalWis <= 15) {
                    _originalMpup = 1;
                } else if (originalWis >= 16) {
                    _originalMpup = 2;
                } else {
                    _originalMpup = 0;
                }
            } else if (isIllusionist()) {
                if (originalWis >= 13 && originalWis <= 15) {
                    _originalMpup = 1;
                } else if (originalWis >= 16) {
                    _originalMpup = 2;
                } else {
                    _originalMpup = 0;
                }
            }
        }
    }

    public void resetOriginalMr() {
        int originalWis = getOriginalWis();
        if (isCrown()) {
            if (originalWis == 12 || originalWis == 13) {
                _originalMr = 1;
            } else if (originalWis >= 14) {
                _originalMr = 2;
            } else {
                _originalMr = 0;
            }
        } else if (isKnight()) {
            if (originalWis == 10 || originalWis == 11) {
                _originalMr = 1;
            } else if (originalWis >= 12) {
                _originalMr = 2;
            } else {
                _originalMr = 0;
            }
        } else if (isElf()) {
            if (originalWis >= 13 && originalWis <= 15) {
                _originalMr = 1;
            } else if (originalWis >= 16) {
                _originalMr = 2;
            } else {
                _originalMr = 0;
            }
        } else if (isDarkelf()) {
            if (originalWis >= 11 && originalWis <= 13) {
                _originalMr = 1;
            } else if (originalWis == 14) {
                _originalMr = 2;
            } else if (originalWis == 15) {
                _originalMr = 3;
            } else if (originalWis >= 16) {
                _originalMr = 4;
            } else {
                _originalMr = 0;
            }
        } else if (isWizard()) {
            if (originalWis >= 15) {
                _originalMr = 1;
            } else {
                _originalMr = 0;
            }
        } else if (isDragonKnight()) {
            if (originalWis >= 14) {
                _originalMr = 2;
            } else {
                _originalMr = 0;
            }
        } else if (isIllusionist()) {
            if (originalWis >= 15 && originalWis <= 17) {
                _originalMr = 2;
            } else if (originalWis == 18) {
                _originalMr = 4;
            } else {
                _originalMr = 0;
            }
        }

        addMr(_originalMr);
    }

    public void resetOriginalStrWeightReduction() {
        int originalStr = getOriginalStr();
        if (isCrown()) {
            if (originalStr >= 14 && originalStr <= 16) {
                _originalStrWeightReduction = 1;
            } else if (originalStr >= 17 && originalStr <= 19) {
                _originalStrWeightReduction = 2;
            } else if (originalStr == 20) {
                _originalStrWeightReduction = 3;
            } else {
                _originalStrWeightReduction = 0;
            }
        } else if (isKnight()) {
            _originalStrWeightReduction = 0;
        } else if (isElf()) {
            if (originalStr >= 16) {
                _originalStrWeightReduction = 2;
            } else {
                _originalStrWeightReduction = 0;
            }
        } else if (isDarkelf()) {
            if (originalStr >= 13 && originalStr <= 15) {
                _originalStrWeightReduction = 2;
            } else if (originalStr >= 16) {
                _originalStrWeightReduction = 3;
            } else {
                _originalStrWeightReduction = 0;
            }
        } else if (isWizard()) {
            if (originalStr >= 9) {
                _originalStrWeightReduction = 1;
            } else {
                _originalStrWeightReduction = 0;
            }
        } else if (isDragonKnight()) {
            if (originalStr >= 16) {
                _originalStrWeightReduction = 1;
            } else {
                _originalStrWeightReduction = 0;
            }
        } else if (isIllusionist()) {
            if (originalStr == 18) {
                _originalStrWeightReduction = 1;
            } else {
                _originalStrWeightReduction = 0;
            }
        }
    }

    /**
     * 恢复经验值.
     */
    public void resExp() {
        int oldLevel = getLevel();
        int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        int exp = 0;
        if (oldLevel < 45) {
            exp = (int) (needExp * 0.05);
        } else if (oldLevel == 45) {
            exp = (int) (needExp * 0.045);
        } else if (oldLevel == 46) {
            exp = (int) (needExp * 0.04);
        } else if (oldLevel == 47) {
            exp = (int) (needExp * 0.035);
        } else if (oldLevel == 48) {
            exp = (int) (needExp * 0.03);
        } else if (oldLevel >= 49) {
            exp = (int) (needExp * 0.025);
        }

        if (exp == 0) {
            return;
        }
        addExp(exp);
    }

    /**
     * 将玩家资料存入数据库.
     * 
     * @throws Exception
     */
    public void save() throws Exception {
        if (isGhost()) {
            return;
        }
        if (isInCharReset()) {
            return;
        }
        CharacterTable.getInstance().storeCharacter(this);
    }

    /**
     * 将玩家背包内的道具资料存入数据库.
     */
    public void saveInventory() {
        for (L1ItemInstance item : getInventory().getItems()) {
            getInventory().saveItem(item, item.getRecordingColumns());
        }
    }

    /**
     * 发送封包.
     */
    public void sendPackets(ServerBasePacket serverbasepacket) {
        if (_out == null) {
            return;
        }

        try {
            _out.sendPacket(serverbasepacket);
        } catch (Exception e) {
        }
    }

    /**
     * 发送视觉效果(一般).
     */
    private void sendVisualEffect() {
        int poisonId = 0;
        if (getPoison() != null) { // 毒状態
            poisonId = getPoison().getEffectId();
        }
        if (getParalysis() != null) { // 麻痺状態
            // 麻痺エフェクトを優先して送りたい為、poisonIdを上書き。
            poisonId = getParalysis().getEffectId();
        }
        if (poisonId != 0) { // このifはいらないかもしれない
            sendPackets(new S_Poison(getId(), poisonId));
            broadcastPacket(new S_Poison(getId(), poisonId));
        }
    }

    /**
     * 发送视觉效果(登陆).
     */
    public void sendVisualEffectAtLogin() {
        // S_Emblemの送信はC_Clanに移行
        // for (L1Clan clan : L1World.getInstance().getAllClans()) {
        // sendPackets(new S_Emblem(clan.getClanId()));
        // }

        if (getClanid() != 0) { // クラン所属
            L1Clan clan = L1World.getInstance().getClan(getClanname());
            if (clan != null) {
                if (isCrown() && getId() == clan.getLeaderId() && // プリンスまたはプリンセス、かつ、血盟主で自クランが城主
                        clan.getCastleId() != 0) {
                    sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
                }
            }
        }

        sendVisualEffect();
    }

    /**
     * 发送视觉效果(传送).
     */
    public void sendVisualEffectAtTeleport() {
        if (isDrink()) { // liquorで酔っている
            sendPackets(new S_Liquor(getId()));
        }

        sendVisualEffect();
    }

    /**
     * 设置饱食度.
     * 
     * @param i
     *            - 饱食度
     */
    public void set_food(int i) {
        _food = i;
    }

    /**
     * 设置PK次数.
     * 
     * @param i
     *            - PK次数
     */
    public void set_PKcount(int i) {
        _PKcount = i;
    }

    /**
     * 设置角色的性别(男or女).
     * 
     * @param i
     *            - 性别
     */
    public void set_sex(int i) {
        _sex = (byte) i;
    }

    /**
     * .
     */
    public void setAccessLevel(short i) {
        _accessLevel = i;
    }

    /**
     * .
     */
    public void setAccountName(String s) {
        _accountName = s;
    }

    /**
     * .
     */
    public void setAdvenHp(int i) {
        _advenHp = i;
    }

    /**
     * .
     */
    public void setAdvenMp(int i) {
        _advenMp = i;
    }

    /**
     * .
     */
    public void setAwakeSkillId(int i) {
        _awakeSkillId = i;
    }

    /**
     * .
     */
    public void setBanned(boolean flag) {
        _banned = flag;
    }

    /**
     * .
     */
    public void setBonusStats(int i) {
        _bonusStats = i;
    }

    /**
     * .
     */
    public void setCallClanHeading(int i) {
        _callClanHeading = i;
    }

    /**
     * .
     */
    public void setCallClanId(int i) {
        _callClanId = i;
    }

    public void setCanWhisper(boolean flag) {
        _isCanWhisper = flag;
    }

    public void setChatParty(L1ChatParty cp) {
        _chatParty = cp;
    }

    public void setClanid(int i) {
        _clanid = i;
    }

    public void setClanname(String s) {
        clanname = s;
    }

    public void setClanRank(int i) {
        _clanRank = i;
    }

    public void setClassId(int i) {
        _classId = i;
        _classFeature = L1ClassFeature.newClassFeature(i);
    }

    public void setContribution(int i) {
        _contribution = i;
    }

    public void setCookingId(int i) {
        _cookingId = i;
    }

    @Override
    public void setCurrentHp(int i) {
        if (getCurrentHp() == i) {
            return;
        }
        int currentHp = i;
        if (currentHp >= getMaxHp()) {
            currentHp = getMaxHp();
        }
        setCurrentHpDirect(currentHp);
        sendPackets(new S_HPUpdate(currentHp, getMaxHp()));
        if (isInParty()) { // 组队中
            getParty().updateMiniHP(this);
        }
    }

    @Override
    public void setCurrentMp(int i) {
        if (getCurrentMp() == i) {
            return;
        }
        int currentMp = i;
        if (currentMp >= getMaxMp() || isGm()) {
            currentMp = getMaxMp();
        }
        setCurrentMpDirect(currentMp);
        sendPackets(new S_MPUpdate(currentMp, getMaxMp()));
    }

    public void setCurrentWeapon(int i) {
        _currentWeapon = i;
    }

    public void setDeleteTime(Timestamp time) {
        _deleteTime = time;
    }

    public void setDessertId(int i) {
        _dessertId = i;
    }

    public void setDrink(boolean flag) {
        _isDrink = flag;
    }

    public void setElfAttr(int i) {
        _elfAttr = i;
    }

    public void setElixirStats(int i) {
        _elixirStats = i;
    }

    @Override
    public synchronized void setExp(int i) {
        _exp = i;
    }

    public void setExpRes(int i) {
        _expRes = i;
    }

    public void setFightId(int i) {
        _fightId = i;
    }

    public void setFishing(boolean flag) {
        _isFishing = flag;
    }

    public void setFishingReady(boolean flag) {
        _isFishingReady = flag;
    }

    public void setFishingTime(long i) {
        _fishingTime = i;
    }

    private void setGhost(boolean flag) {
        _ghost = flag;
    }

    private void setGhostCanTalk(boolean flag) {
        _ghostCanTalk = flag;
    }

    public void setGm(boolean flag) {
        _gm = flag;
    }

    public void setGmInvis(boolean flag) {
        _gmInvis = flag;
    }

    public void setGres(boolean flag) {
        _isGres = flag;
    }

    private void setGresValid(boolean valid) {
        _gresValid = valid;
    }

    public void setHellTime(int i) {
        _hellTime = i;
    }

    public void setHighLevel(int i) {
        _highLevel = i;
    }

    public void setHomeTownId(int i) {
        _homeTownId = i;
    }

    public void setInCharReset(boolean flag) {
        _isInCharReset = flag;
    }

    @Override
    public void setKarma(int i) {
        _karma.set(i);
    }

    /**
     * プレイヤーの最終PK時間を現在の時刻に設定する。
     */
    public void setLastPk() {
        _lastPk = new Timestamp(System.currentTimeMillis());
    }

    /**
     * プレイヤーの最終PK時間を設定する。
     * 
     * @param time
     *            最終PK時間（Timestamp型） 解除する場合はnullを代入
     */
    public void setLastPk(Timestamp time) {
        _lastPk = time;
    }

    public void setLastPkForElf() {
        _lastPkForElf = new Timestamp(System.currentTimeMillis());
    }

    public void setLastPkForElf(Timestamp time) {
        _lastPkForElf = time;
    }

    public void setMonitor(boolean flag) {
        _monitor = flag;
    }

    public void setNetConnection(ClientThread clientthread) {
        _netConnection = clientthread;
    }

    public void setOnlineStatus(int i) {
        _onlineStatus = i;
    }

    public void setOriginalCha(int i) {
        _originalCha = i;
    }

    public void setOriginalCon(int i) {
        _originalCon = i;
    }

    public void setOriginalDex(int i) {
        _originalDex = i;
    }

    public void setOriginalInt(int i) {
        _originalInt = i;
    }

    public void setOriginalStr(int i) {
        _originalStr = i;
    }

    public void setOriginalWis(int i) {
        _originalWis = i;
    }

    public void setPacketOutput(PacketOutput out) {
        _out = out;
    }

    public void setPartnerId(int i) {
        _partnerId = i;
    }

    public void setPartnersPrivateShopItemCount(int i) {
        _partnersPrivateShopItemCount = i;
    }

    public void setParty(L1Party p) {
        _party = p;
    }

    public void setPartyID(int partyID) {
        _partyID = partyID;
    }

    public void setPetTarget(L1Character target) {
        Object[] petList = getPetList().values().toArray();
        for (Object pet : petList) {
            if (pet instanceof L1PetInstance) {
                L1PetInstance pets = (L1PetInstance) pet;
                pets.setMasterTarget(target);
            } else if (pet instanceof L1SummonInstance) {
                L1SummonInstance summon = (L1SummonInstance) pet;
                summon.setMasterTarget(target);
            }
        }
    }

    public void setPinkName(boolean flag) {
        _isPinkName = flag;
    }

    public void setPkCountForElf(int i) {
        _PkCountForElf = i;
    }

    @Override
    public void setPoisonEffect(int effectId) {
        sendPackets(new S_Poison(getId(), effectId));

        if (!isGmInvis() && !isGhost() && !isInvisble()) {
            broadcastPacket(new S_Poison(getId(), effectId));
        }
        if (isGmInvis() || isGhost()) {
        } else if (isInvisble()) {
            broadcastPacketForFindInvis(new S_Poison(getId(), effectId), true);
        } else {
            broadcastPacket(new S_Poison(getId(), effectId));
        }
    }

    public void setPrivateShop(boolean flag) {
        _isPrivateShop = flag;
    }

    public void setRegenState(int state) {
        _mpRegen.setState(state);
        _hpRegen.setState(state);
    }

    private void setReserveGhost(boolean flag) {
        _isReserveGhost = flag;
    }

    public void setShapeChange(boolean isShapeChange) {
        _isShapeChange = isShapeChange;
    }

    public void setShopChat(byte[] chat) {
        _shopChat = chat;
    }

    public void setShowTradeChat(boolean flag) {
        _isShowTradeChat = flag;
    }

    public void setShowWorldChat(boolean flag) {
        _isShowWorldChat = flag;
    }

    public void setSkillMastery(int skillid) {
        if (!skillList.contains(skillid)) {
            skillList.add(skillid);
        }
    }

    public void setSummonMonster(boolean SummonMonster) {
        _isSummonMonster = SummonMonster;
    }

    public void setTeleport(boolean flag) {
        _isTeleport = flag;
    }

    public void setTeleportHeading(int i) {
        _teleportHeading = i;
    }

    public void setTeleportMapId(int i) {
        _teleportMapId = i;
    }

    public void setTeleportX(int i) {
        _teleportX = i;
    }

    public void setTeleportY(int i) {
        _teleportY = i;
    }

    public void setTempCharGfxAtDead(int i) {
        _tempCharGfxAtDead = i;
    }

    public void setTempID(int tempID) {
        _tempID = tempID;
    }

    public void setTempLevel(int i) {
        _tempLevel = i;
    }

    public void setTempMaxLevel(int i) {
        _tempMaxLevel = i;
    }

    public void setTradeID(int tradeID) {
        _tradeID = tradeID;
    }

    public void setTradeOk(boolean tradeOk) {
        _tradeOk = tradeOk;
    }

    public void setTradingInPrivateShop(boolean flag) {
        _isTradingInPrivateShop = flag;
    }

    public void setType(int i) {
        _type = i;
    }

    public void setWeapon(L1ItemInstance weapon) {
        _weapon = weapon;
    }

    public boolean simWarResult(L1Character lastAttacker) {
        if (getClanid() == 0) { // クラン所属していない
            return false;
        }
        if (Config.SIM_WAR_PENALTY) { // 模擬戦ペナルティありの場合はfalse
            return false;
        }
        L1PcInstance attacker = null;
        String enemyClanName = null;
        boolean sameWar = false;

        if (lastAttacker instanceof L1PcInstance) {
            attacker = (L1PcInstance) lastAttacker;
        } else if (lastAttacker instanceof L1PetInstance) {
            attacker = (L1PcInstance) ((L1PetInstance) lastAttacker)
                    .getMaster();
        } else if (lastAttacker instanceof L1SummonInstance) {
            attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker)
                    .getMaster();
        } else {
            return false;
        }

        // 全戦争リストを取得
        for (L1War war : L1World.getInstance().getWarList()) {
            L1Clan clan = L1World.getInstance().getClan(getClanname());

            int warType = war.GetWarType();
            boolean isInWar = war.CheckClanInWar(getClanname());
            if (attacker != null && attacker.getClanid() != 0) { // lastAttackerがPC、サモン、ペットでクラン所属中
                sameWar = war.CheckClanInSameWar(getClanname(),
                        attacker.getClanname());
            }

            if (getId() == clan.getLeaderId() && // 血盟主で模擬戦中
                    warType == 2 && isInWar == true) {
                enemyClanName = war.GetEnemyClanName(getClanname());
                if (enemyClanName != null) {
                    war.CeaseWar(getClanname(), enemyClanName); // 終結
                }
            }

            if (warType == 2 && sameWar) {// 模擬戦で同じ戦争に参加中の場合、ペナルティなし
                return true;
            }
        }
        return false;
    }

    public void startHpRegeneration() {
        final int INTERVAL = 1000;

        if (!_hpRegenActive) {
            _hpRegen = new HpRegeneration(this);
            _regenTimer.scheduleAtFixedRate(_hpRegen, INTERVAL, INTERVAL);
            _hpRegenActive = true;
        }
    }

    public void startMpReductionByAwake() {
        final int INTERVAL_BY_AWAKE = 4000;
        if (!_mpReductionActiveByAwake) {
            _mpReductionByAwake = new MpReductionByAwake(this);
            _regenTimer.scheduleAtFixedRate(_mpReductionByAwake,
                    INTERVAL_BY_AWAKE, INTERVAL_BY_AWAKE);
            _mpReductionActiveByAwake = true;
        }
    }

    public void startMpRegeneration() {
        final int INTERVAL = 1000;

        if (!_mpRegenActive) {
            _mpRegen = new MpRegeneration(this);
            _regenTimer.scheduleAtFixedRate(_mpRegen, INTERVAL, INTERVAL);
            _mpRegenActive = true;
        }
    }

    public void startMpRegenerationByDoll() {
        final int INTERVAL_BY_DOLL = 60000;
        boolean isExistMprDoll = false;
        Object[] dollList = getDollList().values().toArray();
        for (Object dollObject : dollList) {
            L1DollInstance doll = (L1DollInstance) dollObject;
            if (doll.isMpRegeneration()) {
                isExistMprDoll = true;
            }
        }
        if (!_mpRegenActiveByDoll && isExistMprDoll) {
            _mpRegenByDoll = new MpRegenerationByDoll(this);
            _regenTimer.scheduleAtFixedRate(_mpRegenByDoll, INTERVAL_BY_DOLL,
                    INTERVAL_BY_DOLL);
            _mpRegenActiveByDoll = true;
        }
    }

    public void startObjectAutoUpdate() {
        removeAllKnownObjects();
        _autoUpdateFuture = GeneralThreadPool.getInstance()
                .pcScheduleAtFixedRate(new L1PcAutoUpdate(getId()), 0L,
                        INTERVAL_AUTO_UPDATE);
    }

    /**
     * 各種モニタータスクを停止する。
     */
    public void stopEtcMonitor() {
        if (_autoUpdateFuture != null) {
            _autoUpdateFuture.cancel(true);
            _autoUpdateFuture = null;
        }
        if (_expMonitorFuture != null) {
            _expMonitorFuture.cancel(true);
            _expMonitorFuture = null;
        }
        if (_ghostFuture != null) {
            _ghostFuture.cancel(true);
            _ghostFuture = null;
        }

        if (_hellFuture != null) {
            _hellFuture.cancel(true);
            _hellFuture = null;
        }

    }

    public void stopHpRegeneration() {
        if (_hpRegenActive) {
            _hpRegen.cancel();
            _hpRegen = null;
            _hpRegenActive = false;
        }
    }

    public void stopMpReductionByAwake() {
        if (_mpReductionActiveByAwake) {
            _mpReductionByAwake.cancel();
            _mpReductionByAwake = null;
            _mpReductionActiveByAwake = false;
        }
    }

    public void stopMpRegeneration() {
        if (_mpRegenActive) {
            _mpRegen.cancel();
            _mpRegen = null;
            _mpRegenActive = false;
        }
    }

    public void stopMpRegenerationByDoll() {
        if (_mpRegenActiveByDoll) {
            _mpRegenByDoll.cancel();
            _mpRegenByDoll = null;
            _mpRegenActiveByDoll = false;
        }
    }

    public void stopPcDeleteTimer() {
        if (_pcDeleteTimer != null) {
            _pcDeleteTimer.cancel();
            _pcDeleteTimer = null;
        }
    }

    // オブジェクト認識処理
    public void updateObject() {
        removeOutOfRangeObjects();

        // 認識範囲内のオブジェクトリストを作成
        for (L1Object visible : L1World.getInstance().getVisibleObjects(this,
                Config.PC_RECOGNIZE_RANGE)) {
            if (!knownsObject(visible)) {
                visible.onPerceive(this);
            } else {
                if (visible instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) visible;
                    if (getLocation().isInScreen(npc.getLocation())
                            && npc.getHiddenStatus() != 0) {
                        npc.approachPlayer(this);
                    }
                }
            }
            if (hasSkillEffect(GMSTATUS_HPBAR)
                    && L1HpBar.isHpBarTarget(visible)) {
                sendPackets(new S_HPMeter((L1Character) visible));
            }
        }
    }

}
