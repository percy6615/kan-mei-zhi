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
package l1j.jrwz.server.model.skill;

import static l1j.jrwz.server.model.skill.L1SkillId.*;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.server.GeneralThreadPool;
import l1j.jrwz.server.datatables.SkillsTable;
import l1j.jrwz.server.model.L1Character;
import l1j.jrwz.server.model.L1PolyMorph;
import l1j.jrwz.server.model.Instance.L1MonsterInstance;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.Instance.L1SummonInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_CurseBlind;
import l1j.jrwz.server.serverpackets.S_Dexup;
import l1j.jrwz.server.serverpackets.S_HPUpdate;
import l1j.jrwz.server.serverpackets.S_MPUpdate;
import l1j.jrwz.server.serverpackets.S_OwnCharAttrDef;
import l1j.jrwz.server.serverpackets.S_OwnCharStatus;
import l1j.jrwz.server.serverpackets.S_PacketBox;
import l1j.jrwz.server.serverpackets.S_Paralysis;
import l1j.jrwz.server.serverpackets.S_Poison;
import l1j.jrwz.server.serverpackets.S_SPMR;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.serverpackets.S_SkillBrave;
import l1j.jrwz.server.serverpackets.S_SkillHaste;
import l1j.jrwz.server.serverpackets.S_SkillIconAura;
import l1j.jrwz.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.jrwz.server.serverpackets.S_SkillIconShield;
import l1j.jrwz.server.serverpackets.S_SkillIconWindShackle;
import l1j.jrwz.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.jrwz.server.serverpackets.S_Strup;
import l1j.jrwz.server.templates.L1Skills;

/*
 * XXX 2008/02/13 vala 本来、このクラスはあるべきではないが暫定処置。
 */
