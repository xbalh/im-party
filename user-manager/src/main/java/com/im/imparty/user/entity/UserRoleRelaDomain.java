package com.im.imparty.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.im.imparty.common.BaseDomain;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@TableName("user_role_rale")
public class UserRoleRelaDomain extends BaseDomain<UserRoleRelaDomain> {


    @TableField("username")
    private String userName;

    @TableField("role_code")
    private String roleCode;

}
