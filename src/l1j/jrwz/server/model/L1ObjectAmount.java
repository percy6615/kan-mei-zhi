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

/**
 * 对象数量.
 */
public class L1ObjectAmount<T> {
    private final T _obj;
    private final int _amount;

    /**
     * 对象数量.
     * 
     * @param obj
     *            - 对象
     * @param amount
     *            - 数量
     */
    public L1ObjectAmount(T obj, int amount) {
        _obj = obj;
        _amount = amount;
    }

    /**
     * 取得对象数量.
     * 
     * @return 数量
     */
    public int getAmount() {
        return _amount;
    }

    /**
     * 取得对象.
     * 
     * @return 对象
     */
    public T getObject() {
        return _obj;
    }
}
