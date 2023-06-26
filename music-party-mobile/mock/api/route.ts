import type {MockMethod} from 'vite-plugin-mock';
import {routeModel, userModel} from '../model';

const apis: MockMethod[] = [
  {
    url: '/mock/getUserRoutes',
    method: 'post',
    response: (options: Service.MockOption): Service.MockServiceResult => {
      const {userId = undefined} = options.body;

      const routeHomeName: AuthRoute.LastDegreeRouteKey = 'apps_chat';

      const role = userModel.find(item => item.userId === userId)?.userRole || 'user';

      const filterRoutes = routeModel['user'];

      return {
        code: 200,
        message: 'ok',
        data: {
          routes: filterRoutes,
          home: routeHomeName
        }
      };
    }
  }
];

export default apis;
