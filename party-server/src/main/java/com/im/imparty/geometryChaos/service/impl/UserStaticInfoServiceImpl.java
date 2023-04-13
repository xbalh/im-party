package com.im.imparty.geometryChaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.geometryChaos.entity.UserStaticInfo;
import com.im.imparty.geometryChaos.mapper.UserStaticInfoMapper;
import com.im.imparty.geometryChaos.service.UserStaticInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liang yanbo
 * @since 2022-08-18
 */
@Service
public class UserStaticInfoServiceImpl extends ServiceImpl<UserStaticInfoMapper, UserStaticInfo> implements UserStaticInfoService {

    @Autowired
    UserStaticInfoMapper userStaticInfoMapper;

}
