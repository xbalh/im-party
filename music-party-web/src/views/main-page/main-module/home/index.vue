<template>
  <div class="home">
    <MusicPlayer></MusicPlayer>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import RequestExample from "@/components/example/requestExample.vue";
import WsExample from "@/components/example/wsExample.vue";
import LangExample from "@/components/example/langExample.vue";
import VuexExample from "@/components/example/vuexExample.vue";
import WorkerExample from "@/components/example/workerExample.vue";
import MusicPlayer from "@/components/music-player/index.vue";
import defaultPageApi from "@/api/default-page"
import Request from "@/utils/requestInstance";

@Component({
  components: {
    RequestExample,
    WsExample,
    LangExample,
    VuexExample,
    WorkerExample,
    MusicPlayer
  }
})

export default class Home extends Vue {
  lazySrc = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2754522765,4239052193&fm=26&gp=0.jpg"

  created() {
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

  longpress() {
    console.log("触发长按事件")
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