package com.im.imparty.geometryChaos.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.common.dic.LevelDic;
import com.im.imparty.common.exception.CustomException;
import com.im.imparty.geometryChaos.mapper.BattleInfoMapper;
import com.im.imparty.geometryChaos.mapper.UserStaticInfoMapper;
import com.im.imparty.geometryChaos.mapper.WpDicMapper;
import com.im.imparty.geometryChaos.mapper.WpInfoMapper;
import com.im.imparty.geometryChaos.service.GeometryChaosMainService;
import com.im.imparty.geometryChaos.entity.*;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.user.service.UserService;
import javafx.scene.control.CustomMenuItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class GeometryChaosMainServiceImpl implements GeometryChaosMainService {

    @Autowired
    private BattleInfoMapper battleInfoMapper;

    @Autowired
    private UserStaticInfoMapper userStaticInfoMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WpInfoMapper wpInfoMapper;

    @Autowired
    private WpDicMapper wpDicMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserStaticInfo createCharacter(String userName) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 5, 5, 5, 6)); // 初始列表
        int sum = 18; // 需要添加的总数
        Random random = new Random();
        while (sum > 0) {
            // 随机选择一个数字
            int index = random.nextInt(numbers.size());
            if (numbers.get(index) >= 15) {
                continue;
            }
            numbers.set(index, numbers.get(index) + 1);
            sum -= 1;
        }
        UserStaticInfo userStaticInfo = new UserStaticInfo();
        userStaticInfo.setUserName(userName)
                .setLv(1)
                .setExpe(0)
                .setStr(numbers.get(0))
                .setAgi(numbers.get(1))
                .setWit(numbers.get(2))
                .setSpd(numbers.get(3))
                .setHtl(numbers.get(4))
                .setYb(0)
                .setInsPoint(0)
                .setAttrPoint(0)
                .setSkPoint(0)
                .setPower(100);
        // save PersonInfo
        int insertResult = userStaticInfoMapper.insert(userStaticInfo);
        if (insertResult == 0) {
            log.error("创建时出错 {}", userStaticInfo);
        }
        return userStaticInfo;
    }


    @Override
    public PersonFightInfo getUserFightInfo(String userName) throws CustomException {
        QueryWrapper<UserStaticInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        UserStaticInfo userStaticInfo = userStaticInfoMapper.selectOne(wrapper);
        if (userStaticInfo == null) {
            throw new CustomException("角色还未创建，需要先创立角色！", 700);
        }

        QueryWrapper<WpInfo> wpWrapper = new QueryWrapper<>();
        wpWrapper.eq("user_name", userName);
        WpInfo wpInfo = wpInfoMapper.selectOne(wpWrapper);
        PersonFightInfo personFightInfo = new PersonFightInfo();
        Integer str = userStaticInfo.getStr();
        Integer agi = userStaticInfo.getAgi();
        Integer spd = userStaticInfo.getSpd();
        Integer wit = userStaticInfo.getWit();
        Integer htl = userStaticInfo.getHtl();

        // TODO skInfo 改变基础fightInfo
        personFightInfo.setName(userStaticInfo.getUserName())
                .setWpInfo(wpInfo)
                .setHp(20 + htl * 12)
                .setStr(str)
                .setWit(wit)
                .setSpd(spd)
                .setAgi(agi)
                .setHtl(htl)
                .setBlk(1000 + agi * 100)
                .setBlkPer(30)
                .setCri(500 + agi * 150)
                .setCriPer(50 + agi * 3)
                .setMiss(300 + agi * 30)
                .setCa(250 + agi * 25)
                .setFinalAtk(new BigDecimal("1"))
                .setFinalDef(new BigDecimal("1"))
                .setUsedWeapon(new ArrayList<>())
                .setBuff(new ArrayList<>());
        return personFightInfo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String fightStart(PersonFightInfo user, PersonFightInfo enemy, FightInfo fightInfo) {
        // 扣除挑战者10点体力
        UserStaticInfo userStaticInfo = userStaticInfoMapper.selectById(user.getName());
        if (userStaticInfo.getPower() < 10) {
            throw new CustomException("体力不足10点！", 700);
        }
        userStaticInfo.setPower(userStaticInfo.getPower() - 10);
        userStaticInfoMapper.updateById(userStaticInfo);

        BattleInfo battleInfo = new BattleInfo();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        battleInfo.setId(uuid)
                .setOffensiveInfo(JSON.toJSON(user).toString())
                .setDefenseInfo(JSON.toJSON(enemy).toString())
                .setFightInfo(JSON.toJSON(fightInfo).toString())
                .setCreateTime(new Date())
                .setRound(0);
        int insertResult = battleInfoMapper.insert(battleInfo);
        if (insertResult == 0) {
            log.error("创建时出错 {}", battleInfo);
        }

        return uuid;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BattleInfo startOneTurn(TurnInfo turnInfo) {
        String uuid = turnInfo.getUuid();
        BattleInfo battleInfo = battleInfoMapper.selectById(uuid);
        PersonFightInfo offensiveInfo = JSONObject.parseObject(battleInfo.getOffensiveInfo(), PersonFightInfo.class);
        PersonFightInfo defenseInfo = JSONObject.parseObject(battleInfo.getDefenseInfo(), PersonFightInfo.class);
        FightInfo fightInfo = JSONObject.parseObject(battleInfo.getFightInfo(), FightInfo.class);
        fightInfo.setOffensiveName(offensiveInfo.getName())
                .setDefenseName(defenseInfo.getName());
        fightInfo.setInstructions(turnInfo.getInstructions());
        Integer round = battleInfo.getRound();
        JSONArray message = new JSONArray();

        if (offensiveInfo.getHp() > 0 && defenseInfo.getHp() > 0 && fightInfo.getIfVictory() == null) {
            round++;
            fightInfo.setRound(round);
            JSONObject roundMsg = this.newMsgCreate("回合 " + round + " 开始：", null, null, null);
            message.add(roundMsg);
            if (round >= 20) {
                JSONObject newMsgCreate = this.newMsgCreate("回合大于等于20，结算伤害至十倍！", null, null, null);
                message.add(newMsgCreate);
            } else if (round >= 15) {
                JSONObject newMsgCreate = this.newMsgCreate("回合大于等于15，结算伤害至五倍！", null, null, null);
                message.add(newMsgCreate);
            } else if (round >= 10) {
                JSONObject newMsgCreate = this.newMsgCreate("回合大于等于10，结算伤害至两倍！", null, null, null);
                message.add(newMsgCreate);
            }

            // 根据 speed 大小交替行动，行动后判定是否需要结束战斗，根据buff判定是否继续行动
            if (offensiveInfo.getSpd() >= defenseInfo.getSpd()) {
                JSONObject newMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 先手！", null, null, null);
                message.add(newMsgCreate);
                BattleResultInfo battleResultInfo = this.fightOneTurn(offensiveInfo, defenseInfo, fightInfo);
                message.addAll(battleResultInfo.getMessage());

                if (battleResultInfo.getDefenseInfo().getHp() > 0 && battleResultInfo.getOffensiveInfo().getHp() > 0) {
                    this.ifTurnContinue(battleResultInfo, battleResultInfo.getOffensiveInfo(), battleResultInfo.getDefenseInfo(), fightInfo, message);
                }

                battleInfo.setOffensiveInfo(JSON.toJSON(battleResultInfo.getOffensiveInfo()).toString())
                        .setDefenseInfo(JSON.toJSON(battleResultInfo.getDefenseInfo()).toString())
                        .setFightInfo(JSON.toJSON(battleResultInfo.getFightInfo()).toString());

                if (battleResultInfo.getDefenseInfo().getHp() > 0 && battleResultInfo.getOffensiveInfo().getHp() > 0) {
                    BattleResultInfo otherSideBattleResultInfo = this.fightOneTurn(battleResultInfo.getDefenseInfo(), battleResultInfo.getOffensiveInfo(), fightInfo);
                    message.addAll(otherSideBattleResultInfo.getMessage());

                    if (battleResultInfo.getDefenseInfo().getHp() > 0 && battleResultInfo.getOffensiveInfo().getHp() > 0) {
                        this.ifTurnContinue(battleResultInfo, otherSideBattleResultInfo.getDefenseInfo(), otherSideBattleResultInfo.getOffensiveInfo(), fightInfo, message);
                    }

                    battleInfo.setOffensiveInfo(JSON.toJSON(otherSideBattleResultInfo.getOffensiveInfo()).toString())
                            .setDefenseInfo(JSON.toJSON(otherSideBattleResultInfo.getDefenseInfo()).toString())
                            .setFightInfo(JSON.toJSON(otherSideBattleResultInfo.getFightInfo()).toString());
                }
            } else {
                JSONObject newMsgCreate = this.newMsgCreate(defenseInfo.getName() + " 先手！", null, null, null);
                message.add(newMsgCreate);
                BattleResultInfo battleResultInfo = this.fightOneTurn(defenseInfo, offensiveInfo, fightInfo);
                message.addAll(battleResultInfo.getMessage());

                if (battleResultInfo.getDefenseInfo().getHp() > 0 && battleResultInfo.getOffensiveInfo().getHp() > 0) {
                    this.ifTurnContinue(battleResultInfo, battleResultInfo.getDefenseInfo(), battleResultInfo.getOffensiveInfo(), fightInfo, message);
                }

                battleInfo.setOffensiveInfo(JSON.toJSON(battleResultInfo.getOffensiveInfo()).toString())
                        .setDefenseInfo(JSON.toJSON(battleResultInfo.getDefenseInfo()).toString())
                        .setFightInfo(JSON.toJSON(battleResultInfo.getFightInfo()).toString());

                if (battleResultInfo.getOffensiveInfo().getHp() > 0 && battleResultInfo.getDefenseInfo().getHp() > 0) {
                    BattleResultInfo otherSideBattleResultInfo = this.fightOneTurn(battleResultInfo.getOffensiveInfo(), battleResultInfo.getDefenseInfo(), fightInfo);
                    message.addAll(otherSideBattleResultInfo.getMessage());

                    if (otherSideBattleResultInfo.getDefenseInfo().getHp() > 0 && otherSideBattleResultInfo.getOffensiveInfo().getHp() > 0) {
                        this.ifTurnContinue(battleResultInfo, otherSideBattleResultInfo.getOffensiveInfo(), otherSideBattleResultInfo.getDefenseInfo(), fightInfo, message);
                    }

                    battleInfo.setOffensiveInfo(JSON.toJSON(otherSideBattleResultInfo.getDefenseInfo()).toString())
                            .setDefenseInfo(JSON.toJSON(otherSideBattleResultInfo.getOffensiveInfo()).toString())
                            .setFightInfo(JSON.toJSON(otherSideBattleResultInfo.getFightInfo()).toString());
                }
            }
        }

        offensiveInfo = JSONObject.parseObject(battleInfo.getOffensiveInfo(), PersonFightInfo.class);
        defenseInfo = JSONObject.parseObject(battleInfo.getDefenseInfo(), PersonFightInfo.class);
        fightInfo = JSONObject.parseObject(battleInfo.getFightInfo(), FightInfo.class);
        if (offensiveInfo.getHp() <= 0) {
            JSONObject newMsgCreate = this.newMsgCreate("挑战者" + offensiveInfo.getName() + "战败了。挑战者获得20点经验与6音币。",
                    null,
                    "defeat",
                    3);
            message.add(newMsgCreate);
            battleInfo.setId(uuid)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setIfEnd(true)
                    .setMessage(message);
            // 战斗失败，挑战者获得20点经验与6音币
            UserStaticInfo userStaticInfo = userStaticInfoMapper.selectById(offensiveInfo.getName());
            userStaticInfo.setExpe(userStaticInfo.getExpe() + 20)
                    .setYb(userStaticInfo.getYb() + 6);
            int updateById = userStaticInfoMapper.updateById(userStaticInfo);
            if (updateById == 0) {
                log.error("更新战斗后角色信息失败 {}", userStaticInfo);
            }
            return battleInfo;
        } else if (defenseInfo.getHp() <= 0) {
            JSONObject newMsgCreate = this.newMsgCreate("挑战者" + offensiveInfo.getName() + "获得了胜利！挑战者获得30点经验与8音币！",
                    null,
                    "victory",
                    3);
            message.add(newMsgCreate);
            battleInfo.setId(uuid)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setIfEnd(true)
                    .setMessage(message);
            // 战斗胜利，挑战者获得30点经验与8音币
            UserStaticInfo userStaticInfo = userStaticInfoMapper.selectById(offensiveInfo.getName());
            userStaticInfo.setExpe(userStaticInfo.getExpe() + 30)
                    .setYb(userStaticInfo.getYb() + 8);
            int updateById = userStaticInfoMapper.updateById(userStaticInfo);
            if (updateById == 0) {
                log.error("更新战斗后角色信息失败 {}", userStaticInfo);
            }
            return battleInfo;
        } else {
            String uuidNew = UUID.randomUUID().toString().replaceAll("-", "");
            battleInfo.setId(uuidNew)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setIfEnd(false)
                    .setMessage(message);
            int insertResult = battleInfoMapper.insert(battleInfo);
            if (insertResult == 0) {
                log.error("创建时出错 {}", battleInfo);
            }
            return battleInfo;
        }
    }

    @Override
    public List<UserStaticInfo> getChallengableFighter(String userName) {
        // 获取可挑战的对手
        QueryWrapper<UserDomain> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("username", userName);
        List<UserDomain> list = userService.list(queryWrapper);
        Iterator<UserDomain> userDomainIterator = list.iterator();
        List<UserStaticInfo> userStaticInfoList = new ArrayList<>();
        while (userDomainIterator.hasNext()) {
            UserDomain userDomain = userDomainIterator.next();
            String userDomainUserName = userDomain.getUserName();
            UserStaticInfo userStaticInfo = userStaticInfoMapper.selectById(userDomainUserName);
            if (userStaticInfo == null) {
                userDomainIterator.remove();
            } else {
                userStaticInfoList.add(userStaticInfo);
            }
        }
        return userStaticInfoList;
    }

    @Override
    public String fighterLevelUp(String userName) {
        StringBuilder resultStr = new StringBuilder();
        UserStaticInfo userStaticInfo = userStaticInfoMapper.selectById(userName);
        Integer lv = userStaticInfo.getLv();
        Integer expe = userStaticInfo.getExpe();
        BigDecimal calculateExperience = LevelDic.calculateExperience(lv);
        if (expe < calculateExperience.intValue()) {
            throw new CustomException("经验不足", 700);
        }
        userStaticInfo.setLv(lv + 1)
                .setExpe(expe - calculateExperience.intValue())
                .setAttrPoint(userStaticInfo.getAttrPoint() + 1);
        resultStr.append("升级了！目前等级：").append(lv + 1).append(" 级，经验剩余：").append(expe - calculateExperience.intValue()).append("，获得了1点可自由分配的属性点。");

        // 随机获得2点属性点
        List<Integer> numbers = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)); // 初始列表
        int sum = 2; // 需要添加的总数
        Random random = new Random();
        while (sum > 0) {
            // 随机选择一个数字
            int index = random.nextInt(numbers.size());
            if (numbers.get(index) >= 15) {
                continue;
            }
            numbers.set(index, numbers.get(index) + 1);
            sum -= 1;
        }

        userStaticInfo.setStr(userStaticInfo.getStr() + numbers.get(0))
                .setAgi(userStaticInfo.getAgi() + numbers.get(1))
                .setWit(userStaticInfo.getWit() + numbers.get(2))
                .setSpd(userStaticInfo.getSpd() + numbers.get(3))
                .setHtl(userStaticInfo.getHtl() + numbers.get(4));

        // 有30%获得武器，70%获得一个被动技能


        // 每5级获得一个主动技能点
        if ((lv + 1) % 5 == 0) {
            userStaticInfo.setInsPoint(userStaticInfo.getInsPoint() + 1);
            resultStr.append("<br>等级达到了5的倍数，获得了一点指令技能点！");
        }

        return null;
    }


    private BattleResultInfo fightOneTurn(PersonFightInfo fighter1, PersonFightInfo fighter2, FightInfo fightInfo) {
        // 返回消息
        JSONArray message = new JSONArray();

        // 战斗信息
        BattleResultInfo resultInfo = new BattleResultInfo();

        // 基础技能武器信息
        String userSkInfo = this.getUserSkInfo(fighter1);
        String wp = "";

        // 主动指令获取
        String instructions = fightInfo.getInstructions();
        if (StrUtil.isNotBlank(instructions)) {
            this.instructionsEffect(instructions, fighter1, fighter2, message, fightInfo);
        }

        // 回合前buff判定，如果P1有chain/P2有dizzy，在本回合开始前-1
        this.beforeTurnBuffEffect(fighter1, fighter2);

        // 技能大发明家是否生效
        if (userSkInfo.contains("wm")) {
            Random random = new Random();
            // 选择一个武器
            if (random.nextInt(100) < 3) {
                // 触发了大发明家，选择两个
                wp = this.getUserRandomWpInfo(fighter1, 2, instructions);
            } else {
                wp = this.getUserRandomWpInfo(fighter1, 1, instructions);
            }
        } else {
            wp = this.getUserRandomWpInfo(fighter1, 1, instructions);
        }

        // 每把武器都进行一轮战斗
        for (String oneWp : wp.split(",")) {
            this.btSettlement(fightInfo, fighter1, fighter2, oneWp, message);
            // 判定是否结束战斗
            boolean ifBattleEnd = this.ifBattleEnd(fighter1, fighter2, resultInfo, message, fightInfo, instructions);
            if (ifBattleEnd) {
                return resultInfo;
            }

            // 将待触发buff列表判定一次
            this.wpBuffEffect(fightInfo, fighter1, fighter2, wp, message);
        }

        // 判定是否触发反击
        if (!"10".equals(wp)) {
            Random random = new Random();
            List<String> debuffStrList = fighter2.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
            // 反击方获取武器并进行一次战斗判定
            if (!debuffStrList.contains("dizzy") && random.nextInt(10000) < fighter2.getCa()) {
                JSONObject newMsgCreate = this.newMsgCreate(fighter2.getName() + " 反击！",
                        "orange",
                        "buffadd",
                        1);
                message.add(newMsgCreate);

                String enemyRandomWpInfo = this.getUserRandomWpInfo(fighter2, 1, null);
                this.btSettlement(fightInfo, fighter2, fighter1, enemyRandomWpInfo, message);
                // 将待触发buff列表判定一次
                this.wpBuffEffect(fightInfo, fighter1, fighter2, wp, message);
            }
        }

        // 判定是否结束战斗
        boolean ifBattleEnd = this.ifBattleEnd(fighter1, fighter2, resultInfo, message, fightInfo, instructions);
        if (ifBattleEnd) {
            return resultInfo;
        }

        // 检查buff列表并生效
        this.calBuffEffect(fighter1, fighter2, message);

        // 返回单回合战斗信息
        String name = fighter1.getName();
        if (name.equals(fightInfo.getOffensiveName())) {
            resultInfo.setOffensiveInfo(fighter1)
                    .setDefenseInfo(fighter2);
        } else {
            resultInfo.setOffensiveInfo(fighter2)
                    .setDefenseInfo(fighter1);
        }

        resultInfo.setMessage(message)
                .setFightInfo(fightInfo);
        return resultInfo;
    }


    private String getUserSkInfo(PersonFightInfo user) {
        // TODO Load user skill info
        return "skInfo";
    }


    private String getUserRandomWpInfo(PersonFightInfo user, Integer getNum, String instructions) {
        // 根据getNum生成基础返回
        StringBuilder emptySb = new StringBuilder();
        StringBuilder resultSb = new StringBuilder();
        IntStream.range(0, getNum).forEach(i -> {
            emptySb.append("empty");
            if (i < getNum - 1) {
                emptySb.append(",");
            }
        });
        List<String> usedWeapon = user.getUsedWeapon();

        WpInfo wpInfo = user.getWpInfo();
        List<String> wpInfoList = Arrays.asList(wpInfo.getWpHolding().split(","));

        // 从非交集部分抽取武器，如果为空就返回empty
        for (String wp : emptySb.toString().split(",")) {
            String randomWp = this.getRandomNonIntersectingString(wpInfoList, usedWeapon, false);
            if (StrUtil.isNotBlank(instructions) && instructions.contains("1@@")) {
                String s = instructions.split("@@")[1];
                if (!resultSb.toString().contains(s)) {
                    randomWp = s;
                }
            }

            if (randomWp != null) {
                // 如果有抽到武器
                WpDic wpDic = wpDicMapper.selectById(randomWp);
                String wpAtkType = wpDic.getWpAtkType();
                String randomWpOtherHeal = "";
                if ("heal".equals(wpAtkType)) {
                    randomWpOtherHeal = this.getRandomNonIntersectingString(wpInfoList, usedWeapon, true);
                }
                if (resultSb.length() == 0) {
                    resultSb.append(randomWp);
                } else {
                    resultSb.append(",").append(randomWp);
                }

                if (StrUtil.isNotBlank(randomWpOtherHeal)) {
                    resultSb.append(",").append(randomWpOtherHeal);
                }

            } else {
                // 如果没有剩余武器了
                if (resultSb.length() == 0) {
                    resultSb.append("empty");
                } else {
                    resultSb.append(",").append("empty");
                }
            }
        }
        return resultSb.toString();
    }


    private String getRandomNonIntersectingString(List<String> wpInfoList, List<String> usedWeapon, boolean ifHeal) {
        List<String> tempWpInfoList = new ArrayList<>(wpInfoList);
        if (ifHeal) {
            List<String> toRemoveList = new ArrayList<>();
            for (String wp : wpInfoList) {
                WpDic wpDic = wpDicMapper.selectById(wp);
                if ("heal".equals(wpDic.getWpAtkType())) {
                    toRemoveList.add(wp);
                }
            }
            tempWpInfoList.removeAll(toRemoveList);
        }
        List<String> nonIntersecting = new ArrayList<>(tempWpInfoList);
        // 获取非交集部分
        nonIntersecting.removeAll(usedWeapon);
        Random random = new Random();
        if (nonIntersecting.isEmpty()) {
            // 无非交集部分，返回 null
            return null;
        }
        // 随机索引并返回随机字符串
        int randomIndex = random.nextInt(nonIntersecting.size());
        return nonIntersecting.get(randomIndex);
    }


    private FightInfo getWpFinalAtk(PersonFightInfo offensiveInfo, PersonFightInfo defenseInfo, String
            wp, FightInfo fightInfo, JSONArray message) {
        BigDecimal finalDmg = new BigDecimal(0);
        Integer str = offensiveInfo.getStr();
        Integer wit = offensiveInfo.getWit();
        Integer agi = offensiveInfo.getAgi();
        Integer spd = offensiveInfo.getSpd();
        Integer defenseSpd = defenseInfo.getSpd();
        Integer hlt = offensiveInfo.getHtl();

        Integer cri = offensiveInfo.getCri();
        if ("8".equals(wp)) {
            cri += 3000;
        }

        Random random = new Random();
        Integer criPer = offensiveInfo.getCriPer();
        BigDecimal finalAtk = offensiveInfo.getFinalAtk();

        // wp/sk影响
        finalAtk = this.wpOrSkDmgUp(wp, offensiveInfo, defenseInfo, fightInfo, finalAtk);

        // 如果速度有领先，每1点+1%伤害
        if (spd > defenseSpd) {
            finalAtk = finalAtk.add(new BigDecimal("0.01").multiply(new BigDecimal(spd - defenseSpd)));
        }

        // 上下有15%的浮动
        BigDecimal factor = new BigDecimal("0.15");
        Random factorRandom = new Random();
        BigDecimal deviation = factor.multiply(finalAtk); // 浮动的范围
        BigDecimal lowerBound = finalAtk.subtract(deviation); // 下限
        BigDecimal upperBound = finalAtk.add(deviation); // 上限
        finalAtk = lowerBound.add(
                BigDecimal.valueOf(factorRandom.nextDouble()).multiply(upperBound.subtract(lowerBound)));

        // 如果超过10回合，所有结算*2
        if (fightInfo.getRound() >= 10) {
            finalAtk = finalAtk.multiply(new BigDecimal("2"));
        }

        // 如果超过15回合，所有结算*5
        if (fightInfo.getRound() >= 15) {
            finalAtk = finalAtk.multiply(new BigDecimal("2.5"));
        }

        // 如果超过20回合，所有结算*10
        if (fightInfo.getRound() >= 20) {
            finalAtk = finalAtk.multiply(new BigDecimal("2"));
        }

        List<BuffInfo> buff = offensiveInfo.getBuff();
        for (BuffInfo buffInfo : buff) {
            if ("poison".equals(buffInfo.getBuff())) {
                finalAtk = finalAtk.multiply(new BigDecimal("0.9"));
            }
            if ("fear".equals(buffInfo.getBuff())) {
                finalAtk = finalAtk.multiply(new BigDecimal("0.8"));
            }
            if ("inspire".equals(buffInfo.getBuff())) {
                finalAtk = finalAtk.multiply(new BigDecimal("1.2"));
            }
            if ("lucky1".equals(buffInfo.getBuff())) {
                cri += 1000;
                criPer += 60;
            }
            if ("lucky2".equals(buffInfo.getBuff())) {
                cri += 1500;
                criPer += 80;
            }
            if ("lucky3".equals(buffInfo.getBuff())) {
                cri += 2000;
                criPer += 100;
            }
        }

        if (random.nextInt(10000) < cri) {
            JSONObject newMsgCreate = this.newMsgCreate(offensiveInfo.getName() + "暴击！",
                    null,
                    "cri",
                    2);
            message.add(newMsgCreate);
            finalAtk = finalAtk.multiply(new BigDecimal(1).add(new BigDecimal(criPer).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
        }

        WpDic wpDic = wpDicMapper.selectById(wp);
        Integer wpInfoStr = wpDic.getWpStr();
        if (wpInfoStr != null) {
            BigDecimal wpStr = new BigDecimal(wpInfoStr).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            finalDmg = finalDmg.add(wpStr.multiply(new BigDecimal(str)));
        }

        Integer wpInfoWit = wpDic.getWpWit();
        if (wpInfoWit != null) {
            BigDecimal wpWit = new BigDecimal(wpInfoWit).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            finalDmg = finalDmg.add(wpWit.multiply(new BigDecimal(wit)));
        }

        Integer wpInfoAgi = wpDic.getWpAgi();
        if (wpInfoAgi != null) {
            BigDecimal wpAgi = new BigDecimal(wpInfoAgi).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            finalDmg = finalDmg.add(wpAgi.multiply(new BigDecimal(agi)));
        }

        Integer wpInfoHlt = wpDic.getWpHlt();
        if (wpInfoHlt != null) {
            BigDecimal wpHlt = new BigDecimal(wpInfoHlt).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            finalDmg = finalDmg.add(wpHlt.multiply(new BigDecimal(hlt)));
        }

        finalDmg = finalDmg.multiply(finalAtk);
        fightInfo.setFinalNum(finalDmg.intValue() + 1)
                .setMessage(message);
        return fightInfo;
    }


    private JSONArray getFighterFinalDef(PersonFightInfo personInfo, String wp, FightInfo wpFinalAtk, JSONArray message) {
        Integer blk = personInfo.getBlk();
        Integer blkPer = personInfo.getBlkPer();
        Integer miss = personInfo.getMiss();
        BigDecimal finalDef = personInfo.getFinalDef();

        Random random = new Random();
        Random missRandom = new Random();
        if (missRandom.nextInt(10000) < miss) {
            JSONObject btMsgCreate = this.newMsgCreate(personInfo.getName() + "敏捷的闪避了本次攻击！",
                    "grey",
                    "miss",
                    1);
            message.add(btMsgCreate);
            finalDef = new BigDecimal("0");
        } else if (random.nextInt(10000) < blk) {
            JSONObject btMsgCreate = this.newMsgCreate(personInfo.getName() + "格挡住了本次攻击！",
                    "peru",
                    "blk",
                    1);
            message.add(btMsgCreate);
            finalDef = finalDef.multiply(new BigDecimal(1).subtract(new BigDecimal(blkPer).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
        }
        BigDecimal def = new BigDecimal(wpFinalAtk.getFinalNum()).multiply(finalDef);
        wpFinalAtk.setFinalNum(def.intValue());
        return message;
    }


    private void btSettlement(FightInfo fightInfo, PersonFightInfo offensiveInfo, PersonFightInfo
            defenseInfo, String wpInfo, JSONArray message) {
        fightInfo.setIfHeal(null)
                .setIfBuff(null);
        Random random = new Random();
        WpDic wpDic = wpDicMapper.selectById(wpInfo);
        // 12、19有命中率影响
        if ((!"12".equals(wpInfo) && !"19".equals(wpInfo)) || ("12".equals(wpInfo) && random.nextInt(100) < 50) || ("19".equals(wpInfo) && random.nextInt(100) < 99)) {
            int attackNum = 1;
            if ("5".equals(wpInfo)) {
                // 5hits
                attackNum = 5;
            }
            for (int i = 0; i < attackNum; i++) {
                // 计算实际伤害
                FightInfo wpFinalAtk = this.getWpFinalAtk(offensiveInfo, defenseInfo, wpInfo, fightInfo, message);
                if (CollectionUtil.isNotEmpty(wpFinalAtk.getMessage())) {
                    message.addAll(wpFinalAtk.getMessage());
                }
                if (wpFinalAtk.getIfHeal() != null && wpFinalAtk.getIfHeal() == 1) {
                    offensiveInfo.setHp(offensiveInfo.getHp() + wpFinalAtk.getFinalNum());

                    JSONObject newMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 使用了 " + wpDic.getWpName() + "，对 " + offensiveInfo.getName() + " 治疗了 " + wpFinalAtk.getFinalNum() + " 点HP",
                            "limegreen",
                            "heal",
                            null);
                    message.add(newMsgCreate);
                } else {
                    // 判定伤害是否被格挡，治疗不会格挡
                    if (wpFinalAtk.getIfHeal() != null && wpFinalAtk.getIfHeal() != 1) {
                        this.getFighterFinalDef(defenseInfo, wpInfo, wpFinalAtk, message);
                    }

                    defenseInfo.setHp(defenseInfo.getHp() - wpFinalAtk.getFinalNum());

                    JSONObject newMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 使用了 " + wpDic.getWpName() + "，对 " + offensiveInfo.getName() + " 造成了 " + wpFinalAtk.getFinalNum() + " 点伤害",
                            "red",
                            "damage",
                            1);
                    message.add(newMsgCreate);

                    if ("9".equals(wpInfo)) {
                        // heal self HP
                        offensiveInfo.setHp(offensiveInfo.getHp() + wpFinalAtk.getFinalNum());

                        JSONObject btMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 使用了 " + wpDic.getWpName() + "，对 " + offensiveInfo.getName() + " 治疗了 " + wpFinalAtk.getFinalNum() + " 点HP",
                                "limegreen",
                                "heal",
                                null);
                        message.add(btMsgCreate);
                    }

                    if ("13".equals(wpInfo)) {
                        // 自身损失造成伤害的20%
                        Integer finalNum = wpFinalAtk.getFinalNum();
                        BigDecimal multiply = new BigDecimal(finalNum).multiply(new BigDecimal("0.2"));
                        offensiveInfo.setHp(offensiveInfo.getHp() - multiply.intValue());

                        JSONObject btMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 使用了 " + wpDic.getWpName() + "，自爆卡车对自己造成了 " + multiply.intValue() + " 点伤害",
                                "limegreen",
                                "heal",
                                null);
                        message.add(btMsgCreate);
                    }
                }
            }
        } else {
            JSONObject btMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 使用了 " + wpDic.getWpName() + "，但是手滑了，没打中！ ",
                    "grey",
                    "miss",
                    null);
            message.add(btMsgCreate);
        }

        List<String> usedWeapon = offensiveInfo.getUsedWeapon();
        usedWeapon.add(wpInfo);
        offensiveInfo.setUsedWeapon(usedWeapon);
    }


    private JSONArray wpBuffEffect(FightInfo fightInfo, PersonFightInfo fighter1, PersonFightInfo fighter2, String
            wpInfo, JSONArray message) {
        List<BuffInfo> f1Buff = fighter1.getBuff();
        List<BuffInfo> f2Buff = fighter2.getBuff();
        List<BuffUpInfo> ifBuff = fightInfo.getIfBuff();
        if (ifBuff != null) {
            for (BuffUpInfo buffUpInfo : ifBuff) {
                String target = buffUpInfo.getTarget();
                Random random = new Random();
                if (fighter1.getName().equals(target)) {
                    if (random.nextInt(100) <= buffUpInfo.getPer()) {
                        BuffInfo buffInfo = new BuffInfo();
                        buffInfo.setBuff(buffUpInfo.getBuff())
                                .setTurns(buffUpInfo.getTurns());
                        f1Buff.add(buffInfo);
                        fighter1.setBuff(f1Buff);

                        JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 受到了状态 " + buffInfo.getBuff() + " " + buffInfo.getTurns() + " 回合",
                                "orange",
                                "buffadd",
                                1);
                        message.add(newMsgCreate);
                    }
                }
                if (fighter2.getName().equals(target)) {
                    if (random.nextInt(100) <= buffUpInfo.getPer()) {
                        BuffInfo buffInfo = new BuffInfo();
                        buffInfo.setBuff(buffUpInfo.getBuff())
                                .setTurns(buffUpInfo.getTurns());
                        f2Buff.add(buffInfo);
                        fighter2.setBuff(f2Buff);
                        JSONObject newMsgCreate = this.newMsgCreate(fighter2.getName() + " 受到了状态 " + buffInfo.getBuff() + " " + buffInfo.getTurns() + " 回合",
                                "orange",
                                "buffadd",
                                1);
                        message.add(newMsgCreate);
                    }
                }
            }
        }
        return message;
    }


    private BigDecimal wpOrSkDmgUp(String wp, PersonFightInfo offensiveInfo, PersonFightInfo defenseInfo, FightInfo
            fightInfo, BigDecimal finalAtk) {
        WpDic wpDic = wpDicMapper.selectById(wp);
        if ("heal".equals(wpDic.getWpAtkType())) {
            // heal类型标记
            fightInfo.setIfHeal(1);
        }

        // buff类型标记
        if ("2".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("chain")
                    .setPer(25)
                    .setTarget(offensiveInfo.getName())
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("6".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("poison")
                    .setPer(100)
                    .setTarget(defenseInfo.getName())
                    .setTurns(2);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("7".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("restore")
                    .setPer(100)
                    .setTarget(offensiveInfo.getName())
                    .setTurns(2);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("11".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("dizzy")
                    .setPer(50)
                    .setTarget(defenseInfo.getName())
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("16".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("poison")
                    .setPer(100)
                    .setTarget(offensiveInfo.getName())
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("3".equals(wp)) {
            // 只剩下3个以内的武器，伤害+50%，最后一个武器，伤害+200%
            QueryWrapper<WpInfo> wpWrapper = new QueryWrapper<>();
            wpWrapper.eq("user_name", offensiveInfo.getName());
            WpInfo wpInfo = wpInfoMapper.selectOne(wpWrapper);
            String wpHolding = wpInfo.getWpHolding();
            int allWpNum = wpHolding.split(",").length;
            int usedWpNum = offensiveInfo.getUsedWeapon().size();
            if (allWpNum - usedWpNum == 1) {
                finalAtk = finalAtk.add(new BigDecimal("2"));
            } else if (allWpNum - usedWpNum <= 3) {
                finalAtk = finalAtk.add(new BigDecimal("0.5"));
            }
        }

        if ("4".equals(wp)) {
            // 额外-20%~+40%的伤害浮动
            BigDecimal upFactor = new BigDecimal("0.4");
            BigDecimal lowFactor = new BigDecimal("0.2");
            Random factorRandom = new Random();
            BigDecimal upDeviation = upFactor.multiply(finalAtk); // 浮动的上范围
            BigDecimal lowDeviation = lowFactor.multiply(finalAtk); // 浮动的下范围
            BigDecimal upperBound = finalAtk.add(upDeviation); // 上限
            BigDecimal lowerBound = finalAtk.subtract(lowDeviation); // 下限
            finalAtk = lowerBound.add(
                    BigDecimal.valueOf(factorRandom.nextDouble()).multiply(upperBound.subtract(lowerBound)));
        }

        return finalAtk;
    }


    private JSONArray ifTurnContinue(BattleResultInfo battleResultInfo, PersonFightInfo offensiveInfo, PersonFightInfo
            defenseInfo, FightInfo fightInfo, JSONArray message) {
        boolean ifContinue = false;
        while (!ifContinue) {
            if (battleResultInfo.getDefenseInfo().getHp() > 0 && battleResultInfo.getOffensiveInfo().getHp() > 0) {
                ifContinue = true;
                List<String> buffStrList = offensiveInfo.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
                // 判断己方有没有连动，双方是否有眩晕
                if (buffStrList.contains("chain")) {
                    JSONObject newMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 进入连动状态!",
                            "orange",
                            "buffadd",
                            null);
                    message.add(newMsgCreate);
                    BattleResultInfo battleResultInfo1 = this.fightOneTurn(offensiveInfo, defenseInfo, fightInfo);
                    message.addAll(battleResultInfo1.getMessage());
                    ifContinue = false;
                }

                List<String> afterCaDebuffStrList = defenseInfo.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
                if (afterCaDebuffStrList.contains("dizzy")) {
                    JSONObject newMsgCreate = this.newMsgCreate(defenseInfo.getName() + " 眩晕，下回合出手的为 " + offensiveInfo.getName(),
                            "orange",
                            "buffadd",
                            null);
                    message.add(newMsgCreate);
                    BattleResultInfo battleResultInfo1 = this.fightOneTurn(offensiveInfo, defenseInfo, fightInfo);
                    message.addAll(battleResultInfo1.getMessage());
                    ifContinue = false;
                }

                List<String> userDebuffStrList = offensiveInfo.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
                if (userDebuffStrList.contains("dizzy")) {
                    JSONObject newMsgCreate = this.newMsgCreate(offensiveInfo.getName() + " 眩晕，下回合出手的为 " + defenseInfo.getName(),
                            "orange",
                            "buffadd",
                            null);
                    message.add(newMsgCreate);
                    BattleResultInfo battleResultInfo1 = this.fightOneTurn(defenseInfo, offensiveInfo, fightInfo);
                    message.addAll(battleResultInfo1.getMessage());
                    ifContinue = false;
                }
            } else {
                ifContinue = true;
            }
        }
        return message;
    }


    private boolean ifBattleEnd(PersonFightInfo fighter1, PersonFightInfo fighter2, BattleResultInfo resultInfo, JSONArray message, FightInfo fightInfo, String instructions) {
        if (fighter1.getHp() <= 0 && (StrUtil.isNotBlank(instructions) && instructions.contains("3@@"))) {
            int instructionLevel = Integer.parseInt(instructions.split("@@")[1]);
            int maxHP = fighter1.getHtl() * 12 + 20;
            int healNum = new BigDecimal(maxHP).multiply(new BigDecimal("0.05")).multiply(new BigDecimal(instructionLevel)).intValue() + 1;
            JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 装死触发了！抵抗死亡并恢复了 " + healNum + "点HP！",
                    "limegreen",
                    "heal",
                    1);
            message.add(newMsgCreate);
            fighter1.setHp(healNum);
        }

        if (fighter1.getHp() <= 0 || fighter2.getHp() <= 0) {
            String name = fighter1.getName();
            if (name.equals(fightInfo.getOffensiveName())) {
                resultInfo.setOffensiveInfo(fighter1)
                        .setDefenseInfo(fighter2);
            } else {
                resultInfo.setOffensiveInfo(fighter2)
                        .setDefenseInfo(fighter1);
            }
            resultInfo.setMessage(message)
                    .setFightInfo(fightInfo);
            return true;
        }
        return false;
    }


    private JSONArray instructionsEffect(String instructions, PersonFightInfo fighter1, PersonFightInfo fighter2, JSONArray message, FightInfo fightInfo) {
        int instructionLevel = Integer.parseInt(instructions.split("@@")[1]);
        if (fighter1.getName().equals(fightInfo.getOffensiveName())) {
            if (instructions.contains("2@@")) {
                List<BuffInfo> f1Buff = fighter1.getBuff();
                BuffInfo buffInfo = new BuffInfo();
                buffInfo.setBuff("inspire")
                        .setTurns(1 + instructionLevel);
                f1Buff.add(buffInfo);

                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 使用了鼓舞激励之策！",
                        "orange",
                        "buffadd",
                        null);
                message.add(newMsgCreate);

                fighter1.setBuff(f1Buff);
            }

            if (instructions.contains("6@@")) {
                List<BuffInfo> f1Buff = fighter1.getBuff();
                BuffInfo buffInfo = new BuffInfo();
                buffInfo.setBuff("lucky" + instructionLevel)
                        .setTurns(1);
                f1Buff.add(buffInfo);

                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 使用了强运！",
                        "orange",
                        "buffadd",
                        null);
                message.add(newMsgCreate);

                fighter1.setBuff(f1Buff);
            }

            if (instructions.contains("4@@")) {
                Integer htl = fighter1.getHtl();
                int htlMax = 20 + htl * 12;
                if (fighter1.getHp() < htlMax) {
                    int lossHP = htlMax - fighter1.getHp();
                    BigDecimal healPer;
                    if (instructionLevel == 1) {
                        healPer = new BigDecimal("0.1");
                    } else if (instructionLevel == 2) {
                        healPer = new BigDecimal("0.15");
                    } else {
                        healPer = new BigDecimal("0.2");
                    }
                    int healHP = new BigDecimal(lossHP).multiply(new BigDecimal("0.1").multiply(healPer)).intValue() + 1;


                    JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 使用了紧急治疗！，恢复 " + healHP + " 点HP。",
                            "limegreen",
                            "heal",
                            null);
                    message.add(newMsgCreate);

                    fighter1.setHp(fighter1.getHp() + healHP);
                }
            }

            if (instructions.contains("5@@")) {
                List<BuffInfo> f1Buff = fighter1.getBuff();
                int subTurnNum = 1;
                if (instructionLevel == 3) {
                    subTurnNum = 2;
                }
                Iterator<BuffInfo> buffIterator = f1Buff.iterator();
                while (buffIterator.hasNext()) {
                    BuffInfo buffInfo = buffIterator.next();
                    if ("poison".equals(buffInfo.getBuff()) || "fear".equals(buffInfo.getBuff()) || "bleed".equals(buffInfo.getBuff())) {
                        Integer turns = buffInfo.getTurns();
                        if (turns - subTurnNum <= 0) {

                            JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 使用了摆脱，解除了" + buffInfo.getBuff(),
                                    "darkturquoise",
                                    "buffmove",
                                    null);
                            message.add(newMsgCreate);

                            buffIterator.remove();
                        } else {
                            buffInfo.setTurns(turns - subTurnNum);
                        }
                    }
                }

                String healPer = "0.05";
                if (instructionLevel != 1) {
                    healPer = "0.1";
                }
                int maxHP = 20 + fighter1.getHtl() * 12;
                int healHP = new BigDecimal(healPer).multiply(new BigDecimal(maxHP)).intValue() + 1;

                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 使用了摆脱，恢复了  " + healHP + " 点HP。",
                        "limegreen",
                        "heal",
                        null);
                message.add(newMsgCreate);

                fighter1.setHp(fighter1.getHp() + healHP);
            }
        }
        return message;
    }


    private void beforeTurnBuffEffect(PersonFightInfo fighter1, PersonFightInfo fighter2) {
        List<BuffInfo> buff = fighter1.getBuff();
        Iterator<BuffInfo> buffIterator = buff.iterator();
        while (buffIterator.hasNext()) {
            BuffInfo buffInfo = buffIterator.next();
            if ("chain".equals(buffInfo.getBuff())) {
                int turns = buffInfo.getTurns() - 1;
                if (turns == 0) {
                    buffIterator.remove();
                } else {
                    buffInfo.setTurns(turns);
                }
            }
        }

        List<BuffInfo> debuff = fighter2.getBuff();
        Iterator<BuffInfo> debuffIterator = debuff.iterator();
        while (debuffIterator.hasNext()) {
            BuffInfo buffInfo = debuffIterator.next();
            if ("dizzy".equals(buffInfo.getBuff())) {
                int turns = buffInfo.getTurns() - 1;
                if (turns == 0) {
                    debuffIterator.remove();
                } else {
                    buffInfo.setTurns(turns);
                }
            }
        }
    }


    private void calBuffEffect(PersonFightInfo fighter1, PersonFightInfo fighter2, JSONArray message) {
        // 检查buff列表并生效
        List<String> buffStrList = fighter1.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());

        // Settle body buff (restore) , debuff (bleed)
        if (buffStrList.contains("restore")) {
            // restore hp*1.1
            Integer hp = fighter1.getHp();
            BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.1"));
            int restoreHp = multiply.intValue() + 1;
            fighter1.setHp(hp + restoreHp);

            JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + "因为恢复效果，回复了 " + restoreHp + "点HP。",
                    "limegreen",
                    "heal",
                    null);
            message.add(newMsgCreate);
        }

        if (buffStrList.contains("bleed")) {
            // bleed hp*0.9
            Integer hp = fighter1.getHp();
            if (hp > 1) {
                BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.1"));
                int bleedDmg = multiply.intValue() + 1;
                fighter1.setHp(hp - bleedDmg);

                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + "因为流血效果，损失了 " + bleedDmg + "点HP。",
                        "darkred",
                        "bleed",
                        null);
                message.add(newMsgCreate);
            } else {
                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + "因为血量太低，没受到流血的损伤。",
                        "darkred",
                        "bleed",
                        null);
                message.add(newMsgCreate);
            }
        }

        if (buffStrList.contains("poison")) {
            // poison hp*0.95,-10%final atk
            Integer hp = fighter1.getHp();
            if (hp > 1) {
                BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.05"));
                int poisonDmg = multiply.intValue();
                fighter1.setHp(hp - poisonDmg + 1);

                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + "因为中毒效果，损失了 " + poisonDmg + "点生命。",
                        "darkgreen",
                        "poison",
                        null);
                message.add(newMsgCreate);
            } else {
                // 因为只剩1点血，不会受到中毒扣血
                JSONObject newMsgCreate = this.newMsgCreate(fighter1.getName() + " 因为血量太低，没受到中毒的损伤。",
                        "darkgreen",
                        "poison",
                        null);
                message.add(newMsgCreate);
            }
        }

        // 除了眩晕和连动（忽略回合），进攻方所有剩余buff回合-1
        List<BuffInfo> f1BuffList = fighter1.getBuff();
        Iterator<BuffInfo> f1BuffIterator = f1BuffList.iterator();
        while (f1BuffIterator.hasNext()) {
            BuffInfo buffInfo = f1BuffIterator.next();
            if (!"chain".equals(buffInfo.getBuff()) && !"dizzy".equals(buffInfo.getBuff())) {
                int turns = buffInfo.getTurns() - 1;
                if (turns == 0) {
                    f1BuffIterator.remove();
                } else {
                    buffInfo.setTurns(turns);
                }
            }
        }
        fighter1.setBuff(f1BuffList);
    }

    private JSONObject newMsgCreate(String msg, String color, String type, Integer level) {
        if (StrUtil.isBlank(color)) {
            color = "black";
        }
        if (StrUtil.isBlank(type)) {
            type = "normal";
        }
        if (level == null) {
            level = 0;
        }

        // 基本消息构造
        JSONObject newMessage = new JSONObject();
        newMessage.put("msg", msg);
        newMessage.put("color", color);
        newMessage.put("type", type);
        newMessage.put("level", level);

        // 消息停留时长
        int ms = 0;
        if ("cri".equals(type) || "buffadd".equals(type) || "buffmove".equals(type) || "normal".equals(type)) {
            ms += 600;
        } else if ("dmg".equals(type) || "heal".equals(type)) {
            ms += 900;
        } else {
            ms += 300;
        }
        newMessage.put("ms", ms);
        return newMessage;
    }
}