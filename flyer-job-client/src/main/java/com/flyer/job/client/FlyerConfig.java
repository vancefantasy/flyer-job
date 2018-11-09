package com.flyer.job.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * flyer client config
 */
public class FlyerConfig implements EmbeddedValueResolverAware {

    private final Logger log = LoggerFactory.getLogger(FlyerConfig.class);

    /**
     * flyer服务器连接串（required）
     * 例如:1.1.1.1:20180,2.2.2.2:20180
     */
    private String servers;

    /**
     * 应用编码（required）
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

    public boolean getDisable() {
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
        String flyerServers = resolvePropertiesValue(Constants.keyOfFlyerServer);
        if (configLegal(Constants.keyOfFlyerServer, flyerServers)) {
            setServers(flyerServers);
        } else {
            log.error("flyer.server cannot be null");
            throw new RuntimeException("flyer.server cannot be null");
        }

        String appCode = resolvePropertiesValue(Constants.keyOfAppCode);
        if (configLegal(Constants.keyOfAppCode, appCode)) {
            setAppCode(appCode);
        } else {
            log.error("flyer.appCode cannot be null");
            throw new RuntimeException("flyer.appCode cannot be null");
        }

        String corePoolSizeStr = resolvePropertiesValue(Constants.keyOfCorePoolSize);
        if (configLegal(Constants.keyOfCorePoolSize, corePoolSizeStr)) {
            int corePoolSize;
            try {
                corePoolSize = Integer.parseInt(corePoolSizeStr);
                setCorePoolSize(corePoolSize);
            } catch (NumberFormatException e) {
                log.error("illegel flyer.corePoolSize value : {}", corePoolSizeStr, e);
            }
        } else {
            log.info("flyer.corePoolSize was not set, use default value");
        }

        String maxPoolSizeStr = resolvePropertiesValue(Constants.keyOfMaxPoolSize);
        if (configLegal(Constants.keyOfMaxPoolSize, maxPoolSizeStr)) {
            int maxPoolSize;
            try {
                maxPoolSize = Integer.parseInt(maxPoolSizeStr);
                setMaxPoolSize(maxPoolSize);
            } catch (NumberFormatException e) {
                log.error("illegel flyer.maxPoolSize value : {}", maxPoolSizeStr, e);
            }
        } else {
            log.info("flyer.maxPoolSize was not set, use default value");
        }

        String keepAliveTimeStr = resolvePropertiesValue(Constants.keyOfKeepAliveTime);
        if (configLegal(Constants.keyOfKeepAliveTime, keepAliveTimeStr)) {
            int keepAliveTime;
            try {
                keepAliveTime = Integer.parseInt(keepAliveTimeStr);
                setMaxPoolSize(keepAliveTime);
            } catch (NumberFormatException e) {
                log.error("illegel flyer.keepAliveTime value : {}", keepAliveTimeStr, e);
            }
        } else {
            log.info("flyer.keepAliveTime was not set, use default value");
        }

        String dependStr = resolvePropertiesValue(Constants.keyOfDepend);
        if (configLegal(Constants.keyOfDepend, dependStr)) {
            try {
                setDepend(Boolean.valueOf(dependStr));
            } catch (NumberFormatException e) {
                log.error("illegel flyer.depend value : {}", dependStr, e);
            }
        } else {
            log.info("flyer.dependStr was not set, use default value");
        }

        String vhost = resolvePropertiesValue(Constants.keyOfVhost);
        if (configLegal(Constants.keyOfVhost, vhost)) {
            setVhost(vhost);
        } else {
            log.info("flyer.vhost was not set, use default value");
        }

        String disableStr = resolvePropertiesValue(Constants.keyOfDisable);
        if (configLegal(Constants.keyOfDisable, disableStr)) {
            setDisable(Boolean.valueOf(disableStr));
        } else {
            log.info("flyer.disable was not set, use default value");
        }
    }

    private String resolvePropertiesValue(String key) {
        String vaule = null;
        try {
            vaule = stringValueResolver.resolveStringValue(key);
        } catch (IllegalArgumentException iae) {
            //ignore
        }
        return vaule;
    }

    private boolean configLegal(String key, String value) {
        return !eq(Constants.keyOfDisable, value) && notNull(value);
    }

    private boolean eq(String source, String resolveValue) {
        return source.equals(resolveValue);
    }

    private boolean notNull(String str) {
        return str != null && str.trim() != "";
    }

    @Override
    public String toString() {
        return "\n appCode: " + this.appCode + "\n disable: " + this.disable + "\n depend: "
            + this.depend + "\n servers: " + this.servers + "\n vhost: " + this.vhost
            + "\n corePoolSize: " + this.corePoolSize + "\n maxPoolSize: " + this.maxPoolSize
            + "\n keepAliveTime: " + this.keepAliveTime;

    }

}
