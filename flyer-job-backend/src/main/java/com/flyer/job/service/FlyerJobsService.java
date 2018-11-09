package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerJobs;

import java.util.List;

public interface FlyerJobsService {

    RestResponse saveFlyerJobs(FlyerJobs flyerJobs);

    RestResponse removeFlyerJobs(Long flyerJobsId);

    RestResponse updateFlyerJobs(FlyerJobs flyerJobs);

    FlyerJobs findFlyerJobsById(Long flyerJobsId);

    RestResponse listpage(int page, String jobName, String jobBeanId, String appCode);

    RestResponse batchRemove(String ids);

    RestResponse operateFlyerJobs(long id, int status);

    RestResponse runOnce(Long jobId, String connId, String operateUser);

    RestResponse clients(Long id);

    RestResponse cancelJob(Long id, String operateUser);

    List<FlyerJobs> listAll();
}

