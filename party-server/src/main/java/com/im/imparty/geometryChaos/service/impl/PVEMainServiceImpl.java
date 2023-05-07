package com.im.imparty.geometryChaos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.common.dic.LevelDic;
import com.im.imparty.common.exception.CustomException;
import com.im.imparty.geometryChaos.constant.DataConstant;
import com.im.imparty.geometryChaos.entity.*;
import com.im.imparty.geometryChaos.mapper.*;
import com.im.imparty.geometryChaos.service.GeometryChaosMainService;
import com.im.imparty.geometryChaos.service.PVEMainService;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class PVEMainServiceImpl implements PVEMainService {

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

    @Autowired
    private SkDicMapper skDicMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserStaticInfo createCharacter(Integer level) {
        // 基础
        // 根据前端选择级别决定最后要添加的总数
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 5, 5, 5, 8)); // 初始列表
        int sum = this.getRangeNum(level); // 需要添加的总数
        Random random = new Random();
        int maxIndex = sum / 3;
        while (sum > 0) {
            // 随机选择一个数字
            int index = random.nextInt(numbers.size());
            // 单一数值不能超过总数的三分之一
            if (numbers.get(index) >= 8 + maxIndex) {
                continue;
            }
            numbers.set(index, numbers.get(index) + 1);
            sum -= 1;
        }

        // 特殊添加，每level5添加一个
        int specialSkNum = level / 5;
        QueryWrapper<SkDic> skDicQueryWrapper = new QueryWrapper<>();
        skDicQueryWrapper.eq("sk_type", 5);
        List<SkDic> skDics = skDicMapper.selectList(skDicQueryWrapper);
        List<SkDic> chooseSk = new ArrayList<>();
        if (specialSkNum >= skDics.size()) {
            chooseSk = skDics;
        }
        Random randomSk = new Random(System.currentTimeMillis());
        while (chooseSk.size() < specialSkNum) {
            int index = randomSk.nextInt(skDics.size());
            SkDic skDic = skDics.get(index);
            // 判断选取的是否和已选取的SkDic相同，如果不相同则添加到选取列表中
            if (!chooseSk.contains(skDic)) {
                chooseSk.add(skDic);
            }
        }

        // WP添加（PVE 对方的WP不会消耗掉），由传输值决定，区分为三种模式
        // 从已有的里面随机选取一个使用
        UserStaticInfo userStaticInfo = new UserStaticInfo();
        userStaticInfo.setStr(numbers.get(0))
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
        WpInfoResp wpInfoResp = new WpInfoResp();
        BeanUtils.copyProperties(wpInfo, wpInfoResp);
        PersonFightInfo personFightInfo = new PersonFightInfo();
        Integer str = userStaticInfo.getStr();
        Integer agi = userStaticInfo.getAgi();
        Integer spd = userStaticInfo.getSpd();
        Integer wit = userStaticInfo.getWit();
        Integer htl = userStaticInfo.getHtl();
        BigDecimal finalAtk = new BigDecimal("1");
        BigDecimal finalDef = new BigDecimal("1");

        // 读取skInfo
        String skHolding = wpInfo.getSkHolding();
        if (StrUtil.isNotBlank(skHolding)) {
            String[] skSpiltList = skHolding.split(",");
            for (String sk : skSpiltList) {

            }
        }

        personFightInfo.setName(userStaticInfo.getUserName())
                .setWpInfo(wpInfoResp)
                .setHp((int) (DataConstant.STATIC_HEALTH + htl * DataConstant.HTL_TO_HEALTH))
                .setStr(str)
                .setWit(wit)
                .setSpd(spd)
                .setAgi(agi)
                .setHtl(htl)
                .setBlk(DataConstant.STATIC_BLK + agi * DataConstant.AGI_TO_BLK)
                .setBlkPer(DataConstant.STATIC_BLK_PER + agi * DataConstant.AGI_TO_BLK_PER)
                .setCri(DataConstant.STATIC_CRI + agi * DataConstant.AGI_TO_CRI)
                .setCriPer(DataConstant.STATIC_CRI_PER + agi * DataConstant.AGI_TO_CRI_PER)
                .setMiss(DataConstant.STATIC_MISS + agi * DataConstant.AGI_TO_MISS)
                .setCa(DataConstant.STATIC_CA + agi * DataConstant.AGI_TO_CA)
                .setFinalAtk(finalAtk)
                .setFinalDef(finalDef)
                .setUsedWeapon(new ArrayList<>())
                .setBuff(new ArrayList<>());
        return personFightInfo;
    }

    private Integer getRangeNum(int level) {
        BigDecimal sum = new BigDecimal(10);
        // 每个级别都是上个级别的1.1倍+2
        sum = IntStream.rangeClosed(2, level)
                .mapToObj(BigDecimal::valueOf)
                .reduce(sum, (s, i) -> s.multiply(new BigDecimal("1.1")).add(new BigDecimal("2")))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        return sum.intValue();
    }

    public static void main(String[] args) {

    }
}