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

package l1j.jrwz.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.IdFactory;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_Bookmarks;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.utils.SQLUtil;

public class L1BookMark {
    private static Logger _log = Logger.getLogger(L1BookMark.class.getName());

    public static void addBookmark(L1PcInstance pc, String s) {
        // クライアント側でチェックされるため不要
        // if (s.length() > 12) {
        // pc.sendPackets(new S_ServerMessage(204));
        // return;
        // }

        if (!pc.getMap().isMarkable()) {
            pc.sendPackets(new S_ServerMessage(214)); // \f1ここを記憶することができません。
            return;
        }

        int size = pc.getBookMarkSize();
        if (size > 49) {
            return;
        }

        if (pc.getBookMark(s) == null) {
            L1BookMark bookmark = new L1BookMark();
            bookmark.setId(IdFactory.getInstance().nextId());
            bookmark.setCharId(pc.getId());
            bookmark.setName(s);
            bookmark.setLocX(pc.getX());
            bookmark.setLocY(pc.getY());
            bookmark.setMapId(pc.getMapId());

            Connection con = null;
            PreparedStatement pstm = null;

            try {
                con = L1DatabaseFactory.getInstance().getConnection();
                pstm = con
                        .prepareStatement("INSERT INTO character_teleport SET id = ?, char_id = ?, name = ?, locx = ?, locy = ?, mapid = ?");
                pstm.setInt(1, bookmark.getId());
                pstm.setInt(2, bookmark.getCharId());
                pstm.setString(3, bookmark.getName());
                pstm.setInt(4, bookmark.getLocX());
                pstm.setInt(5, bookmark.getLocY());
                pstm.setInt(6, bookmark.getMapId());
                pstm.execute();
            } catch (SQLException e) {
                _log.log(Level.SEVERE, "ブックマークの追加でエラーが発生しました。", e);
            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }

            pc.addBookMark(bookmark);
            pc.sendPackets(new S_Bookmarks(s, bookmark.getMapId(), bookmark
                    .getId()));
        } else {
            pc.sendPackets(new S_ServerMessage(327)); // 同じ名前がすでに存在しています。
        }
    }

    public static void deleteBookmark(L1PcInstance player, String s) {
        L1BookMark book = player.getBookMark(s);
        if (book != null) {
            Connection con = null;
            PreparedStatement pstm = null;
            try {

                con = L1DatabaseFactory.getInstance().getConnection();
                pstm = con
                        .prepareStatement("DELETE FROM character_teleport WHERE id=?");
                pstm.setInt(1, book.getId());
                pstm.execute();
                player.removeBookMark(book);
            } catch (SQLException e) {
                _log.log(Level.SEVERE, "ブックマークの削除でエラーが発生しました。", e);
            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    private int _charId;

    private int _id;

    private String _name;

    private int _locX;

    private int _locY;

    private short _mapId;

    public L1BookMark() {
    }

    public int getCharId() {
        return _charId;
    }

    public int getId() {
        return _id;
    }

    public int getLocX() {
        return _locX;
    }

    public int getLocY() {
        return _locY;
    }

    public short getMapId() {
        return _mapId;
    }

    public String getName() {
        return _name;
    }

    public void setCharId(int i) {
        _charId = i;
    }

    public void setId(int i) {
        _id = i;
    }

    public void setLocX(int i) {
        _locX = i;
    }

    public void setLocY(int i) {
        _locY = i;
    }

    public void setMapId(short i) {
        _mapId = i;
    }

    public void setName(String s) {
        _name = s;
    }
}
