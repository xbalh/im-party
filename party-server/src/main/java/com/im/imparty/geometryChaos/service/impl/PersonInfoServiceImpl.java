package com.im.imparty.geometryChaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.geometryChaos.entity.EverydayInfo;
import com.im.imparty.geometryChaos.entity.PersonInfo;
import com.im.imparty.geometryChaos.mapper.EverydayInfoMapper;
import com.im.imparty.geometryChaos.mapper.PersonInfoMapper;
import com.im.imparty.geometryChaos.service.EverydayInfoService;
import com.im.imparty.geometryChaos.service.PersonInfoService;
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
public class PersonInfoServiceImpl extends ServiceImpl<PersonInfoMapper, PersonInfo> implements PersonInfoService {

    @Autowired
    PersonInfoMapper personInfoMapper;

}
