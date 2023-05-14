<template>
  <div class="d-flex flex-grow-1 flex-column">
    <v-row class="flex-grow-0 my-0" dense>
      <v-col cols="12" xl="4">
        <sales-card
          :value="1837.32"
          class="h-100"
          :percentage="3.2"
          style="min-height: 380px"
          :loading="isLoading.isLoading1"
          :percentage-label="$t('dashboard.lastweek')"
          :action-label="$t('dashboard.viewReport')"
        ></sales-card>
      </v-col>
      <v-col cols="12" md="6" xl="4">
        <activity-card
          class="h-100"
        />
      </v-col>

      <v-col cols="12" md="6" xl="4">
        <sources-card
          :label="$t('dashboard.sources')"
          class="h-100"
          color="#8c9eff"
          :value="432"
          :percentage="4.3"
          style="min-height: 380px"
          :loading="isLoading.isLoading2"
          :percentage-label="$t('dashboard.lastweek')"
          :series="[44, 55, 41, 17]"
        ></sources-card>
      </v-col>
    </v-row>

    <v-row class="flex-grow-0 my-0" dense>
      <v-col cols="12" lg="6">
        <table-card class="h-100" :label="$t('dashboard.recentOrders')"/>
      </v-col>
      <v-col cols="12" lg="6">
        <div class="d-flex flex-column flex-grow-1 h-100">
          <track-card
            :label="$t('dashboard.orders')"
            class="h-100"
            color="#8c9eff"
            :value="432"
            :percentage="4.3"
            :percentage-label="$t('dashboard.lastweek')"
            :loading="isLoading.loading3"
            :series="ordersSeries"
          ></track-card>
          <track-card
            :label="$t('dashboard.customers')"
            class="h-100 mt-2"
            :color="current.colors.success"
            :value="178"
            :percentage="2.12"
            :percentage-label="$t('dashboard.lastweek')"
            :loading="isLoading.loading3"
            :series="customersSeries"
          ></track-card>
        </div>
      </v-col>
    </v-row>

    <v-row class="flex-grow-0 my-0" dense>
      <v-col cols="12" xl="6">
        <todo-card/>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts" setup>
const {current} = useTheme()

const isLoading: Record<string, boolean> = reactive({
  isLoading1: true,
  isLoading2: true,
  isLoading3: true,
  isLoading4: true,
  isLoading5: true,
})
let loadingInterval: NodeJS.Timeout
let count = 0

onMounted(() => {
  loadingInterval = setInterval(() => {
    isLoading[`isLoading${count++}`] = false
    if (count === 4) clearInterval(loadingInterval)
  }, 400)
})

onBeforeUnmount(
  () => clearInterval(loadingInterval)
)


const ordersSeries = reactive([{
  name: 'Orders',
  data: [
    ['2020-02-02', 34],
    ['2020-02-03', 43],
    ['2020-02-04', 40],
    ['2020-02-05', 43]
  ]
}])
const customersSeries = reactive([{
  name: 'Customers',
  data: [
    ['2020-02-02', 13],
    ['2020-02-03', 11],
    ['2020-02-04', 13],
    ['2020-02-05', 12]
  ]
}])

</script>

<style scoped>

</style>
