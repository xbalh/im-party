<template>
  <!-- Navigation -->
  <v-navigation-drawer v-model="drawer" floating name="app-navigation" :theme="theme.menuTheme" class="elevation-1"
    touchless>

    <!-- Navigation menu info -->
    <div class="pa-2">
      <div class="title font-weight-bold text-uppercase text-primary">{{ name }}</div>
      <div class="overline text-grey">{{ version }}</div>
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
        <v-autocomplete :placeholder="$t('menu.search')" prepend-inner-icon="mdi-magnify" hide-details
          :items="routeStore.searchMenus" item-title="meta.title" item-value="path" clearable
          @update:modelValue="searchSelect" variant="filled" density="comfortable" class="v-text-field-rounded"
          single-line>
        </v-autocomplete>
        <v-spacer class="d-none" />
        <toolbar-language />
        <div class="mr-1">
          <toolbar-notifications />
        </div>
        <toolbar-user />
      </div>
    </v-card>
  </v-app-bar>

  <v-main>
    <loading-progress-provider>
      <v-container :fluid="!theme.isContentBoxed" class="h-100 position-relative">
        <router-view v-slot="{ Component }">
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
          <div class="avatar">
            <v-avatar color="surface-variant" size="50">
              <v-img class="musicImg"
                src="http://p2.music.126.net/nuX_gG3J-e7B2uzXrALTwQ==/109951168185440037.jpg?param=130y130"></v-img>
            </v-avatar>
          </div>
          <div class="musicContent">
            <span>测试文本测试文本测试文本测试文本测试文本</span>
          </div>
          <div class="playbarBtnGroup">
            <v-progress-circular v-model="currentProgress" class="me-2 progress" size="30" width="2">
              <v-btn icon="mdi-play" class="playBtn" variant="plain" @click.stop="playOrPauseMusic" size="50">
                <v-icon>{{ isPlay ? 'mdi-pause' : 'mdi-play' }}</v-icon>
              </v-btn>
            </v-progress-circular>
            <v-btn variant="plain" size="x-large" @click.stop="openPlayList" class="playList">
              <v-icon size="x-large">mdi-playlist-play</v-icon>
            </v-btn>
          </div>
        </div>
        <v-divider />
        <div class="myselfBar">
          <div>
            <v-bottom-navigation v-model="navCurrent" elevation="0" grow horizontal color="primary">
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
      </div>
    </v-footer>
  </transition>
</template>

<script setup lang="ts">

import LoadingProgressProvider from "@/components/provider/LoadingProgressLine";
import { computed } from 'vue'
import { useAppInfo, useRouterPush } from "@/composables";
import Bus from "../utils/common/Bus";

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

const currentProgress = ref(0)
Bus.on('music-process-update', (progress: number)=>{
  currentProgress.value = progress
})

const navCurrent = ref(1)
watch(navCurrent, (newValue, oldValue)=>{
  if(newValue === 2){
    navCurrent.value = oldValue
  }
})

const isPlay = ref(false)

const isShowFooter = ref(true)
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
    Bus.emit('music-play', false)
  } else {
    console.log("开始播放")
    Bus.emit('music-play', true)
  }
  isPlay.value = !isPlay.value
}

const openPlayList = () => {

}

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
}

.playbarBtnGroup {
  margin-left: auto;
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


.animate__animated.animate__slideInUp,
.animate__animated.animate__slideOutDown {
  --animate-duration: 0.3s;
}
</style>
