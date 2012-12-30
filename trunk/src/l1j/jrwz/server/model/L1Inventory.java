/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.jrwz.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.IdFactory;
import l1j.jrwz.server.datatables.FurnitureSpawnTable;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.datatables.LetterTable;
import l1j.jrwz.server.datatables.PetTable;
import l1j.jrwz.server.model.Instance.L1FurnitureInstance;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.templates.L1Item;

public class L1Inventory extends L1Object {

    public class DataComparator implements java.util.Comparator<L1ItemInstance> {
        /* 
         * 按强化等级由低到高排序
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(L1ItemInstance item1, L1ItemInstance item2) {
            return item1.getEnchantLevel() - item2.getEnchantLevel();
        }
    }

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(L1Inventory.class.getName());

    protected List<L1ItemInstance> _items = new CopyOnWriteArrayList<L1ItemInstance>();

    public static final int MAX_AMOUNT = 2000000000; // 2G

    public static final int MAX_WEIGHT = 1500;

    // 引数のアイテムを追加しても容量と重量が大丈夫か確認
    public static final int OK = 0;

    public static final int SIZE_OVER = 1;

    public static final int WEIGHT_OVER = 2;

    public static final int AMOUNT_OVER = 3;

    // 引数のアイテムを追加しても倉庫の容量が大丈夫か確認
    public static final int WAREHOUSE_TYPE_PERSONAL = 0;

    public static final int WAREHOUSE_TYPE_CLAN = 1;

    public L1Inventory() {
        //
    }

    public int checkAddItem(L1ItemInstance item, int count) {
        if (item == null) {
            return -1;
        }
        if (item.getCount() <= 0 || count <= 0) {
            return -1;
        }
        if (getSize() > Config.MAX_NPC_ITEM
                || (getSize() == Config.MAX_NPC_ITEM && (!item.isStackable() || !checkItem(item
                        .getItem().getItemId())))) { // 容量確認
            return SIZE_OVER;
        }

        int weight = getWeight() + item.getItem().getWeight() * count / 1000
                + 1;
        if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
            return WEIGHT_OVER;
        }
        if (weight > (MAX_WEIGHT * Config.RATE_WEIGHT_LIMIT_PET)) { // その他の重量確認（主にサモンとペット）
            return WEIGHT_OVER;
        }

        L1ItemInstance itemExist = findItemId(item.getItemId());
        if (itemExist != null && (itemExist.getCount() + count) > MAX_AMOUNT) {
            return AMOUNT_OVER;
        }

        return OK;
    }

    public int checkAddItemToWarehouse(L1ItemInstance item, int count, int type) {
        if (item == null) {
            return -1;
        }
        if (item.getCount() <= 0 || count <= 0) {
            return -1;
        }

        int maxSize = 100;
        if (type == WAREHOUSE_TYPE_PERSONAL) {
            maxSize = Config.MAX_PERSONAL_WAREHOUSE_ITEM;
        } else if (type == WAREHOUSE_TYPE_CLAN) {
            maxSize = Config.MAX_CLAN_WAREHOUSE_ITEM;
        }
        if (getSize() > maxSize
                || (getSize() == maxSize && (!item.isStackable() || !checkItem(item
                        .getItem().getItemId())))) { // 容量確認
            return SIZE_OVER;
        }

        return OK;
    }

    // 強化された特定のアイテムを指定された個数以上所持しているか確認
    // 装備中のアイテムは所持していないと判別する
    public boolean checkEnchantItem(int id, int enchant, int count) {
        int num = 0;
        for (L1ItemInstance item : _items) {
            if (item.isEquipped()) { // 装備しているものは該当しない
                continue;
            }
            if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
                num++;
                if (num == count) {
                    return true;
                }
            }
        }
        return false;
    }

    // 特定のアイテムを指定された個数以上所持しているか確認（矢とか魔石の確認）
    public boolean checkItem(int id) {
        return checkItem(id, 1);
    }

    public boolean checkItem(int id, int count) {
        if (count == 0) {
            return true;
        }
        if (ItemTable.getInstance().getTemplate(id).isStackable()) {
            L1ItemInstance item = findItemId(id);
            if (item != null && item.getCount() >= count) {
                return true;
            }
        } else {
            Object[] itemList = findItemsId(id);
            if (itemList.length >= count) {
                return true;
            }
        }
        return false;
    }

    // 特定のアイテムを全て必要な個数所持しているか確認（イベントとかで複数のアイテムを所持しているか確認するため）
    public boolean checkItem(int[] ids) {
        int len = ids.length;
        int[] counts = new int[len];
        for (int i = 0; i < len; i++) {
            counts[i] = 1;
        }
        return checkItem(ids, counts);
    }

    public boolean checkItem(int[] ids, int[] counts) {
        for (int i = 0; i < ids.length; i++) {
            if (!checkItem(ids[i], counts[i])) {
                return false;
            }
        }
        return true;
    }

    // 特定のアイテムを指定された個数以上所持しているか確認
    // 装備中のアイテムは所持していないと判別する
    public boolean checkItemNotEquipped(int id, int count) {
        if (count == 0) {
            return true;
        }
        return count <= countItems(id);
    }

    // インベントリ内の全てのアイテムを消す（所有者を消すときなど）
    public void clearItems() {
        for (Object itemObject : _items) {
            L1ItemInstance item = (L1ItemInstance) itemObject;
            L1World.getInstance().removeObject(item);
        }
        _items.clear();
    }

    // 強化された特定のアイテムを消費する
    // 装備中のアイテムは所持していないと判別する
    public boolean consumeEnchantItem(int id, int enchant, int count) {
        for (L1ItemInstance item : _items) {
            if (item.isEquipped()) { // 装備しているものは該当しない
                continue;
            }
            if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
                removeItem(item);
                return true;
            }
        }
        return false;
    }

    /**
     * インベントリから指定されたアイテムIDのアイテムを削除する。L1ItemInstanceへの参照
     * がある場合はremoveItemの方を使用するのがよい。 （こちらは矢とか魔石とか特定のアイテムを消費させるときに使う）
     * 
     * @param itemid
     *            -
     *            削除するアイテムのitemid(objidではない)
     * @param count
     *            -
     *            削除する個数
     * @return 実際に削除された場合はtrueを返す。
     */
    public boolean consumeItem(int itemid, int count) {
        if (count <= 0) {
            return false;
        }
        if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
            L1ItemInstance item = findItemId(itemid);
            if (item != null && item.getCount() >= count) {
                removeItem(item, count);
                return true;
            }
        } else {
            L1ItemInstance[] itemList = findItemsId(itemid);
            if (itemList.length == count) {
                for (int i = 0; i < count; i++) {
                    removeItem(itemList[i], 1);
                }
                return true;
            } else if (itemList.length > count) { // 指定個数より多く所持している場合
                DataComparator dc = new DataComparator();
                Arrays.sort(itemList, dc); // エンチャント順にソートし、エンチャント数の少ないものから消費させる
                for (int i = 0; i < count; i++) {
                    removeItem(itemList[i], 1);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * このインベントリ内にある、指定されたIDのアイテムの数を数える。
     * 
     * @return
     */
    public int countItems(int id) {
        if (ItemTable.getInstance().getTemplate(id).isStackable()) {
            L1ItemInstance item = findItemId(id);
            if (item != null) {
                return item.getCount();
            }
        } else {
            Object[] itemList = findItemsIdNotEquipped(id);
            return itemList.length;
        }
        return 0;
    }

    // _itemsから指定オブジェクトを削除(L1PcInstance、L1DwarfInstance、L1GroundInstanceでこの部分をオーバライドする)
    public void deleteItem(L1ItemInstance item) {
        _items.remove(item);
    }

    // アイテムＩＤから検索
    public L1ItemInstance findItemId(int id) {
        for (L1ItemInstance item : _items) {
            if (item.getItem().getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    public L1ItemInstance[] findItemsId(int id) {
        ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
        for (L1ItemInstance item : _items) {
            if (item.getItemId() == id) {
                itemList.add(item);
            }
        }
        return itemList.toArray(new L1ItemInstance[] {});
    }

    public L1ItemInstance[] findItemsIdNotEquipped(int id) {
        ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
        for (L1ItemInstance item : _items) {
            if (item.getItemId() == id) {
                if (!item.isEquipped()) {
                    itemList.add(item);
                }
            }
        }
        return itemList.toArray(new L1ItemInstance[] {});
    }

    // オブジェクトＩＤから検索
    public L1ItemInstance getItem(int objectId) {
        for (Object itemObject : _items) {
            L1ItemInstance item = (L1ItemInstance) itemObject;
            if (item.getId() == objectId) {
                return item;
            }
        }
        return null;
    }

    // インベントリ内の全てのアイテム
    public List<L1ItemInstance> getItems() {
        return _items;
    }

    // インベントリ内のアイテムの総数
    public int getSize() {
        return _items.size();
    }

    // インベントリ内の総重量
    public int getWeight() {
        int weight = 0;

        for (L1ItemInstance item : _items) {
            weight += item.getWeight();
        }

        return weight;
    }

    public void insertItem(L1ItemInstance item) {
    }

    // オーバーライド用
    public void loadItems() {
    }

    /*
     * アイテムを損傷・損耗させる（武器・防具も含む） アイテムの場合、損耗なのでマイナスするが 武器・防具は損傷度を表すのでプラスにする。
     */
    public L1ItemInstance receiveDamage(int objectId) {
        L1ItemInstance item = getItem(objectId);
        return receiveDamage(item);
    }

    public L1ItemInstance receiveDamage(L1ItemInstance item) {
        return receiveDamage(item, 1);
    }

    public L1ItemInstance receiveDamage(L1ItemInstance item, int count) {
        int itemType = item.getItem().getType2();
        int currentDurability = item.get_durability();

        if ((currentDurability == 0 && itemType == 0) || currentDurability < 0) {
            item.set_durability(0);
            return null;
        }

        // 武器・防具のみ損傷度をプラス
        if (itemType == 0) {
            int minDurability = (item.getEnchantLevel() + 5) * -1;
            int durability = currentDurability - count;
            if (durability < minDurability) {
                durability = minDurability;
            }
            if (currentDurability > durability) {
                item.set_durability(durability);
            }
        } else {
            int maxDurability = item.getEnchantLevel() + 5;
            int durability = currentDurability + count;
            if (durability > maxDurability) {
                durability = maxDurability;
            }
            if (currentDurability < durability) {
                item.set_durability(durability);
            }
        }

        updateItem(item, L1PcInventory.COL_DURABILITY);
        return item;
    }

    public L1ItemInstance recoveryDamage(L1ItemInstance item) {
        int itemType = item.getItem().getType2();
        int durability = item.get_durability();

        if ((durability == 0 && itemType != 0) || durability < 0) {
            item.set_durability(0);
            return null;
        }

        if (itemType == 0) {
            // 耐久度をプラスしている。
            item.set_durability(durability + 1);
        } else {
            // 損傷度をマイナスしている。
            item.set_durability(durability - 1);
        }

        updateItem(item, L1PcInventory.COL_DURABILITY);
        return item;
    }

    // 指定したアイテムから指定個数を削除（使ったりゴミ箱に捨てられたとき）戻り値：実際に削除した数
    public int removeItem(int objectId, int count) {
        L1ItemInstance item = getItem(objectId);
        return removeItem(item, count);
    }

    public int removeItem(L1ItemInstance item) {
        return removeItem(item, item.getCount());
    }

    public int removeItem(L1ItemInstance item, int count) {
        if (item == null) {
            return 0;
        }
        if (item.getCount() <= 0 || count <= 0) {
            return 0;
        }
        if (item.getCount() < count) {
            count = item.getCount();
        }
        if (item.getCount() == count) {
            int itemId = item.getItem().getItemId();
            if (itemId == 40314 || itemId == 40316) { // ペットのアミュレット
                PetTable.getInstance().deletePet(item.getId());
            } else if (itemId >= 49016 && itemId <= 49025) { // 便箋
                LetterTable lettertable = new LetterTable();
                lettertable.deleteLetter(item.getId());
            } else if (itemId >= 41383 && itemId <= 41400) { // 家具
                for (L1Object l1object : L1World.getInstance().getObject()) {
                    if (l1object instanceof L1FurnitureInstance) {
                        L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
                        if (furniture.getItemObjId() == item.getId()) { // 既に引き出している家具
                            FurnitureSpawnTable.getInstance().deleteFurniture(
                                    furniture);
                        }
                    }
                }
            }
            deleteItem(item);
            L1World.getInstance().removeObject(item);
        } else {
            item.setCount(item.getCount() - count);
            updateItem(item);
        }
        return count;
    }

    public void shuffle() {
        Collections.shuffle(_items);
    }

    // 新しいアイテムの格納
    public synchronized L1ItemInstance storeItem(int id, int count) {
        if (count <= 0) {
            return null;
        }
        L1Item temp = ItemTable.getInstance().getTemplate(id);
        if (temp == null) {
            return null;
        }

        if (temp.isStackable()) {
            L1ItemInstance item = new L1ItemInstance(temp, count);

            if (findItemId(id) == null) { // 新しく生成する必要がある場合のみIDの発行とL1Worldへの登録を行う
                item.setId(IdFactory.getInstance().nextId());
                L1World.getInstance().storeObject(item);
            }

            return storeItem(item);
        }

        // スタックできないアイテムの場合
        L1ItemInstance result = null;
        for (int i = 0; i < count; i++) {
            L1ItemInstance item = new L1ItemInstance(temp, 1);
            item.setId(IdFactory.getInstance().nextId());
            L1World.getInstance().storeObject(item);
            storeItem(item);
            result = item;
        }
        // 最後に作ったアイテムを返す。配列を戻すようにメソッド定義を変更したほうが良いかもしれない。
        return result;
    }

    // DROP、購入、GMコマンドで入手した新しいアイテムの格納
    public synchronized L1ItemInstance storeItem(L1ItemInstance item) {
        if (item.getCount() <= 0) {
            return null;
        }
        int itemId = item.getItem().getItemId();
        if (item.isStackable()) {
            L1ItemInstance findItem = findItemId(itemId);
            if (findItem != null) {
                findItem.setCount(findItem.getCount() + item.getCount());
                updateItem(findItem);
                return findItem;
            }
        }
        item.setX(getX());
        item.setY(getY());
        item.setMap(getMapId());
        int chargeCount = item.getItem().getMaxChargeCount();
        if (itemId == 40006 || itemId == 40007 || itemId == 40008
                || itemId == 140006 || itemId == 140008 || itemId == 41401) {
            Random random = new Random();
            chargeCount -= random.nextInt(5);
        }
        if (itemId == 20383) {
            chargeCount = 50;
        }
        item.setChargeCount(chargeCount);
        if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light系アイテム
            item.setRemainingTime(item.getItem().getLightFuel());
        } else {
            item.setRemainingTime(item.getItem().getMaxUseTime());
        }
        item.setBless(item.getItem().getBless());
        _items.add(item);
        insertItem(item);
        return item;
    }

    // /trade、倉庫から入手したアイテムの格納
    public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
        if (item.isStackable()) {
            L1ItemInstance findItem = findItemId(item.getItem().getItemId());
            if (findItem != null) {
                findItem.setCount(findItem.getCount() + item.getCount());
                updateItem(findItem);
                return findItem;
            }
        }
        item.setX(getX());
        item.setY(getY());
        item.setMap(getMapId());
        _items.add(item);
        insertItem(item);
        return item;
    }

    // 引数のインベントリにアイテムを移譲
    public synchronized L1ItemInstance tradeItem(int objectId, int count,
            L1Inventory inventory) {
        L1ItemInstance item = getItem(objectId);
        return tradeItem(item, count, inventory);
    }

    public synchronized L1ItemInstance tradeItem(L1ItemInstance item,
            int count, L1Inventory inventory) {
        if (item == null) {
            return null;
        }
        if (item.getCount() <= 0 || count <= 0) {
            return null;
        }
        if (item.isEquipped()) {
            return null;
        }
        if (!checkItem(item.getItem().getItemId(), count)) {
            return null;
        }
        L1ItemInstance carryItem;
        if (item.getCount() <= count) {
            deleteItem(item);
            carryItem = item;
        } else {
            item.setCount(item.getCount() - count);
            updateItem(item);
            carryItem = ItemTable.getInstance().createItem(
                    item.getItem().getItemId());
            carryItem.setCount(count);
            carryItem.setEnchantLevel(item.getEnchantLevel());
            carryItem.setIdentified(item.isIdentified());
            carryItem.set_durability(item.get_durability());
            carryItem.setChargeCount(item.getChargeCount());
            carryItem.setRemainingTime(item.getRemainingTime());
            carryItem.setLastUsed(item.getLastUsed());
            carryItem.setBless(item.getBless());
        }
        return inventory.storeTradeItem(carryItem);
    }

    public void updateItem(L1ItemInstance item) {
    }

    public void updateItem(L1ItemInstance item, int colmn) {
    }

}
