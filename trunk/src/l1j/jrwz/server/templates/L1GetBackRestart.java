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

public class L1GetBackRestart {
    private int _area;

    private String _note;

    private int _locX;

    private int _locY;

    private short _mapId;

    public L1GetBackRestart() {
    }

    public int getArea() {
        return _area;
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

    public String getNote() {
        return _note;
    }

    public void setArea(int i) {
        _area = i;
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

    public void setNote(String s) {
        _note = s;
    }

}
