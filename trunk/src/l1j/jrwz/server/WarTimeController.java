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
package l1j.jrwz.server;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.datatables.CastleTable;
import l1j.jrwz.server.datatables.DoorSpawnTable;
import l1j.jrwz.server.model.L1CastleLocation;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1Teleport;
import l1j.jrwz.server.model.L1WarSpawn;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1CrownInstance;
import l1j.jrwz.server.model.Instance.L1DoorInstance;
import l1j.jrwz.server.model.Instance.L1FieldObjectInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1TowerInstance;
import l1j.jrwz.server.serverpackets.S_PacketBox;
import l1j.jrwz.server.templates.L1Castle;

/**
 * 战争时间控制器.
 */
public class WarTimeController implements Runnable {
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(WarTimeController.class
            .getName());

    private static WarTimeController _instance;

    public static WarTimeController getInstance() {
        if (_instance == null) {
            _instance = new WarTimeController();
        }
        return _instance;
    }

    private final L1Castle[] _l1castle = new L1Castle[8];
    private final Calendar[] _war_start_time = new Calendar[8];
    private final Calendar[] _war_end_time = new Calendar[8];

    private final boolean[] _is_now_war = new boolean[8];

    private WarTimeController() {
        for (int i = 0; i < _l1castle.length; i++) {
            _l1castle[i] = CastleTable.getInstance().getCastleTable(i + 1);
            _war_start_time[i] = _l1castle[i].getWarTime();
            _war_end_time[i] = (Calendar) _l1castle[i].getWarTime().clone();
            _war_end_time[i].add(Config.ALT_WAR_TIME_UNIT, Config.ALT_WAR_TIME);
        }
    }

    public void checkCastleWar(L1PcInstance player) {
        for (int i = 0; i < 8; i++) {
            if (_is_now_war[i]) {
                player.sendPackets(new S_PacketBox(S_PacketBox.MSG_WAR_GOING,
                        i + 1)); // %s的攻城战正在进行中。
            }
        }
    }

    private void checkWarTime() {
        for (int i = 0; i < 8; i++) {
            if (_war_start_time[i].before(getRealTime()) // 攻城開始
                    && _war_end_time[i].after(getRealTime())) {
                if (_is_now_war[i] == false) {
                    _is_now_war[i] = true;
                    // 招出攻城的旗子
                    L1WarSpawn warspawn = new L1WarSpawn();
                    warspawn.SpawnFlag(i + 1);
                    // 修理城門並設定為關閉
                    for (L1DoorInstance door : DoorSpawnTable.getInstance()
                            .getDoorList()) {
                        if (L1CastleLocation.checkInWarArea(i + 1, door)) {
                            door.repairGate();
                        }
                    }

                    L1World.getInstance().broadcastPacketToAll(
                            new S_PacketBox(S_PacketBox.MSG_WAR_BEGIN, i + 1)); // %s的攻城战开始。
                    int[] loc = new int[3];
                    for (L1PcInstance pc : L1World.getInstance()
                            .getAllPlayers()) {
                        int castleId = i + 1;
                        if (L1CastleLocation.checkInWarArea(castleId, pc)
                                && !pc.isGm()) { // 剛好在攻城範圍內
                            L1Clan clan = L1World.getInstance().getClan(
                                    pc.getClanname());
                            if (clan != null) {
                                if (clan.getCastleId() == castleId) { // 如果是城血盟
                                    continue;
                                }
                            }
                            loc = L1CastleLocation.getGetBackLoc(castleId);
                            L1Teleport.teleport(pc, loc[0], loc[1],
                                    (short) loc[2], 5, true);
                        }
                    }
                }
            } else if (_war_end_time[i].before(getRealTime())) { // 攻城結束
                if (_is_now_war[i] == true) {
                    _is_now_war[i] = false;
                    L1World.getInstance().broadcastPacketToAll(
                            new S_PacketBox(S_PacketBox.MSG_WAR_END, i + 1)); // %s的攻城战结束。
                    _war_start_time[i].add(Config.ALT_WAR_INTERVAL_UNIT,
                            Config.ALT_WAR_INTERVAL);
                    _war_end_time[i].add(Config.ALT_WAR_INTERVAL_UNIT,
                            Config.ALT_WAR_INTERVAL);
                    _l1castle[i].setTaxRate(10); // 稅率10%
                    _l1castle[i].setPublicMoney(0); // 清除 public money
                    CastleTable.getInstance().updateCastle(_l1castle[i]);

                    int castle_id = i + 1;
                    for (L1Object l1object : L1World.getInstance().getObject()) {
                        // 取消攻城的旗子
                        if (l1object instanceof L1FieldObjectInstance) {
                            L1FieldObjectInstance flag = (L1FieldObjectInstance) l1object;
                            if (L1CastleLocation
                                    .checkInWarArea(castle_id, flag)) {
                                flag.deleteMe();
                            }
                        }
                        // 移除皇冠
                        if (l1object instanceof L1CrownInstance) {
                            L1CrownInstance crown = (L1CrownInstance) l1object;
                            if (L1CastleLocation.checkInWarArea(castle_id,
                                    crown)) {
                                crown.deleteMe();
                            }
                        }
                        // 移除守護塔
                        if (l1object instanceof L1TowerInstance) {
                            L1TowerInstance tower = (L1TowerInstance) l1object;
                            if (L1CastleLocation.checkInWarArea(castle_id,
                                    tower)) {
                                tower.deleteMe();
                            }
                        }
                    }
                    // 塔重新出現
                    L1WarSpawn warspawn = new L1WarSpawn();
                    warspawn.SpawnTower(castle_id);

                    // 移除城門
                    for (L1DoorInstance door : DoorSpawnTable.getInstance()
                            .getDoorList()) {
                        if (L1CastleLocation.checkInWarArea(castle_id, door)) {
                            door.repairGate();
                        }
                    }
                }
            }

        }
    }

    public Calendar getRealTime() {
        TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        Calendar cal = Calendar.getInstance(_tz);
        return cal;
    }

    public boolean isNowWar(int castle_id) {
        return _is_now_war[castle_id - 1];
    }

    @Override
    public void run() {
        try {
            while (true) {
                checkWarTime(); // 检查攻城时间
                Thread.sleep(1000);
            }
        } catch (Exception e1) {
        }
    }
}
