<template>
  <div>
    <div v-if="!loadedSelf">
      <el-button @click="createCharacter">创建角色</el-button>
    </div>
    <div class="parent" v-if="loadedSelf">
      <div class="child">
        进攻方
        <br>
        <br>
        <span>
          <!-- 名称：{{ selfInfo?.name }}<br /> -->
          HP：{{ selfInfo?.hp }}<br />
        </span>
      </div>
      <div class="child" >
        防守方
        <br>
        <br>
        <span>
          <!-- 名称：{{ enemyInfo?.name }}<br /> -->
          HP：{{ enemyInfo?.hp ? enemyInfo.hp : '？' }}
          <br />
        </span>
      </div>
      
      <div v-if="!loadedEnemy" class="fightButton">
          <el-button @click="selectEnemyDialogVisble = true"
            >选择挑战对象</el-button
          >
        </div>
    </div>
    <div
      v-if="!isStarted"
      class="selfInfo"
    >
      <div class="selfKeyInfo">
        <div v-for="(value, key) in selfInfo" :key="key">
          <span v-if="isMessageStringOrNumber(value)"
            >{{ $t(`geometryChaos.${key}`) }}
          </span>
        </div>
      </div>
      <div class="selfValueInfo">
        <div v-for="(value, key) in selfInfo" :key="key">
          <span v-if="isMessageStringOrNumber(value)"> {{ value }} </span>
        </div>
      </div>
    </div>
    <div>
      <span>军火库展示</span>
      <div class="arsenal">
        <div v-for="(url, index) in wpUrls" :key="index" class="wp-image-div">
          <img
            :src="url.imageUrl"
            class="wp-image"
            @mouseover="showaPOP($event, url.wpDic)"
            @mouseout="showaPOP($event, url.wpDic)"
            @touchend="showaPOP($event, url.wpDic)"
          />
          <div class="popup">
            <p></p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isStarted">
      当前回合: {{ round }}<br />
      <span v-html="fightMessage"></span><br />
      <span v-if="isEnd">对局已结束</span>
    </div>
    <el-dialog
      title="选择挑战对象"
      :visible.sync="selectEnemyDialogVisble"
      width="35%"
    >
      <el-table
        ref="enemysTable"
        :data="enemysTableData"
        highlight-current-row
        @current-change="handleCurrentChange"
        style="width: 100%"
      >
        <el-table-column prop="userName" label="名称" width="180">
        </el-table-column>
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
import {
  PersonFightInfo,
  BuffInfo,
  WpInfo,
  BattleInfo,
  WpNodesInfo,
  WpDic,
} from "@/types/game/chaos";
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
  wpUrls: Array<WpNodesInfo> = [];
  round: number = 0;
  //对局是否已结束
  isEnd: boolean = false;
  showPopup: boolean = false;

  constructor() {
    super();
    this.enemysTableData = [];
    this.curUserInfo = JSON.parse(localStorage.getItem("userInfo") as string);
  }

  created() {
    //获取当前用户对战信息
    this.getCurrentUserInfo();
    //获取敌人选择列表
    this.getEnemyList();
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
      const wpNames = this.selfInfo.wpInfo.wpNameHolding.split(",");
      this.wpUrls = wpNames.map((wpName: string) => {
        return {
          imageUrl: require("../../../../assets/gcimages/" +
            wpName.trim() +
            ".png"),
          wpDic: this.selfInfo.wpInfo.wpDicList.find(
            (item: WpDic) => wpName === item.wpName
          ),
        };
      });

      this.loadedSelf = true;
    }
  }

  async getEnemyList() {
    const res = await Request.post(
      gameApi.geometrychaos.getChallengableFighter,
      {
        data: this.curUserInfo.username,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
      }
    );

    if (res.data.code === 200) {
      this.enemysTableData = res.data.data;
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
    console.log(val);
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

  isMessageStringOrNumber(value: any) {
    return typeof value === "string" || typeof value === "number";
  }

  showaPOP(event: MouseEvent | TouchEvent, wpDic: WpDic) {
    console.log(event);
    console.log(wpDic);
    const target = event.target as HTMLElement;
    const nextSibling = target.nextElementSibling as HTMLElement;
    if (event.type == "mouseover") {
      nextSibling.setAttribute("class", "popup popupMouseOver");
      const p = nextSibling.children[0];
      p.innerHTML = wpDic.wpName +"<br><br>"+ wpDic.wpRarity +"<br><br>"+ wpDic.wpDetail;
    } else {
      nextSibling.setAttribute("class", "popup");
    }
  }

  
}
</script>

<style lang="scss">
.el-main {
  text-align: left !important;
}
body {
  background-image: linear-gradient(-20deg, #e9defa 0%, #fbfcdb 100%);
  margin: 0;
}
p {
  margin: 0;
}
span {
  line-height: 2;
  font-size:20px;
}

.parent {
  margin: 5%;
  display: flex;
}

.child {
  flex: 1;
  height: 100px;
}

.child + .child {
  margin-left: 20px;
}

.fightButton {
  display:inline-block;
  float:right;
}

.selfInfo {
  margin: 5%;
  div {
    width: 100px;
    text-align: left;
  }
}

.selfKeyInfo {
  display: inline-block;
}

.selfValueInfo {
  display: inline-block;
}

.arsenal {
  width: 50%;
  background-image: linear-gradient(to right, #ffecd2 0%, #fcb69f 100%);
}

.wp-image {
  width: 80px;
  height: 80px;
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
  background-image: linear-gradient(120deg, #f6d365 0%, #fda085 100%);
  border-radius: 99%;
  display: inline-block;
}

.wp-image-div {
  width: 80px;
  height: 80px;
  margin: 8px;
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
  border-radius: 99%;
  display: inline-block;
  position: relative;
}

.popup {
  position: absolute;
  visibility: hidden;
  display: block;
  z-index: 999;
  top: -110px;
  right: -210px;
  background-color: rgba(0, 0, 0, 0.6);
  color: #ddd;
  border: 1px solid #ccc;
  padding: 10px;
  width: 200px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  opacity: 0;
}

.popupMouseOver {
  visibility: visible !important;
  transition: opacity 0.4s ease-in-out;
  opacity: 1 !important;
}

// .wp-image:hover {
//   .popup {
//     opacity: 1;transition: opacity 0.4s ease-in-out; /* 设置渐变过渡效果 */
//   }
// }
</style>
