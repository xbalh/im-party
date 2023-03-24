package com.im.imparty.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class User {

    @TableField("username")
    private String userName;

}
