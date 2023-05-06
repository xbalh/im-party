<template>
  <div class="home">
    <!-- 房间列表 -->
    <div class="baseArea leftArea">
      <el-aside>
        <el-tree class="filter-tree" :data="roomTree" :props="roomTreeProps" default-expand-all
          :filter-node-method="filterNode" ref="tree" @node-click="handleNodeClick">
        </el-tree>
      </el-aside>
    </div>
    <div class="baseArea midArea" :style="{'visibility': joinSuccess? 'visible':'hidden'}">
      <!-- 播放器 -->
      <div class="player" ref="player" id="drag-top">
        <!-- <MusicPlayer @currentTime="currentTime"></MusicPlayer> -->
        <audio controls>
          <!-- <source src="../../../../assets/music/test.mp3" /> -->
          <source src="https://su-wsm.github.io/assets/music/test.mp3" />
        </audio>
      </div>
      <!-- 收缩栏 -->
      <!-- <div class="resize-line" title="收缩侧边栏" ref="resize" id="resize" @mousedown="handleMouseDown">
      </div> -->
      <!-- 聊天区域 -->
      <div class="chatArea" ref="charArea" id="drag-down">
        <Chat></Chat>
      </div>
    </div>

    <!-- 房间在线用户列表以及播放队列 -->
    <div class="baseArea rightArea">
      <div class="">
        <el-tabs v-model="activeName" @tab-click="tabClickHandle">
          <el-tab-pane label="歌曲队列" name="songList">歌曲队列</el-tab-pane>
          <el-tab-pane label="用户列表" name="userList">用户列表</el-tab-pane>
        </el-tabs>
      </div>

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
import { Loading } from 'element-ui';
import Chat from "@/components/chat/index.vue";

const userStore = namespace('userStore')

@Component({
  components: {
    RequestExample,
    LangExample,
    VuexExample,
    WorkerExample,
    MusicPlayer,
    Chat
  }
})
export default class Home extends Vue {
  WS: Ws | any;
  timeId: NodeJS.Timeout | any;
  @userStore.Getter('getToken') getToken!: string
  @userStore.Mutation('setUserInfo') setUserInfo!: Function
  currentUserInfo: any = null;
  roomTree: Array<any> = [];
  roomTreeProps: any;

  currentTime: string = "00:00";
  joinSuccess: boolean = false;
  treeClickCount: number = 0;
  currentRoomInfo: Room | any;

  activeName: string = 'songList'
  dragState: any;

  constructor() {
    super();
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
    if (node.level < 2) return
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
          this.joinRoom(data)
        })

      }
    }, 300);
  }

  joinRoom(roomInfo: Room) {
    this.joinSuccess = false
    if (!this.WS && this.WS instanceof Ws) this.WS.destroy()
    const loadingInstance = Loading.service({ fullscreen: true });
    const token = localStorage.getItem("token");
    if (!token) console.error("token为空");
    try {
      //ws连接
      this.WS = new Ws("ws://localhost:8080/musicParty/ws/" + roomInfo.roomNo, [token!], false);
      this.WS.send("connect success");
      this.WS.subscribe("/music/chat", this.handleChat);
      //加载房间信息

    } catch (error) {
      this.$message({
        showClose: true,
        message: '出了点问题，加入房间失败了，重试一下吧',
        type: 'error'
      });
      return
    } finally {
      loadingInstance.close();
    }

    const count = 0;
    this.timeId = setInterval(() => {
      count;
      this.WS.send(`count: ${count}`);
    }, 10000);

    this.currentRoomInfo = roomInfo;
    this.joinSuccess = true
    this.$notify({
      title: '成功',
      message: '成功进入房间！',
      type: 'success'
    });
  }

  handleChat(data: object) {
    console.log(data)
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

  tabClickHandle(tab: any, event: any) {
    console.log(tab, event);
  }

  toggleCollapse(){
    console.log('1')
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
  // max-height: 500px;
}

.baseArea {}

.leftArea {
  // background-color: red;
  width: 20%;

}

.midArea {
  // background-color: white;
  width: 60%;
  visibility:hidden;
}

.rightArea {
  // background-color: blue;
  width: 20%;
}

.roomList {
  margin: 0 2px 0 1px;
  border: 10px black;
  border-radius: 2px
}

.player {
  margin-left: 5%;
  width: 100%;
  /*左侧初始化宽度*/
  height: 15%;
  // background-color: burlywood;
}

.chatArea {
  margin-left: 5px;
  width: 95%;
  /*右侧初始化宽度*/
  height: 85%;
  box-shadow: -1px 4px 5px 3px rgba(0, 0, 0, 0.11);
  // background-color: aqua;
}

/*拖拽区div样式*/
.resize {
  cursor: row-resize;
  position: relative;
  background-color: #d6d6d6;
  border-radius: 5px;
  width: 100%;
  height: 10px;
  background-size: cover;
  background-position: center;
  font-size: 32px;
  color: white;
}

/*拖拽区鼠标悬停样式*/
.resize:hover {
  color: #444444;
}

#drag-box {
  height: 500px;
}

.data-list {
  // overflow-y: scroll;
  border-bottom: 1px solid #e3e5eb;
  height: calc(50% - 4px);
  min-height: 65px;
  max-height: 100%;
}

.target-list {
  height: 50%;
  min-height: 65px;
}

.resize-line {
  height: 4px;
  cursor: move;
}

</style>