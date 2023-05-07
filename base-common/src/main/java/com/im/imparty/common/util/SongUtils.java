package com.im.imparty.common.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.im.imparty.api.music.MusicApi;
import com.im.imparty.common.enums.SongQualityEnum;
import com.im.imparty.spring.util.SpringFactoryUtils;

import java.util.LinkedHashMap;

public class SongUtils {

    public static String getUrlBySongId(String songId, String songQuality) {
        MusicApi bean = SpringFactoryUtils.getBean(MusicApi.class);

        JSONObject song = bean.getSong(ImmutableList.of(songId), SongQualityEnum.check(songQuality).getCode());
        if (song != null && song.getJSONArray("data") != null && song.getJSONArray("data") .size() > 0) {
            LinkedHashMap data = song.getJSONArray("data").getObject(0, LinkedHashMap.class);
            return (String) data.get("url");
        }
        return null;
    }

}
