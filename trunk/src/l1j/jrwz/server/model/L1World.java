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
package l1j.jrwz.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.Instance.L1SummonInstance;
import l1j.jrwz.server.model.map.L1Map;
import l1j.jrwz.server.serverpackets.S_SystemMessage;
import l1j.jrwz.server.serverpackets.ServerBasePacket;
import l1j.jrwz.server.types.Point;

/**
 * 世界.
 */
public class L1World {
    private static Logger _log = Logger.getLogger(L1World.class.getName());

    public static L1World getInstance() {
        if (_instance == null) {
            _instance = new L1World();
        }
        return _instance;
    }

    /** 所有角色. */
    private final ConcurrentHashMap<String, L1PcInstance> _allPlayers;
    /** 所有宠物. */
    private final ConcurrentHashMap<Integer, L1PetInstance> _allPets;
    /** 所有召唤物. */
    private final ConcurrentHashMap<Integer, L1SummonInstance> _allSummons;
    /** 所有对象. */
    private final ConcurrentHashMap<Integer, L1Object> _allObjects;
    /** 可见对象. */
    private final ConcurrentHashMap<Integer, L1Object>[] _visibleObjects;
    /** 所有战争. */
    private final CopyOnWriteArrayList<L1War> _allWars;
    /** 所有血盟. */
    private final ConcurrentHashMap<String, L1Clan> _allClans;
    /** 天气. */
    private int _weather = 4;
    /** 开启世界聊天. */
    private boolean _worldChatEnabled = true;
    /** 处理贡献总额. */
    private boolean _processingContributionTotal = false;
    /** 最大地图编号. */
    private static final int MAX_MAP_ID = 8105;

    private static L1World _instance;

    /** _allObjects的查看. */
    private Collection<L1Object> _allValues;
    /** _allPlayers的查看. */
    private Collection<L1PcInstance> _allPlayerValues;
    /** _allPets的查看. */
    private Collection<L1PetInstance> _allPetValues;
    /** _allSummons的查看. */
    private Collection<L1SummonInstance> _allSummonValues;
    /** _allWars的查看. */
    private List<L1War> _allWarList;
    /** _allClans的查看. */
    private Collection<L1Clan> _allClanValues;

    @SuppressWarnings("unchecked")
    private L1World() {
        _allPlayers = new ConcurrentHashMap<String, L1PcInstance>(); // 所有角色
        _allPets = new ConcurrentHashMap<Integer, L1PetInstance>(); // 所有宠物
        _allSummons = new ConcurrentHashMap<Integer, L1SummonInstance>(); // 所有召唤物
        _allObjects = new ConcurrentHashMap<Integer, L1Object>(); // 所有对象(L1ItemInstance进入、L1Inventory动作)
        _visibleObjects = new ConcurrentHashMap[MAX_MAP_ID + 1]; // 每张地图的对象(L1Inventory进入、L1ItemInstance动作)
        _allWars = new CopyOnWriteArrayList<L1War>(); // 所有战争
        _allClans = new ConcurrentHashMap<String, L1Clan>(); // 所有血盟(Online/Offline都包括)

        for (int i = 0; i <= MAX_MAP_ID; i++) {
            _visibleObjects[i] = new ConcurrentHashMap<Integer, L1Object>();
        }
    }

    /**
     * 增加可见对象.
     * 
     * @param object
     *            - 对象
     */
    public void addVisibleObject(L1Object object) {
        if (object.getMapId() <= MAX_MAP_ID) {
            _visibleObjects[object.getMapId()].put(object.getId(), object);
        }
    }

    /**
     * 增加战争.
     * 
     * @param war
     *            - 战争
     */
    public void addWar(L1War war) {
        if (!_allWars.contains(war)) {
            _allWars.add(war);
        }
    }

    /**
     * 向在线上的所有玩家发送封包.
     * 
     * @param packet
     *            - ServerBasePacket对象、表示要发送的封包
     */
    public void broadcastPacketToAll(ServerBasePacket packet) {
        _log.finest("玩家数量 : " + getAllPlayers().size());
        for (L1PcInstance pc : getAllPlayers()) {
            pc.sendPackets(packet);
        }
    }

