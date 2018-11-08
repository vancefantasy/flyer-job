package com.flyer.job.web;

import com.flyer.job.common.RestResponse;
import com.flyer.job.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jianying.li on 2018/3/8.
 */
@RestController
@RequestMapping("cluster")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @RequestMapping(value = "show")
    public RestResponse show() {
        return clusterService.show();
    }

    @RequestMapping(value = "save")
    public RestResponse save(String servers) {
        return clusterService.save(servers);
    }

}
