<template>
  <!-- Navigation -->
  <v-navigation-drawer v-model="drawer" floating name="app-navigation" :theme="theme.menuTheme" class="elevation-1"
    touchless>

    <!-- Navigation menu info -->
    <div class="pa-2">
      <div class="title font-weight-bold text-uppercase text-primary">{{ name }}</div>
      <div class="overline text-grey">{{ version }}</div>
    </div>


    <div class="mx-auto text-center">
      <v-avatar color="grey">
        <v-img :src="userInfo.userAvatarUrl"></v-img>
      </v-avatar>
      <h3>{{ userInfo.nickName }}</h3>
      <p class="text-caption mt-1">
        {{ userInfo.userName }}
      </p>
    </div>

    <!-- Navigation menu -->
    <div class="py-1">
      <main-menu :menu="menus" />
    </div>

    <!-- Navigation menu footer -->
    <template v-slot:append>
      <!-- Footer navigation links -->
      <!-- <div class="py-2 text-center">
        <v-btn size="small"
               :href="'https://next.vuetifyjs.com/en/'"
               flat
        >
          {{ $t('menu.docs') }}
        </v-btn>
      </div> -->

    </template>
  </v-navigation-drawer>

  <!-- 设置页面 -->
  <side-config-menu />

  <!-- 播放页面 -->

  <!-- Toolbar -->
  <v-app-bar class="overflow-visible px-2" :theme="theme.toolbarTheme === 'global' ? undefined : theme.toolbarTheme"
    :flat="theme.isToolbarDetached" :color="theme.isToolbarDetached ? 'background' : undefined">
    <v-card class="flex-grow-1 d-flex" :class="[theme.isToolbarDetached ? 'pa-1 mt-3 mx-1' : 'pa-0 ma-0']"
      :flat="!theme.isToolbarDetached">
      <div class="d-flex flex-grow-1 align-center">
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
        <!-- <audio controls ></audio> -->
        <v-spacer class="d-none d-lg-block" />
        <v-autocomplete placeholder="搜索歌曲" prepend-inner-icon="mdi-magnify" hide-details :items="searchResult"
          item-title="meta.title" item-value="path" clearable @update:modelValue="searchSelect" variant="filled"
          density="comfortable" class="v-text-field-rounded" single-line>
        </v-autocomplete>
        <v-spacer class="d-none" />
        <!-- <toolbar-language /> -->
        <div class="mr-1">
          <toolbar-notifications />
        </div>
        <div class="mr-1">
          <toolbar-user />
        </div>

      </div>
    </v-card>
  </v-app-bar>

  <v-main style="height: 100% !important;">
    <loading-progress-provider>
      <v-container :fluid="!theme.isContentBoxed" class="h-100 position-relative">
        <router-view v-slot="{ Component }" :key="$route.fullPath">
          <v-fade-transition mode="out-in">
            <component :is="Component" />
          </v-fade-transition>
        </router-view>
      </v-container>
    </loading-progress-provider>
  </v-main>

  <transition leave-active-class="animate__slideOutDown">
    <v-footer class="customFooter animate__animated animate__slideInUp" v-if="isShowFooter">
      <div class="bottomDiv">
        <div class="playerBar" @click="toMusicPage">
          <div class="avatar" ref="avatarDiv">
            <v-avatar ref="avatar" color="surface-variant" :size="avatarSize" :class="isPlay ? 'avatar-spin' : ''">
              <v-img class="musicImg" :src="currentPlayMusicInfo?.songDetailInfo?.al.picUrl"></v-img>
            </v-avatar>
          </div>
          <div class="musicContent">
            <span>{{ currentPlayMusicInfo?.songName }}</span>
            <span>
              {{ currentPlayMusicInfo && '' }}
            </span>
            <span>
              {{ currentPlayMusicInfo && handleArtistName(currentPlayMusicInfo) }}
            </span>
          </div>
          <div class="playbarBtnGroup">
            <!-- 播放按钮 -->
            <v-progress-circular v-model="currentProgress" class="me-2 progress" size="30" width="2">
              <v-btn icon="mdi-play" class="playBtn" variant="plain" @click.stop="playOrPauseMusic" size="50">
                <v-icon>{{ isPlay ? 'mdi-pause' : 'mdi-play' }}</v-icon>
              </v-btn>
            </v-progress-circular>
            <!-- 展示播放列表按钮 -->
            <v-menu transition="fab-transition">
              <template v-slot:activator="{ props }">
                <v-btn variant="plain" size="x-large" class="playList" v-bind="props">
                  <v-icon size="x-large">mdi-playlist-play</v-icon>
                </v-btn>
              </template>
              <v-card>
                <v-slide-y-transition key="playList" group tag="div">
                  <v-list v-model="playList" width="350" class="playlist" max-height="400">
                    <v-list-subheader>当前歌曲列表({{ playList ? playList.length : 0 }})</v-list-subheader>
                    <v-list-item v-if="playList?.length === 0">
                      <span>当前歌曲列表为空，快去点歌吧！</span>
                    </v-list-item>
                    <v-list-item v-for="(songInfo, index) in playList" :key="songInfo.songId" lines="two"
                      :title="`${songInfo.songName} - ${songInfo.singer}`" :subtitle="`点歌人：${songInfo.from}`"
                      :class="index === 0 ? 'hightlight_grey' : ''">
                      <template v-slot:prepend>
                        <span>{{ index + 1 }}</span>
                      </template>
                      <template v-slot:append v-if="index === 0">
                        <v-icon icon="mdi-signal-cellular-3" color="red"></v-icon>
                      </template>
                    </v-list-item>
                  </v-list>
                </v-slide-y-transition>
              </v-card>
            </v-menu>
          </div>
        </div>
        <v-divider />
        <div class="myselfBar">
          <v-bottom-navigation v-model="navCurrent" elevation="0" grow horizontal color="primary" class="bottomNav">
            <v-btn>
              <v-icon>mdi-music-box-outline</v-icon>
              <span>我的</span>
            </v-btn>
            <v-btn>
              <v-icon>mdi-chat-outline</v-icon>
              <span>聊天</span>
            </v-btn>
            <v-btn @click="hideFooter">
              <v-icon>mdi-arrow-down-drop-circle-outline</v-icon>
              <span>隐藏</span>
            </v-btn>
          </v-bottom-navigation>
        </div>
      </div>
    </v-footer>
  </transition>
