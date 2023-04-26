package com.im.imparty.controller;

import com.im.imparty.server.WebSocketServer;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("房间控制")
@RestController
@RequestMapping("/room")
public class MusicRoomController {

    @ApiOperation("播放")
    @GetMapping("/play")
    public BaseResult<String> play(@RequestParam("roomId") Integer roomId) {
        if (WebSocketServer.roomMap.containsKey(roomId)) {
            WebSocketServer.roomMap.get(roomId).play(0);
        } else {
            return BaseResult.fail("房间不存在！");
        }
        return BaseResult.ok("开始播放");
    }

    @ApiOperation("获取当前播放时间")
    @GetMapping("/getCurrentTime")
    public BaseResult<Long> getCurrentTime(@RequestParam("roomId") Integer roomId) {
        if (WebSocketServer.roomMap.containsKey(roomId)) {
            return BaseResult.ok(WebSocketServer.roomMap.get(roomId).getCurrentTime());
        } else {
            return BaseResult.fail("房间不存在！");
        }
    }

}
