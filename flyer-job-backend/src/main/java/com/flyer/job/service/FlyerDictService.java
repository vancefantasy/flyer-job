package com.flyer.job.service;

import com.flyer.job.domain.FlyerDict;

public interface FlyerDictService {

    FlyerDict saveFlyerDict(FlyerDict flyerDict);

    Integer removeFlyerDict(Long flyerDictId);

    Integer updateFlyerDict(FlyerDict flyerDict);

    FlyerDict findFlyerDictById(Long flyerDictId);

}

