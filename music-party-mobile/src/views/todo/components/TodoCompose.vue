<template>
  <v-dialog v-model="dialog" width="600">
    <v-card>
      <v-card-title class="pa-2">
        {{ isEdit ? 'Edit Task' : 'Add Task' }}
        <v-spacer></v-spacer>
        <v-btn icon  variant="plain"  @click="dialog = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <!-- task form -->
      <div>
        <v-text-field
          v-model="title"
          class="px-2 py-1 v-field-flat"
          variant="solo"
          :placeholder="$t('common.title')"
          autofocus
          hide-details
          @keyup.enter="save"
        ></v-text-field>

        <v-divider></v-divider>

        <v-textarea
          v-model="description"
          class="px-2 py-1 v-field-flat"
          variant="solo"
          :placeholder="$t('common.description')"
          hide-details
        ></v-textarea>

        <v-select
          v-model="taskLabels"
          class="px-2 my-3 v-field-flat"
          :items="labels"
          placeholder="Labels"
          item-value="id"
          chips
          multiple
          clearable
          variant="solo"
        >
          <template v-slot:chip="{ item:{raw} }">
            <v-chip
              :color="raw.color"
              label
              variant="outlined"
            >
              {{ raw.title }}
            </v-chip>
          </template>
        </v-select>
      </div>

      <v-divider></v-divider>

      <v-card-actions class="pa-2">
        <v-btn outlined @click="close">{{ $t('common.cancel') }}</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="save">{{ $t('common.save') }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useTodoStore} from "@/views/todo/store";

const dialog = ref(false)
const title = ref('')
const description = ref('')
const taskRef = ref<Todo.Task>()
const taskLabels = ref<Array<string>>([])

const isEdit = computed(() => {
  return taskRef.value && taskRef.value?.id
})
const open = (task: Todo.Task) => {
  if (task) {
    taskRef.value = task
    title.value = taskRef.value?.title
    description.value = taskRef.value?.description
    taskLabels.value = taskRef.value?.labels
  } else {
    title.value = ''
    description.value = ''
    taskLabels.value = []
  }
  dialog.value = true
}
defineExpose({
  open,
})
const close = () => {
  dialog.value = false
}
const save = () => {
  const t: Todo.Task = {
    title: title.value,
    description: description.value,
    labels: taskLabels.value,
    id: '',
    completed: false
  }
  if (taskRef.value?.id) {
    updateTask({
      ...taskRef.value,
      ...t,
    })
  } else {
    addTask(t)
  }

  close()
}

const {updateTask, addTask, labels} = useTodoStore()

</script>
