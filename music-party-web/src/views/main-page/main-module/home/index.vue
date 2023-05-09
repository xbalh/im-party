<template>
  <div class="home">
    <!-- 房间列表 -->
    <div class="baseArea leftArea">
      <el-aside>
        <el-tree class="room-tree" :data="roomTree" :props="roomTreeProps" default-expand-all
          :filter-node-method="filterNode" ref="tree" @node-click="handleNodeClick" empty-text="当前房间列表为空">
        </el-tree>
      </el-aside>
    </div>
    <div class="baseArea midArea">
      <el-empty description="先加入一个喜欢的房间吧" v-if="!joinSuccess"></el-empty>
      <div :style="{ 'visibility': joinSuccess ? 'visible' : 'hidden', 'height': '100%' }">
        <!-- 播放器 -->
        <div class="player" ref="player" id="drag-top">
          <!-- <MusicPlayer @currentTime="currentTime"></MusicPlayer> -->
          <el-button @click="playSong">播放</el-button>
          <audio controls autoplay ref="player" :src="songUrl">
            <!-- <source src="../../../../assets/music/test.mp3" /> -->
          </audio>
        </div>
        <!-- 收缩栏 -->
        <!-- <div class="resize-line" title="收缩侧边栏" ref="resize" id="resize" @mousedown="handleMouseDown">
      </div> -->
        <!-- 聊天区域 -->
        <div class="chatArea" ref="charArea" id="drag-down">
          <Chat :userName="currentUserInfo.username" :sendMsg="sendMsgHandle" ref="chatRef" />
        </div>
      </div>

    </div>

    <!-- 房间在线用户列表以及播放队列 -->
    <div class="baseArea rightArea">
      <div class="">
        <el-tabs v-model="activeName" @tab-click="tabClickHandle" stretch>
          <el-tab-pane label="歌曲队列" name="songList">歌曲队列
            <el-button @click="songSearchDialogVisible = true">搜索歌曲</el-button>
          </el-tab-pane>
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

    <el-dialog title="歌曲搜索" :visible.sync="songSearchDialogVisible" width="50%" :close-on-click-modal="false">
      <el-input v-model="songSearchInputContent" class="searchinput" placeholder="搜索歌曲名称" prefix-icon="el-icon-search">
        <el-button slot="append" class="searchbtn" @click="searchSong">搜索</el-button>
      </el-input>
      <el-table ref="songSearchTable" :data="songSearchResult.songs || []" highlight-current-row style="width: 100%">
        <el-table-column prop="name" label="歌曲名称" width="180">
        </el-table-column>
        <el-table-column prop="artistNames" label="作者" width="180" :formatter="artistsFormatter">
        </el-table-column>
        <el-table-column label="操作">
          <template #default="itemScope">
            <el-button size="mini" @click="onDemandSong(itemScope.$index, itemScope.row)">点歌</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- <el-button @click="confirmEnemy">确定</el-button> -->
    </el-dialog>

  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from "vue-property-decorator";
import RequestExample from "@/components/example/requestExample.vue";
import LangExample from "@/components/example/langExample.vue";
import VuexExample from "@/components/example/vuexExample.vue";
import WorkerExample from "@/components/example/workerExample.vue";
import MusicPlayer from "@/components/music-player/index.vue";
import defaultPageApi from "@/api/default-page";
import musicApi from "@/api/music";
import Request from "@/utils/requestInstance";
import Ws from "@/utils/ws";
import { namespace } from "vuex-class"
import { UserInfoType } from "@/types/user"
import { Room } from "@/types/room"
import { Loading } from 'element-ui';
import Chat from "@/components/chat/index.vue";
import { Msg } from "@/types/chat";

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
  currentUserInfo: UserInfoType | any = {};
  roomTree: Array<any> = [];
  roomTreeProps: any;

  currentTime: string = "00:00";
  joinSuccess: boolean = false;
  treeClickCount: number = 0;
  currentRoomInfo: Room | any;

  activeName: string = 'songList'
  dragState: any;
  songSearchDialogVisible: boolean = false;
  songSearchInputContent: string = '';
  songUrl: string = '';
  songSearchResult: any = {};

  @Ref('chatRef') private chatRef!: Chat;


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
      this.setUserInfo(userInfo);
      this.currentUserInfo = userInfo;
      console.log(this.currentUserInfo.username)
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
      console.log(this.WS)
      // this.WS.send("connect success");
      //订阅 下一首播放 广播
      this.WS.subscribe("/music/playControl/nextPlay", this.handlePlay);
      //订阅 聊天 广播
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

    // const count = 0;
    // this.timeId = setInterval(() => {
    //   count;
    //   this.WS.send(`count: ${count}`);
    // }, 10000);

    this.currentRoomInfo = roomInfo;
    this.joinSuccess = true
    this.$notify({
      title: '成功',
      message: '成功进入房间！',
      type: 'success'
    });
  }

  handleChat(data: Msg) {
    console.log(data);
    this.chatRef.receiveMsg(data);
  }

  handlePlay(data: any) {
    console.log(data)
    this.songUrl = data.url;

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

  toggleCollapse() {
    console.log('1')
  }

  async searchSong() {
    if (this.songSearchInputContent !== '') {
      const res = await Request.get(musicApi.song.search, {
        params: {
          keywords: this.songSearchInputContent,
          offset: 0,
          limit: 10
        }
      }, {})
      this.songSearchResult = res.data.data
      console.log(this.songSearchResult)
    }
  }

  artistsFormatter(row: any, column: any, cellValue: any, index: any) {
    return row.artists && row.artists.map((artist: any) => {
      return artist.name
    }).join(',')
  }

  async onDemandSong(index: any, row: any) {
    const res = await Request.post(musicApi.room.addMusic + `/${this.currentRoomInfo.roomNo}`, {
      params: {
        songId: row.id
      }
    }, {})
    if (res.data.code === 200) {
      this.$notify({
        title: '成功',
        message: '点歌成功！',
        type: 'success'
      });
    }
  }

  sendMsgHandle(msg: any) {
    const data = {
      method: "/music/chat",
      data: {
        msg: msg
      }
    }
    this.WS.send(JSON.stringify(data));
  }

  async playSong(){
    const res = await Request.get(musicApi.room.play, {
      params: {
        roomId: this.currentRoomInfo.roomNo
      }
    }, {})
    if (res.data.code === 200) {
      this.$notify({
        title: '成功',
        message: '开始播放',
        type: 'success'
      });
    }
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
  height: 100%;
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
  // visibility: hidden;
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

.room-tree {
  user-select: none;
}

@keyframes colorChange {
  0% {
    background: linear-gradient(to top, hsla(6, 90%, 68%, 0), hsla(336, 99%, 66%, 0));
  }

  50% {
    background: linear-gradient(to top, hsla(6, 90%, 68%, 50%), hsla(336, 99%, 66%, 50%));
  }

  100% {
    background: linear-gradient(to top, hsla(6, 90%, 68%, 100%), hsla(336, 99%, 66%, 100%));
  }
}

.el-tree-node>.el-tree-node__content {
  background-color: hsla(6, 90%, 68%, 0%);
}

.el-tree-node.is-current>.el-tree-node__content {
  // background-image: linear-gradient(to top, #ff0844 0%, #ffb199 100%);
  // background: linear-gradient(to top, hsla(6, 90%, 68%, 100%), hsla(336, 99%, 66%, 100%));
  background-color: hsla(6, 90%, 68%, 100%);
  transition: background-color 0.5s ease-in-out;
}
</style>