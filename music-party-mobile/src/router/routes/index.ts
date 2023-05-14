import {getLoginModuleRegExp} from '@/utils';

export const ROOT_ROUTE: AuthRoute.Route = {
  name: 'root',
  path: '/',
  redirect: import.meta.env.VITE_ROUTE_HOME_PATH,
  meta: {
    title: 'Root'
  }
};

export const constantRoutes: AuthRoute.Route[] = [
  ROOT_ROUTE,
  {
    name: 'login',
    path: '/login',
    component: 'self',
    props: route => {
      const moduleType = (route.params.module as EnumType.LoginModuleKey) || 'sign-in';
      return {
        module: moduleType
      };
    },
    meta: {
      title: 'login.title',
      dynamicPath: `/login/:module(${getLoginModuleRegExp()})?`,
      singleLayout: 'auth'
    }
  },
  {
    name: '403',
    path: '/403',
    component: 'self',
    meta: {
      title: 'error.forbidden',
      singleLayout: 'blank'
    }
  },
  {
    name: '404',
    path: '/404',
    component: 'self',
    meta: {
      title: 'error.notfound',
      singleLayout: 'error'
    }
  },
  {
    name: '500',
    path: '/500',
    component: 'self',
    meta: {
      title: 'error.other',
      singleLayout: 'error'
    }
  },
  {
    name: 'not-found',
    path: '/:pathMatch(.*)*',
    component: 'blank',
    meta: {
      title: 'error.notfound',
      singleLayout: 'blank'
    }
  }
];
