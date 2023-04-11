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
public class BattleStartInfo {
    private PersonFightInfo user;
    private PersonFightInfo enemy ;
    private FightInfo fightInfo;
}
