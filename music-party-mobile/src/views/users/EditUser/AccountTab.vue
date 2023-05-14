<template>
  <div class="my-2">
    <div>
      <v-card v-if="user.userStatus=='1'" class="bg-warning mb-4" light>
        <v-card-title>User Disabled</v-card-title>
        <v-card-subtitle>This user has been disabled! Login accesss has been revoked.</v-card-subtitle>
        <v-card-text>
          <v-btn theme="dark" @click="user.userStatus = '1'">
            <v-icon left size="small">mdi-account-check</v-icon>
            Enable User
          </v-btn>
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>Basic Information</v-card-title>
        <v-card-text>
          <div class="d-flex flex-column flex-sm-row">
            <div>
              <v-avatar size="95" class="d-flex bg-blue-grey-lighten-4 rounded elevation-3 ma-0">
                <svg-icon :name="user.avatar"></svg-icon>
              </v-avatar>
              <v-btn class="mt-1" size="small">Edit Avatar</v-btn>
            </div>
            <div class="flex-grow-1 pt-2 pa-sm-2">
              <v-text-field v-model="user.name" label="Display name"
                            placeholder="name"></v-text-field>
              <v-text-field v-model="user.email" label="Email" hide-details></v-text-field>

              <div class="d-flex flex-column">
                <v-checkbox v-model="user.verified" density="comfortable" label="Email Verified"></v-checkbox>
                <div>
                  <v-btn
                    v-if="!user.verified"
                  >
                    <v-icon left small>mdi-email</v-icon>
                    Send Verification Email
                  </v-btn>
                </div>
              </div>

              <div class="mt-2">
                <v-btn color="primary">Save</v-btn>
              </div>
            </div>
          </div>
        </v-card-text>
      </v-card>

      <v-expansion-panels v-model="panel" multiple class="user-panels mt-3">
        <v-expansion-panel value="actions">
          <v-expansion-panel-title class="title">Actions</v-expansion-panel-title>
          <v-expansion-panel-text>
            <div class="mb-2">
              <div class="title">Reset User Password</div>
              <div class="subtitle mb-2">Sends a reset password email to the user.</div>
              <v-btn
                class="mb-2"
              >
                <v-icon left small>mdi-email</v-icon>
                Send Reset Password Email
              </v-btn>
            </div>

            <v-divider></v-divider>

            <div class="my-2">
              <div class="title">Export Account Data</div>
              <div class="subtitle mb-2">Export all the platform metadata for this user.</div>
              <v-btn class="mb-2">
                <v-icon left small>mdi-clipboard-account</v-icon>
                Export User Data
              </v-btn>
            </div>

            <v-divider></v-divider>

            <div class="my-2">
              <div class="error--text title">Danger Zone</div>
              <div class="subtitle mb-2">Full administrator with access to this dashboard.</div>

              <div class="my-2">
                <v-btn
                  v-if="user.role === 'admin'"
                  color="primary"
                  @click="user.role = 'user'"
                >
                  <v-icon left small>mdi-security</v-icon>
                  Remove admin access
                </v-btn>
                <v-btn v-else color="primary" @click="user.role = 'admin'">
                  <v-icon left small>mdi-security</v-icon>
                  Set User as Admin
                </v-btn>
              </div>

              <v-divider></v-divider>

              <div class="subtitle mt-3 mb-2">Prevent the user from signing in on the platform.</div>
              <div class="my-2">
                <v-btn
                  v-if="user.userStatus === '2'"
                  color="warning"
                >
                  <v-icon left small>mdi-account-check</v-icon>
                  Enable User
                </v-btn>
                <v-btn
                  v-else
                  color="warning"
                  @click="showDisableDialog(user)"
                >
                  <v-icon left small>mdi-cancel</v-icon>
                  Disable User
                </v-btn>
              </div>

              <v-divider></v-divider>
              <div
                class="subtitle mt-3 mb-2"
              >To delete the user please transfer ownership or delete user's subscriptions.
              </div>
              <v-btn color="error" @click="showDeleteDialog">
                <v-icon left small>mdi-delete</v-icon>
                Delete User
              </v-btn>
            </div>
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel value="metadata">
          <v-expansion-panel-title class="title">Metadata</v-expansion-panel-title>
          <v-expansion-panel-text class="body-2">
            <span class="font-weight-bold">Created</span>
            {{ user.created }}
            <br/>
            <span class="font-weight-bold">Last Sign In</span>
            {{ user.lastSignIn }}
          </v-expansion-panel-text>
        </v-expansion-panel>
        <v-expansion-panel value="rawdata">
          <v-expansion-panel-title class="title">Raw Data</v-expansion-panel-title>
          <v-expansion-panel-text>
            <pre class="body-2">{{ user }}</pre>
          </v-expansion-panel-text>
        </v-expansion-panel>
      </v-expansion-panels>
    </div>
  </div>
</template>

<script setup lang="ts">
import {PropType} from "vue";

defineProps({
  user: {
    type: Object as PropType<UserManagement.User>,
    default: () => ({})
  }
})
const panel = ref(['metadata'])

const showDeleteDialog = () => {
  const deleteDialog = window.$dialog?.show({
    title: 'Delete User',
    main: 'Are you sure you want to delete this user?',
    confirm: () => {
      deleteDialog?.confirmLoading(true)
      setTimeout(() => {
        deleteDialog?.confirmLoading(false)
        window.$snackBar?.success('Action Completed!')
        deleteDialog?.close()
      }, 1000)
    }
  })
}
const showDisableDialog = (user: UserManagement.User) => {
  const disableDialog = window.$dialog?.show({
    title: 'Disable User',
    main: 'Are you sure you want to disable this user?',
    confirmColor: 'warning',
    confirm: () => {
      disableDialog?.confirmLoading(true)
      setTimeout(() => {
        disableDialog?.confirmLoading(false)

        user.userStatus = '2'

        window.$snackBar?.success('Action Completed!')
        disableDialog?.close()
      }, 1000)
    }
  })
}


</script>
<style lang="scss">
.user-panels .v-expansion-panel-title__overlay {
  opacity: 0 !important;
}
</style>
