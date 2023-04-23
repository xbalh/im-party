package com.im.imparty.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.im.imparty.common.util.RequestUtils;
import com.im.imparty.music.service.MusicLoginService;
import com.im.imparty.music.service.MusicSongService;
import com.im.imparty.spring.util.MusicCacheUtils;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("音乐")
@RestController
public class MusicSongController {

    @Resource
    private MusicSongService musicSongService;

    @ApiOperation("搜索")
    @GetMapping("/music/song/search")
    public BaseResult<JSONObject> musicLogin(@RequestParam("keywords") String keywords, @RequestParam(value = "type", required = false) Integer type,
                                   @RequestParam(value = "offset", required = false) Integer offset, @RequestParam(value = "limit", required = false) Integer limit) {
        return BaseResult.ok("", musicSongService.searchSongList(keywords, type, offset, limit));
    }

    @GetMapping("/music/song/getUrl")
    @ApiOperation("获取播放连接 level: 播放音质等级, 分为 standard => 标准,higher => 较高, exhigh=>极高, lossless=>无损, hires=>Hi-Res")
    public BaseResult<JSONArray> musicUrl(@RequestParam("songIds") List<String> songIds, @RequestParam("level") String level) {
        return BaseResult.ok("", musicSongService.getSong(songIds, level));
    }

}
