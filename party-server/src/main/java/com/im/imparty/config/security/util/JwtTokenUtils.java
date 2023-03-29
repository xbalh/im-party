package com.im.imparty.config.security.util;

public class JwtTokenUtils {

    public static boolean isExpiration(String token) {
        return false;
    }

    public static String getUserID(String token) {
        return "root";
    }

}
