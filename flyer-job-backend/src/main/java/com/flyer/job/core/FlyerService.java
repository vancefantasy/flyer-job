package com.flyer.job.core;

import com.flyer.job.common.BeanFactoryRegister;
import com.flyer.job.common.ByteReadBuffer;
import com.flyer.job.common.ByteWriteBuffer;
import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.ProtocolConstants;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.domain.ServerConfig;
import com.flyer.job.service.ClusterService;
import com.flyer.job.common.utils.JobLogUtils;
import com.flyer.job.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.util.List;

/**
 * Created by jianying.li on 2018/2/5.
 */
public class FlyerService {

    private final static Logger log = LoggerFactory.getLogger(FlyerService.class);

    public static void handleShake(ByteReadBuffer buffer, INonBlockingConnection nbc, long packetId)
        throws IOException {
        String appCode = buffer.readString();
        String vhost = buffer.readString();
        int req = buffer.readByte();
        if (req != 0) {
            log.error("handleShake error connection : {}", nbc);
        } else {
            //维护connection
            ConnectionHolder.addConnection(nbc, appCode, vhost);
            byte[] packet = Utils
                .wrapHeader(new byte[] {1}, ProtocolConstants.PROTOCOL_ID_SHAKEHAND_RESPONSE,
                    packetId);
            nbc.write(packet);
            nbc.flush();
        }
    }

