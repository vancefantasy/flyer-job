package com.flyer.job.client;

import com.flyer.job.common.ByteReadBuffer;
import com.flyer.job.common.ByteWriteBuffer;
import com.flyer.job.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;

/**
 * Created by jianying.li on 2018/2/4.
 */
public class FlyerService {

    private final static Logger log = LoggerFactory.getLogger(FlyerService.class);

    private static JobExecutor executor;

    private static FlyerClient flyerClient;

    /**
     * 线程交换数据
     */
    public static ConcurrentHashMap<Long, Exchanger<byte[]>> cache = new ConcurrentHashMap<>(3);

    public FlyerService(FlyerConfig flyerConfig) {
        executor = new JobExecutor(flyerConfig);
    }

    public static void pullTopology(byte[] body, long packetId) {
        excuteExchange(packetId, body);
    }

    public static void handleShake(ByteReadBuffer buffer, INonBlockingConnection nbc) {
        int resp = buffer.readByte();
        if (resp == 1) {
            //log
        } else {
            //log
        }
    }

    public static void handleHeartBeat(ByteReadBuffer buffer, INonBlockingConnection nbc) {
        buffer.readByte();
        int serverVersion = buffer.readInt();
        ConnectionHolder.refreshConnection(Utils.getServerStr(nbc));
        if (serverVersion > flyerClient.getCurrentClientVersion()) {
            flyerClient.refeshConfig(false);
        }
    }

    public static void handleExecuteJob(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) throws IOException {
        String jobBeanId = buffer.readString();
        String paramData = buffer.readString();
        int result = 1;
        try {
            Job job = (Job) FlyerClientContext.getBean(jobBeanId);
            if (job == null) {
                log.error("job : {} not exists", jobBeanId);
            } else {
                executor.execute(new FlyerTask(job, paramData, packetId));
                result = 0;
            }
        } catch (Exception e) {
            log.error("Executes the given task error,jobBeanId: {}", jobBeanId, e);
        }

        ByteWriteBuffer writeBuffer = new ByteWriteBuffer(Utils.getLenth(jobBeanId) + 1);
        writeBuffer.writeString(jobBeanId);
        writeBuffer.writeByte((byte) result);
        byte[] body = Utils
            .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_EXECUTE_JOB_RESPONSE,
                packetId);
        nbc.write(body);
        nbc.flush();
    }

    public static void handleReportJobResult(ByteReadBuffer buffer, INonBlockingConnection nbc) {
        String jobBeanId = buffer.readString();
        int result = buffer.readByte();
        if (result == 1) {

        } else {

        }
    }

    public static void handleCancelJob(ByteReadBuffer buffer, INonBlockingConnection nbc,
        long packetId) throws IOException {

        String vhost = buffer.readString();

        String jobBeanId = buffer.readString();
        Job job = (Job) FlyerClientContext.getBean(jobBeanId);
        byte result = 1;
        if (job == null) {
            log.error("{} not exists", jobBeanId);
        } else {
            if (executor.cancleJob(job, packetId)){
                result = 0;
            }else {
                //没有任务时，即没有执行取消动作，不需要回消息
                return;
            }
        }
        ByteWriteBuffer writeBuffer = new ByteWriteBuffer(Utils.getLenth(jobBeanId) + 1);

        writeBuffer.writeString(vhost);

        writeBuffer.writeString(jobBeanId);
        writeBuffer.writeByte(result);

        byte[] body = Utils
            .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_CANCEL_JOB_RESPONSE,
                packetId);
        nbc.write(body);
        nbc.flush();
    }

    public void handleCommitJob(ByteReadBuffer buffer, INonBlockingConnection nbc)
        throws IOException {

    }

    public static void reportLog(String jobBeanId, FlyerResult flyerResult, long packetId)
        throws IOException {
        ByteWriteBuffer writeBuffer = new ByteWriteBuffer(
            Utils.getLenth(jobBeanId) + Utils.getLenth(flyerResult.getMessage()) + 1);
        writeBuffer.writeString(jobBeanId);
        writeBuffer.writeByte((byte) flyerResult.getResult().code);
        writeBuffer.writeString(flyerResult.getMessage());

        FlyerConnection flyerConnection = ConnectionHolder.getConnectionBySieve();
        if (flyerConnection == null) {
            log.error("reportLog fail, no connection exists");
            return;
        }

        byte[] body = Utils
            .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_REPORT_JOB_RESULT,
                packetId);
        flyerConnection.getConnection().write(body);
        flyerConnection.getConnection().flush();
    }

    public static void setCache(long packetId, Exchanger<byte[]> exchanger) {
        cache.put(packetId, exchanger);
    }

    public static void clearCache(long packetId) {
        cache.remove(packetId);
    }

    public static void excuteExchange(long packetId, byte[] data) {
        Exchanger<byte[]> ex = cache.remove(packetId);
        if (ex != null) {
            try {
                ex.exchange(data);
            } catch (InterruptedException e) {
                log.error("excuteExchange error, packetId: {}, data: {}", packetId,
                    Arrays.toString(data), e);
            }
        } else {
            //just ignore
            //low probability of exchange timeout
            log.warn("exchanger(packetId: {}) not exists, Maybe it's timeout", packetId);
        }
    }

    public static void setFlyerClient(FlyerClient flyerClient) {
        FlyerService.flyerClient = flyerClient;
    }
}
