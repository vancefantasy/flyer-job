package com.flyer.job.client;

import com.flyer.job.common.ByteReadBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.*;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.Arrays;

/**
 * flyer client 业务处理handler
 * Created by jianying.li on 2018/1/31.
 */
public class ClientHandler
    implements IDataHandler, IConnectHandler, IIdleTimeoutHandler, IConnectionTimeoutHandler,
    IDisconnectHandler {

    private final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private FlyerService flyerService;

    public ClientHandler(FlyerService flyerService) {
        this.flyerService = flyerService;
    }

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
            head = nbc.readBytesByLength(20);
            ByteReadBuffer buffer = new ByteReadBuffer(head);

            buffer.readShort();
            packetLength = buffer.readInt();
            protocolId = buffer.readInt();
            protocolVersion = buffer.readShort();
            packetId = buffer.readLong();

            body = nbc.readBytesByLength(packetLength);

            if (protocolId != ProtocolConstants.PROTOCOL_ID_HEARTBEAT_RESPONSE) {
                log.debug(
                    "ClientHandler onData, packetLength: {}, protocolId: {}, packetId: {}, body: {}",
                    packetLength, protocolId, packetId, Arrays.toString(body));
            }

            //处理业务逻辑
            handleOnClient(protocolId, body, nbc, packetId);

            nbc.removeReadMark();
        } catch (BufferUnderflowException e) {
            nbc.resetToReadMark();
            return true;
        }
        return true;
    }

    private void handleOnClient(int protocolId, byte[] body, INonBlockingConnection nbc,
        long packetId) throws IOException {
        ByteReadBuffer buffer = new ByteReadBuffer(body);
        switch (protocolId) {
            case ProtocolConstants.PROTOCOL_ID_PULL_TOPOLOGY_RESPONSE://更新配置信息
                flyerService.pullTopology(body, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_SHAKEHAND_RESPONSE://处理握手返回
                flyerService.handleShake(buffer, nbc);
                break;
            case ProtocolConstants.PROTOCOL_ID_HEARTBEAT_RESPONSE://处理心跳
                flyerService.handleHeartBeat(buffer, nbc);
                break;
            case ProtocolConstants.PROTOCOL_ID_EXECUTE_JOB://处理任务执行
                flyerService.handleExecuteJob(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_REPORT_JOB_RESULT_RESPONSE://处理执行结果上报 返回
                flyerService.handleReportJobResult(buffer, nbc);
                break;
            case ProtocolConstants.PROTOCOL_ID_CANCEL_JOB://处理任务取消
                flyerService.handleCancelJob(buffer, nbc, packetId);
                break;
            case ProtocolConstants.PROTOCOL_ID_COMMIT_JOB_RESPONSE://处理提交外部任务响应
                flyerService.handleCommitJob(buffer, nbc);
                break;
            default:
                log.error("unrecognized protocolId : {}, body: {}", protocolId,
                    Arrays.toString(body));
                break;
        }
    }

    public boolean onConnect(INonBlockingConnection nbc)
        throws IOException, BufferUnderflowException {
        ConnectionHolder.addConnection(nbc);
        log.info("connection {} connect", nbc);
        return true;
    }

    @Override
    public boolean onIdleTimeout(INonBlockingConnection nbc) throws IOException {
        log.info("connection {} idleTimeout", nbc);
        return true;
    }

    @Override
    public boolean onConnectionTimeout(INonBlockingConnection nbc) throws IOException {
        log.info("connection {} connectionTimeout", nbc);
        return true;
    }

    @Override
    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
        ConnectionHolder.removeConnection(nbc);
        log.info("connection {} disconnect", nbc);
        return false;
    }

}
