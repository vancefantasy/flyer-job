package com.flyer.job.web;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerApps;
import com.flyer.job.service.FlyerAppsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jianying.li on 2018/1/24.
 */
@RestController
@RequestMapping("app")
public class FlyerAppsController {

    @Autowired
    private FlyerAppsService flyerAppsService;

    @RequestMapping(value = "listpage")
    public RestResponse listpage(int page, String appName) {
        return flyerAppsService.listpage(page, appName);
    }

    @RequestMapping(value = "add")
    public RestResponse add(FlyerApps flyerApps) {
        return flyerAppsService.saveFlyerApps(flyerApps);
    }

    @RequestMapping(value = "edit")
    public RestResponse edit(@RequestBody FlyerApps flyerApps) {
        return flyerAppsService.updateFlyerApps(flyerApps);
    }

    @RequestMapping(value = "remove")
    public RestResponse remove(long id) {
        return flyerAppsService.removeFlyerApps(id);
    }

    @RequestMapping(value = "batchremove")
    public RestResponse batchremove(String ids) {
        return flyerAppsService.batchRemove(ids);
    }

    @RequestMapping(value = "list")
    public RestResponse list() {
        return flyerAppsService.list();
    }

}
