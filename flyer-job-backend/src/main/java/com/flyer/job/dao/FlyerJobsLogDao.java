package com.flyer.job.dao;


import com.flyer.job.domain.FlyerJobsLog;

import java.util.List;

/**
 * 2018-01-24 14:54:12<br/>
 */
public interface FlyerJobsLogDao {

    Integer saveFlyerJobsLog(FlyerJobsLog flyerJobsLog);

    Integer updateFlyerJobsLog(FlyerJobsLog flyerJobsLog);

    Integer removeFlyerJobsLog(Long id);

    FlyerJobsLog findFlyerJobsLogById(Long id);

    List<FlyerJobsLog> listByRecordId(Long recordId);

    Long calcCount(String jobBeanId);

    Integer removeByLimit(String jobBeanId);
}



