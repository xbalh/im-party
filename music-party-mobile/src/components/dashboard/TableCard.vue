<template>
  <v-card>
    <v-card-title>{{ label }}</v-card-title>
    <v-data-table
      :headers="headers"
      :items="items"
      hide-default-footer
    >
      <!--      <template v-slot:bottom>-->
      <!--      </template>-->
      <template v-slot:item.id="{ item:{raw} }">
        <div class="font-weight-bold">#
          <copy-label :text="raw.id"/>
        </div>
      </template>

      <template v-slot:item.user="{ item:{raw}}">
        <div class="d-flex align-center py-1">
          <v-avatar size="40" class="elevation-1 grey lighten-3">
            <svg-icon :name="raw.user.avatar"></svg-icon>
          </v-avatar>
          <div class="ml-1">
            <div class="font-weight-bold">{{ raw.user.name }}</div>
            <div class="text-caption">
              <copy-label :text="raw.user.email"/>
            </div>
          </div>
        </div>
      </template>

      <template v-slot:item.date="{ item:{raw} }">
        <div>{{ $filters.formatCurrency(raw.date) }}</div>
      </template>

      <template v-slot:item.company="{ item:{raw} }">
        <copy-label :text="raw.company"/>
      </template>

      <template v-slot:item.amount="{ item:{raw} }">
        {{ $filters.formatCurrency(raw.amount) }}
      </template>

      <template v-slot:item.status="{ item:{raw} }">
        <div class="font-weight-bold d-flex align-center text-no-wrap">
          <div v-if="raw.status === 'PENDING'" class="warning--text">
            <v-icon small color="warning">mdi-circle-medium</v-icon>
            <span>Pending</span>
          </div>
          <div v-if="raw.status === 'PAID'" class="success--text">
            <v-icon small color="success">mdi-circle-medium</v-icon>
            <span>Paid</span>
          </div>
        </div>
      </template>

      <template v-slot:item.action>
        <div class="actions">
          <v-btn flat icon >
            <v-icon>mdi-open-in-new</v-icon>
          </v-btn>
        </div>
      </template>
    </v-data-table>
  </v-card>
</template>

<script setup lang="ts">
import {ref} from 'vue'
defineProps
({
  label: {
    type: String,
    default: ''
  }
})
const headers = ref<DataTableHeader>([
  {title: 'Order Id', align: 'start', key: 'id'},
  {
    title: 'User',
    sortable: false,
    key: 'user'
  },
  {title: 'Date', key: 'date'},
  {title: 'Company', key: 'company'},
  {title: 'Amount', key: 'amount'},
  {title: 'Status', key: 'status'},
  {title: '', sortable: false, align: 'end', key: 'action'}
])



const items = ref([
  {
    id: '2837',
    user: {
      name: 'John Simon',
      email: 'johnsimon@blobhill.com',
      avatar: 'avatar1',
    },
    date: '2020-05-10',
    company: 'BlobHill',
    amount: 52877,
    status: 'PAID'
  },
  {
    id: '2838',
    user: {
      name: 'Greg Cool J',
      email: 'cool@caprimooner.com',
      avatar: 'avatar2',
    },
    date: '2020-05-11',
    company: 'Caprimooner',
    amount: 2123,
    status: 'PENDING'
  },
  {
    id: '2839',
    user: {
      name: 'Samantha Bush',
      email: 'bush@catloveisstilllove.com',
      avatar: 'avatar3',
    },
    date: '2020-05-11',
    company: 'CatLovers',
    amount: 12313,
    status: 'PENDING'
  },
  {
    id: '2840',
    user: {
      name: 'Ben Howard',
      email: 'ben@indiecoolers.com',
      avatar: 'avatar4',
    },
    date: '2020-05-12',
    company: 'IndieCoolers',
    amount: 9873,
    status: 'PAID'
  },
  {
    id: '2841',
    user: {
      name: 'Jack Candy',
      email: 'jack@candylooove.com',
      avatar: 'avatar5',
    },
    date: '2020-05-13',
    company: 'CandyLooove',
    amount: 29573,
    status: 'PAID'
  }
])
</script>
