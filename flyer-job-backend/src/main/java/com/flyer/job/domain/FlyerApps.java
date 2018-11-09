package com.flyer.job.domain;

import java.io.Serializable;

public class FlyerApps implements Serializable {

    private static final long serialVersionUID = -8096944474404461693L;

    /**
     * 自增id
     */
    private Integer id;
    /**
     * 应用编码
     */
    private String appCode;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 描述
     */
    private String appDesc;

    /**
     * 创建时间
     */
    private java.util.Date createTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

}
