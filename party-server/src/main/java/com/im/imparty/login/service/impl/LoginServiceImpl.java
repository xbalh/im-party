package com.im.imparty.login.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.common.util.DateTimeUtils;
import com.im.imparty.common.util.JwtTokenUtils;
import com.im.imparty.login.service.LoginService;
import com.im.imparty.login.vo.LoginInVO;
import com.im.imparty.login.vo.LoginInfoVO;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserService userService;

    @Override
    public LoginInfoVO login(LoginInVO param) {
        UserInfoDetail userDetail = userService.getUserDetail(param.getUsername());
        if (userDetail == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }

        JSONObject info = new JSONObject();
        info.put("nickName", userDetail.getNickName());
        info.put("userName", userDetail.getUsername());
        info.put("saltExpiresTime", DateTimeUtils.dateTimeToString(userDetail.getSaltExpiresTime()));
        info.put("roleList", StringUtils.join(Optional.ofNullable(userDetail.getRoleList()).orElse(Collections.emptyList())));
        String tokenJwt = JwtTokenUtils.encryptTokenJwt(info, userDetail.getUsername());
        LoginInfoVO loginInfoVO = new LoginInfoVO(tokenJwt, JwtTokenUtils.encryptRefreshTokenJwt(tokenJwt));
        return loginInfoVO;

    }

}
