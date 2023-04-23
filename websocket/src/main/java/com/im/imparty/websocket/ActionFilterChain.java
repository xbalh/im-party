package com.im.imparty.websocket;

import java.util.List;

public class ActionFilterChain {

    public ActionFilterChain(List<ActionFilter> filterList) {
        this.filterList = filterList;
        this.pos = 0;
    }

    private List<ActionFilter> filterList;

    private int pos;

    public void doFilter(Object msgData, WebsocketSessionImpl session) {
        if (filterList.size() <= pos) {
            return;
        }
        filterList.get(pos++).doFilter(msgData, session, this);
    }

}
