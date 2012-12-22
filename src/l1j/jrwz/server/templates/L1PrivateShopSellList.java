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
// L1PrivateShopSellList

public class L1PrivateShopSellList {
    private int _itemObjectId;

    private int _sellTotalCount; // 売る予定の個数

    private int _sellPrice;

    private int _sellCount; // 売った累計

    public L1PrivateShopSellList() {
    }

    public int getItemObjectId() {
        return _itemObjectId;
    }

    public int getSellCount() {
        return _sellCount;
    }

    public int getSellPrice() {
        return _sellPrice;
    }

    public int getSellTotalCount() {
        return _sellTotalCount;
    }

    public void setItemObjectId(int i) {
        _itemObjectId = i;
    }

    public void setSellCount(int i) {
        _sellCount = i;
    }

    public void setSellPrice(int i) {
        _sellPrice = i;
    }

    public void setSellTotalCount(int i) {
        _sellTotalCount = i;
    }
}
