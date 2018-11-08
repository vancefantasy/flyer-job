package com.flyer.job.client;

/**
 * 协议编号常量类
 * Created by jianying.li on 2018/1/30.
 */
public interface ProtocolConstants {

    /**
     * 版本号
     */
    short CURRENT_PROTOCOL_VERSION = 1;

    /**
     * client->flyer 获取flyer server拓扑结构
     */
    int PROTOCOL_ID_PULL_TOPOLOGY = 1000;

    /**
     * flyer->client 返回flyer server拓扑结构
     */
    int PROTOCOL_ID_PULL_TOPOLOGY_RESPONSE = 1001;

    /**
     * client->flyer 捂手
     */
    int PROTOCOL_ID_SHAKEHAND = 1002;

    /**
     * flyer->client 握手响应
     */
    int PROTOCOL_ID_SHAKEHAND_RESPONSE = 1003;

    /**
     * client->flyer 心跳
     */
    int PROTOCOL_ID_HEARTBEAT = 1004;

    /**
     * flyer->client 心跳响应
     */
    int PROTOCOL_ID_HEARTBEAT_RESPONSE = 1005;

    /**
     * flyer->client 任务执行
     */
    int PROTOCOL_ID_EXECUTE_JOB = 1006;

    /**
     * client->flyer 响应执行结果
     */
    int PROTOCOL_ID_EXECUTE_JOB_RESPONSE = 1007;

    /**
     * client->flyer 上报任务执行结果，日志
     */
    int PROTOCOL_ID_REPORT_JOB_RESULT = 1008;

    /**
     * flyer->client 返回
     */
    int PROTOCOL_ID_REPORT_JOB_RESULT_RESPONSE = 1009;

    /**
     * flyer->client 任务取消
     */
    int PROTOCOL_ID_CANCEL_JOB = 1010;

    /**
     * client->flyer 响应取消结果
     */
    int PROTOCOL_ID_CANCEL_JOB_RESPONSE = 1011;

    /**
     * client->flyer 向flyer提交外部任务
     */
    int PROTOCOL_ID_COMMIT_JOB = 1012;

    /**
     * flyer->client 响应提交结果
     */
    int PROTOCOL_ID_COMMIT_JOB_RESPONSE = 1013;

}
