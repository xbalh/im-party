package com.im.imparty.geometryChaos.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.geometryChaos.mapper.BattleInfoMapper;
import com.im.imparty.geometryChaos.mapper.PersonInfoMapper;
import com.im.imparty.geometryChaos.mapper.WpInfoMapper;
import com.im.imparty.geometryChaos.service.GeometryChaosMainService;
import com.im.imparty.geometryChaos.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class GeometryChaosMainServiceImpl implements GeometryChaosMainService {

    @Autowired
    private BattleInfoMapper battleInfoMapper;

    @Autowired
    private PersonInfoMapper personInfoMapper;

    @Autowired
    private WpInfoMapper wpInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonInfo createCharacter(String userName) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 5, 5, 5, 5)); // 初始列表
        int sum = 8; // 需要添加的总数
        Random random = new Random();
        while (sum > 0) {
            // 随机选择一个数字
            int index = random.nextInt(numbers.size());
            // 最多只能加到10，确保不会超过10
            int addNum = Math.min(sum, 10 - numbers.get(index));
            numbers.set(index, numbers.get(index) + addNum);
            sum -= addNum;
        }
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserName(userName)
                .setLv(1)
                .setExpe(0)
                .setStr(numbers.get(0))
                .setAgi(numbers.get(1))
                .setWit(numbers.get(2))
                .setSpd(numbers.get(3))
                .setHtl(numbers.get(4))
                .setYb(0);
        // save PersonInfo
        int insertResult = personInfoMapper.insert(personInfo);
        if (insertResult == 0) {
            log.error("创建时出错 {}", personInfo);
        }
        return personInfo;
    }


    @Override
    public PersonFightInfo getUserFightInfo(String userName) {
        PersonInfo personInfo = personInfoMapper.selectById(userName);
        WpInfo wpInfo = wpInfoMapper.selectById(userName);
        PersonFightInfo personFightInfo = new PersonFightInfo();
        Integer str = personInfo.getStr();
        Integer agi = personInfo.getAgi();
        Integer spd = personInfo.getSpd();
        Integer wit = personInfo.getWit();
        Integer htl = personInfo.getHtl();

        // TODO skInfo 改变基础fightInfo
        personFightInfo.setName(personInfo.getUserName())
                .setWpInfo(wpInfo)
                .setHp(htl * 5)
                .setStr(str)
                .setWit(wit)
                .setSpd(spd)
                .setAgi(agi)
                .setBlk(agi * 100)
                .setBlkPer(30)
                .setCri(agi * 100)
                .setCriPer(50)
                .setCa(500)
                .setFinalAtk(new BigDecimal("1"))
                .setFinalDef(new BigDecimal("1"))
                .setUsedWeapon(new ArrayList<>())
                .setBuff(new ArrayList<>())
                .setDebuff(new ArrayList<>());
        return personFightInfo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String fightStart(PersonFightInfo user, PersonFightInfo enemy, FightInfo fightInfo) {
        BattleInfo battleInfo = new BattleInfo();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        battleInfo.setId(uuid)
                .setOffensiveInfo(JSON.toJSON(user).toString())
                .setDefenseInfo(JSON.toJSON(enemy).toString())
                .setFightInfo(JSON.toJSON(fightInfo).toString())
                .setCreateTime(new Date())
                .setRound(1);
        int insertResult = battleInfoMapper.insert(battleInfo);
        if (insertResult == 0) {
            log.error("创建时出错 {}", battleInfo);
        }

        return uuid;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startOneTurn(TurnInfo turnInfo) {
        String uuid = turnInfo.getUuid();
        BattleInfo battleInfo = battleInfoMapper.selectById(uuid);
        PersonFightInfo offensiveInfo = JSONObject.parseObject(battleInfo.getOffensiveInfo(), PersonFightInfo.class);
        PersonFightInfo defenseInfo = JSONObject.parseObject(battleInfo.getDefenseInfo(), PersonFightInfo.class);
        FightInfo fightInfo = JSONObject.parseObject(battleInfo.getFightInfo(), FightInfo.class);
        fightInfo.setInstructions(turnInfo.getInstructions());
        Integer round = battleInfo.getRound();

        if (offensiveInfo.getHp() > 0 && defenseInfo.getHp() > 0 && fightInfo.getIfVictory() != null) {
            round++;
            log.info("" + round + "turn start：");

            // 根据 speed 大小交替
            if (offensiveInfo.getSpd() > defenseInfo.getSpd()) {
                BattleResultInfo battleResultInfo = this.fightOneTurn(offensiveInfo, defenseInfo, fightInfo);
                log.info(battleResultInfo.getMessage());

                if (battleResultInfo.getDefenseInfo().getHp() > 0) {
                    BattleResultInfo otherSideBattleResultInfo = this.fightOneTurn(battleResultInfo.getDefenseInfo(), battleResultInfo.getOffensiveInfo(), fightInfo);
                    log.info(otherSideBattleResultInfo.getMessage());
                    // 双方互换了
                    battleInfo.setOffensiveInfo(JSON.toJSON(otherSideBattleResultInfo.getDefenseInfo()).toString())
                            .setDefenseInfo(JSON.toJSON(otherSideBattleResultInfo.getOffensiveInfo()).toString())
                            .setFightInfo(JSON.toJSON(otherSideBattleResultInfo.getFightInfo()).toString());
                }
            } else {
                BattleResultInfo battleResultInfo = this.fightOneTurn(defenseInfo, offensiveInfo, fightInfo);
                log.info(battleResultInfo.getMessage());
                if (battleResultInfo.getOffensiveInfo().getHp() > 0) {
                    BattleResultInfo otherSideBattleResultInfo = this.fightOneTurn(battleResultInfo.getOffensiveInfo(), battleResultInfo.getDefenseInfo(), fightInfo);
                    log.info(otherSideBattleResultInfo.getMessage());
                    // 双方互换了
                    battleInfo.setOffensiveInfo(JSON.toJSON(otherSideBattleResultInfo.getOffensiveInfo()).toString())
                            .setDefenseInfo(JSON.toJSON(otherSideBattleResultInfo.getDefenseInfo()).toString())
                            .setFightInfo(JSON.toJSON(otherSideBattleResultInfo.getFightInfo()).toString());
                }
            }
        }

        // return result
        offensiveInfo = JSONObject.parseObject(battleInfo.getOffensiveInfo(), PersonFightInfo.class);
        defenseInfo = JSONObject.parseObject(battleInfo.getDefenseInfo(), PersonFightInfo.class);
        fightInfo = JSONObject.parseObject(battleInfo.getFightInfo(), FightInfo.class);
        if (offensiveInfo.getHp() <= 0) {
            return "user defeat";
        } else if (defenseInfo.getHp() <= 0) {
            return "user victory";
        } else {
            String uuidNew = UUID.randomUUID().toString().replaceAll("-", "");
            battleInfo.setId(uuidNew)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setMessage(battleInfo.getMessage());
            int insertResult = battleInfoMapper.insert(battleInfo);
            if (insertResult == 0) {
                log.error("创建时出错 {}", battleInfo);
            }
            return uuid;
        }
    }


    private BattleResultInfo fightOneTurn(PersonFightInfo fighter1, PersonFightInfo fighter2, FightInfo fightInfo) {
        // TODO 主动指令
        String instructions = fightInfo.getInstructions();

        // 如果P2有Debuff，在本回合开始前清除掉
        List<BuffInfo> debuff = fighter2.getDebuff();
        Iterator<BuffInfo> iterator = debuff.iterator();
        while (iterator.hasNext()) {
            BuffInfo buffInfo = iterator.next();
            if ("dizzy".equals(buffInfo.getBuff())) {
                int turns = buffInfo.getTurns() - 1;
                if (turns == 0) {
                    iterator.remove();
                } else {
                    buffInfo.setTurns(turns);
                }
            }
        }

        BattleResultInfo btInfo = new BattleResultInfo();
        StringBuilder message = new StringBuilder();

        // To determine whether the great inventor is effective or not
        String userSkInfo = this.getUserSkInfo(fighter1);
        String wp = "";
        if (userSkInfo.contains("wm")) {
            Random random = new Random();
            // Select a wp, randomly select one that excludes the weapon that has been used, if not, empty-handed
            if (random.nextInt(100) < 3) {
                // Select two wp
                wp = this.getUserRandomWpInfo(fighter1, 2);
            } else {
                wp = this.getUserRandomWpInfo(fighter1, 1);
            }
        } else {
            wp = this.getUserRandomWpInfo(fighter1, 1);
        }

        // Determines whether crit, if Crit, FINALATK * (1 + Criper)
        for (String oneWp : wp.split(",")) {
            this.btSettlement(fightInfo, fighter1, fighter2, oneWp, message);
            // Decide once both sides hp
            if (fighter1.getHp() <= 0 || fighter2.getHp() <= 0) {
                btInfo.setMessage(message.toString())
                        .setOffensiveInfo(fighter1)
                        .setDefenseInfo(fighter2)
                        .setFightInfo(fightInfo);
                return btInfo;
            }
            // Whether or not the weapon effects are triggered
            this.wpBuffeffect(fightInfo, fighter1, fighter2, wp, message);
        }

        // Determine whether to fight back (counterAttack)
        Random random = new Random();
        List<String> debuffStrList = fighter2.getDebuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
        if (!debuffStrList.contains("dizzy") && random.nextInt(10000) < fighter2.getCa()) {
            String enemyRandomWpInfo = this.getUserRandomWpInfo(fighter2, 1);
            this.btSettlement(fightInfo, fighter2, fighter1, enemyRandomWpInfo, message);
        }

        // Decide once both sides hp
        if (fighter1.getHp() <= 0 || fighter2.getHp() <= 0) {
            btInfo.setMessage(message.toString())
                    .setOffensiveInfo(fighter1)
                    .setDefenseInfo(fighter2)
                    .setFightInfo(fightInfo);
            return btInfo;
        }

        // Determine whether the enemy is dazed, and if so, move again and undaze the enemy
        List<String> afterCaDebuffStrList = fighter2.getDebuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
        if (afterCaDebuffStrList.contains("dizzy")) {
            // message
            this.fightOneTurn(fighter1, fighter2, fightInfo);
        }

        // Determine if you can act again (Fighter 1 has a chain)
        List<String> buffStrList = fighter1.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());

        // user
        if (buffStrList.contains("restore")) {
            // restore hp*1.1
            Integer hp = fighter1.getHp();
            BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.1"));
            int restoreHp = multiply.intValue() + 1;
            fighter1.setHp(hp + restoreHp);
            // message
            message.append("\n").append("1 restore , +").append(restoreHp);
        }

        if (buffStrList.contains("chain")) {
            // message
            this.fightOneTurn(fighter1, fighter2, fightInfo);
        }

        // Settle body buff (restore) , debuff (bleed)
        // enemy
        if (afterCaDebuffStrList.contains("bleed")) {
            // bleed hp*0.9
            Integer hp = fighter2.getHp();
            if (hp > 1) {
                BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.1"));
                int bleedDmg = multiply.intValue();
                fighter2.setHp(hp - bleedDmg);
                // message
                message.append("\n").append("2 bleed , -").append(bleedDmg);
            } else {
                message.append("\n").append("2 bleed , but 2 hp = 1");
            }
        }

        btInfo.setMessage(message.toString())
                .setOffensiveInfo(fighter1)
                .setDefenseInfo(fighter2)
                .setFightInfo(fightInfo);
        return btInfo;
    }

    private String getUserSkInfo(PersonFightInfo user) {
        // Load user skill info
        return "skInfo";
    }

    private String getUserRandomWpInfo(PersonFightInfo user, Integer getNum) {
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

        for (String wp : emptySb.toString().split(",")) {
            String randomWp = this.getRandomNonIntersectingString(wpInfoList, usedWeapon);
            if (randomWp != null) {
                if (resultSb.length() == 0) {
                    resultSb.append(wp);
                } else {
                    resultSb.append(",").append(wp);
                }
            } else {
                if (resultSb.length() == 0) {
                    resultSb.append("empty");
                } else {
                    resultSb.append(",").append("empty");
                }
            }
        }
        return resultSb.toString();
    }


    private String getRandomNonIntersectingString(List<String> wpInfoList, List<String> usedWeapon) {
        List<String> nonIntersecting = new ArrayList<>(wpInfoList);
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


    private FightInfo getWpFinalAtk(PersonFightInfo personInfo, String wp, FightInfo fightInfo) {
        BigDecimal finalDmg = new BigDecimal(0);
        Integer str = personInfo.getStr();
        Integer wit = personInfo.getWit();
        Integer agi = personInfo.getAgi();
        Integer spd = personInfo.getSpd();
        Integer hlt = personInfo.getHlt();

        Integer cri = personInfo.getCri();

        Random random = new Random();
        Integer criPer = personInfo.getCriPer();
        BigDecimal finalAtk = personInfo.getFinalAtk();
        if (random.nextInt(10000) < cri) {
            finalAtk = finalAtk.multiply(new BigDecimal(1).add(new BigDecimal(criPer).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
        }

        WpDic wpDic = new WpDic();
        Integer wpInfoStr = wpDic.getStr();
        BigDecimal wpStr = new BigDecimal(wpInfoStr).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        finalDmg = finalDmg.add(wpStr.multiply(new BigDecimal(str)));

        Integer wpInfoWit = wpDic.getWit();
        BigDecimal wpWit = new BigDecimal(wpInfoWit).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        finalDmg = finalDmg.add(wpWit.multiply(new BigDecimal(wit)));

        Integer wpInfoAgi = wpDic.getAgi();
        BigDecimal wpAgi = new BigDecimal(wpInfoAgi).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        finalDmg = finalDmg.add(wpAgi.multiply(new BigDecimal(agi)));

        Integer wpInfoSpd = wpDic.getSpd();
        BigDecimal wpSpd = new BigDecimal(wpInfoSpd).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        finalDmg = finalDmg.add(wpSpd.multiply(new BigDecimal(spd)));

        Integer wpInfoHlt = wpDic.getHlt();
        BigDecimal wpHlt = new BigDecimal(wpInfoHlt).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        finalDmg = finalDmg.add(wpHlt.multiply(new BigDecimal(hlt)));

        // heal类型，添加标记

        // de类型，添加标记

        // iv类型，添加标记

        finalDmg = finalDmg.multiply(finalAtk);
        fightInfo.setFinalNum(finalDmg.intValue() + 1);
        return fightInfo;
    }


    private void getFighterFinalDef(PersonFightInfo personInfo, String wp, FightInfo wpFinalAtk) {
        Integer blk = personInfo.getBlk();
        Integer blkPer = personInfo.getBlkPer();
        BigDecimal finalDef = personInfo.getFinalDef();

        Random random = new Random();
        if (random.nextInt(10000) < blk) {
            finalDef = finalDef.multiply(new BigDecimal(1).subtract(new BigDecimal(blkPer).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
        }
        BigDecimal def = new BigDecimal(wpFinalAtk.getFinalNum()).multiply(finalDef);
        wpFinalAtk.setFinalNum(def.intValue() + 1);
    }


    private void btSettlement(FightInfo fightInfo, PersonFightInfo offensiveInfo, PersonFightInfo defenseInfo, String wpInfo, StringBuilder message) {
        FightInfo wpFinalAtk = this.getWpFinalAtk(offensiveInfo, wpInfo, fightInfo);
        if (wpFinalAtk.getIfHeal() == 1) {
            offensiveInfo.setHp(offensiveInfo.getHp() + wpFinalAtk.getFinalNum());
            message.append("1 use ").append(wpInfo).append("to 1 heal").append(wpFinalAtk.getFinalNum());
        } else {
            // determines if blocked, if blocked, FINALATK * (1-enemy blkPer)
            this.getFighterFinalDef(defenseInfo, wpInfo, wpFinalAtk);
            defenseInfo.setHp(defenseInfo.getHp() - wpFinalAtk.getFinalNum());
            message.append("1 use ").append(wpInfo).append("to 2 attack").append(wpFinalAtk.getFinalNum());
        }
        List<String> usedWeapon = offensiveInfo.getUsedWeapon();
        usedWeapon.add(wpInfo);
        offensiveInfo.setUsedWeapon(usedWeapon);
    }


    private void wpBuffeffect(FightInfo fightInfo, PersonFightInfo fighter1, PersonFightInfo fighter2, String wpInfo, StringBuilder message) {
        // If wp buff effect
    }
}
