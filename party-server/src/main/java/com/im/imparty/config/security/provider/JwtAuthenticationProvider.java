package com.im.imparty.config.security.provider;

import com.im.imparty.config.security.authentication.LoginJwtToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    public JwtAuthenticationProvider() {
        System.out.println("JwtAuthenticationProvider");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginJwtToken.class.isAssignableFrom(authentication);
    }
}
