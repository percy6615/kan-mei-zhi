package l1j.jrwz.server.serverpackets;

import java.util.logging.Logger;

import l1j.jrwz.server.Opcodes;
import l1j.jrwz.server.model.Instance.L1MonsterInstance;

public class S_MoveNpcPacket extends ServerBasePacket {
    private static final String _S__1F_S_MOVENPCPACKET = "[S] S_MoveNpcPacket";
    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(S_MoveNpcPacket.class
            .getName());

    public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
        // npc.set_moving(true);

        writeC(Opcodes.S_OPCODE_MOVEOBJECT);
        writeD(npc.getId());
        writeH(x);
        writeH(y);
        writeC(heading);
        writeC(0x81);
        writeD(0x00000000);

        // npc.set_moving(false);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }

    @Override
    public String getType() {
        return _S__1F_S_MOVENPCPACKET;
    }
}
