<template>
  <div>
    <v-card class="text-center pa-1">
      <v-card-title class="justify-center text-h4 mb-2  font-weight-regular">欢迎来到ImParty！</v-card-title>
      <v-card-subtitle>登录，然后享受音乐</v-card-subtitle>

      <!-- sign in form -->
      <v-card-text>
        <v-form ref="form" v-model="isFormValid">
          <v-text-field v-model="username" class="text-left" :rules="[rules.required]" :validate-on-blur="false"
            :error="error" :label="$t('login.username')" variant="outlined" clearable outlined @keyup.enter="submit"
            @change="resetErrors"></v-text-field>

          <v-text-field v-model="password" class="text-left" :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required]" :type="showPassword ? 'text' : 'password'" :error="error"
            :error-messages="errorMessages" :label="$t('login.password')" name="password" variant="outlined" clearable
            @change="resetErrors" @keyup.enter="submit" @click:appendInner="showPassword = !showPassword"></v-text-field>


          <v-btn :loading="isLoading" :disabled="isLoading" block size="x-large" color="primary" @click="submit">{{
            $t('login.button') }}
          </v-btn>

          <!-- <div class="text-caption font-weight-bold text-uppercase my-3">{{ $t('login.orsign') }}</div> -->

          <!-- external providers list -->
          <!-- <v-btn
            v-for="provider in providers"
            :key="provider.id"
            :loading="provider.isLoading"
            :disabled="isLoading"
            class="mb-2 bg-primary-lighten-2 lighten-2 text-primary-darken-3"
            block
            size="x-large"
            to="/"
          >
            <v-icon size="small" start>mdi-{{ provider.id }}</v-icon>
            {{ provider.label }}
          </v-btn> -->

          <div v-if="errorProvider" class="error--text">{{ errorProviderMessages }}</div>

        </v-form>

        <div class="forgot">
          <a class="mt-5 text-decoration-underline " style="cursor: pointer" @click="toLoginModule('forgot')">
            {{ $t('login.forgot') }}
          </a>
        </div>

      </v-card-text>
    </v-card>

    <div class="text-center mt-6">
      {{ $t('login.noaccount') }}
      <a @click="toLoginModule('sign-up')" style="cursor: pointer" class="font-weight-bold text-decoration-underline">
        {{ $t('login.create') }}
      </a>
    </div>
  </div>
</template>

<script lang="ts" setup>

import { ref } from 'vue'
import { useRouterPush } from "@/composables";
import { useAuthStore } from "@/store";

const auth = useAuthStore()
const { loginLoading: isLoading } = storeToRefs(auth)
const { login } = useAuthStore();
const { toLoginModule } = useRouterPush();
const form = ref<import('vuetify/components').VForm>()
const isFormValid = ref(false)
const username = ref('')
const password = ref('')
const error = ref(false)
const errorMessages = ref('')
const errorProvider = ref(false)
const errorProviderMessages = ref('')
const showPassword = ref(false)
// const providers = ref([{
//   id: 'google',
//   label: 'Google',
//   isLoading: false
// }, {
//   id: 'facebook',
//   label: 'Facebook',
//   isLoading: false
// }])
const rules = ref({
  required: (value: boolean) => (value && Boolean(value)) || 'Required'
})

const submit = () => {
  form.value?.validate().then(async r => {
    if (r.valid) {
      console.log("username:"+ username.value)
      console.log("password:"+ password.value)
      await login(username.value, password.value)
    }
  })
}
const resetErrors = () => {
  error.value = false
  errorMessages.value = ""
  errorProvider.value = false
  errorProviderMessages.value = ""
}

</script>

<style lang="scss">
.forgot {
  margin-top: 20px;
}
</style>
