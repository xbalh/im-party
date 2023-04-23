package com.im.imparty.geometryChaos.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class BattleResultInfo {
    private PersonFightInfo offensiveInfo;
    private PersonFightInfo defenseInfo;
    private FightInfo fightInfo;
    private JSONArray message;
}
