<script setup lang="ts">
import { ref, Ref } from "vue";
import NamedTextInput from "@/components/NamedTextInput.vue";
import HiddenNamedTextInput from "@/components/HiddenNamedTextInput.vue";
import Button from "@/components/Button.vue";
import { Api } from "@/api";
import router from "@/router";

let user_name: Ref<string> = ref("");
let user_pwd: Ref<string> = ref("");

function onUsernameInput(name: string) {
    user_name.value = name;
}

function onUserpwdInput(pwd: string) {
    user_pwd.value = pwd;
}

function onLoginClick() {
    Api.PostUserLogin(user_name.value, user_pwd.value, () => router.push("/home"))
}

function onRegisterClick() {
    Api.PostUserRegister(user_name.value, user_pwd.value, () => router.push("/home"));
}

</script>

<template>
    <div class="content">
        <div>
            <h1 class="title"> Project Group: m2e </h1>
            <h2 class="title"> Derobert-Mazure Adrien </h2>
            <h2 class="title"> Galland David </h2>
            <h2 class="title"> Osmani Oussama </h2>
            <p style="font-size=inherit">Built with vue+ts</p>
            <img alt = "Vue logo" src = "@/assets/logo.png" style = "width:40px; padding: 40px;"/>
        </div>
        <div class="login">
            <NamedTextInput name="UserName" style="margin: .5em;" @change="onUsernameInput($event)"/> 
            <HiddenNamedTextInput name="Password" style="margin: .5em;" @change="onUserpwdInput($event)"/> 
            <div class="loginbtncont">
                <Button label="Login" @click="onLoginClick()"/>
                <Button label="Register" @click="onRegisterClick()"/>
            </div>
        </div>
    </div>
   
</template>

<style scoped>
.content {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
}
.login {
    display: flex;
    flex-direction: column;
    justify-content: end;
    height: 100%;
}
.loginbtncont {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
}
</style>
