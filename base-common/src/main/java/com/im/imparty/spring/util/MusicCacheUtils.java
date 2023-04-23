package com.im.imparty.spring.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class MusicCacheUtils {

    private static volatile Cache cache;

    public static void put(String key, String value) {
        getCache().put(key, value);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return getCache().get(key, clazz);
    }

    public static void remove(String key) {
        getCache().evict(key);
    }

    private static Cache getCache() {
        if (cache == null) {
            synchronized (MusicCacheUtils.class) {
                if (cache == null) {
                    cache = SpringFactoryUtils.getBean(CacheManager.class).getCache("MusicLogin");
                }
            }
        }
        return cache;
    }

}
