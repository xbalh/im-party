<template>
  <div>
    <nav-menu-item v-for="(level1Item, level1Index) in menu" :key="level1Index" :menu-item="level1Item">
      <template v-if="level1Item.children">

        <nav-menu-item
          v-for="(level2Item, level2Index) in level1Item.children"
          :key="level2Index"
          :menu-item="level2Item"
          subgroup
          small
        >
          <template v-if="level2Item.children">

            <nav-menu-item
              v-for="(level3Item, level3Index) in level2Item.children"
              :key="level3Index"
              :menu-item="level3Item"
              small
            />
          </template>
        </nav-menu-item>
      </template>
    </nav-menu-item>
  </div>
</template>

<script lang="ts" setup>
import {PropType} from "vue";

defineProps({
  menu: {
    type: Array as PropType<Array<App.GlobalMenuOption>>,
    default: () => []
  }
})
</script>
