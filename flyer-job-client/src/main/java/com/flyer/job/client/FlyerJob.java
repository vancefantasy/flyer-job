package com.flyer.job.client;

/**
 * Created by jianying.li on 2018/1/31.
 */
public abstract class FlyerJob implements Job {

    public abstract FlyerResult execute(String param) throws Exception;

    @Override
    public FlyerResult executeInternal(String param) throws Exception {
        return execute(param);
    }

    @Override
    public String getJobBeanId() {
        String[] arr = getClass().toString().split("\\.");
        String className = arr[arr.length - 1];
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
