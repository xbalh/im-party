package com.im.imparty.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;

import java.util.List;

public interface UserService extends IService<UserDomain> {
    UserInfoDetail getUserDetail(String userName);

    UserInfo getUserInfo(String userName);

    /**
     * 设置网易云cookie
     * @param userName
     * @param cookie
     */
    void setMusicCookie(String userName, String cookie);

    String getMusicCookie(String userName);

    void updateWyyBind(String userName, String wyyUserId);

    List<UserInfo> getUserInfoBatchByUserNames(List<String> userNames);
}
