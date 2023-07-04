<template>
  <v-layout full-height>
    <div class="d-flex flex-grow-1 flex-row" style="display: flex;">
      <div class="channel-page">
        <div style="height: 80%;">

        </div>
        <div style="height: 20%;" class="g-glossy">
          <div>
            <v-progress-linear model-value="30" bg-color="blue-grey" color="lime"></v-progress-linear>
          </div>
          <div>
            <v-btn>
              <v-icon size="x-large" icon="mdi-playlist-music" />
            </v-btn>
          </div>

        </div>

      </div>

    </div>
  </v-layout>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

const musicList = ref()

</script>

<style lang="scss" scoped>
.channel-page-bg {
  background: url("/images/chat/chat-bg-2.png");
}

.channel-page {
  position: absolute;
  top: 0px;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  display: flex;
  flex-direction: column;
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
</style>
