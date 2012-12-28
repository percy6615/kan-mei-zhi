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

import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1NpcInstance;
import l1j.jrwz.server.model.Instance.L1PetInstance;
import l1j.jrwz.server.model.Instance.L1SummonInstance;

public class S_PetMenuPacket extends ServerBasePacket {

    private byte[] _byte = null;

    public S_PetMenuPacket(L1NpcInstance npc, int exppercet) {
        buildpacket(npc, exppercet);
    }

    private void buildpacket(L1NpcInstance npc, int exppercet) {
        writeC(Opcodes.S_OPCODE_SHOWHTML);

        if (npc instanceof L1PetInstance) { // ペット
            L1PetInstance pet = (L1PetInstance) npc;
            writeD(pet.getId());
            writeS("anicom");
            writeC(0x00);
            writeH(10);
            switch (pet.getCurrentPetStatus()) {
                case 1:
                    writeS("$469"); // 攻撃態勢
                    break;
                case 2:
                    writeS("$470"); // 防御態勢
                    break;
                case 3:
                    writeS("$471"); // 休憩
                    break;
                case 5:
                    writeS("$472"); // 警戒
                    break;
                default:
                    writeS("$471"); // 休憩
                    break;
            }
            writeS(Integer.toString(pet.getCurrentHp())); // 現在のＨＰ
            writeS(Integer.toString(pet.getMaxHp())); // 最大ＨＰ
            writeS(Integer.toString(pet.getCurrentMp())); // 現在のＭＰ
            writeS(Integer.toString(pet.getMaxMp())); // 最大ＭＰ
            writeS(Integer.toString(pet.getLevel())); // レベル

            // 名前の文字数が8を超えると落ちる
            // なぜか"セント バーナード","ブレイブ ラビット"はOK
            // String pet_name = pet.get_name();
            // if (pet_name.equalsIgnoreCase("ハイ ドーベルマン")) {
            // pet_name = "ハイ ドーベルマ";
            // }
            // else if (pet_name.equalsIgnoreCase("ハイ セントバーナード")) {
            // pet_name = "ハイ セントバー";
            // }
            // writeS(pet_name);
            writeS(""); // ペットの名前を表示させると不安定になるので、非表示にする
            writeS("$611"); // お腹いっぱい
            writeS(Integer.toString(exppercet)); // 経験値
            writeS(Integer.toString(pet.getLawful())); // アライメント
        } else if (npc instanceof L1SummonInstance) { // サモンモンスター
            L1SummonInstance summon = (L1SummonInstance) npc;
            writeD(summon.getId());
            writeS("moncom");
            writeC(0x00);
            writeH(6); // 渡す引数文字の数の模様
            switch (summon.get_currentPetStatus()) {
                case 1:
                    writeS("$469"); // 攻撃態勢
                    break;
                case 2:
                    writeS("$470"); // 防御態勢
                    break;
                case 3:
                    writeS("$471"); // 休憩
                    break;
                case 5:
                    writeS("$472"); // 警戒
                    break;
                default:
                    writeS("$471"); // 休憩
                    break;
            }
            writeS(Integer.toString(summon.getCurrentHp())); // 現在のＨＰ
            writeS(Integer.toString(summon.getMaxHp())); // 最大ＨＰ
            writeS(Integer.toString(summon.getCurrentMp())); // 現在のＭＰ
            writeS(Integer.toString(summon.getMaxMp())); // 最大ＭＰ
            writeS(Integer.toString(summon.getLevel())); // レベル
            // writeS(summon.getNpcTemplate().get_nameid());
            // writeS(Integer.toString(0));
            // writeS(Integer.toString(790));
        }
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = _bao.toByteArray();
        }

        return _byte;
    }
}
