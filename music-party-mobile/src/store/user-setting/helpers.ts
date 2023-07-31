import {localStg} from '@/utils';
import configs from "@/configs";

export function initUserSettings() {
  const storageSettings = localStg.get('userSettings');
  if (storageSettings) {
    return storageSettings;
  }

  return configs.userConfig;
}
