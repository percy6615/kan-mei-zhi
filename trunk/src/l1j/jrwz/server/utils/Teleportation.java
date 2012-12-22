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

package l1j.jrwz.server.utils;

import static l1j.jrwz.server.model.skill.L1SkillId.MEDITATION;
import static l1j.jrwz.server.model.skill.L1SkillId.WIND_SHACKLE;

import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1Location;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1DollInstance;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.Instance.L1SummonInstance;
import l1j.jrwz.server.model.map.L1Map;
import l1j.jrwz.server.model.map.L1WorldMap;
import l1j.jrwz.server.serverpackets.S_CharVisualUpdate;
import l1j.jrwz.server.serverpackets.S_DollPack;
import l1j.jrwz.server.serverpackets.S_MapID;
import l1j.jrwz.server.serverpackets.S_OtherCharPacks;
import l1j.jrwz.server.serverpackets.S_OwnCharPack;
import l1j.jrwz.server.serverpackets.S_PetPack;
import l1j.jrwz.server.serverpackets.S_SkillIconWindShackle;
import l1j.jrwz.server.serverpackets.S_SummonPack;

// Referenced classes of package l1j.jrwz.server.utils:
// FaceToFace

public class Teleportation {

    @SuppressWarnings("unused")
    private static Logger _log = Logger
            .getLogger(Teleportation.class.getName());

    private static Random _random = new Random();

    private static void teleport(L1NpcInstance npc, int x, int y, short map,
            int head) {
        L1World.getInstance().moveVisibleObject(npc, map);

        L1WorldMap.getInstance().getMap(npc.getMapId())
                .setPassable(npc.getX(), npc.getY(), true);
        npc.setX(x);
        npc.setY(y);
        npc.setMap(map);
        npc.setHeading(head);
        L1WorldMap.getInstance().getMap(npc.getMapId())
                .setPassable(npc.getX(), npc.getY(), false);
    }

    public static void Teleportations(L1PcInstance pc) {
        if (pc.isDead() || pc.isTeleport()) {
            return;
        }

        int x = pc.getTeleportX();
        int y = pc.getTeleportY();
        short mapId = pc.getTeleportMapId();
        int head = pc.getTeleportHeading();

        // テレポート先が不正であれば元の座標へ(GMは除く)
        L1Map map = L1WorldMap.getInstance().getMap(mapId);

        if (!map.isInMap(x, y) && !pc.isGm()) {
            x = pc.getX();
            y = pc.getY();
            mapId = pc.getMapId();
        }

        pc.setTeleport(true);

        L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
        if (clan != null) {
            if (clan.getWarehouseUsingChar() == pc.getId()) { // 自キャラがクラン倉庫使用中
                clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
            }
        }

        L1World.getInstance().moveVisibleObject(pc, mapId);
        pc.setLocation(x, y, mapId);
        pc.setHeading(head);
        pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()));

        if (pc.isReserveGhost()) { // ゴースト状態解除
            pc.endGhost();
        }
        if (pc.isGhost() || pc.isGmInvis()) {
        } else if (pc.isInvisble()) {
            pc.broadcastPacketForFindInvis(new S_OtherCharPacks(pc, true), true);
        } else {
            pc.broadcastPacket(new S_OtherCharPacks(pc));
        }
        pc.sendPackets(new S_OwnCharPack(pc));

        pc.removeAllKnownObjects();
        pc.sendVisualEffectAtTeleport(); // クラウン、毒、水中等の視覚効果を表示
        pc.updateObject();
        // spr番号6310, 5641の変身中にテレポートするとテレポート後に移動できなくなる
        // 武器を着脱すると移動できるようになるため、S_CharVisualUpdateを送信する
        pc.sendPackets(new S_CharVisualUpdate(pc));

        pc.killSkillEffectTimer(MEDITATION);
        pc.setCallClanId(0); // 传送后 血盟召唤技能 无效

        /*
         * subjects ペットとサモンのテレポート先画面内へ居たプレイヤー。
         * 各ペット毎にUpdateObjectを行う方がコード上ではスマートだが、
         * ネットワーク負荷が大きくなる為、一旦Setへ格納して最後にまとめてUpdateObjectする。
         */
        HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
        subjects.add(pc);

        if (!pc.isGhost()) {
            if (pc.getMap().isTakePets()) {
                // ペットとサモンも一緒に移動させる。
                for (L1NpcInstance petNpc : pc.getPetList().values()) {
                    // テレポート先の設定
                    L1Location loc = pc.getLocation().randomLocation(3, false);
                    int nx = loc.getX();
                    int ny = loc.getY();
                    if (pc.getMapId() == 5125 || pc.getMapId() == 5131
                            || pc.getMapId() == 5132 || pc.getMapId() == 5133
                            || pc.getMapId() == 5134) { // ペットマッチ会場
                        nx = 32799 + _random.nextInt(5) - 3;
                        ny = 32864 + _random.nextInt(5) - 3;
                    }
                    teleport(petNpc, nx, ny, mapId, head);
                    if (petNpc instanceof L1SummonInstance) { // サモンモンスター
                        L1SummonInstance summon = (L1SummonInstance) petNpc;
                        pc.sendPackets(new S_SummonPack(summon, pc));
                    } else if (petNpc instanceof L1PetInstance) { // ペット
                        L1PetInstance pet = (L1PetInstance) petNpc;
                        pc.sendPackets(new S_PetPack(pet, pc));
                    }

                    for (L1PcInstance visiblePc : L1World.getInstance()
                            .getVisiblePlayer(petNpc)) {
                        // テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
                        visiblePc.removeKnownObject(petNpc);
                        subjects.add(visiblePc);
                    }
                }

                // マジックドールも一緒に移動させる。
                for (L1DollInstance doll : pc.getDollList().values()) {
                    // テレポート先の設定
                    L1Location loc = pc.getLocation().randomLocation(3, false);
                    int nx = loc.getX();
                    int ny = loc.getY();

                    teleport(doll, nx, ny, mapId, head);
                    pc.sendPackets(new S_DollPack(doll, pc));

                    for (L1PcInstance visiblePc : L1World.getInstance()
                            .getVisiblePlayer(doll)) {
                        // テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
                        visiblePc.removeKnownObject(doll);
                        subjects.add(visiblePc);
                    }
                }
            } else {
                for (L1DollInstance doll : pc.getDollList().values()) {
                    // テレポート先の設定
                    L1Location loc = pc.getLocation().randomLocation(3, false);
                    int nx = loc.getX();
                    int ny = loc.getY();

                    teleport(doll, nx, ny, mapId, head);
                    pc.sendPackets(new S_DollPack(doll, pc));

                    for (L1PcInstance visiblePc : L1World.getInstance()
                            .getVisiblePlayer(doll)) {
                        // テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
                        visiblePc.removeKnownObject(doll);
                        subjects.add(visiblePc);
                    }
                }
            }
        }

        for (L1PcInstance updatePc : subjects) {
            updatePc.updateObject();
        }

        pc.setTeleport(false);

        if (pc.hasSkillEffect(WIND_SHACKLE)) {
            pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc
                    .getSkillEffectTimeSec(WIND_SHACKLE)));
        }
    }

    private Teleportation() {
    }

}
