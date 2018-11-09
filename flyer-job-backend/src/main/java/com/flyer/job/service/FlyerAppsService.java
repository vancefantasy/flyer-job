package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerApps;

public interface FlyerAppsService {

    RestResponse saveFlyerApps(FlyerApps flyerApps);

    RestResponse removeFlyerApps(Long flyerAppsId);

    RestResponse updateFlyerApps(FlyerApps flyerApps);

    FlyerApps findFlyerAppsById(Long flyerAppsId);

    RestResponse listpage(int page, String appName);

    RestResponse batchRemove(String ids);

    RestResponse list();
}

