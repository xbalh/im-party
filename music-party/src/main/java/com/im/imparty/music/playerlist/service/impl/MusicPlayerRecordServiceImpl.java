package com.im.imparty.music.playerlist.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableList;
import com.im.imparty.api.music.MusicApi;
import com.im.imparty.common.enums.SongQualityEnum;
import com.im.imparty.common.exception.CustomException;
import com.im.imparty.common.vo.PlaySongInfo;
import com.im.imparty.music.playerlist.entity.MusicPlayerRecordDomain;
import com.im.imparty.music.playerlist.mapper.MusicPlayerRecordMapper;
import com.im.imparty.music.playerlist.service.MusicPlayerRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 2
 * @since 2023-05-07
 */
@Service
public class MusicPlayerRecordServiceImpl extends ServiceImpl<MusicPlayerRecordMapper, MusicPlayerRecordDomain> implements MusicPlayerRecordService {

    @Resource
    private MusicApi musicApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MusicPlayerRecordDomain addMusic(Integer roomId, String songId, String curUsr) {
        boolean isRepeat = count(lambdaQuery().eq(MusicPlayerRecordDomain::getSongId, songId)
                .eq(MusicPlayerRecordDomain::getIsPlayed, 0).getWrapper()) > 0;
        if (isRepeat) {
            throw new CustomException("当前歌曲已经在排队了哦~");
        }
        JSONObject songDetail = musicApi.getSongDetail(ImmutableList.of(songId));
        JSONArray songs = songDetail.getJSONArray("songs");
        if (songs == null || songs.size() == 0) {
            throw new CustomException("歌曲不存在");
        }
        JSONObject song = songs.getJSONObject(0);
        lambdaQuery().eq(MusicPlayerRecordDomain::getRoomId, roomId);
        MusicPlayerRecordDomain data = new MusicPlayerRecordDomain();
        data.setCrtUsr(curUsr);
        data.setSort(Optional.ofNullable(getBaseMapper().selectMaxSort(roomId)).orElse(1));
        data.setSongId(songId);
        JSONArray singerList = song.getJSONArray("ar");
        if (singerList != null && singerList.size() > 0) {
            data.setSinger(singerList.stream().map(item -> (String) ((LinkedHashMap) item).get("name")).collect(Collectors.joining("/")));
        }
        data.setSongName(song.getString("name"));
        data.setTotalTime(song.getInteger("dt"));
        data.setRoomId(roomId);
        List<String> songQuality = new ArrayList<>();
        if (song.get("sq") != null) {
            songQuality.add(SongQualityEnum.LOSSLESS.getCode());
        }
        if (song.get("h") != null) {
            songQuality.add(SongQualityEnum.EXHIGH.getCode());
        }
        if (song.get("m") != null) {
            songQuality.add(SongQualityEnum.HIGHER.getCode());
        }

        if (song.get("l") != null) {
            songQuality.add(SongQualityEnum.STANDARD.getCode());
        }
        data.setSongQuality(String.join(",", songQuality));
        data.setIsPlayed(0);
        data.setCrtTim(new Date());
        save(data);
        return data;
    }

    @Override
    public List<PlaySongInfo> selectAllUnPlayMusicByRoomId(Integer roomId) {
        Collection<MusicPlayerRecordDomain> list = lambdaQuery()
                .eq(MusicPlayerRecordDomain::getRoomId, roomId)
                .eq(MusicPlayerRecordDomain::getIsPlayed, 0)
                .orderByAsc(MusicPlayerRecordDomain::getCrtTim).list();
        return list.stream().map(item -> {
            PlaySongInfo res = new PlaySongInfo();
            res.setSort(item.getSort());
            res.setSongName(item.getSongName());
            res.setTotalTime(item.getTotalTime());
            res.setSongId(item.getSongId());
            res.setSongQuality(item.getSongQuality());
            res.setSinger(item.getSinger());
            res.setFrom(item.getCrtUsr());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateMusicPlayStatus(List<String> songIds, Integer roomId) {
        lambdaUpdate().eq(MusicPlayerRecordDomain::getRoomId, roomId)
                .in(MusicPlayerRecordDomain::getSongId, songIds)
                .set(MusicPlayerRecordDomain::getIsPlayed, 1).update();
    }
}
