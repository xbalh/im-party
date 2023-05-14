<template>
  <loading-overly-provider>
    <snackbar-provider>
      <dialog-provider>
        <slot/>
        <vuetify-provider-content/>
      </dialog-provider>
    </snackbar-provider>
  </loading-overly-provider>
</template>

<script lang="ts" setup>
import {useSnackBar, useDialog} from "@/components/provider";
import {useLoadingOverly} from "@/components/provider";
import SnackbarProvider from "@/components/provider/SnackbarProvider";
import LoadingOverlyProvider from "@/components/provider/LoadingOverlyProvider";
import DialogProvider from "@/components/provider/DialogProvider";

function registerTools() {
  window.$snackBar = useSnackBar()
  window.$loadingOverly = useLoadingOverly()
  window.$dialog = useDialog()
}

const VuetifyProviderContent = defineComponent({
  name: 'VuetifyProviderContent',
  setup() {
    registerTools()
  },
  render() {
    return h('div');
  }
});

</script>

<style scoped>

</style>
