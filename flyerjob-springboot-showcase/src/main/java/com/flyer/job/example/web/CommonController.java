package com.flyer.job.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Common Controller
 * Created by jianying.li on 2018/9/14.
 */
@RestController
public class CommonController {

    /**
     * healthCheck
     *
     * @return
     */
    @GetMapping(value = "healthCheck")
    public String healthCheck() {
        return "ok";
    }

}
