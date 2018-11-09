package com.flyer.job.domain;

import java.io.Serializable;

public class FlyerJobsRecord implements Serializable {

    private static final long serialVersionUID = 6459714179573453469L;

    /**
     * id
     */
    private Long id;
    /**
     * 应用id
     */
    private String appCode;

    /**
     * vhost,隔离环境
     */
    private String vhost;

    /**
     * 任务id
     */
    private String jobBeanId;
    /**
     * 类型 0 自动执行 1手动执行 2任务取消
     */
    private Integer executeType;
    /**
     * 状态 0已触发1执行成功 2执行失败
     */
    private Integer status;
    /**
     * 操作人
     */
    private String operateUser;
    /**
     * 创建时间
     */
    private java.util.Date createTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setJobBeanId(String jobBeanId) {
        this.jobBeanId = jobBeanId;
    }

    public String getJobBeanId() {
        return jobBeanId;
    }

    public void setExecuteType(Integer executeType) {
        this.executeType = executeType;
    }

    public Integer getExecuteType() {
        return executeType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }
}