class L1SkillStop {
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1SkillStop.class.getName());

    // メッセージの表示（終了するとき）
    private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
        L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillid);
        if (l1skills == null || charaPc == null) {
            return;
        }

        int msgID = l1skills.getSysmsgIdStop();
        if (msgID > 0) {
            charaPc.sendPackets(new S_ServerMessage(msgID));
        }
    }

    public static void stopSkill(L1Character cha, int skillId) {
        if (skillId == LIGHT) { // ライト
            if (cha instanceof L1PcInstance) {
                if (!cha.isInvisble()) {
                    L1PcInstance pc = (L1PcInstance) cha;
                    pc.turnOnOffLight();
                }
            }
        } else if (skillId == GLOWING_AURA) { // グローウィング オーラ
            cha.addHitup(-5);
            cha.addBowHitup(-5);
            cha.addMr(-20);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SPMR(pc));
                pc.sendPackets(new S_SkillIconAura(113, 0));
            }
        } else if (skillId == SHINING_AURA) { // シャイニング オーラ
            cha.addAc(8);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(114, 0));
            }
        } else if (skillId == BRAVE_AURA) { // ブレイブ オーラ
            cha.addDmgup(-5);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(116, 0));
            }
        } else if (skillId == SHIELD) { // シールド
            cha.addAc(2);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconShield(5, 0));
            }
        } else if (skillId == BLIND_HIDING) { // ブラインドハイディング
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.delBlindHiding();
            }
        } else if (skillId == SHADOW_ARMOR) { // シャドウ アーマー
            cha.addAc(3);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconShield(3, 0));
            }
        } else if (skillId == DRESS_DEXTERITY) { // ドレス デクスタリティー
            cha.addDex((byte) -2);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Dexup(pc, 2, 0));
            }
        } else if (skillId == DRESS_MIGHTY) { // ドレス マイティー
            cha.addStr((byte) -2);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Strup(pc, 2, 0));
            }
        } else if (skillId == SHADOW_FANG) { // シャドウ ファング
            cha.addDmgup(-5);
        } else if (skillId == ENCHANT_WEAPON) { // エンチャント ウェポン
            cha.addDmgup(-2);
        } else if (skillId == BLESSED_ARMOR) { // ブレスド アーマー
            cha.addAc(3);
        } else if (skillId == EARTH_BLESS) { // アース ブレス
            cha.addAc(7);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconShield(7, 0));
            }
        } else if (skillId == RESIST_MAGIC) { // レジスト マジック
            cha.addMr(-10);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SPMR(pc));
            }
        } else if (skillId == CLEAR_MIND) { // クリアー マインド
            cha.addWis((byte) -3);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.resetBaseMr();
            }
        } else if (skillId == RESIST_ELEMENTAL) { // レジスト エレメント
            cha.addWind(-10);
            cha.addWater(-10);
            cha.addFire(-10);
            cha.addEarth(-10);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_OwnCharAttrDef(pc));
            }
        } else if (skillId == ELEMENTAL_PROTECTION) { // エレメンタルプロテクション
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                int attr = pc.getElfAttr();
                if (attr == 1) {
                    cha.addEarth(-50);
                } else if (attr == 2) {
                    cha.addFire(-50);
                } else if (attr == 4) {
                    cha.addWater(-50);
                } else if (attr == 8) {
                    cha.addWind(-50);
                }
                pc.sendPackets(new S_OwnCharAttrDef(pc));
            }
        } else if (skillId == ELEMENTAL_FALL_DOWN) { // エレメンタルフォールダウン
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                int attr = pc.getAddAttrKind();
                int i = 50;
                switch (attr) {
                    case 1:
                        pc.addEarth(i);
                        break;
                    case 2:
                        pc.addFire(i);
                        break;
                    case 4:
                        pc.addWater(i);
                        break;
                    case 8:
                        pc.addWind(i);
                        break;
                    default:
                        break;
                }
                pc.setAddAttrKind(0);
                pc.sendPackets(new S_OwnCharAttrDef(pc));
            } else if (cha instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                int attr = npc.getAddAttrKind();
                int i = 50;
                switch (attr) {
                    case 1:
                        npc.addEarth(i);
                        break;
                    case 2:
                        npc.addFire(i);
                        break;
                    case 4:
                        npc.addWater(i);
                        break;
                    case 8:
                        npc.addWind(i);
                        break;
                    default:
                        break;
                }
                npc.setAddAttrKind(0);
            }
        } else if (skillId == IRON_SKIN) { // アイアン スキン
            cha.addAc(10);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconShield(10, 0));
            }
        } else if (skillId == EARTH_SKIN) { // アース スキン
            cha.addAc(6);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconShield(6, 0));
            }
        } else if (skillId == PHYSICAL_ENCHANT_STR) { // フィジカル エンチャント：STR
            cha.addStr((byte) -5);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Strup(pc, 5, 0));
            }
        } else if (skillId == PHYSICAL_ENCHANT_DEX) { // フィジカル エンチャント：DEX
            cha.addDex((byte) -5);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Dexup(pc, 5, 0));
            }
        } else if (skillId == FIRE_WEAPON) { // ファイアー ウェポン
            cha.addDmgup(-4);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(147, 0));
            }
        } else if (skillId == FIRE_BLESS) { // ファイアー ブレス
            cha.addDmgup(-4);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(154, 0));
            }
        } else if (skillId == BURNING_WEAPON) { // バーニング ウェポン
            cha.addDmgup(-6);
            cha.addHitup(-3);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(162, 0));
            }
        } else if (skillId == BLESS_WEAPON) { // ブレス ウェポン
            cha.addDmgup(-2);
            cha.addHitup(-2);
            cha.addBowHitup(-2);
        } else if (skillId == WIND_SHOT) { // ウィンド ショット
            cha.addBowHitup(-6);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(148, 0));
            }
        } else if (skillId == STORM_EYE) { // ストーム アイ
            cha.addBowHitup(-2);
            cha.addBowDmgup(-3);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(155, 0));
            }
        } else if (skillId == STORM_SHOT) { // ストーム ショット
            cha.addBowDmgup(-5);
            cha.addBowHitup(1);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconAura(165, 0));
            }
        } else if (skillId == BERSERKERS) { // バーサーカー
            cha.addAc(-10);
            cha.addDmgup(-5);
            cha.addHitup(-2);
        } else if (skillId == SHAPE_CHANGE) { // シェイプ チェンジ
            L1PolyMorph.undoPoly(cha);
        } else if (skillId == ADVANCE_SPIRIT) { // アドバンスド スピリッツ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMaxHp(-pc.getAdvenHp());
                pc.addMaxMp(-pc.getAdvenMp());
                pc.setAdvenHp(0);
                pc.setAdvenMp(0);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) { // パーティー中
                    pc.getParty().updateMiniHP(pc);
                }
                pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
            }
        } else if (skillId == HASTE || skillId == GREATER_HASTE) { // ヘイスト、グレーターヘイスト
            cha.setMoveSpeed(0);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
            }
        } else if (skillId == HOLY_WALK || skillId == MOVING_ACCELERATION
                || skillId == WIND_WALK || skillId == BLOODLUST) { // ホーリーウォーク、ムービングアクセレーション、ウィンドウォーク、ブラッドラスト
            cha.setBraveSpeed(0);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
                pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            }
        } else if (skillId == ILLUSION_OGRE) { // イリュージョン：オーガ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addDmgup(-4);
                pc.addHitup(-4);
                pc.addBowDmgup(-4);
                pc.addBowHitup(-4);
            }
        } else if (skillId == ILLUSION_LICH) { // イリュージョン：リッチ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addSp(-2);
                pc.sendPackets(new S_SPMR(pc));
            }
        } else if (skillId == ILLUSION_DIA_GOLEM) { // イリュージョン：ダイアモンドゴーレム
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addAc(20);
            }
        } else if (skillId == ILLUSION_AVATAR) { // イリュージョン：アバター
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addDmgup(-10);
                pc.addBowDmgup(-10);
            }
        } else if (skillId == INSIGHT) { // インサイト
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addStr((byte) -1);
                pc.addCon((byte) -1);
                pc.addDex((byte) -1);
                pc.addWis((byte) -1);
                pc.addInt((byte) -1);
            }
        }

        // ****** 状態変化が解けた場合
        else if (skillId == CURSE_BLIND || skillId == DARKNESS) {
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_CurseBlind(0));
            }
        } else if (skillId == CURSE_PARALYZE) { // カーズ パラライズ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Poison(pc.getId(), 0));
                pc.broadcastPacket(new S_Poison(pc.getId(), 0));
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS,
                        false));
            }
        } else if (skillId == WEAKNESS) { // ウィークネス
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addDmgup(5);
                pc.addHitup(1);
            }
        } else if (skillId == DISEASE) { // ディジーズ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addDmgup(6);
                pc.addAc(-12);
            }
        } else if (skillId == ICE_LANCE // アイスランス
                || skillId == FREEZING_BLIZZARD // フリージングブリザード
                || skillId == FREEZING_BREATH) { // フリージングブレス
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Poison(pc.getId(), 0));
                pc.broadcastPacket(new S_Poison(pc.getId(), 0));
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
            } else if (cha instanceof L1MonsterInstance
                    || cha instanceof L1SummonInstance
                    || cha instanceof L1PetInstance) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                npc.broadcastPacket(new S_Poison(npc.getId(), 0));
                npc.setParalyzed(false);
            }
        } else if (skillId == EARTH_BIND) { // アースバインド
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Poison(pc.getId(), 0));
                pc.broadcastPacket(new S_Poison(pc.getId(), 0));
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
            } else if (cha instanceof L1MonsterInstance
                    || cha instanceof L1SummonInstance
                    || cha instanceof L1PetInstance) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                npc.broadcastPacket(new S_Poison(npc.getId(), 0));
                npc.setParalyzed(false);
            }
        } else if (skillId == SHOCK_STUN) { // ショック スタン
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
            } else if (cha instanceof L1MonsterInstance
                    || cha instanceof L1SummonInstance
                    || cha instanceof L1PetInstance) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                npc.setParalyzed(false);
            }
        } else if (skillId == FOG_OF_SLEEPING) { // フォグ オブ スリーピング
            cha.setSleeped(false);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false));
                pc.sendPackets(new S_OwnCharStatus(pc));
            }
        } else if (skillId == ABSOLUTE_BARRIER) { // アブソルート バリア
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.startHpRegeneration();
                pc.startMpRegeneration();
                pc.startMpRegenerationByDoll();
            }
        } else if (skillId == WIND_SHACKLE) { // ウィンド シャックル
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0));
            }
        } else if (skillId == SLOW || skillId == ENTANGLE
                || skillId == MASS_SLOW) { // スロー、エンタングル、マススロー
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
            }
            cha.setMoveSpeed(0);
        } else if (skillId == STATUS_FREEZE) { // Freeze
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
            } else if (cha instanceof L1MonsterInstance
                    || cha instanceof L1SummonInstance
                    || cha instanceof L1PetInstance) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                npc.setParalyzed(false);
            }
        } else if (skillId == GUARD_BRAKE) { // ガードブレイク
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addAc(-15);
            }
        } else if (skillId == HORROR_OF_DEATH) { // ホラーオブデス
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addStr(5);
                pc.addInt(5);
            }
        } else if (skillId == STATUS_CUBE_IGNITION_TO_ALLY) { // キューブ[イグニション]：味方
            cha.addFire(-30);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_OwnCharAttrDef(pc));
            }
        } else if (skillId == STATUS_CUBE_QUAKE_TO_ALLY) { // キューブ[クエイク]：味方
            cha.addEarth(-30);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_OwnCharAttrDef(pc));
            }
        } else if (skillId == STATUS_CUBE_SHOCK_TO_ALLY) { // キューブ[ショック]：味方
            cha.addWind(-30);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_OwnCharAttrDef(pc));
            }
        } else if (skillId == STATUS_CUBE_IGNITION_TO_ENEMY) { // キューブ[イグニション]：敵
        } else if (skillId == STATUS_CUBE_QUAKE_TO_ENEMY) { // キューブ[クエイク]：敵
        } else if (skillId == STATUS_CUBE_SHOCK_TO_ENEMY) { // キューブ[ショック]：敵
        } else if (skillId == STATUS_MR_REDUCTION_BY_CUBE_SHOCK) { // キューブ[ショック]によるMR減少
            // cha.addMr(10);
            // if (cha instanceof L1PcInstance) {
            // L1PcInstance pc = (L1PcInstance) cha;
            // pc.sendPackets(new S_SPMR(pc));
            // }
        } else if (skillId == STATUS_CUBE_BALANCE) { // キューブ[バランス]
        }

        // ****** アイテム関係
        else if (skillId == STATUS_BRAVE || skillId == STATUS_ELFBRAVE) { // ブレイブポーション等
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
                pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
            }
            cha.setBraveSpeed(0);
        } else if (skillId == STATUS_RIBRAVE) { // ユグドラの実
            if (cha instanceof L1PcInstance) {
                // L1PcInstance pc = (L1PcInstance) cha;
                // XXX ユグドラの実のアイコンを消す方法が不明
            }
            cha.setBraveSpeed(0);
        } else if (skillId == STATUS_HASTE) { // グリーン ポーション
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
                pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
            }
            cha.setMoveSpeed(0);
        } else if (skillId == STATUS_BLUE_POTION) { // ブルー ポーション
        } else if (skillId == STATUS_UNDERWATER_BREATH) { // エヴァの祝福＆マーメイドの鱗
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));
            }
        } else if (skillId == STATUS_WISDOM_POTION) { // ウィズダム ポーション
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                cha.addSp(-2);
                pc.sendPackets(new S_SkillIconWisdomPotion(0));
            }
        } else if (skillId == STATUS_CHAT_PROHIBITED) { // チャット禁止
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$288)); // チャットができるようになりました。
            }
        }

        // ****** 毒関係
        else if (skillId == STATUS_POISON) { // ダメージ毒
            cha.curePoison();
        }

        // ****** 料理関係
        else if (skillId == COOKING_1_0_N || skillId == COOKING_1_0_S) { // フローティングアイステーキ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addWind(-10);
                pc.addWater(-10);
                pc.addFire(-10);
                pc.addEarth(-10);
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                pc.sendPackets(new S_PacketBox(53, 0, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_1_N || skillId == COOKING_1_1_S) { // ベアーステーキ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMaxHp(-30);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) { // パーティー中
                    pc.getParty().updateMiniHP(pc);
                }
                pc.sendPackets(new S_PacketBox(53, 1, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_2_N || skillId == COOKING_1_2_S) { // ナッツ餅
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 2, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_3_N || skillId == COOKING_1_3_S) { // 蟻脚のチーズ焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addAc(1);
                pc.sendPackets(new S_PacketBox(53, 3, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_4_N || skillId == COOKING_1_4_S) { // フルーツサラダ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMaxMp(-20);
                pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                pc.sendPackets(new S_PacketBox(53, 4, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_5_N || skillId == COOKING_1_5_S) { // フルーツ甘酢あんかけ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 5, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_6_N || skillId == COOKING_1_6_S) { // 猪肉の串焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMr(-5);
                pc.sendPackets(new S_SPMR(pc));
                pc.sendPackets(new S_PacketBox(53, 6, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_1_7_N || skillId == COOKING_1_7_S) { // キノコスープ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 7, 0));
                pc.setDessertId(0);
            }
        } else if (skillId == COOKING_2_0_N || skillId == COOKING_2_0_S) { // キャビアカナッペ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 8, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_1_N || skillId == COOKING_2_1_S) { // アリゲーターステーキ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMaxHp(-30);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) { // パーティー中
                    pc.getParty().updateMiniHP(pc);
                }
                pc.addMaxMp(-30);
                pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                pc.sendPackets(new S_PacketBox(53, 9, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_2_N || skillId == COOKING_2_2_S) { // タートルドラゴンの菓子
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addAc(2);
                pc.sendPackets(new S_PacketBox(53, 10, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_3_N || skillId == COOKING_2_3_S) { // キウィパロット焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 11, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_4_N || skillId == COOKING_2_4_S) { // スコーピオン焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 12, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_5_N || skillId == COOKING_2_5_S) { // イレッカドムシチュー
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMr(-10);
                pc.sendPackets(new S_SPMR(pc));
                pc.sendPackets(new S_PacketBox(53, 13, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_6_N || skillId == COOKING_2_6_S) { // クモ脚の串焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addSp(-1);
                pc.sendPackets(new S_SPMR(pc));
                pc.sendPackets(new S_PacketBox(53, 14, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_2_7_N || skillId == COOKING_2_7_S) { // クラブスープ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 15, 0));
                pc.setDessertId(0);
            }
        } else if (skillId == COOKING_3_0_N || skillId == COOKING_3_0_S) { // クラスタシアンのハサミ焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 16, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_1_N || skillId == COOKING_3_1_S) { // グリフォン焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMaxHp(-50);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) { // パーティー中
                    pc.getParty().updateMiniHP(pc);
                }
                pc.addMaxMp(-50);
                pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                pc.sendPackets(new S_PacketBox(53, 17, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_2_N || skillId == COOKING_3_2_S) { // コカトリスステーキ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 18, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_3_N || skillId == COOKING_3_3_S) { // タートルドラゴン焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addAc(3);
                pc.sendPackets(new S_PacketBox(53, 19, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_4_N || skillId == COOKING_3_4_S) { // レッサードラゴンの手羽先
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMr(-15);
                pc.sendPackets(new S_SPMR(pc));
                pc.addWind(-10);
                pc.addWater(-10);
                pc.addFire(-10);
                pc.addEarth(-10);
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                pc.sendPackets(new S_PacketBox(53, 20, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_5_N || skillId == COOKING_3_5_S) { // ドレイク焼き
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addSp(-2);
                pc.sendPackets(new S_SPMR(pc));
                pc.sendPackets(new S_PacketBox(53, 21, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_6_N || skillId == COOKING_3_6_S) { // 深海魚のシチュー
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.addMaxHp(-30);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) { // パーティー中
                    pc.getParty().updateMiniHP(pc);
                }
                pc.sendPackets(new S_PacketBox(53, 22, 0));
                pc.setCookingId(0);
            }
        } else if (skillId == COOKING_3_7_N || skillId == COOKING_3_7_S) { // バシリスクの卵スープ
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PacketBox(53, 23, 0));
                pc.setDessertId(0);
            }
        }

        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            sendStopMessage(pc, skillId);
            pc.sendPackets(new S_OwnCharStatus(pc));
        }
    }
}

/**
 * 技能计时器接口.
 */
public interface L1SkillTimer {
    /** 开始. */
    public void begin();

    /** 结束. */
    public void end();

    /** 取得剩余时间. */
    public int getRemainingTime();

    /** 删除. */
    public void kill();
}

class L1SkillTimerThreadImpl extends Thread implements L1SkillTimer {
    private final L1Character _cha;

    private final int _timeMillis;

    private final int _skillId;

    private int _remainingTime;

    public L1SkillTimerThreadImpl(L1Character cha, int skillId, int timeMillis) {
        _cha = cha;
        _skillId = skillId;
        _timeMillis = timeMillis;
    }

    @Override
    public void begin() {
        GeneralThreadPool.getInstance().execute(this);
    }

    @Override
    public void end() {
        super.interrupt();
        L1SkillStop.stopSkill(_cha, _skillId);
    }

    @Override
    public int getRemainingTime() {
        return _remainingTime;
    }

    @Override
    public void kill() {
        if (Thread.currentThread().getId() == super.getId()) {
            return; // 如果调用方不停止
        }
        super.interrupt();
    }

    @Override
    public void run() {
        for (int timeCount = _timeMillis / 1000; timeCount > 0; timeCount--) {
            try {
                Thread.sleep(1000);
                _remainingTime = timeCount;
            } catch (InterruptedException e) {
                return;
            }
        }
        _cha.removeSkillEffect(_skillId);
    }
}

class L1SkillTimerTimerImpl implements L1SkillTimer, Runnable {
    private static Logger _log = Logger.getLogger(L1SkillTimerTimerImpl.class
            .getName());
    private ScheduledFuture<?> _future = null;

    private final L1Character _cha;

    private final int _timeMillis;

    private final int _skillId;

    private int _remainingTime;

    public L1SkillTimerTimerImpl(L1Character cha, int skillId, int timeMillis) {
        _cha = cha;
        _skillId = skillId;
        _timeMillis = timeMillis;

        _remainingTime = _timeMillis / 1000;
    }

    @Override
    public void begin() {
        _future = GeneralThreadPool.getInstance().scheduleAtFixedRate(this,
                1000, 1000);
    }

    @Override
    public void end() {
        kill();
        try {
            L1SkillStop.stopSkill(_cha, _skillId);
        } catch (Throwable e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    @Override
    public int getRemainingTime() {
        return _remainingTime;
    }

    @Override
    public void kill() {
        if (_future != null) {
            _future.cancel(false);
        }
    }

    @Override
    public void run() {
        _remainingTime--;
        if (_remainingTime <= 0) {
            _cha.removeSkillEffect(_skillId);
        }
    }
}
