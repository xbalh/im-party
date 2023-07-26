package com.im.imparty.websocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.spring.util.SpringFactoryUtils;
import com.im.imparty.websocket.WebsocketSessionImpl;
import com.im.imparty.websocket.WebsocketSessionManager;
import com.im.imparty.websocket.annotation.ActionController;
import com.im.imparty.websocket.annotation.ActionMethod;
import com.im.imparty.websocket.util.MsgData;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@ActionController("/music")
public class MusicPlayController {

    /**
     * 前端传过来的消息结构为
     * {
     *     "method": "/music/chat",
     *     "data": {
     *         msg: "xxxxx"
     *     }
     * }
     * @param session
     * @param msg
     */
    @ActionMethod("/chat/{roomId}")
    public void receiveChatMsg(WebsocketSessionImpl session, String msg, @PathVariable String roomId) throws IOException {
        MsgData res = new MsgData("/music/chat/"+ roomId);
        res.putData("from", session.getUserName());
        res.putData("msg", msg);
        res.putData("timestamp", System.currentTimeMillis());
        session.getSessionManager().broadcastMsg(res.toJSONString());
    }

}
