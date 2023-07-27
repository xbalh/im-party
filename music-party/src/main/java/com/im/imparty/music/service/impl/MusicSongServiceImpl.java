package com.im.imparty.music.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.api.music.MusicApi;
import com.im.imparty.music.service.MusicSongService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MusicSongServiceImpl implements MusicSongService {

    @Resource
    private MusicApi musicApi;

    @Override
    public JSONObject searchSongList(String keywords, Integer type,
                                     Integer offset, Integer limit) {
        JSONObject search = musicApi.search(keywords, type, offset, limit);
        JSONObject result = search.getJSONObject("result");
        return result;
    }

    @Override
    public JSONArray getSong(List<String> songIds, String level) {
        JSONObject search = musicApi.getSong(songIds, level);
        JSONArray result = search.getJSONArray("data");
        return result;
    }

    @Override
    public JSONArray getPlayList(String musicUserId) {
        JSONObject search = musicApi.getPlayList(musicUserId, "999");
        JSONArray result = search.getJSONArray("playlist");
        return result;
    }

    @Cacheable(cacheNames = "trackMusicList", key = "T(String).valueOf(#id).concat('-').concat(#limit).concat('-').concat(#offset)", unless = "#result=null")
    @Override
    public JSONArray getPlayListAllMusic(String id, Integer limit, Integer offset) {
        JSONObject search = musicApi.getPlayListAllMusic(id, limit, offset);
        JSONArray result = search.getJSONArray("songs");
        return result;
    }

    @Override
    public JSONObject getUserDetailInfo(String userId) {
        JSONObject search = musicApi.getUserDetailInfo(userId);
        JSONArray result = search.getJSONArray("songs");

        return null;
    }

}
