package com.im.imparty.config.security;

import com.im.imparty.web.vo.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component("loginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {


        response.setContentType("application/json;charset=UTF-8");
        // 获取前端传到后端的全部参数
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            System.out.println("参数- " + paraName + " : " + request.getParameter(paraName));
        }
        logger.info("登录认证成功");
        //这里写你登录成功后的逻辑，可加验证码验证等

        //ajax请求认证方式
        response.getWriter().write(BaseResult.ok("登陆成功").data(authentication).toJSONString());
    }
}
