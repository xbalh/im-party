const dashboard: AuthRoute.Route = {
  name: 'dashboard',
  path: '/dashboard',
  component: 'basic',
  children: [
    {
      name: 'dashboard_analytics',
      path: '/dashboard/analytics',
      component: 'self',
      meta: {
        icon: 'mdi-view-dashboard-outline',
        title: 'menu.dashboard',
        requiresAuth: true
      }
    }
  ],
  meta: {
    title: 'menu.dashboard',
    icon: 'mdi-view-dashboard-outline',
    order: 1,
    requiresAuth: true
  }
};

export default dashboard;
