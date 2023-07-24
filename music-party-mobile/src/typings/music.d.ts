declare namespace Music {

  interface SongInfo {
    singer?: string;
    songId?: string;
    songName?: string;
    songQuality?: string;
    sort?: number;
    totalTime?: number;
    url?: string;
    from?: string;
    songDetailInfo?: ApiMusic.playListMusicInfo;
    nowTime?: number;
    type: string;
  }
}


