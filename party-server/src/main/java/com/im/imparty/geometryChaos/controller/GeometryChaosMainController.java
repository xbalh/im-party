package com.im.imparty.geometryChaos.controller;

import com.im.imparty.geometryChaos.entity.*;
import com.im.imparty.geometryChaos.service.GeometryChaosMainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("瞎几百相关请求")
@RestController
public class GeometryChaosMainController {

    @Resource
    private GeometryChaosMainService geometryChaosMainService;


    @ApiOperation(value = "创建角色")
    @PostMapping("/geometryChaosMain/createCharacter")
    public UserStaticInfo createCharacter(@RequestBody String userName) {
        UserStaticInfo geometryChaosMainServiceCharacter = geometryChaosMainService.createCharacter(userName);
        return geometryChaosMainServiceCharacter;
    }


    @ApiOperation(value = "获取战斗前基础属性")
    @PostMapping("/geometryChaosMain/getUserFightInfo")
    public PersonFightInfo getUserFightInfo(@RequestBody String userName) {
        PersonFightInfo userFightInfo = geometryChaosMainService.getUserFightInfo(userName);
        return userFightInfo;
    }


    @ApiOperation(value = "开始参数")
    @PostMapping("/geometryChaosMain/fightStart")
    public String fightStart(@RequestBody BattleStartInfo battleStartInfo) {
        String result = geometryChaosMainService.fightStart(battleStartInfo.getUser(), battleStartInfo.getEnemy(), battleStartInfo.getFightInfo());
        return result;
    }


    @ApiOperation(value = "开始单个回合")
    @PostMapping("/geometryChaosMain/startOneTurn")
    public BattleInfo startOneTurn(@RequestBody TurnInfo turnInfo) {
        BattleInfo result = geometryChaosMainService.startOneTurn(turnInfo);
        return result;
    }
}
