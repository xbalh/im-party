<template>
  <span>
    <span v-if="value === 0">
      {{ value }}%
    </span>
    <span v-else-if="value > 0" class="success--text">
      <v-icon small color="success">mdi-arrow-top-right</v-icon> {{ value }}%
    </span>
    <span v-else class="error--text">
      <v-icon small color="error">mdi-arrow-bottom-right</v-icon> {{ Math.abs(value) }}%
    </span>
  </span>
</template>

<script setup lang="ts">
defineProps({
  value: {
    type: Number,
    default: 0
  }
})
</script>
