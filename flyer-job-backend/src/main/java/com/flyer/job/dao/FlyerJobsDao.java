package com.flyer.job.dao;

import com.flyer.job.domain.FlyerJobs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlyerJobsDao {

    Integer saveFlyerJobs(FlyerJobs flyerJobs);

    Integer updateFlyerJobs(FlyerJobs flyerJobs);

    Integer removeFlyerJobsById(Long id);

    FlyerJobs findFlyerJobsById(Long id);

    FlyerJobs findByJobBeanId(String jobBeanId);

    void batchRemoveByIds(@Param("ids") String[] ids);

    List<FlyerJobs> searchPageList(@Param("jobName") String jobName,
        @Param("jobBeanId") String jobBeanId, @Param("appCode") String appCode);

    List<FlyerJobs> listAll();
}



