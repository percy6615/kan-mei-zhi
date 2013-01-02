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

import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.datatables.ShopTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1Inventory;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1DollInstance;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.model.item.L1ItemId;
import l1j.jrwz.server.model.shop.L1Shop;
import l1j.jrwz.server.model.shop.L1ShopBuyOrderList;
import l1j.jrwz.server.model.shop.L1ShopSellOrderList;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.templates.L1PrivateShopBuyList;
import l1j.jrwz.server.templates.L1PrivateShopSellList;

/**
 * TODO 翻譯，好多
 * 處理收到由客戶端傳來取得結果的封包
 */
public class C_Result extends ClientBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Result.class.getName());
    private static final String C_RESULT = "[C] C_Result";

    public C_Result(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);
        int npcObjectId = readD();
        int resultType = readC();
        int size = readC();
        @SuppressWarnings("unused")
        int unknown = readC();

        L1PcInstance pc = clientthread.getActiveChar();
        int level = pc.getLevel();

        int npcId = 0;
        String npcImpl = "";
        boolean isPrivateShop = false;
        boolean tradable = true;
        L1Object findObject = L1World.getInstance().findObject(npcObjectId);
        if (findObject != null) {
            int diffLocX = Math.abs(pc.getX() - findObject.getX());
            int diffLocY = Math.abs(pc.getY() - findObject.getY());
            // 3格以上的距離視為無效要求
            if (diffLocX > 3 || diffLocY > 3) {
                return;
            }
            if (findObject instanceof L1NpcInstance) {
                L1NpcInstance targetNpc = (L1NpcInstance) findObject;
                npcId = targetNpc.getNpcTemplate().get_npcId();
                npcImpl = targetNpc.getNpcTemplate().getImpl();
            } else if (findObject instanceof L1PcInstance) {
                isPrivateShop = true;
            }
        }

        if (resultType == 0 && size != 0
                && npcImpl.equalsIgnoreCase("L1Merchant")) { // 買道具
            L1Shop shop = ShopTable.getInstance().get(npcId);
            L1ShopBuyOrderList orderList = shop.newBuyOrderList();
            for (int i = 0; i < size; i++) {
                orderList.add(readD(), readD());
            }
            shop.sellItems(pc, orderList);
        } else if (resultType == 1 && size != 0
                && npcImpl.equalsIgnoreCase("L1Merchant")) { // 賣道具
            L1Shop shop = ShopTable.getInstance().get(npcId);
            L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
            for (int i = 0; i < size; i++) {
                orderList.add(readD(), readD());
            }
            shop.buyItems(orderList);
        } else if (resultType == 2 && size != 0
                && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // 自己的倉庫
            int objectId, count;
            for (int i = 0; i < size; i++) {
                tradable = true;
                objectId = readD();
                count = readD();
                L1Object object = pc.getInventory().getItem(objectId);
                L1ItemInstance item = (L1ItemInstance) object;
                if (!item.getItem().isTradable()) {
                    tradable = false;
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem()
                            .getName())); // \f1%0%d 是不可丢弃或转移的
                }
                Object[] petlist = pc.getPetList().values().toArray();
                for (Object petObject : petlist) {
                    if (petObject instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance) petObject;
                        if (item.getId() == pet.getItemObjId()) {
                            tradable = false;
                            // \f1%0%d 是不可丢弃或转移的
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item
                                    .getItem().getName()));
                            break;
                        }
                    }
                }
                Object[] dolllist = pc.getDollList().values().toArray();
                for (Object dollObject : dolllist) {
                    if (dollObject instanceof L1DollInstance) {
                        L1DollInstance doll = (L1DollInstance) dollObject;
                        if (item.getId() == doll.getItemObjId()) {
                            tradable = false;
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1181)); // 这个魔法娃娃目前正在使用中。
                            break;
                        }
                    }
                }
                if (pc.getDwarfInventory().checkAddItemToWarehouse(item, count,
                        L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$75)); // \f1存放物品的空间不足。
                    break;
                }
                if (tradable) {
                    pc.getInventory().tradeItem(objectId, count,
                            pc.getDwarfInventory());
                    pc.turnOnOffLight();
                }
            }
        } else if (resultType == 3 && size != 0
                && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // 從倉庫取出東西
            int objectId, count;
            L1ItemInstance item;
            for (int i = 0; i < size; i++) {
                objectId = readD();
                count = readD();
                item = pc.getDwarfInventory().getItem(objectId);
                if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) // 檢查重量與容量
                {
                    if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
                        pc.getDwarfInventory().tradeItem(item, count,
                                pc.getInventory());
                    } else {
                        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$189)); // \f1金币不足。
                        break;
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$270)); // \f1当你负担过重时不能交易。
                    break;
                }
            }
        } else if (resultType == 4 && size != 0
                && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // 儲存道具到倉庫
            int objectId, count;
            if (pc.getClanid() != 0) { // 有血盟
                for (int i = 0; i < size; i++) {
                    tradable = true;
                    objectId = readD();
                    count = readD();
                    L1Clan clan = L1World.getInstance().getClan(
                            pc.getClanname());
                    L1Object object = pc.getInventory().getItem(objectId);
                    L1ItemInstance item = (L1ItemInstance) object;
                    if (clan != null) {
                        if (!item.getItem().isTradable()) {
                            tradable = false;
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item
                                    .getItem().getName())); // \f1%0%d 是不可丢弃或转移的
                        }
                        if (item.getBless() >= 128) { // 被封印的裝備
                            tradable = false;
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item
                                    .getItem().getName())); // \f1%0%d 是不可丢弃或转移的
                        }
                        Object[] petlist = pc.getPetList().values().toArray();
                        for (Object petObject : petlist) {
                            if (petObject instanceof L1PetInstance) {
                                L1PetInstance pet = (L1PetInstance) petObject;
                                if (item.getId() == pet.getItemObjId()) {
                                    tradable = false;
                                    // \f1%0%d 是不可丢弃或转移的
                                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210,
                                            item.getItem().getName()));
                                    break;
                                }
                            }
                        }
                        Object[] dolllist = pc.getDollList().values().toArray();
                        for (Object dollObject : dolllist) {
                            if (dollObject instanceof L1DollInstance) {
                                L1DollInstance doll = (L1DollInstance) dollObject;
                                if (item.getId() == doll.getItemObjId()) {
                                    tradable = false;
                                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1181)); // 这个魔法娃娃目前正在使用中。
                                    break;
                                }
                            }
                        }
                        if (clan.getDwarfForClanInventory()
                                .checkAddItemToWarehouse(item, count,
                                        L1Inventory.WAREHOUSE_TYPE_CLAN) == L1Inventory.SIZE_OVER) {
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$75)); // \f1存放物品的空间不足。
                            break;
                        }
                        if (tradable) {
                            pc.getInventory().tradeItem(objectId, count,
                                    clan.getDwarfForClanInventory());
                            pc.turnOnOffLight();
                        }
                    }
                }
            } else {
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$208)); // \f1若想使用血盟仓库，必须加入血盟。
            }
        } else if (resultType == 5 && size != 0
                && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) { // 從克萊因倉庫中取出道具
            int objectId, count;
            L1ItemInstance item;

            L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
            if (clan != null) {
                for (int i = 0; i < size; i++) {
                    objectId = readD();
                    count = readD();
                    item = clan.getDwarfForClanInventory().getItem(objectId);
                    if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
                        if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
                            clan.getDwarfForClanInventory().tradeItem(item,
                                    count, pc.getInventory());
                        } else {
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$189)); // \f1金币不足。
                            break;
                        }
                    } else {
                        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$270)); // \f1当你负担过重时不能交易。
                        break;
                    }
                }
                clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
            }
        } else if (resultType == 5 && size == 0
                && npcImpl.equalsIgnoreCase("L1Dwarf")) { // クラン倉庫から取り出し中にCancel、または、ESCキー
            L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
            if (clan != null) {
                clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
            }
        } else if (resultType == 8 && size != 0
                && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5
                && pc.isElf()) { // 自分のエルフ倉庫に格納
            int objectId, count;
            for (int i = 0; i < size; i++) {
                tradable = true;
                objectId = readD();
                count = readD();
                L1Object object = pc.getInventory().getItem(objectId);
                L1ItemInstance item = (L1ItemInstance) object;
                if (!item.getItem().isTradable()) {
                    tradable = false;
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item.getItem()
                            .getName())); // \f1%0%d 是不可丢弃或转移的
                }
                Object[] petlist = pc.getPetList().values().toArray();
                for (Object petObject : petlist) {
                    if (petObject instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance) petObject;
                        if (item.getId() == pet.getItemObjId()) {
                            tradable = false;
                            // \f1%0%d 是不可丢弃或转移的
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$210, item
                                    .getItem().getName()));
                            break;
                        }
                    }
                }
                Object[] dolllist = pc.getDollList().values().toArray();
                for (Object dollObject : dolllist) {
                    if (dollObject instanceof L1DollInstance) {
                        L1DollInstance doll = (L1DollInstance) dollObject;
                        if (item.getId() == doll.getItemObjId()) {
                            tradable = false;
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1181)); // 这个魔法娃娃目前正在使用中。
                            break;
                        }
                    }
                }
                if (pc.getDwarfForElfInventory().checkAddItemToWarehouse(item,
                        count, L1Inventory.WAREHOUSE_TYPE_PERSONAL) == L1Inventory.SIZE_OVER) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$75)); // \f1存放物品的空间不足。
                    break;
                }
                if (tradable) {
                    pc.getInventory().tradeItem(objectId, count,
                            pc.getDwarfForElfInventory());
                    pc.turnOnOffLight();
                }
            }
        } else if (resultType == 9 && size != 0
                && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5
                && pc.isElf()) { // 自分のエルフ倉庫から取り出し
            int objectId, count;
            L1ItemInstance item;
            for (int i = 0; i < size; i++) {
                objectId = readD();
                count = readD();
                item = pc.getDwarfForElfInventory().getItem(objectId);
                if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
                    if (pc.getInventory().consumeItem(40494, 2)) { // ミスリル
                        pc.getDwarfForElfInventory().tradeItem(item, count,
                                pc.getInventory());
                    } else {
                        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$337, "$767")); // \f1缺少%0%s。纯粹的米索莉块
                        break;
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$270)); // \f1当你负担过重时不能交易。
                    break;
                }
            }
        } else if (resultType == 0 && size != 0 && isPrivateShop) { // 個人商店からアイテム購入
            int order;
            int count;
            int price;
            ArrayList<L1PrivateShopSellList> sellList;
            L1PrivateShopSellList pssl;
            int itemObjectId;
            int sellPrice;
            int sellTotalCount;
            int sellCount;
            L1ItemInstance item;
            boolean[] isRemoveFromList = new boolean[8];

            L1PcInstance targetPc = null;
            if (findObject instanceof L1PcInstance) {
                targetPc = (L1PcInstance) findObject;
            }
            if (targetPc.isTradingInPrivateShop()) {
                return;
            }
            sellList = targetPc.getSellList();
            synchronized (sellList) {
                // 売り切れが発生し、閲覧中のアイテム数とリスト数が異なる
                if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
                    return;
                }
                targetPc.setTradingInPrivateShop(true);

                for (int i = 0; i < size; i++) { // 購入予定の商品
                    order = readD();
                    count = readD();
                    pssl = (L1PrivateShopSellList) sellList.get(order);
                    itemObjectId = pssl.getItemObjectId();
                    sellPrice = pssl.getSellPrice();
                    sellTotalCount = pssl.getSellTotalCount(); // 売る予定の個数
                    sellCount = pssl.getSellCount(); // 売った累計
                    item = targetPc.getInventory().getItem(itemObjectId);
                    if (item == null) {
                        continue;
                    }
                    if (count > sellTotalCount - sellCount) {
                        count = sellTotalCount - sellCount;
                    }
                    if (count == 0) {
                        continue;
                    }

                    if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
                        for (int j = 0; j < count; j++) { // オーバーフローをチェック
                            if (sellPrice * j > 2000000000) {
                                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$904, // 总共贩卖价格无法超过 %d金币。
                                        "2000000000"));
                                targetPc.setTradingInPrivateShop(false);
                                return;
                            }
                        }
                        price = count * sellPrice;
                        if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
                            L1ItemInstance adena = pc.getInventory()
                                    .findItemId(L1ItemId.ADENA);
                            if (targetPc != null && adena != null) {
                                if (targetPc.getInventory().tradeItem(item,
                                        count, pc.getInventory()) == null) {
                                    targetPc.setTradingInPrivateShop(false);
                                    return;
                                }
                                pc.getInventory().tradeItem(adena, price,
                                        targetPc.getInventory());
                                String message = item.getItem().getName()
                                        + " (" + String.valueOf(count) + ")";
                                targetPc.sendPackets(new S_ServerMessage(L1SystemMessageId.$877, // 将 %1%o 卖给 %0。
                                        pc.getName(), message));
                                pssl.setSellCount(count + sellCount);
                                sellList.set(order, pssl);
                                if (pssl.getSellCount() == pssl
                                        .getSellTotalCount()) { // 売る予定の個数を売った
                                    isRemoveFromList[order] = true;
                                }
                            }
                        } else {
                            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$189)); // \f1金币不足。
                            break;
                        }
                    } else {
                        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$270)); // \f1当你负担过重时不能交易。
                        break;
                    }
                }
                // 売り切れたアイテムをリストの末尾から削除
                for (int i = 7; i >= 0; i--) {
                    if (isRemoveFromList[i]) {
                        sellList.remove(i);
                    }
                }
                targetPc.setTradingInPrivateShop(false);
            }
        } else if (resultType == 1 && size != 0 && isPrivateShop) { // 個人商店にアイテム売却
            int count;
            int order;
            ArrayList<L1PrivateShopBuyList> buyList;
            L1PrivateShopBuyList psbl;
            int itemObjectId;
            L1ItemInstance item;
            int buyPrice;
            int buyTotalCount;
            int buyCount;
            // L1ItemInstance targetItem;
            boolean[] isRemoveFromList = new boolean[8];

            L1PcInstance targetPc = null;
            if (findObject instanceof L1PcInstance) {
                targetPc = (L1PcInstance) findObject;
            }
            if (targetPc.isTradingInPrivateShop()) {
                return;
            }
            targetPc.setTradingInPrivateShop(true);
            buyList = targetPc.getBuyList();

            for (int i = 0; i < size; i++) {
                itemObjectId = readD();
                count = readCH();
                order = readC();
                item = pc.getInventory().getItem(itemObjectId);
                if (item == null) {
                    continue;
                }
                psbl = (L1PrivateShopBuyList) buyList.get(order);
                buyPrice = psbl.getBuyPrice();
                buyTotalCount = psbl.getBuyTotalCount(); // 買う予定の個数
                buyCount = psbl.getBuyCount(); // 買った累計
                if (count > buyTotalCount - buyCount) {
                    count = buyTotalCount - buyCount;
                }
                if (item.isEquipped()) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$905)); // 无法贩卖装备中的道具。
                    continue;
                }

                if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 容量重量確認及びメッセージ送信
                    for (int j = 0; j < count; j++) { // オーバーフローをチェック
                        if (buyPrice * j > 2000000000) {
                            targetPc.sendPackets(new S_ServerMessage(L1SystemMessageId.$904, // 总共贩卖价格无法超过 %d金币。
                                    "2000000000"));
                            return;
                        }
                    }
                    if (targetPc.getInventory().checkItem(L1ItemId.ADENA,
                            count * buyPrice)) {
                        L1ItemInstance adena = targetPc.getInventory()
                                .findItemId(L1ItemId.ADENA);
                        if (adena != null) {
                            targetPc.getInventory().tradeItem(adena,
                                    count * buyPrice, pc.getInventory());
                            pc.getInventory().tradeItem(item, count,
                                    targetPc.getInventory());
                            psbl.setBuyCount(count + buyCount);
                            buyList.set(order, psbl);
                            if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // 買う予定の個数を買った
                                isRemoveFromList[order] = true;
                            }
                        }
                    } else {
                        targetPc.sendPackets(new S_ServerMessage(L1SystemMessageId.$189)); // \f1金币不足。
                        break;
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$271)); // \f1对方携带的物品过重，无法交易。
                    break;
                }
            }
            // 買い切ったアイテムをリストの末尾から削除
            for (int i = 7; i >= 0; i--) {
                if (isRemoveFromList[i]) {
                    buyList.remove(i);
                }
            }
            targetPc.setTradingInPrivateShop(false);
        }
    }

    @Override
    public String getType() {
        return C_RESULT;
    }

}
