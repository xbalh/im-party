package com.im.imparty.geometryChaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class WpInfo {
    @TableId
    private String userName;
    private String wpHolding;
    private String wpNameHolding;
    private String skHolding;
    private String friendHolding;
    private String insHolding;
    private String strategyHolding;
}
