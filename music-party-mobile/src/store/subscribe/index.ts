import subscribeThemeStore from './theme';
import subscribeUserConfigStore from './userConfig';

export function subscribeStore() {
  subscribeThemeStore();
  subscribeUserConfigStore();
}
