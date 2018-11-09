package com.flyer.job.service.impl;

import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.core.InternalJob;
import com.flyer.job.dao.FlyerJobsDao;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.service.QuartzService;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class QuartzServiceImpl implements QuartzService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private FlyerJobsDao flyerJobsDao;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void scheduleJob(long jobId) {
        FlyerJobs flyerJobs = flyerJobsDao.findFlyerJobsById(jobId);
        if (flyerJobs == null) {
            log.error("job: {} not exists", jobId);
            throw new RuntimeException("job not exists");
        }

        String name = flyerJobs.getJobBeanId();
        String group = flyerJobs.getAppCode();
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            Trigger triggerPossibly = scheduler.getTrigger(triggerKey);
            if (triggerPossibly != null) {
                scheduler.rescheduleJob(triggerKey, triggerPossibly);
            } else {
                Trigger trigger = newTrigger().withIdentity(name, group)
                    .withSchedule(cronSchedule(flyerJobs.getCronExpression())).build();

                JobKey jobKey = new JobKey(name, group);
                scheduler.deleteJob(jobKey);
                JobDetail jobDetail = newJob(InternalJob.class).withIdentity(name, group)
                    .usingJobData(FlyerConstants.dataKey, JsonMapper.INSTANCE.toJson(flyerJobs))
                    .storeDurably().build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            log.info("scheduleJob error, jobId : {}", jobId, e);
            throw new RuntimeException("scheduleJob error, jobId:" + jobId);
        }
    }

    @Override
    public void pauseJob(long jobId) {
        FlyerJobs flyerJobs = flyerJobsDao.findFlyerJobsById(jobId);
        if (flyerJobs == null) {
            //error
        }
        String name = flyerJobs.getJobBeanId();
        String group = flyerJobs.getAppCode();
        TriggerKey triggerKey = new TriggerKey(name, group);
        try {
            scheduler.unscheduleJob(triggerKey);
        } catch (SchedulerException e) {
            log.error("pauseJob error, triggerKey : {}", triggerKey.toString(), e);
        }
    }

    @Override
    public void triggerJob(long jobId, String connId, String operateUser) {
        FlyerJobs flyerJobs = flyerJobsDao.findFlyerJobsById(jobId);
        if (flyerJobs == null) {
            //error
        }

        //手动执行
        flyerJobs.setExecuteType(FlyerConstants.executeTypeManual);
        flyerJobs.setOperateUser(operateUser);

        String name = flyerJobs.getJobBeanId();
        String group = flyerJobs.getAppCode();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(FlyerConstants.dataKey, JsonMapper.INSTANCE.toJson(flyerJobs));
        jobDataMap.put(FlyerConstants.connId, connId);

        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetailPossibly = scheduler.getJobDetail(jobKey);
            if (jobDetailPossibly != null) {
                scheduler.triggerJob(jobKey, jobDataMap);
            } else {
                JobDetail jobDetail = newJob(InternalJob.class).withIdentity(name, group)
                    .usingJobData(FlyerConstants.dataKey, JsonMapper.INSTANCE.toJson(flyerJobs))
                    .storeDurably(true).build();
                //将任务添加至调度器
                scheduler.addJob(jobDetail, false);
                scheduler.triggerJob(jobDetail.getKey(), jobDataMap);
            }
        } catch (Exception e) {
            log.info("triggerJob error, jobId : {}", jobId, e);
        }
    }
}
