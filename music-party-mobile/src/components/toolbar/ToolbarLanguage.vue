<template>
  <v-menu
  >
    <template v-slot:activator="{ props }">
      <v-btn text v-bind="props">
        <flag-icon :round="smAndDown" :flag="currentLocale.flag"></flag-icon>
        <span v-show="mdAndUp&& showLabel"
              class="ml-1"
        > {{ currentLocale.label }}</span>
        <v-icon v-if="showArrow" right>mdi-chevron-down</v-icon>
      </v-btn>
    </template>

    <v-list nav density="comfortable">
      <v-list-item v-for="locale in availableLocales" :key="locale.code" @click="setLocale(locale.code)">
        <v-list-item-title class="d-flex justify-center align-center">
          <flag-icon class="mr-1" :flag="locale.flag"></flag-icon>
          {{ locale.label }}
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script setup lang="ts">
import FlagIcon from '../common/FlagIcon.vue'
import {computed} from "vue";
import {useI18n} from 'vue-i18n'
import configs from "@/configs";
import {useDisplay} from 'vuetify'

defineProps({
  showArrow: {
    type: Boolean,
    default: false
  },
  // Show the country label
  showLabel: {
    type: Boolean,
    default: true
  }
})

const {smAndDown, mdAndUp} = useDisplay()

const {locale} = useI18n()
const {availableLocales: locales} = configs.locales

const currentLocale = computed(() => {
  return locales.find((i: any) => i.code === locale.value)
})
const availableLocales = computed(() => {
  return locales.filter((i: any) => i.code !== locale.value)
})

const setLocale = (use: string) => {
  locale.value = use
}
</script>
