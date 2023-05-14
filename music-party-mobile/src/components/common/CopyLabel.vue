<template>
  <div ref="copyLabel" class="copylabel animate__faster" @click.stop.prevent="copy">{{ text }}
    <v-tooltip location="bottom" activator="parent">
      <span>{{ tooltip }}</span>
    </v-tooltip>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  // Text to copy to clipboard.ts
  text: {
    type: String,
    default: ''
  },
  // Text to show on toast
  toastText: {
    type: String,
    default: 'Copied to clipboard.ts!'
  },
  animation: {
    type: String,
    default: 'heartBeat'
  }
})
const tooltip = ref('copy')
const copyLabel = ref<HTMLElement>()
let timeout: NodeJS.Timeout
onBeforeUnmount(() => {
  if (timeout) clearTimeout(timeout)
})
const copy = () => {
  if (copyLabel.value) {
    animate(copyLabel.value, props.animation)
  }
  clipboard(props.text, props.toastText)
  tooltip.value = 'Copied!'
  timeout = setTimeout(() => {
    tooltip.value = 'Copy'
  }, 2000)
}
</script>

<style scoped>
.copylabel {
  cursor: pointer;
  display: inline-block;
  border-bottom: 1px dashed;
}
</style>
