import { RouteConfig } from 'vue-router'

const routes: RouteConfig[] = [
  {
    path: "chaos",
    name: "chaos",
    component: () =>
      import(/* webpackChunkName: "home" */ "@/views/game/game-module/geometrychaos/index.vue"),
    meta: {
      isLogin: true,
      isCache: true
    }
  }
]
export default routes