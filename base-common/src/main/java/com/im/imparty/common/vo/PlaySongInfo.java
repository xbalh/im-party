package com.im.imparty.common.vo;

import com.im.imparty.common.util.SongUtils;
import lombok.Data;


@Data
public class PlaySongInfo {

    private String songId;

    private Integer totalTime;

    private String songName;

    private Integer sort;

    private String url;

    private String singer;

    private String songQuality;

    public static PlaySongInfo defaultSong() {
        PlaySongInfo res = new PlaySongInfo();
        res.setSongId("210049");
        res.setTotalTime(294000);
        res.setSort(1);
        res.setSongName("布拉格广场");
        res.setSinger("蔡依林,周杰伦");
        res.setSongQuality("lossless");
        res.setUrl(SongUtils.getUrlBySongId(res.getSongId(), res.getSongQuality()));
        return res;
    }
}
