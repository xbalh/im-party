package com.im.imparty;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.user.entity.User;
import com.im.imparty.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class BaseTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        log.info("{}", users);
    }
}
