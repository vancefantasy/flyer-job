package com.flyer.job.domain;

import java.io.Serializable;

public class FlyerJobsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long id;
    /**
     * 执行记录id
     */
    private Long recordId;

    /**
     * 日志类型1:system 2:info 3:warn 4:error
     */
    private Integer logLevel;
    /**
     * 任务beanid
     */
    private String jobBeanId;

    /**
     * 日志详情
     */
    private String logText;
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

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setJobBeanId(String jobBeanId) {
        this.jobBeanId = jobBeanId;
    }

    public String getJobBeanId() {
        return jobBeanId;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getLogText() {
        return logText;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

}
