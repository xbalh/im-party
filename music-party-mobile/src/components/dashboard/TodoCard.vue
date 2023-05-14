<template>
  <tasks-page class="todo-card"></tasks-page>
</template>

<script lang="ts" setup>
import TasksPage from '@/views/todo/pages/TasksPage.vue'
</script>
<style lang="scss" scoped>
.todo-card {
  height: 100%;
  max-height: 360px;
  overflow-y: scroll;
}
</style>
