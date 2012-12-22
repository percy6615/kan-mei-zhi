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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.datatables.CharacterTable;
import l1j.jrwz.server.datatables.MailTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_Mail;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.templates.L1Mail;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來信件的封包
 */
public class C_Mail extends ClientBasePacket {

    private static final String C_MAIL = "[C] C_Mail";

    private static Logger _log = Logger.getLogger(C_Mail.class.getName());

    private static int TYPE_NORMAL_MAIL = 0; // 一般
    private static int TYPE_CLAN_MAIL = 1; // 血盟
    private static int TYPE_MAIL_BOX = 2; // 保管箱

    public C_Mail(byte abyte0[], ClientThread client) {
        super(abyte0);
        int type = readC();
        L1PcInstance pc = client.getActiveChar();

        if (type == 0x00 || type == 0x01 || type == 0x02) { // 開啟
            pc.sendPackets(new S_Mail(pc.getName(), type));
        } else if (type == 0x10 || type == 0x11 || type == 0x12) { // 讀取
            int mailId = readD();
            MailTable.getInstance();
            L1Mail mail = MailTable.getMail(mailId);
            if (mail.getReadStatus() == 0) {
                MailTable.getInstance().setReadStatus(mailId);
            }
            pc.sendPackets(new S_Mail(mailId, type));
        } else if (type == 0x20) { // 一般信紙
            @SuppressWarnings("unused")
            int unknow = readH();
            String receiverName = readS();
            byte[] text = readByte();
            L1PcInstance receiver = L1World.getInstance().getPlayer(
                    receiverName);
            if (receiver != null) { // 對方在線上
                if (getMailSizeByReceiver(receiverName, TYPE_NORMAL_MAIL) >= 20) {
                    pc.sendPackets(new S_Mail(type));
                    return;
                }
                MailTable.getInstance().writeMail(TYPE_NORMAL_MAIL,
                        receiverName, pc, text);
                if (receiver.getOnlineStatus() == 1) {
                    receiver.sendPackets(new S_Mail(receiverName,
                            TYPE_NORMAL_MAIL));
                }
            } else { // 對方離線中
                try {
                    L1PcInstance restorePc = CharacterTable.getInstance()
                            .restoreCharacter(receiverName);
                    if (restorePc != null) {
                        if (getMailSizeByReceiver(receiverName,
                                TYPE_NORMAL_MAIL) >= 20) {
                            pc.sendPackets(new S_Mail(type));
                            return;
                        }
                        MailTable.getInstance().writeMail(TYPE_NORMAL_MAIL,
                                receiverName, pc, text);
                    } else {
                        pc.sendPackets(new S_ServerMessage(109, receiverName)); // 没有叫%0 的人。
                    }
                } catch (Exception e) {
                    _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }
        } else if (type == 0x21) { // 血盟信紙
            @SuppressWarnings("unused")
            int unknow = readH();
            String clanName = readS();
            byte[] text = readByte();
            L1Clan clan = L1World.getInstance().getClan(clanName);
            if (clan != null) {
                for (String name : clan.getAllMembers()) {
                    int size = getMailSizeByReceiver(name, TYPE_CLAN_MAIL);
                    if (size >= 50) {
                        continue;
                    }
                    MailTable.getInstance().writeMail(TYPE_CLAN_MAIL, name, pc,
                            text);
                    L1PcInstance clanPc = L1World.getInstance().getPlayer(name);
                    if (clanPc != null) { // 在線上
                        clanPc.sendPackets(new S_Mail(name, TYPE_CLAN_MAIL));
                    }
                }
            }
        } else if (type == 0x30 || type == 0x31 || type == 0x32) { // 刪除
            int mailId = readD();
            MailTable.getInstance().deleteMail(mailId);
            pc.sendPackets(new S_Mail(mailId, type));
        } else if (type == 0x40) { // 保管箱儲存
            int mailId = readD();
            MailTable.getInstance().setMailType(mailId, TYPE_MAIL_BOX);
            pc.sendPackets(new S_Mail(mailId, type));
        }
    }

    private int getMailSizeByReceiver(String receiverName, int type) {
        ArrayList<L1Mail> mails = new ArrayList<L1Mail>();
        MailTable.getInstance();
        for (L1Mail mail : MailTable.getAllMail()) {
            if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
                if (mail.getType() == type) {
                    mails.add(mail);
                }
            }
        }
        return mails.size();
    }

    @Override
    public String getType() {
        return C_MAIL;
    }
}
