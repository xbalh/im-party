package com.im.imparty.websocket.filter;

import com.im.imparty.websocket.ActionFilter;
import com.im.imparty.websocket.ActionFilterChain;
import com.im.imparty.websocket.WebsocketSessionImpl;
import org.springframework.core.annotation.Order;

@Order(10)
public class ActionControllerFilter implements ActionFilter {
    @Override
    public void doFilter(Object msgData, WebsocketSessionImpl session, ActionFilterChain filterChain) {

    }
}