</template>

<script setup lang="ts">

import LoadingProgressProvider from "@/components/provider/LoadingProgressLine";
import { computed } from 'vue'
import { useAppInfo, useRouterPush } from "@/composables";
import Bus from "@/utils/common/Bus";
import { useResizeObserver } from "@vueuse/core";
import { useAuthStore } from "@/store";
const { userInfo } = useAuthStore();
const searchResult = ref()
const theme = useThemeStore()
const drawer = ref()
const routeStore = useRouteStore();
const menus = computed(() => routeStore.menus as App.GlobalMenuOption[]);
const { name, version } = useAppInfo();
const push = useRouterPush()
const searchSelect = (item: AuthRoute.Route) => {
  if (item)
    push.routerPush(item)
}

const currentPlayMusicInfo = ref<Music.SongInfo>()
Bus.on('clear-currentMusicInfo', (flag: boolean) => {
  currentPlayMusicInfo.value = {}
  currentProgress.value = 0
  isPlay.value = false
})

const currentProgress = ref(0)
Bus.on('music-process-update', (progress: number) => {
  currentProgress.value = progress
})

const navMenu = {
  0: 'myself',
  1: 'chat'
}

const navCurrent = ref(1)
watch(navCurrent, (newValue, oldValue) => {
  if (newValue === 2 || newValue === undefined) {
    navCurrent.value = oldValue
    return
  }
  Bus.emit('bottom-nav-change', navMenu[newValue])
})

const isPlay = ref(false)

const isShowFooter = ref(false)
const hideFooter = () => {
  isShowFooter.value = false
  Bus.emit('rightCircleBtnShow', true)
}

