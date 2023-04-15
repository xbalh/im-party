package com.im.imparty.geometryChaos.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class BattleInfo {
    private String id;
    private String offensiveInfo;
    private String defenseInfo;
    private String fightInfo;
    private Integer round;
    private Date createTime;
    private String message;
    private String surplusCd;
    private Boolean ifEnd;
}
