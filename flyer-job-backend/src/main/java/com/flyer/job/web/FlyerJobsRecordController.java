package com.flyer.job.web;

import com.flyer.job.common.BaseController;
import com.flyer.job.common.RestResponse;
import com.flyer.job.service.FlyerJobsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务日志controller
 */
@RestController
@RequestMapping("record")
public class FlyerJobsRecordController extends BaseController {

    @Autowired
    private FlyerJobsRecordService flyerJobsRecordService;

    @RequestMapping(value = "list")
    public RestResponse listpage(long jobId) {
        return flyerJobsRecordService.list(jobId);
    }

}
