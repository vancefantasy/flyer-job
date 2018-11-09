package com.flyer.job.domain;

import java.util.Date;

public class ServerConfig {

    /**
     * flyer server 配置
     */
    private String servers;

    /**
     * 当前版本
     */
    private int version;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
