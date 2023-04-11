package com.im.imparty.websocket;

import java.io.IOException;

public interface WebsocketSession {


    void sendMessage(String msg) throws IOException;

    void close();
}
