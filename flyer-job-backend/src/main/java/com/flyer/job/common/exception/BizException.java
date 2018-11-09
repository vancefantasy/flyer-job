package com.flyer.job.common.exception;


import com.flyer.job.constants.ErrorEnum;

/**
 * User-defined Exception
 */
public class BizException extends RuntimeException {

    /**
     * error info
     */
    private ErrorEnum error;

    public BizException(ErrorEnum error) {
        this.error = error;
    }

    public ErrorEnum getError() {
        return error;
    }

    public static void throwOut(ErrorEnum error) {
        throw new BizException(error);
    }

}
