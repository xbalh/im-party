package com.im.imparty.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDomain> implements UserService {


    @Cacheable(cacheNames = "userInfo", key = "#userName")
    @Override
    public UserInfoDetail getUserDetail(String userName) {
        log.info("获取用户:{}的缓存", userName);
        UserInfoDetail detail = getBaseMapper().getDetail(userName);
        return detail;
    }

}
