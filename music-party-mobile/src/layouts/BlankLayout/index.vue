<template>
  <router-view v-slot="{ Component }">
    <v-fade-transition mode="out-in">
      <component :is="Component"/>
    </v-fade-transition>
  </router-view>
</template>

<script setup lang="ts">
</script>

<style scoped></style>
