package com.im.imparty.common.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.im.imparty.common.exception.JwtExpiredException;
import com.im.imparty.common.exception.JwtValidException;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class JwtTokenUtils {

    private static final Algorithm JWT_SIGN = Algorithm.HMAC256(DigestUtils.md5Digest("im-imparty".getBytes(StandardCharsets.UTF_8)));

    private static volatile JWTVerifier jwtVerifier = null;


    public static JWTVerifier getJwtVerifier() {
        if (jwtVerifier == null) {
            synchronized (JWTVerifier.class) {
                if (jwtVerifier == null) {
                    jwtVerifier = JWT.require(JWT_SIGN).build();
                }
            }
        }
        return jwtVerifier;
    }


    public static boolean isExpiration(String token) {
        return false;
    }

    public static String getUserID(String token) {
        return "root";
    }

    public static String encryptTokenJwt(JSONObject info, String userName, String randomStr) {
        return JWT.create()
                .withClaim("userName", userName)
                .withClaim("validStr", randomStr)
                .withClaim("userInfo", info.toJSONString())
                .withClaim("expiredTime", LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.UTC))
                .sign(JWT_SIGN);
    }



    public static String encryptRefreshTokenJwt(JSONObject info, String userName, String randomStr) {
        return JWT.create()
                .withClaim("validStr", randomStr)
                .withClaim("userName", userName)
                .withClaim("userInfo", info.toJSONString())
                .withClaim("expiredTime", LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC))
                .sign(JWT_SIGN);
    }

    public static Map<String, Claim> decryptJwt(String token) {
        DecodedJWT verify = null;
        try {
            verify = getJwtVerifier().verify(token);
        } catch (TokenExpiredException e) {
            throw new JwtExpiredException("cookie已经过期", e);
        } catch (Exception e) {
            throw new JwtValidException("cookie有问题！", e);
        }
        return verify.getClaims();
    }

}
