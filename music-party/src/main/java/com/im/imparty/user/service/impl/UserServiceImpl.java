package com.im.imparty.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.im.imparty.music.service.MusicSongService;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDomain> implements UserService {

    @Resource
    private MusicSongService musicSongService;

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
        userInfo.setUserAvatarUrl(detail.getUserAvatarUrl());
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
        LambdaUpdateChainWrapper<UserDomain> chainWrapper = lambdaUpdate().eq(UserDomain::getUserName, userName).eq(UserDomain::getValidSts, "A")
                .set(UserDomain::getWyyUserId, wyyUserId);
        JSONObject userDetailInfo = musicSongService.getUserDetailInfo(wyyUserId);
        if (userDetailInfo.containsKey("profile")) {
            Map profile = (Map) userDetailInfo.get("profile");
            chainWrapper.set(UserDomain::getUserAvatarUrl, (String) profile.get("avatarUrl"));
        }
        chainWrapper.update();
    }

    @Override
    public List<UserInfo> getUserInfoBatchByUserNames(List<String> userNames) {
        if (CollectionUtil.isEmpty(userNames)) {
            return new ArrayList<>();
        }
        List<UserDomain> userDomains = lambdaQuery()
                .eq(UserDomain::getValidSts, "A")
                .in(UserDomain::getUserName, userNames)
                .list();

        return BeanUtil.copyToList(userDomains, UserInfo.class);
    }

    @Override
    public void updateUserInfo(UserDomain userDomain, String userName) {
        UserDomain currentInfo = lambdaQuery()
                .eq(UserDomain::getValidSts, "A")
                .eq(UserDomain::getUserName, userName)
                .one();
        if (StringUtils.isNotBlank(userDomain.getNickName())) {
            currentInfo.setNickName(userDomain.getNickName());
        }
        baseMapper.updateById(currentInfo);
    }

}
