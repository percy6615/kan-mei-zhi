package l1j.jrwz.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.templates.L1Item;
import l1j.jrwz.server.utils.SQLUtil;

public class L1DwarfForElfInventory extends L1Inventory {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static Logger _log = Logger.getLogger(L1DwarfForElfInventory.class
            .getName());

    private final L1PcInstance _owner;

    public L1DwarfForElfInventory(L1PcInstance owner) {
        _owner = owner;
    }

    // ＤＢのcharacter_elf_warehouseから削除
    @Override
    public void deleteItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM character_elf_warehouse WHERE id = ?");
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

    // ＤＢのcharacter_elf_warehouseへ登録
    @Override
    public void insertItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO character_elf_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchant_kind = ?, attr_enchant_level = ?");
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
                    .prepareStatement("SELECT * FROM character_elf_warehouse WHERE account_name = ?");
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

    // ＤＢのcharacter_elf_warehouseを更新
    @Override
    public void updateItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("UPDATE character_elf_warehouse SET count = ? WHERE id = ?");
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
