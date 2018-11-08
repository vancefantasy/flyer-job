package com.flyer.job.client;

import com.flyer.job.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jianying.li on 2018/1/31.
 */
public class ConnectionHolder {

    private final static Logger log = LoggerFactory.getLogger(ConnectionHolder.class);

    /**
     * key:host:port
     * value:conn
     * 连接管理
     */
    private static Map<String, FlyerConnection> connections = new ConcurrentHashMap<>();

    public static FlyerConnection getConnectionBySieve() {
        int target = getRandom(connections.size());
        int i = 0;
        for (FlyerConnection connection : connections.values()) {
            if (i == target) {
                return connection;
            }
            i++;
        }
        return null;
    }

    public static FlyerConnection getConnectionByServer(String server) {
        return connections.get(server);
    }

    public static Map<String, FlyerConnection> getConnections() {
        return connections;
    }

    public static void removeConnection(INonBlockingConnection nbc) {
        Iterator<FlyerConnection> iterator = connections.values().iterator();
        while (iterator.hasNext()) {
            FlyerConnection flyerConnection = iterator.next();
            if (flyerConnection.getConnection() == nbc) {
                iterator.remove();
                log.info("connection {} was remove", flyerConnection.getConnection());
            }
        }
    }

    public static void refreshConnection(String server) {
        FlyerConnection connection = connections.get(server);
        if (connection != null) {
            connection.setLastFlushTime(new Date());
        } else {
            log.warn("refreshConnection fail, {} no exist in collection {}", server,
                Arrays.toString(connections.keySet().toArray()));
        }
    }

    public static void addConnection(INonBlockingConnection nbc) {
        Date now = new Date();
        FlyerConnection flyerConnection = new FlyerConnection(nbc, now, now);
        connections.put(Utils.getServerStr(nbc), flyerConnection);
    }

    public static void closeAllConnection() {
        Iterator<FlyerConnection> iterator = connections.values().iterator();
        while (iterator.hasNext()) {
            FlyerConnection flyerConnection = iterator.next();
            INonBlockingConnection connection = flyerConnection.getConnection();
            if (connection != null) {
                try {
                    connection.close();
                    log.info("connection : {} closed", connection);
                } catch (IOException e) {
                    log.error("connection : {} close error", connection, e);
                }
            }
        }
        connections.clear();
    }

    private static int getRandom(int i) {
        return new Random().nextInt(i);
    }
}
