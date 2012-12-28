package l1j.jrwz.server.serverpackets;

import java.io.IOException;

import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1TrapInstance;

public class S_Trap extends ServerBasePacket {
    public S_Trap(L1TrapInstance trap, String name) {

        writeC(Opcodes.S_OPCODE_DROPITEM);
        writeH(trap.getX());
        writeH(trap.getY());
        writeD(trap.getId());
        writeH(7); // adena
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
        writeD(0);
        writeC(0);
        writeC(0);
        writeS(name);
        writeC(0);
        writeD(0);
        writeD(0);
        writeC(255);
        writeC(0);
        writeC(0);
        writeC(0);
        writeH(65535);
        // writeD(0x401799a);
        writeD(0);
        writeC(8);
        writeC(0);
    }

    @Override
    public byte[] getContent() throws IOException {
        return getBytes();
    }
}
