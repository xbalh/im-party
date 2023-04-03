package com.im.imparty.config.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.im.imparty.common.util.JwtTokenUtils;
import com.im.imparty.config.security.authentication.LoginJwtToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.stream.Stream;

public class JwtVerifyAuthenticationFilter extends BasicAuthenticationFilter {
    public JwtVerifyAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        Cookie cookie = Arrays.stream(cookies).filter(item -> "Authentication".equals(item.getName())).findAny().orElse(null);

        if (cookie != null || request.getHeader("Authentication") != null) {
            System.out.println("request.getHeader(\"Token\")" + request.getHeader("Authentication"));
            System.out.println("cookie" + cookie.getValue());

            String jwtStr = cookie == null || cookie.getValue() == null ? request.getHeader("Authentication") : cookie.getValue();

            Cookie refreshTokenCookie = Arrays.stream(cookies).filter(item -> "refresh_token".equals(item.getName())).findAny().orElse(null);
            String refreshToken = refreshTokenCookie == null || refreshTokenCookie.getValue() == null ? request.getHeader("refresh_token") : refreshTokenCookie.getValue();

            // 验证jwt
            Map<String, Claim> jsonObject = JwtTokenUtils.decryptJwt(jwtStr);
            if (jsonObject.get("userName") == null) {
                return;
            }
            String userName = jsonObject.get("userName").asString();

            if (jsonObject.get("exp") == null) {
                return;
            }
            Instant exp = jsonObject.get("exp").asInstant();

            if (jsonObject.get("userInfo") == null) {
                return;
            }
            JSONObject userInfo = JSONObject.parseObject(jsonObject.get("userInfo").asString());
            String[] roleList = Optional.ofNullable(userInfo.getString("roleList")).orElse("").split(",");
            List<GrantedAuthority> authorityList = Arrays.stream(roleList).map(item -> (GrantedAuthority) () -> item).collect(Collectors.toList());
            LoginJwtToken loginJwtToken = new LoginJwtToken(authorityList, userName, cookie.getValue());
            loginJwtToken.setNickName(Optional.ofNullable(userInfo.getString("nickName")).orElse(userName));
            loginJwtToken.setExpireTime(exp);
            loginJwtToken.setRefreshToken(refreshToken);
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
        chain.doFilter(request, response);
    }
}
