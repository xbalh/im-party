import {App} from "vue";

export function registerFilters(app: App) {
  app.config.globalProperties.$filters = {
    formatCurrency: formatCurrency
  }
}
