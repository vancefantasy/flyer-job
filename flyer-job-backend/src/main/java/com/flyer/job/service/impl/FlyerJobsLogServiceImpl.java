package com.flyer.job.service.impl;

import com.flyer.job.dao.FlyerJobsLogDao;
import com.flyer.job.domain.FlyerJobsLog;
import com.flyer.job.service.FlyerJobsLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 2018-01-24 14:54:12<br/>
 */
@Service
public class FlyerJobsLogServiceImpl implements FlyerJobsLogService {

    private final Logger log = LoggerFactory.getLogger(FlyerJobsLogServiceImpl.class);

    @Autowired
    private FlyerJobsLogDao flyerJobsLogDao;

    public FlyerJobsLog saveFlyerJobsLog(FlyerJobsLog flyerJobsLog) {
        FlyerJobsLog original = flyerJobsLog;
        int result = flyerJobsLogDao.saveFlyerJobsLog(flyerJobsLog);
        if (result == 0) {
            throw new RuntimeException("FlyerJobsLog save error result = 0");
        }
        return flyerJobsLog;
    }

    public Integer updateFlyerJobsLog(FlyerJobsLog flyerJobsLog) {
        FlyerJobsLog original = flyerJobsLog;
        return flyerJobsLogDao.updateFlyerJobsLog(flyerJobsLog);
    }

    public Integer removeFlyerJobsLog(Long flyerJobsLogId) {
        return flyerJobsLogDao.removeFlyerJobsLog(flyerJobsLogId);
    }

    public FlyerJobsLog findFlyerJobsLogById(Long flyerJobsLogId) {
        return flyerJobsLogDao.findFlyerJobsLogById(flyerJobsLogId);
    }

    @Override
    public void clearLogs(String jobBeanId, long rmCount) {
        long count = flyerJobsLogDao.calcCount(jobBeanId);
        //flyer_jobs_log保留不超过1000条记录（平均每个job）
        if (count <= 5000) {
            //如果job没有要删除的数据，就不打印日志了
            if (rmCount != 0){
                log.info("job: {} clear logs count: {}", jobBeanId, rmCount);
            }
            return;
        } else {
            rmCount += flyerJobsLogDao.removeByLimit(jobBeanId);
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("clearLogs interrupted");
            }
            //递归处理
            clearLogs(jobBeanId, rmCount);
        }
    }

}

