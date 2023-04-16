package com.im.imparty.geometryChaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.geometryChaos.entity.EverydayInfo;
import com.im.imparty.geometryChaos.entity.GachaInfo;
import com.im.imparty.geometryChaos.entity.UserStaticInfo;
import com.im.imparty.geometryChaos.mapper.EverydayInfoMapper;
import com.im.imparty.geometryChaos.mapper.GachaMapper;
import com.im.imparty.geometryChaos.mapper.UserStaticInfoMapper;
import com.im.imparty.geometryChaos.service.EverydayInfoService;
import com.im.imparty.geometryChaos.service.GachaService;
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
public class GachaServiceImpl extends ServiceImpl<GachaMapper, GachaInfo> implements GachaService {

    @Autowired
    UserStaticInfoMapper userStaticInfoMapper;

    @Autowired
    GachaMapper gachaMapper;

    @Override
    public String getGacha(GachaInfo gachaInfo) {

        return null;
    }
}
