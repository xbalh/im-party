<template>
  <div>
    <!-- 聊天室 -->
    <div>
      <!-- 隐藏到右侧 -->

      <v-btn theme="dark" ref="chatBtnDot" icon="mdi-forum-outline" class="drawer-button" color="#55BB46"
        v-show="isMusicPage && dragDotRight" @click="dragDotRight = false">
        <v-badge dot offset-x="15" offset-y="-10" color="red" v-show="currentUnReadCount > 0">
          <v-icon class="fa-spin">mdi-forum-outline</v-icon>
        </v-badge>
        <v-icon class="fa-spin">mdi-forum-outline</v-icon>
      </v-btn>
      <!-- 完全显示 -->
      <transition leave-active-class="animate__fadeOutRight">
        <div class="drawer-button1 animate__animated animate__fadeInRight" ref="chatBtn" v-dialogDrag="true"
          v-dialogDrag::click="toChatPage">
          <v-btn theme="dark" icon="mdi-forum-outline" color="#55BB46" v-show="isMusicPage && !dragDotRight">
            <v-badge :content="handleBadge(currentUnReadCount)" offset-x="-10" offset-y="-10" color="red"
              v-show="currentUnReadCount > 0">
              <v-icon class="fa-spin">mdi-forum-outline</v-icon>
            </v-badge>
            <v-icon class="fa-spin">mdi-forum-outline</v-icon>
          </v-btn>
        </div>
      </transition>
    </div>
    <!-- 音乐播放 -->
    <div>
      <!-- 隐藏到右侧 -->
      <div ref="musicBtnDot" v-show="!isMusicPage && dragDotRight" class="music-button">
        <v-btn theme="dark" icon="mdi-music" color="#ec4444" @click="dragDotRight = false">
          <v-icon class="fa-spin">mdi-music</v-icon>
        </v-btn>
      </div>
      <!-- 完全显示 -->
      <transition leave-active-class="animate__fadeOutRight">
        <div class="music-button1 animate__animated animate__fadeInRight" ref="musicBtn" v-dialogDrag="true"
          v-dialogDrag::click="showFooter" v-show="isShow">
          <v-progress-circular v-model="currentProgress" class="me-2" size="60" width="3"
            v-show="!isMusicPage && !dragDotRight">
            <v-btn theme="dark" ref="refButton2" icon="mdi-music" color="#ec4444" v-show="!dragDotRight">
              <v-icon class="fa-spin">mdi-music</v-icon>
            </v-btn>
          </v-progress-circular>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import Bus from "@/utils/common/Bus";

const chatBtn = ref<HTMLButtonElement>()

const chatBtnDot = ref<HTMLButtonElement>()

const musicBtn = ref<HTMLButtonElement>()

const musicBtnDot = ref<HTMLButtonElement>()

const currentProgress = ref(99)
Bus.on('music-process-update', (progress: number) => {
  currentProgress.value = progress
})

const isMusicPage = ref(false)
Bus.on('isMusicPage', (flag: boolean) => {
  isMusicPage.value = flag
})

const isShow = ref(false)
Bus.on('rightCircleBtnShow', (flag: boolean) => {
  isShow.value = flag
})

Bus.on('openMusicPage', (flag: boolean) => {
  isMusicPage.value = true
})
const currentUnReadCount = ref(0)
Bus.on('unread-add', () => {
  currentUnReadCount.value++
})

const handleBadge = (count: number) => {
  if (count > 0) {
    if (count < 99) {
      return String(count)
    } else {
      return '99+'
    }
  }
  return ''
}



watch(isMusicPage, (newValue, oldValue) => {
  if (newValue) {
    //为true：音乐按钮隐藏，聊天按钮展示，把音乐按钮位置赋给聊天按钮
    const top = musicBtn.value!.style.top
    const left = musicBtn.value!.style.left
    chatBtn.value!.style.cssText += `;left:${left};top:${top};`
  } else {
    //为false：聊天按钮隐藏，音乐按钮展示，把聊天按钮位置赋给音乐按钮
    const top = chatBtn.value!.style.top
    const left = chatBtn.value!.style.left
    musicBtn.value!.style.cssText += `;left:${left};top:${top};`

  }

  // console.log('值发生了变更', newValue, oldValue);
});

// watch(router.currentRoute, (newValue, oldValue) => {
//   console.log("新路由：" + newValue + "旧路由：" + oldValue)
//   if (newValue.name === 'apps_chat-channel') {
//     isShow.value = true
//   } else {
//     isShow.value = false
//   }
// });

onBeforeMount(() => {
  // if (router.currentRoute.value.name === 'apps_chat-channel') {
  //   isShow.value = true
  // }

})

const dragDotRight = ref(false)
Bus.on('dragDotRight', (flag: boolean) => {
  const top = musicBtn.value!.style.top
  musicBtnDot.value!.style.cssText += `;top:${top};`
  dragDotRight.value = flag
})

const showFooter = () => {
  // console.log("去音乐播放页面")
  // isMusicPage.value = true
  isShow.value = false
  Bus.emit("showFooter", true)
  // routerPush('/special/music')
  //展示

}

const toChatPage = () => {
  // console.log("去聊天页面")
  isMusicPage.value = false
  Bus.emit("openMusicPage", false)
  // routerPush('/apps/chat-channel/:id')
  //隐藏
}

// // 拖拽开始事件  
// const dragstart = (e: any) => {
//   // 记录拖拽元素初始位置

// };
// // 拖拽完成事件
// const dragend = (e: any) => {
//   console.log("eeeeee", e);
//   let btn = document.getElementById("my_btn") as HTMLElement
//   btn!.style.left = e.clientX + 'px';
//   btn!.style.top = e.clientY + 'px';
// }

onMounted(() => {
  // execAnimate()
  // const dotHeight = chatBtnDot.value!.offsetHeight
  // chatBtnDot.value!.style.cssText += `;width:${dotHeight}px;` 
})
// onBeforeUnmount(() => {
//   if (timeout) clearTimeout(timeout)
// })

</script>

<style lang="scss" scoped>
.drawer-button {
  width: 60px;
  position: fixed;
  top: 340px;
  right: -20px;
  z-index: 2000;
  box-shadow: 1px 1px 18px #55BB46;
  opacity: 75%;

  .v-icon {
    margin-left: -18px;
    font-size: 1.3rem;
  }
}

.drawer-button1 {
  width: 60px;
  position: fixed;
  top: 340px;
  right: 35px;
  z-index: 2000;
  // opacity: 75%;

  .v-btn {
    box-shadow: 1px 1px 18px #55BB46;
  }

  .v-icon {
    // margin-left: -18px;
    font-size: 1.3rem;
  }

  .fa-spin {
    // animation: spin 2s infinite linear;
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }

    to {
      transform: rotate(360deg);
    }
  }
}

.music-button {
  right: -20px;
  z-index: 2000;
  position: fixed;
  top: 340px;

  .v-btn {
    box-shadow: 1px 1px 18px #ec4444;
    opacity: 75%;
  }

  .v-icon {
    margin-left: -18px;
    font-size: 1.3rem;
  }
}

.music-button1 {
  width: 60px;
  position: fixed;
  top: 340px;
  right: 35px;
  z-index: 2000;

  // border: 1px solid;
  .v-btn {
    box-shadow: 1px 1px 18px #ec4444;
  }

  .v-icon {
    font-size: 1.3rem;
  }
}

.progress {
  position: fixed;
  top: 340px;
  right: 35px;
  z-index: 9999;
}

.animate__animated.animate__fadeInRight,
.animate__animated.animate__fadeOutRight {
  --animate-duration: 0.3s;
}
</style>
