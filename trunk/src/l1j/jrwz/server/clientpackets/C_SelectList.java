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

import java.util.logging.Logger;

import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.datatables.NpcTable;
import l1j.jrwz.server.datatables.PetTable;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1PcInventory;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.model.item.L1ItemId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.templates.L1Npc;
import l1j.jrwz.server.templates.L1Pet;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來選擇清單的封包
 */
public class C_SelectList extends ClientBasePacket {

    private static final String C_SELECT_LIST = "[C] C_SelectList";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(C_SelectList.class.getName());

    public C_SelectList(byte abyte0[], ClientThread clientthread) {
        super(abyte0);
        // アイテム毎にリクエストが来る。
        int itemObjectId = readD();
        int npcObjectId = readD();
        L1PcInstance pc = clientthread.getActiveChar();

        if (npcObjectId != 0) { // 武器的修理
            L1Object obj = L1World.getInstance().findObject(npcObjectId);
            if (obj != null) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    int difflocx = Math.abs(pc.getX() - npc.getX());
                    int difflocy = Math.abs(pc.getY() - npc.getY());
                    // 3格以上的距離視為無效請求
                    if (difflocx > 3 || difflocy > 3) {
                        return;
                    }
                }
            }

            L1PcInventory pcInventory = pc.getInventory();
            L1ItemInstance item = pcInventory.getItem(itemObjectId);
            int cost = item.get_durability() * 200;
            if (!pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
                return;
            }
            item.set_durability(0);
            pcInventory.updateItem(item, L1PcInventory.COL_DURABILITY);
        } else { // 領出寵物
            int petCost = 0;
            int petCount = 0;
            int divisor = 6;
            Object[] petList = pc.getPetList().values().toArray();
            for (Object pet : petList) {
                petCost += ((L1NpcInstance) pet).getPetcost();
            }
            int charisma = pc.getCha();
            if (pc.isCrown()) { // 王族
                charisma += 6;
            } else if (pc.isElf()) { // 妖精
                charisma += 12;
            } else if (pc.isWizard()) { // 法師
                charisma += 6;
            } else if (pc.isDarkelf()) { // 黑暗妖精
                charisma += 6;
            } else if (pc.isDragonKnight()) { // 龍騎士
                charisma += 6;
            } else if (pc.isIllusionist()) { // 幻術師
                charisma += 6;
            }

            L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
            if (l1pet != null) {
                int npcId = l1pet.get_npcid();
                charisma -= petCost;
                if (npcId == 45313 || npcId == 45710 // タイガー、バトルタイガー
                        || npcId == 45711 || npcId == 45712) { // 紀州犬の子犬、紀州犬
                    divisor = 12;
                } else {
                    divisor = 6;
                }
                petCount = charisma / divisor;
                if (petCount <= 0) {
                    pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$489)); // 引き取ろうとするペットが多すぎます。
                    return;
                }
                L1Npc npcTemp = NpcTable.getInstance().getTemplate(npcId);
                L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
                pet.setPetcost(divisor);
            }
        }
    }

    @Override
    public String getType() {
        return C_SELECT_LIST;
    }
}
