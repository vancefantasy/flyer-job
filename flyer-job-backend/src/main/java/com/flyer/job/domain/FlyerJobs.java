package com.flyer.job.domain;

import java.io.Serializable;

public class FlyerJobs implements Serializable {

    private static final long serialVersionUID = 4725604409277457182L;

    /**
     * 自增id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 所属app
     */
    private Integer appId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 主要用来环境隔离
     */
    private String vhost;

    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 任务执行beanid
     */
    private String jobBeanId;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 创建时间
     */
    private java.util.Date createTime;
    /**
     * 参数，格式为json
     */
    private String paramData;
    /**
     * 0 已创建 1 已启动 2 删除
     */
    private Integer status;
    /**
     * 责任人
     */
    private String owner;


    /**
     * 执行记录ID
     */
    private Long recordId;

    /**
     * 0 自动执行 1 手动执行 2 任务取消
     */
    private Integer executeType;

    /**
     * 操作人
     */
    private String operateUser;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getAppId() {
        return appId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public void setJobBeanId(String jobBeanId) {
        this.jobBeanId = jobBeanId;
    }

    public String getJobBeanId() {
        return jobBeanId;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }

    public String getParamData() {
        return paramData;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Integer getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Integer executeType) {
        this.executeType = executeType;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }
}
