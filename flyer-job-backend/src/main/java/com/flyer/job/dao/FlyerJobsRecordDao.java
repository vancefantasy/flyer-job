package com.flyer.job.dao;

import com.flyer.job.domain.FlyerJobsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 2018-02-12 18:10:57<br/>
 */
public interface FlyerJobsRecordDao {

    Integer saveFlyerJobsRecord(FlyerJobsRecord flyerJobsRecord);

    Integer updateFlyerJobsRecord(FlyerJobsRecord flyerJobsRecord);

    Integer removeFlyerJobsRecord(Long id);

    FlyerJobsRecord findFlyerJobsRecordById(Long id);

    List<FlyerJobsRecord> listRecent(@Param("appCode") String appCode,
        @Param("jobBeanId") String jobBeanId, @Param("vhost") String vhost);

    Long calcCount(String jobBeanId);

    Integer removeByLimit(String jobBeanId);
}



