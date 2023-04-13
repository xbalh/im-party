package com.im.imparty.common.exception;

import com.im.imparty.web.vo.BaseResult;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class JwtAuthenticationAbstractException extends AuthenticationException implements JwtAuthenticationExceptionHandler {

    public JwtAuthenticationAbstractException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationAbstractException(String msg) {
        super(msg);
    }

    @Override
    public void handler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(getResult().toJSONString());
    }

    public abstract BaseResult getResult();


}
