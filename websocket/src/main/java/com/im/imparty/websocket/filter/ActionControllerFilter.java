package com.im.imparty.websocket.filter;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.spring.util.SpringFactoryUtils;
import com.im.imparty.websocket.ActionFilter;
import com.im.imparty.websocket.ActionFilterChain;
import com.im.imparty.websocket.WebsocketSessionImpl;
import com.im.imparty.websocket.annotation.ActionController;
import com.im.imparty.websocket.annotation.ActionMethod;
import com.im.imparty.websocket.exception.WebsocketMsgException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Order(10)
public class ActionControllerFilter implements ActionFilter, ApplicationContextAware {

    private static final Map<String, MethodWrapper> pathMap = new HashMap<>();

    @Override
    public void doFilter(Object msgData, WebsocketSessionImpl session, ActionFilterChain filterChain) {
        log.info("ActionControllerFilter msg:{}", msgData);
        JSONObject msgJson = null;
        if (msgData instanceof String) {
            msgJson = JSONObject.parseObject(msgData.toString());
        } else if (msgData instanceof JSONObject) {
            msgJson = (JSONObject) msgData;
        } else {
            throw new WebsocketMsgException("消息格式错误");
        }
        String path = null;
        if (msgJson.containsKey("method")) {
            path = Arrays.stream(msgJson.getString("method").split("/")).filter(StringUtils::isNotBlank).collect(Collectors.joining("/"));
        }
        if (path != null && pathMap.containsKey(path)) {
            MethodWrapper methodWrapper = pathMap.get(path);
            JSONObject data = msgJson.getJSONObject("data");
            Parameter[] parameters = methodWrapper.getMethod().getParameters();
            List<Object> params = new ArrayList<>();
            for (Parameter param : parameters) {
                if (param.getType().equals(WebsocketSessionImpl.class)) {
                    params.add(session);
                } else {
                    params.add(data.getObject(param.getName(), param.getType()));
                }
            }
            try {
                Object invoke = methodWrapper.getMethod().invoke(methodWrapper.object, params.toArray());
                if (invoke != null) {
                    JSONObject res = new JSONObject();
                    res.put("method", msgJson.getString("method"));
                    if (invoke instanceof String) {
                        res.put("data", invoke);
                    } else {
                        res.put("data", JSONObject.toJSONString(invoke));
                    }
                    session.sendMessage(res.toJSONString());
                }
            } catch (WebsocketMsgException e) {
                session.sendMessage("消息格式错误");
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(msgData, session);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init(applicationContext);
    }

    private void init(ApplicationContext applicationContext) {
        List<Object> controller = SpringFactoryUtils.getBeansByAnnotation(applicationContext, ActionController.class);
        for (Object item : controller) {
            log.info("初始化， {}", item);
            Class<?> aClass = item.getClass();
            ActionController annotation = aClass.getAnnotation(ActionController.class);
            List<String> path = new ArrayList<>();
            Arrays.stream(annotation.value().split("/")).filter(StringUtils::isNotBlank).forEach(i -> path.add(i));
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                ActionMethod annotation1 = method.getAnnotation(ActionMethod.class);
                if (annotation1 == null) {
                    continue;
                }
                List<String> fullPath = new ArrayList<>(path);
                Arrays.stream(annotation1.value().split("/")).filter(StringUtils::isNotBlank).forEach(i -> fullPath.add(i));
                String fullPathStr = String.join("/", fullPath);
                if (pathMap.containsKey(fullPathStr)) {
                    throw new RuntimeException(fullPathStr + "路径重复--" + aClass.getName() + "-" + method.getName());
                }
                pathMap.put(fullPathStr, new MethodWrapper(item, method));
            }
        }
    }
    @Getter
    @AllArgsConstructor
    private class MethodWrapper{
        private Object object;
        private Method method;
    }
}
