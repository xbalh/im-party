import {computed} from 'vue';
import {useRoute} from 'vue-router';
import {routePath} from '@/router';
import {useRouteStore} from '@/store';
import {getBreadcrumbsByPredicate} from '@/utils';


export default function useBreadcrumb(rootPath: Exclude<AuthRoute.AllRouteKey, 'not-found'> = 'root') {
  const route = useRoute();
  const routeStore = useRouteStore();


  const breadcrumbs = computed(() =>
    getBreadcrumbsByPredicate(menu => {
      return !!route.matched.find(m => m.path == menu.routePath)
    }, routeStore.menus as App.GlobalMenuOption[], routePath(rootPath))
  );

  return {
    breadcrumbs
  };
}
