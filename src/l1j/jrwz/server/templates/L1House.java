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

import java.util.Calendar;

public class L1House {
    private int _houseId;

    private String _houseName;

    private int _houseArea;

    private String _location;

    private int _keeperId;

    private boolean _isOnSale;

    private boolean _isPurchaseBasement;

    private Calendar _taxDeadline;

    public L1House() {
    }

    public int getHouseArea() {
        return _houseArea;
    }

    public int getHouseId() {
        return _houseId;
    }

    public String getHouseName() {
        return _houseName;
    }

    public int getKeeperId() {
        return _keeperId;
    }

    public String getLocation() {
        return _location;
    }

    public Calendar getTaxDeadline() {
        return _taxDeadline;
    }

    public boolean isOnSale() {
        return _isOnSale;
    }

    public boolean isPurchaseBasement() {
        return _isPurchaseBasement;
    }

    public void setHouseArea(int i) {
        _houseArea = i;
    }

    public void setHouseId(int i) {
        _houseId = i;
    }

    public void setHouseName(String s) {
        _houseName = s;
    }

    public void setKeeperId(int i) {
        _keeperId = i;
    }

    public void setLocation(String s) {
        _location = s;
    }

    public void setOnSale(boolean flag) {
        _isOnSale = flag;
    }

    public void setPurchaseBasement(boolean flag) {
        _isPurchaseBasement = flag;
    }

    public void setTaxDeadline(Calendar i) {
        _taxDeadline = i;
    }

}
