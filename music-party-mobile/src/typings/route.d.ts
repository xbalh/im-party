declare namespace AuthRoute {
  type RootRoutePath = '/';

  type NotFoundRoutePath = '/:pathMatch(.*)*';

  type RootRouteKey = PageRoute.RootRouteKey;

  type NotFoundRouteKey = PageRoute.NotFoundRouteKey;

  type RouteKey = PageRoute.RouteKey;

  type LastDegreeRouteKey = PageRoute.LastDegreeRouteKey;

  type AllRouteKey = RouteKey | RootRouteKey | NotFoundRouteKey;

  type RoutePath<K extends AllRouteKey = AllRouteKey> = AuthRouteUtils.GetRoutePath<K>;

  type RouteComponentType = 'basic' | 'blank' | 'self' | 'auth' | 'error' | 'todo' | 'chat';

  interface RouteMeta<K extends AuthRoute.RoutePath> {
    title: string;
    dynamicPath?: AuthRouteUtils.GetDynamicPath<K>;
    singleLayout?: Extract<RouteComponentType, 'basic' | 'blank' | 'auth' | 'error' | 'todo' | 'chat'>;
    requiresAuth?: boolean;
    permissions?: Auth.RoleType[];
    keepAlive?: boolean;
    icon?: string;
    hide?: boolean;
    href?: string;
    multiTab?: boolean;
    order?: number;
    activeMenu?: RouteKey;
    multi?: boolean;
    affix?: boolean;
  }

  type Route<K extends AllRouteKey = AllRouteKey> = K extends AllRouteKey
    ? {
    name: K;
    path: AuthRouteUtils.GetRoutePath<K>;
    redirect?: AuthRouteUtils.GetRoutePath;
    /**
     * 路由组件
     * - basic: 基础布局，具有公共部分的布局
     * - blank: 空白布局
     * - multi: 多级路由布局(三级路由或三级以上时，除第一级路由和最后一级路由，其余的采用该布局)
     * - self: 作为子路由，使用自身的布局(作为最后一级路由，没有子路由)
     */
    component?: RouteComponentType;
    children?: Route[];
    meta: RouteMeta<AuthRouteUtils.GetRoutePath<K>>;
  } & Omit<import('vue-router').RouteRecordRaw, 'name' | 'path' | 'redirect' | 'component' | 'children' | 'meta'>
    : never;

  type RouteModule = Record<string, { default: Route }>;
}

declare namespace AuthRouteUtils {
  type RouteKeySplitMark = '_';

  type RoutePathSplitMark = '/';

  type BlankString = '';

  /** key转换成path */
  type KeyToPath<K extends string> = K extends `${infer _Left}${RouteKeySplitMark}${RouteKeySplitMark}${infer _Right}`
    ? never
    : K extends `${infer Left}${RouteKeySplitMark}${infer Right}`
      ? Left extends BlankString
        ? never
        : Right extends BlankString
          ? never
          : KeyToPath<`${Left}${RoutePathSplitMark}${Right}`>
      : `${RoutePathSplitMark}${K}`;

  /** 根据路由key获取路由路径 */
  type GetRoutePath<K extends AuthRoute.AllRouteKey = AuthRoute.AllRouteKey> = K extends AuthRoute.AllRouteKey
    ? K extends AuthRoute.RootRouteKey
      ? AuthRoute.RootRoutePath
      : K extends AuthRoute.NotFoundRouteKey
        ? AuthRoute.NotFoundRoutePath
        : KeyToPath<K>
    : never;

  /** 获取一级路由(有子路由的一级路由和没有子路由的路由) */
  type GetFirstDegreeRouteKey<K extends AuthRoute.RouteKey = AuthRoute.RouteKey> =
    K extends `${infer _Left}${RouteKeySplitMark}${infer _Right}` ? never : K;

  /** 获取有子路由的一级路由 */
  type GetFirstDegreeRouteKeyWithChildren<K extends AuthRoute.RouteKey = AuthRoute.RouteKey> =
    K extends `${infer Left}${RouteKeySplitMark}${infer _Right}` ? Left : never;

  /** 单级路由的key (单级路由需要添加一个父级路由用于应用布局组件) */
  type SingleRouteKey = Exclude<GetFirstDegreeRouteKey,
    GetFirstDegreeRouteKeyWithChildren | AuthRoute.RootRouteKey | AuthRoute.NotFoundRouteKey>;

  /** 单独路由父级路由key */
  type SingleRouteParentKey = `${SingleRouteKey}-parent`;

  /** 单独路由父级路由path */
  type SingleRouteParentPath = KeyToPath<SingleRouteParentKey>;

  /** 获取路由动态路径 */
  type GetDynamicPath<P extends AuthRoute.RoutePath> =
    | `${P}/:${string}`
    | `${P}/:${string}(${string})`
    | `${P}/:${string}(${string})?`;
}
