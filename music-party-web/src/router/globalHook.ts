import VueRouter from 'vue-router'
import store from '@/store'
import ProgressBar from "@/utils/progressBar"

const progressBar = new ProgressBar()

export default function gloablHook(router: VueRouter) {
  router
    .beforeEach((to, from, next) => {
      const { fullPath, meta: { isLogin = false } = {} } = to
      const token = store.getters['userStore/getToken']

      progressBar.start()
      if (token && to.path === '/login') {
        return next('/')
      }

      //如果没有token，去别的页面要自动跳转到登录页面
      if (to.path !== '/login' && !token) {
        if(from.path == '/login') return
        return router.replace({
          name: 'Login',
          query: {
            redirectUrl: fullPath
          }
        })
      }

      //如果没有token才需要登录
      if (isLogin && !token) {
        return router.replace({
          name: 'Login',
          query: {
            redirectUrl: fullPath
          }
        })
      }
      next()
    })

  
  router.afterEach(() => {
    progressBar.close()
  })
}