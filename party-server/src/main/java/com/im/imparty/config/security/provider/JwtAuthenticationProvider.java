package com.im.imparty.config.security.provider;

import com.auth0.jwt.interfaces.Claim;
import com.im.imparty.common.util.JwtTokenUtils;
import com.im.imparty.config.security.authentication.LoginJwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    public JwtAuthenticationProvider() {
        System.out.println("JwtAuthenticationProvider");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginJwtToken jwtToken = (LoginJwtToken) authentication;
        if (!jwtToken.getExpireTime().isBefore(Instant.now())) {
            return authentication;
        }
        // token过期了
        Map<String, Claim> stringClaimMap = JwtTokenUtils.decryptJwt(((LoginJwtToken) authentication).getRefreshToken());
        Instant exp = stringClaimMap.get("exp").asInstant();
        if (!exp.isBefore(Instant.now())) {
            log.warn("token已经过期了，理应刷新token");
            // TODO 刷新token
            return authentication;
        }
        throw new CredentialsExpiredException("验证信息已经过期");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginJwtToken.class.isAssignableFrom(authentication);
    }
}
