const themeConfig: ThemeConfig.Config = {

  primary: '#55BB46',

  followOs: true,

  globalTheme: 'light', // light | dark

  menuTheme: 'global', // global | light | dark

  toolbarTheme: 'global', // global | light | dark

  isToolbarDetached: false,

  isContentBoxed: false,

  isRTL: false,

  dark: {
    dark: true,
    colors: {
      background: '#444444',
      surface: '#25272a',
      primary: '#55BB46',
      secondary: '#829099',
      accent: '#82B1FF',
      error: '#FF5252',
      info: '#2196F3',
      success: '#4CAF50',
      warning: '#FFC107',
    }
  },

  // light theme colors
  light: {
    dark: false,
    variables: {
      "high-emphasis-opacity": 1,
      "border-opacity": 0.05,
    },
    colors: {
      background: '#f2f5f8',
      surface: '#ffffff',
      primary: '#55BB46',
      secondary: '#a0b9c8',
      accent: '#048ba8',
      error: '#ef476f',
      info: '#2196F3',
      success: '#06d6a0',
      "on-success": '#ffffff',
      warning: '#ffd166',
    }
  },
}

export default themeConfig

