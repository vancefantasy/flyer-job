package com.flyer.job.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LogLevel {

    INFO(1), WARN(2), ERROR(3);

    public final int code;

    LogLevel(int code) {
        this.code = code;
    }

    private final static Logger log = LoggerFactory.getLogger(LogLevel.class);

    public static String getName(int code) {
        for (LogLevel logLevel : LogLevel.values()) {
            if (logLevel.code == code) {
                return logLevel.name();
            }
        }
        log.warn("unknow logName with code : {}", code);
        return "未知类型";
    }

}
