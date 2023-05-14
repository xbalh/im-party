<template>
  <v-card
    class="properties-panel position-relative"
    width="280"
  >
    <div class="position-absolute"
         style="left: 0;right: 0;top: 0;bottom: 0"
    >
      <v-card-title>{{ panelTitle }}</v-card-title>
      <v-expansion-panels
        variant="accordion"
        multiple>

        <component v-for="(c) in renderComponents" :is="c"></component>

      </v-expansion-panels>
    </div>
  </v-card>
</template>

<script lang="ts" setup>


import {markRaw, ref} from "vue";
import type {Component} from "vue";
import General from "./components/general.vue";
import Documentation from "./components/documentation.vue";
import UserService from "./components/userAssigne.vue";
import Form from "./components/form.vue";
import Opinion from "./components/actions.vue";
import Condition from "./components/condition.vue";
import {debounce} from 'lodash-es'
import EventEmitter from "@/utils/flow/EventEmitter";
import {useModelStore} from '@/store'
import {isUserService, isStartEvent} from "@/views/flowable/bo-utils/userTaskUtil";
import {isNotConditional} from "@/views/flowable/bo-utils/conditionalUtil";

const modelStore = useModelStore()
const renderComponents = ref<Component[]>([])
const setCurrentComponents = (activatedElement: any) => {

  renderComponents.value.splice(0, renderComponents.value.length)
  nextTick(() => {
    renderComponents.value.push(markRaw(General))
    renderComponents.value.push(markRaw(Documentation))

    isUserService(activatedElement) &&
    renderComponents.value.push(markRaw(UserService)) &&
    renderComponents.value.push(markRaw(Form)) &&
    renderComponents.value.push(markRaw(Opinion))

    isStartEvent(activatedElement) &&
    renderComponents.value.push(markRaw(Form))


    !isNotConditional(activatedElement) &&
    renderComponents.value.push(markRaw(Condition))
  })
}

let Modeler: any = null
const currentElementId = ref<String | undefined>()
const currentElementType = ref<String | undefined>()
const panelTitle = ref<String | undefined>()
const setCurrentElement = debounce((activatedElement?: any) => {
  if (!activatedElement) {
    activatedElement =
      Modeler?.get('elementRegistry').find((el: any) => el.type === 'bpmn:Process') ||
      Modeler?.get('elementRegistry').find((el: any) => el.type === 'bpmn:Collaboration')
    if (!activatedElement) {
      return console.error('No Element found!')
    }
  }

  modelStore.setElement(markRaw(activatedElement), activatedElement.id)

  currentElementId.value = activatedElement.id
  currentElementType.value = activatedElement.type.split(":")[1]
  panelTitle.value = Modeler?.get('translate')(currentElementType.value)
  setCurrentComponents(activatedElement)
  EventEmitter.emit('element-update', activatedElement)

}, 100)

onMounted(() => !currentElementId.value && setCurrentElement())

const initialize = () => {
  EventEmitter.on("modeler-init", (modeler: any) => {
    Modeler = modeler
    const eventBus = modeler.get('eventBus')
    // 导入完成后默认选中 process 节点
    eventBus.on('import.done', () => {
      setCurrentElement(null)
    })
    // 监听选择事件，修改当前激活的元素以及表单
    eventBus.on('selection.changed', ({newSelection}: { newSelection: any }) => {
      setCurrentElement(newSelection[0] || null)
    })
    eventBus.on('element.changed', ({element}: { element: any }) => {
      // if (element && element.id === currentElementId.value) {
      //   setCurrentElement(element)
      // }
    })
  })
}
initialize()

</script>

<style scoped lang="scss">
.properties-panel {
  overflow-y: scroll;

  .v-expansion-panel {
    border-radius: 0;
  }

  &:deep(.v-expansion-panel-text__wrapper) {
    padding: 8px 8px;

    .v-input:not(:last-child) {
      margin-bottom: 8px;
    }
  }

  &:deep(.v-expansion-panel-title) {
    min-height: 24px !important;
  }
}

</style>
