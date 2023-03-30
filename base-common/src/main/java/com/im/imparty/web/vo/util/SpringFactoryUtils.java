package com.im.imparty.web.vo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringFactoryUtils implements ApplicationContextAware {

    private static volatile ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext value) throws BeansException {
        if (applicationContext == null) {
            synchronized (this) {
                if (applicationContext == null) {
                    initApplicationContext(value);
                }
            }
        }
    }

    private static void initApplicationContext(ApplicationContext value) {
        applicationContext = value;
    }
}
