package com.im.imparty.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.im.imparty.common.BaseDomain;
import lombok.Builder;
import lombok.Data;

@TableName("role")
@Builder
@Data
public class RoleDomain extends BaseDomain<RoleDomain> {

    @TableField("code")
    private String code;

}
