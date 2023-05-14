<template>

  <v-expansion-panel
    title="General"
  >
    <v-expansion-panel-text>
      <v-text-field variant="outlined" label="name" density="comfortable" hide-details
                    :model-value="elementName" @change="updateElementName"></v-text-field>
      <v-text-field variant="outlined" label="id" density="comfortable" hide-details
                    :model-value="elementId" @change="updateElementId"></v-text-field>
      <template v-if="isProcess">
        <v-text-field variant="outlined" label="versionTag" density="comfortable" hide-details
                      :model-value="elementVersion" @change="updateElementVersion"></v-text-field>
        <v-checkbox label="executable" hide-details density="comfortable"
                    :model-value="elementExecutable" @change="updateElementExecutable"></v-checkbox>
      </template>
    </v-expansion-panel-text>
  </v-expansion-panel>

</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useModelStore} from '@/store'
import {getNameValue, setNameValue} from "@/views/flowable/bo-utils/nameUtil";
import {
  getProcessExecutable,
  getProcessVersionTag,
  setProcessExecutable,
  setProcessVersionTag
} from "@/views/flowable/bo-utils/processUtil";
import EventEmitter from "@/utils/flow/EventEmitter";
import {setIdValue} from "@/views/flowable/bo-utils/idUtil";

const isProcess = ref<Boolean>(false)
const elementName = ref<String>("")
const elementId = ref<String>("")
const elementExecutable = ref<Boolean>(true)
const elementVersion = ref<String>("")

const modelStore = useModelStore()

const reloadGenerationData = () => {
  isProcess.value = !!modelStore.getActive && modelStore.getActive.type === 'bpmn:Process'
  elementId.value = modelStore.getActiveId as string
  elementName.value = getNameValue(modelStore.getActive) || ''
  if (isProcess.value) {
    elementExecutable.value = getProcessExecutable(modelStore.getActive)
    elementVersion.value = getProcessVersionTag(modelStore.getActive) || ''
  }
}

const updateElementName = (value: any) => {
  setNameValue(modelStore.getActive, value.target.value)
}
const updateElementId = (value: any) => {
  setIdValue(modelStore.getActive, value.target.value)
}
const updateElementVersion = (value: any) => {
  const reg = /((\d|([1-9](\d*))).){2}(\d|([1-9](\d*)))/
  if (reg.test(value.target.value)) {
    setProcessVersionTag(modelStore.getActive, value.target.value)
  } else {
    window.$snackBar?.error('版本号必须符合语义化版本2.0.0 要点')
  }
}
const updateElementExecutable = (value: boolean) => {
  setProcessExecutable(modelStore.getActive, value)
}

onMounted(() => {
  reloadGenerationData()
  EventEmitter.on('element-update', reloadGenerationData)
})


</script>

<style scoped>

</style>
