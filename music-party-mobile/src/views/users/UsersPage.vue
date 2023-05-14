<template>
  <div class="d-flex flex-column flex-grow-1">
    <div class="d-flex align-center py-3">
      <div>
        <div class="text-h4">Users</div>
        <breadcrumb :root="'apps'"/>
      </div>
      <v-spacer></v-spacer>
      <v-btn class="text-capitalize" color="primary">
        Create User
      </v-btn>
    </div>

    <v-card>
      <!-- user list -->
      <v-row dense class="pa-2 align-center">
        <v-col cols="6">
          <v-menu offset-y left>
            <template v-slot:activator="{ props}">
              <v-slide-x-reverse-transition mode="out-in">
                <v-btn v-show="selectedUsers.length > 0" v-bind="props">
                  Actions
                  <v-icon right>mdi-menu-down</v-icon>
                </v-btn>
              </v-slide-x-reverse-transition>
            </template>
            <v-list dense>
              <v-list-item>
                <v-list-item-title>Verify</v-list-item-title>
              </v-list-item>
              <v-list-item>
                <v-list-item-title>Disable</v-list-item-title>
              </v-list-item>
              <v-divider></v-divider>
              <v-list-item>
                <v-list-item-title>Delete</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>

        </v-col>
        <v-col cols="6" class="d-flex text-right align-center">
          <v-text-field
            v-model="searchQuery"
            append-inner-icon="mdi-magnify"
            class="flex-grow-1 mr-md-2"
            variant="solo"
            density="comfortable"
            hide-details
            clearable
            placeholder="e.g. filter for id, email, name, etc"
          ></v-text-field>
          <v-btn
            :loading="loading"
            @click="getTableData()"
            icon
            flat
            small
            class="ml-2"
          >
            <v-icon>mdi-refresh</v-icon>
          </v-btn>
        </v-col>
      </v-row>

      <v-data-table-server
        v-model="selectedUsers"
        show-select
        :headers="headers"
        :items="users"
        :search="searchQuery"
        :loading="loading"
        :items-length="total"
        :items-per-page="pageSize"
        @update:options="options = $event"
        class="flex-grow-1"
      >
        <template v-slot:item.id="{ item  : {raw} }">
          <div class="font-weight-bold">#
            <copy-label :text="raw.id + ''"/>
          </div>
        </template>

        <template v-slot:item.email="{ item:{raw} }">
          <div class="d-flex align-center py-1">
            <v-avatar size="32" class="elevation-1 grey lighten-3">
              <svg-icon :name="raw.avatar"></svg-icon>
            </v-avatar>
            <div class="ml-1 text-caption font-weight-bold">
              <copy-label :text="raw.email"/>
            </div>
          </div>
        </template>

        <template v-slot:item.verified="{item:{raw}}">
          <v-icon v-if="raw.verified" small color="success">
            mdi-check-circle
          </v-icon>
          <v-icon v-else small>
            mdi-circle-outline
          </v-icon>
        </template>

        <template v-slot:item.userStatus="{item:{raw}}">
          {{ userStatusLabels [(raw as UserManagement.User).userStatus] }}
        </template>

        <template v-slot:item.role="{ item  : {raw} }">
          <v-chip
            label
            size="small"
            class="font-weight-bold"
            :color="raw.role === 'admin' ? 'primary' : undefined"
          >{{ raw.role }}
          </v-chip>
        </template>

        <template v-slot:item.created="{ item  : {raw} }">
          <div>{{ raw.created }}</div>
        </template>

        <template v-slot:item.lastSignIn="{ item  : {raw} }">
          <div>{{ raw.lastSignIn }}</div>
        </template>

        <template v-slot:item.action="{item:{raw} }">
          <div class="actions">
            <v-btn flat icon :to="`/apps/manager-user/edit/${raw.id}`">
              <v-icon>mdi-open-in-new</v-icon>
            </v-btn>
          </div>
        </template>
      </v-data-table-server>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import {Ref, ref} from "vue";
import {useLoading} from '@/hooks';
import {fetchUserList} from "@/service";
import {userStatusLabels} from '@/constants'

const {loading, startLoading, endLoading} = useLoading(true);

const options = ref()
const total = ref(0)
const pageSize = ref(10)
const users = ref<Array<UserManagement.User>>([])
const searchQuery = ref('')
const selectedUsers = ref<Array<UserManagement.User>>([])
const headers: Ref<DataTableHeader> = ref<DataTableHeader>([
  {title: 'Id', align: 'start', key: 'id'},
  {title: 'Email', key: 'email'},
  {title: 'Verified', key: 'verified'},
  {title: 'Name', align: 'start', key: 'name'},
  {title: 'Role', key: 'role'},
  {title: 'Created', key: 'created'},
  {title: 'Last SignIn', key: 'lastSignIn'},
  {title: 'Status', key: 'userStatus'},
  {title: '', sortable: false, align: 'end', key: 'action'}
])


async function getTableData() {
  startLoading();
  const {data} = await fetchUserList();
  endLoading();
  if (data) {
    total.value = data.total
    users.value = data.list
  }
}

async function init() {
  await getTableData()
  watch(options, (n, o) => {
    getTableData()
  }, {deep: true})
}

init()

</script>

