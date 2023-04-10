package com.im.imparty.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface ActionController {

    String value();

}
