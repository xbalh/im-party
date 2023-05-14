<template>
  <v-expansion-panel
    title="Condition"
  >
    <v-expansion-panel-text>
      <v-select variant="outlined" label="opinion" density="comfortable" hide-details
                color="primary"
                multiple
                chips
                v-model="opinionRef"
                :items="opinionOption"
                @update:modelValue="updateElementFormRef"></v-select>
    </v-expansion-panel-text>
  </v-expansion-panel>

</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useModelStore} from '@/store'
import {getBusinessObject} from "bpmn-js/lib/util/ModelUtil";

const opinionOption = ref(['approve', 'disapprove'])
const opinionRef = ref<Array<String>>([])
const modelStore = useModelStore()
const element = modelStore.getActive
const businessObject = getBusinessObject(element);

const getFormRef: () => Array<String> = () => {
  return businessObject.get('camunda:opinionRef');
};

if (getFormRef()) {
  opinionRef.value = getFormRef()
}

const updateElementFormRef = (value: String) => {
  modelStore.getCommandStack.execute('element.updateModdleProperties', {
    element,
    moddleElement: getBusinessObject(element),
    properties: {
      'camunda:opinionRef': value
    }
  });
}

</script>

<style scoped>

</style>
