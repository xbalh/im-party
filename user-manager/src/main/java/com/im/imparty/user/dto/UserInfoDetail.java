package com.im.imparty.user.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
public class UserInfoDetail implements Serializable {


    private String userName;

    private String nickName;

    private String password;

    private String validSts;

    private String salt;

    private LocalDateTime saltExpiresTime;

    private List<RoleInfo> roleList;
}
