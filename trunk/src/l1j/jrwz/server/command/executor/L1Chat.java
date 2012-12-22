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

import java.util.StringTokenizer;

import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;

/**
 * GM指令：全體聊天
 */
public class L1Chat implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1Chat.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1Chat();
    }

    private L1Chat() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            if (st.hasMoreTokens()) {
                String flag = st.nextToken();
                String msg;
                if (flag.compareToIgnoreCase("on") == 0) {
                    L1World.getInstance().set_worldChatElabled(true);
                    msg = "开启全体聊天。";
                } else if (flag.compareToIgnoreCase("off") == 0) {
                    L1World.getInstance().set_worldChatElabled(false);
                    msg = "关闭全体聊天。";
                } else {
                    throw new Exception();
                }
                pc.sendPackets(new S_SystemMessage(msg));
            } else {
                String msg;
                if (L1World.getInstance().isWorldChatElabled()) {
                    msg = "全体聊天已开启。.chat off 能使其关闭。";
                } else {
                    msg = "全体聊天已关闭。.chat on 能使其开启。";
                }
                pc.sendPackets(new S_SystemMessage(msg));
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 " + cmdName + " [on|off]"));
        }
    }
}
