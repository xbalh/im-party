<template>
  <div class="flex-grow-1" v-if="user">
    <div class="d-flex align-center py-3">
      <div>
        <div class="text-h4"> 编辑个人资料 {{ user.userName && `- ${user.userName}` }}</div>
        <!-- <breadcrumb :root="'apps'" /> -->
      </div>
      <v-spacer></v-spacer>
      <v-btn variant="plain" icon @click="getData">
        <v-icon>mdi-refresh</v-icon>
      </v-btn>
    </div>

    <div v-if="user.userRole === 'admin'" class="d-flex align-center font-weight-bold text-primary my-2">
      <v-icon size="x-small" color="primary">mdi-security</v-icon>
      <span class="ma-1">Administrator</span>
    </div>

    <div class="mb-4">
      <div class="d-flex">
        <span class="font-weight-bold">修改昵称</span>
        <v-text-field v-model="user.nickName" clearable></v-text-field>
      </div>
      <div class="d-flex">
        <!-- <span class="font-weight-bold">ID</span>
        <span class="mx-1">
          <copy-label :text="user.id + ''" />
        </span> -->
      </div>
    </div>


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
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { fetchUserInfo } from '@/service/api/auth'
import { useRouter } from 'vue-router'
import { useLoadingProgressLine } from "@/components/provider";

let user = ref<ApiAuth.UserInfo | null>(null)

const tab = ref()
const { currentRoute } = useRouter()
const userName = currentRoute.value.params['id'] as string

const { show, hide } = useLoadingProgressLine()

async function getData() {
  show()
  const { data } = await fetchUserInfo()
  hide()
  if (data) {
    user.value = data
  }
}

getData()

</script>
