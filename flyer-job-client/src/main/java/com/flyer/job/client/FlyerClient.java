package com.flyer.job.client;

import com.flyer.job.common.ByteReadBuffer;
import com.flyer.job.common.ByteWriteBuffer;
import com.flyer.job.common.UID;
import com.flyer.job.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.NonBlockingConnection;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * flyer客户端
 * 负责维护client与每个server的通道连接
 */
public class FlyerClient {

    private Logger log = LoggerFactory.getLogger(FlyerClient.class);

    /**
     * 守护线程，心跳、检查僵死连接
     */
    private MaintThread maintThread;

    private FlyerService flyerService;

    /**
     * 客户端配置
     */
    private FlyerConfig config;

    private ClientHandler clientHandler;

    /**
     * 心跳间隔
     */
    private long heartbeatInterval = 30 * 1000;

    private int currentServerVersion = 0;

    private int currentClientVersion = 0;

    private String currentLocalServers;

    private String currentRemoteServers;

    /**
     * 客户端状态
     */
    private static State state = State.READY;

    public FlyerClient(FlyerConfig config, FlyerService flyerService) {
        this.config = config;
        this.flyerService = flyerService;
        this.clientHandler = new ClientHandler(flyerService);
    }

    public synchronized void refeshConfig(boolean launch) {

        this.state = State.NOT_READY;

        String[] serverArr = config.getServers().split(",");
        for (String server : serverArr) {
            try {
                tryPullOnce(server);
                break;
            } catch (Exception e) {
                log.error("tryPullOnce error, server: {}", server, e);
            }
        }
        //启动时
        if (launch) {
            currentClientVersion = currentServerVersion;
            currentLocalServers = currentRemoteServers;
        } else {
            //版本号升级
            if (currentServerVersion > currentClientVersion) {
                //remoteServers发生了变化
                if (!currentLocalServers.equals(currentRemoteServers)) {
                    //关闭所有连接
                    ConnectionHolder.closeAllConnection();
                    //更新本地配置
                    currentClientVersion = currentServerVersion;
                    currentLocalServers = currentRemoteServers;

                    //sleep 等关闭连接
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        log.error("refeshConfig InterruptedException", e);
                    }
                    //重新连接flyer server
                    start();
                } else {
                    //版本号变了，但servers配置并没有变，此时修改currentClientVersion
                    currentClientVersion = currentServerVersion;
                }
            } else {
                log.warn("currentClientVersion {} equal latest serverVersion {}",
                    currentClientVersion, currentServerVersion);
            }
        }
        this.state = State.READY;
    }

    private void tryPullOnce(String server) {
        String[] arr = server.split(":");
        String host = arr[0];
        int port = Integer.parseInt(arr[1]);
        long packetId = UID.next();

        INonBlockingConnection nbc = null;

        try {
            nbc = new NonBlockingConnection(host, port, this.clientHandler);
            byte[] packet = Utils
                .wrapHeader(new byte[] {}, ProtocolConstants.PROTOCOL_ID_PULL_TOPOLOGY, packetId);
            nbc.write(packet);
            nbc.flush();

            //缓存交换数据
            Exchanger<byte[]> ex = new Exchanger<>();
            flyerService.setCache(packetId, ex);
            byte[] result = null;

            //10s timeout
            result = ex.exchange(null, 10, TimeUnit.SECONDS);

            ByteReadBuffer readBuffer = new ByteReadBuffer(result);

            int serverVersion = readBuffer.readInt();
            String servers = readBuffer.readString();

            log.info("pull server config success, serverVersion : {}, servers : {}", serverVersion,
                servers);
            currentServerVersion = serverVersion;
            currentRemoteServers = servers;

        } catch (Exception e) {
            log.error("tryPullOnce error, server : {}", server, e);
        } finally {
            flyerService.clearCache(packetId);
            if (nbc != null) {
                try {
                    nbc.close();
                } catch (IOException e) {
                    log.info("nbc close error", e);
                }
            }
        }
    }

    public void load(boolean launch) {
        //更新本地配置
        refeshConfig(launch);

        //去 连接
        start();
    }

    public void start() {
        String[] serverArr = currentLocalServers.split(",");

        for (String server : serverArr) {
            this.connectToFlyer(server);
            log.debug("connectToFlyer to {}", server);
        }

        //启动守护线程。心跳，自检，断连，维护连接池
        startMaintThread();
    }

    public void connectToFlyer(String server) {
        String[] arr = server.split(":");
        String host = arr[0];
        int port = Integer.parseInt(arr[1]);

        try {
            INonBlockingConnection nbc = new NonBlockingConnection(host, port, this.clientHandler);
            //去握手
            ByteWriteBuffer writeBuffer = new ByteWriteBuffer(
                Utils.getLenth(config.getAppCode()) + Utils.getLenth(config.getVhost()) + 1);
            writeBuffer.writeString(config.getAppCode());
            writeBuffer.writeString(config.getVhost());
            writeBuffer.writeByte((byte) 0);

            byte[] packet = Utils
                .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_SHAKEHAND, 0l);
            nbc.write(packet);
            nbc.flush();
        } catch (Exception e) {
            log.error("connectToFlyer error, server : {}", server, e);
        }
    }

    //从Spring容器获取本地加载的所有job实现,组装成 "xxxJob1,xxxJob2" 格式
    private String getJobs() {
        Map<String, ?> listByType = FlyerClientContext.getListByType(Job.class);

        Iterator<String> iterator = listByType.keySet().iterator();
        StringBuilder sb = new StringBuilder();

        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    protected void startMaintThread() {
        if (this.maintThread != null) {
            if (this.maintThread.isRunning()) {
                log.warn("maintThread already running");
            } else {
                this.maintThread.start();
            }
        } else {
            this.maintThread = new MaintThread(this);
            this.maintThread.start();
        }
    }

    protected class MaintThread extends Thread {
        private boolean running;
        private FlyerClient flyerClient;

        protected MaintThread(FlyerClient flyerClient) {
            this.flyerClient = flyerClient;
            setDaemon(true);
            setName("FlyerClientMaintThread");
        }

        public boolean isRunning() {
            return running;
        }

        public void run() {
            this.running = true;

            while (this.running) {
                try {
                    if (this.flyerClient.state == FlyerClient.State.READY) {
                        this.flyerClient.selfMaint();
                    }
                    Thread.sleep(this.flyerClient.heartbeatInterval);
                } catch (Exception e) {
                    log.error("MaintThread error", e);
                }
            }
        }
    }

    protected void selfMaint() {
        //检查僵死连接
        checkDeadConnection();

        //心跳
        heartBeat();
    }

    private void heartBeat() {

        String[] serverArr = currentLocalServers.split(",");

        for (String server : serverArr) {
            FlyerConnection flyerConnection = ConnectionHolder.getConnectionByServer(server);

            if (flyerConnection == null) {
                log.warn("server {} is not in ConnectionHolder, try connect", server);
                connectToFlyer(server);
            } else {
                INonBlockingConnection nbc = flyerConnection.getConnection();
                boolean isAcitve = false;
                try {
                    ByteWriteBuffer writeBuffer = new ByteWriteBuffer(
                        Utils.getLenth(config.getAppCode()) + Utils.getLenth(config.getVhost())
                            + 1);
                    writeBuffer.writeString(config.getAppCode());
                    writeBuffer.writeString(config.getVhost());
                    writeBuffer.writeByte((byte) 0);

                    byte[] packet = Utils
                        .wrapHeader(writeBuffer.getData(), ProtocolConstants.PROTOCOL_ID_HEARTBEAT,
                            0l);
                    nbc.write(packet);
                    nbc.flush();
                    isAcitve = true;
                } catch (Exception e) {
                    log.error("flush heartBeat data error", e);
                }

                if (!isAcitve) {
                    //如通道异常，1,关闭连接 2，将连接从维护连接池移除3，尝试重建连接
                    try {
                        nbc.close();
                    } catch (IOException e) {
                        log.error("connection close error, conn: {}",
                            flyerConnection.getConnection(), e);
                    }

                    ConnectionHolder.removeConnection(nbc);
                    log.warn("connection is not active, try reconnect flyer server");
                    this.connectToFlyer(server);
                }
            }
        }
    }

    private void checkDeadConnection() {
        Iterator<Map.Entry<String, FlyerConnection>> iterator =
            ConnectionHolder.getConnections().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FlyerConnection> entry = iterator.next();
            FlyerConnection flyerConnection = entry.getValue();
            long lastFlushTime = flyerConnection.getLastFlushTime().getTime();
            long inteval = System.currentTimeMillis() - lastFlushTime;
            if (inteval > 2 * this.heartbeatInterval) {
                iterator.remove();
                log.info("connection {} may dead, last lastFlushTime : {}, inteval by second: {}",
                    flyerConnection.getConnection(), flyerConnection.getLastFlushTime(),
                    inteval / 1000);
            }
        }
    }


    public void close() {
        log.info("FlyerClient close");
        Map<String, FlyerConnection> connectionMap = ConnectionHolder.getConnections();
        for (FlyerConnection flyerConnection : connectionMap.values()) {
            INonBlockingConnection connection = flyerConnection.getConnection();
            try {
                connection.close();
            } catch (IOException e) {
                log.error("connection : {} close error", connection, e);
            }
        }
    }

    public int getCurrentClientVersion() {
        return currentClientVersion;
    }

    public enum State {
        READY, NOT_READY//拉取server端配置期间，我们认为客户端是 未就绪状态。未就绪状态不需要自检、心跳
        ;
    }
}
