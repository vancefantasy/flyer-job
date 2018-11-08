package com.flyer.job.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 执行任务线程
 * Created by jianying.li on 2018/2/2.
 */
public class FlyerTask implements Runnable {

    private Job job;

    private String param;

    private long packetId;

    private Thread runThread;

    private final Logger log = LoggerFactory.getLogger(FlyerTask.class);

    public FlyerTask(Job job, String param, long packetId) {
        this.job = job;
        this.param = param;
        this.packetId = packetId;
    }

    @Override
    public void run() {
        runThread = Thread.currentThread();

        FlyerResult result = new FlyerResult();
        try {
            result = job.executeInternal(param);
        }//任务取消
        catch (InterruptedException ie) {
            log.warn("{} was interrupt", job.getJobBeanId());
            result =
                new FlyerResult(FlyerResult.Result.CANCELD, FlyerResult.Result.CANCELD.message);
        } catch (Exception e) {
            result =
                new FlyerResult(FlyerResult.Result.EXCEPTION, FlyerResult.Result.EXCEPTION.message);
        } finally {
            try {
                FlyerService.reportLog(job.getJobBeanId(), result, packetId);
            } catch (IOException e) {
                log.error("FlyerTask reportLog error", e);
            }
        }
    }

    public void cancel() {
        if (runThread != null) {
            runThread.interrupt();
        } else {
            //任务可能并不存在
            log.debug("cancel fail, job" + job.getJobBeanId() + " is null");
        }
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setPacketId(long packetId) {
        this.packetId = packetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FlyerTask flyerTask = (FlyerTask) o;

        return job.getJobBeanId().equals(flyerTask.job.getJobBeanId());
    }

    @Override
    public int hashCode() {
        return job.getJobBeanId().hashCode();
    }
}
