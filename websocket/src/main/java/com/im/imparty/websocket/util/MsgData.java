package com.im.imparty.websocket.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Optional;

public class MsgData extends JSONObject {

    public MsgData(String method) {
        super();
        put("method", method);
    }

    public MsgData setData(JSONObject data) {
        put("data", data);
        return this;
    }

    public MsgData putData(String key, Object data) {
        JSONObject data1 = Optional.ofNullable(getJSONObject("data")).orElse(new JSONObject());
        data1.put(key, data);
        setData(data1);
        return this;
    }

}
