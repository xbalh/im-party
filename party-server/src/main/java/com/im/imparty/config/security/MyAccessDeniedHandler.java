package com.im.imparty.config.security;

import com.im.imparty.web.vo.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("登录失败");
        response.setContentType("application/json;charset=UTF-8");
        log.info("登录失败原因：" + accessDeniedException.getMessage());
        //ajax请求认证方式
        response.getWriter().write(BaseResult.fail(accessDeniedException.getMessage()).toJSONString());
    }
}
