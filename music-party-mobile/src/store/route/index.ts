import {defineStore} from 'pinia';
import {ROOT_ROUTE, constantRoutes, router, routes as staticRoutes} from '@/router';
import {fetchUserRoutes} from '@/service';
import {
  localStg,
  filterAuthRoutesByUserPermission,
  getCacheRoutes,
  getConstantRouteNames,
  transformAuthRouteToVueRoutes,
  transformAuthRouteToVueRoute,
  transformAuthRouteToMenu,
  transformAuthRouteToSearchMenus,
  transformRouteNameToRoutePath,
  transformRoutePathToRouteName,
  sortRoutes
} from '@/utils';
import {useAuthStore} from '../auth';

interface RouteState {
  authRouteMode: ImportMetaEnv['VITE_AUTH_ROUTE_MODE'];
  isInitAuthRoute: boolean;
  routeHomeName: AuthRoute.AllRouteKey;
  menus: App.GlobalMenuOption[];
  searchMenus: AuthRoute.Route[];
  cacheRoutes: string[];
}

export const useRouteStore = defineStore('route-store', {
  state: (): RouteState => ({
    authRouteMode: import.meta.env.VITE_AUTH_ROUTE_MODE,
    isInitAuthRoute: false,
    routeHomeName: transformRoutePathToRouteName(import.meta.env.VITE_ROUTE_HOME_PATH),
    menus: [],
    searchMenus: [],
    cacheRoutes: []
  }),
  actions: {
    resetRouteStore() {
      this.resetRoutes();
      this.$reset();
    },
    resetRoutes() {
      const routes = router.getRoutes();
      routes.forEach(route => {
        const name = (route.name || 'root') as AuthRoute.AllRouteKey;
        if (!this.isConstantRoute(name)) {
          router.removeRoute(name);
        }
      });
    },
    isConstantRoute(name: AuthRoute.AllRouteKey) {
      const constantRouteNames = getConstantRouteNames(constantRoutes);
      return constantRouteNames.includes(name);
    },
    isValidConstantRoute(name: AuthRoute.AllRouteKey) {
      const NOT_FOUND_PAGE_NAME: AuthRoute.NotFoundRouteKey = 'not-found';
      const constantRouteNames = getConstantRouteNames(constantRoutes);
      return constantRouteNames.includes(name) && name !== NOT_FOUND_PAGE_NAME;
    },
    handleAuthRoute(routes: AuthRoute.Route[]) {
      (this.menus as App.GlobalMenuOption[]) = transformAuthRouteToMenu(routes);
      this.searchMenus = transformAuthRouteToSearchMenus(routes);

      const vueRoutes = transformAuthRouteToVueRoutes(routes);

      vueRoutes.forEach(route => {
        router.addRoute(route);
      });

      this.cacheRoutes = getCacheRoutes(vueRoutes);
    },
    handleUpdateRootRedirect(routeKey: AuthRoute.AllRouteKey) {
      if (routeKey === 'root' || routeKey === 'not-found') {
        throw new Error('root or not-found should not be routeKey');
      }
      const rootRoute: AuthRoute.Route = {...ROOT_ROUTE, redirect: transformRouteNameToRoutePath(routeKey)};
      const rootRouteName: AuthRoute.AllRouteKey = 'root';
      router.removeRoute(rootRouteName);
      const rootVueRoute = transformAuthRouteToVueRoute(rootRoute)[0];
      router.addRoute(rootVueRoute);
    },
    async initDynamicRoute() {
      const {userId} = localStg.get('userInfo') || {};

      if (!userId) {
        throw new Error('userId is mandatory ');
      }

      const {error, data} = await fetchUserRoutes(userId);

      if (!error) {
        this.routeHomeName = data.home;
        this.handleUpdateRootRedirect(data.home);
        this.handleAuthRoute(sortRoutes(data.routes));

        this.isInitAuthRoute = true;
      }
    },
    async initStaticRoute() {
      const auth = useAuthStore();

      const routes = filterAuthRoutesByUserPermission(staticRoutes, auth.userInfo.userRole);
      this.handleAuthRoute(routes);

      this.isInitAuthRoute = true;
    },
    async initAuthRoute() {
      if (this.authRouteMode === 'dynamic') {
        await this.initDynamicRoute();
      } else {
        await this.initStaticRoute();
      }
    }
  }
});
