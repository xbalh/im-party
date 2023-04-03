package com.im.imparty;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class BaseTest {

    @Resource
    private UserService userService;

    @Test
    public void test() {
        UserInfoDetail users = userService.getUserDetail("root");
        users = userService.getUserDetail("root");
        log.info("{}", users);
    }
}
