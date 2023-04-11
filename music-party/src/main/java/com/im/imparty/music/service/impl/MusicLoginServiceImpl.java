package com.im.imparty.music.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.music.api.LoginApi;
import com.im.imparty.music.service.MusicLoginService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MusicLoginServiceImpl implements MusicLoginService {

    @Resource
    private MusicLoginService self;

    @Resource
    private LoginApi loginApi;

    @Cacheable(cacheNames = "MusicLogin", key = "'qrimg'")
    @Override
    public String getLoginQr() {
        String qrKey = self.getQrKey();
        JSONObject jsonObject = loginApi.qrCreate(qrKey, true);
        JSONObject data = jsonObject.getJSONObject("data");
        String qrimg = data.getString("qrimg");
        return qrimg;
    }

    @Override
    @Cacheable(cacheNames = "MusicLogin", key = "'key'")
    public String getQrKey() {
        JSONObject jsonObject = loginApi.qrKey();
        JSONObject data = jsonObject.getJSONObject("data");
        return data.getString("unikey");
    }

    @Override
    @Cacheable(cacheNames = "MusicLogin", key = "'key'", unless = "#result != null")
    public String getQrKeyInCache() {
        return null;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "MusicLogin", key = "'key'"),
                    @CacheEvict(cacheNames = "MusicLogin", key = "'qrimg'")
            }
    )
    public void clearQrKey() {

    }
}
