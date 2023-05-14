/**
 * 获取面包屑数据
 * @param activeKey - 当前页面路由的key
 * @param menus - 菜单数据
 * @param rootPath - 根路由路径
 */
export function getBreadcrumbByRouteKey(activeKey: string, menus: App.GlobalMenuOption[], rootPath: string) {
  const breadcrumbMenu = getBreadcrumbMenu((menu) => activeKey.includes(menu.routeName), menus);
  const breadcrumb = breadcrumbMenu.map(item => transformBreadcrumbMenuToBreadcrumb(item, rootPath));
  return breadcrumb;
}

export function getBreadcrumbsByPredicate(predicate: (menu: App.GlobalMenuOption) => boolean, menus: App.GlobalMenuOption[], rootPath: string) {
  const breadcrumbMenu = getBreadcrumbMenu(predicate, menus);
  const breadcrumb = breadcrumbMenu.map(item => transformBreadcrumbMenuToBreadcrumb(item, rootPath));
  return breadcrumb;
}

/**
 * 根据菜单数据获取面包屑格式的菜单
 * @param predicate
 * @param menus - 菜单数据
 */
function getBreadcrumbMenu(predicate: (menu: App.GlobalMenuOption) => boolean, menus: App.GlobalMenuOption[]) {
  const breadcrumbMenu: App.GlobalMenuOption[] = [];
  menus.some(menu => {
    const flag = predicate(menu);
    if (flag) {
      breadcrumbMenu.push(...getBreadcrumbMenuItem(predicate, menu));
    }
    return flag;
  });
  return breadcrumbMenu;
}

/**
 * 根据单个菜单数据获取面包屑格式的菜单
 * @param predicate
 * @param menu - 单个菜单数据
 */
function getBreadcrumbMenuItem(predicate: (menu: App.GlobalMenuOption) => boolean, menu: App.GlobalMenuOption) {
  const breadcrumbMenu: App.GlobalMenuOption[] = [];
  if (predicate(menu)) {
    breadcrumbMenu.push(menu);
  }
  if (predicate(menu) && menu.children && menu.children.length) {
    breadcrumbMenu.push(
      ...menu.children.map(item => getBreadcrumbMenuItem(predicate, item as App.GlobalMenuOption)).flat(1)
    );
  }

  return breadcrumbMenu;
}

/**
 * 将面包屑格式的菜单数据转换成面包屑数据
 * @param menu - 单个菜单数据
 * @param rootPath - 根路由路径
 */
function transformBreadcrumbMenuToBreadcrumb(menu: App.GlobalMenuOption, rootPath: string) {
  const breadcrumb: App.GlobalBreadcrumb = {
    key: menu.routeName,
    title: menu.label,
    to: menu.routePath,
    disabled: menu.routePath === rootPath,
  };
  return breadcrumb;
}
