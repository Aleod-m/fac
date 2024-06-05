<template>
    <NavBar/>
    <div class="content">
        <h1>Logged in as {{userLoggedIn()}}.</h1>
        <Button label="logout" @click="onButtonLogout()"/>
    </div>
</template>

<script setup lang="ts">
import {Ref, ref} from "vue";
import NavBar from "@/components/NavBar.vue";
import NamedTextInput from "@/components/NamedTextInput.vue";
import Button from "@/components/Button.vue";
import { Api, userLoggedIn } from "@/api";
import router from "@/router";

function onButtonLogout() {
    let username = userLoggedIn();
    if(username == undefined) 
        router.push("/");
    Api.DeleteUserLogout(username!, () => router.push("/"))
}

</script>

<style scoped>
.test {
    width: 200px;
    height: 10px;
}
</style>
