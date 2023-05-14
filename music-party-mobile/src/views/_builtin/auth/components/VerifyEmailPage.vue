<template>
  <v-card class="pa-2">
    <h1>Please verify the email</h1>
    <div class="mb-6 overline">Please check your email for the link to verify the email.</div>

    <v-btn
      :loading="isLoading"
      :disabled="disabled"
      block
      depressed
      size="x-large"
      color="primary"
      @click="resend"
    >Re-send email {{ seconds }}
    </v-btn>
  </v-card>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useLoading} from "@/hooks";

const TIMEOUT = 10

const {loading: isLoading} = useLoading()
const disabled = ref(true)
const times = ref(0)
const resendInterval = ref<NodeJS.Timer>()
const secondsToEnable = ref(TIMEOUT)
const seconds = ref('')
onMounted(() => {
  setTimer()
})


const setTimer = () => {
  disabled.value = true
  times.value++
  secondsToEnable.value = TIMEOUT * times.value

  resendInterval.value = setInterval(() => {
    if (secondsToEnable.value === 0) {
      clearInterval(resendInterval.value)
      seconds.value = ''
      disabled.value = false
    } else {
      seconds.value = `( ${secondsToEnable.value} )`
      secondsToEnable.value--
    }
  }, 1000)
}


const resend = async () => {
  setTimer()
}

</script>
