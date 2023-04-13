package com.im.imparty.spring.authentication;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;


@Setter
@Getter
public class LoginJwtToken extends AbstractAuthenticationToken {

    private String jwtString;

    private String nickName;

    private String userName;

    private Instant expireTime;

    private String validStr;

    private Object principal = null;

    private Object credentials;

    private String refreshToken;

    public LoginJwtToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    public LoginJwtToken(Collection<? extends GrantedAuthority> authorities, String jwtString) {
        super(authorities);
        this.jwtString = jwtString;
    }
}
