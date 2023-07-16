<template>
  <transition leave-active-class="animate__slideOutDown">
    <v-app class="music-page animate__animated animate__slideInUp" v-show="display">
      <v-main>
        <div>
          <div style="height: 80%;">
            <v-btn @click="closeMusicPage" variant="plain" icon="mdi-chevron-down">
            </v-btn>
            {{ currentSong?.songName }}/{{ currentSong?.singer }}
          </div>
          <div style="height: 20%;" class="g-glossy">
            <div>
              <audio ref="player" content="never" @timeupdate="timeupdate"
                src="http://m701.music.126.net/20230716135435/33fca99b7aad2303df47ecd929d4d542/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/22259947907/f0fb/a2c1/e86c/9092405ea985a394a4af69d32dd32fc9.flac"></audio>
              <v-progress-linear model-value="30" bg-color="blue-grey" color="lime"></v-progress-linear>
            </div>
            <div>
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
const player = ref<HTMLAudioElement>()
const currentSong = ref<Music.SongInfo>()
Bus.on('openMusicPage', (flag: boolean) => {
  display.value = flag
})

const timeupdate = () => {
  const currentTime = player.value!.currentTime
  const duration = player.value!.duration
  try {
    const currentProgress = currentTime / duration * 100
    Bus.emit('music-process-update', parseInt(String(currentProgress)))
  } catch (e) { }

}

Bus.on('music-play', (flag: boolean) => {
  if (flag) {
    player.value!.play()
  } else {
    player.value!.pause()
  }
})

const closeMusicPage = () => {
  display.value = false
  Bus.emit("isMusicPage", false)
}

Bus.on('changeSong', (songInfo: Music.SongInfo) => {
  currentSong.value = songInfo
  const currentPlayer = player.value!
  currentPlayer.setAttribute('src', songInfo.url)
  currentPlayer.play()
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
