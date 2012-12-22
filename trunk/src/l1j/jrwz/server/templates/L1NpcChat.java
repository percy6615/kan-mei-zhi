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

public class L1NpcChat {
    private int _npcId;

    private int _chatTiming;

    private int _startDelayTime;

    private String _chatId1;

    private String _chatId2;

    private String _chatId3;

    private String _chatId4;

    private String _chatId5;

    private int _chatInterval;

    private boolean _isShout;

    private boolean _isWorldChat;

    private boolean _isRepeat;

    private int _repeatInterval;

    private int _gameTime;

    public L1NpcChat() {
    }

    public String getChatId1() {
        return _chatId1;
    }

    public String getChatId2() {
        return _chatId2;
    }

    public String getChatId3() {
        return _chatId3;
    }

    public String getChatId4() {
        return _chatId4;
    }

    public String getChatId5() {
        return _chatId5;
    }

    public int getChatInterval() {
        return _chatInterval;
    }

    public int getChatTiming() {
        return _chatTiming;
    }

    public int getGameTime() {
        return _gameTime;
    }

    public int getNpcId() {
        return _npcId;
    }

    public int getRepeatInterval() {
        return _repeatInterval;
    }

    public int getStartDelayTime() {
        return _startDelayTime;
    }

    public boolean isRepeat() {
        return _isRepeat;
    }

    public boolean isShout() {
        return _isShout;
    }

    public boolean isWorldChat() {
        return _isWorldChat;
    }

    public void setChatId1(String s) {
        _chatId1 = s;
    }

    public void setChatId2(String s) {
        _chatId2 = s;
    }

    public void setChatId3(String s) {
        _chatId3 = s;
    }

    public void setChatId4(String s) {
        _chatId4 = s;
    }

    public void setChatId5(String s) {
        _chatId5 = s;
    }

    public void setChatInterval(int i) {
        _chatInterval = i;
    }

    public void setChatTiming(int i) {
        _chatTiming = i;
    }

    public void setGameTime(int i) {
        _gameTime = i;
    }

    public void setNpcId(int i) {
        _npcId = i;
    }

    public void setRepeat(boolean flag) {
        _isRepeat = flag;
    }

    public void setRepeatInterval(int i) {
        _repeatInterval = i;
    }

    public void setShout(boolean flag) {
        _isShout = flag;
    }

    public void setStartDelayTime(int i) {
        _startDelayTime = i;
    }

    public void setWorldChat(boolean flag) {
        _isWorldChat = flag;
    }

}
