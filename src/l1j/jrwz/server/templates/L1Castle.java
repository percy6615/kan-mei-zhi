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

public class L1Castle {
    private int _id;

    private String _name;

    private Calendar _warTime;

    private int _taxRate;

    private int _publicMoney;

    public L1Castle(int id, String name) {
        _id = id;
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getPublicMoney() {
        return _publicMoney;
    }

    public int getTaxRate() {
        return _taxRate;
    }

    public Calendar getWarTime() {
        return _warTime;
    }

    public void setPublicMoney(int i) {
        _publicMoney = i;
    }

    public void setTaxRate(int i) {
        _taxRate = i;
    }

    public void setWarTime(Calendar i) {
        _warTime = i;
    }

}
