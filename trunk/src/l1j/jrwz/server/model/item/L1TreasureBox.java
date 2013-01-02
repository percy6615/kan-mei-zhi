package l1j.jrwz.server.model.item;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.L1Inventory;
import l1j.jrwz.server.model.L1World;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.identity.L1SystemMessageId;
import l1j.jrwz.server.serverpackets.S_ServerMessage;
import l1j.jrwz.server.utils.PerformanceTimer;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1TreasureBox {

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Item {
        @XmlAttribute(name = "ItemId")
        private int _itemId;

        @XmlAttribute(name = "Count")
        private int _count;

        private int _chance;

        public double getChance() {
            return _chance;
        }

        public int getCount() {
            return _count;
        }

        public int getItemId() {
            return _itemId;
        }

        @XmlAttribute(name = "Chance")
        private void setChance(double chance) {
            _chance = (int) (chance * 10000);
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "TreasureBoxList")
    private static class TreasureBoxList implements Iterable<L1TreasureBox> {
        @XmlElement(name = "TreasureBox")
        private List<L1TreasureBox> _list;

        @Override
        public Iterator<L1TreasureBox> iterator() {
            return _list.iterator();
        }
    }

    private static enum TYPE {
        RANDOM, SPECIFIC
    }

    private static Logger _log = Logger
            .getLogger(L1TreasureBox.class.getName());

    private static final String PATH = "./data/xml/Item/TreasureBox.xml";

    private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<Integer, L1TreasureBox>();

    /**
     * 指定されたIDのTreasureBoxを返す。
     * 
     * @param id
     *            - TreasureBoxのID。普通はアイテムのItemIdになる。
     * @return 指定されたIDのTreasureBox。見つからなかった場合はnull。
     */
    public static L1TreasureBox get(int id) {
        return _dataMap.get(id);
    }

    public static void load() {
        PerformanceTimer timer = new PerformanceTimer();
        System.out.print("loading TreasureBox...");
        try {
            JAXBContext context = JAXBContext
                    .newInstance(L1TreasureBox.TreasureBoxList.class);

            Unmarshaller um = context.createUnmarshaller();

            File file = new File(PATH);
            TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);

            for (L1TreasureBox each : list) {
                each.init();
                _dataMap.put(each.getBoxId(), each);
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, PATH + " 加载失败。", e);
            System.exit(0);
        }
        System.out.println("OK! " + timer.get() + "ms");
    }

    private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
        L1Inventory inventory;

        if (pc.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
            inventory = pc.getInventory();
        } else {
            // 持てない場合は地面に落とす 処理のキャンセルはしない（不正防止）
            inventory = L1World.getInstance().getInventory(pc.getLocation());
        }
        inventory.storeItem(item);
        pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$403, item.getLogName())); // %0を手に入れました。
    }

    @XmlAttribute(name = "ItemId")
    private int _boxId;

    @XmlAttribute(name = "Type")
    private TYPE _type;

    @XmlElement(name = "Item")
    private CopyOnWriteArrayList<Item> _items;

    private int _totalChance;

    private int getBoxId() {
        return _boxId;
    }

    private List<Item> getItems() {
        return _items;
    }

    private int getTotalChance() {
        return _totalChance;
    }

    private TYPE getType() {
        return _type;
    }

    private void init() {
        for (Item each : getItems()) {
            _totalChance += each.getChance();
            if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
                getItems().remove(each);
                _log.warning("对象ID " + each.getItemId() + " 无法找到对应的Template。");
            }
        }
        if (getTotalChance() != 0 && getTotalChance() != 1000000) {
            _log.warning("ID " + getBoxId() + " 的机率总合不等于100%。");
        }
    }

    /**
     * TreasureBoxを開けるPCにアイテムを入手させる。PCがアイテムを持ちきれなかった場合は
     * アイテムは地面に落ちる。
     * 
     * @param pc
     *            - TreasureBoxを開けるPC
     * @return 開封した結果何らかのアイテムが出てきた場合はtrueを返す。
     *         持ちきれず地面に落ちた場合もtrueになる。
     */
    public boolean open(L1PcInstance pc) {
        L1ItemInstance item = null;

        if (getType().equals(TYPE.SPECIFIC)) {
            // 出るアイテムが決まっているもの
            for (Item each : getItems()) {
                item = ItemTable.getInstance().createItem(each.getItemId());
                if (item != null) {
                    item.setCount(each.getCount());
                    storeItem(pc, item);
                }
            }

        } else if (getType().equals(TYPE.RANDOM)) {
            // 出るアイテムがランダムに決まるもの
            Random random = new Random();
            int chance = 0;

            int r = random.nextInt(getTotalChance());

            for (Item each : getItems()) {
                chance += each.getChance();

                if (r < chance) {
                    item = ItemTable.getInstance().createItem(each.getItemId());
                    if (item != null) {
                        item.setCount(each.getCount());
                        storeItem(pc, item);
                    }
                    break;
                }
            }
        }

        if (item == null) {
            return false;
        } else {
            int itemId = getBoxId();

            // 魂の結晶の破片、魔族のスクロール、ブラックエントの実
            if (itemId == 40576 || itemId == 40577 || itemId == 40578
                    || itemId == 40411 || itemId == 49013) {
                pc.death(null); // キャラクターを死亡させる
            }
            return true;
        }
    }
}
