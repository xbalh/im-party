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
