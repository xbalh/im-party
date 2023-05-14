<template>
  <v-card class="text-center w-full error-page pa-3">
    <v-img src="/images/illustrations/500-illustration.svg" max-height="400" contain/>
    <div class="text-h3 mt-10">OOPS! Something went wrong here</div>
    <div class="mt-3 mb-10">Our experts are working to fix the issue.</div>
    <v-text-field variant="solo" placeholder="Search website"></v-text-field>
    <v-btn to="/" block size="large" color="primary">Send me back</v-btn>
  </v-card>
</template>

<style>
.error-page {
  max-width: 500px;
}
</style>
