package com.im.imparty.controller;

import com.im.imparty.music.service.MusicLoginService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        String loginQr = musicLoginService.getLoginQr();
        Map<String, String> param = new HashMap<>();
        param.put("image", loginQr);
        return new ModelAndView("index", param);
    }

}
