package com.flyer.job.core;

import com.flyer.job.common.ByteReadBuffer;
import com.flyer.job.ProtocolConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.*;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.Arrays;

/**
 * flyer服务端协议处理handler
 * Created by jianying.li on 2018/1/30.
 */
public class ServerHandler
    implements IDataHandler, IConnectHandler, IIdleTimeoutHandler, IConnectionTimeoutHandler,
    IDisconnectHandler {

    private final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public boolean onData(INonBlockingConnection nbc) throws IOException, BufferUnderflowException {

        int packetLength = 0; //包长
        int protocolId = 0;//协议号
        int protocolVersion = 0; //协议版本
        long packetId = 0L;//包编号
        byte[] head = null; //包头
        byte[] body = null;//包体

        nbc.markReadPosition();
        try {
            //包头20字节
            head = nbc.readBytesByLength(20);
            ByteReadBuffer buffer = new ByteReadBuffer(head);
            buffer.readShort();
            packetLength = buffer.readInt();
            protocolId = buffer.readInt();
            protocolVersion = buffer.readShort();
            packetId = buffer.readLong();

            body = nbc.readBytesByLength(packetLength);

            if (protocolId != ProtocolConstants.PROTOCOL_ID_HEARTBEAT) {
                log.debug(
                    "ServerHandler onData, packetLength: {}, protocolId: {}, packetId: {}, body: {}",
                    packetLength, protocolId, packetId, Arrays.toString(body));
            }

            //处理业务逻辑
            handleOnServer(protocolId, body, nbc, packetId);

            nbc.removeReadMark();
        } catch (BufferUnderflowException e) {
            nbc.resetToReadMark();
            log.debug("BufferUnderflowException resetToReadMark");
        }
        return true;
    }

    private void handleOnServer(int protocolId, byte[] body, INonBlockingConnection nbc,
        long packetId) throws IOException {
        ByteReadBuffer buffer = new ByteReadBuffer(body);
        switch (protocolId) {
            case ProtocolConstants.PROTOCOL_ID_PULL_TOPOLOGY://拉取flyer server配置
                FlyerService.handlePullTopology(nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_SHAKEHAND://握手
                FlyerService.handleShake(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_HEARTBEAT://心跳
                FlyerService.handleHeartBeat(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_EXECUTE_JOB_RESPONSE://任务执行
                FlyerService.handleExecuteJob(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_REPORT_JOB_RESULT://执行结果上报 返回
                FlyerService.handleReportJobResult(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_CANCEL_JOB_RESPONSE://任务取消
                FlyerService.handleCancelJob(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_COMMIT_JOB://提交外部任务响应
                FlyerService.handleCommitJob(buffer, nbc, packetId);
                break;
            default:
                log.error("unrecognized protocolId : {}", protocolId);
                break;
        }
    }

    public boolean onConnect(INonBlockingConnection nbc)
        throws IOException, BufferUnderflowException {
        String remoteHost = nbc.getRemoteAddress().getHostAddress();
        log.info("Connection Client: {}, connection: {}", remoteHost, nbc);
        return true;
    }

    @Override
    public boolean onIdleTimeout(INonBlockingConnection nbc) throws IOException {
        log.info("Connection: {} idle timeout", nbc);
        return false;
    }

    @Override
    public boolean onConnectionTimeout(INonBlockingConnection nbc) throws IOException {
        log.info("Connection: {} connect timeout", nbc);
        return true;
    }

    @Override
    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
        log.info("Connection: {} disconnect", nbc);
        ConnectionHolder.removeConnection(nbc);
        return false;
    }

}
