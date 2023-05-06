import { RouteConfig } from 'vue-router'

const routes: RouteConfig[] = [
  {
    path: "home1",
    name: "Home1",
    component: () =>
      import(/* webpackChunkName: "home" */ "@/views/default-page/test-module/home/index.vue")
  },
  {
    path: "home2",
    name: "Home2",
    component: () =>
      import(/* webpackChunkName: "home" */ "@/views/default-page/test-module/home2/index.vue")
  }
]
export default routes