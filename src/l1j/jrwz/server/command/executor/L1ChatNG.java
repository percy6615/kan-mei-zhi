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
package l1j.jrwz.server.command.executor;

import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;

import java.util.StringTokenizer;

import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.serverpackets.S_SkillIconGFX;
import l1j.jrwz.server.serverpackets.S_SystemMessage;

/**
 * GM指令：禁言
 */
public class L1ChatNG implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1ChatNG.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1ChatNG();
    }

    private L1ChatNG() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String name = st.nextToken();
            int time = Integer.parseInt(st.nextToken());

            L1PcInstance tg = L1World.getInstance().getPlayer(name);

            if (tg != null) {
                tg.setSkillEffect(STATUS_CHAT_PROHIBITED, time * 60 * 1000);
                tg.sendPackets(new S_SkillIconGFX(36, time * 60));
                tg.sendPackets(new S_ServerMessage(286, String.valueOf(time))); // \f3ゲームに適合しない行動であるため、今後%0分間チャットを禁じます。
                pc.sendPackets(new S_ServerMessage(287, name)); // %0のチャットを禁じました。
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 " + cmdName
                    + " 玩家名称 时间(分)。"));
        }
    }
}
