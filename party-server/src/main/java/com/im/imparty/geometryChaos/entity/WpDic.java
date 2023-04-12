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
    private String id;
    private Integer wpStr;
    private Integer wpWit;
    private Integer wpAgi;
    private Integer wpHlt;
    private String wpType;
    private String wpRarity;
    private String wpSize;
}
