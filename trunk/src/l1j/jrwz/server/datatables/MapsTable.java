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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.utils.SQLUtil;

public final class MapsTable {
    private class MapData {
        public int startX = 0;
        public int endX = 0;
        public int startY = 0;
        public int endY = 0;
        public double monster_amount = 1;
        public double dropRate = 1;
        public boolean isUnderwater = false;
        public boolean markable = false;
        public boolean teleportable = false;
        public boolean escapable = false;
        public boolean isUseResurrection = false;
        public boolean isUsePainwand = false;
        public boolean isEnabledDeathPenalty = false;
        public boolean isTakePets = false;
        public boolean isRecallPets = false;
        public boolean isUsableItem = false;
        public boolean isUsableSkill = false;
    }

    private static Logger _log = Logger.getLogger(MapsTable.class.getName());

    private static MapsTable _instance;

    /**
     * MapsTableのインスタンスを返す。
     * 
     * @return MapsTableのインスタンス
     */
    public static MapsTable getInstance() {
        if (_instance == null) {
            _instance = new MapsTable();
        }
        return _instance;
    }

    /**
     * KeyにマップID、Valueにテレポート可否フラグが格納されるHashMap
     */
    private final Map<Integer, MapData> _maps = new HashMap<Integer, MapData>();

    /**
     * 新しくMapsTableオブジェクトを生成し、マップのテレポート可否フラグを読み込む。
     */
    private MapsTable() {
        loadMapsFromDatabase();
    }

    /**
     * マップのドロップ倍率を返す
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return ドロップ倍率
     */
    public double getDropRate(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return map.dropRate;
    }

    /**
     * マップがのX終了座標を返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return X終了座標
     */
    public int getEndX(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).endX;
    }

    /**
     * マップがのY終了座標を返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return Y終了座標
     */
    public int getEndY(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).endY;
    }

    /**
     * マップのモンスター量倍率を返す
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return モンスター量の倍率
     */
    public double getMonsterAmount(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return map.monster_amount;
    }

    /**
     * マップがのX開始座標を返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return X開始座標
     */
    public int getStartX(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).startX;
    }

    /**
     * マップがのY開始座標を返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return Y開始座標
     */
    public int getStartY(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).startY;
    }

    /**
     * マップが、デスペナルティがあるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return デスペナルティであればtrue
     */
    public boolean isEnabledDeathPenalty(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isEnabledDeathPenalty;
    }

    /**
     * マップが、MAPを超えたテレポート可能であるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return 可能であればtrue
     */
    public boolean isEscapable(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).escapable;
    }

    /**
     * マップが、ブックマーク可能であるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return ブックマーク可能であればtrue
     */
    public boolean isMarkable(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).markable;
    }

    /**
     * マップが、ペット・サモンを呼び出せるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return ペット・サモンを呼び出せるならばtrue
     */
    public boolean isRecallPets(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isRecallPets;
    }

    /**
     * マップが、ペット・サモンを連れて行けるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return ペット・サモンを連れて行けるならばtrue
     */
    public boolean isTakePets(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isTakePets;
    }

    /**
     * マップが、ランダムテレポート可能であるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * @return 可能であればtrue
     */
    public boolean isTeleportable(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).teleportable;
    }

    /**
     * マップが、水中であるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return 水中であればtrue
     */
    public boolean isUnderwater(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUnderwater;
    }

    /**
     * マップが、アイテムを使用できるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return アイテムを使用できるならばtrue
     */
    public boolean isUsableItem(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUsableItem;
    }

    /**
     * マップが、スキルを使用できるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return スキルを使用できるならばtrue
     */
    public boolean isUsableSkill(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUsableSkill;
    }

    /**
     * マップが、パインワンド使用可能であるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return パインワンド使用可能であればtrue
     */
    public boolean isUsePainwand(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUsePainwand;
    }

    /**
     * マップが、復活可能であるかを返す。
     * 
     * @param mapId
     *            調べるマップのマップID
     * 
     * @return 復活可能であればtrue
     */
    public boolean isUseResurrection(int mapId) {
        MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUseResurrection;
    }

    /**
     * マップのテレポート可否フラグをデータベースから読み込み、HashMap _mapsに格納する。
     */
    private void loadMapsFromDatabase() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM mapids");

            for (rs = pstm.executeQuery(); rs.next();) {
                MapData data = new MapData();
                int mapId = rs.getInt("mapid");
                // rs.getString("locationname");
                data.startX = rs.getInt("startX");
                data.endX = rs.getInt("endX");
                data.startY = rs.getInt("startY");
                data.endY = rs.getInt("endY");
                data.monster_amount = rs.getDouble("monster_amount");
                data.dropRate = rs.getDouble("drop_rate");
                data.isUnderwater = rs.getBoolean("underwater");
                data.markable = rs.getBoolean("markable");
                data.teleportable = rs.getBoolean("teleportable");
                data.escapable = rs.getBoolean("escapable");
                data.isUseResurrection = rs.getBoolean("resurrection");
                data.isUsePainwand = rs.getBoolean("painwand");
                data.isEnabledDeathPenalty = rs.getBoolean("penalty");
                data.isTakePets = rs.getBoolean("take_pets");
                data.isRecallPets = rs.getBoolean("recall_pets");
                data.isUsableItem = rs.getBoolean("usable_item");
                data.isUsableSkill = rs.getBoolean("usable_skill");

                _maps.put(new Integer(mapId), data);
            }

            _log.config("Maps " + _maps.size());
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

}
