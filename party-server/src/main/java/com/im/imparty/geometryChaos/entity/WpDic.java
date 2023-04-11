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
public class WpDic {
    private Integer str;
    private Integer wit;
    private Integer agi;
    private Integer spd;
    private Integer hlt;
}
