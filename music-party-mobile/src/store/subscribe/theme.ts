import {effectScope, onScopeDispose, watch} from 'vue';
import {useOsTheme} from 'vooks';
import {useThemeStore} from '@/store';

export default function subscribeThemeStore() {
  const theme = useThemeStore();
  const osTheme = useOsTheme();
  const scope = effectScope();
  const {themes, global} = useTheme()

  scope.run(() => {
    // themeconfig
    watch(
      () => theme,
      (n) => {
        themes.value["dark"].colors.primary = n.primary
        themes.value["light"].colors.primary = n.primary
        global.name.value = n.globalTheme
        theme.cacheThemeSettings()
      },
      {immediate: true, deep: true}
    );

    // watch os theme
    watch(
      osTheme,
      newValue => {
        if (theme.followOs) {
          global.name.value = newValue || 'light'
          theme.cacheThemeSettings()
        }
      },
      {immediate: true}
    );
  });

  onScopeDispose(() => {
    scope.stop();
  });
}
