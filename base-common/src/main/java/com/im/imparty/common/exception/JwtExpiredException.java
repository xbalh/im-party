package com.im.imparty.common.exception;

import com.im.imparty.web.vo.BaseResult;

public class JwtExpiredException extends JwtAuthenticationAbstractException
{
    public JwtExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtExpiredException(String msg) {
        super(msg);
    }

    @Override
    public BaseResult getResult() {
        return BaseResult.build(1001, "登录已过期，请重新登录！");
    }
}
