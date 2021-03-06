package com.flyer.job.web;

import com.flyer.job.common.BaseController;
import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.service.FlyerJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 任务管理controller
 */
@RestController
@RequestMapping("flyerjob")
public class FlyerJobsController extends BaseController {

    @Autowired
    private FlyerJobsService flyerJobsService;

    @RequestMapping(value = "listpage")
    public RestResponse listpage(int page, String jobName, String jobBeanId, String appCode) {
        return flyerJobsService.listpage(page, jobName, jobBeanId, appCode);
    }

    @RequestMapping(value = "add")
    public RestResponse add(FlyerJobs flyerJobs) {
        return flyerJobsService.saveFlyerJobs(flyerJobs);
    }

    @RequestMapping(value = "edit")
    public RestResponse edit(@RequestBody FlyerJobs flyerJobs) {
        return flyerJobsService.updateFlyerJobs(flyerJobs);
    }

    @RequestMapping(value = "remove")
    public RestResponse remove(long id) {
        return flyerJobsService.removeFlyerJobs(id);
    }

    @RequestMapping(value = "batchremove")
    public RestResponse batchremove(String ids) {
        return flyerJobsService.batchRemove(ids);
    }

    @RequestMapping(value = "operate")
    public RestResponse operate(long id, int status) {
        return flyerJobsService.operateFlyerJobs(id, status);
    }

    @RequestMapping(value = "runnow")
    public RestResponse runnow(HttpServletRequest request, Long jobId, String connId) {
        return flyerJobsService.runOnce(jobId, connId, getLoginUser(request));
    }

    @RequestMapping(value = "clients")
    public RestResponse clients(long id) {
        return flyerJobsService.clients(id);
    }

    @RequestMapping(value = "cancel")
    public RestResponse cancel(HttpServletRequest request, long id) {
        return flyerJobsService.cancelJob(id, getLoginUser(request));
    }

}
