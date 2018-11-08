package com.flyer.job.service.impl;

import com.flyer.job.common.RestResponse;
import com.flyer.job.dao.FlyerAppsDao;
import com.flyer.job.domain.FlyerApps;
import com.flyer.job.service.FlyerAppsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 2018-01-24 14:54:12<br/>
 */
@Service
public class FlyerAppsServiceImpl implements FlyerAppsService {

    @Autowired
    private FlyerAppsDao flyerAppsDao;

    public RestResponse saveFlyerApps(FlyerApps flyerApps) {
        flyerApps.setCreateTime(new Date());

        int result = flyerAppsDao.saveFlyerApps(flyerApps);
        if (result == 0) {
            throw new RuntimeException("FlyerApps save error result = 0");
        }
        return RestResponse.success(flyerApps.getId()).build();
    }

    public RestResponse updateFlyerApps(FlyerApps flyerApps) {
        int result = flyerAppsDao.updateFlyerApps(flyerApps);
        if (result == 0) {
            throw new RuntimeException("FlyerApps update error result = 0");
        }
        return RestResponse.success().build();
    }

    public RestResponse removeFlyerApps(Long flyerAppsId) {
        flyerAppsDao.removeFlyerAppsById(flyerAppsId);
        return RestResponse.success().build();
    }

    public FlyerApps findFlyerAppsById(Long flyerAppsId) {
        return flyerAppsDao.findFlyerAppsById(flyerAppsId);
    }

    @Override
    public RestResponse listpage(int page, String appName) {
        int pageSize = 10;
        PageHelper.startPage(page, pageSize);

        if (StringUtils.isNotBlank(appName)) {
            appName = "%" + appName + "%";
        }
        List<FlyerApps> list = flyerAppsDao.searchPageList(appName);
        PageInfo<FlyerApps> pageInfo = new PageInfo<>(list);
        return RestResponse.success(pageInfo).build();
    }

    @Override
    public RestResponse batchRemove(String ids) {
        flyerAppsDao.batchRemoveByIds(ids.split(","));
        return RestResponse.success().build();
    }

    @Override
    public RestResponse list() {
        List<FlyerApps> list = flyerAppsDao.getAllFlyerApps();
        List<Map> results = new ArrayList<>();
        for (FlyerApps app : list) {
            Map map = new HashMap();
            map.put("value", app.getAppCode());
            map.put("label", app.getAppCode());
            results.add(map);
        }
        return RestResponse.success(results).build();
    }

}

