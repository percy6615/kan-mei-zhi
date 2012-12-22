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

import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_0_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_1_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_1_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_2_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_2_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_3_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_3_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_4_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_4_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_5_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_5_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_6_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_0_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_0_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_1_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_1_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_2_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_2_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_3_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_3_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_4_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_4_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_5_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_5_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_6_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_2_6_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_0_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_0_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_1_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_1_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_2_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_2_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_3_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_3_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_4_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_4_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_5_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_5_S;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_6_N;
import static l1j.jrwz.server.model.skill.L1SkillId.COOKING_3_6_S;
import static l1j.jrwz.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.jrwz.server.model.skill.L1SkillId.STATUS_ELFBRAVE;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.utils.SQLUtil;

public class CharBuffTable {
    private static Logger _log = Logger
            .getLogger(CharBuffTable.class.getName());

    private static final int[] buffSkill = {
            2,
            67, // ライト、シェイプチェンジ
            3,
            99,
            151,
            159,
            168, // シールド、シャドウアーマー、アーススキン、アースブレス、アイアンスキン
            43,
            54,
            1000,
            1001,
            STATUS_ELFBRAVE, // ヘイスト、グレーターヘイスト、ブレイブポーション、グリーンポーション、エルヴンワッフル
            52,
            101,
            150, // ホーリーウォーク、ムービングアクセレーション、ウィンドウォーク
            26,
            42,
            109,
            110, // PE:DEX、PE:STR、ドレスマイティー、ドレスデクスタリティー
            114,
            115,
            117, // グローウィングオーラ、シャイニングオーラ、ブレイブオーラ
            148,
            155,
            163, // ファイアーウェポン、ファイアーブレス、バーニングウェポン
            149,
            156,
            166, // ウィンドショット、ストームアイ、ストームショット
            1002,
            1005, // ブルーポーション、チャット禁止
            COOKING_1_0_N,
            COOKING_1_0_S,
            COOKING_1_1_N,
            COOKING_1_1_S, // 料理(デザートは除く)
            COOKING_1_2_N, COOKING_1_2_S, COOKING_1_3_N, COOKING_1_3_S,
            COOKING_1_4_N, COOKING_1_4_S, COOKING_1_5_N, COOKING_1_5_S,
            COOKING_1_6_N, COOKING_1_6_S, COOKING_2_0_N, COOKING_2_0_S,
            COOKING_2_1_N, COOKING_2_1_S, COOKING_2_2_N, COOKING_2_2_S,
            COOKING_2_3_N, COOKING_2_3_S, COOKING_2_4_N, COOKING_2_4_S,
            COOKING_2_5_N, COOKING_2_5_S, COOKING_2_6_N, COOKING_2_6_S,
            COOKING_3_0_N, COOKING_3_0_S, COOKING_3_1_N, COOKING_3_1_S,
            COOKING_3_2_N, COOKING_3_2_S, COOKING_3_3_N, COOKING_3_3_S,
            COOKING_3_4_N, COOKING_3_4_S, COOKING_3_5_N, COOKING_3_5_S,
            COOKING_3_6_N, COOKING_3_6_S };

    public static void DeleteBuff(L1PcInstance pc) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
            pstm.setInt(1, pc.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);

        }
    }

    public static void SaveBuff(L1PcInstance pc) {
        for (int skillId : buffSkill) {
            int timeSec = pc.getSkillEffectTimeSec(skillId);
            if (0 < timeSec) {
                int polyId = 0;
                if (skillId == SHAPE_CHANGE) {
                    polyId = pc.getTempCharGfx();
                }
                StoreBuff(pc.getId(), skillId, timeSec, polyId);
            }
        }
    }

    private static void StoreBuff(int objId, int skillId, int time, int polyId) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?");
            pstm.setInt(1, objId);
            pstm.setInt(2, skillId);
            pstm.setInt(3, time);
            pstm.setInt(4, polyId);
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private CharBuffTable() {
    }

}
