package l1j.jrwz.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class BinaryInputStream extends InputStream {
    InputStream _in;

    public BinaryInputStream(InputStream in) {
        _in = in;
    }

    @Override
    public int available() throws IOException {
        return _in.available();
    }

    @Override
    public void close() throws IOException {
        _in.close();
    }

    @Override
    public int read() throws IOException {
        return _in.read();
    }

    public int readByte() throws IOException {
        return _in.read();
    }

    public int readInt() throws IOException {
        return readShort() | ((readShort() << 16) & 0xFFFF0000);
    }

    public int readShort() throws IOException {
        return _in.read() | ((_in.read() << 8) & 0xFF00);
    }

    @Override
    public long skip(long n) throws IOException {
        return _in.skip(n);
    }
}
