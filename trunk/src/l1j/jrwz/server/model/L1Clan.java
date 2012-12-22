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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.server.datatables.CharacterTable;
import l1j.jrwz.server.model.Instance.L1PcInstance;

public class L1Clan {

    public static final int CLAN_RANK_PROBATION = 1;
    public static final int CLAN_RANK_PUBLIC = 2;
    public static final int CLAN_RANK_GUARDIAN = 3;
    public static final int CLAN_RANK_PRINCE = 4;

    private static final Logger _log = Logger.getLogger(L1Clan.class.getName());

    private int _clanId;

    private String _clanName;

    private int _leaderId;

    private String _leaderName;

    private int _castleId;

    private int _houseId;

    private int _warehouse = 0;

    private final L1DwarfForClanInventory _dwarfForClan = new L1DwarfForClanInventory(
            this);

    private final ArrayList<String> membersNameList = new ArrayList<String>();

    public void addMemberName(String member_name) {
        if (!membersNameList.contains(member_name)) {
            membersNameList.add(member_name);
        }
    }

    public void delMemberName(String member_name) {
        if (membersNameList.contains(member_name)) {
            membersNameList.remove(member_name);
        }
    }

    public String[] getAllMembers() {
        return membersNameList.toArray(new String[membersNameList.size()]);
    }

    public String getAllMembersFP() {
        String result = "";
        for (String name : membersNameList) {
            result = result + name + " ";
        }
        return result;
    }

    public String getAllMembersFPWithRank() {
        String result = "";
        try {
            for (String name : membersNameList) {
                L1PcInstance pc = CharacterTable.getInstance()
                        .restoreCharacter(name);
                if (pc != null) {
                    result = result + name + getRankString(pc) + " ";
                }
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return result;
    }

    public int getCastleId() {
        return _castleId;
    }

    public int getClanId() {
        return _clanId;
    }

    public String getClanName() {
        return _clanName;
    }

    public L1DwarfForClanInventory getDwarfForClanInventory() {
        return _dwarfForClan;
    }

    public int getHouseId() {
        return _houseId;
    }

    public int getLeaderId() {
        return _leaderId;
    }

    public String getLeaderName() {
        return _leaderName;
    }

    public L1PcInstance[] getOnlineClanMember() // オンライン中のクラン員のみ
    {
        ArrayList<L1PcInstance> onlineMembers = new ArrayList<L1PcInstance>();
        for (String name : membersNameList) {
            L1PcInstance pc = L1World.getInstance().getPlayer(name);
            if (pc != null && !onlineMembers.contains(pc)) {
                onlineMembers.add(pc);
            }
        }
        return onlineMembers.toArray(new L1PcInstance[onlineMembers.size()]);
    }

    public String getOnlineMembersFP() { // FP means "For Pledge"
        String result = "";
        for (String name : membersNameList) {
            L1PcInstance pc = L1World.getInstance().getPlayer(name);
            if (pc != null) {
                result = result + name + " ";
            }
        }
        return result;
    }

    public String getOnlineMembersFPWithRank() {
        String result = "";
        for (String name : membersNameList) {
            L1PcInstance pc = L1World.getInstance().getPlayer(name);
            if (pc != null) {
                result = result + name + getRankString(pc) + " ";
            }
        }
        return result;
    }

    private String getRankString(L1PcInstance pc) {
        String rank = "";
        if (pc != null) {
            if (pc.getClanRank() == CLAN_RANK_PROBATION) {
                rank = "[见习]";
            } else if (pc.getClanRank() == CLAN_RANK_PUBLIC) {
                rank = "[一般]";
            } else if (pc.getClanRank() == CLAN_RANK_GUARDIAN) {
                rank = "[守护骑士]";
            } else if (pc.getClanRank() == CLAN_RANK_PRINCE) {
                rank = "[血盟盟主]";
            } else {
                rank = "";
            }
        }
        return rank;
    }

    public int getWarehouseUsingChar() {
        return _warehouse;
    }

    public void setCastleId(int hasCastle) {
        _castleId = hasCastle;
    }

    public void setClanId(int clan_id) {
        _clanId = clan_id;
    }

    public void setClanName(String clan_name) {
        _clanName = clan_name;
    }

    public void setHouseId(int hasHideout) {
        _houseId = hasHideout;
    }

    public void setLeaderId(int leader_id) {
        _leaderId = leader_id;
    }

    public void setLeaderName(String leader_name) {
        _leaderName = leader_name;
    }

    public void setWarehouseUsingChar(int objid) {
        _warehouse = objid;
    }
}
