package com.im.imparty.websocket.controller;

import cn.hutool.cache.CacheUtil;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.spring.util.SpringFactoryUtils;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.user.service.UserService;
import com.im.imparty.websocket.WebsocketSessionImpl;
import com.im.imparty.websocket.WebsocketSessionManager;
import com.im.imparty.websocket.annotation.ActionController;
import com.im.imparty.websocket.annotation.ActionMethod;
import com.im.imparty.websocket.util.MsgData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@ActionController("/music")
public class MusicPlayController {

    @Resource
    private UserService userService;

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
    public void receiveChatMsg(WebsocketSessionImpl session, String msg) throws IOException {
        MsgData res = new MsgData("/music/chat");
        res.putData("from", session.getUserName());
        try{
            UserInfo userInfo = userService.getUserInfo(session.getUserName());
            res.putData("userInfo", userInfo);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        res.putData("msg", msg);
        res.putData("timestamp", System.currentTimeMillis());
        session.getSessionManager().broadcastMsg(res.toJSONString());
    }

}
