package com.flyer.job.common.utils;

import com.flyer.job.common.BeanFactoryRegister;
import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.domain.FlyerJobsLog;
import com.flyer.job.domain.FlyerJobsRecord;
import com.flyer.job.service.FlyerJobsLogService;
import com.flyer.job.service.FlyerJobsRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

/**
 * 日志记录工具类
 */
public class JobLogUtils {

    @Async
    public static void addJobRecord(FlyerJobs flyerJobs) {
        FlyerJobsRecord record = new FlyerJobsRecord();
        record.setId(flyerJobs.getRecordId());
        record.setAppCode(flyerJobs.getAppCode());
        record.setJobBeanId(flyerJobs.getJobBeanId());
        record.setVhost(flyerJobs.getVhost());
        record.setStatus(0);

        if (flyerJobs.getExecuteType() != null) {
            record.setExecuteType(flyerJobs.getExecuteType());
        } else {
            record.setExecuteType(FlyerConstants.executeTypeAuto);
        }

        if (StringUtils.isNoneBlank(flyerJobs.getOperateUser())) {
            record.setOperateUser(flyerJobs.getOperateUser());
        } else {
            record.setOperateUser(FlyerConstants.operateUserSystem);
        }

        record.setCreateTime(new Date());
        FlyerJobsRecordService flyerJobsRecordService =
            BeanFactoryRegister.getBean(FlyerJobsRecordService.class);
        flyerJobsRecordService.saveFlyerJobsRecord(record);
    }

    @Async
    public static void addJobsLog(long recordId, int logLevel, String jobBeanId,
        String logText) {
        FlyerJobsLogService flyerJobsLogService =
            BeanFactoryRegister.getBean(FlyerJobsLogService.class);
        FlyerJobsLog flyerJobsLog = new FlyerJobsLog();
        flyerJobsLog.setCreateTime(new Date());
        flyerJobsLog.setRecordId(recordId);
        flyerJobsLog.setLogLevel(logLevel);
        flyerJobsLog.setJobBeanId(jobBeanId);
        flyerJobsLog.setLogText(logText);
        flyerJobsLogService.saveFlyerJobsLog(flyerJobsLog);
    }



}
