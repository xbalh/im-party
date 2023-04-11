package com.im.imparty.geometryChaos.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class PersonFightInfo {
    private String name;
    private Integer hp;
    private Integer str;
    private Integer wit;
    private Integer agi;
    private Integer spd;
    private Integer hlt;
    private Integer cri;
    private Integer criPer;
    private Integer blk;
    private Integer blkPer;
    private Integer ca;
    private BigDecimal finalAtk;
    private BigDecimal finalDef;
    private List<String> usedWeapon;
    private List<BuffInfo> buff;
    private List<BuffInfo> debuff;
    private WpInfo wpInfo;
}
