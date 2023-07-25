import { mockRequest, request } from '../request';

//搜索歌曲
export function bindWyyUser(wyyUserId: string) {
  return request.post<Auth.UserInfo>("/users/updateWyyBind", wyyUserId, {
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  });
}


