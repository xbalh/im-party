package com.im.imparty.common.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException{

    private Integer code;

    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = 500;
    }

    public ServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }
}
