package l1j.jrwz.server.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.Config;
import l1j.jrwz.server.datatables.ItemTable;
import l1j.jrwz.server.model.Instance.L1ItemInstance;
import l1j.jrwz.server.model.map.L1Map;
import l1j.jrwz.server.model.map.L1WorldMap;
import l1j.jrwz.server.types.Point;

/**
 * 生成精灵元素石
 */
public class ElementalStoneGenerator implements Runnable {

    private static Logger _log = Logger.getLogger(ElementalStoneGenerator.class
            .getName());

    private static final int ELVEN_FOREST_MAPID = 4;
    private static final int MAX_COUNT = Config.ELEMENTAL_STONE_AMOUNT; // 设置个数
    private static final int INTERVAL = 3; // 设置间隔 (秒)
    private static final int SLEEP_TIME = 300; // 设置终了后、再设置的睡眠时间 (秒)
    private static final int FIRST_X = 32911;
    private static final int FIRST_Y = 32210;
    private static final int LAST_X = 33141;
    private static final int LAST_Y = 32500;
    private static final int ELEMENTAL_STONE_ID = 40515; // 元素石

    public static ElementalStoneGenerator getInstance() {
        if (_instance == null) {
            _instance = new ElementalStoneGenerator();
        }
        return _instance;
    }

    private final ArrayList<L1GroundInventory> _itemList = new ArrayList<L1GroundInventory>(
            MAX_COUNT);

    private final Random _random = new Random();

    private static ElementalStoneGenerator _instance = null;

    private final L1Object _dummy = new L1Object();

    private ElementalStoneGenerator() {
    }

    /**
     * 返回在指定地点放置精灵石。
     */
    private boolean canPut(L1Location loc) {
        _dummy.setMap(loc.getMap());
        _dummy.setX(loc.getX());
        _dummy.setY(loc.getY());

        // 检查玩家的可视范围
        if (L1World.getInstance().getVisiblePlayer(_dummy).size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 设置下一个放置点。
     */
    private Point nextPoint() {
        int newX = _random.nextInt(LAST_X - FIRST_X) + FIRST_X;
        int newY = _random.nextInt(LAST_Y - FIRST_Y) + FIRST_Y;

        return new Point(newX, newY);
    }

    /**
     * 把石头放入指定地点。
     */
    private void putElementalStone(L1Location loc) {
        L1GroundInventory gInventory = L1World.getInstance().getInventory(loc);

        L1ItemInstance item = ItemTable.getInstance().createItem(
                ELEMENTAL_STONE_ID);
        item.setEnchantLevel(0);
        item.setCount(1);
        gInventory.storeItem(item);
        _itemList.add(gInventory);
    }

    /**
     * 捡起石头从名单中删除。
     */
    private void removeItemsPickedUp() {
        for (int i = 0; i < _itemList.size(); i++) {
            L1GroundInventory gInventory = _itemList.get(i);
            if (!gInventory.checkItem(ELEMENTAL_STONE_ID)) {
                _itemList.remove(i);
                i--;
            }
        }
    }

    @Override
    public void run() {
        try {
            L1Map map = L1WorldMap.getInstance().getMap(
                    (short) ELVEN_FOREST_MAPID);
            while (true) {
                removeItemsPickedUp();

                while (_itemList.size() < MAX_COUNT) { // 减少的情况
                    L1Location loc = new L1Location(nextPoint(), map);

                    if (!canPut(loc)) {
                        // XXX 设置范围内全てにPCが居た场合无限循环…
                        continue;
                    }

                    putElementalStone(loc);

                    Thread.sleep(INTERVAL * 1000); // 设置时间间隔
                }
                Thread.sleep(SLEEP_TIME * 1000); // maxまで設置終了後一定時間は再設置しない
            }
        } catch (Throwable e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
}
