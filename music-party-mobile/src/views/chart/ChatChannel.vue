<template>
  <div>
    <!-- channel toolbar -->
    <v-toolbar flat height="64" color="surface">
      <v-app-bar-nav-icon class="hidden-lg-and-up" @click="$emit('toggle-menu')"></v-app-bar-nav-icon>
      <v-toolbar-title>
        # {{ $route.params.id }}
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn class="mx-1" icon @click.stop="$emit('toggle-usersDrawer')">
        <v-icon>mdi-account-group-outline</v-icon>
      </v-btn>
    </v-toolbar>

    <v-divider></v-divider>
    <!-- channel messages -->
    <div class="channel-page" :class="{'channel-page-bg':!current.dark}">
      <div id="messages" ref="messagesRef" class="messages mx-2">
        <v-slide-y-transition group tag="div">
          <channel-message
            v-for="message in messages"
            :key="message.id"
            :message="message"
            class="my-4 d-flex"
          />
        </v-slide-y-transition>
      </div>

      <div class="input-box pa-2">
        <div class="d-flex position-relative align-center">
          <v-text-field
            v-model="input"
            variant="outlined"
            density="comfortable"
            ref="inputMessage"
            autofocus
            class="font-weight-bold position-relative align-center"
            :placeholder="`${$t('chat.message')} #${$route.params.id}`"
            hide-details
            @keyup.enter="sendMessage"
          >
          </v-text-field>
          <v-btn
            flat
            rounded
            icon
            size="small"
            color="primary"
            class="mx-1"
            :disabled="!input"
            @click="sendMessage"
          >
            <v-icon size="small">mdi-send</v-icon>
          </v-btn>
        </div>
      </div>
    </div>

  </div>
</template>

<script lang="ts" setup>
import ChannelMessage from './ChannelMessage.vue'
import {ref} from "vue";
import {fetchMessage} from '@/service'
import {useTheme} from 'vuetify'
import {useAuthStore} from "@/store";
import {createId} from "seemly";


const {current} = useTheme()
const {userInfo} = useAuthStore()
const messagesRef = ref<HTMLDivElement>()
const inputMessage = ref<HTMLDivElement>()
const messages = ref<Array<ApiChatManagement.message>>([
  {
    id: '1',
    text: "hi ,i am frank silva",
    timestamp: "2022-12-13 15:13:10",
    user: {
      avatar: 'avatar1',
      id: '1'
    }
  },
  {
    id: '2',
    text: "who is lulu?",
    timestamp: "2022-12-13 15:18:10",
    user: {
      avatar: 'avatar3',
      id: '2'
    }
  },
  {
    id: '3',
    text: "lulu is my arrogant cat",
    image: 'https://i.imgur.com/3e63lxL.jpg',
    timestamp: "2022-12-13 16:13:10",
    user: {
      avatar: 'avatar9',
      id: '3'
    }
  }
])
defineEmits(['toggle-menu', 'toggle-usersDrawer'])

const scrollBottom = () => {
  nextTick(() => {
    if (messagesRef.value)
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  })
}

const input = ref('')

const demo = ref()

onBeforeMount(() => {
  demo.value = setInterval(async () => {
    const resp = await fetchMessage()
    if (resp.data) {
      messages.value.push(resp.data)
      scrollBottom()
    }
  }, 2000)
})

setTimeout(() => {
  demo.value && clearInterval(demo.value)
}, 1000 * 20)

onUnmounted(() => {
  clearInterval(demo.value)
})


const sendMessage = () => {
  messages.value.push({
    id: createId(),
    text: input.value,
    timestamp: new Date().toLocaleString(),
    user: {
      id: userInfo.userId,
      avatar: userInfo.userAvatar ?? ''
    }
  })
  input.value = ""
  inputMessage.value?.focus()
  scrollBottom()
}


</script>

<style lang="scss" scoped>
.channel-page-bg {
  background: url("/images/chat/chat-bg-2.png");
}

.channel-page {
  position: absolute;
  top: 65px;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  display: flex;
  flex-direction: column;

  .messages {
    flex-grow: 1;
    margin-bottom: 68px;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    min-height: 0;
  }

  .input-box {
    position: fixed;
    bottom: 12px;
    width: 100%;
  }

  .messages {
    padding-bottom: 0;
  }

  .input-box {
    position: absolute;
    bottom: 12px;
  }
}
</style>
