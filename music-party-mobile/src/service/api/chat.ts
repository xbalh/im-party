import {mockRequest} from '../request';

export function fetchMessage() {
  return mockRequest.post<ApiChatManagement.message>("/getMessage");
}
