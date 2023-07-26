package com.im.imparty.user.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
public class  UserInfo implements Serializable {


    private String userName;

    private String nickName;

    private String wyyUserId;

    private String userAvatarUrl;


}
