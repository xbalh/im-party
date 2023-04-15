package com.im.imparty.geometryChaos.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.common.exception.CustomException;
import com.im.imparty.geometryChaos.entity.*;
import com.im.imparty.geometryChaos.service.GeometryChaosMainService;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api("瞎几百相关请求")
@RestController
public class GeometryChaosMainController {

    @Resource
    private GeometryChaosMainService geometryChaosMainService;


    @ApiOperation(value = "创建角色")
    @PostMapping("/geometryChaosMain/createCharacter")
    public BaseResult<UserStaticInfo> createCharacter(@RequestBody String userName) {
        UserStaticInfo geometryChaosMainServiceCharacter = geometryChaosMainService.createCharacter(userName);
        return BaseResult.ok(geometryChaosMainServiceCharacter);
    }


    @ApiOperation(value = "获取战斗前基础属性")
    @PostMapping("/geometryChaosMain/getUserFightInfo")
    public BaseResult<PersonFightInfo> getUserFightInfo(@RequestBody String userName) throws CustomException {
        PersonFightInfo userFightInfo = geometryChaosMainService.getUserFightInfo(userName);
        return BaseResult.ok(userFightInfo);
    }


    @ApiOperation(value = "开始参数")
    @PostMapping("/geometryChaosMain/fightStart")
    public BaseResult<JSONObject> fightStart(@RequestBody BattleStartInfo battleStartInfo) {
        String result = geometryChaosMainService.fightStart(battleStartInfo.getUser(), battleStartInfo.getEnemy(), battleStartInfo.getFightInfo());
        JSONObject resultJSON = JSONObject.parseObject(result);
        return BaseResult.ok(resultJSON);
    }


    @ApiOperation(value = "开始单个回合")
    @PostMapping("/geometryChaosMain/startOneTurn")
    public BaseResult<BattleInfo> startOneTurn(@RequestBody TurnInfo turnInfo) {
        BattleInfo result = geometryChaosMainService.startOneTurn(turnInfo);
        return BaseResult.ok(result);
    }


    @ApiOperation(value = "获取可挑战的对手")
    @PostMapping("/geometryChaosMain/getChallengableFighter")
    public BaseResult<List<UserStaticInfo>> getChallengableFighter(@RequestBody String userName) {
        List<UserStaticInfo> challengableFighter = geometryChaosMainService.getChallengableFighter(userName);
        return BaseResult.ok(challengableFighter);
    }

}
