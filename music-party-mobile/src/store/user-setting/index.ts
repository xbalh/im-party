import {defineStore} from 'pinia'
import {localStg} from "@/utils";
import {initUserSettings} from "@/store/user-setting/helpers";

export const useUserSettingStore = defineStore("userSetting", {
  state: () => {
    return {
      ...initUserSettings(),
    }
  },
  actions: {
    cacheUserSettings() {
      localStg.set('userSettings', this.$state);
    },
  }
})
