<template>
  <v-card class="pa-2">
    <v-card-title class="justify-center text-h4 mb-2">Set new password</v-card-title>
    <div class="overline">{{ status }}</div>
    <div class="error--text mt-2 mb-4">{{ error }}</div>

    <a v-if="error" href="/public">Back to Sign In</a>

    <v-text-field
      v-model="newPassword"
      :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
      :rules="[rules.required]"
      :type="showPassword ? 'text' : 'password'"
      :error="errorNewPassword"
      :error-messages="errorNewPasswordMessage"
      variant="underlined"
      name="password"
      label="New Password"
      class="mt-4"
      @change="resetErrors"
      @keyup.enter="confirmPasswordReset"
      @click:append="showPassword = !showPassword"
    ></v-text-field>

    <v-btn
      :loading="isLoading"
      block
      depressed
      size="x-large"
      color="primary"
      @click="confirmPasswordReset"
    >Set new password and Sign In
    </v-btn>
  </v-card>
</template>

<script lang="ts" setup>
import {useLoading} from "@/hooks";

const {loading: isLoading, startLoading, endLoading} = useLoading()
const newPassword = ref('')
const errorNewPassword = ref(false)
const errorNewPasswordMessage = ref('')
const showPassword = ref(false)
const status = ref('Resetting password')
const error = ref()
const rules = ref({
  required: (value: string) => (value && Boolean(value)) || 'Required'
})

const confirmPasswordReset = () => {
  startLoading()
  setTimeout(() => {
    endLoading()
  }, 1000)
}

const resetErrors = () => {
  errorNewPassword.value = false
  errorNewPasswordMessage.value = ''
}
</script>