    /**
     * 向在线上的所有玩家发送服务器信息.
     * 
     * @param message
     *            - 要发送的信息
     */
    public void broadcastServerMessage(String message) {
        broadcastPacketToAll(new S_SystemMessage(message));
    }

    /**
     * 清空全部状态。<br>
     * 调试和特殊的目的以外呼出。
     */
    public void clear() {
        _instance = new L1World();
    }

    /**
     * 创建Line地图.
     * 
     * @param src
     *            - 原始坐标点
     * @param target
     *            - 目标坐标点
     * @return Line地图
     */
    private ConcurrentHashMap<Integer, Integer> createLineMap(Point src,
            Point target) {
        ConcurrentHashMap<Integer, Integer> lineMap = new ConcurrentHashMap<Integer, Integer>();

        /*
         * http://www2.starcat.ne.jp/~fussy/algo/algo1-1.htmより
         */
        int E;
        int x;
        int y;
        int key;
        int i;
        int x0 = src.getX();
        int y0 = src.getY();
        int x1 = target.getX();
        int y1 = target.getY();
        int sx = (x1 > x0) ? 1 : -1;
        int dx = (x1 > x0) ? x1 - x0 : x0 - x1;
        int sy = (y1 > y0) ? 1 : -1;
        int dy = (y1 > y0) ? y1 - y0 : y0 - y1;

        x = x0;
        y = y0;
        /* 傾きが1以下の場合 */
        if (dx >= dy) {
            E = -dx;
            for (i = 0; i <= dx; i++) {
                key = (x << 16) + y;
                lineMap.put(key, key);
                x += sx;
                E += 2 * dy;
                if (E >= 0) {
                    y += sy;
                    E -= 2 * dx;
                }
            }
            /* 傾きが1より大きい場合 */
        } else {
            E = -dy;
            for (i = 0; i <= dy; i++) {
                key = (x << 16) + y;
                lineMap.put(key, key);
                y += sy;
                E += 2 * dx;
                if (E >= 0) {
                    x += sx;
                    E -= 2 * dy;
                }
            }
        }

        return lineMap;
    }

    /**
     * 发现对象.
     * 
     * @param oID
     *            - 对象ID
     * @return 集合内的对象ID
     */
    public L1Object findObject(int oID) {
        return _allObjects.get(oID);
    }

    /**
     * 取得所有血盟.
     * 
     * @return 集合内的所有血盟值
     */
    public Collection<L1Clan> getAllClans() {
        Collection<L1Clan> vs = _allClanValues;
        return (vs != null) ? vs : (_allClanValues = Collections
                .unmodifiableCollection(_allClans.values()));
    }

    /**
     * 取得所有宠物.
     * 
     * @return 集合内的所有宠物
     */
    public Collection<L1PetInstance> getAllPets() {
        Collection<L1PetInstance> vs = _allPetValues;
        return (vs != null) ? vs : (_allPetValues = Collections
                .unmodifiableCollection(_allPets.values()));
    }

    /**
     * 取得所有角色.
     * 
     * @return 集合内的所有角色
     */
    public Collection<L1PcInstance> getAllPlayers() {
        Collection<L1PcInstance> vs = _allPlayerValues;
        return (vs != null) ? vs : (_allPlayerValues = Collections
                .unmodifiableCollection(_allPlayers.values()));
    }

    /**
     * 取得所有召唤物.
     * 
     * @return 集合内的所有召唤物
     */
    public Collection<L1SummonInstance> getAllSummons() {
        Collection<L1SummonInstance> vs = _allSummonValues;
        return (vs != null) ? vs : (_allSummonValues = Collections
                .unmodifiableCollection(_allSummons.values()));
    }

    /**
     * 取得所有可见对象.
     * 
     * @return 集合内的所有对象
     */
    public final Map<Integer, L1Object> getAllVisibleObjects() {
        return _allObjects;
    }

    /**
     * 取得血盟.
     * 
     * @param clan_name
     *            - 血盟名称
     * @return 集合内的血盟名称
     */
    public L1Clan getClan(String clan_name) {
        return _allClans.get(clan_name);
    }

