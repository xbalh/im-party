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
public class FightInfo {
    private Integer ifVictory;
    private Integer ifHeal;
    private Integer ifDebuff;
    private Integer finalNum;
    private String instructions;
}
