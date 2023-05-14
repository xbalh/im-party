export function filterAuthRoutesByUserPermission(routes: AuthRoute.Route[], permission: Auth.RoleType) {
  return routes.map(route => filterAuthRouteByUserPermission(route, permission)).flat(1);
}

function filterAuthRouteByUserPermission(route: AuthRoute.Route, permission: Auth.RoleType): AuthRoute.Route[] {
  const filterRoute = { ...route };
  const hasPermission =
    !route.meta.permissions || permission === 'admin' || route.meta.permissions.includes(permission);

  if (filterRoute.children) {
    const filterChildren = filterRoute.children.map(item => filterAuthRouteByUserPermission(item, permission)).flat(1);
    Object.assign(filterRoute, { children: filterChildren });
  }
  return hasPermission ? [filterRoute] : [];
}
