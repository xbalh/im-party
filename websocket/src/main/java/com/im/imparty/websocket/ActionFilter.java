package com.im.imparty.websocket;

public interface ActionFilter {

    void doFilter(Object msgData, WebsocketSessionImpl session, ActionFilterChain filterChain);

}
