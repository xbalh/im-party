<template>
  <v-card class="flex-grow-1 h-100" v-if="user" color="surface">
    <v-card-title class="justify-center text-h4 mb-2">
      编辑个人资料
    </v-card-title>
    <v-card-text>
      <!-- <div class="d-flex align-center py-3">
        <div>
          <div class="text-h4"> 编辑个人资料 {{ user.userName && `- ${user.userName}` }}</div>
          <breadcrumb :root="'apps'" />
        </div>
        <v-spacer></v-spacer>
        <v-btn variant="plain" icon @click="getData">
          <v-icon>mdi-refresh</v-icon>
        </v-btn>
      </div> -->

      <div v-if="user.userRole === 'admin'" class="d-flex align-center font-weight-bold text-primary my-2">
        <v-icon size="x-small" color="primary">mdi-security</v-icon>
        <span class="ma-1">管理员</span>
      </div>

      <!-- <div class="mb-4">
      <div class="d-flex">
        <span class="font-weight-bold">修改昵称</span>
        <v-text-field v-model="user.nickName" clearable></v-text-field>
      </div>
      <div class="d-flex">
        <span class="font-weight-bold">ID</span>
        <span class="mx-1">
          <copy-label :text="user.id + ''" />
        </span>
      </div>
    </div> -->


      <v-form fast-fail @submit.prevent="submit">
        <div class="d-flex">
          <span class="font-weight-bold">昵称</span>
        </div>
        <v-text-field v-model="user.nickName" :rules="nickNameRules"></v-text-field>
        <div class="d-flex" v-if="currentWyyUserInfo">
          <span class="font-weight-bold">当前绑定的网易云账号</span>
        </div>
        <div v-if="currentWyyUserInfo">
          <v-list-item :prepend-avatar="currentWyyUserInfo.avatarUrl" :title="currentWyyUserInfo.nickname"></v-list-item>
        </div>
        <div class="d-flex">
          <v-text-field v-model="searchInput" clearable append-icon="mdi-magnify" label="通过用户名搜索网易云用户" single-line
            hide-details @click:append="searchWyyUserHandle"></v-text-field>
        </div>

        <div class="d-flex">
          <v-list>
            <v-list-item v-for="item in searchWyyUserItems" lines="one" :title="item.nickname"
              :prepend-avatar="item.avatarUrl" @click="bindUser(item)">
            </v-list-item>
          </v-list>
        </div>

        <!-- <v-text-field v-model="lastName" label="Last name" :rules="lastNameRules"></v-text-field> -->

        <v-btn type="submit" block class="mt-2">保存</v-btn>
      </v-form>


      <!-- <v-tabs v-model="tab" :show-arrows="false" background-color="transparent" color="primary">
      <v-tab value="tabs-account">Account</v-tab>
      <v-tab value="tabs-information">Information</v-tab>
    </v-tabs> -->
      <!-- <v-window v-model="tab">
      <v-window-item value="tabs-account">
        <account-tab :user="user"></account-tab>
      </v-window-item>
      <v-window-item value="tabs-information">
        <information-tab :user="user"></information-tab>
      </v-window-item>
    </v-window> -->
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { fetchUserInfo } from '@/service/api/auth'
import { fetchWyyUserInfo, searchWyyUser } from '@/service/api/music'
import { bindWyyUser, updateUserInfo } from "@/service/api/user";
import { useLoadingProgressLine } from "@/components/provider";
import { trim } from "lodash-es";
import { localStg } from "@/utils";

const user = ref<Auth.UserInfo>()
const currentWyyUserInfo = ref()
const searchInput = ref('')
const searchWyyUserItems = ref()
const searchWyyUserHandle = async () => {
  if (!searchInput || trim(searchInput.value) === '') return
  const resp = await searchWyyUser(searchInput.value)
  if (resp.data) {
    searchWyyUserItems.value = resp.data.userprofiles
  }
}

const bindUser = (wyyUserInfo) => {
  const bindDialog = window.$dialog?.show({
    main: `确定要将用户【${wyyUserInfo.nickname}】绑定至当前账号么`,
    title: '提醒',
    confirmText: '确定',
    cancelText: '取消',
    confirm: async () => {
      console.log("确认绑定")
      const resp = await bindWyyUser(wyyUserInfo.userId)
      if (resp.data) {
        window.$snackBar?.success('绑定成功！')
        localStg.set('userInfo', resp.data)
        router.go(0)
      }
      bindDialog?.close()
    }
  })
}
const { show, hide } = useLoadingProgressLine()


const nickNameRules = [
  value => {
    if (value?.length > 1) return true

    return '昵称不能为空'
  },
  value => {
    if (value?.length < 15) return true

    return '不能超过15字'
  },
]

const submit = async () => {
  if (user.value) {
    const { data: newUserInfo } = await updateUserInfo(user.value)
    if (newUserInfo) {
      window.$snackBar?.success("修改成功", { location: 'bottom center' })
      localStg.set('userInfo', newUserInfo)
      router.go(0)
    }
  }

}

async function getData() {
  show()
  const { data } = await fetchUserInfo()
  hide()
  if (data) {
    user.value = data
  }
  if (data?.wyyUserId) {
    const { data: wyyUserInfo } = await fetchWyyUserInfo(data?.wyyUserId)
    if (wyyUserInfo.profile) {
      currentWyyUserInfo.value = wyyUserInfo.profile
    }
  }
}

onMounted(() => {
  getData()
})


</script>
