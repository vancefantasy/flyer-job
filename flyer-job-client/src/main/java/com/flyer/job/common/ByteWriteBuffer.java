package com.flyer.job.common;

import java.io.UnsupportedEncodingException;

public class ByteWriteBuffer {
    private byte[] data;
    private int pos;

    public ByteWriteBuffer(int length) {
        this.pos = 0;
        this.data = new byte[length];
    }

    public ByteWriteBuffer(byte[] data, int index) {
        this.data = data;
        this.pos = index;
    }

    public void writeByte(byte value) {
        this.data[(this.pos++)] = value;
    }

    public void writeBytes(int value, int length) {
        for (int i = 0; i < length; i++)
            this.data[(this.pos++)] = (byte) (value >> i * 8);
    }

    public void writeShort(short value) {
        this.data[(this.pos++)] = (byte) (value >> 0);
        this.data[(this.pos++)] = (byte) (value >> 8);
    }

    public void writeByteArray(byte[] data) {
        for (byte b : data)
            this.data[(this.pos++)] = b;
    }

    public void writeInt(int value) {
        this.data[(this.pos++)] = (byte) (value >> 0);
        this.data[(this.pos++)] = (byte) (value >> 8);
        this.data[(this.pos++)] = (byte) (value >> 16);
        this.data[(this.pos++)] = (byte) (value >> 24);
    }

    public void writeLong(long value) {
        this.data[(this.pos++)] = (byte) (int) (value >> 0);
        this.data[(this.pos++)] = (byte) (int) (value >> 8);
        this.data[(this.pos++)] = (byte) (int) (value >> 16);
        this.data[(this.pos++)] = (byte) (int) (value >> 24);
        this.data[(this.pos++)] = (byte) (int) (value >> 32);
        this.data[(this.pos++)] = (byte) (int) (value >> 40);
        this.data[(this.pos++)] = (byte) (int) (value >> 48);
        this.data[(this.pos++)] = (byte) (int) (value >> 56);
    }

    public void writeString(String value) {
        byte[] str = value.getBytes();
        int length = str.length;
        writeShort((short) length);

        for (byte s : str)
            this.data[(this.pos++)] = s;
    }

    public void writeString(String value, String charset) throws UnsupportedEncodingException {
        byte[] str = value.getBytes(charset);
        int length = str.length;
        writeShort((short) length);
        for (byte s : str)
            this.data[(this.pos++)] = s;
    }

    public byte[] getData() {
        byte[] result = new byte[this.pos];
        System.arraycopy(this.data, 0, result, 0, this.pos);
        return result;
    }
}
