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
    private Integer target; // 自己0 对方1
    private Integer turns;
    private Integer per;
}
