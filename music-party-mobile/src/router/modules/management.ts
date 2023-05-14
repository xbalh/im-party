const management: AuthRoute.Route = {
    name: 'apps',
    path: '/apps',
    component: 'basic',
    children: [
      {
        name: 'apps_manager-user',
        path: '/apps/manager-user',
        component: 'blank',
        children: [
          {
            name: 'apps_manager-user_list',
            path: '/apps/manager-user/list',
            component: 'self',
            meta: {
              title: 'menu.usersList',
              requiresAuth: true
            }
          },
          {
            name: 'apps_manager-user_edit',
            path: '/apps/manager-user/edit',
            component: 'self',
            meta: {
              title: 'menu.usersEdit',
              dynamicPath: '/apps/manager-user/edit/:id?',
              requiresAuth: true
            }
          }
        ],
        meta: {
          title: 'menu.users',
          icon: 'mdi-account-multiple-outline',
          requiresAuth: true,
          order: 2,
        }
      },
      {
        name: 'apps_board',
        path: '/apps/board',
        component: 'self',
        meta: {
          title: 'menu.board',
          icon: 'mdi-view-column-outline',
          order: 1,
          requiresAuth: true,
        }
      },
      {
        name: 'apps_todo',
        path: '/apps/todo',
        component: 'todo',
        children: [
          {
            name: 'apps_todo_tasks',
            path: '/apps/todo/tasks',
            component: 'self',
            meta: {
              title: 'todo.tasks',
              requiresAuth: true,
              hide: true,
            }

          },
          {
            name: 'apps_todo_completed',
            path: '/apps/todo/completed',
            component: 'self',
            meta: {
              title: 'todo.completed',
              requiresAuth: true,
              hide: true,
            }
          },
          {
            name: 'apps_todo_label',
            path: '/apps/todo/label',
            component: 'self',
            meta: {
              title: 'todo.labels',
              hide: true,
              requiresAuth: true,
              dynamicPath: '/apps/todo/label/:id'
            }

          }
        ],
        meta: {
          title: 'menu.todo',
          icon: 'mdi-format-list-checkbox',
          requiresAuth: true,
          order: 1
        }
      }
    ],
    meta: {
      title: 'menu.apps',
      requiresAuth: true,
    }
  }
;

export default management;
