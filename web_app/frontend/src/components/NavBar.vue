<template>
  <nav>
        <router-link to="/"> Home </router-link>
        <router-link to="/gallery"> Galery </router-link>
        <router-link to="/select"> Select </router-link>
        <router-link to="/post"> Post </router-link>
  </nav>
</template>

<script lang="ts" setup>
</script>


<style scoped>
nav {
    width: 100vw;
    background-color: var(--dark);
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    height: 40px;
    font-size: 1.5em;
}

nav > a {
    display: flex;
    box-sizing: border-box;
    text-decoration: none;
    align-items: center;
    justify-content: center;
    color: var(--fg);
    height: 100%;
    flex-basis: calc(100% / 4);
    cursor: pointer;
    transition: 0.3s;
}

nav > a:hover {
    background-color: var(--normal);
    border-bottom: 3px solid var(--accent-light);
    text-align: center;
}
.router-link-exact-active {
    border-bottom: 3px solid var(--accent-light);
}
</style>
