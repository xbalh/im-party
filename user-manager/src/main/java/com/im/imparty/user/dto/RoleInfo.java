package com.im.imparty.user.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Data
public class RoleInfo implements Serializable, GrantedAuthority {

    private String userName;

    private String roleCode;

    @Override
    public String getAuthority() {
        return roleCode;
    }
}
