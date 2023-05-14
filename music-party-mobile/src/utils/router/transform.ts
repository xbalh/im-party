import type {RouteRecordRaw} from 'vue-router';
import {getLayoutComponent, getViewComponent} from '@/utils';
import i18n from "@/plugins/vue-i18n";

export function transformAuthRouteToVueRoutes(routes: AuthRoute.Route[]) {
  return routes.map(route => transformAuthRouteToVueRoute(route)).flat(1);
}

type ComponentAction = Record<AuthRoute.RouteComponentType, () => void>;

export function transformAuthRouteToVueRoute(item: AuthRoute.Route) {
  const resultRoute: RouteRecordRaw[] = [];

  const itemRoute = {...item} as RouteRecordRaw;

  if (hasDynamicPath(item)) {
    Object.assign(itemRoute, {path: item.meta.dynamicPath});
  }

  if (hasHref(item)) {
    Object.assign(itemRoute, {component: getViewComponent('404')});
  }

  if (hasComponent(item)) {
    const action: ComponentAction = {
      basic() {
        itemRoute.component = getLayoutComponent('basic');
      },
      blank() {
        itemRoute.component = getLayoutComponent('blank');
      },
      auth() {
        itemRoute.component = getLayoutComponent('auth');
      },
      error() {
        itemRoute.component = getLayoutComponent('error');
      },
      todo() {
        itemRoute.component = getLayoutComponent('todo');
      },
      chat() {
        itemRoute.component = getLayoutComponent('chat');
      },
      self() {
        itemRoute.component = getViewComponent(item.name as AuthRoute.LastDegreeRouteKey);
      }
    };
    try {
      if (item.component) {
        action[item.component]();
      } else {
        window.console.error('router resolve failed: ', item);
      }
    } catch {
      window.console.error('router resolve failed: ', item);
    }
  }

  if (isSingleRoute(item)) {
    if (hasChildren(item)) {
      window.console.error('singleRoute should not has child: ', item);
    }

    if (item.name === 'not-found') {
      itemRoute.children = [
        {
          path: '',
          name: item.name,
          component: getViewComponent('not-found')
        }
      ];
    } else {

      //prepare parent layout
      const parentPath = `${itemRoute.path}-parent` as AuthRouteUtils.SingleRouteKey;

      let layout
      switch (item.meta.singleLayout) {
        case 'blank':
          layout = getLayoutComponent('blank')
          break
        case 'auth':
          layout = getLayoutComponent('auth')
          break
        case 'error':
          layout = getLayoutComponent('error')
          break
        case 'basic':
          layout = getLayoutComponent('basic')
          break
        case 'todo':
          layout = getLayoutComponent('todo')
          break
        case 'chat':
          layout = getLayoutComponent('chat')
          break
        default:
          layout = getLayoutComponent('blank')
      }
      const parentRoute: RouteRecordRaw = {
        path: parentPath,
        component: layout,
        redirect: item.path,
        children: [itemRoute]
      };

      return [parentRoute];
    }
  }

  if (hasChildren(item)) {
    const children = (item.children as AuthRoute.Route[]).map(child => transformAuthRouteToVueRoute(child)).flat();

    const redirectPath = (children.find(v => !v.meta?.multi)?.path || '/') as AuthRoute.RoutePath;

    if (redirectPath === '/') {
      window.console.error('could not found effective child in multiple router ', item);
    }

    itemRoute.children = children;
    itemRoute.redirect = redirectPath;
  }

  resultRoute.push(itemRoute);

  return resultRoute;
}

export function transformAuthRouteToSearchMenus(routes: AuthRoute.Route[], treeMap: AuthRoute.Route[] = [], parentTitle: string = '') {
  if (routes && routes.length === 0) return [];
  return routes.reduce((acc, cur) => {
    const parent = parentTitle.length > 0 ? parentTitle + "/" : parentTitle
    const title = parent + i18n.global.t(cur.meta.title)
    if (!cur.meta?.hide && (!cur.children || cur.children?.length == 0)) {
      acc.push({
        ...cur,
        meta: {
          ...cur,
          title,
        }
      });
    }
    if (cur.children && cur.children.length > 0) {
      transformAuthRouteToSearchMenus(cur.children, treeMap, title);
    }
    return acc;
  }, treeMap);
}

export function transformRouteNameToRoutePath(name: Exclude<AuthRoute.AllRouteKey, 'not-found'>): AuthRoute.RoutePath {
  const rootPath: AuthRoute.RoutePath = '/';
  if (name === 'root') return rootPath;

  const splitMark = '_';
  const pathSplitMark = '/';
  const path = name.split(splitMark).join(pathSplitMark);

  return (pathSplitMark + path) as AuthRoute.RoutePath;
}

export function transformRoutePathToRouteName<K extends AuthRoute.RoutePath>(path: K) {
  if (path === '/') return 'root';

  const pathSplitMark = '/';
  const routeSplitMark = '_';

  const name = path.split(pathSplitMark).slice(1).join(routeSplitMark) as AuthRoute.AllRouteKey;

  return name;
}

function hasHref(item: AuthRoute.Route) {
  return Boolean(item.meta.href);
}

function hasDynamicPath(item: AuthRoute.Route) {
  return Boolean(item.meta.dynamicPath);
}

function hasComponent(item: AuthRoute.Route) {
  return Boolean(item.component);
}

function hasChildren(item: AuthRoute.Route) {
  return Boolean(item.children && item.children.length);
}

function isSingleRoute(item: AuthRoute.Route) {
  return Boolean(item.meta.singleLayout);
}
