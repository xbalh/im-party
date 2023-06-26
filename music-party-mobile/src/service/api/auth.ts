import { mockRequest, request } from '../request';

export function fetchSmsCode(phone: string) {
  return mockRequest.post<boolean>('/getSmsCode', { phone });
}

export function fetchLogin(username: string, password: string) {
  return request.post<ApiAuth.Token>('/login', { username, password });
  // return mockRequest.post<ApiAuth.Token>('/login', { userName, password });
}

export function fetchUserInfo() {
  return request.get<ApiAuth.Token>('/users/info');
  // return mockRequest.get<ApiAuth.UserInfo>('/getUserInfo');
}

export function fetchUserRoutes(userName: string) {
  return mockRequest.post<ApiRoute.Route>('/getUserRoutes', { userName });
}

export function fetchUpdateToken(refreshToken: string) {
  return mockRequest.post<ApiAuth.Token>('/updateToken', { refreshToken });
}
