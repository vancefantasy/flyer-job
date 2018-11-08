package com.flyer.job.service;

import com.flyer.job.domain.FlyerDict;

/**
 * 2018-03-08 11:49:04 <br/>
 */
public interface FlyerDictService {

    FlyerDict saveFlyerDict(FlyerDict flyerDict);

    Integer removeFlyerDict(Long flyerDictId);

    Integer updateFlyerDict(FlyerDict flyerDict);

    FlyerDict findFlyerDictById(Long flyerDictId);

}

