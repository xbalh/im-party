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
public class BuffUpInfo {
    private String buff;
    private String target;
    private Integer turns;
    private Integer per;
}
