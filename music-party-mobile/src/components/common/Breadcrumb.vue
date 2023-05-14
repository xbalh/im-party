<template>
  <v-breadcrumbs class="pa-0 py-2" :items="breadcrumbs" active-color="primary">
    <template v-slot:title="{ item}">
      {{ $t((item as App.GlobalBreadcrumb ).title || 'unknown') }}
    </template>
  </v-breadcrumbs>
</template>

<script setup lang="ts">
import {PropType} from 'vue';
import useBreadcrumb from "@/hooks/common/useBreadcrumb";

const props = defineProps({
  root: {
    type: String as PropType<Exclude<AuthRoute.AllRouteKey, 'not-found'>>,
    default: () => 'root'
  }
})

const {breadcrumbs} = useBreadcrumb(props.root)

</script>

<style scoped></style>
