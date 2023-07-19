import { mockRequest, request } from '../request';

//搜索歌曲
export function searchSong() {
  return request.get<ApiChatManagement.message>("/music/song/search");
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
