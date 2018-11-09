package com.flyer.job.common;

import com.flyer.job.ProtocolConstants;
import org.xsocket.connection.INonBlockingConnection;

/**
 * 内部工具类
 */
public class Utils {

    public static int getLenth(String str) {
        return str.getBytes().length + 2;
    }

    public static byte[] wrapHeader(byte[] body, int protocolId, long packetId) {
        //head(20) + body(?)
        int bodyLength = body.length;
        ByteWriteBuffer bw = new ByteWriteBuffer(20 + bodyLength);
        bw.writeShort((short) 1);
        bw.writeInt(bodyLength);
        bw.writeInt(protocolId);
        bw.writeShort(ProtocolConstants.CURRENT_PROTOCOL_VERSION);
        bw.writeLong(packetId);
        bw.writeByteArray(body);
        return bw.getData();
    }

    public static String getServerStr(INonBlockingConnection nbc) {
        return nbc.getRemoteAddress().getHostAddress() + ":" + nbc.getRemotePort();
    }

    public static String getClientStr(INonBlockingConnection nbc) {
        return nbc.getRemoteAddress().getHostAddress() + ":" + nbc.getRemotePort();
    }
}
