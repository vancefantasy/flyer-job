package com.flyer.job.core;

import com.flyer.job.common.BeanFactoryRegister;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.service.FlyerJobsLogService;
import com.flyer.job.service.FlyerJobsRecordService;
import com.flyer.job.service.FlyerJobsService;
import com.google.common.base.Stopwatch;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 清理任务日志job
 * flyer_jobs_record表每个任务最多保留1500条数据，最少保留1000条
 * flyer_jobs_log表每个任务最多保留5000条数据，最少保留4500条
 * <p>
 * 调度策略，每天凌晨2:00开始运行
 * Created by jianying.li on 2018/4/27.
 */
public class ClearLogJob implements Job {

    private final Logger log = LoggerFactory.getLogger(ClearLogJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("ClearLogTask run");
        Stopwatch watch = Stopwatch.createStarted();
        List<FlyerJobs> jobsList = BeanFactoryRegister.getBean(FlyerJobsService.class).listAll();
        CountDownLatch latch = new CountDownLatch(jobsList.size());
        for (FlyerJobs job : jobsList) {
            clear(job.getJobBeanId(), latch);
        }
        try {
            latch.await();
            log.info("ClearLogTask end, consume : {}", watch.stop());
        } catch (InterruptedException e) {
            log.error("CountDownLatch InterruptedException", e);
        }
    }

    @Async
    private void clear(String jobBeanId, CountDownLatch latch) {
        BeanFactoryRegister.getBean(FlyerJobsRecordService.class).clearRecords(jobBeanId, 0);
        BeanFactoryRegister.getBean(FlyerJobsLogService.class).clearLogs(jobBeanId, 0);
        latch.countDown();
    }
}
