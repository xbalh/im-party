<template>
  <div> 
    <v-btn id="my_btn" style="position: fixed;" theme="dark" ref="refButton" icon="mdi-forum-outline"
      class="drawer-button" color="#55BB46" v-dialogDrag="true">
      <!-- <v-badge content="66" offset-x="20" offset-y="-15">
        <v-icon class="fa-spin">mdi-forum-outline</v-icon>
      </v-badge> -->
      <v-badge dot offset-x="15" offset-y="-10" color="red">
        <v-icon class="fa-spin">mdi-forum-outline</v-icon>
      </v-badge>
    </v-btn>
  </div>
</template>

<script setup lang="ts">
import Bus from "@/utils/common/Bus";

const refButton = ref<import('vuetify/components').VBtn>()

const openSetting = ref(false)
Bus.on('openSetting', (flag: boolean) => {
  openSetting.value = flag
})


// // 拖拽开始事件
// const dragstart = (e: any) => {
//   // 记录拖拽元素初始位置

// };
// // 拖拽完成事件
// const dragend = (e: any) => {
//   console.log("eeeeee", e);
//   let btn = document.getElementById("my_btn") as HTMLElement
//   btn!.style.left = e.clientX + 'px';
//   btn!.style.top = e.clientY + 'px';
// }

onMounted(() => {
  // execAnimate()
})
// onBeforeUnmount(() => {
//   if (timeout) clearTimeout(timeout)
// })

</script>

<style lang="scss" scoped>
.drawer-button {
  position: fixed;
  top: 340px;
  right: -20px;
  z-index: 9999;
  box-shadow: 1px 1px 18px #55BB46;
  opacity: 75%;

  .v-icon {
    margin-left: -18px;
    font-size: 1.3rem;
  }

  .fa-spin {
    // animation: spin 2s infinite linear;
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }

    to {
      transform: rotate(360deg);
    }
  }
}
</style>
