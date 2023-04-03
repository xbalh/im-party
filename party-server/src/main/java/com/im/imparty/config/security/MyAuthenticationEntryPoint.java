package com.im.imparty.config.security;

import com.im.imparty.web.vo.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录失败");
        response.setContentType("application/json;charset=UTF-8");
        //这里写你登录失败后的逻辑，可加验证码验证等
        String errorInfo = "";
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException) {
            errorInfo = "账户名或者密码输入错误!";
        } else if (exception instanceof LockedException) {
            errorInfo = "账户被锁定，请联系管理员!";
        } else if (exception instanceof CredentialsExpiredException) {
            errorInfo = "密码过期，请联系管理员!";
        } else if (exception instanceof AccountExpiredException) {
            errorInfo = "账户过期，请联系管理员!";
        } else if (exception instanceof DisabledException) {
            errorInfo = "账户被禁用，请联系管理员!";
        } else {
            errorInfo = "登录失败!";
        }
        log.info("登录失败原因：" + errorInfo);
        //ajax请求认证方式
        response.getWriter().write(BaseResult.fail(errorInfo).toJSONString());
    }
}
