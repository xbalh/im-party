<template>
  <v-expansion-panel
    title="Documentation"
  >
    <v-expansion-panel-text>
      <v-textarea variant="outlined" label="Element Documentation" density="comfortable" hide-details
                  :model-value="elementDoc" @change="updateElementDoc"></v-textarea>
    </v-expansion-panel-text>
  </v-expansion-panel>

</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useModelStore} from '@/store'
import EventEmitter from "@/utils/flow/EventEmitter";
import {getDocumentValue, setDocumentValue} from "@/views/flowable/bo-utils/documentationUtil";

const elementDoc = ref<String>("")

const modelStore = useModelStore()

const updateElementDoc = (value: any) => {
  setDocumentValue(modelStore.getActive, value.target.value)
}

onMounted(() => {
  elementDoc.value = getDocumentValue(modelStore.getActive || '')
  EventEmitter.on('element-update', () => {
    elementDoc.value = getDocumentValue(modelStore.getActive || '')
  })
})


</script>

<style scoped>

</style>
