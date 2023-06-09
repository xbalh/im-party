package com.im.imparty.config.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.im.imparty.login.vo.LoginInVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


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
        String errorMsg = "";
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
                //UserService bean = SpringFactoryUtils.getBean(UserService.class);
                //UserInfoDetail login = bean.getUserDetail(loginIn.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginIn.getUsername()
                                , loginIn.getPassword());
                setDetails(request, authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        } catch (IOException e) {
            log.error("LoginVerifyAuthenticationFilter验证io异常");
            throw new InternalAuthenticationServiceException(e.getMessage());
        } catch (AuthenticationException e) {
            log.error("LoginVerifyAuthenticationFilter异常");
            throw e;
        }
        return null;
    }
}
