import { mockRequest, request } from '../request';

//搜索歌曲
export function bindWyyUser(wyyUserId: string) {
  return request.post<Auth.UserInfo>("/users/updateWyyBind", wyyUserId, {
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  });
}

//批量获取用户信息
export function fetchUserInfoBatch(userNames: string[]) {
  return request.post<ApiAuth.UserInfo[]>("/users/fetchUsersInfoBatch", userNames);
}