Bus.on('showFooter', (flag: boolean) => {
  isShowFooter.value = flag
})

const toMusicPage = () => {
  Bus.emit('openMusicPage', true)
}

const playOrPauseMusic = () => {
  if (isPlay.value) {
    console.log("暂停播放")
    Bus.emit('request-music-play', false)
  } else {
    console.log("开始播放")
    Bus.emit('request-music-play', true)
  }
}


Bus.on('change-room', (flag: boolean) => {
  playList.value = []
  currentProgress.value = 0
  isPlay.value = false
  currentPlayMusicInfo.value = {}
})

const playList = ref<Music.SongInfo[]>()
Bus.on('playlist-change', (songList: Music.SongInfo[]) => {
  playList.value = songList
  if (!songList || songList.length === 0) {
    currentPlayMusicInfo.value = {}
  }
})

const avatar = ref<import('vuetify/components').VAvatar>()
const avatarDiv = ref<HTMLDivElement>()
const avatarSize = ref(30)

useResizeObserver(avatarDiv, (entries) => {
  const entry = entries[0]
  const { height } = entry.contentRect
  avatarDiv.value!.style.cssText += `;width: ${height}px;`
  avatarSize.value = height - 15
})



onMounted(() => {
  if (router.currentRoute.value.name === 'apps_chat-channel') {
    isShowFooter.value = true
  }
  const height = avatarDiv.value?.offsetHeight
  if (height) {
    avatarDiv.value!.style.cssText += `;width: ${height}px;`
    avatarSize.value = height - 15
  }
})

Bus.on('change-song', (songInfo: Music.SongInfo) => {
  currentPlayMusicInfo.value = songInfo
})

const handleArtistName = (songInfo: Music.SongInfo) => {
  return songInfo.songDetailInfo?.ar.map((artistInfo: ApiMusic.artistInfo) => artistInfo.name).join('/');
}

Bus.on('music-play', (flag: boolean) => {
  isPlay.value = flag
})

watch(router.currentRoute, (newValue, oldValue) => {
  console.log("新路由：" + newValue + "旧路由：" + oldValue)
  if (newValue.name === 'apps_chat-channel') {
    isShowFooter.value = true
  } else {
    isShowFooter.value = false
  }
});

</script>

<style scoped>
.v-text-field-rounded :deep(.v-field__input) {
  flex-direction: column;
  justify-content: center;
}

.v-footer {
  padding: 0;
}

.avatar {
  /* top: 5px; */
  margin-left: 3%;
  border-radius: 50%;
  height: 80%;
  background-color: black;
  display: flex;
  align-items: center;
  justify-content: center;

}

.avatar-spin {
  animation: spin 10s infinite linear;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}



.playbarBtnGroup {
  margin-left: auto;
  height: 100%;
  display: flex;
  flex: 1;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.progress {
  /* margin-left: auto; */
  z-index: 777;
}

.playBtn {
  /* top: 0;
  right: -1px; */
}

.playList {
  width: 40px;
  /* margin-left: auto; */
}

.playList.v-btn--size-x-large {
  min-width: 30px;
}

.customFooter {
  position: absolute;
  z-index: 999;
  bottom: 0;
  width: 100%;
  height: 15%;
}

.bottomDiv {
  /* justify-content: center;
  align-items: center; */
  height: 100%;
  width: 100%;
}

.playerBar {
  height: 50%;
  overflow: hidden !important;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.myselfBar {
  height: 50%;
  overflow: hidden !important;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.musicContent {
  width: 55%;
  margin-left: 3%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.bottomNav {
  height: 100% !important;
  width: 100% !important;
  position: relative !important;
}

.playlist :deep(.v-list-item__prepend) {
  margin-right: 15px;
  font-size: 20px;
}


.animate__animated.animate__slideInUp,
.animate__animated.animate__slideOutDown {
  --animate-duration: 0.3s;
}

.hightlight_grey {
  background-color: rgb(230, 230, 230);
}
</style>
