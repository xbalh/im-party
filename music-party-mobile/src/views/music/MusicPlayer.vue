<template>
  <transition leave-active-class="animate__slideOutDown">
    <v-app class="music-page animate__animated animate__slideInUp" v-show="display">
      <v-main>
        <div>
          <div style="height: 80%;">
            <v-btn @click="closeMusicPage" variant="plain" icon="mdi-chevron-down">
            </v-btn>
            {{ currentSong?.songName }} - {{ currentSong?.singer }}
          </div>
          <div style="height: 20%;" class="g-glossy">
            <div>
              {{ currentProgress }}/{{ totalProgress }}
              <audio ref="player" content="never" @timeupdate="timeupdate"></audio>
              <v-progress-linear v-model="progress" bg-color="blue-grey" color="lime"></v-progress-linear>
            </div>
            <div>
              <v-btn icon="mdi-play" variant="plain" @click="playOrPauseMusic" size="x-large">
                <v-icon>{{ isPlay ? 'mdi-pause' : 'mdi-play' }}</v-icon>
              </v-btn>
            </div>
          </div>
        </div>
      </v-main>
    </v-app>
  </transition>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import Bus from '@/utils/common/Bus';

const musicList = ref()
const display = ref(false)
const disablePlay = ref(true)
const player = ref<HTMLAudioElement>()
const currentSong = ref<Music.SongInfo>()
const progress = ref(0)
const currentProgress = ref('--')
const totalProgress = ref('--')
//TODO 从本地存储获取设置：是否自动开始播放
const isAutoPlay = ref(true)
Bus.on('openMusicPage', (flag: boolean) => {
  display.value = flag
})

const isPlay = ref(false)
const playOrPauseMusic = () => {
  if (isPlay.value) {
    console.log("暂停播放")
    player.value!.pause()
    Bus.emit('music-play', false)
  } else {
    console.log("开始播放")
    if (!player.value?.src) {
      return
    }
    player.value!.play()
    Bus.emit('music-play', true)
  }
  isPlay.value = !isPlay.value
}

const timeupdate = () => {
  const currentTime = player.value!.currentTime
  // console.log(currentTime)
  const duration = player.value!.duration
  try {
    currentProgress.value = musicTimeformat(currentTime)
    totalProgress.value = musicTimeformat(duration)
    const current = currentTime / duration * 100
    progress.value = parseInt(String(current))
    Bus.emit('music-process-update', parseInt(String(current)))
    if (progress.value === 100) {
      isPlay.value = false
      Bus.emit('music-play', false)
      disablePlay.value = true
      setTimeout(function () {
        if (progress.value === 100) {
          currentSong.value = {}
          Bus.emit('clear-currentMusicInfo', true)
        }
      }, 1000)
    }
  } catch (e) { }

}

const musicTimeformat = (value: number) => {
  let interval = Math.floor(value)
  let minute = (Math.floor(interval / 60)).toString().padStart(2, '0')
  let second = (interval % 60).toString().padStart(2, '0')
  return `${minute}:${second}`
}

Bus.on('request-music-play', (flag: boolean) => {
  if (flag) {
    if (disablePlay || !player || !player.value!.src) {
      return
    }
    player.value!.play()
    Bus.emit('music-play', true)
    isPlay.value = true
  } else {
    player.value!.pause()
    Bus.emit('music-play', false)
    isPlay.value = false
  }
})

const closeMusicPage = () => {
  display.value = false
  Bus.emit("isMusicPage", false)
}

Bus.on('change-song', (songInfo: Music.SongInfo) => {
  currentSong.value = songInfo
  const currentPlayer = player.value!
  currentPlayer.setAttribute('src', songInfo.url!)
  if (isAutoPlay) {
    currentPlayer.currentTime = songInfo.type === 'next-play'? 0 : songInfo.nowTime! / 1000
    currentPlayer.play()
    isPlay.value = true
    Bus.emit('music-play', true)
  } else if (isPlay.value) {
    currentPlayer.play()
    Bus.emit('music-play', true)
  }


})

Bus.on('change-room', (flag: boolean)=>{
  progress.value = 0
  currentSong.value = {}
  isPlay.value = false
  player.value?.pause()
})



</script>

<style lang="scss" scoped>
.music-page {
  position: absolute !important;
  width: 100%;
  height: 100%;
  z-index: 1999 !important;
  // position: absolute;
  // top: 0px;
  // bottom: 0;
  // left: 0;
  // right: 0;
  // width: 100%;
  // display: flex;
  // flex-direction: column;
  background-image: linear-gradient(135deg, #FEB692 10%, #EA5455 100%);
  // -webkit-filter: blur(3px);
  // -moz-filter: blur(3px);
  // -o-filter: blur(3px);
  // filter: blur(3px);
}

.g-glossy {
  position: relative;
  width: 600px;
  height: 300px;
  background-color: rgba(255, 255, 255, 0.5);
  overflow: hidden;
  z-index: 10;

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: linear-gradient(135deg, #FEB692 10%, #EA5455 100%);
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-size: cover;
    filter: blur(10px);
    z-index: -1;
  }
}

.animate__animated.animate__slideInUp {
  --animate-duration: 0.3s;
}
</style>
