package com.flyer.job.core;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.IConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import java.util.Arrays;

/**
 * Created by jianying.li on 2018/1/30.
 */
public class FlyerServer {

    private final static Logger log = LoggerFactory.getLogger(FlyerServer.class);

    /**
     * 默认端口
     */
    private static int port = 20180;

    public static void main(String[] args) {
        //初始化参数
        init(args);

        //监听服务
        startServer();
    }

    private static void startServer() {
        try {
            IServer server = new Server(port, new ServerHandler());
            //异步模式
            server.setFlushmode(IConnection.FlushMode.ASYNC);

            //手动刷新数据
            server.setAutoflush(false);

            //连接闲置超时60s，超过60s未收到数据包，由服务端主动断开
            server.setIdleTimeoutMillis(60 * 1000);

            //建立连接超时，暂未发现有什么用
            server.setConnectionTimeoutMillis(10 * 1000);

            server.start();

        } catch (Exception e) {
            log.error("flyer server run error", e);
            throw new RuntimeException(e);
        }
    }

    private static void init(String[] args) {
        if (args != null && args.length > 0) {
            log.info("flyer server load args : {}", Arrays.toString(args));
        }

        String portStr = System.getProperty("port");
        if (StringUtils.isNotBlank(portStr)) {
            port = Integer.parseInt(portStr);
        }
    }

}
