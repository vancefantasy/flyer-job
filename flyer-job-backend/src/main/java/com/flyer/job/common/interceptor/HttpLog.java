package com.flyer.job.common.interceptor;

import com.flyer.job.common.RestResponse;

import java.util.Map;

public class HttpLog {

    private Map general;

    private Map parameters;

    private String requestBody;

    private RestResponse responseBody;

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public Map getGeneral() {
        return general;
    }

    public void setGeneral(Map general) {
        this.general = general;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public RestResponse getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(RestResponse responseBody) {
        this.responseBody = responseBody;
    }
}
