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
public class UserInfoDetail implements Serializable, UserDetails {


    private String userName;

    private String nickName;

    private String password;

    private String validSts;

    private String salt;

    private LocalDateTime saltExpiresTime;

    private List<RoleInfo> roleList;

    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return StringUtils.equals(validSts, "A");
    }

    @Override
    public boolean isAccountNonLocked() {
        return StringUtils.equals(validSts, "A");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return StringUtils.equals(validSts, "A");
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.equals(validSts, "A");
    }
}
