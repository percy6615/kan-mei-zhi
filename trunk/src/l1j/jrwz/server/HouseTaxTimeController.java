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

import l1j.jrwz.Config;
import l1j.jrwz.server.datatables.AuctionBoardTable;
import l1j.jrwz.server.datatables.ClanTable;
import l1j.jrwz.server.datatables.HouseTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.templates.L1AuctionBoard;
import l1j.jrwz.server.templates.L1House;

public class HouseTaxTimeController implements Runnable {
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(HouseTaxTimeController.class
            .getName());

    private static HouseTaxTimeController _instance;

    public static HouseTaxTimeController getInstance() {
        if (_instance == null) {
            _instance = new HouseTaxTimeController();
        }
        return _instance;
    }

    private void checkTaxDeadline() {
        for (L1House house : HouseTable.getInstance().getHouseTableList()) {
            if (!house.isOnSale()) { // 不檢查再拍賣的血盟小屋
                if (house.getTaxDeadline().before(getRealTime())) {
                    sellHouse(house);
                }
            }
        }
    }

    public Calendar getRealTime() {
        TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        Calendar cal = Calendar.getInstance(tz);
        return cal;
    }

    @Override
    public void run() {
        try {
            while (true) {
                checkTaxDeadline();
                Thread.sleep(600000);
            }
        } catch (Exception e1) {
        }
    }

    private void sellHouse(L1House house) {
        AuctionBoardTable boardTable = new AuctionBoardTable();
        L1AuctionBoard board = new L1AuctionBoard();
        if (board != null) {
            // 在拍賣板張貼新公告
            int houseId = house.getHouseId();
            board.setHouseId(houseId);
            board.setHouseName(house.getHouseName());
            board.setHouseArea(house.getHouseArea());
            TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
            Calendar cal = Calendar.getInstance(tz);
            cal.add(Calendar.DATE, 5); // 5天以後
            cal.set(Calendar.MINUTE, 0); //
            cal.set(Calendar.SECOND, 0);
            board.setDeadline(cal);
            board.setPrice(100000);
            board.setLocation(house.getLocation());
            board.setOldOwner("");
            board.setOldOwnerId(0);
            board.setBidder("");
            board.setBidderId(0);
            boardTable.insertAuctionBoard(board);
            house.setOnSale(true); // 設定為拍賣中
            house.setPurchaseBasement(true); // TODO: 翻譯 地下アジト未購入に設定
            cal.add(Calendar.DATE, Config.HOUSE_TAX_INTERVAL);
            house.setTaxDeadline(cal);
            HouseTable.getInstance().updateHouse(house); // 儲存到資料庫中
            // 取消之前的擁有者
            for (L1Clan clan : L1World.getInstance().getAllClans()) {
                if (clan.getHouseId() == houseId) {
                    clan.setHouseId(0);
                    ClanTable.getInstance().updateClan(clan);
                }
            }
        }
    }

}
