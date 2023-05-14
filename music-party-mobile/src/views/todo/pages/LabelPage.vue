<template>
  <todo-list :tasks="labelTasks" @edit-task="$emit('edit-task', $event)"/>
</template>

<script lang="ts" setup>
import {useTodoStore} from "../store";
import {useRouter, useRoute} from "vue-router";
import {computed, ref} from 'vue'
import TodoList from '../components/TodoList.vue'

const route = useRoute()

const {taskList: tasks} = useTodoStore()
const {currentRoute} = useRouter()
const labelId = ref(currentRoute.value.params['id'] as string)


watch(() => route.params['id'], (nId) => {
  labelId.value = (nId as string)
})

const labelTasks = computed(() => {
  return tasks
    .filter((t) => t.labels.indexOf(labelId.value) !== -1)
})
</script>
