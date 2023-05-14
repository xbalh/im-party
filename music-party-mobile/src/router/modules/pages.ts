const pages: AuthRoute.Route = {
  name: 'pages',
  path: '/pages',
  component: 'error',
  children: [
    {
      name: 'pages_error',
      path: '/pages/error',
      meta: {
        title: 'menu.errorPages',
        icon: 'mdi-file-cancel-outline',
        order: 1,
        requiresAuth: true
      },
      children: [
        {
          name: 'pages_error_notfound',
          path: '/pages/error/notfound',
          component: 'self',
          meta: {
            icon: 'mdi-file-outline',
            title: 'menu.errorNotFound',
          },
        },
        {
          name: 'pages_error_unexpected',
          path: '/pages/error/unexpected',
          component: 'self',
          meta: {
            icon: 'mdi-file-outline',
            title: 'menu.errorUnexpected',
          },
        }
      ]
    },
  ],
  meta: {
    title: 'menu.pages',
    order: 1,
  }
};

export default pages;
