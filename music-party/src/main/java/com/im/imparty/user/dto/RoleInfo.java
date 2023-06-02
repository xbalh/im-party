package com.im.imparty.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleInfo implements Serializable{

    private String userName;

    private String roleCode;
}
