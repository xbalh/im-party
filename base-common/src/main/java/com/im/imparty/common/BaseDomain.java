package com.im.imparty.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

public class BaseDomain<T extends Model<?>> extends Model<T> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

}
