package com.im.imparty.common.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class JwtTokenUtils {

    public static boolean isExpiration(String token) {
        return false;
    }

    public static String getUserID(String token) {
        return "root";
    }

    public static String encryptTokenJwt(JSONObject info, String userName, String salt) {
        return JWT.create()
                .withClaim("userName", userName)
                .withClaim("userInfo", info.toJSONString())
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC))
                .sign(Algorithm.HMAC256("im-imparty" + salt));
    }

    public static String encryptRefreshTokenJwt(String token, String salt) {
        return JWT.create()
                .withClaim("token", token)
                .withExpiresAt(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC))
                .sign(Algorithm.HMAC256("im-imparty" + salt));
    }

}
