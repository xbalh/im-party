<!-- 
  坑点
  <template functional>
  路由出口不能使用函数式单文件组件
-->
<template>
  <div class="base-layout">
    <el-container>
      <!-- <el-header>
                                        <h3>base-layout</h3>
                                      </el-header> -->
      <el-container>
        <el-aside :class="asideStatus ? 'aside_main_unfold' : 'aside_main_collapse'">
          <ul id="nav">
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/game/chaos">游戏中心</router-link></li>
            <li @click="quit"><router-link to="/login">退出登录</router-link></li>
            <li>
              <router-link to="/other/news1" target="_blank">其他</router-link>
            </li>
          </ul>
        </el-aside>
        <el-main>
          <div class="aside_open_close" @click="asidechange">
            <i class="el-icon-arrow-left" :class="{ rotate180: !asideStatus }" v-if="asideOpenClose"></i>
            <i class="el-icon-arrow-right" :class="{ rotate180: asideStatus }" v-else></i>
          </div>
          <transition name="page">
            <router-view />
          </transition>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator"
import Request from "@/utils/requestInstance";
import defaultPageApi from "@/api/default-page"
import { namespace } from "vuex-class"

const permissionsStore = namespace('permissionsStore')
const userStore = namespace('userStore')

@Component
export default class BaseLayout extends Vue {
  @permissionsStore.Mutation('setPermissions') setPermissions!: Function
  @userStore.Mutation('setToken') setToken!: Function
  @userStore.Mutation('setRefreshToken') setRefreshToken!: Function
  @userStore.Mutation('setUserInfo') setUserInfo!: Function


  asideStatus: boolean = false;
  asideOpenClose: boolean = false;
  // constructor() {
  //   super();
  //   this.asideStatus = false;
  //   this.asideOpenClose = false;
  // }

  created() {
    this.init("test");
  }




  async init(params: | string) {
    console.log(params, "init", this.asideStatus);
    console.log("this.asideStatus", this.asideStatus)
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

  asidechange() {
    this.asideStatus = !this.asideStatus

    if (this.asideStatus) {
      setTimeout(() => {
        this.asideOpenClose = true
      }, 500)
    } else {
      setTimeout(() => {
        this.asideOpenClose = false
      }, 500)
    }
  }

  quit() {
    this.setToken('')
    this.setRefreshToken('')
    this.setUserInfo('')
    this.$router.replace({ path: '/login' })
  }

}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/common.scss";
@import "@/assets/styles/pageAnimate.scss";

.base-layout {
  height: 100%;
}

#nav {
  margin-top: 20px;
  display: block !important;
  width: 200px;
  overflow: hidden;
  li {
    // display: inline-block !important;
    padding: 15px 20px;
  }

  a {
    font-weight: bold;
    color: #2c3e50;
  }

  a.router-link-exact-active {
    color: #42b983;
  }
}

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  height: 60px;
}

.el-container {
  min-height: calc(100vh - 76px);
  height: 100%;
}

.el-aside {
  background-image: linear-gradient(120deg, #d4fc79 0%, #96e6a1 100%);
  text-align: center;
}

.el-main {
  color: #333;
  text-align: center;
}

.aside_main_unfold {
  width: 200px !important;
  transition: width 0.5s;

  ul li {
    opacity: 1 !important;
    ;
  }
}

.aside_main_collapse {
  width: 0px !important;
  transition: width 0.5s;

  ul li {
    opacity: 0;
    transition: opacity 0.3s ease-out;
  }
}

/* 侧边栏按钮样式 */
.aside_open_close {
  position: absolute;
  left: 0;
  top: 50%;
  width: 16px;
  height: 60px;
  line-height: 60px;
  color: #fff;
  background-color: #2A333A;
  border-radius: 0 6px 6px 0;
  font-size: 20px;
  z-index: 1000;
  cursor: pointer;
}

.aside_open_close:hover {
  background-color: #FF8E2B;
  color: #fff;
}

@keyframes rotate180 {
  0% {
    transform: rotate(0deg);
  }

  50% {
    transform: rotate(90deg);
  }

  100% {
    transform: rotate(180deg);
  }
}

.rotate180 {
  animation-name: rotate180;
  animation-duration: 0.5s;
  animation-fill-mode: forwards;
}
</style>
