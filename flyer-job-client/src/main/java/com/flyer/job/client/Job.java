package com.flyer.job.client;

/**
 * Created by jianying.li on 2018/2/3.
 */
public interface Job {

    /**
     * 应用端实现此方法，定义自己的业务逻辑
     *
     * @param param
     * @throws InterruptedException
     */
    FlyerResult execute(String param) throws Exception;

    /**
     * 任务执行内部封装，扩展用
     *
     * @param param
     * @return
     * @throws Exception
     */
    FlyerResult executeInternal(String param) throws Exception;

    /**
     * 获取执行任务的beanId
     *
     * @return
     */
    String getJobBeanId();
}
