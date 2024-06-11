<template>
  <v-app data-app id="app">
    <div class="background-image">
      <router-view />
    </div>
  </v-app>
</template>


<script>

</script>
<style>
.background-image {
  display: flex;
  background-image: url('assets/lonely.jpg');
  background-size: cover;
  height: 100vh;
  width: 100%;
}
</style>