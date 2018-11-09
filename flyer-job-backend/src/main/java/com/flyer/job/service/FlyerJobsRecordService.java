package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerJobsRecord;

public interface FlyerJobsRecordService {
    FlyerJobsRecord saveFlyerJobsRecord(FlyerJobsRecord flyerJobsRecord);

    Integer removeFlyerJobsRecord(Long flyerJobsRecordId);

    Integer updateFlyerJobsRecord(FlyerJobsRecord flyerJobsRecord);

    FlyerJobsRecord findFlyerJobsRecordById(Long flyerJobsRecordId);

    RestResponse list(Long jobId);

    void clearRecords(String jobBeanId, long rmCount);
}

