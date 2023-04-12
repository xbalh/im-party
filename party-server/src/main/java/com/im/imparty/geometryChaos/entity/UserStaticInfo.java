package com.im.imparty.geometryChaos.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class UserStaticInfo {
    private String userName;
    private Integer str;
    private Integer wit;
    private Integer agi;
    private Integer spd;
    private Integer htl;
    private Integer yb;
    private Integer lv;
    private Integer expe;
}
