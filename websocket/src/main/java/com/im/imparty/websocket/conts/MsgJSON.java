package com.im.imparty.websocket.conts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.common.vo.PlaySongInfo;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.websocket.WebsocketSessionImpl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MsgJSON {

    public static JSONObject nextPlay(PlaySongInfo playSongInfo) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/playControl/nextPlay");
        res.put("data", playSongInfo);
        return res;
    }

    public static JSONObject play(PlaySongInfo playSongInfo, Long nowTime) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/playControl/play");
        playSongInfo.setNowTime(nowTime);
        res.put("data", playSongInfo);
        return res;
    }

    public static JSONObject userJoin(String userName, Integer roomId) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/room/user-join/" + roomId);
        res.put("data", userName);
        return res;
    }

    public static JSONObject userLeave(String userName, Integer roomId) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/room/user-leave/" + roomId);
        res.put("data", userName);
        return res;
    }

    public static JSONObject currentTime(long currentTime) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/playControl/currentTime");
        res.put("data", currentTime);
        return res;
    }

    public static JSONObject songListChange(Collection<PlaySongInfo> songList, Integer roomId) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/room/playlist-change/" + roomId);
        res.put("data", songList);
        return res;
    }

    public static JSONObject currentUserList(List<String> currentUserList, Integer roomId) {
        JSONObject res = new JSONObject();
        res.put("method", "/music/room/current-users/" + roomId);
        res.put("data", currentUserList);
        return res;
    }
}
