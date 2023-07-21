<template>
  <div>
    <!-- channel toolbar -->
    <v-toolbar flat height="64" color="surface">
      <v-app-bar-nav-icon class="hidden-lg-and-up" @click="$emit('toggle-menu')"></v-app-bar-nav-icon>
      <v-toolbar-title>
        {{ roomName }}
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn class="mx-1" icon @click.stop="$emit('toggle-usersDrawer')">
        <v-icon>mdi-account-group-outline</v-icon>
      </v-btn>
    </v-toolbar>

    <v-divider></v-divider>
    <!-- channel messages -->
    <div class="channel-page" :class="{ 'channel-page-bg': !current.dark }">
      <div id="messages" ref="messagesRef" class="messages mx-2">
        <v-slide-y-transition group tag="div">
          <channel-message v-for="message in messages" :key="message.id" :message="message" class="my-4 d-flex" />
        </v-slide-y-transition>
      </div>

      <div class="input-box pa-2">
        <div class="d-flex position-relative align-center">
          <v-text-field v-model="input" variant="outlined" density="comfortable" ref="inputMessage" autofocus
            class="font-weight-bold position-relative align-center" :placeholder="`${$t('chat.message')}`" hide-details
            @keyup.enter="sendMessage" @blur="changeBlur">
          </v-text-field>
          <v-btn flat rounded icon size="small" color="primary" class="mx-1" :disabled="!input" @click="sendMessage">
            <v-icon size="small">mdi-send</v-icon>
          </v-btn>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import ChannelMessage from './ChannelMessage.vue'
import { ref } from "vue";
import { useTheme } from 'vuetify'
import { useAuthStore } from "@/store";
import { createId } from "seemly";
import { useRoute } from 'vue-router'
import Ws from '@/utils/common/ws';
import { getServiceEnvConfig } from '~/.env-config';
import { localStg } from '@/utils';
import Bus from '@/utils/common/Bus';
import { formatDate } from '@vueuse/core';
import { random } from 'lodash-es';
import { addMusic } from "@/service/api/room";


const route = useRoute()
const { roomNo, roomName } = route.query
const ws = ref<Ws>()
const { current } = useTheme()
const auth = useAuthStore();
const { userInfo } = storeToRefs(auth)
const messagesRef = ref<HTMLDivElement>()
const inputMessage = ref<HTMLDivElement>()
const { wsUrl } = getServiceEnvConfig(import.meta.env);
const messages = ref<Array<ApiChatManagement.message>>([
  {
    id: '1',
    text: "这句话是用来测试的",
    timestamp: "2022-12-13 15:13:10",
    user: {
      avatar: 'avatar1',
      id: '1',
      name: 'test1'
    }
  }
])
defineEmits(['toggle-menu', 'toggle-usersDrawer'])

const currentHeight = ref<Number>()

const scrollBottom = () => {
  nextTick(() => {
    if (messagesRef.value)
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    currentHeight.value = messagesRef.value!.scrollHeight
  })
}

const input = ref('')

const demo = ref()

onMounted(() => {
  if (roomNo) {
    window.$loadingOverly?.show()
    connectWS(roomNo as string)
    currentHeight.value = messagesRef.value!.scrollHeight
  }
  // demo.value = setInterval(async () => {
  //   const resp = await fetchMessage()
  //   if (resp.data) {
  //     messages.value.push(resp.data)
  //     scrollBottom()
  //   }
  // }, 2000)
})

// watch(route.query, (newValue, oldValue) => {
//   const { roomNo, roomName } = route.query
//   connectWS(roomNo as string)
// })

/**建立WS连接，并订阅 */
const connectWS = (roomNo: string) => {
  ws.value = new Ws(wsUrl + `/musicParty/ws/${roomNo}`, localStg.get('token') as string)
  ws.value.subscribe('/music/chat', chatHandle)
  ws.value.subscribe('/music/playControl/nextPlay', nextPlayHandle)
  ws.value.subscribe(`/music/room/user-join/${roomNo}`, userJoinHandle)
  ws.value.subscribe(`/music/room/user-leave/${roomNo}`, userLeaveHandle)
  ws.value.subscribe(`/music/room/playlist-change/${roomNo}`, playlistChangeHandle)
}

/* 用户加入房间处理 */
const userJoinHandle = (userName: object) => {
  console.log("用户：" + String(userName) + "加入了")
}

/* 用户离开房间处理 */
const userLeaveHandle = (userName: object) => {
  console.log("用户：" + String(userName) + "离开了")
}

/** 歌曲列表变动处理 */
const playlistChangeHandle = (songList: Music.SongInfo[]) =>{
  Bus.emit('playlist-change', songList);
}

/**聊天处理 */
const chatHandle = (data: Chat.Msg) => {
  if (data.from === userInfo.value.username) return
  let msg: ApiChatManagement.message = {
    id: String(random(1, 9999, false)),
    text: data.msg,
    timestamp: formatDate(new Date(data.timestamp), 'YYYY-MM-DD hh:mm:ss'),
    user: {
      avatar: 'avatar1',
      id: String(random(1, 9999, false)),
      name: data.from
    }
  }
  const isBottom = checkScrollIsToBottom()
  messages.value.push(msg)
  if (isBottom) {
    scrollBottom()
  }
}

const checkScrollIsToBottom = () => {
  // 获取距离顶部的距离
  const scrollTop = messagesRef.value!.scrollTop;
  // 获取可视区的高度
  const windowHeight = messagesRef.value!.clientHeight;
  // 获取滚动条的总高度
  const scrollHeight = messagesRef.value!.scrollHeight;
  if (scrollTop + windowHeight - scrollHeight <= 5) {
    // 把距离顶部的距离加上可视区域的高度 等于或者大于滚动条的总高度就是到达底部
    return true
  }
  return false
}

/**切换下一首处理 */
const nextPlayHandle = (data: Music.SongInfo) => {
  Bus.emit('changeSong', data)
}

setTimeout(() => {
  demo.value && clearInterval(demo.value)
}, 1000 * 20)

onUnmounted(() => {
  ws.value?.destroy()
  clearInterval(demo.value)
})

const changeBlur = () => {
  window.scroll(0, 0);//失焦后强制让页面归位
}

const sendMessage = () => {
  messages.value.push({
    id: createId(),
    text: input.value,
    timestamp: new Date().toLocaleString(),
    user: {
      id: userInfo.value.userId,
      avatar: userInfo.value.userAvatar ?? '',
      name: userInfo.value.userName
    }
  })
  const data = {
    method: "/music/chat",
    data: {
      msg: input.value
    }
  }
  ws.value?.send(JSON.stringify(data))
  input.value = ""
  inputMessage.value?.focus()
  scrollBottom()
}

const onDemandMusic = async (musicInfo: ApiMusic.playListMusicInfo) => {
  const resp = await addMusic(parseInt(roomNo as string), String(musicInfo.id));
  if (resp.error?.code) {
    window.$snackBar?.error(resp.error.msg)
    return
  }
  window.$snackBar?.success('点歌成功')
}

Bus.on('onDemandMusic', (musicInfo: ApiMusic.playListMusicInfo) => {
  if (!roomNo) {
    window.$snackBar?.error('请先进入一个房间~');
    return;
  }
  onDemandMusic(musicInfo)
})



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
