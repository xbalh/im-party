package com.im.imparty.config.security.filter;

import com.im.imparty.config.security.authentication.LoginJwtToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

// @Component
public class LoginVerifyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public LoginVerifyAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginJwtToken loginJwtToken = new LoginJwtToken( Collections.emptyList(), "username", "123");
        loginJwtToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
        return this.getAuthenticationManager().authenticate(loginJwtToken);
    }
}
