package com.flyer.job.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @GetMapping(value = "healthcheck")
    public String healthCheck() {
        return "ok";
    }

}
