import { RouteConfig } from 'vue-router'

const routes: RouteConfig[] = [
  {
    path: "home",
    name: "Home",
    component: () =>
      import(/* webpackChunkName: "home" */ "@/views/main-page/main-module/home/index.vue"),
    meta: {
      isLogin: true,
      isCache: true
    }
  }
]
export default routes