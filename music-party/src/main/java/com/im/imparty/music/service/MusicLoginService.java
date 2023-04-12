package com.im.imparty.music.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface MusicLoginService {
    String getLoginQr();

    @Cacheable(cacheNames = "MusicLogin", key = "key")
    String getQrKey();

    @Cacheable(cacheNames = "MusicLogin", key = "key", unless = "#result != null")
    String getQrKeyInCache();

    String getCookie();

    @CacheEvict(cacheNames = "MusicLogin", key = "key")
    void clearQrKey();
}
