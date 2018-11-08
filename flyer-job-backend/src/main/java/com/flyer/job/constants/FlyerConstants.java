package com.flyer.job.constants;

/**
 * Created by jianying.li on 2018/1/30.
 */
public interface FlyerConstants {

    String dataKey = "flyerJobs";
    String connId = "connId";

    int jobStatusDefault = 0;
    int jobStatusStart = 1;
    int jobStatusDelete = 2;


    int executeTypeAuto = 0; //自动执行
    int executeTypeManual = 1;//手动执行
    int executeTypeCancel = 2;//任务取消

    String operateUserSystem = "flyer";

    int logLevelInfo = 1;
    int logLevelWarn = 2;
    int logLevelError = 3;

    String groupOfInternal = "flyerInternal";
    String nameOfClearLog = "clearLogJob";

    String keyOfProfile = "spring.profiles.active";
    String defaultActiveProfile = "dev"; //默认profile

    String sessionUser = "flyer_job_token";

}
