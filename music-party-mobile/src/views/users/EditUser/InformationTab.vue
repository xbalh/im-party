<template>
  <v-card class="my-2">
    <v-card-title>User Information</v-card-title>
    <v-card-text>
      <v-form>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field v-model="user.address.detail" label="Address Line 1"></v-text-field>
            <v-text-field v-model="user.address.zipCode" label="Zip Code"></v-text-field>
            <v-text-field v-model="user.address.city" label="City"></v-text-field>
            <v-text-field v-model="user.address.state" label="State"></v-text-field>
            <v-text-field v-model="user.address.country" label="Country"></v-text-field>
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field v-model="user.phone" label="Phone"></v-text-field>
            <v-text-field
              v-model="user.birthDay"
              label="Birthday date"
              type="date"
            ></v-text-field>
            <v-radio-group v-model="user.gender" label="Gender">
              <v-radio v-for="g in genderOptions" :label="g.label" :value="g.value"></v-radio>
            </v-radio-group>
          </v-col>
        </v-row>

        <div class="d-flex">
          <v-btn>Reset</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="primary">Save</v-btn>
        </div>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import {PropType} from "vue";
import {genderOptions} from '@/constants';

const props = defineProps({
  user: {
    type: Object as PropType<ApiUserManagement.User>,
    default: () => ({address: {}})
  }
})

onBeforeMount(() => {
  if (!props.user?.address) {
    props.user.address = {}
  }
})

</script>
