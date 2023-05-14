<template>
  <v-card>
    <v-card-title>
      <div>{{ $t('dashboard.activity') }}</div>
      <v-spacer></v-spacer>
      <v-menu offset-y left transition="slide-y-transition">
        <template v-slot:activator="{ props }">
          <v-btn icon v-bind="props" flat>
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>
        <v-list density="comfortable">
          <v-list-item
            v-for="(item,index) in menu"
            :key="index"
            :to="item.link"
            :disabled="item.disabled"
            link
            :prepend-icon="item.icon"
            :title="item.text"
          >
          </v-list-item>
        </v-list>
      </v-menu>
    </v-card-title>

    <v-card-text>
      <v-timeline class="pa-0" density="comfortable" align="start" truncate-line="start">
        <v-timeline-item v-for="(item,index) in activity" :key="index" :dot-color="item.color" size="small">
          <strong>{{ item.what }}</strong>
          <div class="text-caption">
            <div>{{ item.where }}</div>
            <div class="text-grey">{{ item.when }}</div>
          </div>
        </v-timeline-item>
      </v-timeline>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import {reactive} from "vue";

interface _menu {
  icon: string,
  text: string,
  disabled?: boolean,
  link?: string
}

const menu = reactive<Array<_menu>>([
  {icon: 'mdi-refresh', text: 'Refresh'},
  {icon: 'mdi-delete-outline', text: 'Clear'}
])
const activity = reactive([
  {
    what: 'New Emoji',
    where: 'Chat App',
    when: '4pm',
    color: 'primary'
  }, {
    what: 'Design Stand Up',
    where: 'Chat App',
    when: '2pm',
    color: 'purple'
  }, {
    what: 'Lunch Break',
    where: '',
    when: '11am',
    color: 'primary'
  }, {
    what: 'Answer Emails',
    where: 'Work work',
    when: '9pm',
    color: 'teal lighten-3'
  }
])

</script>
