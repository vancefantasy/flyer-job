package com.flyer.job.common.interceptor;

import org.springframework.core.NamedThreadLocal;


public class HttpLogHolder {
    private static NamedThreadLocal<HttpLog> threadLocal =
        new NamedThreadLocal<>("HttpLogHolder");

    public static void set(HttpLog httpLog) {
        threadLocal.set(httpLog);
    }

    public static HttpLog get() {
        return threadLocal.get();
    }

    public static void clean() {
        threadLocal.remove();
    }
}
