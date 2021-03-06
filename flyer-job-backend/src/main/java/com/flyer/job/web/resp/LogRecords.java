package com.flyer.job.web.resp;

import java.util.List;

/**
 * 日志信息
 */
public class LogRecords {

    private long id;

    private String title;

    private List<String> logs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
