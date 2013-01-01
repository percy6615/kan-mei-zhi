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

import static l1j.jrwz.server.model.skill.L1SkillId.CANCELLATION;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import l1j.jrwz.server.model.Instance.L1DoorInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.skill.L1SkillUse;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

public class L1HauntedHouse {
    public class L1HauntedHouseReadyTimer extends TimerTask {

        public L1HauntedHouseReadyTimer() {
        }

        public void begin() {
            Timer timer = new Timer();
            timer.schedule(this, 90000); // 90秒くらい？
        }

        @Override
        public void run() {
            startHauntedHouse();
            L1HauntedHouseTimer hhTimer = new L1HauntedHouseTimer();
            hhTimer.begin();
        }

    }

    public class L1HauntedHouseTimer extends TimerTask {

        public L1HauntedHouseTimer() {
        }

        public void begin() {
            Timer timer = new Timer();
            timer.schedule(this, 300000); // 5分
        }

        @Override
        public void run() {
            endHauntedHouse();
            this.cancel();
        }

    }

    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(L1HauntedHouse.class
            .getName());
    public static final int STATUS_NONE = 0;

    public static final int STATUS_READY = 1;
    public static final int STATUS_PLAYING = 2;
    private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();
    private int _hauntedHouseStatus = STATUS_NONE;

    private int _winnersCount = 0;

    private int _goalCount = 0;

    private static L1HauntedHouse _instance;

    public static L1HauntedHouse getInstance() {
        if (_instance == null) {
            _instance = new L1HauntedHouse();
        }
        return _instance;
    }

    public void addMember(L1PcInstance pc) {
        if (!_members.contains(pc)) {
            _members.add(pc);
        }
        if (getMembersCount() == 1 && getHauntedHouseStatus() == STATUS_NONE) {
            readyHauntedHouse();
        }
    }

    public void clearMembers() {
        _members.clear();
    }

    public void endHauntedHouse() {
        setHauntedHouseStatus(STATUS_NONE);
        setWinnersCount(0);
        setGoalCount(0);
        for (L1PcInstance pc : getMembersArray()) {
            if (pc.getMapId() == 5140) {
                L1SkillUse l1skilluse = new L1SkillUse();
                l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(),
                        pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
                L1Teleport.teleport(pc, 32624, 32813, 4, 5, true);
            }
        }
        clearMembers();
        for (L1Object object : L1World.getInstance().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getMapId() == 5140) {
                    door.close();
                }
            }
        }
    }

    public int getGoalCount() {
        return _goalCount;
    }

    public int getHauntedHouseStatus() {
        return _hauntedHouseStatus;
    }

    public L1PcInstance[] getMembersArray() {
        return _members.toArray(new L1PcInstance[_members.size()]);
    }

    public int getMembersCount() {
        return _members.size();
    }

    public int getWinnersCount() {
        return _winnersCount;
    }

    public boolean isMember(L1PcInstance pc) {
        return _members.contains(pc);
    }

    private void readyHauntedHouse() {
        setHauntedHouseStatus(STATUS_READY);
        L1HauntedHouseReadyTimer hhrTimer = new L1HauntedHouseReadyTimer();
        hhrTimer.begin();
    }

    public void removeMember(L1PcInstance pc) {
        _members.remove(pc);
    }

    public void removeRetiredMembers() {
        L1PcInstance[] temp = getMembersArray();
        for (L1PcInstance element : temp) {
            if (element.getMapId() != 5140) {
                removeMember(element);
            }
        }
    }

    public void sendMessage(int type, String msg) {
        for (L1PcInstance pc : getMembersArray()) {
            pc.sendPackets(new S_ServerMessage(type, msg));
        }
    }

    public void setGoalCount(int i) {
        _goalCount = i;
    }

    private void setHauntedHouseStatus(int i) {
        _hauntedHouseStatus = i;
    }

    private void setWinnersCount(int i) {
        _winnersCount = i;
    }

    private void startHauntedHouse() {
        setHauntedHouseStatus(STATUS_PLAYING);
        int membersCount = getMembersCount();
        if (membersCount <= 4) {
            setWinnersCount(1);
        } else if (5 >= membersCount && membersCount <= 7) {
            setWinnersCount(2);
        } else if (8 >= membersCount && membersCount <= 10) {
            setWinnersCount(3);
        }
        for (L1PcInstance pc : getMembersArray()) {
            L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(),
                    pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
            L1PolyMorph.doPoly(pc, 6284, 300, L1PolyMorph.MORPH_BY_NPC);
        }

        for (L1Object object : L1World.getInstance().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getMapId() == 5140) {
                    door.open();
                }
            }
        }
    }

}
