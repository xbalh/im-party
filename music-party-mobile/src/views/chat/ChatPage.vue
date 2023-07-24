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
                <v-list-item v-for="roomInfo in roomlist" :key="roomInfo.roomNo" replace :to="enterRoom(roomInfo)" exact>
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
    <div v-if="isUserBindWyy">
      <div v-show="!isPlayListDetailPage">
        <v-tabs v-model="topTabCurrent" fixed-tabs color="primary">
          <v-tab value="myPlayList">我的歌单</v-tab>
          <v-tab value="collectPlayList">收藏歌单</v-tab>
        </v-tabs>
        <div class="listArea">
          <div class="playListDiv mx-2">
            <v-window v-model="topTabCurrent">
              <v-window-item value="myPlayList">
                <v-slide-y-transition key="myPlayList" group tag="div">
                  <v-list class="playlist">
                    <v-list-item lines="one" :title="mySubheaderTitle" class="subheader"></v-list-item>
                    <v-list-item v-for="item in myPlayList" lines="two" :type="item.type" item-props :key="item.title"
                      :title="item.title" :subtitle="item.subtitle" :prepend-avatar="item.prependAvatar" class="item"
                      @click="enterPlayList(item)"></v-list-item>
                  </v-list>
                </v-slide-y-transition>
              </v-window-item>
              <v-window-item value="collectPlayList">
                <v-slide-y-transition key="collectPlayList" group tag="div">
                  <v-list lines="two" class="playlist">
                    <v-list-item lines="one" :title="collectSubheaderTitle" class="subheader"></v-list-item>
                    <v-list-item v-for="item in collectPlayList" lines="two" :type="item.type" item-props
                      :key="item.title" :title="item.title" :subtitle="item.subtitle" :prepend-avatar="item.prependAvatar"
                      class="item" @click="enterPlayList(item)"></v-list-item>
                  </v-list>
                </v-slide-y-transition>
              </v-window-item>
            </v-window>
          </div>
        </div>
      </div>
      <div v-show="isPlayListDetailPage">
        <v-toolbar color="surface">
          <v-btn icon class="hidden-xs-only" @click="closePlayListDetailPage">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <v-toolbar-title>{{ currentPlayList?.title }}</v-toolbar-title>
        </v-toolbar>
        <div class="listArea">
          <div class="playListDiv mx-2">
            <v-infinite-scroll :items="currentPlayListAllMusic" height="100%" class="musicList" item-height="48"
              @load="musicListScrollBottmHandle">
              <template v-for="(musicInfo, index) in currentPlayListAllMusic" :key="item">
                <v-list-item lines="two" :title="musicInfo.name" :subtitle="handleMusicSubtitle(musicInfo)"
                  @click="onDemandMusic(musicInfo)">
                  <template v-slot:prepend>
                    <span>{{ index + 1 }}</span>
                  </template>
                </v-list-item>
              </template>
            </v-infinite-scroll>
          </div>
        </div>
      </div>
    </div>
    <div v-else>

      <v-card>
        <v-card-title> 请先绑定网易云账号 </v-card-title>
        <v-card-item>
          <v-spacer></v-spacer>
          <v-text-field v-model="searchInput" append-icon="mdi-magnify" label="通过用户名搜索网易云用户" single-line hide-details
            @click:append="searchWyyUserHandle"></v-text-field>
        </v-card-item>
        <v-card-item>
          <v-list>
            <v-list-item v-for="item in searchWyyUserItems" lines="one" :title="item.nickname" :prepend-avatar="item.avatarUrl"
              @click="bindUser(item)">
            </v-list-item>
          </v-list>
        </v-card-item>
      </v-card>
    </div>

    <v-dialog v-model="dialog" width="auto">
      <v-card>
        <v-card-title class="text-h5">
          确定要点这首歌吗？
        </v-card-title>
        <v-card-text>{{ currentWishOnDemandMusic?.name }}</v-card-text>
        <v-card-text>{{ currentWishOnDemandMusic && handleMusicSubtitle(currentWishOnDemandMusic) }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green-darken-1" variant="text" @click="dialog = false">
            取消
          </v-btn>
          <v-btn color="green-darken-1" variant="text" @click="confirmOndemandMusic">
            确定
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script lang="ts" setup>
import { useDisplay } from "vuetify";
import { ref } from 'vue'
import { useLoading } from "@/hooks";
import { useRouter } from "vue-router";
import { useRouterPush } from "@/composables";
import { fetchRoomList } from "@/service/api/room";
import { fetchPlayList, fetchPlayListAllMusic, searchWyyUser } from "@/service/api/music";
import { groupByKey } from "@/utils";
import Bus from "@/utils/common/Bus";
import { trim } from "lodash-es";

const { loading: isLoadingAdd, startLoading, endLoading } = useLoading()

const isMusicPage = ref(false)
Bus.on('toMusicPage', (flag: boolean) => {
  isMusicPage.value = flag
})
const currentTab = ref('chat')
Bus.on('bottom-nav-change', (tabName: string) => {
  currentTab.value = tabName
})
const isUserBindWyy = ref(false)
const mySubheaderTitle = ref()
const myPlayList = ref()
const collectSubheaderTitle = ref()
const collectPlayList = ref()
const isPlayListDetailPage = ref(false)
const currentPlayListAllMusic = ref<Array<ApiMusic.playListMusicInfo>>()
const currentWishOnDemandMusic = ref<ApiMusic.playListMusicInfo>()
const dialog = ref(false)

const topTabCurrent = ref('myPlayList')

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

const currentPlayList = ref()

const { loading: fetchOnlineLoading, startLoading: startFetchRoom, endLoading: endFetchRoom } = useLoading()
// const fetchOnlineUsers = async () => {
//   startFetchOnline()
//   const resp = await fetchUserList();
//   endFetchOnline()
//   if (resp.data) {
//     onlineUsers.value = resp.data.list
//   }
// }

const searchInput = ref('')
const searchWyyUserItems = ref()
const searchWyyUserHandle = async () => {
  if (!searchInput || trim(searchInput.value) === '') return
  const resp = await searchWyyUser(searchInput.value)
  if (resp.data) {
    searchWyyUserItems.value = resp.data.userprofiles
  }
}

const bindUser = (userInfo) => {
  const bindDialog = window.$dialog?.show({
    main: `确定要将用户【${userInfo.nickname}】绑定至当前账号么`,
    title: '提醒',
    confirmText: '确定',
    cancelText: '取消',
    confirm: () => {
      console.log("确认绑定")
      bindDialog?.close()
    }
  })
}

onMounted(() => {
  fetchRooms()
  // routerPush(`/apps/chat-channel/${channels.value[0]}`)
  // fetchOnlineUsers()
  fetchUserPlayList()

})

const enterRoom = (roomInfo: ApiRoomManagement.roomInfo) => {
  return { name: 'apps_chat-channel', query: { roomNo: roomInfo.roomNo, roomName: roomInfo.roomName } }
}

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

const fetchUserPlayList = async () => {
  if (!userInfo.value.wyyUserId) return
  window.$loadingOverly?.show()
  const resp = await fetchPlayList(userInfo.value.wyyUserId);
  window.$loadingOverly?.hide()
  if (resp.data) {
    //通过subscribed区分是我的歌单还是收藏歌单
    //我的歌单
    const myPlayListItemList = resp.data.filter((playList: ApiMusic.playListInfo) => !playList.subscribed)
    const myPlayListItems = handlePlayList(myPlayListItemList)
    const myplayListCount = myPlayListItems.length
    const myPlayListSubheaderTitle = `我的歌单(${myplayListCount}个)`
    mySubheaderTitle.value = myPlayListSubheaderTitle
    myPlayList.value = myPlayListItems
    //收藏歌单
    const collectPlayListItemList = resp.data.filter((playList: ApiMusic.playListInfo) => playList.subscribed)
    const collectPlayListItems = handlePlayList(collectPlayListItemList)
    const collectPlayListCount = collectPlayListItems.length
    const collectPlayListSubheaderTitle = `收藏歌单(${collectPlayListCount}个)`
    collectSubheaderTitle.value = collectPlayListSubheaderTitle
    collectPlayList.value = collectPlayListItems
    isUserBindWyy.value = true
  }
}

const handlePlayList = (playListItems: ApiMusic.playListInfo[]) => {
  return playListItems.map((playList: ApiMusic.playListInfo) => {
    return {
      id: playList.id,
      prependAvatar: playList.coverImgUrl,
      title: playList.name,
      subtitle: `${playList.trackCount}首`,
    }
  })
}

const currentOffSet = ref(0)
const hasMore = ref(true)

const fetchMusicList = async (playListId: string) => {
  const resp = await fetchPlayListAllMusic(playListId, 200, currentOffSet.value);
  window.$loadingOverly?.hide()
  if (resp.data) {
    if (!resp.data || resp.data.length === 0) {
      hasMore.value = false
      if (!currentPlayListAllMusic.value) currentPlayListAllMusic.value = []
      currentPlayListAllMusic.value.push(...[])
      return false;
    }
    if (!currentPlayListAllMusic.value) currentPlayListAllMusic.value = []
    currentPlayListAllMusic.value.push(...resp.data)
    isPlayListDetailPage.value = true
    currentOffSet.value += 200
  }
  return true
}

const enterPlayList = async (item) => {
  // console.log(el)
  console.log("进入歌单：" + item.id)
  currentPlayList.value = item
  window.$loadingOverly?.show()
  fetchMusicList(item.id)
}

const handleMusicSubtitle = (musicInfo: ApiMusic.playListMusicInfo) => {
  //处理作者
  const artistNameStr = musicInfo.ar.map((artistInfo: ApiMusic.artistInfo) => artistInfo.name).join('/');
  return artistNameStr + ' - ' + musicInfo.al.name
}

const onDemandMusic = (musicInfo: ApiMusic.playListMusicInfo) => {
  console.log(musicInfo.id)
  currentWishOnDemandMusic.value = musicInfo
  dialog.value = true
}

const confirmOndemandMusic = () => {
  if (!currentWishOnDemandMusic) return
  Bus.emit('on-demand-music', currentWishOnDemandMusic.value)
  dialog.value = false
}

const musicListScrollBottmHandle = async ({ done }) => {
  if (!currentPlayList.value) {
    console.log('触底了')
    done('ok')
    return
  }
  if (hasMore.value) {
    console.log('触底了')
    const noEmpty = await fetchMusicList(currentPlayList.value?.id)
    noEmpty ? done('ok') : done('empty')
    return
  }
}

const closePlayListDetailPage = () => {
  currentPlayListAllMusic.value = []
  currentOffSet.value = 0
  isPlayListDetailPage.value = false
  hasMore.value = true
}

</script>

<style lang="scss" scoped>
.listArea {
  position: absolute;
  top: 50px;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.playListDiv {
  flex-grow: 1;
  overflow-y: scroll;
  -webkit-overflow-scrolling: touch;
  min-height: 0;
}

.playlist :deep(.v-avatar) {
  border-radius: 20%;
}

.playlist :deep(.v-avatar.v-avatar--size-default) {
  --v-avatar-height: 50px;
}

.musicList {
  left: -15px;
}

.musicList :deep(.v-list-item__prepend) {
  margin-right: 15px;
  font-size: 20px;
}

.musicList :deep(.v-list-item-subtitle) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: block
}

.subheader :deep(.v-list-item--density-default.v-list-item--one-line) {
  min-height: 24px;

  .v-list-item-title {
    font-size: small;
    opacity: var(--v-medium-emphasis-opacity)
  }
}
</style>
