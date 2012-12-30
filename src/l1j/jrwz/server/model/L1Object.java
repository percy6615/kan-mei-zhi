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

import java.io.Serializable;

import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.map.L1Map;
import l1j.jrwz.server.model.map.L1WorldMap;

// Referenced classes of package l1j.jrwz.server.model:
// L1PcInstance, L1Character

/**
 * 世界上存在的所有对象的基类.
 */
public class L1Object implements Serializable {
    private static final long serialVersionUID = 1L;

    private final L1Location _loc = new L1Location();

    private int _id = 0;

    /**
     * 取得该对象的唯一ID.
     * 
     * @return 对象ID
     */
    public int getId() {
        return _id;
    }

    /**
     * 取得与另一个对象间的直线距离.
     * 
     * @param obj
     *            - 对象
     * @return 对象的坐标
     */
    public double getLineDistance(L1Object obj) {
        return this.getLocation().getLineDistance(obj.getLocation());
    }

    /**
     * 取得对象存在在地图上的L1Location.
     * 
     * @return 保存的坐标、对象参照L1Location
     */
    public L1Location getLocation() {
        return _loc;
    }

    /**
     * 取得对象所存在的地图(保存在L1Map内的).
     * 
     * @return 地图
     */
    public L1Map getMap() {
        return _loc.getMap();
    }

    /**
     * 取得对象所在的地图ID.
     * 
     * @return 地图ID
     */
    public short getMapId() {
        return (short) _loc.getMap().getId();
    }

    /**
     * 取得指定的对象与另一个对象间的X轴+Y轴的距离.
     * 
     * @param obj
     *            - 对象
     * @return 坐标位置
     */
    public int getTileDistance(L1Object obj) {
        return this.getLocation().getTileDistance(obj.getLocation());
    }

    /**
     * 取得直线距离上指定的对象与另一个对象间的距离X轴或Y轴较大的那一个.
     * 
     * @param obj
     *            - 对象
     * @return 坐标位置
     */
    public int getTileLineDistance(L1Object obj) {
        return this.getLocation().getTileLineDistance(obj.getLocation());
    }

    /**
     * 取得该对象所在的X坐标.
     * 
     * @return X坐标
     */
    public int getX() {
        return _loc.getX();
    }

    /**
     * 取得该对象所在的Y坐标.
     * 
     * @return Y坐标
     */
    public int getY() {
        return _loc.getY();
    }

    /**
     * 对象的动作发生时将调用.
     * 
     * @param actionFrom
     *            - 引发动作的PC
     */
    public void onAction(L1PcInstance actionFrom) {
    }

    /**
     * 画面内进入对象时将调用(认识的).
     * 
     * @param perceivedFrom
     *            - PC认识的物件
     */
    public void onPerceive(L1PcInstance perceivedFrom) {
    }

    /**
     * 对象谈话时将调用.
     * 
     * @param talkFrom
     *            - 谈话的PC
     */
    public void onTalkAction(L1PcInstance talkFrom) {
    }

    /**
     * 设置能够唯一标识该物件的ID.
     * 
     * @param id
     *            物件ID
     */
    public void setId(int id) {
        _id = id;
    }

    /**
     * 设置坐标位置.
     * 
     * @param x
     *            - X坐标
     * @param y
     *            - Y坐标
     * @param mapid
     *            - 地图编号
     */
    public void setLocation(int x, int y, int mapid) {
        _loc.setX(x);
        _loc.setY(y);
        _loc.setMap(mapid);
    }

    /**
     * 设置坐标位置.
     * 
     * @param loc
     *            - 位置信息
     */
    public void setLocation(L1Location loc) {
        _loc.setX(loc.getX());
        _loc.setY(loc.getY());
        _loc.setMap(loc.getMapId());
    }

    /**
     * 设置对象所在的地图的值.
     * 
     * @param map
     *            - 保存在L1Map中所存在的对象
     */
    public void setMap(L1Map map) {
        if (map == null) {
            throw new NullPointerException();
        }
        _loc.setMap(map);
    }

    /**
     * 设置对象所在的地图ID的值.
     * 
     * @param mapId
     *            - 地图ID
     */
    public void setMap(int mapId) {
        _loc.setMap(L1WorldMap.getInstance().getMap(mapId));
    }

    /**
     * 设置对象所在的X坐标的值.
     * 
     * @param x
     *            - X坐标
     */
    public void setX(int x) {
        _loc.setX(x);
    }

    /**
     * 设置对象所在的Y坐标的值.
     * 
     * @param y
     *            - Y坐标
     */
    public void setY(int y) {
        _loc.setY(y);
    }
}
