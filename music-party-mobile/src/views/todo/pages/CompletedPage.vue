<template>
  <todo-list :tasks="completeTasks" @edit-task="$emit('edit-task', $event)"/>
</template>

<script lang="ts" setup>
import {useTodoStore} from "@/views/todo/store";
import TodoList from '../components/TodoList.vue'
const {completeTasks} = storeToRefs(useTodoStore())

</script>
