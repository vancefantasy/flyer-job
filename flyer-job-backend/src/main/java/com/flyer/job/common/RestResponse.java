package com.flyer.job.common;

/**
 * 通用返回数据格式
 * Created by jianying.li on 2018/1/28.
 */
public class RestResponse {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAIL = -1;
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAIL = "fail";

    /**
     * ret: 返回码.
     */
    private int code;

    /**
     * msg:文本消息.
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    private RestResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private RestResponse(Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }

    public static Builder success() {
        return new Builder().code(CODE_SUCCESS).msg(MSG_SUCCESS);
    }

    public static Builder success(Object data) {
        return new Builder().code(CODE_SUCCESS).msg(MSG_SUCCESS).data(data);
    }

    public static Builder error() {
        return new Builder().code(CODE_FAIL).msg(MSG_FAIL);
    }

    public static class Builder {

        private int code;
        private String msg;
        private Object data;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder success() {
            this.code = CODE_SUCCESS;
            this.msg = MSG_SUCCESS;
            return this;
        }

        public Builder fail() {
            this.code = CODE_FAIL;
            this.msg = MSG_FAIL;
            return this;
        }

        public RestResponse build() {
            return new RestResponse(this);
        }

    }


}
