package com.im.imparty.login.vo;

import com.im.imparty.user.dto.UserInfoDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@ApiModel("登录返回结果")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginInfoVO {

    @ApiModelProperty("鉴权密钥")
    private String accessToken;

    @ApiModelProperty("续期密钥")
    private String refreshToken;
}