    /**
     * 取得地面道具.
     * 
     * @param x
     *            - X坐标
     * @param y
     *            - Y坐标
     * @param map
     *            - 地图
     * @return 地面道具object
     */
    public L1GroundInventory getInventory(int x, int y, int map) {
        int inventoryKey = ((x - 30000) * 10000 + (y - 30000)) * -1; // xyのマイナス値をインベントリキーとして使用

        Object object = _visibleObjects[map].get(inventoryKey);
        if (object == null) {
            return new L1GroundInventory(inventoryKey, x, y, map);
        } else {
            return (L1GroundInventory) object;
        }
    }

    /**
     * 取得地面道具.
     * 
     * @param loc
     *            - 位置
     * @return 道具的XY坐标与地图ID
     */
    public L1GroundInventory getInventory(L1Location loc) {
        return getInventory(loc.getX(), loc.getY(), (short) loc.getMap()
                .getId());
    }

    /**
     * 取得对象.
     * 
     * @return 集合内的所有对象
     */
    public Collection<L1Object> getObject() {
        Collection<L1Object> vs = _allValues;
        return (vs != null) ? vs : (_allValues = Collections
                .unmodifiableCollection(_allObjects.values()));
    }

    /**
     * 取得指定名称的玩家(世界上).
     * 
     * @param name
     *            - 玩家名称(大写小写字符将被忽略)
     * @return L1PcInstance指定的名称。如果没有合适的玩家返回null
     */
    public L1PcInstance getPlayer(String name) {
        if (_allPlayers.contains(name)) {
            return _allPlayers.get(name);
        }
        for (L1PcInstance each : getAllPlayers()) {
            if (each.getName().equalsIgnoreCase(name)) {
                return each;
            }
        }
        return null;
    }

    /**
     * 取得object认识范围内的玩家.
     * 
     * @param object
     *            - 对象
     * @return 认识范围内的玩家
     */
    public ArrayList<L1PcInstance> getRecognizePlayer(L1Object object) {
        return getVisiblePlayer(object, Config.PC_RECOGNIZE_RANGE);
    }

    /**
     * 取得区域.
     * 
     * @param object
     *            - 对象
     * @return null
     */
    public Object getRegion(Object object) {
        return null;
    }

    /**
     * 取得可见Box对象 (范围).
     * 
     * @param object
     *            - 对象
     * @param heading
     *            - 面向
     * @param width
     *            - 宽度
     * @param height
     *            - 高度
     * @return 结果
     */
    public ArrayList<L1Object> getVisibleBoxObjects(L1Object object,
            int heading, int width, int height) {
        int x = object.getX();
        int y = object.getY();
        int map = object.getMapId();
        L1Location location = object.getLocation();
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
        double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
        double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);

        if (map <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[map].values()) {
                if (element.equals(object)) {
                    continue;
                }
                if (map != element.getMapId()) {
                    continue;
                }

                // 同じ座標に重なっている場合は範囲内とする
                if (location.isSamePoint(element.getLocation())) {
                    result.add(element);
                    continue;
                }

                int distance = location.getTileLineDistance(element
                        .getLocation());
                // 直線距離が高さ、幅どちらよりも大きい場合、計算するまでもなく範囲外
                if (distance > height && distance > width) {
                    continue;
                }

                // objectの位置を原点とするための座標補正
                int x1 = element.getX() - x;
                int y1 = element.getY() - y;

                // Z軸回転させ角度を0度にする。
                int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
                int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);

                int xmin = 0;
                int xmax = height;
                int ymin = -width;
                int ymax = width;

