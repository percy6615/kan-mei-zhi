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
package l1j.jrwz.server.datatables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.model.L1UltimateBattle;
import l1j.jrwz.server.utils.SQLUtil;

public class UBTable {
    private static Logger _log = Logger.getLogger(UBTable.class.getName());

    private static UBTable _instance = new UBTable();

    public static UBTable getInstance() {
        return _instance;
    }

    private final HashMap<Integer, L1UltimateBattle> _ub = new HashMap<Integer, L1UltimateBattle>();

    private UBTable() {
        loadTable();
    }

    public Collection<L1UltimateBattle> getAllUb() {
        return Collections.unmodifiableCollection(_ub.values());
    }

    /**
     * 指定されたUBIDに対するパターンの最大数を返す。
     * 
     * @param ubId
     *            調べるUBID。
     * @return パターンの最大数。
     */
    public int getMaxPattern(int ubId) {
        int n = 0;
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("SELECT MAX(pattern) FROM spawnlist_ub WHERE ub_id=?");
            pstm.setInt(1, ubId);
            rs = pstm.executeQuery();
            if (rs.next()) {
                n = rs.getInt(1);
            }
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return n;
    }

    public L1UltimateBattle getUb(int ubId) {
        return _ub.get(ubId);
    }

    public L1UltimateBattle getUbForNpcId(int npcId) {
        for (L1UltimateBattle ub : _ub.values()) {
            if (ub.containsManager(npcId)) {
                return ub;
            }
        }
        return null;
    }

    private void loadTable() {

        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {

            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM ub_settings");
            rs = pstm.executeQuery();

            while (rs.next()) {

                L1UltimateBattle ub = new L1UltimateBattle();
                ub.setUbId(rs.getInt("ub_id"));
                ub.setMapId(rs.getShort("ub_mapid"));
                ub.setLocX1(rs.getInt("ub_area_x1"));
                ub.setLocY1(rs.getInt("ub_area_y1"));
                ub.setLocX2(rs.getInt("ub_area_x2"));
                ub.setLocY2(rs.getInt("ub_area_y2"));
                ub.setMinLevel(rs.getInt("min_lvl"));
                ub.setMaxLevel(rs.getInt("max_lvl"));
                ub.setMaxPlayer(rs.getInt("max_player"));
                ub.setEnterRoyal(rs.getBoolean("enter_royal"));
                ub.setEnterKnight(rs.getBoolean("enter_knight"));
                ub.setEnterMage(rs.getBoolean("enter_mage"));
                ub.setEnterElf(rs.getBoolean("enter_elf"));
                ub.setEnterDarkelf(rs.getBoolean("enter_darkelf"));
                ub.setEnterDragonKnight(rs.getBoolean("enter_dragonknight"));
                ub.setEnterIllusionist(rs.getBoolean("enter_illusionist"));
                ub.setEnterMale(rs.getBoolean("enter_male"));
                ub.setEnterFemale(rs.getBoolean("enter_female"));
                ub.setUsePot(rs.getBoolean("use_pot"));
                ub.setHpr(rs.getInt("hpr_bonus"));
                ub.setMpr(rs.getInt("mpr_bonus"));
                ub.resetLoc();

                _ub.put(ub.getUbId(), ub);
            }
        } catch (SQLException e) {
            _log.warning("ubsettings couldnt be initialized:" + e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
        }

        // ub_managers load
        try {
            pstm = con.prepareStatement("SELECT * FROM ub_managers");
            rs = pstm.executeQuery();

            while (rs.next()) {
                L1UltimateBattle ub = getUb(rs.getInt("ub_id"));
                if (ub != null) {
                    ub.addManager(rs.getInt("ub_manager_npc_id"));
                }
            }
        } catch (SQLException e) {
            _log.warning("ub_managers couldnt be initialized:" + e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
        }

        // ub_times load
        try {
            pstm = con.prepareStatement("SELECT * FROM ub_times");
            rs = pstm.executeQuery();

            while (rs.next()) {
                L1UltimateBattle ub = getUb(rs.getInt("ub_id"));
                if (ub != null) {
                    ub.addUbTime(rs.getInt("ub_time"));
                }
            }
        } catch (SQLException e) {
            _log.warning("ub_times couldnt be initialized:" + e);
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        _log.config("UB列表 " + _ub.size() + "件");
    }

}
