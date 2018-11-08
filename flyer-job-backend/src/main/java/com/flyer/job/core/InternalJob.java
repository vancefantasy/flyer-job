package com.flyer.job.core;

import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.common.utils.JobLogUtils;
import com.flyer.job.common.UID;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 核心任务定义类
 * Created by jianying.li on 2018/1/28.
 */
public class InternalJob implements Job {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //遵守http://www.quartz-scheduler.org/documentation/best-practices.html#jobs
        try {
            excuteInternal(context);
        } catch (Exception e) {
            log.info("InternalJob error", e);
        }
    }

    private void excuteInternal(JobExecutionContext context) throws Exception {
        //获取JobDetail的上下文信息
        String data = context.getMergedJobDataMap().getString(FlyerConstants.dataKey);
        if (StringUtils.isBlank(data)) {
            log.error("InternalJob execute error, job data is null");
            return;
        }

        //手动运行情况，可能指定了客户端通道
        String connId = context.getMergedJobDataMap().getString(FlyerConstants.connId);

        //记录执行动作
        FlyerJobs flyerJobs = JsonMapper.INSTANCE.fromJson(data, FlyerJobs.class);
        flyerJobs.setRecordId(UID.next());
        JobLogUtils.addJobRecord(flyerJobs);

        log.debug("jobId : {} triggered, param data : {}", flyerJobs.getId(),
            flyerJobs.getParamData());

        //分发任务至client
        FlyerService.sendExecuteJob(flyerJobs, connId);
    }
}
