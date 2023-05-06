<template>
  <div>
    <div v-if="!loadedSelf">
      <el-button @click="createCharacter">创建角色</el-button>
    </div>
    <div class="parent" style="background-color: lightgrey" v-if="loadedSelf">
      <div class="child" style="background-color: lightblue">
        挑战者
        <span>
          名称：{{ selfInfo?.name }}<br />
          血量：{{ selfInfo?.hp }}<br />
        </span>
      </div>
      <div class="child" style="background-color: lightgreen">
        被挑战者
        <div v-if="!loadedEnemy">
          <el-button @click="selectEnemyDialogVisble = true"
            >选择一位想要挑战的对象</el-button
          >
        </div>
        <span>
          名称：{{ enemyInfo?.name }}<br />
          血量：{{ enemyInfo?.hp }}<br />
        </span>
      </div>
    </div>
    <div>
      当前回合: {{ round }}<br />
      <span v-html="fightMessage"></span><br />
      <span v-if="isEnd">对局已结束</span>
    </div>
    <el-dialog title="选择挑战对象" :visible.sync="selectEnemyDialogVisble" width="35%">
      <el-table
        ref="enemysTable"
        :data="enemysTableData"
        highlight-current-row
        @current-change="handleCurrentChange"
        style="width: 100%"
      >
        <el-table-column prop="userName" label="名称" width="180"> </el-table-column>
      </el-table>
      <el-button @click="confirmEnemy">确定</el-button>
    </el-dialog>
    <div v-if="loadedSelf && loadedEnemy && !isStarted">
      <el-button @click="startFight">开始对局</el-button>
    </div>
    <div v-if="isStarted && !isEnd">
      <el-button @click="nextTurn">下一回合</el-button>
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
import gameApi from "@/api/game";
import Request from "@/utils/requestInstance";
import Ws from "@/utils/ws";
import { namespace } from "vuex-class";
import { PersonFightInfo, BuffInfo, WpInfo, BattleInfo } from "@/types/game/chaos";
import { UserInfoType } from "@/types/user";
import ElementUI, { Table } from "element-ui";
import { ElementUIComponent } from "element-ui/types/component";
import { JsonObject } from "type-fest";

const userStore = namespace("userStore");

interface EnemyTableRowType {
  userName: string;
}

@Component({
  components: {
    RequestExample,
    LangExample,
    VuexExample,
    WorkerExample,
    MusicPlayer,
  },
})
export default class GeometryChaos extends Vue {
  //本人信息
  selfInfo: PersonFightInfo | any;
  //敌人信息
  enemyInfo: PersonFightInfo | any;
  //本人是否已加载
  loadedSelf: boolean = false;
  //敌人是否已加载
  loadedEnemy: boolean = false;
  //选择敌人的弹窗可见性
  selectEnemyDialogVisble: boolean = false;
  isStarted: boolean = false;
  enemysTableData: Array<EnemyTableRowType> | null;
  currentRow: any = null;
  curUserInfo: UserInfoType;
  fightId: any = null;
  fightInfo: BattleInfo | any;
  fightMessage: string = "";
  round: number = 0;
  //对局是否已结束
  isEnd: boolean = false;

  constructor() {
    super();
    this.enemysTableData = [];
    this.curUserInfo = JSON.parse(localStorage.getItem("userInfo") as string);
  }

  created() {
    //获取当前用户对战信息
    this.getCurrentUserInfo();
    //获取敌人选择列表
    this.getEnemyList()
  }

  async getCurrentUserInfo() {
    const res = await Request.post(
      gameApi.geometrychaos.getUserFightInfo,
      {
        data: this.curUserInfo.username,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
      },
      { isNeedToken: true }
    );
    //第一次游戏，还没有创建角色
    if (res.data.code === 700) {
      this.$message({
        message: "第一次游戏，需要创建角色",
        type: "warning",
      });
    }
    if (res.data.code === 200) {
      this.selfInfo = res.data.data;
      console.log("自己信息：" + this.selfInfo);
      this.loadedSelf = true;
    }
  }

  async getEnemyList(){
    const res = await Request.post(
      gameApi.geometrychaos.getChallengableFighter,
      {
        data: this.curUserInfo.username,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
      }
    );

      if(res.data.code === 200){
        this.enemysTableData = res.data.data
      }

  }

  async getEnemyInfo(userName: string) {
    const res = await Request.post(
      gameApi.geometrychaos.getUserFightInfo,
      {
        data: userName,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
      },
      { isNeedToken: true }
    );
    if (res.data.code === 200) {
      this.enemyInfo = res.data.data;
      console.log("敌人信息：" + this.enemyInfo);
      this.loadedEnemy = true;
      this.selectEnemyDialogVisble = false;
    }
  }

  async createCharacter() {
    const res = await Request.post(
      gameApi.geometrychaos.createCharacter,
      {
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
        data: this.curUserInfo.username as string,
      },
      { isNeedToken: true }
    );
    if (res.data.code === 200) {
      this.getCurrentUserInfo();
    }
  }

  confirmEnemy() {
    if (!this.currentRow) {
      this.$message({
        message: "需要选择一位玩家进行挑战",
        type: "warning",
      });
    }
    this.getEnemyInfo(this.currentRow.userName);
  }

  handleCurrentChange(val: any) {
    this.currentRow = val;
  }

  async startFight() {
    const startFightParam = {
      user: this.selfInfo,
      enemy: this.enemyInfo,
      fightInfo: {},
    };
    const res = await Request.post(gameApi.geometrychaos.fightStart, {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      data: startFightParam,
    });
    if (res.data.code !== 200) {
      this.$message({
        message: "对局开始失败",
        type: "warning",
      });
    }
    this.fightId = res.data.msg;
    this.isStarted = true;
    this.nextTurn();
  }

  async nextTurn() {
    this.fightMessage += "<br>";
    const res = await Request.post(gameApi.geometrychaos.startOneTurn, {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      data: {
        uuid: this.fightId,
      },
    });

    this.fightInfo = res.data.data;
    this.selfInfo = JSON.parse(this.fightInfo.offensiveInfo);
    this.enemyInfo = JSON.parse(this.fightInfo.defenseInfo);
    for (const oneTurnMessage of this.fightInfo.message) {
      const toViewMsg = oneTurnMessage.msg;
      await new Promise<void>((resolve) => {
        setTimeout(() => {
          this.fightMessage +=
            '<br/><span style="color:' +
            oneTurnMessage.color +
            '">' +
            toViewMsg +
            "</span>";
          resolve(undefined);
        }, oneTurnMessage.ms);
      });
    }

    this.round = this.fightInfo.round;
    this.fightId = this.fightInfo.id;
    this.isEnd = this.fightInfo.ifEnd;
  }
}
</script>

<style lang="scss">
body,
p {
  margin: 0;
}

span {
  line-height: 2;
}

.parent {
  display: flex;
}

.child {
  flex: 1;
  height: 100px;
}

.child + .child {
  margin-left: 20px;
}
</style>
