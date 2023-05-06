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
            <li><router-link to="/home1">Home1</router-link></li>
            <li><router-link to="/home2">Home2</router-link></li>
            <li>
              <router-link to="/other/news1" target="_blank">List</router-link>
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

@Component
export default class BaseLayout extends Vue {
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

}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/common.scss";
@import "@/assets/styles/pageAnimate.scss";

.base-layout {
  height: 100%;
}

#nav {
  li {
    padding: 10px 20px;
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
  background-color: #d3dce6;
  color: #333;
  text-align: center;
}

.el-main {
  color: #333;
  text-align: center;
}

.aside_main_unfold {
  width: 200px !important;
  transition: width 0.5s;
}

.aside_main_collapse {
  width: 0px !important;
  transition: width 0.5s;
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
