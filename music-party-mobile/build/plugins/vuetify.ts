import vuetify from 'vite-plugin-vuetify'

export default function vuetifyPlugin() {
  return vuetify({
    autoImport: true,
    styles: {
      configFile: 'src/assets/scss/settings.scss'
    }
  })

}
