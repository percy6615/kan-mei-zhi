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
package l1j.jrwz.server.model.npc.action;

import java.util.ArrayList;
import java.util.List;

import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.L1ObjectAmount;
import l1j.jrwz.server.model.L1PcInventory;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.model.npc.L1NpcHtml;
import l1j.jrwz.server.serverpackets.S_HowManyMake;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.templates.L1Item;
import l1j.jrwz.server.utils.IterableElementList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class L1NpcMakeItemAction extends L1NpcXmlAction {
    private final List<L1ObjectAmount<Integer>> _materials = new ArrayList<L1ObjectAmount<Integer>>();
    private final List<L1ObjectAmount<Integer>> _items = new ArrayList<L1ObjectAmount<Integer>>();
    private final boolean _isAmountInputable;
    private final L1NpcAction _actionOnSucceed;
    private final L1NpcAction _actionOnFail;

    public L1NpcMakeItemAction(Element element) {
        super(element);

        _isAmountInputable = L1NpcXmlParser.getBoolAttribute(element,
                "AmountInputable", true);
        NodeList list = element.getChildNodes();
        for (Element elem : new IterableElementList(list)) {
            if (elem.getNodeName().equalsIgnoreCase("Material")) {
                int id = Integer.valueOf(elem.getAttribute("ItemId"));
                int amount = Integer.valueOf(elem.getAttribute("Amount"));
                _materials.add(new L1ObjectAmount<Integer>(id, amount));
                continue;
            }
            if (elem.getNodeName().equalsIgnoreCase("Item")) {
                int id = Integer.valueOf(elem.getAttribute("ItemId"));
                int amount = Integer.valueOf(elem.getAttribute("Amount"));
                _items.add(new L1ObjectAmount<Integer>(id, amount));
                continue;
            }
        }

        if (_items.isEmpty() || _materials.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Element elem = L1NpcXmlParser.getFirstChildElementByTagName(element,
                "Succeed");
        _actionOnSucceed = elem == null ? null : new L1NpcListedAction(elem);
        elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
        _actionOnFail = elem == null ? null : new L1NpcListedAction(elem);
    }

    /**
     * 指定されたインベントリ内に、素材が何セットあるか数える
     */
    private int countNumOfMaterials(L1PcInventory inv) {
        int count = Integer.MAX_VALUE;
        for (L1ObjectAmount<Integer> material : _materials) {
            int numOfSet = inv.countItems(material.getObject())
                    / material.getAmount();
            count = Math.min(count, numOfSet);
        }
        return count;
    }

    @Override
    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj,
            byte[] args) {
        int numOfMaterials = countNumOfMaterials(pc.getInventory());
        if (1 < numOfMaterials && _isAmountInputable) {
            pc.sendPackets(new S_HowManyMake(obj.getId(), numOfMaterials,
                    actionName));
            return null;
        }
        return executeWithAmount(actionName, pc, obj, 1);
    }

    @Override
    public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc,
            L1Object obj, int amount) {
        L1NpcInstance npc = (L1NpcInstance) obj;
        L1NpcHtml result = null;
        if (makeItems(pc, npc.getNpcTemplate().get_name(), amount)) {
            if (_actionOnSucceed != null) {
                result = _actionOnSucceed.execute(actionName, pc, obj,
                        new byte[0]);
            }
        } else {
            if (_actionOnFail != null) {
                result = _actionOnFail
                        .execute(actionName, pc, obj, new byte[0]);
            }
        }
        return result == null ? L1NpcHtml.HTML_CLOSE : result;
    }

    private boolean makeItems(L1PcInstance pc, String npcName, int amount) {
        if (amount <= 0) {
            return false;
        }

        boolean isEnoughMaterials = true;
        for (L1ObjectAmount<Integer> material : _materials) {
            if (!pc.getInventory().checkItemNotEquipped(material.getObject(),
                    material.getAmount() * amount)) {
                L1Item temp = ItemTable.getInstance().getTemplate(
                        material.getObject());
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$337, temp.getName()
                        + "("
                        + ((material.getAmount() * amount) - pc.getInventory()
                                .countItems(temp.getItemId())) + ")")); // \f1%0が不足しています。
                isEnoughMaterials = false;
            }
        }
        if (!isEnoughMaterials) {
            return false;
        }

        // 容量と重量の計算
        int countToCreate = 0; // アイテムの個数（纏まる物は1個）
        int weight = 0;

        for (L1ObjectAmount<Integer> makingItem : _items) {
            L1Item temp = ItemTable.getInstance().getTemplate(
                    makingItem.getObject());
            if (temp.isStackable()) {
                if (!pc.getInventory().checkItem(makingItem.getObject())) {
                    countToCreate += 1;
                }
            } else {
                countToCreate += makingItem.getAmount() * amount;
            }
            weight += temp.getWeight() * (makingItem.getAmount() * amount)
                    / 1000;
        }
        // 容量確認
        if (pc.getInventory().getSize() + countToCreate > 180) {
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$263)); // \f1一人のキャラクターが持って歩けるアイテムは最大180個までです。
            return false;
        }
        // 重量確認
        if (pc.getMaxWeight() < pc.getInventory().getWeight() + weight) {
            pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$82)); // アイテムが重すぎて、これ以上持てません。
            return false;
        }

        for (L1ObjectAmount<Integer> material : _materials) {
            // 材料消費
            pc.getInventory().consumeItem(material.getObject(),
                    material.getAmount() * amount);
        }

        for (L1ObjectAmount<Integer> makingItem : _items) {
            L1ItemInstance item = pc.getInventory().storeItem(
                    makingItem.getObject(), makingItem.getAmount() * amount);
            if (item != null) {
                String itemName = ItemTable.getInstance()
                        .getTemplate(makingItem.getObject()).getName();
                if (makingItem.getAmount() * amount > 1) {
                    itemName = itemName + " (" + makingItem.getAmount()
                            * amount + ")";
                }
                pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$143, npcName, itemName)); // \f1%0が%1をくれました。
            }
        }
        return true;
    }

}
