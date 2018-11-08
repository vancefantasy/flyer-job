package com.flyer.job.common;

import java.io.UnsupportedEncodingException;

public class ByteReadBuffer {
    private byte[] data;
    private int pos;
    private int length;

    public ByteReadBuffer(byte[] data) {
        this.data = data;
        this.pos = 0;
        this.length = data.length;
    }

    public ByteReadBuffer(byte[] data, int offset) {
        this.data = data;
        this.pos = offset;
        this.length = data.length;
    }

    public int readByte() {
        return this.data[(this.pos++)] & 0xFF;
    }

    public char readChar() {
        return (char) readShort();
    }

    public int readShort() {
        byte s1 = this.data[(this.pos++)];
        byte s2 = this.data[(this.pos++)];
        return (s1 & 0xFF) + ((s2 & 0xFF) << 8);
    }

    public int read3Bytes() {
        byte i1 = this.data[(this.pos++)];
        byte i2 = this.data[(this.pos++)];
        byte i3 = this.data[(this.pos++)];
        return (i1 & 0xFF) + ((i2 & 0xFF) << 8) + ((i3 & 0xFF) << 16);
    }

    public int readInt() {
        byte i1 = this.data[(this.pos++)];
        byte i2 = this.data[(this.pos++)];
        byte i3 = this.data[(this.pos++)];
        byte i4 = this.data[(this.pos++)];
        return (i1 & 0xFF) + ((i2 & 0xFF) << 8) + ((i3 & 0xFF) << 16) + ((i4 & 0xFF) << 24);
    }

    public long readLong() {
        byte a1 = this.data[(this.pos++)];
        byte a2 = this.data[(this.pos++)];
        byte a3 = this.data[(this.pos++)];
        byte a4 = this.data[(this.pos++)];
        byte a5 = this.data[(this.pos++)];
        byte a6 = this.data[(this.pos++)];
        byte a7 = this.data[(this.pos++)];
        byte a8 = this.data[(this.pos++)];

        long b1 = a1 & 0xFF;
        long b2 = a2 & 0xFF;
        long b3 = a3 & 0xFF;
        long b4 = a4 & 0xFF;
        long b5 = a5 & 0xFF;
        long b6 = a6 & 0xFF;
        long b7 = a7 & 0xFF;
        long b8 = a8 & 0xFF;
        long value =
            (b1 << 0) + (b2 << 8) + (b3 << 16) + (b4 << 24) + (b5 << 32) + (b6 << 40) + (b7 << 48)
                + (b8 << 56);

        return value;
    }

    public String readString() {
        int length = readShort();
        byte[] str = new byte[length];
        for (int i = 0; i < length; i++) {
            str[i] = this.data[(this.pos++)];
        }
        return new String(str);
    }

    public String readGBKString() {
        String result = null;
        try {
            result = readString("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readString(String charCode) throws UnsupportedEncodingException {
        int length = readShort();
        byte[] str = new byte[length];
        for (int i = 0; i < length; i++) {
            str[i] = this.data[(this.pos++)];
        }
        return new String(str, charCode);
    }

    public String readString4ByteLen(String charCode) throws UnsupportedEncodingException {
        int length = readInt();
        byte[] str = new byte[length];
        for (int i = 0; i < length; i++) {
            str[i] = this.data[(this.pos++)];
        }
        return new String(str, charCode);
    }

    public void clear() {
        this.data = null;
        this.pos = 0;
        this.length = 0;
    }

    public byte[] getData() {
        return this.data;
    }
}
