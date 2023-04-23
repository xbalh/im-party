package com.im.imparty.websocket.exception;

public class WebsocketMsgException extends RuntimeException{
    public WebsocketMsgException(String message) {
        super(message);
    }

    public WebsocketMsgException(String message, Throwable cause) {
        super(message, cause);
    }
}
