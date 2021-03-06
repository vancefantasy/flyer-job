package com.flyer.job.example.jobs;

import com.flyer.job.client.FlyerJob;
import com.flyer.job.client.FlyerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldJob extends FlyerJob {

    private final static Logger log = LoggerFactory.getLogger(HelloWorldJob.class);

    @Override
    public FlyerResult execute(String param) throws Exception {
        log.info("HelloWorldJob run, param : {}", param);
        return new FlyerResult(FlyerResult.Result.SUCCESS, "run ok");
    }
}
