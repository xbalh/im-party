package com.im.imparty.websocket;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.im.imparty.api.music.MusicApi;
import com.im.imparty.common.exception.CustomException;
import com.im.imparty.common.exception.ServiceException;
import com.im.imparty.common.util.SongUtils;
import com.im.imparty.common.vo.PlaySongInfo;
import com.im.imparty.music.playerlist.service.MusicPlayerRecordService;
import com.im.imparty.spring.authentication.LoginJwtToken;
import com.im.imparty.spring.util.SpringFactoryUtils;
import com.im.imparty.websocket.conts.MsgJSON;
import com.im.imparty.websocket.timer.PlayTimer;
import com.im.imparty.websocket.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import javax.websocket.Session;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class WebsocketSessionManager {

    private ConcurrentHashMap<String, WebsocketSessionImpl> socketStore = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, ConcurrentHashMap<String, WebsocketSessionImpl>> socketStoreByRole = new ConcurrentHashMap<>();

    private static AtomicInteger count = new AtomicInteger();

    private final ConcurrentLinkedQueue<PlaySongInfo> songList = new ConcurrentLinkedQueue();

    private PlaySongInfo currentSongInfo = null;

    // 播放计时器
    private PlayTimer playTimer;

    private Integer roomId;

    public boolean addUser(Session session, Integer roomId) {
        this.roomId = roomId;
        Principal userPrincipal = session.getUserPrincipal();
        String name = userPrincipal.getName();
        if (name == null || name == "") {
            return false;
        }
        WebsocketSessionImpl websocketSession = new WebsocketSessionImpl(session, name, this);
        websocketSession.setValidFlag(true);
        if (userPrincipal instanceof LoginJwtToken) {
            Collection<GrantedAuthority> authorities = ((LoginJwtToken) userPrincipal).getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : Optional.ofNullable(authorities).orElse(Collections.emptyList())) {
                roles.add(authority.getAuthority());
                if (socketStoreByRole.containsKey(authority.getAuthority())) {
                    socketStoreByRole.get(authority.getAuthority()).put(name, websocketSession);
                } else {
                    ConcurrentHashMap<String, WebsocketSessionImpl> tmpMap = new ConcurrentHashMap<>();
                    tmpMap.put(name, websocketSession);
                    socketStoreByRole.put(authority.getAuthority(), tmpMap);
                }
            }
            websocketSession.setRoleList(roles);
        }

        socketStore.put(name, websocketSession);
        // count.incrementAndGet();
        websocketSession.sendMessage(MsgJSON.songListChange(songList, this.roomId).toJSONString());
        broadcastMsg(MsgJSON.userJoin(name, this.roomId).toJSONString());
        List<String> currentUserNameList = new ArrayList<>(socketStore.keySet());
        websocketSession.sendMessage(MsgJSON.currentUserList(currentUserNameList, this.roomId).toJSONString());
        if (currentSongInfo != null && playTimer != null && playTimer.isRunning()) {
            websocketSession.sendMessage(MsgJSON.play(currentSongInfo, playTimer.getCurrentTime()).toJSONString());
        }
        return true;
    }

    public boolean close(Session session) {
        WebsocketSessionImpl sessionImpl = getSessionBySession(session);
        return close(sessionImpl);
    }

    public boolean close(WebsocketSessionImpl sessionImpl) {
        Session session = sessionImpl.getSession();
        String userName = SessionUtils.getUserName(session);
        if (socketStore.remove(userName) != null) {
            for (GrantedAuthority userRole : getUserRoles(session)) {
                if (socketStoreByRole.containsKey(userRole.getAuthority())) {
                    socketStoreByRole.get(userRole.getAuthority()).remove(userName);
                }
            }
            sessionImpl.close();
            // count.decrementAndGet();
            broadcastMsg(MsgJSON.userLeave(userName, roomId).toJSONString());
            return true;
        }
        return false;
    }

    public int count() {
        return socketStore.size();
    }

    public void broadcastMsg(String msg) {
        for (WebsocketSessionImpl value : socketStore.values()) {
            if (value.getSession().isOpen()) {
                value.sendMessage(msg);
            }
        }
    }

    public WebsocketSessionImpl getSessionByUserName(String userName) {
        return socketStore.get(userName);
    }

    public WebsocketSessionImpl getSessionBySession(Session session) {
        String userName = SessionUtils.getUserName(session);
        return socketStore.get(userName);
    }

    private Collection<GrantedAuthority> getUserRoles(Session session) {
        if (session.getUserPrincipal() instanceof LoginJwtToken) {
            Collection<GrantedAuthority> authorities = ((LoginJwtToken) session.getUserPrincipal()).getAuthorities();
            return authorities;
        }
        return Collections.emptyList();
    }

    public PlayTimer init(Collection<PlaySongInfo> songList) {
        initSongList(songList);
        PlaySongInfo playSongInfo1 = nextSong();
        if (playSongInfo1 == null) {
            throw new CustomException("当前房间的播放队列为空，请先点歌");
        }
        currentSongInfo = playSongInfo1;
        MusicApi musicApi = SpringFactoryUtils.getBean(MusicApi.class);
        JSONObject songDetail = musicApi.getSongDetail(ImmutableList.of(playSongInfo1.getSongId()));
        JSONArray songs = songDetail.getJSONArray("songs");
        JSONObject song = songs.getJSONObject(0);
        playSongInfo1.setSongDetailInfo(song);
        broadcastMsg(MsgJSON.nextPlay(playSongInfo1).toJSONString());

        MusicPlayerRecordService musicPlayerRecordService = SpringFactoryUtils.getBean(MusicPlayerRecordService.class);
        this.playTimer = new PlayTimer(playSongInfo1.getTotalTime(), (o) -> {
            if (o) {
                PlaySongInfo playSongInfo = nextSong();
                if (currentSongInfo != null) {
                    List<PlaySongInfo> collect = songList.stream().filter(item -> !currentSongInfo.getSongId().equals(item.getSongId())).collect(Collectors.toList());
                    songList.clear();
                    songList.addAll(collect);
                    // songList.remove(currentSongInfo);
                    musicPlayerRecordService.updateMusicPlayStatus(ImmutableList.of(currentSongInfo.getSongId()), this.roomId);
                    broadcastMsg(MsgJSON.songListChange(songList, roomId).toJSONString());
                }
                if (playSongInfo == null) {
                    currentSongInfo = null;
                    playTimer.over();
                    log.info("房间号：{}，播放完毕！", roomId);
                    return null;
                }
                currentSongInfo = playSongInfo;
                JSONObject songDetail1 = musicApi.getSongDetail(ImmutableList.of(playSongInfo.getSongId()));
                JSONArray songs1 = songDetail1.getJSONArray("songs");
                JSONObject song1 = songs1.getJSONObject(0);
                playSongInfo.setSongDetailInfo(song1);
                broadcastMsg(MsgJSON.nextPlay(playSongInfo).toJSONString());
                playTimer.playSong(playSongInfo.getTotalTime());

            } else {
                log.info("房间号：{}，当前播放信息：{},播放时间：{}, 总共时间：{}", roomId, currentSongInfo.getSongName(), playTimer.getCurrentTime(), playTimer.getTotalTime());
                log.info("房间号：{}， 播放列表: {}", roomId, songList.stream().map(PlaySongInfo::getSongName).collect(Collectors.joining("|")));
                // broadcastMsg(MsgJSON.currentTime(getCurrentTime()).toJSONString());
            }
            return null;
        });
        musicPlayerRecordService.updateMusicPlayStatus(ImmutableList.of(playSongInfo1.getSongId()), this.roomId);
        return playTimer;
    }

    public void play(long startTime, List<PlaySongInfo> songList, Integer roomId) {
        this.roomId = roomId;
        if (this.playTimer == null) {
            init(songList);
        }
        playTimer.play(startTime);
    }

    public void play(String songId, Integer roomId) {
        if (this.playTimer == null) {
            init(songList);
        }
        PlaySongInfo playSongInfo = songList.stream().filter(item -> songId.equals(item.getSongId())).findFirst().orElseThrow(
                () -> new ServiceException("歌曲不存在！")
        );
        playTimer.playSong(playSongInfo.getTotalTime());

        JSONObject songDetail1 = SpringFactoryUtils.getBean(MusicApi.class).getSongDetail(ImmutableList.of(playSongInfo.getSongId()));
        JSONArray songs1 = songDetail1.getJSONArray("songs");
        JSONObject song1 = songs1.getJSONObject(0);
        playSongInfo.setSongDetailInfo(song1);
        playSongInfo.setUrl(SongUtils.getUrlBySongId(songId, playSongInfo.getSongQuality()));

        broadcastMsg(MsgJSON.play(playSongInfo, 0L).toJSONString());
        this.currentSongInfo = playSongInfo;
    }

    public long getCurrentTime() {
        return playTimer.getCurrentTime();
    }

    public void initSongList(Collection<PlaySongInfo> dataList) {
        if (dataList == songList) {
            return;
        }
        songList.clear();
        songList.addAll(dataList);
    }

    public void addSong(PlaySongInfo songInfo) {
        songList.add(songInfo);
        // ssongList.sort(Comparator.comparingInt(PlaySongInfo::getSort));
        broadcastMsg(MsgJSON.songListChange(songList, roomId).toJSONString());
        if (currentSongInfo == null) {
            play(songInfo.getSongId(), roomId);
        }
    }

    public PlaySongInfo nextSong() {
        if (songList.isEmpty()) {
//            PlaySongInfo playSongInfo = PlaySongInfo.defaultSong();
//            currentSongId = playSongInfo.getSongId();
//            return playSongInfo;
            return null;
        }
        PlaySongInfo playSongInfo = null;
        if (currentSongInfo == null) {
            playSongInfo = songList.element();
        }
        if (playSongInfo == null) {
            Iterator<PlaySongInfo> iterator = songList.iterator();
            while (iterator.hasNext()) {
                PlaySongInfo next = iterator.next();
                if (next.getSongId().equals(currentSongInfo.getSongId())) {
                    if (iterator.hasNext()) {
                        playSongInfo = iterator.next();
                    }
                    break;
                }
            }
        }
        if (playSongInfo == null) {
            if (songList.size() > 1) {
                playSongInfo = songList.element();
            } else {
                return null;
            }
        }
        playSongInfo.setUrl(SongUtils.getUrlBySongId(playSongInfo.getSongId(), playSongInfo.getSongQuality()));
        return playSongInfo;
    }

    public void skipSong(List<String> songIds) {
        if(!songList.isEmpty()){
            songList.removeIf(song-> songIds.contains(song.getSongId()));
            init(BeanUtil.copyToList(songList, PlaySongInfo.class));
        }
    }
}
