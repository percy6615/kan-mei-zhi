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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.utils.SQLUtil;

public class IdFactory {
    private static Logger _log = Logger.getLogger(IdFactory.class.getName());

    private int _curId;

    private final Object _monitor = new Object();

    private static final int FIRST_ID = 0x10000000;

    private static IdFactory _instance = new IdFactory();

    public static IdFactory getInstance() {
        return _instance;
    }

    private IdFactory() {
        loadState();
    }

    private void loadState() {
        // 取得資料庫中最大的ID+1
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("select max(id)+1 as nextid from (select id from character_items union all select id from character_teleport union all select id from character_warehouse union all select id from character_elf_warehouse union all select objid as id from characters union all select clan_id as id from clan_data union all select id from clan_warehouse union all select objid as id from pets) t");
            rs = pstm.executeQuery();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt("nextid");
            }
            if (id < FIRST_ID) {
                id = FIRST_ID;
            }
            _curId = id;
            _log.info("目前的物件ID: " + _curId);
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public int nextId() {
        synchronized (_monitor) {
            return _curId++;
        }
    }
}
