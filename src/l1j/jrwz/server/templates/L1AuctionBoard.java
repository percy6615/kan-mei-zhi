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

public class L1AuctionBoard {
    private int _houseId;

    private String _houseName;

    private int _houseArea;

    private Calendar _deadline;

    private int _price;

    private String _location;

    private String _oldOwner;

    private int _oldOwnerId;

    private String _bidder;

    private int _bidderId;

    public L1AuctionBoard() {
    }

    public String getBidder() {
        return _bidder;
    }

    public int getBidderId() {
        return _bidderId;
    }

    public Calendar getDeadline() {
        return _deadline;
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

    public String getLocation() {
        return _location;
    }

    public String getOldOwner() {
        return _oldOwner;
    }

    public int getOldOwnerId() {
        return _oldOwnerId;
    }

    public int getPrice() {
        return _price;
    }

    public void setBidder(String s) {
        _bidder = s;
    }

    public void setBidderId(int i) {
        _bidderId = i;
    }

    public void setDeadline(Calendar i) {
        _deadline = i;
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

    public void setLocation(String s) {
        _location = s;
    }

    public void setOldOwner(String s) {
        _oldOwner = s;
    }

    public void setOldOwnerId(int i) {
        _oldOwnerId = i;
    }

    public void setPrice(int i) {
        _price = i;
    }

}
