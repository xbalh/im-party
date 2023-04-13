package com.im.imparty.common.util;

import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static void remove(HttpServletResponse response, String cookieName) {
        Cookie authentication2 = new Cookie(cookieName, "");
        authentication2.setMaxAge(1);
        authentication2.setPath("/");
        response.addCookie(authentication2);
    }

}
