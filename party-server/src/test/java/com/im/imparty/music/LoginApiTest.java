package com.im.imparty.music;

import com.im.imparty.BaseTest;
import com.im.imparty.music.api.LoginApi;
import com.im.imparty.music.api.MusicApi;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Arrays;

public class LoginApiTest extends BaseTest {

    @Resource
    private LoginApi loginApi;

    @Resource
    private MusicApi musicApi;

    @Test
    public void test() {
        String s = musicApi.getSong(Arrays.asList("210049"), "exhigh");
        System.out.println(s);
    }
}
