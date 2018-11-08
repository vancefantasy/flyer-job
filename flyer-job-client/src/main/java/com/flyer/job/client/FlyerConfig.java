package com.flyer.job.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * 客户端配置
 * Created by jianying.li on 2018/1/31.
 */
public class FlyerConfig implements EmbeddedValueResolverAware {

    private final Logger log = LoggerFactory.getLogger(FlyerConfig.class);

    /**
     * flyer服务器连接串（必填）
     * 例如:1.1.1.1:20180,2.2.2.2:20180
     */
    private String servers;

    /**
     * 应用编码（必填）
     */
    private String appCode;

    /**
     * 核心线程数，默认值:3
     */
    private int corePoolSize = 3;

    /**
     * 最大线程数，默认值：10
     */
    private int maxPoolSize = 10;

    /**
     * 线程空闲超时时间
     * 单位：s
     * 默认值：5分钟
     */
    private int keepAliveTime = 5 * 60;

    /**
     * 是否强依赖于flyer server
     * 默认 强依赖
     */
    private boolean depend = true;

    /**
     * 是否关闭flyer server
     * 默认：开启状态
     */
    private boolean disable = false;

    /**
     * virtual host
     * 隔离环境使用
     * 默认值 空串，非Null
     */
    private String vhost = "";

    private StringValueResolver stringValueResolver;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public boolean getDepend() {
        return depend;
    }

    public void setDepend(boolean depend) {
        this.depend = depend;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        stringValueResolver = resolver;
    }

    public void setConfig() {

        String flyerServers = stringValueResolver.resolveStringValue(Constants.keyOfFlyerServer);
        if (!eq(Constants.keyOfFlyerServer, flyerServers) && flyerServers != null
            && flyerServers.trim() != "") {
            setServers(flyerServers);
        } else {
            log.error("flyer.server cannot be null");
            throw new RuntimeException("flyer.server cannot be null");
        }

        String appCode = stringValueResolver.resolveStringValue(Constants.keyOfAppCode);
        if (!eq(Constants.keyOfAppCode, appCode) && appCode != null && appCode.trim() != "") {
            setAppCode(appCode);
        } else {
            log.error("flyer.appCode cannot be null");
            throw new RuntimeException("flyer.appCode cannot be null");
        }

        String corePoolSizeStr =
            stringValueResolver.resolveStringValue(Constants.keyOfCorePoolSize);
        if (!eq(Constants.keyOfCorePoolSize, corePoolSizeStr) && corePoolSizeStr != null
            && corePoolSizeStr.trim() != "") {
            int corePoolSize;
            try {
                corePoolSize = Integer.parseInt(corePoolSizeStr);
                setCorePoolSize(corePoolSize);
            } catch (NumberFormatException e) {
                log.error("illegel flyer.corePoolSize value : {}", corePoolSizeStr, e);
            }
        } else {
            log.warn("flyer.corePoolSize was not set, use default value");
        }

        String maxPoolSizeStr = stringValueResolver.resolveStringValue(Constants.keyOfMaxPoolSize);
        if (!eq(Constants.keyOfMaxPoolSize, maxPoolSizeStr) && maxPoolSizeStr != null
            && maxPoolSizeStr.trim() != "") {
            int maxPoolSize;
            try {
                maxPoolSize = Integer.parseInt(maxPoolSizeStr);
                setMaxPoolSize(maxPoolSize);
            } catch (NumberFormatException e) {
                log.error("illegel flyer.maxPoolSize value : {}", maxPoolSizeStr, e);
            }
        } else {
            log.warn("flyer.maxPoolSize was not set, use default value");
        }

        String keepAliveTimeStr =
            stringValueResolver.resolveStringValue(Constants.keyOfKeepAliveTime);
        if (!eq(Constants.keyOfKeepAliveTime, keepAliveTimeStr) && keepAliveTimeStr != null
            && keepAliveTimeStr.trim() != "") {
            int keepAliveTime;
            try {
                keepAliveTime = Integer.parseInt(keepAliveTimeStr);
                setMaxPoolSize(keepAliveTime);
            } catch (NumberFormatException e) {
                log.error("illegel flyer.keepAliveTime value : {}", keepAliveTimeStr, e);
            }
        } else {
            log.warn("flyer.keepAliveTime was not set, use default value");
        }


        String dependStr = stringValueResolver.resolveStringValue(Constants.keyOfDepend);
        if (!eq(Constants.keyOfDepend, dependStr) && dependStr != null && dependStr.trim() != "") {
            try {
                setDepend(Boolean.valueOf(dependStr));
            } catch (NumberFormatException e) {
                log.error("illegel flyer.depend value : {}", dependStr, e);
            }
        } else {
            log.warn("flyer.dependStr was not set, use default value");
        }

        String vhost = stringValueResolver.resolveStringValue(Constants.keyOfVhost);
        if (!eq(Constants.keyOfVhost, vhost) && vhost != null && vhost.trim() != "") {
            setVhost(vhost);
        } else {
            log.warn("flyer.vhost was not set, use default value");
        }

        String disableStr = stringValueResolver.resolveStringValue(Constants.keyOfDisable);
        if (!eq(Constants.keyOfDisable, disableStr) && disableStr != null && disableStr.trim() != "") {
            setDisable(Boolean.valueOf(disableStr));
        } else {
            log.warn("flyer.disable was not set, use default value");
        }

    }

    private boolean eq(String source, String resolveValue) {
        return source.equals(resolveValue);
    }

    @Override
    public String toString() {
        return "servers: " + this.servers + ",appCode: " + this.appCode + ",corePoolSize: "
            + this.corePoolSize + ",maxPoolSize: " + this.maxPoolSize + ",keepAliveTime: "
            + this.keepAliveTime + ",depend: " + this.depend + ",vhost: " + this.vhost;
    }
}
