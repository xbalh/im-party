package com.im.imparty.config.security;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.common.util.DateTimeUtils;
import com.im.imparty.common.util.JwtTokenUtils;
import com.im.imparty.login.vo.LoginInfoVO;
import com.im.imparty.user.dto.RoleInfo;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.web.vo.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

        UserInfoDetail userDetail = (UserInfoDetail) authentication.getPrincipal();
        //ajax请求认证方式
        JSONObject info = new JSONObject();
        info.put("nickName", userDetail.getNickName());
        info.put("userName", authentication.getName());
        // info.put("saltExpiresTime", DateTimeUtils.dateTimeToString(userDetail.getSaltExpiresTime()));
        info.put("roleList", StringUtils.join(Optional.ofNullable(userDetail.getRoleList()).orElse(Collections.emptyList()).stream().map(RoleInfo::getRoleCode).collect(Collectors.toList()), ","));
        String randomStr = UUID.randomUUID().toString();
        String tokenJwt = JwtTokenUtils.encryptTokenJwt(info, userDetail.getUsername(), randomStr);
        LoginInfoVO loginInfoVO = new LoginInfoVO(tokenJwt, JwtTokenUtils.encryptRefreshTokenJwt(info, userDetail.getUsername(), randomStr));
        response.setHeader("Authentication", String.format("Bearer %s", tokenJwt));
        response.setHeader("refreshToken", loginInfoVO.getRefreshToken());
        response.addCookie(new Cookie("Authentication", tokenJwt));
        response.addCookie(new Cookie("refreshToken", loginInfoVO.getRefreshToken()));
        response.getWriter().write(BaseResult.ok(authentication.getName() + "登陆成功").data(loginInfoVO).toJSONString());
    }
}
