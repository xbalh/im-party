<template>
  <div>
    <v-btn
      theme="dark"
      ref="refButton"
      class="drawer-button"
      color="#ee44aa"
      @click="right = true"
    >
      <v-icon class="fa-spin">mdi-cog-outline</v-icon>
    </v-btn>

    <v-navigation-drawer
      v-model="right"
      location="right"
      floating
      temporary
      order="-10"
      width="310"
    >
      <div class="d-flex align-center pa-2">
        <div class="title">Settings</div>
        <v-spacer></v-spacer>
        <v-btn flat icon @click="right = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </div>

      <v-divider></v-divider>

      <div class="pa-2">
        <div class="font-weight-bold my-1">Follow Os Theme</div>
        <v-switch v-model="themeConfig.followOs" color="primary"></v-switch>
        <div class="font-weight-bold my-1">Global Theme</div>
        <v-btn-toggle v-model="themeConfig.globalTheme" color="primary" mandatory variant="outlined" class="mb-2">
          <v-btn value="light">Light</v-btn>
          <v-btn value="dark">Dark</v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Toolbar Theme</div>
        <v-btn-toggle v-model="themeConfig.toolbarTheme" color="primary" mandatory variant="outlined" class="mb-2">
          <v-btn value="global">Global</v-btn>
          <v-btn value="light">Light</v-btn>
          <v-btn value="dark">Dark</v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Toolbar Style</div>
        <v-btn-toggle v-model="themeConfig.isToolbarDetached" color="primary" mandatory variant="outlined" class="mb-2">
          <v-btn :value="false">Full</v-btn>
          <v-btn :value="true">Solo</v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Content Layout</div>
        <v-btn-toggle v-model="themeConfig.isContentBoxed" color="primary" mandatory variant="outlined" class="mb-2">
          <v-btn :value="false">Fluid</v-btn>
          <v-btn :value="true">Boxed</v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Menu Theme</div>
        <v-btn-toggle v-model="themeConfig.menuTheme" color="primary" mandatory variant="outlined" class="mb-2">
          <v-btn value="global">Global</v-btn>
          <v-btn value="light">Light</v-btn>
          <v-btn value="dark">Dark</v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Primary Color</div>

        <v-color-picker v-model="themeConfig.primary" mode="hexa" :swatches="swatches" show-swatches></v-color-picker>
      </div>

      <v-divider></v-divider>
    </v-navigation-drawer>
  </div>
</template>

<script setup lang="ts">
import {ComponentPublicInstance} from "vue";
import {useThemeStore} from "@/store";

const themeConfig = useThemeStore()
const right = ref(false)
let timeout: NodeJS.Timeout
const swatches = reactive([['#0096c7', '#31944f'],
  ['#EE4f12', '#46BBB1'],
  ['#ee44aa', '#55BB46']])
const refButton = ref<ComponentPublicInstance>()
const execAnimate = () => {
  if (timeout) {
    clearTimeout(timeout)
  }
  const time = (Math.floor(Math.random() * 10 + 1) + 10) * 1000
  timeout = setTimeout(() => {
    if (refButton.value) {
      animate(refButton.value.$el, 'bounce')
    }
    execAnimate()
  }, time)
}

onMounted(() => {
  execAnimate()
})
onBeforeUnmount(() => {
  if (timeout) clearTimeout(timeout)
})

</script>

<style lang="scss" scoped>
.drawer-button {
  position: fixed;
  top: 340px;
  right: -20px;
  z-index: 9999;
  box-shadow: 1px 1px 18px #ee44aa;

  .v-icon {
    margin-left: -18px;
    font-size: 1.3rem;
  }

  .fa-spin {
    animation: spin 2s infinite linear;
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
}
</style>
