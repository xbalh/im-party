package com.im.imparty.server;

import com.im.imparty.websocket.*;
import com.im.imparty.websocket.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
@Slf4j
@ServerEndpoint("/musicParty/ws/{roomId}")
public class WebSocketServer implements ApplicationContextAware {

    // private static WebsocketSessionManager sessionManager = new WebsocketSessionManager();

    public static Map<Integer, WebsocketSessionManager> roomMap = new ConcurrentHashMap<>();

    private static List<ActionFilter> filterList;

    @Autowired
    public static void setFilterList(List<ActionFilter> filterList) {
        AnnotationAwareOrderComparator.sort(filterList);
        WebSocketServer.filterList = filterList;
    }

    private Integer roomId;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Integer roomId) {
        this.roomId = roomId;
        if (roomMap.containsKey(roomId)) {
            roomMap.get(roomId).addUser(session, roomId);
        } else {
            // TODO 判断房间是否存在
            WebsocketSessionManager websocketSessionManager = new WebsocketSessionManager();
            websocketSessionManager.addUser(session, roomId);
            roomMap.put(roomId, websocketSessionManager);
        }//加入set中
        try {
            log.info("房间号：{}, 人数{}", roomId, roomMap.get(roomId).count());
        } catch (Exception e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        String userName = SessionUtils.getUserName(session);
        WebsocketSessionManager sessionManager = roomMap.get(roomId);
        sessionManager.close(session);
        //断开连接情况下，更新主板占用情况为释放
        log.info("房间号：{}, 释放的userName为：{}, 人数{}", roomId, userName, roomMap.get(roomId).count());

    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        String userName = SessionUtils.getUserName(session);
        log.info("房间号：{}, 收到来自:{} 的信息:{}", roomId, userName, message);
        //群发消息
        try {
            WebsocketSessionManager sessionManager = roomMap.get(roomId);
            WebsocketSessionImpl session1 = sessionManager.getSessionByUserName(userName);
            new ActionFilterChain(filterList).doFilter(message, session1);
        } catch (Exception e) {
            log.error("发送消息失败！", e);
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(WebsocketSession session, String message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<ActionFilter> resList = new ArrayList<>();
        String[] beanNamesForType = applicationContext.getBeanNamesForType(ActionFilter.class);
        for (String name : beanNamesForType) {
            resList.add(applicationContext.getBean(name, ActionFilter.class));
        }
        AnnotationAwareOrderComparator.sort(resList);
        WebSocketServer.filterList = resList;
    }
}
