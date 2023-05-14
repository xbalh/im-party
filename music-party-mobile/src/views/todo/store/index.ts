import {defineStore} from "pinia";
import {taskList, labels} from './content'

interface TodoState {
  taskList: Array<Todo.Task>
  labels: Array<Todo.Label>
}

export const useTodoStore = defineStore('todo-store', {
  state: (): TodoState => ({
    taskList,
    labels
  }),
  getters:{
    incompleteTasks({taskList}) {
      return taskList.filter((t) => !t.completed)
    },
    completeTasks({taskList}) {
      return taskList.filter((t) => t.completed)
    }
  },
  actions: {
    addTask(task: Todo.Task) {
      this.taskList.push({
        ...task,
        completed: false,
        id: '_' + Math.random().toString(36).substr(2, 9),
      })
    },
    updateTask(task: Todo.Task) {
      const taskIdx = this.taskList.find((t) => t.id === task.id)
      if (taskIdx)
        Object.assign(taskIdx, task)
    },
    taskCompleted(task: Todo.Task) {
      const taskIdx = this.taskList.findIndex((t) => t.id === task.id)
      this.taskList[taskIdx].completed = true
    },
    taskIncomplete(task: Todo.Task) {
      const taskIdx = this.taskList.findIndex((t) => t.id === task.id)
      this.taskList[taskIdx].completed = false
    },
    deleteTask(task: Todo.Task) {
      const taskIdx = this.taskList.findIndex((t) => t.id === task.id)

      if (taskIdx !== -1) this.taskList.splice(taskIdx, 1)
    }
  }
})
