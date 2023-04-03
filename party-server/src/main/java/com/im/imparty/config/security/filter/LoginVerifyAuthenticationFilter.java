package com.im.imparty.config.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.im.imparty.config.security.authentication.LoginJwtToken;
import com.im.imparty.login.service.LoginService;
import com.im.imparty.login.vo.LoginInVO;
import com.im.imparty.login.vo.LoginInfoVO;
import com.im.imparty.spring.util.SpringFactoryUtils;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;


@Slf4j
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
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try (BufferedReader reader = request.getReader()){
            StringBuilder sb = new StringBuilder();
            String buffer = null;
            while ((buffer = reader.readLine()) != null) {
                sb.append(buffer);
                if (sb.length() > 10000) {
                    return null;
                }
            }
            if (JSONValidator.from(sb.toString()).validate()) {
                LoginInVO loginIn = JSONObject.parseObject(sb.toString(), LoginInVO.class);
                UserService bean = SpringFactoryUtils.getBean(UserService.class);
                UserInfoDetail login = bean.getUserDetail(loginIn.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(login.getUserName()
                                , login.getPassword()
                                , login.getRoleList().stream().map(i ->
                                (GrantedAuthority) () -> i.getRoleCode()
                        ).collect(Collectors.toList()));
                authenticationToken.setDetails(login);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        } catch (IOException e) {
            log.error("LoginVerifyAuthenticationFilter验证io异常");
            return null;
        } catch (Exception e) {
            log.error("LoginVerifyAuthenticationFilter异常");
            return null;
        }
        return null;
    }
}
