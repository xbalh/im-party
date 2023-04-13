package com.im.imparty.common.exception;

public class JwtValidException extends JwtAuthenticationAbstractException{
    public JwtValidException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtValidException(String msg) {
        super(msg);
    }
}
