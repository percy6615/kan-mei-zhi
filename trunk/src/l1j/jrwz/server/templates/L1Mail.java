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

public class L1Mail {
    private int _id;

    private int _type;

    private String _senderName;

    private String _receiverName;

    private String _date = null; // yy/mm/dd

    private int _readStatus = 0;

    private byte[] _subject = null;

    private byte[] _content = null;

    public L1Mail() {
    }

    public byte[] getContent() {
        return _content;
    }

    public String getDate() {
        return _date;
    }

    public int getId() {
        return _id;
    }

    public int getReadStatus() {
        return _readStatus;
    }

    public String getReceiverName() {
        return _receiverName;
    }

    public String getSenderName() {
        return _senderName;
    }

    public byte[] getSubject() {
        return _subject;
    }

    public int getType() {
        return _type;
    }

    public void setContent(byte[] arg) {
        _content = arg;
    }

    public void setDate(String s) {
        _date = s;
    }

    public void setId(int i) {
        _id = i;
    }

    public void setReadStatus(int i) {
        _readStatus = i;
    }

    public void setReceiverName(String s) {
        _receiverName = s;
    }

    public void setSenderName(String s) {
        _senderName = s;
    }

    public void setSubject(byte[] arg) {
        _subject = arg;
    }

    public void setType(int i) {
        _type = i;
    }

}
