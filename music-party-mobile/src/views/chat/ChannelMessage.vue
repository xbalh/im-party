<template>
  <div class="d-flex flex-grow-1" :class="{ 'flex-row-reverse': isOwnMessage}">
    <v-avatar size="40" class="elevation-1 grey lighten-3">
      <svg-icon :name="message.user.avatar"></svg-icon>
    </v-avatar>

    <div class="mx-2">
      <v-tooltip location="bottom">
        <template v-slot:activator="{ props }">
          <v-card
            class="pa-1"
            :color="isOwnMessage?'primary-darken-1':undefined"
            v-bind="props"
          >
            <div class="font-weight-bold">{{ message.text }}</div>
            <v-img class="mt-1 rounded" v-if="message.image" :src="message.image" max-width="300"
                   min-width="125">
            </v-img>
          </v-card>
        </template>
        <span>{{ message.timestamp }}</span>
      </v-tooltip>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {PropType} from "vue";
import {useAuthStore} from "@/store";

const {userInfo} = useAuthStore()

const props = defineProps({
  message: {
    type: Object as PropType<ApiChatManagement.message>,
    default: () => {
    }
  }
})


const isOwnMessage = computed(() => {
  return userInfo.userId === props.message.user.id
})

</script>
