package com.im.imparty.login.service;

import com.im.imparty.login.vo.LoginInVO;
import com.im.imparty.login.vo.LoginInfoVO;

public interface LoginService {
    LoginInfoVO login(LoginInVO param);
}
