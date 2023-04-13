package com.im.imparty.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JwtAuthenticationExceptionHandler {

    void handler(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
