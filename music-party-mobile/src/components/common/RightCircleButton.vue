<template>
  <div>
    <!-- 聊天室 -->
    <div>
      <!-- 隐藏到右侧 -->
      <v-btn theme="dark" ref="chatBtnDot" icon="mdi-forum-outline" class="drawer-button" color="#55BB46"
        v-show="isMusicPage && dragDotRight" @click="dragDotRight = false">
        <v-badge dot offset-x="15" offset-y="-10" color="red">
          <v-icon class="fa-spin">mdi-forum-outline</v-icon>
        </v-badge>
      </v-btn>
      <!-- 完全显示 -->
      <div class="drawer-button1" ref="chatBtn" v-dialogDrag="true" v-dialogDrag::click="toChatPage">
        <v-btn theme="dark" icon="mdi-forum-outline" color="#55BB46"
          v-show="isMusicPage && !dragDotRight" >
          <v-badge content="99+" offset-x="-10" offset-y="-10" color="red">
            <v-icon class="fa-spin">mdi-forum-outline</v-icon>
          </v-badge>
        </v-btn>
      </div>
    </div>
    <!-- 音乐播放 -->
    <div>
      <!-- 隐藏到右侧 -->
      <v-btn theme="dark" ref="musicBtnDot" icon="mdi-music" class="music-button" color="#ec4444"
        v-if="!isMusicPage && dragDotRight" @click="dragDotRight = false">
        <v-icon class="fa-spin">mdi-music</v-icon>
      </v-btn>
      <!-- 完全显示 -->
      <div class="music-button1" ref="musicBtn" v-dialogDrag="true" v-dialogDrag::click="toMusicPage">
        <v-progress-circular v-model="progress" class="me-2" size="60" width="3" v-show="!isMusicPage && !dragDotRight">
          <v-btn theme="dark" ref="refButton2" icon="mdi-music" color="#ec4444" v-show="!dragDotRight">
            <v-icon class="fa-spin">mdi-music</v-icon>
          </v-btn>
        </v-progress-circular>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import Bus from "@/utils/common/Bus";
import { useRouterPush } from "@/composables";

const chatBtn = ref<HTMLElement>()

const musicBtn = ref<HTMLElement>()

const isMusicPage = ref(false)

const { routerPush } = useRouterPush(false)

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



const dragDotRight = ref(false)
Bus.on('dragDotRight', (flag: boolean) => {
  dragDotRight.value = flag
})

const progress = ref<number>(50)

const toMusicPage = () => {
  console.log("去音乐播放页面")
  isMusicPage.value = true
  // Bus.emit("toMusicPage", true)
  routerPush('/special/music')
}

const toChatPage = () => {
  console.log("去聊天页面")
  isMusicPage.value = false
  Bus.emit("toMusicPage", false)
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
  z-index: 9999;
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
  z-index: 9999;
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
  position: fixed;
  top: 340px;
  right: -20px;
  z-index: 999;
  box-shadow: 1px 1px 18px #ec4444;
  opacity: 75%;

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
  z-index: 9999;

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
</style>
