package com.im.imparty.music.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface MusicLoginService {

    String getLoginQr(String userName);


    String getQrKey(String userName);

    int getCookie(String userName);


    void clearQrKey(String userName);

    JSONObject getUserInfo();
}
