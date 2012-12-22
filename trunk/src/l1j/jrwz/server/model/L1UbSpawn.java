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
package l1j.jrwz.server.model;

import java.util.logging.Logger;

import l1j.jrwz.server.IdFactory;
import l1j.jrwz.server.datatables.NpcTable;
import l1j.jrwz.server.datatables.UBTable;
import l1j.jrwz.server.model.Instance.L1MonsterInstance;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.serverpackets.S_NPCPack;

public class L1UbSpawn implements Comparable<L1UbSpawn> {
    private int _id;
    private int _ubId;
    private int _pattern;
    private int _group;
    private int _npcTemplateId;
    private int _amount;
    private int _spawnDelay;
    private int _sealCount;
    private String _name;

    private static final Logger _log = Logger.getLogger(L1UbSpawn.class
            .getName());

    @Override
    public int compareTo(L1UbSpawn rhs) {
        // XXX - 本当はもっと厳密な順序付けがあるはずだが、必要なさそうなので後回し
        if (getId() < rhs.getId()) {
            return -1;
        }
        if (getId() > rhs.getId()) {
            return 1;
        }
        return 0;
    }

    public int getAmount() {
        return _amount;
    }

    public int getGroup() {
        return _group;
    }

    // --------------------start getter/setter--------------------
    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getNpcTemplateId() {
        return _npcTemplateId;
    }

    public int getPattern() {
        return _pattern;
    }

    public int getSealCount() {
        return _sealCount;
    }

    public int getSpawnDelay() {
        return _spawnDelay;
    }

    public int getUbId() {
        return _ubId;
    }

    public void setAmount(int amount) {
        _amount = amount;
    }

    public void setGroup(int group) {
        _group = group;
    }

    public void setId(int id) {
        _id = id;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setNpcTemplateId(int npcTemplateId) {
        _npcTemplateId = npcTemplateId;
    }

    public void setPattern(int pattern) {
        _pattern = pattern;
    }

    public void setSealCount(int i) {
        _sealCount = i;
    }

    // --------------------end getter/setter--------------------

    public void setSpawnDelay(int spawnDelay) {
        _spawnDelay = spawnDelay;
    }

    public void setUbId(int ubId) {
        _ubId = ubId;
    }

    public void spawnAll() {
        for (int i = 0; i < getAmount(); i++) {
            spawnOne();
        }
    }

    public void spawnOne() {
        L1UltimateBattle ub = UBTable.getInstance().getUb(_ubId);
        L1Location loc = ub.getLocation().randomLocation(
                (ub.getLocX2() - ub.getLocX1()) / 2, false);
        L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance()
                .getTemplate(getNpcTemplateId()));
        if (mob == null) {
            _log.warning("mob == null");
            return;
        }

        mob.setId(IdFactory.getInstance().nextId());
        mob.setHeading(5);
        mob.setX(loc.getX());
        mob.setHomeX(loc.getX());
        mob.setY(loc.getY());
        mob.setHomeY(loc.getY());
        mob.setMap((short) loc.getMapId());
        mob.set_storeDroped(!(3 < getGroup()));
        mob.setUbSealCount(getSealCount());
        mob.setUbId(getUbId());

        L1World.getInstance().storeObject(mob);
        L1World.getInstance().addVisibleObject(mob);

        S_NPCPack s_npcPack = new S_NPCPack(mob);
        for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(mob)) {
            pc.addKnownObject(mob);
            mob.addKnownObject(pc);
            pc.sendPackets(s_npcPack);
        }
        // モンスターのＡＩを開始
        mob.onNpcAI();
        mob.turnOnOffLight();
        // mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット開始
    }
}
