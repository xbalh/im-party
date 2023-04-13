package com.im.imparty.config.security.provider;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.im.imparty.common.exception.JwtExpiredException;
import com.im.imparty.common.exception.JwtValidException;
import com.im.imparty.common.util.JwtTokenUtils;
import com.im.imparty.spring.authentication.LoginJwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    public JwtAuthenticationProvider() {
        System.out.println("JwtAuthenticationProvider");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginJwtToken verify = null;
        try {
            verify = verify(authentication);
        } catch (Exception e) {
            return null;
        }
        if (verify.getExpireTime().isBefore(Instant.now())) {
            // 过期了 在需要验证refresh token
            LoginJwtToken jwtToken = (LoginJwtToken) authentication;
            String refreshToken = jwtToken.getRefreshToken();
            Map<String, Claim> refreshMap = JwtTokenUtils.decryptJwt(refreshToken);
            if (refreshMap.get("expiredTime").asInstant().isBefore(Instant.now())) {
                //throw new JwtExpiredException("cookie已经过期");
                return null;
            }
            String randomStr = refreshMap.get("randomStr").asString();
            if (StringUtils.equals(verify.getUserName(), randomStr)) {
                // 刷新token
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                HttpServletResponse response = requestAttributes.getResponse();
                JSONObject userInfo = refreshMap.get("userInfo").as(JSONObject.class);
                String userName = refreshMap.get("userName").asString();
                String newJwt = JwtTokenUtils.encryptTokenJwt(userInfo, userName, randomStr);
                response.setHeader("Authentication", String.format("Bearer %s", newJwt));
                response.addCookie(new Cookie("Authentication", newJwt));
            } else {
                //throw new JwtValidException("cookie有问题！");
                return null;
            }
        }
        return verify;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginJwtToken.class.isAssignableFrom(authentication);
    }

    private LoginJwtToken verify(Authentication authentication) {

        LoginJwtToken jwtToken = (LoginJwtToken) authentication;
        String jwtStr = jwtToken.getJwtString();
        // 验证jwt
        Map<String, Claim> jsonObject = JwtTokenUtils.decryptJwt(jwtStr);
        if (jsonObject.get("userName") == null) {
            throw  new JwtValidException("cookie信息错误");
        }
        String userName = jsonObject.get("userName").asString();

        if (jsonObject.get("exp") == null) {
            throw  new JwtValidException("cookie信息错误");
        }
        Instant exp = jsonObject.get("expiredTime").asInstant();

        if (jsonObject.get("userInfo") == null) {
            throw  new JwtValidException("cookie信息错误");
        }
        JSONObject userInfo = JSONObject.parseObject(jsonObject.get("userInfo").asString());
        String[] roleList = Optional.ofNullable(userInfo.getString("roleList")).orElse("").split(",");
        List<GrantedAuthority> authorityList = Arrays.stream(roleList).map(item -> (GrantedAuthority) () -> item).collect(Collectors.toList());
        LoginJwtToken loginJwtToken = new LoginJwtToken(authorityList, userName, jwtStr);
        loginJwtToken.setRefreshToken(jwtToken.getRefreshToken());
        loginJwtToken.setNickName(Optional.ofNullable(userInfo.getString("nickName")).orElse(userName));
        loginJwtToken.setExpireTime(exp);
        return loginJwtToken;
    }
}
