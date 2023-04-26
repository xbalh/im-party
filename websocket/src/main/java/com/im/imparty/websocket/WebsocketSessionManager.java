package com.im.imparty.websocket;

import com.im.imparty.spring.authentication.LoginJwtToken;
import com.im.imparty.websocket.conts.MsgJSON;
import com.im.imparty.websocket.timer.PlayTimer;
import com.im.imparty.websocket.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import javax.websocket.Session;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WebsocketSessionManager {

    private static ConcurrentHashMap<String, WebsocketSessionImpl> socketStore = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, WebsocketSessionImpl>> socketStoreByRole = new ConcurrentHashMap<>();

    private static AtomicInteger count = new AtomicInteger();

    // 播放计时器
    private PlayTimer playTimer;

    public boolean addUser(Session session) {
        Principal userPrincipal = session.getUserPrincipal();
        String name = userPrincipal.getName();
        if (name == null || name == "") {
            return false;
        }
        WebsocketSessionImpl websocketSession = new WebsocketSessionImpl(session, name, this);
        websocketSession.setValidFlag(true);
        if (userPrincipal instanceof LoginJwtToken) {
            Collection<GrantedAuthority> authorities = ((LoginJwtToken) userPrincipal).getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : Optional.ofNullable(authorities).orElse(Collections.emptyList())) {
                roles.add(authority.getAuthority());
                if (socketStoreByRole.containsKey(authority.getAuthority())) {
                    socketStoreByRole.get(authority.getAuthority()).put(name, websocketSession);
                } else {
                    ConcurrentHashMap<String, WebsocketSessionImpl> tmpMap = new ConcurrentHashMap<>();
                    tmpMap.put(name, websocketSession);
                    socketStoreByRole.put(authority.getAuthority(), tmpMap);
                }
            }
            websocketSession.setRoleList(roles);
        }

        socketStore.put(name, websocketSession);
        count.incrementAndGet();
        return true;
    }

    public boolean close(Session session) {
        WebsocketSessionImpl sessionImpl = getSessionBySession(session);
        return close(sessionImpl);
    }

    public boolean close(WebsocketSessionImpl sessionImpl){
        Session session = sessionImpl.getSession();
        String userName = SessionUtils.getUserName(session);
        if (socketStore.remove(userName) != null) {
            for (GrantedAuthority userRole : getUserRoles(session)) {
                if (socketStoreByRole.containsKey(userRole.getAuthority())) {
                    socketStoreByRole.get(userRole.getAuthority()).remove(userName);
                }
            }
            sessionImpl.close();
            count.decrementAndGet();
            return true;
        }
        return false;
    }

    public int count() {
        return count.get();
    }

    public void broadcastMsg(String msg) {
        for (WebsocketSessionImpl value : socketStore.values()) {
            value.sendMessage(msg);
        }
    }

    public WebsocketSessionImpl getSessionByUserName(String userName) {
        return socketStore.get(userName);
    }

    public WebsocketSessionImpl getSessionBySession(Session session) {
        String userName = SessionUtils.getUserName(session);
        return socketStore.get(userName);
    }

    private Collection<GrantedAuthority> getUserRoles(Session session) {
        if (session.getUserPrincipal() instanceof LoginJwtToken) {
            Collection<GrantedAuthority> authorities = ((LoginJwtToken) session.getUserPrincipal()).getAuthorities();
            return authorities;
        }
        return Collections.emptyList();
    }

    public PlayTimer init() {
        this.playTimer = new PlayTimer(10, (o) -> {
            if (o) {
                broadcastMsg(MsgJSON.nextPlay("1", "http://baidu.com").toJSONString());
                playTimer.play(0);
            } else {
                broadcastMsg(MsgJSON.currentTime(getCurrentTime()).toJSONString());
            }
            return null;
        });
        return playTimer;
    }

    public void play(long startTime) {
        if (this.playTimer == null) {
            init();
        }
        playTimer.play(startTime);
    }

    public long getCurrentTime() {
        return playTimer.getCurrentTime();
    }
}
