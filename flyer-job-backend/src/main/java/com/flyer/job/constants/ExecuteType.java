package com.flyer.job.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类型
 */
public enum ExecuteType {

    AUTO(0, "任务执行"), MANUAL(1, "任务执行(手动)"), CANCEL(2, "任务取消");

    public final int code;

    public final String text;

    ExecuteType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    private final static Logger log = LoggerFactory.getLogger(ExecuteType.class);

    public static String getText(int code) {
        for (ExecuteType executeType : ExecuteType.values()) {
            if (executeType.code == code) {
                return executeType.text;
            }
        }
        log.warn("unknow executeType with code : {}", code);
        return "未知类型";
    }

}
