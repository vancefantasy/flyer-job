package com.flyer.job.core;

import com.flyer.job.constants.FlyerConstants;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * flyer启动后，初始化一些内部任务
 */
@Component
public class InitRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(InitRunner.class);

    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... strings) throws Exception {
        loadClearLogJob();
    }

    /**
     * 加载清理日志任务
     *
     * @throws Exception
     */
    private void loadClearLogJob() throws Exception {
        JobKey jobKey = new JobKey(FlyerConstants.nameOfClearLog, FlyerConstants.groupOfInternal);
        //job不存在时，提交job
        //由于开启了Job持久化，理论上自上线之后，只会加载一次，之后重启也不会提交
        if (!scheduler.checkExists(jobKey)) {
            JobDetail jobDetail = newJob(ClearLogJob.class)
                .withIdentity(jobKey)
                .storeDurably().build();
            //每天2:00执行一次清理
            Trigger trigger = newTrigger()
                .withIdentity(FlyerConstants.nameOfClearLog, FlyerConstants.groupOfInternal)
                .withSchedule(cronSchedule("0 0 2 * * ?")).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
        log.info("loadClearLogJob ok");
    }

}
