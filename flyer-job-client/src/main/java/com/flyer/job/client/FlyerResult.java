package com.flyer.job.client;

/**
 * 任务执行结果
 */
public class FlyerResult {

    private Result result;

    private String message;

    public FlyerResult() {
    }

    public FlyerResult(Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public enum Result {
        SUCCESS(0, "sucess"), EXCEPTION(1, "exception"), CANCELD(2, "canceld"),
        OTHER(3, "other error");

        public final int code;

        public final String message;

        Result(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}
