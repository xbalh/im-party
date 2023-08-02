package com.im.imparty.controller;

import com.im.imparty.common.exception.CustomException;
import com.im.imparty.common.vo.PlaySongInfo;
import com.im.imparty.server.WebSocketServer;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.user.dto.UserInfoDetail;
import com.im.imparty.user.entity.UserDomain;
import com.im.imparty.user.mapper.UserMapper;
import com.im.imparty.user.service.UserService;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @GetMapping("/info")
    public BaseResult<UserInfo> currentUserInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userService.getUserInfo(userName);
        return BaseResult.ok(userInfo);
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

    @PostMapping("/updateWyyBind")
    public BaseResult<UserInfo> updateWyyBind(@RequestBody String wyyUserId) {
        if(StringUtils.isBlank(wyyUserId)){
            throw new CustomException("请选择一个要绑定的用户");
        }
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateWyyBind(userName, wyyUserId);
        return BaseResult.ok(userService.getUserInfo(userName));
    }

    @ApiOperation("批量获取用户基本信息")
    @PostMapping("/fetchUsersInfoBatch")
    public BaseResult<List<UserInfo>> fetchUsersInfoBatch(@RequestBody List<String> userNames) {
        List<UserInfo> userInfoList = userService.getUserInfoBatchByUserNames(userNames);
        return BaseResult.ok(userInfoList);
    }

    @PostMapping("/updateUserInfo")
    public BaseResult<UserInfo> updateUserInfo(@RequestBody UserDomain userDomain) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateUserInfo(userDomain, userName);
        return BaseResult.ok(userService.getUserInfo(userName));
    }

}
