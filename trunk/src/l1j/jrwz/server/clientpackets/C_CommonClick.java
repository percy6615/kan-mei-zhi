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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.Config;
import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.datatables.CharacterTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.serverpackets.S_CharAmount;
import l1j.jrwz.server.serverpackets.S_CharPacks;
import l1j.jrwz.server.utils.SQLUtil;

/**
 * 處理收到由客戶端傳來的角色選擇畫面封包
 */
public class C_CommonClick {
    private static final String C_COMMON_CLICK = "[C] C_CommonClick";

    private static Logger _log = Logger
            .getLogger(C_CommonClick.class.getName());

    public C_CommonClick(ClientThread client) {
        deleteCharacter(client); // 到達刪除期限，刪除角色
        int amountOfChars = client.getAccount().countCharacters();
        client.sendPacket(new S_CharAmount(amountOfChars, client));
        if (amountOfChars > 0) {
            sendCharPacks(client);
        }
    }

    private void deleteCharacter(ClientThread client) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {

            conn = L1DatabaseFactory.getInstance().getConnection();
            pstm = conn
                    .prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
            pstm.setString(1, client.getAccountName());
            rs = pstm.executeQuery();

            while (rs.next()) {
                String name = rs.getString("char_name");
                String clanname = rs.getString("Clanname");

                Timestamp deleteTime = rs.getTimestamp("DeleteTime");
                if (deleteTime != null) {
                    Calendar cal = Calendar.getInstance();
                    long checkDeleteTime = ((cal.getTimeInMillis() - deleteTime
                            .getTime()) / 1000) / 3600;
                    if (checkDeleteTime >= 0) {
                        L1Clan clan = L1World.getInstance().getClan(clanname);
                        if (clan != null) {
                            clan.delMemberName(name);
                        }
                        CharacterTable.getInstance().deleteCharacter(
                                client.getAccountName(), name);
                    }
                }
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
    }

    public String getType() {
        return C_COMMON_CLICK;
    }

    private void sendCharPacks(ClientThread client) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {

            conn = L1DatabaseFactory.getInstance().getConnection();
            pstm = conn
                    .prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
            pstm.setString(1, client.getAccountName());
            rs = pstm.executeQuery();

            while (rs.next()) {
                String name = rs.getString("char_name");
                String clanname = rs.getString("Clanname");
                int type = rs.getInt("Type");
                byte sex = rs.getByte("Sex");
                int lawful = rs.getInt("Lawful");

                int currenthp = rs.getInt("CurHp");
                if (currenthp < 1) {
                    currenthp = 1;
                } else if (currenthp > 32767) {
                    currenthp = 32767;
                }

                int currentmp = rs.getInt("CurMp");
                if (currentmp < 1) {
                    currentmp = 1;
                } else if (currentmp > 32767) {
                    currentmp = 32767;
                }

                int lvl;
                if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
                    lvl = rs.getInt("level");
                    if (lvl < 1) {
                        lvl = 1;
                    } else if (lvl > 127) {
                        lvl = 127;
                    }
                } else {
                    lvl = 1;
                }

                int ac = rs.getByte("Ac");
                int str = rs.getByte("Str");
                int dex = rs.getByte("Dex");
                int con = rs.getByte("Con");
                int wis = rs.getByte("Wis");
                int cha = rs.getByte("Cha");
                int intel = rs.getByte("Intel");
                int accessLevel = rs.getShort("AccessLevel");

                S_CharPacks cpk = new S_CharPacks(name, clanname, type, sex,
                        lawful, currenthp, currentmp, ac, lvl, str, dex, con,
                        wis, cha, intel, accessLevel);

                client.sendPacket(cpk);
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
    }
}
