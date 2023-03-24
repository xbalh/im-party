package com.im.imparty.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.user.entity.User;
import com.im.imparty.user.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/list")
    public List<User> userList() {
        return userMapper.selectList(new QueryWrapper<>());
    }

}
