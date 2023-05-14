/**
 * plugins/user.d.ts
 *
 * Automatically included in `./src/main.ts`
 */

// Plugins
import VueApexCharts from 'vue3-apexcharts'
import 'virtual:svg-icons-register';

// Types
import type {App} from 'vue'

export function registerPlugins(app: App) {
  app.use(vuetify)
  app.use(vueI18n)
  app.component('apexchart', VueApexCharts)


}
