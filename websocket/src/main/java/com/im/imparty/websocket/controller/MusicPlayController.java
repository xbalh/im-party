package com.im.imparty.websocket.controller;

import com.im.imparty.websocket.WebsocketSessionImpl;
import com.im.imparty.websocket.annotation.ActionController;
import com.im.imparty.websocket.annotation.ActionMethod;

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
    @ActionMethod("/chat")
    public String receiveChatMsg(WebsocketSessionImpl session, String msg) throws IOException {
        return "收到了";
    }

}
