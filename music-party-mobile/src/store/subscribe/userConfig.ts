import { effectScope, onScopeDispose, watch } from 'vue';
import { useUserSettingStore } from '@/store';

export default function subscribeUserConfigStore() {
  const userConfig = useUserSettingStore();
  const scope = effectScope();

  scope.run(() => {
    // themeconfig
    watch(
      () => userConfig,
      (n) => {
        userConfig.cacheUserSettings()
      },
      { immediate: true, deep: true }
    );

    // // watch os theme
    // watch(
    //   osTheme,
    //   newValue => {
    //     userConfig.cacheUserSettings()
    //   },
    //   { immediate: true }
    // );
  });

  onScopeDispose(() => {
    scope.stop();
  });
}
