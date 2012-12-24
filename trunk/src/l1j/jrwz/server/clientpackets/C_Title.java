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

package l1j.jrwz.server.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_CharTitle;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來變更稱號的封包
 */
public class C_Title extends ClientBasePacket {

    private static final String C_TITLE = "[C] C_Title";
    private static Logger _log = Logger.getLogger(C_Title.class.getName());

    public C_Title(byte abyte0[], ClientThread clientthread) {
        super(abyte0);
        L1PcInstance pc = clientthread.getActiveChar();
        String charName = readS();
        String title = readS();

        if (charName.isEmpty() || title.isEmpty()) {
            // \f1请以如下的格式输入: "/title+空格+角色名称+要赋予的称号"
            pc.sendPackets(new S_ServerMessage(196));
            return;
        }
        L1PcInstance target = L1World.getInstance().getPlayer(charName);
        if (target == null) {
            return;
        }

        if (pc.isGm()) {
            changeTitle(target, title);
            return;
        }

        if (isClanLeader(pc)) { // 血盟主
            if (pc.getId() == target.getId()) { // 自己
                if (pc.getLevel() < 10) {
                    // \f1加入血盟之后等级10以上才可使用称号。
                    pc.sendPackets(new S_ServerMessage(197));
                    return;
                }
                changeTitle(pc, title);
            } else { // 他人
                if (pc.getClanid() != target.getClanid()) {
                    // \f1除了王族以外的人，不能够授与头衔给其他人。
                    pc.sendPackets(new S_ServerMessage(199));
                    return;
                }
                if (target.getLevel() < 10) {
                    // \f1%0的等级还不到10级，因此无法封称号。
                    pc.sendPackets(new S_ServerMessage(202, charName));
                    return;
                }
                changeTitle(target, title);
                L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
                if (clan != null) {
                    for (L1PcInstance clanPc : clan.getOnlineClanMember()) {
                        // \f1%0%s 赋予%1 '%2'称号。
                        clanPc.sendPackets(new S_ServerMessage(203, pc
                                .getName(), charName, title));
                    }
                }
            }
        } else {
            if (pc.getId() == target.getId()) { // 自分
                if (pc.getClanid() != 0 && !Config.CHANGE_TITLE_BY_ONESELF) {
                    // \f1王子或公主才可给血盟员封称号。
                    pc.sendPackets(new S_ServerMessage(198));
                    return;
                }
                if (target.getLevel() < 40) {
                    // \f1若等级40以上，没有加入血盟也可拥有称号。
                    pc.sendPackets(new S_ServerMessage(200));
                    return;
                }
                changeTitle(pc, title);
            } else { // 他人
                if (pc.isCrown()) { // 連合に所属した君主
                    if (pc.getClanid() == target.getClanid()) {
                        // \f1%0%d 不是你的血盟成员。
                        pc.sendPackets(new S_ServerMessage(201, pc
                                .getClanname()));
                        return;
                    }
                }
            }
        }
    }

    private void changeTitle(L1PcInstance pc, String title) {
        int objectId = pc.getId();
        pc.setTitle(title);
        pc.sendPackets(new S_CharTitle(objectId, title));
        pc.broadcastPacket(new S_CharTitle(objectId, title));
        try {
            pc.save(); // 儲存玩家的資料到資料庫中
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String getType() {
        return C_TITLE;
    }

    private boolean isClanLeader(L1PcInstance pc) {
        boolean isClanLeader = false;
        if (pc.getClanid() != 0) { // 有血盟
            L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
            if (clan != null) {
                if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 君主、かつ、血盟主
                    isClanLeader = true;
                }
            }
        }
        return isClanLeader;
    }

}
