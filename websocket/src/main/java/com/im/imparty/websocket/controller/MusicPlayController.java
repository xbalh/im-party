package com.im.imparty.websocket.controller;

import com.im.imparty.websocket.WebsocketSessionImpl;
import com.im.imparty.websocket.annotation.ActionController;
import com.im.imparty.websocket.annotation.ActionMethod;

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
    @ActionMethod("/chat")
    public void receiveChatMsg(WebsocketSessionImpl session, String msg) {
        session.getSessionManager().broadcastMsg(msg);
    }

}
