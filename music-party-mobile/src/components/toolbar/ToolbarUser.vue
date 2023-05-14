<template>
  <v-menu offset-y left transition="slide-y-transition">
    <template v-slot:activator="{ props }">
      <v-btn icon size="small" class="elevation-2" v-bind="props">

        <v-badge
          color="success"
          dot
          bordered
        >
          <v-avatar size="40" :class="{'bg-grey':!userInfo.userAvatar}">
            <svg-icon v-if="!!userInfo.userAvatar" :name="userInfo.userAvatar"></svg-icon>
          </v-avatar>
        </v-badge>
      </v-btn>
    </template>

    <!-- user menu list -->
    <v-list dense nav>
      <v-list-item v-if="!isLogin">
        <v-list-item-title>{{ $t('usermenu.notSignin') }}</v-list-item-title>
      </v-list-item>
      <v-list-item
        v-else
        v-for="(item, index) in menu"
        :key="index"
        :to="item.link"
        link
      >
        <template v-slot:prepend>
          <v-icon size="small" :icon="item.icon"></v-icon>
        </template>
        <v-list-item-title>{{ $t(item.key) }}</v-list-item-title>
      </v-list-item>

      <v-divider class="my-1"></v-divider>

      <v-list-item @click="logout" prepend-icon="mdi-logout-variant" :title="$t('menu.logout')">
      </v-list-item>
    </v-list>


  </v-menu>
</template>

<script setup lang="ts">
import {reactive} from "vue";

const menu = reactive(
  [
    {icon: 'mdi-account-box-outline', key: 'menu.profile', link: '/apps/manager-user/edit'},
    {icon: 'mdi-format-list-checkbox', key: 'menu.todo', link: '/apps/todo'},
    {icon: 'mdi-email-outline', key: 'menu.board', link: '/apps/board'},
    {icon: 'mdi-forum-outline', key: 'menu.chat', link: '/apps/chat-channel/'}
  ]
)

const auth = useAuthStore();
const {isLogin, userInfo} = storeToRefs(auth)
const logout = () => {
  window.$dialog?.show({
    title: 'Logo out',
    main: 'Are you sure logo out?',
    confirm: () => {
      window.$loadingOverly?.show()
      setTimeout(() => {
        auth.resetAuthStore()
      }, 1000)
    }

  })
}
</script>
