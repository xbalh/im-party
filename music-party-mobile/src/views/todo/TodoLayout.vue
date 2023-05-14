<template>
  <v-layout full-height :class="{'position-static':!lgAndUp}">
    <div class="d-flex flex-grow-1 flex-row">
      <v-navigation-drawer
        v-model="drawer"
        :permanent="lgAndUp"
        floating
        :class="{'top-z-index':!lgAndUp}"
        class="elevation-1 rounded flex-shrink-0"
      >
        <todo-menu class="todo-app-menu pa-2" @open-compose="openCompose"></todo-menu>
      </v-navigation-drawer>


      <v-main>
        <div class="d-flex flex-grow-1 h-100 flex-column" :class="{'pl-3':lgAndUp}">
            <v-toolbar class="hidden-lg-and-up flex-grow-0 mb-2" elevation="1" rounded color="surface">
              <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
              <div class="title font-weight-bold">Todo App</div>
            </v-toolbar>
          <router-view class="flex-grow-1"/>
        </div>
      </v-main>

      <todo-compose ref="compose"/>
    </div>
  </v-layout>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import TodoCompose from '@/views/todo/components/TodoCompose.vue'
import TodoMenu from './components/TodoMenu.vue'
import {useDisplay} from "vuetify";

const {lgAndUp} = useDisplay()

const drawer = ref()
const compose = ref<InstanceType<typeof TodoCompose> | null>()
const openCompose = (task: Todo.Task) => {
  compose.value?.open(task)
}
</script>
<style>

</style>
