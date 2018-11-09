package com.flyer.job.core;

import com.flyer.job.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接持有者
 */
public class ConnectionHolder {

    private final static Logger log = LoggerFactory.getLogger(ConnectionHolder.class);

    /**
     * key:appCode+vhost
     * value:FlyerConnection集合
     */
    private static Map<String, List<FlyerConnection>> appConnsMap = new ConcurrentHashMap<>();

    /**
     * key: host+port
     * value:单个FlyerConnection
     */
    private static Map<String, FlyerConnection> clientConnsMap = new ConcurrentHashMap<>();

    public static synchronized void addConnection(INonBlockingConnection nbc, String appCode,
        String vhost) {

        Date now = new Date();
        FlyerConnection flyerConnection = new FlyerConnection(nbc, appCode, vhost, now, now);

        //维护appConnsMap
        List<FlyerConnection> connectionList = appConnsMap.get(appCode);
        if (connectionList == null) {
            connectionList = new ArrayList<>();
        }
        connectionList.add(flyerConnection);
        appConnsMap.put(appCode, connectionList);

        //维护clientConnsMap
        clientConnsMap.put(Utils.getClientStr(nbc), flyerConnection);
    }

    public static synchronized void removeConnection(INonBlockingConnection nbc) {
        String clientStr = Utils.getClientStr(nbc);
        FlyerConnection connection = clientConnsMap.remove(clientStr);
        if (connection == null) {
            log.warn("nbc: {} not exists in clientConnsMap", nbc);
            //异常情况，尝试遍历所有conn
            for (List<FlyerConnection> list : appConnsMap.values()) {
                Iterator<FlyerConnection> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getConnection() == nbc) {
                        iterator.remove();
                    }
                }
            }
        } else {
            //维护appConnsMap
            List<FlyerConnection> connectionList =
                appConnsMap.get(connection.getAppCode());
            Iterator<FlyerConnection> iterator = connectionList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getConnection() == nbc) {
                    iterator.remove();
                }
            }
        }
    }

    public static synchronized void refreshConnection(INonBlockingConnection nbc, String appCode,
        String vhost) {
        Date now = new Date();

        FlyerConnection flyerConnection = clientConnsMap.get(Utils.getClientStr(nbc));
        if (flyerConnection != null) {
            flyerConnection.setLastFlushTime(now);

            List<FlyerConnection> flyerConnectionList =
                appConnsMap.get(appCode);

            for (FlyerConnection conn : flyerConnectionList) {
                if (nbc == conn.getConnection()) {
                    conn.setLastFlushTime(now);
                }
            }
        }
    }

    public static List<FlyerConnection> getConnectionByAppCode(String appCode) {
        if (StringUtils.isBlank(appCode))
            return null;
        return appConnsMap.get(appCode);
    }

    public static FlyerConnection getConnectionBySieve(String appCode, String vhost) {
        if (StringUtils.isBlank(appCode))
            return null;
        List<FlyerConnection> connectionList = getConnectionByAppCode(appCode);
        if (connectionList == null || connectionList.isEmpty())
            return null;

        //过滤相同vhost
        List<FlyerConnection> filterList = new ArrayList<>();
        for (FlyerConnection flyerConnection : connectionList){
            if (vhost.equals(flyerConnection.getVhost())) {
                filterList.add(flyerConnection);
            }
        }

        //找不到符合条件的客户端
        if (filterList.size() < 1){
            return null;
        }

        //随机数
        int target = new Random().nextInt(filterList.size());
        int i = 0;
        for (FlyerConnection connection : filterList) {
            if (i == target) {
                return connection;
            }
            i++;
        }
        return null;
    }

    public static FlyerConnection getConnectionByConnId(String appCode, String connId) {
        if (StringUtils.isBlank(appCode) || StringUtils.isBlank(connId))
            return null;
        List<FlyerConnection> connectionList = getConnectionByAppCode(appCode);
        if (connectionList == null || connectionList.isEmpty())
            return null;

        for (FlyerConnection flyerConnection : connectionList){
            if (connId.equals(flyerConnection.getConnection().getId())) {
                return flyerConnection;
            }
        }
        return null;
    }

}
