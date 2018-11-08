package com.flyer.job.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义任务执行器，使用ThreadPoolExecutor
 * Created by jianying.li on 2018/2/4.
 */
public class JobExecutor extends ThreadPoolExecutor {

    private final Logger log = LoggerFactory.getLogger(JobExecutor.class);

    /**
     * 记录当前正在执行的线程
     */
    private static Map<String, FlyerTask> taskMap = new ConcurrentHashMap<>();

    public JobExecutor(FlyerConfig config) {
        //任务队列长度：500
        //队列满时任务处理策略：直接抛弃
        super(config.getCorePoolSize(), config.getMaxPoolSize(), config.getKeepAliveTime(),
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(500),
            new ThreadPoolExecutor.DiscardPolicy());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        FlyerTask flyerTask = (FlyerTask) r;
        taskMap.put(flyerTask.getJob().getJobBeanId(), flyerTask);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        FlyerTask flyerTask = (FlyerTask) r;
        taskMap.remove(flyerTask.getJob().getJobBeanId());
    }

    public boolean cancleJob(Job job, long packetId) {
        //当前运行的任务
        FlyerTask task = taskMap.get(job.getJobBeanId());

        if (task != null) {
            //清理队列中可能存在的任务
            getQueue().remove(task);

            //warn:特殊处理，为了记录取消的日志，这里将packetId重置为 新值
            task.setPacketId(packetId);

            //中断线程
            task.cancel();

            return true;
        } else {
            log.debug("job : {} not running", job.getJobBeanId());
            return false;
        }
    }
}
