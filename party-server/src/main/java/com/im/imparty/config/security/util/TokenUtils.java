package com.im.imparty.config.security.util;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Token");
        return token;
    }

}
