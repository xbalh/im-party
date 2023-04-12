package com.im.imparty.geometryChaos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.geometryChaos.mapper.BattleInfoMapper;
import com.im.imparty.geometryChaos.mapper.UserStaticInfoMapper;
import com.im.imparty.geometryChaos.mapper.WpDicMapper;
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
    private UserStaticInfoMapper userStaticInfoMapper;

    @Autowired
    private WpInfoMapper wpInfoMapper;

    @Autowired
    private WpDicMapper wpDicMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserStaticInfo createCharacter(String userName) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(4, 4, 4, 4, 4)); // 初始列表
        int sum = 18; // 需要添加的总数
        Random random = new Random();
        while (sum > 0) {
            // 随机选择一个数字
            int index = random.nextInt(numbers.size());
            if (numbers.get(index) >= 12) {
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
                .setYb(0);
        // save PersonInfo
        int insertResult = userStaticInfoMapper.insert(userStaticInfo);
        if (insertResult == 0) {
            log.error("创建时出错 {}", userStaticInfo);
        }
        return userStaticInfo;
    }


    @Override
    public PersonFightInfo getUserFightInfo(String userName) {
        QueryWrapper<UserStaticInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        UserStaticInfo userStaticInfo = userStaticInfoMapper.selectOne(wrapper);

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
                .setHp(40 + htl * 20)
                .setStr(str)
                .setWit(wit)
                .setSpd(spd)
                .setAgi(agi)
                .setHtl(htl)
                .setBlk(agi * 100)
                .setBlkPer(30)
                .setCri(agi * 100)
                .setCriPer(50 + agi * 2)
                .setCa(500)
                .setFinalAtk(new BigDecimal("1"))
                .setFinalDef(new BigDecimal("1"))
                .setUsedWeapon(new ArrayList<>())
                .setBuff(new ArrayList<>());
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
        fightInfo.setInstructions(turnInfo.getInstructions());
        Integer round = battleInfo.getRound();
        String message = battleInfo.getMessage();

        if (offensiveInfo.getHp() > 0 && defenseInfo.getHp() > 0 && fightInfo.getIfVictory() == null) {
            round++;
            fightInfo.setRound(round);
            message = "<br>turn " + round + " start：";
            if (round >= 20) {
                message = message + "round >=20 settlement *10";
            } else if (round >= 15) {
                message = message + "round >=15 settlement *5";
            } else if (round >= 10) {
                message = message + "round >=10 settlement *2";
            }

            // 根据 speed 大小交替
            if (offensiveInfo.getSpd() >= defenseInfo.getSpd()) {
                BattleResultInfo battleResultInfo = this.fightOneTurn(offensiveInfo, defenseInfo, fightInfo);
                log.info(battleResultInfo.getMessage());
                message = message + battleResultInfo.getMessage();
                if (battleResultInfo.getDefenseInfo().getHp() > 0) {
                    BattleResultInfo otherSideBattleResultInfo = this.fightOneTurn(battleResultInfo.getDefenseInfo(), battleResultInfo.getOffensiveInfo(), fightInfo);
                    log.info(otherSideBattleResultInfo.getMessage());
                    message = message + otherSideBattleResultInfo.getMessage();
                    // 双方互换了
                    battleInfo.setOffensiveInfo(JSON.toJSON(otherSideBattleResultInfo.getDefenseInfo()).toString())
                            .setDefenseInfo(JSON.toJSON(otherSideBattleResultInfo.getOffensiveInfo()).toString())
                            .setFightInfo(JSON.toJSON(otherSideBattleResultInfo.getFightInfo()).toString());
                }
            } else {
                BattleResultInfo battleResultInfo = this.fightOneTurn(defenseInfo, offensiveInfo, fightInfo);
                log.info(battleResultInfo.getMessage());
                message = message + battleResultInfo.getMessage();
                if (battleResultInfo.getOffensiveInfo().getHp() > 0) {
                    BattleResultInfo otherSideBattleResultInfo = this.fightOneTurn(battleResultInfo.getOffensiveInfo(), battleResultInfo.getDefenseInfo(), fightInfo);
                    log.info(otherSideBattleResultInfo.getMessage());
                    message = message + otherSideBattleResultInfo.getMessage();
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
            battleInfo.setId(uuid)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setMessage(message + "<br>user defeat !!!");
            return battleInfo;
        } else if (defenseInfo.getHp() <= 0) {
            battleInfo.setId(uuid)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setMessage(message + "<br>user victory !!!");
            return battleInfo;
        } else {
            String uuidNew = UUID.randomUUID().toString().replaceAll("-", "");
            battleInfo.setId(uuidNew)
                    .setOffensiveInfo(JSON.toJSON(offensiveInfo).toString())
                    .setDefenseInfo(JSON.toJSON(defenseInfo).toString())
                    .setFightInfo(JSON.toJSON(fightInfo).toString())
                    .setCreateTime(new Date())
                    .setRound(round)
                    .setMessage(message);
            int insertResult = battleInfoMapper.insert(battleInfo);
            if (insertResult == 0) {
                log.error("创建时出错 {}", battleInfo);
            }
            return battleInfo;
        }
    }


    private BattleResultInfo fightOneTurn(PersonFightInfo fighter1, PersonFightInfo fighter2, FightInfo fightInfo) {
        // TODO 主动指令
        String instructions = fightInfo.getInstructions();

        // 如果P1有chain/P2有dizzy，在本回合开始前-1
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
            String btSettlement = this.btSettlement(fightInfo, fighter1, fighter2, oneWp, new StringBuilder());
            message.append(btSettlement);
            // Decide once both sides hp
            if (fighter1.getHp() <= 0 || fighter2.getHp() <= 0) {
                btInfo.setMessage(btSettlement)
                        .setOffensiveInfo(fighter1)
                        .setDefenseInfo(fighter2)
                        .setFightInfo(fightInfo);
                return btInfo;
            }
            // Whether or not the weapon effects are triggered
            String buffMessage = this.wpBuffeffect(fightInfo, fighter1, fighter2, wp, new StringBuilder());
            message.append(buffMessage);
        }

        // Determine whether to fight back (counterAttack)
        if (!"10".equals(wp)) {
            Random random = new Random();
            List<String> debuffStrList = fighter2.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());

            if (!debuffStrList.contains("dizzy") && random.nextInt(10000) < fighter2.getCa()) {
                String enemyRandomWpInfo = this.getUserRandomWpInfo(fighter2, 1);
                String btSettlement = this.btSettlement(fightInfo, fighter2, fighter1, enemyRandomWpInfo, new StringBuilder());
                message.append(btSettlement);
            }
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
        List<String> afterCaDebuffStrList = fighter2.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());
        if (afterCaDebuffStrList.contains("dizzy")) {
            // message
            message.append("<br>").append(fighter2.getName()).append(" dizzy, next turn is ").append(fighter1.getName());
            this.fightOneTurn(fighter1, fighter2, fightInfo);
        }

        // Determine if you can act again (Fighter 1 has a chain)
        List<String> buffStrList = fighter1.getBuff().stream().map(BuffInfo::getBuff).collect(Collectors.toList());

        // Settle body buff (restore) , debuff (bleed)
        if (buffStrList.contains("restore")) {
            // restore hp*1.1
            Integer hp = fighter1.getHp();
            BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.1"));
            int restoreHp = multiply.intValue() + 1;
            fighter1.setHp(hp + restoreHp);
            // message
            message.append("<br>").append(fighter1.getName()).append(" restore , +").append(restoreHp);
        }

        if (buffStrList.contains("chain")) {
            message.append("<br>").append(fighter1.getName()).append(" chain!");
            // message
            this.fightOneTurn(fighter1, fighter2, fightInfo);
        }

        if (buffStrList.contains("bleed")) {
            // bleed hp*0.9
            Integer hp = fighter1.getHp();
            if (hp > 1) {
                BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.1"));
                int bleedDmg = multiply.intValue();
                fighter1.setHp(hp - bleedDmg);
                // message
                message.append("<br>").append(fighter1.getName()).append(" bleed , -").append(bleedDmg);
            } else {
                message.append("<br>").append(fighter1.getName()).append(" bleed , but it hp = 1");
            }
        }

        if (buffStrList.contains("poison")) {
            // poison hp*0.95
            Integer hp = fighter1.getHp();
            if (hp > 1) {
                BigDecimal multiply = new BigDecimal(hp).multiply(new BigDecimal("0.05"));
                int poisonDmg = multiply.intValue();
                fighter1.setHp(hp - poisonDmg);
                // message
                message.append("<br>").append(fighter1.getName()).append(" poison , -").append(poisonDmg);
            } else {
                message.append("<br>").append(fighter1.getName()).append(" poison , but it hp = 1");
            }
        }

        // 所有buff回合-1
        List<BuffInfo> f1BuffList = fighter1.getBuff();
        Iterator<BuffInfo> f1BuffIterator = f1BuffList.iterator();
        while (f1BuffIterator.hasNext()) {
            BuffInfo buffInfo = f1BuffIterator.next();
            int turns = buffInfo.getTurns() - 1;
            if (turns == 0) {
                f1BuffIterator.remove();
            } else {
                buffInfo.setTurns(turns);
            }
        }
        fighter1.setBuff(f1BuffList);

        btInfo.setMessage(message.toString())
                .setOffensiveInfo(fighter1)
                .setDefenseInfo(fighter2)
                .setFightInfo(fightInfo);
        return btInfo;
    }

    private String getUserSkInfo(PersonFightInfo user) {
        // TODO Load user skill info
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
                    resultSb.append(randomWp);
                } else {
                    resultSb.append(",").append(randomWp);
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


    private FightInfo getWpFinalAtk(PersonFightInfo offensiveInfo, PersonFightInfo defenseInfo, String wp, FightInfo fightInfo) {
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
        if (random.nextInt(10000) < cri) {
            finalAtk = finalAtk.multiply(new BigDecimal(1).add(new BigDecimal(criPer).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));

            // 如果速度有领先，每1点+1%伤害
            if (spd > defenseSpd) {
                finalAtk = finalAtk.add(new BigDecimal("0.01").multiply(new BigDecimal(spd - defenseSpd)));
            }

            // wp/sk影响
            finalAtk = this.wpOrSkDmgUp(wp, offensiveInfo, fightInfo, finalAtk);

            // 上下有10%的浮动
            BigDecimal factor = new BigDecimal("0.1");
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
        }

        WpDic wpDic = wpDicMapper.selectById(wp);
        Integer wpInfoStr = wpDic.getWpStr();
        if (wpInfoStr != null) {
            BigDecimal wpStr = new BigDecimal(wpInfoStr).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            finalDmg = finalDmg.add(wpStr.multiply(new BigDecimal(str)));
        }

        Integer wpInfoWit = wpDic.getWpWit();
        if (wpInfoWit != null) {
            BigDecimal wpWit = new BigDecimal(wpInfoWit).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            finalDmg = finalDmg.add(wpWit.multiply(new BigDecimal(wit)));
        }

        Integer wpInfoAgi = wpDic.getWpAgi();
        if (wpInfoAgi != null) {
            BigDecimal wpAgi = new BigDecimal(wpInfoAgi).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            finalDmg = finalDmg.add(wpAgi.multiply(new BigDecimal(agi)));
        }
//
//        Integer wpInfoSpd = wpDic.get();
//        if (wpInfoSpd != null) {
//            BigDecimal wpSpd = new BigDecimal(wpInfoSpd).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
//            finalDmg = finalDmg.add(wpSpd.multiply(new BigDecimal(spd)));
//        }

        Integer wpInfoHlt = wpDic.getWpHlt();
        if (wpInfoHlt != null) {
            BigDecimal wpHlt = new BigDecimal(wpInfoHlt).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            finalDmg = finalDmg.add(wpHlt.multiply(new BigDecimal(hlt)));
        }

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
        wpFinalAtk.setFinalNum(def.intValue());
    }


    private String btSettlement(FightInfo fightInfo, PersonFightInfo offensiveInfo, PersonFightInfo defenseInfo, String wpInfo, StringBuilder message) {
        Random random = new Random();
        // 12、19有命中率影响
        if ((!"12".equals(wpInfo) && !"19".equals(wpInfo)) || ("12".equals(wpInfo) && random.nextInt(100) < 50) || ("19".equals(wpInfo) && random.nextInt(100) < 99)) {
            int attackNum = 1;
            if ("5".equals(wpInfo)) {
                // 5hits
                attackNum = 5;
            }
            for (int i = 0; i < attackNum; i++) {
                FightInfo wpFinalAtk = this.getWpFinalAtk(offensiveInfo, defenseInfo, wpInfo, fightInfo);
                if (wpFinalAtk.getIfHeal() != null && wpFinalAtk.getIfHeal() == 1) {
                    offensiveInfo.setHp(offensiveInfo.getHp() + wpFinalAtk.getFinalNum());
                    message.append("<br>").append(offensiveInfo.getName()).append(" use ").append(wpInfo).append("to ").append(offensiveInfo.getName()).append(" heal").append(wpFinalAtk.getFinalNum());
                } else {
                    // determines if blocked, if blocked, FINALATK * (1-enemy blkPer)
                    this.getFighterFinalDef(defenseInfo, wpInfo, wpFinalAtk);
                    defenseInfo.setHp(defenseInfo.getHp() - wpFinalAtk.getFinalNum());
                    message.append("<br>").append(offensiveInfo.getName()).append(" use wp ").append(wpInfo).append(" to ").append(defenseInfo.getName()).append(" attack ").append(wpFinalAtk.getFinalNum());
                    if ("9".equals(wpInfo)) {
                        // heal self HP
                        offensiveInfo.setHp(offensiveInfo.getHp() + wpFinalAtk.getFinalNum());
                        message.append("<br>").append(offensiveInfo.getName()).append(" use ").append(wpInfo).append("to ").append(offensiveInfo.getName()).append(" heal").append(wpFinalAtk.getFinalNum());
                    }

                    if ("13".equals(wpInfo)) {
                        // 自身损失造成伤害的20%
                        Integer finalNum = wpFinalAtk.getFinalNum();
                        BigDecimal multiply = new BigDecimal(finalNum).multiply(new BigDecimal("0.2"));
                        offensiveInfo.setHp(offensiveInfo.getHp() - multiply.intValue());
                        message.append("<br>").append(offensiveInfo.getName()).append(" use wp ").append(wpInfo).append(" to ").append(offensiveInfo.getName()).append(" attack ").append(multiply.intValue());
                    }
                }
            }
        } else {
            message.append("<br>").append(offensiveInfo.getName()).append(" use wp ").append(wpInfo).append(" attack miss! ");
        }

        List<String> usedWeapon = offensiveInfo.getUsedWeapon();
        usedWeapon.add(wpInfo);
        offensiveInfo.setUsedWeapon(usedWeapon);
        return message.toString();
    }


    private String wpBuffeffect(FightInfo fightInfo, PersonFightInfo fighter1, PersonFightInfo fighter2, String wpInfo, StringBuilder message) {
        // If wp buff effect
        List<BuffInfo> f1Buff = fighter1.getBuff();
        List<BuffInfo> f2Buff = fighter2.getBuff();
        List<BuffUpInfo> ifBuff = fightInfo.getIfBuff();
        if (ifBuff != null) {
            for (BuffUpInfo buffUpInfo : ifBuff) {
                Integer target = buffUpInfo.getTarget();
                Random random = new Random();
                if (target == 0) {
                    if (random.nextInt(100) <= buffUpInfo.getPer()) {
                        BuffInfo buffInfo = new BuffInfo();
                        buffInfo.setBuff(buffUpInfo.getBuff())
                                .setTurns(buffUpInfo.getTurns());
                        f1Buff.add(buffInfo);
                        fighter1.setBuff(f1Buff);
                        message.append("<br>").append(fighter1.getName()).append(" add buff ").append(buffInfo.getBuff()).append(buffInfo.getTurns()).append("turn");
                    }
                }
                if (target == 1) {
                    if (random.nextInt(100) <= buffUpInfo.getPer()) {
                        BuffInfo buffInfo = new BuffInfo();
                        buffInfo.setBuff(buffUpInfo.getBuff())
                                .setTurns(buffUpInfo.getTurns());
                        f2Buff.add(buffInfo);
                        fighter2.setBuff(f2Buff);
                        message.append("<br>").append(fighter2.getName()).append(" add buff ").append(buffInfo.getBuff()).append(buffInfo.getTurns()).append("turn");
                    }
                }
            }
        }
        return message.toString();
    }


    private BigDecimal wpOrSkDmgUp(String wp, PersonFightInfo offensiveInfo, FightInfo fightInfo, BigDecimal finalAtk) {
        if ("1".equals(wp) || "6".equals(wp) || "16".equals(wp) || "17".equals(wp)) {
            // heal类型标记
            fightInfo.setIfHeal(1);
        }

        // buff类型标记
        if ("2".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("chain")
                    .setPer(20)
                    .setTarget(0)
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("6".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("poison")
                    .setPer(100)
                    .setTarget(1)
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("7".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("restore")
                    .setPer(100)
                    .setTarget(0)
                    .setTurns(2);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("11".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("dizzy")
                    .setPer(50)
                    .setTarget(1)
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("16".equals(wp)) {
            BuffUpInfo buffUpInfo = new BuffUpInfo();
            buffUpInfo.setBuff("poison")
                    .setPer(100)
                    .setTarget(0)
                    .setTurns(1);
            List<BuffUpInfo> resultList = new ArrayList<>();
            resultList.add(buffUpInfo);
            fightInfo.setIfBuff(resultList);
        }

        if ("3".equals(wp)) {
            // 只剩下3个以内的武器，伤害+50%，最后一个武器，伤害+200%
            WpInfo wpInfo = wpInfoMapper.selectById(offensiveInfo.getName());
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
}
