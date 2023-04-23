package com.im.imparty.music.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface MusicSongService {
    JSONObject searchSongList(String keywords, Integer type,
                              Integer offset, Integer limit);

    JSONArray getSong(List<String> songIds, String level);
}
