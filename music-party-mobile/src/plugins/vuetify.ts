/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import {createVuetify} from 'vuetify'
import configs from "@/configs";
import i18n from "@/plugins/vue-i18n";

export default createVuetify({
  defaults: {
    VTextField: {
      variant: 'underlined',
      'clearIcon': 'mdi-close'
    },
    VAutocomplete: {
      'clearIcon': 'mdi-close',
      'noDataText': i18n.global.t('$vuetify.dataIterator.noResultsText')

    },
    VSelect: {
      'clearIcon': 'mdi-close'
    },
    VBtn: {
      variant: 'elevated',
    },
  },
  theme: {
    themes: {
      light: configs.theme.light,
      dark: configs.theme.dark,
    },
    variations: {
      colors: ["primary"],
      lighten: 5,
      darken: 5
    },
  },
})
