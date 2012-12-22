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

public class L1Pet {
    private int _itemobjid;

    private int _objid;

    private int _npcid;

    private String _name;

    private int _level;

    private int _hp;

    private int _mp;

    private int _exp;

    private int _lawful;

    public L1Pet() {
    }

    public int get_exp() {
        return _exp;
    }

    public int get_hp() {
        return _hp;
    }

    public int get_itemobjid() {
        return _itemobjid;
    }

    public int get_lawful() {
        return _lawful;
    }

    public int get_level() {
        return _level;
    }

    public int get_mp() {
        return _mp;
    }

    public String get_name() {
        return _name;
    }

    public int get_npcid() {
        return _npcid;
    }

    public int get_objid() {
        return _objid;
    }

    public void set_exp(int i) {
        _exp = i;
    }

    public void set_hp(int i) {
        _hp = i;
    }

    public void set_itemobjid(int i) {
        _itemobjid = i;
    }

    public void set_lawful(int i) {
        _lawful = i;
    }

    public void set_level(int i) {
        _level = i;
    }

    public void set_mp(int i) {
        _mp = i;
    }

    public void set_name(String s) {
        _name = s;
    }

    public void set_npcid(int i) {
        _npcid = i;
    }

    public void set_objid(int i) {
        _objid = i;
    }
}
