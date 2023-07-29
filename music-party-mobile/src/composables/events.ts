import { useEventListener } from '@vueuse/core';
import {useThemeStore, useUserSettingStore} from "@/store";


export function useGlobalEvents() {
  const appConfig= useThemeStore();
  const userConfig= useUserSettingStore();

  useEventListener(window, 'beforeunload', () => {
    appConfig.cacheThemeSettings();
    userConfig.cacheUserSettings();
  });
}
