package com.im.imparty.websocket.conts;

import com.alibaba.fastjson.JSONObject;

public class MsgJSON {

    public static JSONObject nextPlay(String songId, String downUrl) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/playControl/nextPlay");
        JSONObject resData = new JSONObject();
        resData.put("songId", songId);
        resData.put("downUrl", downUrl);
        res.put("data", resData);
        return res;
    }

    public static JSONObject currentTime(long currentTime) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/playControl/currentTime");
        res.put("data", currentTime);
        return res;
    }

}
