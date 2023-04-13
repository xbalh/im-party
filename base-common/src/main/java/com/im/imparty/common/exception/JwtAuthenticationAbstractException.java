package com.im.imparty.common.exception;

import org.springframework.security.core.AuthenticationException;

public abstract class JwtAuthenticationAbstractException extends AuthenticationException {
    public JwtAuthenticationAbstractException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationAbstractException(String msg) {
        super(msg);
    }
}
