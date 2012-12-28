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
package l1j.jrwz.server.datatables;

import static l1j.jrwz.server.codes.ActionCodes.ACTION_Attack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_AxeAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_AxeWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_BowAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_BowWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_ClawAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_ClawWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_DaggerAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_DaggerWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_EdoryuAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_EdoryuWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_SkillAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_SkillBuff;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_SpearAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_SpearWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_StaffAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_StaffWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_SwordAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_SwordWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_ThrowingKnifeAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_ThrowingKnifeWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_TwoHandSwordAttack;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_TwoHandSwordWalk;
import static l1j.jrwz.server.codes.ActionCodes.ACTION_Walk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.utils.SQLUtil;

public class SprTable {

    private static class Spr {
        private final HashMap<Integer, Integer> moveSpeed = new HashMap<Integer, Integer>();

        private final HashMap<Integer, Integer> attackSpeed = new HashMap<Integer, Integer>();

        private int nodirSpellSpeed = 1200;

        private int dirSpellSpeed = 1200;
    }

    private static Logger _log = Logger.getLogger(SprTable.class.getName());

    private static final HashMap<Integer, Spr> _dataMap = new HashMap<Integer, Spr>();

    private static final SprTable _instance = new SprTable();

    public static SprTable getInstance() {
        return _instance;
    }

    private SprTable() {
        loadSprAction();
    }

    /**
     * フレーム数とフレームレートからアクションの合計時間(ms)を計算して返す。
     */
    private int calcActionSpeed(int frameCount, int frameRate) {
        return (int) (frameCount * 40 * (24D / frameRate));
    }

    /**
     * 指定されたsprの攻撃速度を返す。もしsprに指定されたweapon_typeのデータが 設定されていない場合は、1.attackのデータを返す。
     * 
     * @param sprid
     *            -
     *            調べるsprのID
     * @param actid
     *            -
     *            武器の種類を表す値。L1Item.getType1()の返り値 + 1 と一致する
     * @return 指定されたsprの攻撃速度(ms)
     */
    public int getAttackSpeed(int sprid, int actid) {
        if (_dataMap.containsKey(sprid)) {
            if (_dataMap.get(sprid).attackSpeed.containsKey(actid)) {
                return _dataMap.get(sprid).attackSpeed.get(actid);
            } else if (actid == ACTION_Attack) {
                return 0;
            } else {
                return _dataMap.get(sprid).attackSpeed.get(ACTION_Attack);
            }
        }
        return 0;
    }

    public int getDirSpellSpeed(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid).dirSpellSpeed;
        }
        return 0;
    }

    public int getMoveSpeed(int sprid, int actid) {
        if (_dataMap.containsKey(sprid)) {
            if (_dataMap.get(sprid).moveSpeed.containsKey(actid)) {
                return _dataMap.get(sprid).moveSpeed.get(actid);
            } else if (actid == ACTION_Walk) {
                return 0;
            } else {
                return _dataMap.get(sprid).moveSpeed.get(ACTION_Walk);
            }
        }
        return 0;
    }

    public int getNodirSpellSpeed(int sprid) {
        if (_dataMap.containsKey(sprid)) {
            return _dataMap.get(sprid).nodirSpellSpeed;
        }
        return 0;
    }

    /**
     * spr_actionテーブルをロードする。
     */
    public void loadSprAction() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Spr spr = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM spr_action");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("spr_id");
                if (!_dataMap.containsKey(key)) {
                    spr = new Spr();
                    _dataMap.put(key, spr);
                } else {
                    spr = _dataMap.get(key);
                }

                int actid = rs.getInt("act_id");
                int frameCount = rs.getInt("framecount");
                int frameRate = rs.getInt("framerate");
                int speed = calcActionSpeed(frameCount, frameRate);

                switch (actid) {
                    case ACTION_Walk:
                    case ACTION_SwordWalk:
                    case ACTION_AxeWalk:
                    case ACTION_BowWalk:
                    case ACTION_SpearWalk:
                    case ACTION_StaffWalk:
                    case ACTION_DaggerWalk:
                    case ACTION_TwoHandSwordWalk:
                    case ACTION_EdoryuWalk:
                    case ACTION_ClawWalk:
                    case ACTION_ThrowingKnifeWalk:
                        spr.moveSpeed.put(actid, speed);
                        break;
                    case ACTION_SkillAttack:
                        spr.dirSpellSpeed = speed;
                        break;
                    case ACTION_SkillBuff:
                        spr.nodirSpellSpeed = speed;
                        break;
                    case ACTION_Attack:
                    case ACTION_SwordAttack:
                    case ACTION_AxeAttack:
                    case ACTION_BowAttack:
                    case ACTION_SpearAttack:
                    case ACTION_StaffAttack:
                    case ACTION_DaggerAttack:
                    case ACTION_TwoHandSwordAttack:
                    case ACTION_EdoryuAttack:
                    case ACTION_ClawAttack:
                    case ACTION_ThrowingKnifeAttack:
                        spr.attackSpeed.put(actid, speed);
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.config("SPRデータ " + _dataMap.size() + "件ロード");
    }
}