                // 奥行きが射程とかみ合わないので直線距離で判定するように変更。
                // if (rotX > xmin && rotX <= xmax && rotY >= ymin && rotY <=
                // ymax) {
                if (rotX > xmin && distance <= xmax && rotY >= ymin
                        && rotY <= ymax) {
                    result.add(element);
                }
            }
        }

        return result;
    }

    /**
     * 取得可见Line对象 (直线距离).
     * 
     * @param src
     *            - 原始对象
     * @param target
     *            - 目标对象
     * @return 结果
     */
    public ArrayList<L1Object> getVisibleLineObjects(L1Object src,
            L1Object target) {
        ConcurrentHashMap<Integer, Integer> lineMap = createLineMap(
                src.getLocation(), target.getLocation());

        int map = target.getMapId();
        ArrayList<L1Object> result = new ArrayList<L1Object>();

        if (map <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[map].values()) {
                if (element.equals(src)) {
                    continue;
                }

                int key = (element.getX() << 16) + element.getY();
                if (lineMap.containsKey(key)) {
                    result.add(element);
                }
            }
        }

        return result;
    }

    /**
     * 取得可见对象.
     * 
     * @return 集合内的对象
     */
    public final Map<Integer, L1Object>[] getVisibleObjects() {
        return _visibleObjects;
    }

    /**
     * 取得可见对象.
     * 
     * @param mapId
     *            - 地图ID
     * @return 集合内的地图ID
     */
    public final Map<Integer, L1Object> getVisibleObjects(int mapId) {
        return _visibleObjects[mapId];
    }

    /**
     * 取得可见对象.
     * 
     * @param object
     *            - 对象
     * @return 画面内的可见对象
     */
    public ArrayList<L1Object> getVisibleObjects(L1Object object) {
        return getVisibleObjects(object, -1);
    }

    /**
     * 取得可见对象.
     * 
     * @param object
     *            - 对象
     * @param radius
     *            - 范围
     * @return 结果
     */
    public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
        L1Map map = object.getMap();
        Point pt = object.getLocation();
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        if (map.getId() <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[map.getId()].values()) {
                if (element.equals(object)) {
                    continue;
                }
                if (map != element.getMap()) {
                    continue;
                }

                if (radius == -1) {
                    if (pt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                } else if (radius == 0) {
                    if (pt.isSamePoint(element.getLocation())) {
                        result.add(element);
                    }
                } else {
                    if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                        result.add(element);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 取得可见角色.
     * 
     * @param object
     *            - 对象
     * @return 画面内的可见角色
     */
    public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object) {
        return getVisiblePlayer(object, -1);
    }

    /**
     * 取得可见角色.
     * 
     * @param object
     *            - 对象
     * @param radius
     *            - 范围
     * @return 结果
     */
    public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
        int map = object.getMapId();
        Point pt = object.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();

        for (L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }

            if (map != element.getMapId()) {
                continue;
            }

            if (radius == -1) {
                if (pt.isInScreen(element.getLocation())) {
                    result.add(element);
                }
            } else if (radius == 0) {
                if (pt.isSamePoint(element.getLocation())) {
                    result.add(element);
                }
            } else {
                if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    /**
     * 取得可见角色、排除目标视线.
     * 
     * @param object
     *            - 对象
     * @param target
     *            - 目标
     * @return 结果
     */
    public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(
            L1Object object, L1Object target) {
        int map = object.getMapId();
        Point objectPt = object.getLocation();
        Point targetPt = target.getLocation();
        ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();

        for (L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }

            if (map != element.getMapId()) {
                continue;
            }

            if (Config.PC_RECOGNIZE_RANGE == -1) {
                if (objectPt.isInScreen(element.getLocation())) {
                    if (!targetPt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                }
            } else {
                if (objectPt.getTileLineDistance(element.getLocation()) <= Config.PC_RECOGNIZE_RANGE) {
                    if (targetPt.getTileLineDistance(element.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                        result.add(element);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 取得可见坐标.
     * 
     * @param loc
     *            - 位置
     * @param radius
     *            - 范围
     * @return 结果
     */
    public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
        ArrayList<L1Object> result = new ArrayList<L1Object>();
        int mapId = loc.getMapId(); // 在一个循环中呼出

        if (mapId <= MAX_MAP_ID) {
            for (L1Object element : _visibleObjects[mapId].values()) {
                if (mapId != element.getMapId()) {
                    continue;
                }

                if (loc.getTileLineDistance(element.getLocation()) <= radius) {
                    result.add(element);
                }
            }
        }

        return result;
    }

    /**
     * 取得战争列表.
     * 
     * @return 集合内的所有战争
     */
    public List<L1War> getWarList() {
        List<L1War> vs = _allWarList;
        return (vs != null) ? vs : (_allWarList = Collections
                .unmodifiableList(_allWars));
    }

    /**
     * 取得天气.
     * 
     * @return 天气
     */
    public int getWeather() {
        return _weather;
    }

    /**
     * 是否处理贡献总额.
     * 
     * @return True：开启、False：关闭
     */
    public boolean isProcessingContributionTotal() {
        return _processingContributionTotal;
    }

    /**
     * 是否开启世界聊天.
     * 
     * @return True：开启、False：关闭
     */
    public boolean isWorldChatElabled() {
        return _worldChatEnabled;
    }

    /**
     * 移动可见对象(set_Mapで新しいMapにするまえに呼ぶこと).
     * 
     * @param object
     *            - 对象
     * @param newMap
     *            - 新地图
     */
    public void moveVisibleObject(L1Object object, int newMap) {
        if (object.getMapId() != newMap) {
            if (object.getMapId() <= MAX_MAP_ID) {
                _visibleObjects[object.getMapId()].remove(object.getId());
            }
            if (newMap <= MAX_MAP_ID) {
                _visibleObjects[newMap].put(object.getId(), object);
            }
        }
    }

    /**
     * 删除血盟.
     * 
     * @param clan
     *            - 血盟
     */
    public void removeClan(L1Clan clan) {
        L1Clan temp = getClan(clan.getClanName());
        if (temp != null) {
            _allClans.remove(clan.getClanName());
        }
    }

    /**
     * 删除对象.
     * 
     * @param object
     *            - 对象
     */
    public void removeObject(L1Object object) {
        if (object == null) {
            throw new NullPointerException();
        }

        _allObjects.remove(object.getId());
        if (object instanceof L1PcInstance) {
            _allPlayers.remove(((L1PcInstance) object).getName());
        }
        if (object instanceof L1PetInstance) {
            _allPets.remove(object.getId());
        }
        if (object instanceof L1SummonInstance) {
            _allSummons.remove(object.getId());
        }
    }

    /**
     * 删除可见对象.
     * 
     * @param object
     *            - 对象
     */
    public void removeVisibleObject(L1Object object) {
        if (object.getMapId() <= MAX_MAP_ID) {
            _visibleObjects[object.getMapId()].remove(object.getId());
        }
    }

    /**
     * 删除战争.
     * 
     * @param war
     *            - 战争
     */
    public void removeWar(L1War war) {
        if (_allWars.contains(war)) {
            _allWars.remove(war);
        }
    }

    /**
     * 设置是否开启世界聊天.
     * 
     * @param flag
     *            - True or False
     */
    public void set_worldChatElabled(boolean flag) {
        _worldChatEnabled = flag;
    }

    /**
     * 设置是否处理贡献总额.
     * 
     * @param flag
     *            - True or False
     */
    public void setProcessingContributionTotal(boolean flag) {
        _processingContributionTotal = flag;
    }

    /**
     * 设置天气.
     * 
     * @param weather
     *            - 天气
     */
    public void setWeather(int weather) {
        _weather = weather;
    }

    /**
     * 保存血盟.
     * 
     * @param clan
     *            - 血盟
     */
    public void storeClan(L1Clan clan) {
        L1Clan temp = getClan(clan.getClanName());
        if (temp == null) {
            _allClans.put(clan.getClanName(), clan);
        }
    }

    /**
     * 保存对象.
     * 
     * @param object
     *            - 对象
     */
    public void storeObject(L1Object object) {
        if (object == null) {
            throw new NullPointerException();
        }

        _allObjects.put(object.getId(), object);
        if (object instanceof L1PcInstance) {
            _allPlayers.put(((L1PcInstance) object).getName(),
                    (L1PcInstance) object);
        }
        if (object instanceof L1PetInstance) {
            _allPets.put(object.getId(), (L1PetInstance) object);
        }
        if (object instanceof L1SummonInstance) {
            _allSummons.put(object.getId(), (L1SummonInstance) object);
        }
    }
}
