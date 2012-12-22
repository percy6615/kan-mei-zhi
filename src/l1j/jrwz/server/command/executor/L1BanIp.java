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

import l1j.jrwz.server.datatables.IpTable;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_SystemMessage;

/**
 * GM指令：禁止登入
 */
public class L1BanIp implements L1CommandExecutor {
    // private static Logger _log = Logger.getLogger(L1BanIp.class.getName());

    public static L1CommandExecutor getInstance() {
        return new L1BanIp();
    }

    private L1BanIp() {
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer stringtokenizer = new StringTokenizer(arg);
            // IPを指定
            String s1 = stringtokenizer.nextToken();

            // add/delを指定(しなくてもOK)
            String s2 = null;
            try {
                s2 = stringtokenizer.nextToken();
            } catch (Exception e) {
            }

            IpTable iptable = IpTable.getInstance();
            boolean isBanned = iptable.isBannedIp(s1);

            for (L1PcInstance tg : L1World.getInstance().getAllPlayers()) {
                if (s1.equals(tg.getNetConnection().getIp())) {
                    String msg = new StringBuilder().append("IP: ").append(s1)
                            .append(" 连线中的角色名称: ").append(tg.getName())
                            .toString();
                    pc.sendPackets(new S_SystemMessage(msg));
                }
            }

            if ("add".equals(s2) && !isBanned) {
                iptable.banIp(s1); // BANリストへIPを加える
                String msg = new StringBuilder().append("IP: ").append(s1)
                        .append(" 被新增到封锁名单。").toString();
                pc.sendPackets(new S_SystemMessage(msg));
            } else if ("del".equals(s2) && isBanned) {
                if (iptable.liftBanIp(s1)) { // BANリストからIPを削除する
                    String msg = new StringBuilder().append("IP:").append(s1)
                            .append(" 已从封锁名单中删除。").toString();
                    pc.sendPackets(new S_SystemMessage(msg));
                }
            } else {
                // BANの確認
                if (isBanned) {
                    String msg = new StringBuilder().append("IP:").append(s1)
                            .append(" 已被登记在封锁名单中。").toString();
                    pc.sendPackets(new S_SystemMessage(msg));
                } else {
                    String msg = new StringBuilder().append("IP:").append(s1)
                            .append(" 尚未被登记在封锁名单中。").toString();
                    pc.sendPackets(new S_SystemMessage(msg));
                }
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("请输入 " + cmdName
                    + " IP [ add | del ]。"));
        }
    }
}
