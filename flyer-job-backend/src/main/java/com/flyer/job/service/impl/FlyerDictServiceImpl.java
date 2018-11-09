package com.flyer.job.service.impl;

import com.flyer.job.dao.FlyerDictDao;
import com.flyer.job.domain.FlyerDict;
import com.flyer.job.service.FlyerDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlyerDictServiceImpl implements FlyerDictService {

    @Autowired
    private FlyerDictDao flyerDictDao;

    public FlyerDict saveFlyerDict(FlyerDict flyerDict) {
        int result = flyerDictDao.saveFlyerDict(flyerDict);
        if (result == 0) {
            throw new RuntimeException("FlyerDict save error result = 0");
        }
        return flyerDict;
    }

    public Integer updateFlyerDict(FlyerDict flyerDict) {
        return flyerDictDao.updateFlyerDict(flyerDict);
    }

    public Integer removeFlyerDict(Long flyerDictId) {
        return flyerDictDao.removeFlyerDictById(flyerDictId);
    }

    public FlyerDict findFlyerDictById(Long flyerDictId) {
        return flyerDictDao.findFlyerDictById(flyerDictId);
    }

}
