package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerApps;

/**
 * 2018-01-24 14:54:12 <br/>
 */
public interface FlyerAppsService {

    RestResponse saveFlyerApps(FlyerApps flyerApps);

    RestResponse removeFlyerApps(Long flyerAppsId);

    RestResponse updateFlyerApps(FlyerApps flyerApps);

    FlyerApps findFlyerAppsById(Long flyerAppsId);

    RestResponse listpage(int page, String appName);

    RestResponse batchRemove(String ids);

    RestResponse list();
}

