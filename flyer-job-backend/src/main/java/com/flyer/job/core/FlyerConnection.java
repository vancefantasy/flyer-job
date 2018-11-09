package com.flyer.job.core;

import org.xsocket.connection.INonBlockingConnection;

import java.util.Date;

/**
 * 连接实体定义
 */
public class FlyerConnection {

    /**
     * xSocket连接
     */
    private INonBlockingConnection connection;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * vhost
     * 区分环境
     */
    private String vhost;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次刷新时间
     */
    private Date lastFlushTime;

    public FlyerConnection(INonBlockingConnection connection, String appCode, String vhost, Date createTime,
        Date lastFlushTime) {
        this.connection = connection;
        this.appCode = appCode;
        this.vhost = vhost;
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

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }
}
