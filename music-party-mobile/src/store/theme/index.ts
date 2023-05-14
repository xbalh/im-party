import {defineStore} from 'pinia'
import {localStg} from "@/utils";
import {initThemeSettings} from "@/store/theme/helpers";

export const useThemeStore = defineStore("theme", {
  state: () => {
    return {
      ...initThemeSettings(),
    }
  },
  actions: {
    cacheThemeSettings() {
      localStg.set('themeSettings', this.$state);
    },
  }
})
