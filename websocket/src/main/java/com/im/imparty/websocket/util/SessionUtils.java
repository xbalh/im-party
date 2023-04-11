package com.im.imparty.websocket.util;

import javax.websocket.Session;

public class SessionUtils {

    public static String getUserName(Session session) {
        return session.getUserPrincipal().getName();
    }

}
