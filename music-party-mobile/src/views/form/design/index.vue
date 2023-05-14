<template>
  <v-layout class="h-100">
    <v-navigation-drawer width="250" :permanent="true">
      <v-list>
        <v-list-item>
          <div class="text-h5 font-weight-bold">Form Component</div>
        </v-list-item>
        <v-list-item v-for="component in formComponents">
          <v-list-item-title>
            {{ component.name }}
          </v-list-item-title>
          <v-list-item-action>
            <div v-if="component.children" class="component-list d-flex flex-row w-100  flex-wrap ">
              <vue-draggable :list="component.children" item-key="name"
                             :clone="cloneComponent"
                             @end="endComponent"
                             :sort="false"
                             :group="{ name: 'formComponentGroups', pull: 'clone', put: false }">
                <template #item="{ element }">
                  <v-chip label color="primary" class="component-list-item cursor-move" @click="addComponent(element)">
                    {{ element.name }}
                  </v-chip>
                </template>
              </vue-draggable>
            </div>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-navigation-drawer width="250" :permanent="true" location="right">
      <v-card elevation="0">
        <v-card-title>
          Base Attribute
        </v-card-title>
        <v-card-text>
          <formitem-edit v-if="!!activeComponent" v-model="activeComponent"></formitem-edit>
        </v-card-text>
      </v-card>
    </v-navigation-drawer>
    <v-main class="form-design">
      <v-card elevation="0" class="h-100">
        <v-card-title class="d-flex align-center">
          <v-text-field density="comfortable" hide-details
                        variant="filled"
                        placeholder="formName"
                        class="v-text-field-rounded pl-0 "
          ></v-text-field>

          <v-spacer/>
          <span class="text-caption">{{ formId }}</span>
          <v-btn variant="tonal" color="primary" :disabled="selectedFormComponents.length===0">Preview</v-btn>
          <v-btn variant="tonal" color="error" class="ml-1" :disabled="selectedFormComponents.length===0"
                 @click="selectedFormComponents=[]">ClearAll
          </v-btn>
        </v-card-title>
        <v-card-text class="px-0">
          <v-form class="px-1">
            <div v-if="selectedFormComponents.length === 0" class="px-1 text-h6 text-primary position-absolute">
              drag component from left panel
              to here !
            </div>
            <vue-draggable
              :list="selectedFormComponents"
              class="draggable-area"
              :animation="340"
              group="formComponentGroups"
              ghost-class="ghost"
              item-key="id"
            >
              <template #item="{ element }">
                <formitem :item="element"
                          :active="activeComponent"
                          :list="selectedFormComponents"
                          :id-prefix="formId"
                          @delete="deleteComponent"
                          @duplicate="duplicateComponent"
                          @selected="selectComponent"
                >
                </formitem>
              </template>
            </vue-draggable>

          </v-form>
        </v-card-text>
      </v-card>

    </v-main>
  </v-layout>
</template>

<script setup lang="ts">
import VueDraggable from 'vuedraggable'
import {ref} from 'vue'
import Formitem from "@/views/form/design/components/formitem";
import {cloneDeep, uniqueId} from 'lodash-es'
import formComponentsGetter from "./formComponents";
import FormitemEdit from "@/views/form/design/components/formitemEdit";

const formId = 'Form' + (+new Date).toString(36).slice(-10);
const activeComponent = ref<formComponent>()

const theme = useTheme();
const formComponents = formComponentsGetter(theme.current.value.colors.primary)
const cloningComponent = ref<formComponent>()
const selectedFormComponents = ref<Array<formComponent>>([])
const selectComponent = (c: formComponent) => {
  activeComponent.value = c
}
const endComponent = (c: { to: any, from: any }) => {
  if (c.from != c.to) {
    cloningComponent.value && selectComponent(cloningComponent.value)
  }
}
const addComponent = (c: formComponent) => {
  c = cloneComponent(c)
  cloningComponent.value = c
  activeComponent.value = cloningComponent.value
  selectedFormComponents.value.push(activeComponent.value)
}
const cloneComponent = (c: formComponent) => {
  const clone = cloneDeep(c)
  clone.id = formId + '_' + uniqueId()
  cloningComponent.value = clone
  return clone
}

const deleteComponent = (c: formComponent) => {
  // const index = selectedFormComponents.value.findIndex(i => i.id == c.id)
  // if (index > -1) {
  //   selectedFormComponents.value.splice(index, 1)
  // }
}
const duplicateComponent = (c: formComponent) => {
  // const find = selectedFormComponents.value.find(i => i.id == c.id)
  // if (find) {
  //   const tmp = cloneComponent(find)
  //   selectedFormComponents.value.splice(selectedFormComponents.value.indexOf(find), 0, tmp)
  // }
}

</script>

<style scoped lang="scss">

.component-list {
  margin-right: -8px;
  margin-bottom: -8px;

  .component-list-item {
    margin-right: 8px;
    margin-bottom: 8px;
  }
}

.form-design {
  .ghost {
    position: relative;
    display: block;
    min-height: 60px;
    //height: 60px;

    &::before {
      content: " ";
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      height: 4px;
      background: rgb(var(--v-theme-primary));
      z-index: 2;
    }
  }

  .draggable-area {

    min-height: 500px;

    &:deep(.form-item) {
      position: relative;
      margin-bottom: 8px;
      border: 1px dashed #ccc;


      .form-item-active-options {
        z-index: 10;
        position: absolute;
        right: 0;
        top: -10px;
      }

      .form-item-active-flex {
        z-index: 10;
        position: absolute;
        left: 0;
        top: -10px
      }
    }

    &:deep(.form-item:last-child) {
      margin-bottom: 0;
    }

    &:deep(.form-item-active) {
      background-color: rgb(var(--v-theme-primary), 0.1);
      border: none;
    }
  }
}
</style>
