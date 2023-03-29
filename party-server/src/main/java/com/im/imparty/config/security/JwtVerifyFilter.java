package com.im.imparty.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtVerifyFilter extends BasicAuthenticationFilter {
    public JwtVerifyFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        Cookie cookie = Arrays.stream(cookies).filter(item -> "Token".equals(item.getName())).findAny().orElse(null);
        if (cookie != null || request.getHeader("Token") != null) {
            System.out.println("request.getHeader(\"Token\")" + request.getHeader("Token"));
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 用户可以访问的资源名称（或者说用户所拥有的权限） 注意：必须"ROLE_"开头
            authorities.add(new SimpleGrantedAuthority("user:resource"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                    ("admin", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            // 游客登录
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                    ("游客", null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
