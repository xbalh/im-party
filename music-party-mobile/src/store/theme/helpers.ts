import {localStg} from '@/utils';
import configs from "@/configs";

export function initThemeSettings() {
  const storageSettings = localStg.get('themeSettings');
  if (storageSettings) {
    return storageSettings;
  }

  return configs.theme;
}
