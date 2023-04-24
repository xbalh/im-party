<template>
  <div class="home">
    <!-- 房间列表 -->
    <div class="leftArea">
      <div class="roomList">
        <el-tree class="filter-tree" :data="roomTree" :props="roomTreeProps" default-expand-all
          :filter-node-method="filterNode" ref="tree" @node-click="handleNodeClick">
        </el-tree>
      </div>
    </div>
    <div class="midArea">
      <!-- 播放器 -->
      <div class="player" v-if="isInRoom">
        <MusicPlayer @currentTime="currentTime"></MusicPlayer>
      </div>
      <!-- 聊天区域 -->
      <div class="charArea">

      </div>
    </div>

    <!-- 房间在线用户列表以及播放队列 -->
    <div class="rightArea">

    </div>


    <!-- <div>
      <el-button @click="setCurrentTime">
        控制当前播放进度
      </el-button>
      <el-button @click="gotoChaos">
        大乱斗
      </el-button>
    </div> -->
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import RequestExample from "@/components/example/requestExample.vue";
import LangExample from "@/components/example/langExample.vue";
import VuexExample from "@/components/example/vuexExample.vue";
import WorkerExample from "@/components/example/workerExample.vue";
import MusicPlayer from "@/components/music-player/index.vue";
import defaultPageApi from "@/api/default-page";
import Request from "@/utils/requestInstance";
import Ws from "@/utils/ws";
import { namespace } from "vuex-class"
import { UserInfoType } from "@/types/user"
import { Room } from "@/types/room"
import { TreeNode } from "element-ui/types/tree";

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
  @userStore.Mutation('setUserInfo') setUserInfo!: Function
  currentUserInfo: any = null;
  roomTree: Array<any> = [];
  roomTreeProps: any;

  currentTime: string = "00:00";
  isInRoom: boolean = false;
  treeClickCount: number = 0;

  constructor() {
    super();
    const token = localStorage.getItem("token");
    if (!token) console.error("token为空");
    this.WS = new Ws("ws://localhost:8080/musicParty/ws", [token!], false);
    this.WS.send("connect success");
    this.handleChat = (data: object) => console.log(data);
    this.WS.subscribe("/music/chat", this.handleChat);

    let count = 0;
    this.timeId = setInterval(() => {
      ++count;
      this.WS.send(`count: ${count}`);
    }, 10000);

    this.roomTreeProps = {
      children: 'roomList',
      label: 'label'
    }

  }

  created() {
    //初始化接口
    this.init("test");
    //获取当前用户信息
    this.getCurrentUserInfo();
    //初始化房间列表
    this.initRoomList();
  }

  async init(params: | string) {
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

  async getCurrentUserInfo() {
    try {
      const res = await Request.get(defaultPageApi.user.info, {}, { isNeedToken: false })
      const userInfo: UserInfoType = {
        username: res.data.msg! as string,
        age: 24,
        sex: 0
      }
      this.setUserInfo(userInfo)
    } catch (error) {
      console.error(error, '获取用户信息出错了')
    }
  }

  async initRoomList() {
    const res = await Request.get(defaultPageApi.room.list)
    const roomList: Array<Room> = res.data.data
    const roomMap: Map<string, Array<Room>> = new Map();
    roomList.forEach((room: Room) => {
      if (!roomMap.has(room.roomStyle)) {
        roomMap.set(room.roomStyle, Array.of(room))
      } else {
        roomMap.get(room.roomStyle)?.push(room)
      }
    });
    for (const [style, roomList] of roomMap) {
      const children = roomList.map((room) => {
        return {
          ...room,
          id: room.roomNo,
          label: room.roomName
        }
      })
      this.roomTree.push({
        label: style,
        roomList: children
      })
    }

  }

  handleNodeClick(data: any, node: any) {
    //记录点击次数
    this.treeClickCount++;
    //单次点击次数超过2次不作处理,直接返回,也可以拓展成多击事件
    if (this.treeClickCount >= 2) {
      return;
    }
    //计时器,计算300毫秒为单位,可自行修改
    window.setTimeout(() => {
      if (this.treeClickCount == 1) {
        //把次数归零
        this.treeClickCount = 0;
        //单击事件处理

      } else if (this.treeClickCount > 1) {
        //把次数归零
        this.treeClickCount = 0;
        //双击事件
        this.$confirm('是否进入房间【' + data.roomStyle + '-' + data.roomName + '】?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '进入成功!'
          });
        })

      }
    }, 300);
  }



  setCurrentTime() {
    this.currentTime = "01:01"
  }

  gotoChaos() {
    this.$router.replace({ path: '/game/chaos' })
  }

  filterNode(value: any, data: any) {
    if (!value) return true;
    return data.label.indexOf(value) !== -1;
  }

}
</script>

<style lang="scss">
h1 {
  padding: 20px 0;
  font-size: 30px;
  font-weight: 700;
}

.home {
  width: 100%;
  display: flex;
}

.leftArea {
  // background-color: red;
  width: 20%;
}

.midArea {
  // background-color: white;
  width: 60%;
}

.rightArea {
  // background-color: blue;
  width: 20%;
}
</style>