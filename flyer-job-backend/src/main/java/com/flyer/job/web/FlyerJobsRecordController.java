package com.flyer.job.web;

import com.flyer.job.common.RestResponse;
import com.flyer.job.service.FlyerJobsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jianying.li on 2018/2/27.
 */
@RestController
@RequestMapping("record")
public class FlyerJobsRecordController {

    @Autowired
    private FlyerJobsRecordService flyerJobsRecordService;

    @RequestMapping(value = "list")
    public RestResponse listpage(long jobId) {
        return flyerJobsRecordService.list(jobId);
    }

}
