package com.flyer.job.service;

public interface QuartzService {

    /**
     * 发起调度
     *
     * @param jobId
     */
    void scheduleJob(long jobId);

    /**
     * 停止调度
     *
     * @param jobId
     */
    void pauseJob(long jobId);

    /**
     * 立即执行(1次)
     *
     * @param jobId
     * @param connId 指定客户端端通道
     */
    void triggerJob(long jobId, String connId, String operateUser);

}
