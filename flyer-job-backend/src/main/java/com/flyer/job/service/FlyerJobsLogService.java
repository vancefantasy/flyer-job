package com.flyer.job.service;

import com.flyer.job.domain.FlyerJobsLog;

public interface FlyerJobsLogService {
    FlyerJobsLog saveFlyerJobsLog(FlyerJobsLog flyerJobsLog);

    Integer removeFlyerJobsLog(Long flyerJobsLogId);

    Integer updateFlyerJobsLog(FlyerJobsLog flyerJobsLog);

    FlyerJobsLog findFlyerJobsLogById(Long flyerJobsLogId);

    void clearLogs(String jobBeanId, long rmCount);
}

