package com.im.imparty.controller;

import com.im.imparty.common.exception.CustomException;
import com.im.imparty.common.util.SongUtils;
import com.im.imparty.common.vo.PlaySongInfo;
import com.im.imparty.music.playerlist.entity.MusicPlayerRecordDomain;
import com.im.imparty.music.playerlist.service.MusicPlayerRecordService;
import com.im.imparty.server.WebSocketServer;
import com.im.imparty.user.dto.UserInfo;
import com.im.imparty.user.service.UserService;
import com.im.imparty.web.vo.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Api("房间控制")
@RestController
@RequestMapping("/room")
public class MusicRoomController {

    @Resource
    private MusicPlayerRecordService musicPlayerRecordService;

    @Resource
    private MusicPlayerRecordService playerRecordService;

    @Resource
    private UserService userService;

    @ApiOperation("播放")
    @GetMapping("/play")
    public BaseResult<String> play(@RequestParam("roomId") Integer roomId) {
        if (WebSocketServer.roomMap.containsKey(roomId)) {
            List<PlaySongInfo> playSongInfos = playerRecordService.selectAllUnPlayMusicByRoomId(roomId);
            WebSocketServer.roomMap.get(roomId).play(0, playSongInfos, roomId);
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

    @ApiOperation("点歌")
    @PostMapping("/addMusic/{roomId}")
    public BaseResult addMusic(@PathVariable("roomId") Integer roomId, @RequestParam("songId") String songId) {
        MusicPlayerRecordDomain data = musicPlayerRecordService.addMusic(roomId, songId, SecurityContextHolder.getContext().getAuthentication().getName());

        Optional.ofNullable(SongUtils.getUrlBySongId(data.getSongId(), data.getSongQuality())).orElseThrow(() -> new CustomException("这首个没法听，换一首吧"));

        if (WebSocketServer.roomMap.get(roomId) != null) {
            PlaySongInfo songInfo = new PlaySongInfo();
            songInfo.setSongId(data.getSongId());
            songInfo.setSongName(data.getSongName());
            songInfo.setSongQuality(data.getSongQuality());
            songInfo.setSinger(data.getSinger());
            songInfo.setSort(data.getSort());
            songInfo.setTotalTime(data.getTotalTime());
            UserInfo userInfo = userService.getUserInfo(data.getCrtUsr());
            songInfo.setFrom(userInfo.getNickName() + "(" + userInfo.getUserName() + ")");
            WebSocketServer.roomMap.get(roomId).addSong(songInfo);
        }
        return BaseResult.ok();
    }

    @ApiOperation("切歌")
    @PostMapping("/skipMusic/{roomId}")
    public BaseResult skipMusic(@PathVariable("roomId") Integer roomId, @RequestParam("songIds") List<String> songIds) {
        musicPlayerRecordService.updateMusicPlayStatus(songIds, roomId);
        if (WebSocketServer.roomMap.get(roomId) != null) {
            WebSocketServer.roomMap.get(roomId).skipSong(songIds);
        }
        return BaseResult.ok();
    }

//    @ApiOperation("切歌")
//    @PostMapping("/addRoom")
//    public BaseResult skipMusic(@PathVariable("roomId") Integer roomId, @RequestParam("songIds") List<String> songIds) {
////        .updateMusicPlayStatus(songIds, roomId);
//        if (WebSocketServer.roomMap.get(roomId) != null) {
//            WebSocketServer.roomMap.get(roomId).skipSong(songIds);
//        }
//        return BaseResult.ok();
//    }

}
