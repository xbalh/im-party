package com.im.imparty.config.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.im.imparty.common.util.JwtTokenUtils;
import com.im.imparty.spring.authentication.LoginJwtToken;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class JwtVerifyAuthenticationFilter extends BasicAuthenticationFilter {
    public JwtVerifyAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Sec-WebSocket-Protocol不为空说明是ws请求
        String jwtStr = null;
        String refreshJwtStr = null;
        if (StringUtils.isNotBlank(request.getHeader("Sec-WebSocket-Protocol"))) {
            jwtStr = request.getHeader("Sec-WebSocket-Protocol");
            LoginJwtToken loginJwtToken = new LoginJwtToken(Collections.emptyList(), jwtStr);
            SecurityContextHolder.getContext().setAuthentication(loginJwtToken);
        } else {
            Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
            Cookie cookie = Arrays.stream(cookies).filter(item -> "Authentication".equals(item.getName())).findAny().orElse(null);
            Cookie refreshToken = Arrays.stream(cookies).filter(item -> "refreshToken".equals(item.getName())).findAny().orElse(null);

            String authentication = request.getHeader("Authentication");
            String refreshTokenHead = request.getHeader("refreshToken");
            if (cookie != null || authentication != null) {
                System.out.println("request.getHeader(\"Token\")" + authentication);
                System.out.println("cookie" + cookie.getValue());
                jwtStr = cookie == null || cookie.getValue() == null ? authentication.replace("Bearer  ", "") : cookie.getValue();
                refreshJwtStr = refreshToken == null || refreshToken.getValue() == null ? refreshTokenHead: refreshToken.getValue();

                LoginJwtToken loginJwtToken = new LoginJwtToken(Collections.emptyList(), jwtStr);
                loginJwtToken.setRefreshToken(refreshJwtStr);
                SecurityContextHolder.getContext().setAuthentication(loginJwtToken);
            } else {
                cookie = Arrays.stream(cookies).filter(item -> "useVisitor".equals(item.getName())).findAny().orElse(null);
                // 判断游客登录
                if (cookie != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                            (" ", null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }


        chain.doFilter(request, response);
    }
}
