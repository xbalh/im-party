<template>
  <div class="home">
    <!-- 当前在线用户 -->

    <!-- 聊天区域 -->

    <!-- 播放器 -->
    <div>
      <MusicPlayer @currentTime="currentTime"></MusicPlayer>
    </div>>
    <!-- 歌曲队列 -->





<div>
  <el-button @click="setCurrentTime">
    控制当前播放进度
  </el-button>
</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import RequestExample from "@/components/example/requestExample.vue";
import LangExample from "@/components/example/langExample.vue";
import VuexExample from "@/components/example/vuexExample.vue";
import WorkerExample from "@/components/example/workerExample.vue";
import MusicPlayer from "@/components/music-player/index.vue";
import defaultPageApi from "@/api/default-page"
import Request from "@/utils/requestInstance";
import Ws from "@/utils/ws";
import { namespace } from "vuex-class"

const userStore = namespace('userStore')

@Component({
  components: {
    RequestExample,
    LangExample,
    VuexExample,
    WorkerExample,
    MusicPlayer
  }
})
export default class Home extends Vue {
  WS: Ws;
  handleChat: (data: object) => any;
  timeId: NodeJS.Timeout;
  @userStore.Getter('getToken') getToken!: string

  currentTime: string = "00:00";

  constructor() {
    super();
    const token = localStorage.getItem("token");
    if(!token) console.error("token为空");
    this.WS = new Ws("ws://localhost:8080/musicParty/ws", [token!], false);
    this.WS.send("connect success");
    this.handleChat = (data: object) => console.log(data);
    this.WS.subscribe("/music/chat", this.handleChat);

    let count = 0;
    this.timeId = setInterval(() => {
      ++count;
      this.WS.send(`count: ${count}`);
    }, 10000);

  }

  created() {
    // ws初始化
    this.init("test");
    
  }

  async init(params: | string ) {
    console.log(params, "init");
    try {
      const res = await Request.get(
        defaultPageApi.home.init,
        {
          params,
        },
        { isNeedToken: true }
      );

    } catch (error) {
      console.error(error, "init");
    }
  }

  setCurrentTime() {
    this.currentTime = "01:01"
  }

  handleClose() {
    clearInterval(this.timeId);
    this.WS.close();
  }

  handleStart() {
    this.WS.start();
  }

  handleUnsubscribe() {
    this.WS.unsubscribe("chat", this.handleChat);
  }

  handleDestroy() {
    clearInterval(this.timeId);
    this.WS.destroy();
  }


}
</script>

<style lang="scss">
h1 {
  padding: 20px 0;
  font-size: 30px;
  font-weight: 700;
}
</style>