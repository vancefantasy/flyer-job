package com.flyer.job.dao;

import com.flyer.job.domain.FlyerJobsLog;

import java.util.List;

public interface FlyerJobsLogDao {

    Integer saveFlyerJobsLog(FlyerJobsLog flyerJobsLog);

    Integer updateFlyerJobsLog(FlyerJobsLog flyerJobsLog);

    Integer removeFlyerJobsLog(Long id);

    FlyerJobsLog findFlyerJobsLogById(Long id);

    List<FlyerJobsLog> listByRecordId(Long recordId);

    Long calcCount(String jobBeanId);

    Integer removeByLimit(String jobBeanId);
}



