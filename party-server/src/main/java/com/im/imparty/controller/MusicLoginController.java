package com.im.imparty.controller;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.common.util.RequestUtils;
import com.im.imparty.music.service.MusicLoginService;
import com.im.imparty.spring.util.MusicCacheUtils;
import com.im.imparty.web.vo.BaseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MusicLoginController {

    @Resource
    private MusicLoginService musicLoginService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/music/login")
    public ModelAndView musicLogin() {
        String userName = RequestUtils.getUserName();
        String loginQr = musicLoginService.getLoginQr(userName);
        Map<String, String> param = new HashMap<>();
        param.put("image", loginQr);
        param.put("key", MusicCacheUtils.get(userName + "key", String.class));
        return new ModelAndView("index", param);
    }

    @ResponseBody
    @GetMapping("/music/check")
    public BaseResult<String> musicCheck() {
        int cookie = musicLoginService.getCookie(RequestUtils.getUserName());
        switch (cookie) {
            case 0:
            case 800:
                return BaseResult.build(2000, "过期了，请刷新二维码！");
            case 802:
                return BaseResult.build(2001, "待确认");
            case 803:
                return BaseResult.build(2002, "登录成功");
        }
        return BaseResult.build(2003, "请扫码！");
    }

    @ResponseBody
    @GetMapping("/music/getUserInfo")
    public BaseResult<JSONObject> getUserInfo() {
        return BaseResult.ok("", musicLoginService.getUserInfo());
    }

}
