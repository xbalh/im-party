<template>
  <v-card key="chat" class="h-100" v-show="currentTab === 'chat'">
    <v-layout full-height :class="{ 'position-static': !lgAndUp }">
      <div class="d-flex flex-grow-1 flex-row">
        <v-navigation-drawer v-model="channelDrawer" :permanent="lgAndUp" floating
          class="elevation-1 rounded flex-shrink-0" :class="{ 'top-z-index': !lgAndUp }" width="240" touchless>
          <div class="px-2 py-1">
            <div class="title font-weight-bold text-primary">音趴</div>
            <div class="overline">1.0.0</div>
          </div>

          <v-list density="comfortable">

            <v-list-subheader class="overline">{{ $t('room.room') }}</v-list-subheader>
            <div class="mx-2 mb-2">
              <v-btn variant="outlined" block @click="showCreateDialog = true">
                <v-icon size="small" start>mdi-plus</v-icon>
                {{ $t('room.createRoom') }}
              </v-btn>
            </div>

            <v-list v-model:opened="open">
              <v-list-group v-for="(roomlist, roomstyle) in roomMap" :key="roomstyle" :value="roomstyle">
                <template v-slot:activator="{ props }">
                  <v-list-item v-bind="props" :title="roomstyle"></v-list-item>
                </template>
                <v-list-item v-for="roomInfo in roomlist" :key="roomInfo.roomNo"
                  :to="{ name: 'apps_chat-channel', query: { roomNo: roomInfo.roomNo, roomName: roomInfo.roomName } }"
                  exact>
                  <v-list-item-title class="text-subtitle-2">{{ roomInfo.roomName }}</v-list-item-title>
                </v-list-item>
              </v-list-group>
            </v-list>
          </v-list>
        </v-navigation-drawer>
        <v-main>
          <div :class="{ 'pl-3': lgAndUp }" class="d-flex flex-grow-1 h-100 flex-column">
            <v-card class="flex-grow-1 h-100">
              <router-view :key="currentRoute.fullPath" @toggle-users-drawer="usersDrawer = !usersDrawer"
                @toggle-menu="channelDrawer = !channelDrawer"></router-view>
            </v-card>
          </div>
        </v-main>

        <v-dialog v-model="showCreateDialog" max-width="400">
          <v-card>
            <v-card-title class="title">{{ $t('room.createRoom') }}</v-card-title>
            <div class="pa-3">
              <v-text-field ref="channel" v-model="newChannel" :label="$t('room.room')" maxlength="20" counter="20"
                autofocus @keyup.enter="addChannel()"></v-text-field>
            </div>
            <v-card-actions class="pa-2">
              <v-spacer></v-spacer>
              <v-btn @click="showCreateDialog = false">{{ $t('common.cancel') }}</v-btn>
              <v-btn :loading="isLoadingAdd" :disabled="newChannel.length === 0" color="success" @click="addChannel()">
                {{ $t('common.add') }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>

    </v-layout>
    <v-navigation-drawer v-model="usersDrawer" width="180" order="-1" location="right" touchless>
      <v-list v-if="!fetchOnlineLoading" dense>
        <v-list-item-subtitle class="mx-2 my-2 overline ">
          {{ $t('chat.online', { count: onlineUsers.length }) }}
        </v-list-item-subtitle>
        <v-list-item v-for="item in onlineUsers" :key="item.id">
          <v-list-item-title class="text-body-2" :class="{ 'text-primary': item.id === userInfo.userId }">
            <v-avatar size="40" class="elevation-1 grey lighten-3">
              <svg-icon :name="item.avatar"></svg-icon>
            </v-avatar>
            {{ item.name }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
      <v-row v-else class="fill-height" align-content="center" justify="center">
        <v-col class="text-subtitle-1 text-center" cols="12">
          Fetching Online Users
        </v-col>
        <v-col cols="6">
          <v-progress-linear color="primary" indeterminate rounded height="6"></v-progress-linear>
        </v-col>
      </v-row>
    </v-navigation-drawer>
  </v-card>
  <v-card key="myself" class="h-100" v-show="currentTab === 'myself'">
    <v-tabs v-model="topTabCurrent" fixed-tabs color="primary">
      <v-tab :value="1">我的歌单</v-tab>
      <v-tab :value="2">收藏歌单</v-tab>
    </v-tabs>
    <v-list :items="items"></v-list>
  </v-card>
</template>

<script lang="ts" setup>
import { useDisplay } from "vuetify";
import { ref } from 'vue'
import { useLoading } from "@/hooks";
import { useRouter } from "vue-router";
import { useRouterPush } from "@/composables";
import { fetchRoomList } from "@/service/api/room";
import { groupByKey } from "@/utils";
import Bus from "@/utils/common/Bus";

const { loading: isLoadingAdd, startLoading, endLoading } = useLoading()

const isMusicPage = ref(false)
Bus.on('toMusicPage', (flag: boolean) => {
  isMusicPage.value = flag
})
const currentTab = ref('chat')
Bus.on('bottom-nav-change', (tabName: string) => {
  currentTab.value = tabName
})

const items = ref([
  { type: 'subheader', title: '我的歌单(26个)' },
  {
    title: 'Item #1',
    value: 1,
  },
  {
    title: 'Item #2',
    value: 2,
  },
  {
    title: 'Item #3',
    value: 3,
  },
])

const topTabCurrent = ref(0)

const channelDrawer = ref()
const showCreateDialog = ref(false)
const channels = ref(['general', 'production', 'qa', 'staging', 'random'])
const newChannel = ref("")
const { routerPush } = useRouterPush()
const addChannel = () => {
  startLoading()
  setTimeout(() => {
    channels.value.push(newChannel.value)
    showCreateDialog.value = false
    routerPush(`/apps/chat-channel/${newChannel.value}`)
    newChannel.value = ''
    endLoading()
  }, 1000)
}
const { lgAndUp } = useDisplay()
const auth = useAuthStore();
const { userInfo } = storeToRefs(auth)
const { currentRoute } = useRouter()

const usersDrawer = ref()
const onlineUsers = ref<Array<ApiUserManagement.User>>([])

const roomList = ref<Array<ApiRoomManagement.roomInfo>>([])

const roomMap = ref()

const open = ref<Array<string>>([])

const { loading: fetchOnlineLoading, startLoading: startFetchRoom, endLoading: endFetchRoom } = useLoading()
// const fetchOnlineUsers = async () => {
//   startFetchOnline()
//   const resp = await fetchUserList();
//   endFetchOnline()
//   if (resp.data) {
//     onlineUsers.value = resp.data.list
//   }
// }

const fetchRooms = async () => {
  startFetchRoom()
  const resp = await fetchRoomList();
  endFetchRoom()
  if (resp.data) {
    roomList.value = resp.data
    roomMap.value = groupByKey(resp.data, 'roomStyle')
    open.value = Object.keys(roomMap.value)
  }
}

onMounted(() => {
  fetchRooms()
  // routerPush(`/apps/chat-channel/${channels.value[0]}`)
  // fetchOnlineUsers()

})
</script>

<style scoped></style>
