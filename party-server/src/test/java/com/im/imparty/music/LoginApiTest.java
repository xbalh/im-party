package com.im.imparty.music;

import com.im.imparty.BaseTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class LoginApiTest extends BaseTest {

    @Resource
    private LoginApi loginApi;

    @Resource
    private MusicApi musicApi;

    @Test
    public void test() {
        String s = loginApi.qrKey();
        System.out.println(s);
    }
}
