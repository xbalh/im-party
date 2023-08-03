package com.im.imparty.music.service.impl;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.api.music.LoginApi;
import com.im.imparty.music.service.MusicLoginService;
import com.im.imparty.spring.util.MusicCacheUtils;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Order(Integer.MAX_VALUE)
@Service
public class MusicLoginServiceImpl implements MusicLoginService, ApplicationContextAware {

    private static final Set<String> qrKeySet = new ConcurrentHashSet<>();

    @Resource
    private MusicLoginService self;

    @Resource
    private LoginApi loginApi;

    @Resource
    private UserService userService;

    @Override
    public String getLoginQr(String userName) {
        String qrimg = MusicCacheUtils.get(userName + "qrimg", String.class);
        if (StringUtils.isNotEmpty(qrimg)) {
            return qrimg;
        }
        String qrKey = self.getQrKey(userName);
        JSONObject jsonObject = loginApi.qrCreate(qrKey, true);
        JSONObject data = jsonObject.getJSONObject("data");
        qrimg = data.getString("qrimg");
        MusicCacheUtils.put(userName + "qrimg", qrimg);



        return qrimg;
    }

    @Override
    public String getQrKey(String userName) {
        String key = MusicCacheUtils.get(userName + "key", String.class);
        if (StringUtils.isNotEmpty(key)) {
            return key;
        }
        JSONObject jsonObject = loginApi.qrKey();
        JSONObject data = jsonObject.getJSONObject("data");
        key = data.getString("unikey");
        MusicCacheUtils.put(userName + "key", key);
        qrKeySet.add(userName);
        return key;
    }

    @Override
    public int getCookie(String userName) {
        log.info("getCookie userName:{}", userName);
        String cookie = MusicCacheUtils.get(userName + "cookie", String.class);
        if (StringUtils.isNotEmpty(cookie)) {
            JSONObject status = loginApi.getStatus();
            log.info("getCookie MusicCacheUtils status:{}", status);
            status = status.getJSONObject("data");
            if (status.get("profile") != null) {
                userService.setMusicCookie(userName, cookie);
                qrKeySet.remove(userName);
                // 从缓存中获取到了music cookie
                log.info("从缓存中获取到了music cookie");
                return 803;
            }
        }
        cookie = userService.getMusicCookie(userName);
        if (StringUtils.isNotEmpty(cookie)) {
            MusicCacheUtils.put(userName + "cookie", cookie);
            JSONObject status = loginApi.getStatus();
            status = status.getJSONObject("data");
            if (status.get("profile") != null) {
                qrKeySet.remove(userName);
                // 从数据库中获取到了music cookie
                log.info("从数据库中获取到了music cookie");
                return 803;
            }
        }

        String key = getQrKey(userName);
        if (StringUtils.isEmpty(key)) {
            return 0;
            // throw new ServiceException("登录错误");
        }
        JSONObject jsonObject = loginApi.qrCheck(key);
        log.info("getCookie qrCheck:{}", jsonObject);
        Integer code = Optional.ofNullable(jsonObject.getInteger("code")).orElse(0);
        if (code.equals(800)) {
            clearQrKey(userName);
            return 800;//过期
        } else if (code.equals(801)) {
            return 801;// 等待扫码
        } else if (code.equals(802)) {
            return 802;// 待确认
        } else if (code.equals(803))  {
            cookie = jsonObject.getString("cookie");
            userService.setMusicCookie(userName, cookie);
            MusicCacheUtils.put(userName + "cookie", cookie);
            qrKeySet.remove(userName);
            return 803;// 成功
        } else {
            clearQrKey(userName);
            return 0;// 未知错误
        }
    }

    public void clearQrKey(String userName) {
        MusicCacheUtils.remove(userName + "key");
        MusicCacheUtils.remove(userName + "qrimg");
    }

    @Override
    public JSONObject getUserInfo() {
        return loginApi.getUserCount();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        qrKeySet.add("admin");
        new Timer().schedule(new TimerTask(){

            @Override
            public void run() {
                //log.info("定时器时间：" + LocalDateTime.ofInstant(Instant.ofEpochMilli(scheduledExecutionTime()), ZoneId.systemDefault()));
                if (qrKeySet.isEmpty()) {
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                for (String s : qrKeySet) {
                    int cookie = self.getCookie(s);
                    if (cookie == 803) {
                        qrKeySet.remove(s);
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 5000, 3000);
    }
}
