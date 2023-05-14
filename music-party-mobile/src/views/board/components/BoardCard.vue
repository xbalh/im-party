<template>
  <v-card @click="$emit('edit')">
    <div class="font-weight-bold">{{ card.title }}</div>
    <div>{{ card.description }}</div>
    <v-menu offset-y left>
      <template v-slot:activator="{ props }">
        <v-btn icon variant="text" size="small"  class="board-item-menu" v-bind="props">
          <v-icon color="grey darken-2">mdi-dots-vertical</v-icon>
        </v-btn>
      </template>
      <v-list>
        <v-list-item @click="$emit('edit')">
          <v-list-item-title>{{ $t('board.editCard') }}</v-list-item-title>
        </v-list-item>
        <v-list-item @click="$emit('delete')">
          <v-list-item-title>{{ $t('board.deleteCard') }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-card>
</template>

<script setup lang="ts">
import {PropType} from "vue";
import type {card} from '../types'

defineProps({
  card: {
    type: Object as PropType<card>,
    default: () => ({})
  }
})
</script>

<style scoped>
.board-item-menu {
  position: absolute;
  top: 10px;
  right: 2px;
}

.v-application--is-rtl .board-item-menu {
  left: 2px;
  right: auto;
}
</style>
