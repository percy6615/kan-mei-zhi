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

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.datatables.AuctionBoardTable;
import l1j.jrwz.server.datatables.HouseTable;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.datatables.NpcActionTable;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.model.item.L1ItemId;
import l1j.jrwz.server.model.npc.L1NpcHtml;
import l1j.jrwz.server.model.npc.action.L1NpcAction;
import l1j.jrwz.server.serverpackets.S_NPCTalkReturn;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.storage.CharactersItemStorage;
import l1j.jrwz.server.templates.L1AuctionBoard;
import l1j.jrwz.server.templates.L1House;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket, C_Amount

/**
 * 處理客戶端傳來拍賣的封包
 */
public class C_Amount extends ClientBasePacket {

    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(C_Amount.class.getName());
    private static final String C_AMOUNT = "[C] C_Amount";

    public C_Amount(byte[] decrypt, ClientThread client) throws Exception {
        super(decrypt);
        int objectId = readD();
        int amount = readD();
        @SuppressWarnings("unused")
        int c = readC();
        String s = readS();

        L1PcInstance pc = client.getActiveChar();
        L1Object npc = L1World.getInstance().findObject(objectId);
        if (npc == null) {
            return;
        }

        String s1 = "";
        String s2 = "";
        try {
            StringTokenizer stringtokenizer = new StringTokenizer(s);
            s1 = stringtokenizer.nextToken();
            s2 = stringtokenizer.nextToken();
        } catch (NoSuchElementException e) {
            s1 = "";
            s2 = "";
        }
        if (s1.equalsIgnoreCase("agapply")) { // 如果你在拍賣競標
            String pcName = pc.getName();
            AuctionBoardTable boardTable = new AuctionBoardTable();
            for (L1AuctionBoard board : boardTable.getAuctionBoardTableList()) {
                if (pcName.equalsIgnoreCase(board.getBidder())) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$523)); // 已经参与其他血盟小屋拍卖。
                    return;
                }
            }
            int houseId = Integer.valueOf(s2);
            L1AuctionBoard board = boardTable.getAuctionBoardTable(houseId);
            if (board != null) {
                int nowPrice = board.getPrice();
                int nowBidderId = board.getBidderId();
                if (pc.getInventory().consumeItem(L1ItemId.ADENA, amount)) {
                    // 更新拍賣公告
                    board.setPrice(amount);
                    board.setBidder(pcName);
                    board.setBidderId(pc.getId());
                    boardTable.updateAuctionBoard(board);
                    if (nowBidderId != 0) {
                        // 將金幣退還給投標者
                        L1PcInstance bidPc = (L1PcInstance) L1World.getInstance().findObject(nowBidderId);
                        if (bidPc != null) { // 玩家在線上
                            bidPc.getInventory().storeItem(L1ItemId.ADENA, nowPrice);
                            // 有人提出比您高的金额，因此无法给你购买权。%n因为您参与拍卖没有得标，所以还给你
                            // %0金币。%n谢谢。%n%n
                            bidPc.sendPackets(new S_ServerMessage(L1SystemMessageId.$525, String.valueOf(nowPrice)));
                        } else { // 玩家離線中
                            L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
                            item.setCount(nowPrice);
                            CharactersItemStorage storage = CharactersItemStorage.create();
                            storage.storeItem(nowBidderId, item);
                        }
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$189)); // \f1金币不足。
                }
            }
        } else if (s1.equalsIgnoreCase("agsell")) { // 出售盟屋
            int houseId = Integer.valueOf(s2);
            AuctionBoardTable boardTable = new AuctionBoardTable();
            L1AuctionBoard board = new L1AuctionBoard();
            if (board != null) {
                // 新增拍賣公告到拍賣板
                board.setHouseId(houseId);
                L1House house = HouseTable.getInstance().getHouseTable(houseId);
                board.setHouseName(house.getHouseName());
                board.setHouseArea(house.getHouseArea());
                TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
                Calendar cal = Calendar.getInstance(tz);
                cal.add(Calendar.DATE, 5); // 5天後
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                board.setDeadline(cal);
                board.setPrice(amount);
                board.setLocation(house.getLocation());
                board.setOldOwner(pc.getName());
                board.setOldOwnerId(pc.getId());
                board.setBidder("");
                board.setBidderId(0);
                boardTable.insertAuctionBoard(board);

                house.setOnSale(true); // 設定盟屋為拍賣中
                house.setPurchaseBasement(true); // 地下アジト未購入に設定
                HouseTable.getInstance().updateHouse(house); // 更新到資料庫中
            }
        } else {
            L1NpcAction action = NpcActionTable.getInstance().get(s, pc, npc);
            if (action != null) {
                L1NpcHtml result = action.executeWithAmount(s, pc, npc, amount);
                if (result != null) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), result));
                }
                return;
            }
        }
    }

    @Override
    public String getType() {
        return C_AMOUNT;
    }
}
