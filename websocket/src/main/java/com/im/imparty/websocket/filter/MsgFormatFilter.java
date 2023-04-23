package com.im.imparty.websocket.filter;

import com.im.imparty.websocket.ActionFilter;
import com.im.imparty.websocket.ActionFilterChain;
import com.im.imparty.websocket.WebsocketSessionImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(3)
public class MsgFormatFilter implements ActionFilter {
    @Override
    public void doFilter(Object msgData, WebsocketSessionImpl session, ActionFilterChain filterChain) {
        log.info("MsgFormatFilter msg:{}", msgData);
        filterChain.doFilter(msgData, session);
    }
}
