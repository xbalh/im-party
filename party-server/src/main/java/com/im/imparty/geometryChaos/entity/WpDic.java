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
public class WpDic {
    @TableId
    private String id;
    private Integer wpStr;
    private Integer wpWit;
    private Integer wpAgi;
    private Integer wpHlt;
    private String wpType;
    private String wpDetail;
    private String wpName;
    private String wpRarity;
    private String wpSize;
    private String wpAtkType;
}
