package com.im.imparty.controller;

import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.web.vo.BaseResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public List<UserDomain> userList() {
        return null;
//        return new ArrayList<User>() {
//            {
//                add(new User("1"));
//                add(new User("2"));
//                add(new User("3"));
//                add(new User("4"));
//            }
//        };
    }

    // public BaseResult<String> getWebsocket

}
