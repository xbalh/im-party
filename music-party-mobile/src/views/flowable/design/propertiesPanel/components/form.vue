<template>
  <v-expansion-panel
    title="Forms"
  >
    <v-expansion-panel-text>
      <v-select variant="outlined" label="form" density="comfortable" hide-details
                v-model="formRef"
                :items="formOption"
                @update:modelValue="updateElementFormRef"></v-select>
    </v-expansion-panel-text>
  </v-expansion-panel>

</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useModelStore} from '@/store'
import {getBusinessObject} from "bpmn-js/lib/util/ModelUtil";

const formOption = ref(['ticket_form', 'review_form'])
const formRef = ref<String>("")
const modelStore = useModelStore()
const element = modelStore.getActive
const businessObject = getBusinessObject(element);

const getFormRef: () => String = () => {
  return businessObject.get('camunda:formRef');
};

if (getFormRef()) {
  formRef.value = getFormRef()
}

const updateElementFormRef = (value: String) => {
  modelStore.getCommandStack.execute('element.updateModdleProperties', {
    element,
    moddleElement: getBusinessObject(element),
    properties: {
      'camunda:formRef': value
    }
  });
}

</script>

<style scoped>

</style>
