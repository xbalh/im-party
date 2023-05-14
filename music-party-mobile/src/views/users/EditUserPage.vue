<template>
  <div class="flex-grow-1" v-if="user">
    <div class="d-flex align-center py-3">
      <div>
        <div class="text-h4">Edit User {{ user.name && `- ${user.name}` }}</div>
        <breadcrumb :root="'apps'"/>
      </div>
      <v-spacer></v-spacer>
      <v-btn variant="plain" icon @click="getData">
        <v-icon>mdi-refresh</v-icon>
      </v-btn>
    </div>

    <div
      v-if="user.role === 'admin'"
      class="d-flex align-center font-weight-bold text-primary my-2"
    >
      <v-icon size="x-small" color="primary">mdi-security</v-icon>
      <span class="ma-1">Administrator</span>
    </div>

    <div class="mb-4">
      <div class="d-flex">
        <span class="font-weight-bold">Email</span>
        <span class="mx-1">
          <copy-label :text="user.email"/>
        </span>
      </div>
      <div class="d-flex">
        <span class="font-weight-bold">ID</span>
        <span class="mx-1">
          <copy-label :text="user.id + ''"/>
        </span>
      </div>
    </div>


    <v-tabs v-model="tab" :show-arrows="false" background-color="transparent" color="primary">
      <v-tab value="tabs-account">Account</v-tab>
      <v-tab value="tabs-information">Information</v-tab>
    </v-tabs>
    <v-window v-model="tab">
      <v-window-item value="tabs-account">
        <account-tab :user="user"></account-tab>
      </v-window-item>
      <v-window-item value="tabs-information">
        <information-tab :user="user"></information-tab>
      </v-window-item>
    </v-window>
  </div>
</template>

<script setup lang="ts">
import AccountTab from './EditUser/AccountTab.vue'
import InformationTab from './EditUser/InformationTab.vue'
import {ref} from "vue";
import {fetchUser} from '@/service'
import {useRouter} from 'vue-router'
import {useLoadingProgressLine} from "@/components/provider";

let user = ref<UserManagement.User | null>(null)

const tab = ref()
const {currentRoute} = useRouter()
const userId = currentRoute.value.params['id'] as string

const {show, hide} = useLoadingProgressLine()

async function getData() {
  show()
  const {data} = await fetchUser(userId)
  hide()
  if (data) {
    user.value = data
  }
}

getData()

</script>
