package com.im.imparty.geometryChaos.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description
 * @Author Liang Yanbo
 * @Date 2023/4/6
 **/
@Data
@Accessors(chain = true)
public class FightInfo {
    private Integer round;
    private Integer ifVictory;
    private Integer ifHeal;
    private List<BuffUpInfo> ifBuff;
    private Integer finalNum;
    private String instructions;
    private JSONArray message;
    private String offensiveName;
    private String defenseName;
}
