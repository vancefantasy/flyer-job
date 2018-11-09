package com.flyer.job.client;

import org.xsocket.connection.INonBlockingConnection;

import java.util.Date;

/**
 * 连接信息
 */
public class FlyerConnection {

    /**
     * xSocket连接
     */
    private INonBlockingConnection connection;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次刷新时间
     */
    private Date lastFlushTime;

    public FlyerConnection(INonBlockingConnection connection, Date createTime, Date lastFlushTime) {
        this.connection = connection;
        this.createTime = createTime;
        this.lastFlushTime = lastFlushTime;
    }

    public INonBlockingConnection getConnection() {
        return connection;
    }

    public void setConnection(INonBlockingConnection connection) {
        this.connection = connection;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastFlushTime() {
        return lastFlushTime;
    }

    public void setLastFlushTime(Date lastFlushTime) {
        this.lastFlushTime = lastFlushTime;
    }
}
