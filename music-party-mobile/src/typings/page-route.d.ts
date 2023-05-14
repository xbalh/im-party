declare namespace PageRoute {
  /**
   * the root route key
   * @translate 根路由
   */
  type RootRouteKey = 'root';

  /**
   * the not found route, which catch the invalid route path
   * @translate 未找到路由(捕获无效路径的路由)
   */
  type NotFoundRouteKey = 'not-found';

  /**
   * the route key
   * @translate 页面路由
   */
  type RouteKey =
    | '403'
    | '404'
    | '500'
    | 'login'
    | 'not-found'
    | 'dashboard'
    | 'dashboard_analytics'
    | 'apps'
    | 'apps_manager-user'
    | 'apps_manager-user_list'
    | 'apps_manager-user_edit'
    | 'apps_board'
    | 'apps_todo'
    | 'apps_todo_tasks'
    | 'apps_todo_completed'
    | 'apps_todo_label'
    | 'apps_chat'
    | 'apps_chat-channel'
    | 'pages'
    | 'pages_error'
    | 'pages_error_notfound'
    | 'pages_error_unexpected'
    | 'other'
    | 'blank-page'
    | 'other_menu-levels'
    | 'other_menu-levels-2-1'
    | 'other_menu-levels-2-2'
    | 'other_menu-levels-3-1'
    | 'other_menu-levels-3-2'
    | 'flowable'
    | 'flowable_design'
    | 'form'
    | 'form_list'
    | 'form_design'
    ;
  /**
   * last degree route key, which has the page file
   * @translate 最后一级路由(该级路有对应的页面文件)
   */
  type LastDegreeRouteKey = Extract<RouteKey,
    | '403'
    | '404'
    | '500'
    | 'login'
    | 'not-found'
    | 'dashboard_analytics'
    | 'apps_manager-user_list'
    | 'apps_manager-user_edit'
    | 'apps_board'
    | 'apps_todo_tasks'
    | 'apps_todo_completed'
    | 'apps_todo_label'
    | 'apps_chat-channel'
    | 'pages_error_notfound'
    | 'pages_error_unexpected'
    | 'other_menu-levels-2-1'
    | 'other_menu-levels-3-1'
    | 'other_menu-levels-3-2'
    | 'flowable_design'
    | 'form_list'
    | 'form_design'
    >;
}
