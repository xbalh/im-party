<template>
  <v-app>
    <vuetify-provider>
      <router-view/>
    </vuetify-provider>
  </v-app>
</template>

<script setup lang="ts">
import {useGlobalEvents} from "@/composables/events";
import {subscribeStore} from '@/store';


subscribeStore()
useGlobalEvents();
</script>
