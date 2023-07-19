package com.im.imparty.controller;

import com.alibaba.fastjson.JSONArray;
import com.im.imparty.music.service.MusicSongService;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("音乐")
@RestController
public class MusicUserController {

    @Resource
    private MusicSongService musicSongService;

    @GetMapping("/music/playlist")
    @ApiOperation("获取用户歌单")
    public BaseResult<JSONArray> getPlayList(@RequestParam("uid") String uid) {
        return BaseResult.ok("", musicSongService.getPlayList(uid));
    }

    @GetMapping("/music/playlist/track/all")
    @ApiOperation("获取歌单所有歌曲")
    public BaseResult<JSONArray> getPlayListAllMusic(@RequestParam("id") String id,
                                                     @RequestParam("limit") Integer limit,
                                                     @RequestParam("offset") Integer offset) {
        return BaseResult.ok("", musicSongService.getPlayListAllMusic(id, limit, offset));
    }

}
