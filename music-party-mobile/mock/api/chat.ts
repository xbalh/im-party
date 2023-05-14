import {mock} from 'mockjs';
import type {MockMethod} from 'vite-plugin-mock';

const apis: MockMethod[] = [
  {
    url: '/mock/getMessage',
    method: 'post',
    response: (): Service.MockServiceResult<ApiChatManagement.message> => {

      const data = mock({
        id: '@id',
        text: '@sentence',
        timestamp: '@datetime',
        user: {
          id: '@id',
          avatar: () => {
            return 'avatar' + mock({
              "number|1-20": 2
            })['number']
          }
        }
      })

      return {
        code: 200,
        message: 'ok',
        data
      };
    }
  },
];

export default apis;
