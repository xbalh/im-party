<template>
  <transition name="fade-slide" mode="out-in" appear>
    <component :is="activeModule.component"/>
  </transition>
</template>

<script lang="ts" setup>
import type {Component} from 'vue';
import {ResetPg, SigninPg, SignUpPg, VerifyEmailPg, ForgotPg} from './components';
interface Props {
  module: EnumType.LoginModuleKey;
}

const props = defineProps<Props>();


interface LoginModule {
  key: EnumType.LoginModuleKey;
  component: Component;
}

const modules: LoginModule[] = [
  {key: 'forgot', component: ForgotPg},
  {key: 'sign-in', component: SigninPg},
  {key: 'sign-up', component: SignUpPg},
  {key: 'reset', component: ResetPg},
  {key: 'verify-email', component: VerifyEmailPg}
];

const activeModule = computed(() => {
  const active: LoginModule = {...modules[0]};
  const findItem = modules.find(item => item.key === props.module);
  if (findItem) {
    Object.assign(active, findItem);
  }
  return active;
});
</script>

<style scoped>

</style>
