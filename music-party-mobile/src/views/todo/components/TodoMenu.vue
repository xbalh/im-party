<template>
  <div>
    <v-btn
      block
      size="large"
      color="primary"
      class="mb-3 text-body-2"
      @click="$emit('open-compose')"
    >{{ $t('todo.addTask') }}
    </v-btn>

    <v-list nav class="mt-2 pa-0">
      <v-list-item to="/apps/todo/tasks" active-class="primary--text" link>
        <template #prepend>
          <v-icon size="small" >mdi-checkbox-marked-circle-outline</v-icon>
        </template>
        <v-list-item-title>{{ $t('todo.tasks') }}</v-list-item-title>
        <template #append>
          <v-list-item-action v-if="incompleteTasks.length > 0">
            <v-badge inline color="primary" class="font-weight-bold" :content="incompleteTasks.length">
            </v-badge>
          </v-list-item-action>
        </template>
      </v-list-item>

      <v-list-item to="/apps/todo/completed" active-class="primary--text" link>
        <template #prepend>
          <v-icon  size="small">mdi-check</v-icon>
        </template>
        <v-list-item-title>{{ $t('todo.completed') }}</v-list-item-title>
      </v-list-item>
    </v-list>

    <v-list dense nav class="mt-2 pa-0">
      <div class="overline pa-1 mt-2">{{ $t('todo.labels') }}</div>

      <v-list-item
        v-for="label in labels"
        :key="label.id"
        :to="`/apps/todo/label/${label.id}`"
        exact
        active-class="primary--text"
        link
      >
        <template #prepend>
          <v-icon  size="small" :color="label.color">mdi-label-outline</v-icon>
        </template>
        <v-list-item-title>{{ label.title }}</v-list-item-title>
      </v-list-item>
    </v-list>
  </div>
</template>

<script lang="ts" setup>

import {useTodoStore} from "@/views/todo/store";

const {labels, incompleteTasks} = storeToRefs(useTodoStore())

</script>
