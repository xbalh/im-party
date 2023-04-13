package com.im.imparty.geometryChaos.service;

import com.im.imparty.geometryChaos.entity.*;

public interface GeometryChaosMainService {
    /**
     * 新建
     *
     * @param userName
     **/
    UserStaticInfo createCharacter(String userName);

    /**
     * 获取战斗前基础属性
     *
     * @param userName
     **/
    PersonFightInfo getUserFightInfo(String userName);

    /**
     * 开始参数
     *
     * @param user
     * @param enemy
     * @param fightInfo
     **/
    String fightStart(PersonFightInfo user, PersonFightInfo enemy, FightInfo fightInfo);

    /**
     * 单个回合
     *
     * @param turnInfo
     **/
    BattleInfo startOneTurn(TurnInfo turnInfo);
}
