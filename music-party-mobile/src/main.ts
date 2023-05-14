/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from './App.vue'
// Composables
import "@/assets/scss/theme.scss"
import "animate.css/animate.min.css"

async function setupApp() {
  const app = createApp(App)
  setupStore(app)
  await setupRouter(app)

  registerFilters(app)
  registerPlugins(app)
  app.mount('#app')
}

setupApp()
