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

import static l1j.jrwz.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static l1j.jrwz.server.model.skill.L1SkillId.SILENCE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;

import java.util.logging.Logger;

import l1j.jrwz.Config;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.GMCommands;
import l1j.jrwz.server.Opcodes;
import l1j.jrwz.server.datatables.ChatLogTable;
import l1j.jrwz.server.model.L1Clan;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1MonsterInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_ChatPacket;
import l1j.jrwz.server.serverpackets.S_NpcChatPacket;
import l1j.jrwz.server.serverpackets.S_PacketBox;
import l1j.jrwz.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來的聊天封包
 */
public class C_Chat extends ClientBasePacket {

    private static final String C_CHAT = "[C] C_Chat";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_Chat.class.getName());

    public C_Chat(byte abyte0[], ClientThread clientthread) {
        super(abyte0);

        L1PcInstance pc = clientthread.getActiveChar();
        int chatType = readC();
        String chatText = readS();
        if (pc.hasSkillEffect(SILENCE) || pc.hasSkillEffect(AREA_OF_SILENCE)
                || pc.hasSkillEffect(STATUS_POISON_SILENCE)) {
            return;
        }
        if (pc.hasSkillEffect(1005)) { // 被魔封
            pc.sendPackets(new S_ServerMessage(242)); // 你已被禁止聊天。
            return;
        }

        if (chatType == 0) { // 一般聊天
            if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
                return;
            }
            // GM指令
            if (chatText.startsWith(".") && (pc.isGm() || pc.isMonitor())) {
                String cmd = chatText.substring(1);
                GMCommands.getInstance().handleCommands(pc, cmd);
                return;
            }

            // 交易頻道
            // 本来はchatType==12になるはずだが、行頭の$が送信されない
            if (chatText.startsWith("$")) {
                String text = chatText.substring(1);
                chatWorld(pc, text, 12);
                if (!pc.isGm()) {
                    pc.checkChatInterval();
                }
                return;
            }

            ChatLogTable.getInstance().storeChat(pc, null, chatText, chatType);
            S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
                    Opcodes.S_OPCODE_NORMALCHAT, 0);
            if (!pc.getExcludingList().contains(pc.getName())) {
                pc.sendPackets(s_chatpacket);
            }
            for (L1PcInstance listner : L1World.getInstance()
                    .getRecognizePlayer(pc)) {
                if (!listner.getExcludingList().contains(pc.getName())) {
                    listner.sendPackets(s_chatpacket);
                }
            }
            // ドッペル処理
            for (L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) obj;
                    if (mob.getNpcTemplate().is_doppel()
                            && mob.getName().equals(pc.getName())) {
                        mob.broadcastPacket(new S_NpcChatPacket(mob, chatText,
                                0));
                    }
                }
            }
        } else if (chatType == 2) { // 喊叫
            if (pc.isGhost()) {
                return;
            }
            ChatLogTable.getInstance().storeChat(pc, null, chatText, chatType);
            S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
                    Opcodes.S_OPCODE_NORMALCHAT, 2);
            if (!pc.getExcludingList().contains(pc.getName())) {
                pc.sendPackets(s_chatpacket);
            }
            for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(
                    pc, 50)) {
                if (!listner.getExcludingList().contains(pc.getName())) {
                    listner.sendPackets(s_chatpacket);
                }
            }

            // ドッペル処理
            for (L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) obj;
                    if (mob.getNpcTemplate().is_doppel()
                            && mob.getName().equals(pc.getName())) {
                        for (L1PcInstance listner : L1World.getInstance()
                                .getVisiblePlayer(mob, 50)) {
                            listner.sendPackets(new S_NpcChatPacket(mob,
                                    chatText, 2));
                        }
                    }
                }
            }
        } else if (chatType == 3) { // 全體聊天
            chatWorld(pc, chatText, chatType);
        } else if (chatType == 4) { // 血盟聊天
            if (pc.getClanid() != 0) { // 所屬血盟
                L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
                int rank = pc.getClanRank();
                if (clan != null
                        && (rank == L1Clan.CLAN_RANK_PUBLIC
                                || rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_PRINCE)) {
                    ChatLogTable.getInstance().storeChat(pc, null, chatText,
                            chatType);
                    S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
                            Opcodes.S_OPCODE_GLOBALCHAT, 4);
                    L1PcInstance[] clanMembers = clan.getOnlineClanMember();
                    for (L1PcInstance listner : clanMembers) {
                        if (!listner.getExcludingList().contains(pc.getName())) {
                            listner.sendPackets(s_chatpacket);
                        }
                    }
                }
            }
        } else if (chatType == 11) { // 組隊聊天
            if (pc.isInParty()) { // 組隊中
                ChatLogTable.getInstance().storeChat(pc, null, chatText,
                        chatType);
                S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
                        Opcodes.S_OPCODE_GLOBALCHAT, 11);
                L1PcInstance[] partyMembers = pc.getParty().getMembers();
                for (L1PcInstance listner : partyMembers) {
                    if (!listner.getExcludingList().contains(pc.getName())) {
                        listner.sendPackets(s_chatpacket);
                    }
                }
            }
        } else if (chatType == 12) { // 交易聊天
            chatWorld(pc, chatText, chatType);
        } else if (chatType == 13) { // 連合血盟
            if (pc.getClanid() != 0) { // 在血盟中
                L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
                int rank = pc.getClanRank();
                if (clan != null
                        && (rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_PRINCE)) {
                    ChatLogTable.getInstance().storeChat(pc, null, chatText,
                            chatType);
                    S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
                            Opcodes.S_OPCODE_GLOBALCHAT, 13);
                    L1PcInstance[] clanMembers = clan.getOnlineClanMember();
                    for (L1PcInstance listner : clanMembers) {
                        int listnerRank = listner.getClanRank();
                        if (!listner.getExcludingList().contains(pc.getName())
                                && (listnerRank == L1Clan.CLAN_RANK_GUARDIAN || listnerRank == L1Clan.CLAN_RANK_PRINCE)) {
                            listner.sendPackets(s_chatpacket);
                        }
                    }
                }
            }
        } else if (chatType == 14) { // 聊天組隊
            if (pc.isInChatParty()) { // 聊天組隊
                ChatLogTable.getInstance().storeChat(pc, null, chatText,
                        chatType);
                S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText,
                        Opcodes.S_OPCODE_NORMALCHAT, 14);
                L1PcInstance[] partyMembers = pc.getChatParty().getMembers();
                for (L1PcInstance listner : partyMembers) {
                    if (!listner.getExcludingList().contains(pc.getName())) {
                        listner.sendPackets(s_chatpacket);
                    }
                }
            }
        }
        if (!pc.isGm()) {
            pc.checkChatInterval();
        }
    }

    private void chatWorld(L1PcInstance pc, String chatText, int chatType) {
        if (pc.isGm()) {
            ChatLogTable.getInstance().storeChat(pc, null, chatText, chatType);
            L1World.getInstance().broadcastPacketToAll(
                    new S_ChatPacket(pc, chatText, Opcodes.S_OPCODE_GLOBALCHAT,
                            chatType));
        } else if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
            if (L1World.getInstance().isWorldChatElabled()) {
                if (pc.get_food() >= 6) {
                    pc.set_food(pc.get_food() - 5);
                    ChatLogTable.getInstance().storeChat(pc, null, chatText,
                            chatType);
                    pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc
                            .get_food()));
                    for (L1PcInstance listner : L1World.getInstance()
                            .getAllPlayers()) {
                        if (!listner.getExcludingList().contains(pc.getName())) {
                            if (listner.isShowTradeChat() && chatType == 12) {
                                listner.sendPackets(new S_ChatPacket(pc,
                                        chatText, Opcodes.S_OPCODE_GLOBALCHAT,
                                        chatType));
                            } else if (listner.isShowWorldChat()
                                    && chatType == 3) {
                                listner.sendPackets(new S_ChatPacket(pc,
                                        chatText, Opcodes.S_OPCODE_GLOBALCHAT,
                                        chatType));
                            }
                        }
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(462)); // \f1你太过于饥饿以致于无法谈话。
                }
            } else {
                pc.sendPackets(new S_ServerMessage(510)); // 管理者有非常重要的事项公告，请见谅。
            }
        } else {
            pc.sendPackets(new S_ServerMessage(195, String
                    .valueOf(Config.GLOBAL_CHAT_LEVEL))); // 等級 %0 以下的角色無法使用公頻或買賣頻道。
        }
    }

    @Override
    public String getType() {
        return C_CHAT;
    }
}
