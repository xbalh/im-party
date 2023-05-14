<template>
  <svg aria-hidden="true" v-bind="bindAttrs" class="d-flex flex-column justify-end" >
    <use :href="symbolId" fill="currentColor"/>
  </svg>
</template>

<script lang="ts" setup>
const props = defineProps({
  name: {
    type: String,
    required: true,
  },
})
const attrs = useAttrs();
const bindAttrs = computed<{ class: string; style: string }>(() => ({
  class: (attrs.class as string) || '',
  style: (attrs.style as string) || ''
}));

const symbolId = computed(() => {
  const {VITE_ICON_PREFFIX: prefix} = import.meta.env;

  return `#${prefix}-${props.name}`
})
</script>
