<template>
  <v-card min-height="380px">
    <v-text-field
      v-model="filter"
      class="todo-filter px-1 py-2 v-field-flat"
      placeholder="Filter tasks"
      prepend-inner-icon="mdi-magnify"
      variant="solo"
      clearable
      color="red"
      hide-details
    ></v-text-field>
    <v-divider></v-divider>

    <div v-if="tasks.length === 0">
      <div class="px-1 py-6 text-center">All done! No more tasks!</div>
    </div>


    <v-slide-y-reverse-transition
      v-else
      group
      tag="div"
    >
      <div v-for="(task,index) in visibleTasks" :key="index" class="d-flex pa-2 task-item align-center"
           @click="$emit('edit-task', task)">
        <v-checkbox-btn
          :input-value="task.completed"
          hide-details
          false-icon="mdi-checkbox-blank-circle-outline"
          true-icon="mdi-checkbox-marked-circle"
        >
        </v-checkbox-btn>

        <div class="task-item-content flex-grow-1" :class="{ 'complete': task.completed }">
          <div class="font-weight-bold" v-text="task.title"></div>
          <div v-text="task.description"></div>
          <div>
            <v-chip
              v-for="label in task.labels"
              :key="label"
              :color="getLabelColor(label)"
              class="font-weight-bold mt-1 mr-1"
              size="x-small"

            >
              {{ getLabelTitle(label) }}
            </v-chip>
          </div>
        </div>
        <div class="d-flex align-center">
          <v-btn icon flat>
            <v-icon>mdi-delete-outline</v-icon>
          </v-btn>
        </div>
      </div>
    </v-slide-y-reverse-transition>
  </v-card>
</template>

<script lang="ts" setup>
import {PropType} from "vue";
import {useTodoStore} from "@/views/todo/store";

const {labels} = useTodoStore()

const props = defineProps({
  // task list
  tasks: {
    type: Array as PropType<Array<Todo.Task>>,
    default: () => []
  }
})
const filter = ref("")

const visibleTasks = computed(() => {
  if (!filter.value) {
    return props.tasks
  }
  return props.tasks?.filter(i => i.title.includes(filter.value))
})

const getLabelColor = (id: string) => {
  const label = labels.find((l) => l.id === id)
  return label ? label.color : ''
}
const getLabelTitle = (id: string) => {
  const label = labels.find((l) => l.id === id)
  return label ? label.title : ''
}
</script>

<style lang="scss" scoped>
.todo-filter {
  position: sticky;
  background-color: var(--v-background-base) !important;
  z-index: 2;
  top: 0;
}


.task-item {
  cursor: pointer;
  border-bottom: 1px solid rgba(100, 100, 100, 0.1);

  &:hover {
    background-color: rgba(100, 100, 100, 0.1);
  }

  .task-item-content {
    &.complete {
      text-decoration: line-through;
    }
  }
}
</style>
