import { mockRequest, request } from '../request';

//搜索歌曲
export function searchSong() {
  return request.get<ApiChatManagement.message>("/music/song/search");
}

//搜索网易云用户
export function searchWyyUser(keywords: string, limit?: number, offset?: number) {
  return request.get<any>("/music/song/search", {
    params: {
      type: 1002,
      keywords: keywords,
      limit: limit || null,
      offset: offset || null
    }
  });
}

//获取用户歌单
export function fetchPlayList(uid: string) {
  return request.get<ApiMusic.playListInfo[]>("/music/playlist", {
    params: {
      uid: uid
    }
  });
}

//获取用户歌单
export function fetchPlayListAllMusic(id: string, limit: number, offset: number) {
  return request.get<ApiMusic.playListMusicInfo[]>("/music/playlist/track/all", {
    params: {
      id: id,
      limit: limit,
      offset: offset
    }
  });
}
