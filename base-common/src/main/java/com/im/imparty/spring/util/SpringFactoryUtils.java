package com.im.imparty.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Order(1)
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

    public static <T> List<T> getBeans(Class<T> clazz) {
        return getBeans(applicationContext, clazz);
    }

    public static <T extends Annotation> List<Object> getBeansByAnnotation(Class<T> clazz) {
        return getBeansByAnnotation(applicationContext, clazz);
    }

    public static <T> List<T> getBeans(ApplicationContext value, Class<T> clazz) {
        List<T> resList = new ArrayList<>();
        String[] beanNamesForType = value.getBeanNamesForType(clazz);
        for (String name : beanNamesForType) {
            resList.add(value.getBean(name, clazz));
        }
        return resList;
    }

    public static <T extends Annotation> List<Object> getBeansByAnnotation(ApplicationContext value, Class<T> clazz) {
        List<Object> resList = new ArrayList<>();
        String[] beanNamesForType = value.getBeanNamesForAnnotation(clazz);
        for (String name : beanNamesForType) {
            resList.add(value.getBean(name));
        }
        return resList;
    }
}
