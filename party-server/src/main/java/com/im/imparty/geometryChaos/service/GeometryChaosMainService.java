package com.im.imparty.geometryChaos.service;

import com.im.imparty.common.exception.CustomException;
import com.im.imparty.geometryChaos.entity.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
    PersonFightInfo getUserFightInfo(String userName) throws CustomException;

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

    List<UserStaticInfo> getChallengableFighter(String userName);

    String fighterLevelUp(String userName);
}
