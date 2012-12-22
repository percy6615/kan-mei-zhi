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

package l1j.jrwz.server.serverpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.jrwz.server.serverpackets:
// ServerBasePacket

public class S_Letter extends ServerBasePacket {

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_Letter.class.getName());
    private static final String S_LETTER = "[S] S_Letter";
    private byte[] _byte = null;

    public S_Letter(L1ItemInstance item) {
        buildPacket(item);
    }

    private void buildPacket(L1ItemInstance item) {
        /*
         * Connection con = null; PreparedStatement pstm = null; ResultSet rs = null;
         * try { con = L1DatabaseFactory.getInstance().getConnection(); pstm = con
         * .prepareStatement("SELECT * FROM letter WHERE item_object_id=?");
         * pstm.setInt(1, item.getId()); rs = pstm.executeQuery(); while (rs.next()) {
         * writeC(Opcodes.S_OPCODE_LETTER); writeD(item.getId()); if (item.get_gfxid() ==
         * 465) { // 開く前 writeH(466); // 開いた後 } else if (item.get_gfxid() == 606) {
         * writeH(605); } else if (item.get_gfxid() == 616) { writeH(615); } else {
         * writeH(item.get_gfxid()); } writeH(rs.getInt(2)); writeS(rs.getString(3));
         * writeS(rs.getString(4)); writeByte(rs.getBytes(7));
         * writeByte(rs.getBytes(8)); writeC(rs.getInt(6)); // テンプレ
         * writeS(rs.getString(5)); // 日付 } } catch (SQLException e) {
         * _log.log(Level.SEVERE, e.getLocalizedMessage(), e); } finally {
         * SQLUtil.close(rs); SQLUtil.close(pstm); SQLUtil.close(con); }
         */
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return S_LETTER;
    }
}
