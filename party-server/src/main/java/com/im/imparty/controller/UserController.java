package com.im.imparty.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.im.imparty.user.entity.User;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.web.vo.BaseResult;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController("/users")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/info")
    public BaseResult<String> currentUserInfo() {
        return BaseResult.ok(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/list")
    public List<User> userList() {
        return new ArrayList<User>() {
            {
                add(new User("1"));
                add(new User("2"));
                add(new User("3"));
                add(new User("4"));
            }
        };
    }

}
