<template>
  <div>
    <v-card class="text-center pa-1">
      <v-card-title class="justify-center text-h4 mb-2">{{ $t('register.title') }}</v-card-title>
      <v-card-subtitle>Let's build amazing products</v-card-subtitle>

      <!-- sign up form -->
      <v-card-text>
        <v-form ref="form" v-model="isFormValid">
          <v-text-field
            v-model="name"
            :rules="[rules.required]"
            class="text-left"
            :error="errorName"
            :error-messages="errorNameMessage"
            :label="$t('register.name')"
            name="name"
            variant="outlined"
            @keyup.enter="submit"
            @change="resetErrors"
          ></v-text-field>

          <v-text-field
            v-model="email"
            :rules="[rules.required]"
            :error="errorEmail"
            class="text-left"
            :error-messages="errorEmailMessage"
            :label="$t('register.email')"
            name="email"
            variant="outlined"
            @keyup.enter="submit"
            @change="resetErrors"
          ></v-text-field>

          <v-text-field
            v-model="password"
            class="text-left"
            :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required]"
            :type="showPassword ? 'text' : 'password'"
            :error="errorPassword"
            :error-messages="errorPasswordMessage"
            :label="$t('register.password')"
            name="password"
            variant="outlined"
            @change="resetErrors"
            @keyup.enter="submit"
            @click:appendInner="showPassword = !showPassword"
          ></v-text-field>

          <v-btn
            :loading="isLoading"
            :disabled="isSignUpDisabled"
            block
            size="x-large"
            color="primary"
            @click="submit"
          >{{ $t('register.button') }}
          </v-btn>

          <div class="caption font-weight-bold text-uppercase my-3">{{ $t('register.orsign') }}</div>

          <!-- external providers list -->
          <v-btn
            v-for="provider in providers"
            :key="provider.id"
            :loading="provider.isLoading"
            :disabled="isSignUpDisabled"
            class="mb-2 primary lighten-2 primary--text text--darken-3"
            block
            size="x-large"
            @click="signProvider(provider)"
          >
            <v-icon small left>mdi-{{ provider.id }}</v-icon>
            {{ provider.label }}
          </v-btn>

          <div v-if="errorProvider" class="error--text">{{ errorProviderMessages }}</div>

          <div class="mt-5 overline">
            {{ $t('register.agree') }}
            <br/>
            <router-link to="">{{ $t('common.tos') }}</router-link>
            &
            <router-link to="">{{ $t('common.policy') }}</router-link>
          </div>
        </v-form>
      </v-card-text>
    </v-card>

    <div class="text-center mt-6">
      {{ $t('register.account') }}
      <a @click="toLoginModule('sign-in')" class="font-weight-bold cursor-pointer">
        {{ $t('register.signin') }}
      </a>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useLoading} from "@/hooks";
import {useRouterPush} from "@/composables";

const {toLoginModule} = useRouterPush();
const {loading: isLoading, startLoading} = useLoading()
const isSignUpDisabled = ref(false)
const isFormValid = ref(true)
const email = ref('')
const password = ref('')
const name = ref('')
const errorName = ref(false)
const errorEmail = ref(false)
const errorPassword = ref(false)
const errorNameMessage = ref('')
const errorEmailMessage = ref('')
const errorPasswordMessage = ref('')
const errorProvider = ref(false)
const errorProviderMessages = ref('')
const showPassword = ref(false)

interface provider {
  id: string,
  label: string,
  isLoading: false
}

const providers = ref<Array<provider>>([{
  id: 'google',
  label: 'Google',
  isLoading: false
}, {
  id: 'facebook',
  label: 'Facebook',
  isLoading: false
}])

const rules = ref(
  {
    required: (value: string) => (value && Boolean(value)) || 'Required'
  })

const form = ref<import('vuetify/components').VForm>()

const submit = () => {
  form.value?.validate().then(r => {
    if (r.valid) {
      startLoading()
      isSignUpDisabled.value = true
      signUp(email.value, password.value)
    }
  })
}
const signUp = (email: string, password: string) => {

}
const signProvider = (p: provider) => {
}

const resetErrors = () => {

  errorName.value = false
  errorEmail.value = false
  errorPassword.value = false
  errorNameMessage.value = ''
  errorEmailMessage.value = ''
  errorPasswordMessage.value = ''

  errorProvider.value = false
  errorProviderMessages.value = ''
}
</script>
