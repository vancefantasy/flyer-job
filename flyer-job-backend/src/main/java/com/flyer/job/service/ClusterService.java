package com.flyer.job.service;


import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.ServerConfig;

/**
 * Created by jianying.li on 2018/3/8.
 */
public interface ClusterService {

    String codeOfSettingsInDict = "flyer.server.settings";

    RestResponse show();

    RestResponse save(String servers);

    ServerConfig getCurrentConfig();
}
