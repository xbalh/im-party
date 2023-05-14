import {createPinia} from 'pinia'
import type {App} from "vue";

export function setupStore(app: App) {
  const store = createPinia()
  app.use(store)
}

export * from './theme'
export * from './route'
export * from './auth'
export * from './subscribe'
export * from './flow'
