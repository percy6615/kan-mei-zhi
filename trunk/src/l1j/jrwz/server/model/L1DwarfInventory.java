package l1j.jrwz.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.templates.L1Item;
import l1j.jrwz.server.utils.SQLUtil;

public class L1DwarfInventory extends L1Inventory {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static Logger _log = Logger.getLogger(L1DwarfInventory.class
            .getName());

    private static void present(ArrayList<String> accountList, int itemid,
            int enchant, int count) throws Exception {

        L1Item temp = ItemTable.getInstance().getTemplate(itemid);
        if (temp == null) {
            throw new Exception("不存在的道具ID");
        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            con.setAutoCommit(false);

            for (String account : accountList) {

                if (temp.isStackable()) {
                    L1ItemInstance item = ItemTable.getInstance().createItem(
                            itemid);
                    item.setEnchantLevel(enchant);
                    item.setCount(count);

                    pstm = con
                            .prepareStatement("INSERT INTO character_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?");
                    pstm.setInt(1, item.getId());
                    pstm.setString(2, account);
                    pstm.setInt(3, item.getItemId());
                    pstm.setString(4, item.getName());
                    pstm.setInt(5, item.getCount());
                    pstm.setInt(6, item.getEnchantLevel());
                    pstm.setInt(7, item.isIdentified() ? 1 : 0);
                    pstm.setInt(8, item.get_durability());
                    pstm.setInt(9, item.getChargeCount());
                    pstm.setInt(10, item.getRemainingTime());
                    pstm.execute();
                } else {
                    L1ItemInstance item = null;
                    int createCount;
                    for (createCount = 0; createCount < count; createCount++) {
                        item = ItemTable.getInstance().createItem(itemid);
                        item.setEnchantLevel(enchant);

                        pstm = con
                                .prepareStatement("INSERT INTO character_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?");
                        pstm.setInt(1, item.getId());
                        pstm.setString(2, account);
                        pstm.setInt(3, item.getItemId());
                        pstm.setString(4, item.getName());
                        pstm.setInt(5, item.getCount());
                        pstm.setInt(6, item.getEnchantLevel());
                        pstm.setInt(7, item.isIdentified() ? 1 : 0);
                        pstm.setInt(8, item.get_durability());
                        pstm.setInt(9, item.getChargeCount());
                        pstm.setInt(10, item.getRemainingTime());
                        pstm.execute();
                    }
                }
            }

            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ignore) {
                // ignore
            }
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw new Exception(".present处理时发送错误。");
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static void present(int minlvl, int maxlvl, int itemid, int enchant,
            int count) throws Exception {

        L1Item temp = ItemTable.getInstance().getTemplate(itemid);
        if (temp == null) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();

            pstm = con
                    .prepareStatement("SELECT distinct(account_name) as account_name FROM characters WHERE level between ? and ?");
            pstm.setInt(1, minlvl);
            pstm.setInt(2, maxlvl);
            rs = pstm.executeQuery();

            ArrayList<String> accountList = new ArrayList<String>();
            while (rs.next()) {
                accountList.add(rs.getString("account_name"));
            }

            present(accountList, itemid, enchant, count);

        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw e;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

    }

    public static void present(String account, int itemid, int enchant,
            int count) throws Exception {

        L1Item temp = ItemTable.getInstance().getTemplate(itemid);
        if (temp == null) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();

            if (account.compareToIgnoreCase("*") == 0) {
                pstm = con.prepareStatement("SELECT * FROM accounts");
            } else {
                pstm = con
                        .prepareStatement("SELECT * FROM accounts WHERE login=?");
                pstm.setString(1, account);
            }
            rs = pstm.executeQuery();

            ArrayList<String> accountList = new ArrayList<String>();
            while (rs.next()) {
                accountList.add(rs.getString("login"));
            }

            present(accountList, itemid, enchant, count);

        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw e;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

    }

    private final L1PcInstance _owner;

    public L1DwarfInventory(L1PcInstance owner) {
        _owner = owner;
    }

    // ＤＢのcharacter_warehouseから削除
    @Override
    public void deleteItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM character_warehouse WHERE id = ?");
            pstm.setInt(1, item.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        _items.remove(_items.indexOf(item));
    }

    // ＤＢのcharacter_warehouseへ登録
    @Override
    public void insertItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO character_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchant_kind = ?, attr_enchant_level = ?");
            pstm.setInt(1, item.getId());
            pstm.setString(2, _owner.getAccountName());
            pstm.setInt(3, item.getItemId());
            pstm.setString(4, item.getName());
            pstm.setInt(5, item.getCount());
            pstm.setInt(6, item.getEnchantLevel());
            pstm.setInt(7, item.isIdentified() ? 1 : 0);
            pstm.setInt(8, item.get_durability());
            pstm.setInt(9, item.getChargeCount());
            pstm.setInt(10, item.getRemainingTime());
            pstm.setTimestamp(11, item.getLastUsed());
            pstm.setInt(12, item.getBless());
            pstm.setInt(13, item.getAttrEnchantKind());
            pstm.setInt(14, item.getAttrEnchantLevel());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

    }

    // ＤＢのcharacter_itemsの読込
    @Override
    public void loadItems() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("SELECT * FROM character_warehouse WHERE account_name = ?");
            pstm.setString(1, _owner.getAccountName());

            rs = pstm.executeQuery();

            while (rs.next()) {
                L1ItemInstance item = new L1ItemInstance();
                int objectId = rs.getInt("id");
                item.setId(objectId);
                L1Item itemTemplate = ItemTable.getInstance().getTemplate(
                        rs.getInt("item_id"));
                item.setItem(itemTemplate);
                item.setCount(rs.getInt("count"));
                item.setEquipped(false);
                item.setEnchantLevel(rs.getInt("enchantlvl"));
                item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
                item.set_durability(rs.getInt("durability"));
                item.setChargeCount(rs.getInt("charge_count"));
                item.setRemainingTime(rs.getInt("remaining_time"));
                item.setLastUsed(rs.getTimestamp("last_used"));
                item.setBless(rs.getInt("bless"));
                item.setAttrEnchantKind(rs.getInt("attr_enchant_kind"));
                item.setAttrEnchantLevel(rs.getInt("attr_enchant_level"));

                _items.add(item);
                L1World.getInstance().storeObject(item);
            }

        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    // ＤＢのcharacter_warehouseを更新
    @Override
    public void updateItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("UPDATE character_warehouse SET count = ? WHERE id = ?");
            pstm.setInt(1, item.getCount());
            pstm.setInt(2, item.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
