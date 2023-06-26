import {mockRequest, request} from '../request';

//搜索歌曲
export function searchSong() {
  return request.get<ApiChatManagement.message>("/music/song/search");
}
