package com.im.imparty.common.exception;

import com.im.imparty.web.vo.BaseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtValidException extends JwtAuthenticationAbstractException{
    public JwtValidException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtValidException(String msg) {
        super(msg);
    }

    @Override
    public BaseResult getResult() {
        return BaseResult.build(1001, "登录错误，请重新登录！");
    }
}
