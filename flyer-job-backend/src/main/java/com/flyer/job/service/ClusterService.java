package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.ServerConfig;

public interface ClusterService {

    String codeOfSettingsInDict = "flyer.server.settings";

    RestResponse show();

    RestResponse save(String servers);

    ServerConfig getCurrentConfig();
}