    public static void handleHeartBeat(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) throws IOException {
        String appCode = buffer.readString();
        String vhost = buffer.readString();
        int req = buffer.readByte();
        if (req != 0) {
            log.error("handleHeartBeat error, connection: {}", nbc);
        } else {
            //更新connection
            ConnectionHolder.refreshConnection(nbc, appCode, vhost);

            ClusterService settingsService = BeanFactoryRegister.getBean(ClusterService.class);
            ServerConfig serverConfig = settingsService.getCurrentConfig();
            ByteWriteBuffer writeBuffer = new ByteWriteBuffer(1 + 4);
            writeBuffer.writeByte((byte) 1);
            writeBuffer.writeInt(serverConfig.getVersion());
            byte[] packet = Utils
                .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_HEARTBEAT_RESPONSE,
                    packetId);
            nbc.write(packet);
            nbc.flush();
        }
    }

    public static void handleExecuteJob(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) {
        String jobBeanId = buffer.readString();
        int result = buffer.readByte();

        log.info("handleExecuteJob jobBeanId: {}, result: {}", jobBeanId, result);

        JobLogUtils.addJobsLog(packetId, FlyerConstants.logLevelInfo, jobBeanId,
            "[" + Utils.getClientStr(nbc) + "]收到任务消息，准备执行");
    }

    public static void handleReportJobResult(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) throws IOException {
        String jobBeanId = buffer.readString();
        int result = buffer.readByte();
        String logStr = buffer.readString();
        log.info("handleReportJobResult jobBeanId: {}, result: {}, logStr: {}", jobBeanId, result,
            logStr);

        ByteWriteBuffer writeBuffer = new ByteWriteBuffer(Utils.getLenth(jobBeanId) + 1);
        writeBuffer.writeString(jobBeanId);
        writeBuffer.writeByte((byte) 0);

        byte[] packet = Utils.wrapHeader(writeBuffer.getData(),
            ProtocolConstants.PROTOCOL_ID_REPORT_JOB_RESULT_RESPONSE, packetId);
        nbc.write(packet);
        nbc.flush();

        //记录日志
        writeLog(result, logStr, nbc, packetId, jobBeanId);
    }

    public static void handleCancelJob(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) {
        String jobBeanId = buffer.readString();
        int result = buffer.readByte();

        String client = Utils.getClientStr(nbc);

        JobLogUtils.addJobsLog(packetId, FlyerConstants.logLevelInfo, jobBeanId,
            "[" + client + "]接收到取消任务的消息");

        log.info("handleCancelJob jobBeanId: {}, result: {}", jobBeanId, result);
    }

    public static void handleCommitJob(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) {
        //not implemented fixme
    }


    public static void handlePullTopology(INonBlockingConnection nbc, long packetId)
        throws IOException {

        ClusterService settingsService = BeanFactoryRegister.getBean(ClusterService.class);
        ServerConfig serverConfig = settingsService.getCurrentConfig();

        ByteWriteBuffer writeBuffer =
            new ByteWriteBuffer(Utils.getLenth(serverConfig.getServers()) + 4);
        writeBuffer.writeInt(serverConfig.getVersion());
        writeBuffer.writeString(serverConfig.getServers());

        byte[] packet = Utils
            .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_PULL_TOPOLOGY_RESPONSE,
                packetId);
        nbc.write(packet);
        nbc.flush();
    }

    public static void sendExecuteJob(FlyerJobs flyerJobs, String connId) throws IOException {

        FlyerConnection connection = null;

        if (StringUtils.isNotBlank(connId)){
            connection = ConnectionHolder.getConnectionByConnId(flyerJobs.getAppCode(), connId);
        }else {
            connection = ConnectionHolder.getConnectionBySieve(flyerJobs.getAppCode(), flyerJobs.getVhost());
        }

        if (connection == null) {
            JobLogUtils.addJobsLog(flyerJobs.getRecordId(), FlyerConstants.logLevelWarn,
                flyerJobs.getJobBeanId(),
                "任务分发失败，未找到客户端");
            return;
        }
        ByteWriteBuffer writeBuffer = new ByteWriteBuffer(
            Utils.getLenth(flyerJobs.getJobBeanId()) + Utils.getLenth(flyerJobs.getParamData()));

        writeBuffer.writeString(flyerJobs.getJobBeanId());
        writeBuffer.writeString(flyerJobs.getParamData());

        byte[] packet = Utils
            .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_EXECUTE_JOB,
                flyerJobs.getRecordId());

        connection.getConnection().write(packet);
        connection.getConnection().flush();

        //记录日志
        JobLogUtils.addJobsLog(flyerJobs.getRecordId(), FlyerConstants.logLevelInfo,
            flyerJobs.getJobBeanId(),
            "开始执行，任务分发至[" + Utils.getClientStr(connection.getConnection()) + "]");
    }


    public static void sendCancelJob(FlyerJobs flyerJobs) throws IOException {

        //fixme specify vhost
        List<FlyerConnection> connectionList =
            ConnectionHolder.getConnectionByAppCode(flyerJobs.getAppCode());

        if (connectionList == null || connectionList.isEmpty()) {
            log.info("sendCancelJob fail, no connection for {}", flyerJobs.getAppCode());
            return;
        }

        ByteWriteBuffer writeBuffer = new ByteWriteBuffer(Utils.getLenth(flyerJobs.getJobBeanId()));
        writeBuffer.writeString(flyerJobs.getJobBeanId());
        byte[] packet = Utils
            .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_CANCEL_JOB,
                flyerJobs.getRecordId());

        for (FlyerConnection flyerConnection : connectionList) {
            INonBlockingConnection nbc = flyerConnection.getConnection();
            nbc.write(packet);
            nbc.flush();
        }
    }


    private static void writeLog(int result, String logStr, INonBlockingConnection nbc,
        long packetId, String jobBeanId) {
        int logLevel = -1;
        String client = Utils.getClientStr(nbc);
        String logText = "收到[" + client + "]消息,";

        //成功
        if (result == 0) {
            logLevel = FlyerConstants.logLevelInfo;
            logText += "执行成功,消息[" + logStr + "]";
        }//异常
        else if (result == 1) {
            logLevel = FlyerConstants.logLevelError;
            logText += "执行异常,消息[" + logStr + "]";
        }//取消
        else if (result == 2) {
            logLevel = FlyerConstants.logLevelWarn;
            logText += "任务取消,消息[" + logStr + "]";
        }//其他错误
        else if (result == 3) {
            logLevel = FlyerConstants.logLevelError;
            logText += "其他错误,消息[" + logStr + "]";
        }
        JobLogUtils.addJobsLog(packetId, logLevel, jobBeanId, logText);
    }

}
