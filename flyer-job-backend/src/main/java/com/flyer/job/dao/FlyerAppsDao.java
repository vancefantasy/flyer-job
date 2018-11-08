package com.flyer.job.dao;

import com.flyer.job.domain.FlyerApps;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 2018-01-24 14:54:12<br/>
 */
public interface FlyerAppsDao {

    Integer saveFlyerApps(FlyerApps flyerApps);

    Integer updateFlyerApps(FlyerApps flyerApps);

    Integer removeFlyerAppsById(Long id);

    FlyerApps findFlyerAppsById(Long id);

    List<FlyerApps> searchPageList(@Param("appName") String appName);

    int batchRemoveByIds(@Param("ids") String[] ids);

    List getAllFlyerApps();
}



