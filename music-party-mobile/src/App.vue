<template>
  <v-app style="height: 100%;">
    <vuetify-provider>
      <router-view />
      <right-circle-button />
      <music-player/>
    </vuetify-provider>
  </v-app>
</template>

<script setup lang="ts">
import { useGlobalEvents } from "@/composables/events";
import { subscribeStore } from '@/store';
import MusicPlayer from "./views/music/MusicPlayer.vue";


subscribeStore()
useGlobalEvents();
</script>
