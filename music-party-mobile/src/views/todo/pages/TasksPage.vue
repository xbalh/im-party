<template>
  <todo-list :tasks="incompleteTasks" @edit-task="$emit('edit-task', $event)"/>
</template>

<script setup lang="ts">
import {useTodoStore} from "@/views/todo/store";
import TodoList from '@/views/todo/components/TodoList.vue'

const {incompleteTasks} = storeToRefs(useTodoStore())
</script>
