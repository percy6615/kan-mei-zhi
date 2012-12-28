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
package l1j.jrwz.server.clientpackets;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.codes.ActionCodes;
import l1j.jrwz.server.datatables.HouseTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1DoorInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.templates.L1House;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket, C_Door

/**
 * 處理收到由客戶端傳來開關門的封包
 */
public class C_Door extends ClientBasePacket {

    public class CloseTimer extends TimerTask {

        private final L1DoorInstance _door;

        public CloseTimer(L1DoorInstance door) {
            _door = door;
        }

        public void begin() {
            Timer timer = new Timer();
            timer.schedule(this, 5 * 1000);
        }

        @Override
        public void run() {
            if (_door.getOpenStatus() == ActionCodes.ACTION_Open) {
                _door.close();
            }
        }
    }

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Door.class.getName());

    private static final String C_DOOR = "[C] C_Door";

    public C_Door(byte abyte0[], ClientThread client) throws Exception {
        super(abyte0);
        // int locX = readH();
        // int locY = readH();
        int objectId = readD();

        L1PcInstance pc = client.getActiveChar();
        L1DoorInstance door = (L1DoorInstance) L1World.getInstance()
                .findObject(objectId);
        if (door == null) {
            return;
        }

        if ((door.getDoorId() >= 5001 && door.getDoorId() <= 5010)) { // 水晶洞窟
            return;
        } else if (door.getDoorId() == 6006) { // 冒險洞穴二樓
            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                return;
            }
            if (pc.getInventory().consumeItem(40163, 1)) { // 黃金鑰匙
                door.open();
                CloseTimer closetimer = new CloseTimer(door);
                closetimer.begin();
            }
        } else if (door.getDoorId() == 6007) { // 冒險洞穴二樓
            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                return;
            }
            if (pc.getInventory().consumeItem(40313, 1)) { // 銀鑰匙
                door.open();
                CloseTimer closetimer = new CloseTimer(door);
                closetimer.begin();
            }
        } else if (!isExistKeeper(pc, door.getKeeperId())) {
            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                door.close();
            } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
                door.open();
            }
        }
    }

    @Override
    public String getType() {
        return C_DOOR;
    }

    private boolean isExistKeeper(L1PcInstance pc, int keeperId) {
        if (keeperId == 0) {
            return false;
        }

        L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
        if (clan != null) {
            int houseId = clan.getHouseId();
            if (houseId != 0) {
                L1House house = HouseTable.getInstance().getHouseTable(houseId);
                if (keeperId == house.getKeeperId()) {
                    return false;
                }
            }
        }
        return true;
    }
}
