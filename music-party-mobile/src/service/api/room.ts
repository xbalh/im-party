import { mockRequest, request } from '../request';

//搜索歌曲
export function fetchRoomList() {
  return request.get<ApiRoomManagement.roomInfo[]>("/room/list");
}

//点歌
export function addMusic(roomId: number, songId: string) {
  return request.post(`/room/addMusic/${roomId}?songId=${songId}`);
}