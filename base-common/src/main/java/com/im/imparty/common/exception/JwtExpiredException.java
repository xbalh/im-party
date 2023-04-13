package com.im.imparty.common.exception;

public class JwtExpiredException extends JwtAuthenticationAbstractException
{
    public JwtExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtExpiredException(String msg) {
        super(msg);
    }
}
