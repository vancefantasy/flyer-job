package com.flyer.job.dao;

import com.flyer.job.domain.FlyerApps;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlyerAppsDao {

    Integer saveFlyerApps(FlyerApps flyerApps);

    Integer updateFlyerApps(FlyerApps flyerApps);

    Integer removeFlyerAppsById(Long id);

    FlyerApps findFlyerAppsById(Long id);

    List<FlyerApps> searchPageList(@Param("appName") String appName);

    int batchRemoveByIds(@Param("ids") String[] ids);

    List getAllFlyerApps();
}



