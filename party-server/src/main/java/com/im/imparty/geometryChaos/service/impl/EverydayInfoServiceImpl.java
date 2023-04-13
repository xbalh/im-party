package com.im.imparty.geometryChaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.geometryChaos.entity.UserStaticInfo;
import com.im.imparty.geometryChaos.mapper.EverydayInfoMapper;
import com.im.imparty.geometryChaos.entity.EverydayInfo;
import com.im.imparty.geometryChaos.mapper.UserStaticInfoMapper;
import com.im.imparty.geometryChaos.service.EverydayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liang yanbo
 * @since 2022-08-18
 */
@Service
public class EverydayInfoServiceImpl extends ServiceImpl<EverydayInfoMapper, EverydayInfo> implements EverydayInfoService {

    @Autowired
    UserStaticInfoMapper userStaticInfoMapper;

    @Autowired
    EverydayInfoMapper everydayInfoMapper;

    @Override
    public String getFirstEveryday(String userName) {
        // 计算天数，记录历史数据
        QueryWrapper<EverydayInfo> everydayInfoQueryWrapper = new QueryWrapper<>();
        everydayInfoQueryWrapper.eq("user_name", userName);
        everydayInfoQueryWrapper.orderByDesc("create_time");
        everydayInfoQueryWrapper.last(" limit 1");
        Integer nowDays;
        EverydayInfo everydayInfo = everydayInfoMapper.selectOne(everydayInfoQueryWrapper);
        // 如果没有就新建
        if (everydayInfo == null) {
            everydayInfo = new EverydayInfo();
            everydayInfo.setUserName(userName)
                    .setDaysCount("100")
                    .setEverydayNum("0")
                    .setCreateTime(new Date())
                    .setId(UUID.randomUUID().toString());
            nowDays = 0;
        } else {
            nowDays = Integer.parseInt(everydayInfo.getEverydayNum());
        }
        everydayInfo.setEverydayNum(String.valueOf(Integer.parseInt(everydayInfo.getEverydayNum()) + 1))
                .setCreateTime(new Date())
                .setId(UUID.randomUUID().toString());

        QueryWrapper<UserStaticInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        UserStaticInfo userImportantInfo = userStaticInfoMapper.selectOne(wrapper);

        Integer finalCoin = 20;
        if (nowDays > 500) {
            nowDays = 500;
        }
        finalCoin += nowDays;
//        Integer uLevel = userImportantInfo.getULevel();
//        finalCoin = finalCoin + (uLevel * 3);

        // 添加实际数值
//        Integer newVLv = VUtils.ifVUp(userImportantInfo.getVLevel(), userImportantInfo.getVValue(), 10);
//        if (!newVLv.equals(userImportantInfo.getVLevel())) {
//            userImportantInfo.setVLevel(newVLv);
//        }

//        Double finalDouble = finalCoin * (1 + newVLv * 0.1);
//        int finalGetCoin = finalDouble.intValue();
        boolean weekend = this.isWeekend();
        if (weekend) {
            finalCoin = finalCoin * 3;
        }

        userImportantInfo.setYb(userImportantInfo.getYb() + finalCoin);
        userStaticInfoMapper.updateById(userImportantInfo);

        everydayInfo.setDaysCount(String.valueOf(finalCoin));
        everydayInfoMapper.insert(everydayInfo);

        return "签到第 " + (Integer.parseInt(everydayInfo.getEverydayNum())) + " 天，获得 " + finalCoin + " 硬币，共 " + userImportantInfo.getYb() + " 硬币。";
    }

    private boolean isWeekend() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
}
