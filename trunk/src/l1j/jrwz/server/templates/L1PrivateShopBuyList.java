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

// Referenced classes of package l1j.jrwz.server.templates:
// L1PrivateShopBuyList

public class L1PrivateShopBuyList {
    private int _itemObjectId;

    private int _buyTotalCount; // 買う予定の個数

    private int _buyPrice;

    private int _buyCount; // 買った累計

    public L1PrivateShopBuyList() {
    }

    public int getBuyCount() {
        return _buyCount;
    }

    public int getBuyPrice() {
        return _buyPrice;
    }

    public int getBuyTotalCount() {
        return _buyTotalCount;
    }

    public int getItemObjectId() {
        return _itemObjectId;
    }

    public void setBuyCount(int i) {
        _buyCount = i;
    }

    public void setBuyPrice(int i) {
        _buyPrice = i;
    }

    public void setBuyTotalCount(int i) {
        _buyTotalCount = i;
    }

    public void setItemObjectId(int i) {
        _itemObjectId = i;
    }
}
