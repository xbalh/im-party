package com.im.imparty.music;

import com.im.imparty.BaseTest;
import com.im.imparty.api.music.LoginApi;
import com.im.imparty.api.music.MusicApi;
import com.im.imparty.music.service.MusicLoginService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class LoginApiTest extends BaseTest {

    @Resource
    private LoginApi loginApi;

    @Resource
    private MusicApi musicApi;

    @Resource
    private MusicLoginService musicLoginService;

    @Test
    public void test() {
        //String s = musicApi.getSong(Arrays.asList("210049"), "exhigh");
        //System.out.println(s);
    }

    @Test
    public void check() {
        musicLoginService.getCookie("root");
        System.out.println("s");
    }
}
