package com.im.imparty.common.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class RequestUtils {

    public static String getUserName() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication().getPrincipal().toString();
    }

}
