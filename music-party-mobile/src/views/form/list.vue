<template>
  <div class="d-flex flex-column flex-grow-1">
    <div class="d-flex align-center py-3">
      <div>
        <div class="text-h4">Forms</div>
        <breadcrumb/>
      </div>
      <v-spacer></v-spacer>
      <v-btn class="text-capitalize" color="primary">
        Create Form
      </v-btn>
    </div>

    <v-card>
      <!-- items list -->
      <v-row dense class="pa-2 align-center">
        <v-col cols="6">
          <v-menu offset-y left>
            <template v-slot:activator="{ props}">
              <v-slide-x-reverse-transition mode="out-in">
                <v-btn v-show="selected.length > 0" v-bind="props">
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
        v-model="selected"
        show-select
        :headers="headers"
        :items="items"
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

        <template v-slot:item.status="{item:{raw}}">
          {{ formStatusLabels [(raw as FormManagement.Form ).status] }}
        </template>

        <template v-slot:item.created="{ item  : {raw} }">
          <div>{{ raw.created }}</div>
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
import {fetchFormList} from "@/service";
import {formStatusLabels} from '@/constants'

const {loading, startLoading, endLoading} = useLoading(true);

const options = ref()
const total = ref(0)
const pageSize = ref(10)
const items = ref<Array<FormManagement.Form>>([])
const searchQuery = ref('')
const selected = ref<Array<FormManagement.Form>>([])
const headers: Ref<DataTableHeader> = ref<DataTableHeader>([
  {title: 'Id', align: 'start', key: 'id'},
  {title: 'Name', key: 'name'},
  {title: 'Status', key: 'status'},
  {title: 'Created', align: 'start', key: 'created'},
  {title: '', sortable: false, align: 'end', key: 'action'}
])

async function getTableData() {
  startLoading();
  const {data} = await fetchFormList();
  endLoading();
  if (data) {
    total.value = data.total
    items.value = data.list
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

<style scoped>

</style>
