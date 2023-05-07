package com.im.imparty.common.exception;

public class CustomException extends RuntimeException {
    private int errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = 500;
    }

    public CustomException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}