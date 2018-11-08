package com.flyer.job.dao;


import com.flyer.job.domain.FlyerDict;

/**
 * 2018-03-08 11:49:04<br/>
 */
public interface FlyerDictDao {

    Integer saveFlyerDict(FlyerDict flyerDict);

    Integer updateFlyerDict(FlyerDict flyerDict);

    Integer updateFlyerDictByCode(FlyerDict flyerDict);

    Integer removeFlyerDictById(Long id);

    FlyerDict findFlyerDictById(Long id);

    FlyerDict findFlyerDictByCode(String fieldCode);

}



