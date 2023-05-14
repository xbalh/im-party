import { useEventListener } from '@vueuse/core';
import {useThemeStore} from "@/store";


export function useGlobalEvents() {
  const appConifg= useThemeStore();

  useEventListener(window, 'beforeunload', () => {
    appConifg.cacheThemeSettings();
  });
}
