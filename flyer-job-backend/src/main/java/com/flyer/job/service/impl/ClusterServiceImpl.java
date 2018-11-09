package com.flyer.job.service.impl;

import com.flyer.job.common.RestResponse;
import com.flyer.job.dao.FlyerDictDao;
import com.flyer.job.domain.FlyerDict;
import com.flyer.job.domain.ServerConfig;
import com.flyer.job.service.ClusterService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private FlyerDictDao flyerDictDao;

    @Override
    public RestResponse show() {
        FlyerDict dict = flyerDictDao.findFlyerDictByCode(codeOfSettingsInDict);
        ServerConfig config = null;
        if (dict != null) {
            config = JsonMapper.INSTANCE.fromJson(dict.getFieldValue(), ServerConfig.class);
            config.setLastUpdateTime(dict.getTimestamp());
        }
        return RestResponse.success(config).build();
    }

    @Override
    public RestResponse save(String servers) {
        FlyerDict dict = flyerDictDao.findFlyerDictByCode(codeOfSettingsInDict);
        FlyerDict newOne = new FlyerDict();
        //for new
        if (dict == null) {
            newOne.setFieldCode(codeOfSettingsInDict);
            newOne.setCreateTime(new Date());
            newOne.setFieldDesc("flyer server配置");
            newOne.setFieldType(1);

            ServerConfig config = new ServerConfig();
            config.setServers(servers);
            config.setVersion(0);
            newOne.setFieldValue(JsonMapper.INSTANCE.toJson(config));

            flyerDictDao.saveFlyerDict(newOne);
        }//for update
        else {
            ServerConfig config =
                JsonMapper.INSTANCE.fromJson(dict.getFieldValue(), ServerConfig.class);
            int currentVersion = config.getVersion();
            config.setServers(servers);
            config.setVersion(++currentVersion);
            newOne.setId(dict.getId());
            newOne.setFieldValue(JsonMapper.INSTANCE.toJson(config));
            flyerDictDao.updateFlyerDict(newOne);
        }

        return RestResponse.success().build();
    }

    //版本号缓存
    LoadingCache<String, ServerConfig> serverConfigCache =
        CacheBuilder.newBuilder().maximumSize(1).refreshAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ServerConfig>() {
                @Override
                public ServerConfig load(String s) throws Exception {
                    FlyerDict dict = flyerDictDao.findFlyerDictByCode(codeOfSettingsInDict);
                    ServerConfig config =
                        JsonMapper.INSTANCE.fromJson(dict.getFieldValue(), ServerConfig.class);
                    return config;
                }
            });

    @Override
    public ServerConfig getCurrentConfig() {
        return serverConfigCache.getUnchecked(codeOfSettingsInDict);
    }

}
