package com.im.imparty.login.controller;

import com.im.imparty.login.service.LoginService;
import com.im.imparty.login.vo.LoginInVO;
import com.im.imparty.login.vo.LoginInfoVO;
import com.im.imparty.user.service.UserService;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("用户登录相关请求")
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public BaseResult<LoginInfoVO> login(@RequestBody LoginInVO param) {
        return BaseResult.ok("登录成功！", loginService.login(param));
    }

    @ApiOperation("续期")
    @PostMapping("/renewal")
    public BaseResult<LoginInfoVO> renewal() {
        return null;
    }

}
