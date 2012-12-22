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
import java.util.List;
import java.util.logging.Logger;

import l1j.jrwz.server.model.Instance.L1NpcInstance;

// Referenced classes of package l1j.jrwz.server.model:
// L1MobGroupInfo

public class L1MobGroupInfo {
    @SuppressWarnings("unused")
    private static final Logger _log = Logger.getLogger(L1MobGroupInfo.class
            .getName());

    private final List<L1NpcInstance> _membersList = new ArrayList<L1NpcInstance>();

    private L1NpcInstance _leader;

    private L1Spawn _spawn;

    private boolean _isRemoveGroup;

    public L1MobGroupInfo() {
    }

    public void addMember(L1NpcInstance npc) {
        if (npc == null) {
            throw new NullPointerException();
        }

        // 最初のメンバーであればリーダーにする
        if (_membersList.isEmpty()) {
            setLeader(npc);
            // リーダーの再ポップ情報を保存する
            if (npc.isReSpawn()) {
                setSpawn(npc.getSpawn());
            }
        }

        if (!_membersList.contains(npc)) {
            _membersList.add(npc);
        }
        npc.setMobGroupInfo(this);
        npc.setMobGroupId(_leader.getId());
    }

    public L1NpcInstance getLeader() {
        return _leader;
    }

    public int getNumOfMembers() {
        return _membersList.size();
    }

    public L1Spawn getSpawn() {
        return _spawn;
    }

    public boolean isLeader(L1NpcInstance npc) {
        return npc.getId() == _leader.getId();
    }

    public boolean isRemoveGroup() {
        return _isRemoveGroup;
    }

    public synchronized int removeMember(L1NpcInstance npc) {
        if (npc == null) {
            throw new NullPointerException();
        }

        if (_membersList.contains(npc)) {
            _membersList.remove(npc);
        }
        npc.setMobGroupInfo(null);

        // リーダーで他のメンバーがいる場合は、新リーダーにする
        if (isLeader(npc)) {
            if (isRemoveGroup() && _membersList.size() != 0) { // リーダーが死亡したらグループ解除する場合
                for (L1NpcInstance minion : _membersList) {
                    minion.setMobGroupInfo(null);
                    minion.setSpawn(null);
                    minion.setreSpawn(false);
                }
                return 0;
            }
            if (_membersList.size() != 0) {
                setLeader(_membersList.get(0));
            }
        }

        // 残りのメンバー数を返す
        return _membersList.size();
    }

    public void setLeader(L1NpcInstance npc) {
        _leader = npc;
    }

    public void setRemoveGroup(boolean flag) {
        _isRemoveGroup = flag;
    }

    public void setSpawn(L1Spawn spawn) {
        _spawn = spawn;
    }

}
