package com.im.imparty.login.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("登录参数")
@Data
public class LoginInVO {

    private String username;

    private String password;

}
