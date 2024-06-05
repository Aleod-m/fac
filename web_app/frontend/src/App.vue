<script setup lang = "ts">
</script>

<template>
    <div class="app">
        <router-view />
      </div>
</template>

<style lang="css">
.app {
   justify-content: center;
    width: 95%;
    height: 100%;
    padding-top: calc(100vw * 0.025);
}

#app {
     -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
}

.content {
    background-color: var(--dark);
    padding-bottom: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

</style>
