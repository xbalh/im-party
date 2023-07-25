package com.im.imparty.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public UserInfo getUserInfo(String userName) {
        UserInfoDetail detail = getBaseMapper().getDetail(userName);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(detail.getUsername());
        userInfo.setNickName(detail.getNickName());
        userInfo.setWyyUserId(detail.getWyyUserId());
        return userInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setMusicCookie(String userName, String cookie) {
        lambdaUpdate().eq(UserDomain::getUserName, userName)
                .set(UserDomain::getWyyCookie, cookie).update();
    }

    @Override
    public String getMusicCookie(String userName) {
        List<UserDomain> entity = lambdaQuery().eq(UserDomain::getUserName, userName)
                .select(UserDomain::getWyyCookie).list();
        return entity == null || entity.isEmpty() ? "" : entity.get(0).getWyyCookie();
    }

    @Override
    public void updateWyyBind(String userName, String wyyUserId) {
        lambdaUpdate().eq(UserDomain::getUserName, userName).eq(UserDomain::getValidSts, "A")
                .set(UserDomain::getWyyUserId, wyyUserId).update();
    }

}
