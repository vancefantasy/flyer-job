package com.flyer.job.service.impl;

import com.flyer.job.common.RestResponse;
import com.flyer.job.constants.ExecuteType;
import com.flyer.job.constants.LogLevel;
import com.flyer.job.dao.FlyerJobsDao;
import com.flyer.job.dao.FlyerJobsLogDao;
import com.flyer.job.dao.FlyerJobsRecordDao;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.domain.FlyerJobsLog;
import com.flyer.job.domain.FlyerJobsRecord;
import com.flyer.job.service.FlyerJobsRecordService;
import com.flyer.job.web.resp.LogRecords;
import com.flyer.job.common.utils.DateUtil;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;



/**
 * 2018-02-12 18:10:57<br/>
 */
@Service
public class FlyerJobsRecordServiceImpl implements FlyerJobsRecordService {

    private final Logger log = LoggerFactory.getLogger(FlyerJobsRecordServiceImpl.class);

    @Autowired
    private FlyerJobsRecordDao flyerJobsRecordDao;

    @Autowired
    private FlyerJobsLogDao flyerJobsLogDao;

    @Autowired
    private FlyerJobsDao flyerJobsDao;

    public FlyerJobsRecord saveFlyerJobsRecord(FlyerJobsRecord flyerJobsRecord) {
        int result = flyerJobsRecordDao.saveFlyerJobsRecord(flyerJobsRecord);
        if (result == 0) {
            throw new RuntimeException("FlyerJobsRecord save error result = 0");
        }
        return flyerJobsRecord;
    }

    public Integer updateFlyerJobsRecord(FlyerJobsRecord flyerJobsRecord) {
        return flyerJobsRecordDao.updateFlyerJobsRecord(flyerJobsRecord);
    }

    public Integer removeFlyerJobsRecord(Long flyerJobsRecordId) {
        return flyerJobsRecordDao.removeFlyerJobsRecord(flyerJobsRecordId);
    }

    public FlyerJobsRecord findFlyerJobsRecordById(Long flyerJobsRecordId) {
        return flyerJobsRecordDao.findFlyerJobsRecordById(flyerJobsRecordId);
    }

    @Override
    public RestResponse list(Long jobId) {
        FlyerJobs flyerJobs = flyerJobsDao.findFlyerJobsById(jobId);

        if (flyerJobs == null) {
            throw new RuntimeException("flyerJobs is not exists, id" + jobId);
        }

        List<FlyerJobsRecord> list = flyerJobsRecordDao
            .listRecent(flyerJobs.getAppCode(), flyerJobs.getJobBeanId(), flyerJobs.getVhost());
        List<LogRecords> voList = new ArrayList<>();
        for (FlyerJobsRecord record : list) {
            LogRecords vo = new LogRecords();
            vo.setId(record.getId());
            vo.setTitle(DateUtil.format(record.getCreateTime(), DateUtil.FORMAT_DATETIME) + " "
                + ExecuteType.getText(record.getExecuteType()));
            List<String> logs = new ArrayList<>();
            List<FlyerJobsLog> logList = flyerJobsLogDao.listByRecordId(record.getId());
            for (FlyerJobsLog log : logList) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(DateUtil.format(log.getCreateTime(), "yyyy-MM-dd HH:mm:ss.SSS"));
                sb.append("] ");
                sb.append(LogLevel.getName(log.getLogLevel()));
                sb.append(" ");
                sb.append(log.getLogText());
                logs.add(sb.toString());
            }
            vo.setLogs(logs);
            voList.add(vo);
        }

        //最近10次运行时间
        List<String> triggerTimes = new ArrayList<>();
        try {
            CronExpression cron = new CronExpression(flyerJobs.getCronExpression());
            recursionPrint(triggerTimes, cron, new Date(), 1);
        } catch (ParseException e) {
            //表达式不合法
            triggerTimes.add("CronExpression '" + flyerJobs.getCronExpression() + "' Illegal");
        }

        Map map = new HashMap<>();
        map.put("records", voList);
        map.put("triggerTimes", triggerTimes);

        return RestResponse.success(map).build();
    }


    private static void recursionPrint(List<String> triggerTimes, CronExpression cron, Date date,
        int num) {
        if (num > 10) {
            return;
        } else {
            Date next = cron.getNextValidTimeAfter(date);
            if (next != null) {
                triggerTimes.add(DateUtil.format(next, DateUtil.FORMAT_DATETIME));
                recursionPrint(triggerTimes, cron, next, ++num);
            }
        }
    }

    @Override
    public void clearRecords(String jobBeanId, long rmCount) {
        long count = flyerJobsRecordDao.calcCount(jobBeanId);
        //flyer_jobs_record保留不超过1000条记录（平均每个job）
        if (count < 1500) {
            //如果job没有要删除的数据，就不打印日志了
            if (rmCount != 0) {
                log.info("job: {} clear record count: {}", jobBeanId, rmCount);
            }
            return;
        } else {
            rmCount += flyerJobsRecordDao.removeByLimit(jobBeanId);
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("clearRecords interrupted");
            }
            clearRecords(jobBeanId, rmCount);
        }

    }



}

